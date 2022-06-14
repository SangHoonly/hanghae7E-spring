package simple_blog.LeeJerry.service;

import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.auth.JwtProvider;
import simple_blog.LeeJerry.dto.UserReq;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }


    @Transactional
    public void registerUser(UserReq userReq) {
        if (userRepository.existsByEmail(userReq.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

        userRepository.save(userReq.toEntity());
    }

    @Transactional
    public String login(UserReq userReq) {
        UserEntity userEntity = userRepository
            .findByEmail(userReq.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다!"));

        if(!userEntity.getPassword().equals(userReq.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.");

        return jwtProvider.createToken(userEntity.getId());
    }
}
