package simple_blog.LeeJerry.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return new UserProxy(userRepository.findById(Long.parseLong(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다.")));
    }
}
