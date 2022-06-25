package simple_blog.LeeJerry.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import simple_blog.LeeJerry.custom.WithCustomUser;
import simple_blog.LeeJerry.dto.UserReq;
import simple_blog.LeeJerry.exception.InvalidException;
import simple_blog.LeeJerry.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    UserService userService;

    @BeforeEach
    public void initMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    public String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


    @Test
    @DisplayName("POST /api/register [로그인X]: 이메일이 유효한 형식이 아닐 경우 400 에러를 반환합니다.")
    public void userRegisterInvalidEmailTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@45").password("123456!").username("user").build();

        //when & then
        mvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/register [로그인X]: 비밀번호가 유효한 형식이 아닐 경우 400 에러를 반환합니다.")
    public void userRegisterInvalidPasswordTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@455.com").password("1234").username("user").build();

        //when & then
        mvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/register [로그인X]: 사용자 이름이 없을 경우 400 에러를 반환합니다.")
    public void userRegisterInvalidUsernameTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@455.com").password("123456!").build();

        //when & then
        mvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomUser
    @DisplayName("POST /api/register [로그인O]: 이미 로그인 되어있는 경우 400 에러를 반환합니다.")
    public void userRegisterAlreadyLoginTest() throws Exception {
        //when & then
        mvc.perform(post("/api/register"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("POST /api/register [로그인X]: 사용자의 정보가 유효한 형식일 경우 상태 코드 200을 반환합니다.")
    public void userRegisterTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@456.com").password("123456!").username("user").build();

        //when & then
        mvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("POST /api/login [로그인X]: 응답 헤더에 JWT를 발급합니다.")
    public void userLoginTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@456.com").password("123").build();
        String jwt = "jwt";
        given(userService.login(any())).willReturn(jwt);

        //when & then
        mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Authorization", jwt));
    }

    @Test
    @DisplayName("POST /api/login [로그인X]: 이메일과 비밀번호가 일치하지 않을 경우 400 에러를 반환합니다.")
    public void userInvalidLoginTest() throws Exception {
        //given
        UserReq userReq = UserReq.builder().email("123@456.com").password("123").build();
        given(userService.login(any())).willThrow(InvalidException.class);

        //when & then
        mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(userReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomUser
    @DisplayName("POST /api/login [로그인O]: 이미 로그인 되어있는 경우 400 에러를 반환합니다.")
    public void userLoginAlreadyLoginTest() throws Exception {
        //when & then
        mvc.perform(post("/api/login"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").isString());
    }


}
