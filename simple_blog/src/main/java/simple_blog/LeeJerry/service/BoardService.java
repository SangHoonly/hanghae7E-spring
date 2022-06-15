package simple_blog.LeeJerry.service;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import simple_blog.LeeJerry.converter.DuckHeeBoardReq;
import simple_blog.LeeJerry.dto.BoardReqTemplate;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.entity.ImageEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.NotFoundException;
import simple_blog.LeeJerry.exception.UnAuthorizedException;
import simple_blog.LeeJerry.repository.BoardRepository;
import simple_blog.LeeJerry.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class BoardService {

    final BoardRepository boardRepository;
    final ImageRepository imageRepository;

    @Value("${url.server}")
    private String SERVER_URL;

    public List<BoardRes> getBoards() {
        return boardRepository.findAll().stream().map(BoardRes::toRes).collect(toList());
    }

    @Transactional
    public BoardRes findBoard(Long boardId) throws NotFoundException {
        ImageEntity imageEntity = imageRepository.findByBoardId(boardId).orElse(null);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        board.viewed();

        if (imageEntity == null) return BoardRes.toRes(board);

        String imageUrl = SERVER_URL + "/api/images/" + imageEntity.toString();

        return BoardRes.toRes(board, imageUrl);
    }

    @Transactional
    Board findBoardEntity(Long boardId) throws NotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void insertBoard(BoardReqTemplate boardReqTemplateReq) throws IOException {
        if (!(boardReqTemplateReq instanceof DuckHeeBoardReq)) {
            boardRepository.save(boardReqTemplateReq.toEntity());
            return;
        }

        DuckHeeBoardReq boardReq = (DuckHeeBoardReq) boardReqTemplateReq;
        ImageEntity image = ImageEntity.builder().name(boardReq.getFiles().getName()).body(boardReq.getFiles().getBytes()).build();

        imageRepository.save(image);
        boardReq.setImageEntity(image);

        boardRepository.save(boardReq.toEntity());
    }

    @Transactional
    public void updateBoard(BoardReqTemplate boardReq, Long currentUserId) throws AbstractException {
        Board board = findBoardEntity(boardReq.getId());

        if (board.getUserEntity().getId() != currentUserId) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);

        board.update(boardReq.getBody(), boardReq.getTitle());

        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long currentUserId) throws AbstractException {
        Board board = findBoardEntity(boardId);

        if (board.getUserEntity().getId() != currentUserId) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);

        boardRepository.deleteById(boardId);
    }
}
