package simple_blog.LeeJerry.dto;

import lombok.Builder;
import lombok.Getter;
import simple_blog.LeeJerry.entity.UserEntity;

@Getter
public class UserReq {

    private final String email;

    private final String username;

    private final String password;

    public UserEntity toEntity() {
        return UserEntity.builder().email(email).username(username).password(password).build();
    }

    @Builder
    public UserReq(String email, String password, String username)
    {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
