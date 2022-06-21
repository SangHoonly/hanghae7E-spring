package simple_blog.LeeJerry.controller;


import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import simple_blog.LeeJerry.custom.WithCustomUser;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.service.BoardService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BoardControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    BoardService boardService;


    @BeforeEach
    public void initMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    public String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test
    @DisplayName("GET /api/board [로그인 & 로그인X]: 모든 게시글을 반환합니다.")
    public void findBoardsTest() throws Exception {
        //given
        BoardRes board1 = BoardRes.builder().build();
        BoardRes board2 = BoardRes.builder().build();

        List <BoardRes> boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);

        given(boardService.findBoards()).willReturn(boards);

        //when & then
        mvc.perform(get("/api/board"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(2)));

    }

    @Test
    @DisplayName("GET /api/board/{boardId} [로그인]: 해당 id의 게시글을 반환합니다.")
    @WithMockUser
    public void findSingleBoardWithAuthTest() throws Exception {
        //given
        BoardRes board = BoardRes.builder().title("게시글 1").build();
        given(boardService.findBoard(1L)).willReturn(board);

        //when & then
        mvc.perform(get("/api/board/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("게시글 1")));
    }

    @Test
    @DisplayName("GET /api/board/{boardId} [로그인X]: 401 에러를 반환합니다.")
    public void findSingleBoardWithNoAuthTest() throws Exception {
        //given
        BoardRes board = BoardRes.builder().title("게시글 1").build();
        given(boardService.findBoard(1L)).willReturn(board);

        //when & then
        mvc.perform(get("/api/board/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/board/{boardId} [로그인X]: 401 에러를 반환합니다.")
    public void addSingleBoardWithAuthTest() throws Exception {
        //given
        BoardRes board = BoardRes.builder().title("게시글 1").build();
        given(boardService.findBoard(1L)).willReturn(board);

        //when & then
        mvc.perform(get("/api/board/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/board [로그인]: status 200을 반환합니다.")
    @WithCustomUser
    public void insertBoardWithAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        //when & then
        mvc.perform(post("/api/board")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/board [로그인X]: 401 에러를 반환합니다.")
    public void insertBoardWithNoAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        mvc.perform(post("/api/board")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("PUT /api/board/{boardId} [로그인]: status 200을 반환합니다.")
    @WithCustomUser
    public void updateBoardWithAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        //when & then
        mvc.perform(put("/api/board/1")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/board/{boardId} [로그인X]: 401 에러를 반환합니다.")
    public void updateBoardWithNoAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        mvc.perform(put("/api/board/1")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("DELETE /api/board/{boardId} [로그인]: status 200을 반환합니다.")
    @WithCustomUser
    public void deleteBoardWithAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        //when & then
        mvc.perform(delete("/api/board/1")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/board/{boardId} [로그인X]: 401 에러를 반환합니다.")
    public void deleteBoardWithNoAuthTest() throws Exception {
        BoardReq boardReq = BoardReq.builder().build();

        mvc.perform(delete("/api/board/1")
                .content(toJsonString(boardReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}