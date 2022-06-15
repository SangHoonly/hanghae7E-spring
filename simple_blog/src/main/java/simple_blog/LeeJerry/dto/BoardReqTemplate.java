package simple_blog.LeeJerry.dto;

import lombok.Getter;
import lombok.Setter;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.entity.ImageEntity;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.InvalidException;

@Getter
@Setter
public class BoardReqTemplate {
    Long id;

    String title;

    String body;

    Integer view_count;

    UserEntity userEntity;

    ImageEntity imageEntity;

    public BoardReqTemplate(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public BoardReqTemplate() {

    }

    public boolean validate() throws AbstractException {
        return (this.title != null && this.body != null);
    }

    public void transfer() {};



    public Board toEntity() {
        if (!validate()) throw new InvalidException(ErrorCode.INVALID_BOARD_REQ);

        return Board.builder()
            .id(id)
            .userEntity(userEntity)
            .imageEntity(imageEntity)
            .title(this.title)
            .body(this.body)
            .build();
    }
}
