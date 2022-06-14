package simple_blog.LeeJerry.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.NotFoundException;
import simple_blog.LeeJerry.exception.UnAuthorizedException;
import simple_blog.LeeJerry.repository.BoardRepository;

@Service
public class BoardService {

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    BoardRepository boardRepository;

    public List<BoardRes> getBoards() {
        return boardRepository.findAll().stream().map(BoardRes::toRes).collect(toList());
    }

    public BoardRes findBoard(Long boardId) throws NotFoundException {
        return BoardRes.toRes(
            boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND)));
    }

    @Transactional
    Board findBoardEntity(Long boardId) throws NotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void insertBoard(BoardReq boardReq) {

        boardRepository.save(boardReq.toEntity());
    }

    @Transactional
    public void updateBoard(BoardReq boardReq, Authentication authentication) throws AbstractException {
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();
        Board board = findBoardEntity(boardReq.getId());

        if (board.getUserEntity().getId() != userProxy.getId()) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);


        board.update(boardReq.getBody(), boardReq.getTitle());

        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Authentication authentication) throws AbstractException {
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();
        Board board = findBoardEntity(boardId);

        if (board.getUserEntity().getId() != userProxy.getId()) throw new UnAuthorizedException(ErrorCode.NOT_AUTHOR);

        boardRepository.deleteById(boardId);
    }
}
