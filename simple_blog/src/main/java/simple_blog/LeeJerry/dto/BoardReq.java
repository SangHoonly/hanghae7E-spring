package simple_blog.LeeJerry.dto;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.entity.UserEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardReq {
    private Long id;

    private String title;

    private String body;

    private Integer view_count;


    public Board toEntity(UserEntity userEntity) {
        if (title.equals("")) throw new InvalidParameterException("Board.title is null");
        if (body.equals("")) throw new InvalidParameterException("Board.body is null");

        return Board.builder()
            .id(id)
            .userEntity(userEntity)
            .title(this.title)
            .body(this.body)
            .build();
    }
}
