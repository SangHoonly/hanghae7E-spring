package simple_blog.LeeJerry.service;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.entity.BoardEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.NotFoundException;
import simple_blog.LeeJerry.exception.UnAuthorizedException;
import simple_blog.LeeJerry.repository.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${url.server}")
    private String SERVER_URL;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    public List<BoardRes> findBoards() {
        return boardRepository.findAll(Sort.by(Direction.DESC, "favoriteCount"))
            .stream().map(BoardRes::toRes).collect(toList());
    }

    @Transactional
    public BoardRes findBoard(Long boardId) throws NotFoundException {
        
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        boardEntity.viewed();

        return BoardRes.toRes(boardEntity);
    }

    BoardEntity findBoardEntity(Long boardId) throws NotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void insertBoard(BoardReq boardReq) throws IOException {
        BoardEntity boardEntity = boardRepository.save(boardReq.toEntity());
        sendMultipartImageToS3(boardReq.getFiles(), boardEntity.getId());

        String imageUrl = amazonS3Client.getUrl(BUCKET_NAME, toS3Key(boardEntity.getId())).toString();

        boardEntity.setImageUrl(imageUrl);
    }

    @Transactional
    public void updateBoard(BoardReq boardReq, Long currentUserId) throws AbstractException, IOException {
        BoardEntity boardEntity = findBoardEntity(boardReq.getId());

        if (boardEntity.getUserEntity().getId() != currentUserId) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);

        boardEntity.updateTitleAndBody(boardReq);

        if (boardReq.getFiles() != null) {
            amazonS3Client.deleteObject(BUCKET_NAME, boardEntity.getId().toString());

            sendMultipartImageToS3(boardReq.getFiles(), boardEntity.getId());
            String imageUrl = amazonS3Client.getUrl(BUCKET_NAME, toS3Key(boardEntity.getId())).toString();
            boardEntity.setImageUrl(imageUrl);
        }

        boardRepository.save(boardEntity);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long currentUserId) throws AbstractException {
        BoardEntity boardEntity = findBoardEntity(boardId);
        amazonS3Client.deleteObject(BUCKET_NAME, toS3Key(boardId));

        if (boardEntity.getUserEntity().getId() != currentUserId) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);

        boardRepository.deleteById(boardId);
    }

    private void sendMultipartImageToS3(MultipartFile multipartFile, Long boardId) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType("image/jpeg");

        amazonS3Client.putObject(new PutObjectRequest(BUCKET_NAME, toS3Key(boardId),  multipartFile.getInputStream(), objectMetadata));
    }

    public String toS3Key(Long boardId) {
        return "images/" + boardId.toString() + ".jpg";
    }
}
