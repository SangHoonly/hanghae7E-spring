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

    @Transactional
    public void insertBoard(BoardReq boardReq, Authentication authentication) {
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();

        boardRepository.save(boardReq.toEntity(userProxy.getUser()));
    }

    @Transactional
    public void updateBoard(BoardReq boardReq, Authentication authentication) {
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();

        Board board = boardRepository.findById(boardReq.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글이 존재하지 않습니다."));

        if (board.getUserEntity().getId() != userProxy.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 작성자가 아닙니다.");


        board.update(boardReq.getBody(), boardReq.getTitle());

        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Authentication authentication) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 보드가 존재하지 않습니다."));
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();

        if (board.getUserEntity().getId() != userProxy.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글의 작성자가 아닙니다.");

        boardRepository.deleteById(boardId);
    }
}
