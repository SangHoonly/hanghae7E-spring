package simple_blog.LeeJerry.custom;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.entity.UserEntity;

public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser withCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        String username = StringUtils.hasLength(withCustomUser.username()) ? withCustomUser.username() : withCustomUser.value();

        UserDetails principal = new UserProxy(UserEntity.builder()
            .username(username)
            .password(withCustomUser.password())
            .build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        context.setAuthentication(authentication);

        return context;
    }
}
