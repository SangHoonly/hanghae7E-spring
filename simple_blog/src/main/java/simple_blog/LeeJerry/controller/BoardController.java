package simple_blog.LeeJerry.controller;


import java.io.IOException;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.service.BoardService;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    BoardService boardService;


    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<BoardRes> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/{boardId}")
    public BoardRes getBoard(@PathVariable Long boardId) {
        return boardService.findBoard(boardId);
    }

    @PostMapping
    public void insertBoard(BoardReq boardReq, @AuthenticationPrincipal UserProxy userProxy) throws IOException {
        boardReq.setUserEntity(userProxy.getUser());

        boardService.insertBoard(boardReq);
    }

    @PutMapping("/{boardId}")
    public void updateBoard(@RequestBody BoardReq boardReq, @AuthenticationPrincipal UserProxy userProxy, @PathVariable Long boardId)
        throws AbstractException, IOException {
        boardReq.setId(boardId);
        boardService.updateBoard(boardReq, userProxy.getId());
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserProxy userProxy) {
        boardService.deleteBoard(boardId, userProxy.getId());
    }
}
