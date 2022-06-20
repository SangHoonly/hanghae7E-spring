package simple_blog.LeeJerry.dto;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import simple_blog.LeeJerry.entity.BoardEntity;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.InvalidException;

@Getter
public class BoardReq {
    private Long id;

    private final String title;

    private final String body;

    private UserEntity userEntity;

    private final MultipartFile files;

    @Builder
    public BoardReq(MultipartFile files, String title, String body) {
        this.files = files;
        this.title = title;
        this.body = body;
    }


    public File multipartFileToFile() throws IOException {
        File file = new File(Objects.requireNonNull(files.getOriginalFilename()));
        files.transferTo(file);

        return file;
    }

    public boolean validate() throws AbstractException {
        return (this.title != null && this.body != null);
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public BoardEntity toEntity() {
        if (!validate()) throw new InvalidException(ErrorCode.INVALID_BOARD_REQ);

        return BoardEntity.builder()
            .id(id)
            .userEntity(userEntity)
            .title(this.title)
            .body(this.body)
            .build();
    }

    public void setId(Long boardId) {
        this.id = boardId;
    }
}
