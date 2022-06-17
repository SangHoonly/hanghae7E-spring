package simple_blog.LeeJerry.controller;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.dto.UserReq;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.InvalidException;
import simple_blog.LeeJerry.service.UserService;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping ("/api/register")
    void registerUser(@RequestBody UserReq userReq, Authentication authentication) {
        if (authentication != null) throw new InvalidException(ErrorCode.ALREADY_LOGIN);

        String emailPattern = "^\\w+@\\w+\\.\\w+(\\.\\w+)?";
        String passwordPattern = "^[a-zA-Z0-9가-힣]{6,}[!@#$%^&*(),.?\\\":{}|<>]+";

        if (!Pattern.matches(emailPattern, userReq.getEmail())) throw new InvalidException(ErrorCode.INVALID_REGISTER_EMAIL);
        if (!Pattern.matches(passwordPattern, userReq.getPassword())) throw new InvalidException(ErrorCode.INVALID_REGISTER_PASSWORD);

        userService.registerUser(userReq);
    }

    @PostMapping("/api/login")
    void login(@RequestBody UserReq userReq, Authentication authentication, HttpServletResponse response) {
        if (authentication != null) throw new InvalidException(ErrorCode.ALREADY_LOGIN);

        String jwt = userService.login(userReq);

        response.addHeader("Authorization", jwt);
    }
}
