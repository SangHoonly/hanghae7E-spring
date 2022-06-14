package simple_blog.LeeJerry.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;

@Getter
public class NotFoundException extends AbstractException {
    private final ErrorCode error;

    public NotFoundException(ErrorCode error) {
        super(NOT_FOUND, error.getMessage());
        this.error = error;
    }
}
