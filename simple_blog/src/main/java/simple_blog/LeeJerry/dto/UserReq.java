package simple_blog.LeeJerry.dto;

import lombok.Getter;
import simple_blog.LeeJerry.entity.UserEntity;

@Getter
public class UserReq {

    private String email;

    private String username;

    private String password;

    public UserEntity toEntity() {
        return UserEntity.builder().email(email).username(username).password(password).build();
    }
}
