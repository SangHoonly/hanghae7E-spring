package simple_blog.LeeJerry.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import simple_blog.LeeJerry.service.FavoriteService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FavoriteControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    FavoriteService favoriteService;


    @BeforeEach
    public void initMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    public String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test
    @DisplayName("POST /api/board/{boardId}/favorite [로그인X]: 401 에러를 반환합니다.")
    public void insertFavoriteWithNoAuthTest() throws Exception {
        //when & then
        mvc.perform(post("/api/board/1/favorite"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/board/{boardId}/favorite [로그인O]: 응답 코드 200을 반환합니다.")
    public void insertFavoriteTest() throws Exception {

        //when & then
        mvc.perform(post("/api/board/1/favorite"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete /api/board/{boardId}/favorite [로그인X]: 401 에러를 반환합니다.")
    public void deleteFavoriteWithNoAuthTest() throws Exception {
        //when & then
        mvc.perform(delete("/api/board/1/favorite"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Delete /api/board/{boardId}/favorite [로그인O]: 응답 코드 200을 반환합니다.")
    public void deleteFavoriteTest() throws Exception {

        //when & then
        mvc.perform(delete("/api/board/1/favorite"))
            .andExpect(status().isOk());
    }
}
