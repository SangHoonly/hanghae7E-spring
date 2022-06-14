package simple_blog.LeeJerry.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import simple_blog.LeeJerry.auth.JwtAuthEntryPoint;
import simple_blog.LeeJerry.auth.JwtFilter;
import simple_blog.LeeJerry.auth.JwtProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthEntryPoint)

            .and()
            .authorizeRequests()
            .antMatchers("/api/login", "/api/register").permitAll()
            .antMatchers(HttpMethod.GET, "/api/board").permitAll()
            .anyRequest().authenticated()


            .and()
            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
