package simple_blog.LeeJerry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.dto.UserReq;
import simple_blog.LeeJerry.service.UserService;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping ("/api/register")
    ResponseEntity<String> registerUser(@RequestBody UserReq userReq) {
        userService.registerUser(userReq);

        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    @PostMapping("/api/login")
    String login(@RequestBody UserReq userReq, Authentication authentication) {
        if (authentication.isAuthenticated()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return userService.login(userReq);
    }
}
