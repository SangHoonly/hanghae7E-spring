package simple_blog.LeeJerry.controller;


import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
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

    @PostMapping
    public void insertBoard(@RequestBody BoardReq boardReq, Authentication authentication) {
        boardService.insertBoard(boardReq, authentication);
    }

    @PutMapping("/{boardId}")
    public void updateBoard(
        @RequestBody BoardReq boardReq, Authentication authentication, @PathVariable Long boardId) {
        boardReq.setId(boardId);
        boardService.updateBoard(boardReq, authentication);
    }


    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId, Authentication authentication) {
        boardService.deleteBoard(boardId, authentication);
    }
}
