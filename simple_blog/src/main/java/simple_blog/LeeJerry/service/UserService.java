package simple_blog.LeeJerry.service;

import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.auth.JwtProvider;
import simple_blog.LeeJerry.dto.UserReq;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.InvalidException;
import simple_blog.LeeJerry.exception.NotFoundException;
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
    public void registerUser(UserReq userReq) throws InvalidException {
        if (userRepository.existsByEmail(userReq.getEmail())) throw new InvalidException(ErrorCode.USER_ALREADY_EXISTS);
        if (userReq.getUsername() == null) throw new InvalidException(ErrorCode.INVALID_REGISTER_USERNAME);
        UserEntity user = userReq.toEntity();
        userRepository.save(user);
    }

    @Transactional
    public String login(UserReq userReq) throws AbstractException {
        UserEntity userEntity = userRepository.findByEmail(userReq.getEmail()).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if(!userEntity.getPassword().equals(userReq.getPassword())) throw new InvalidException(ErrorCode.INVALID_LOGIN);

        return jwtProvider.createToken(userEntity.getId());
    }
}
