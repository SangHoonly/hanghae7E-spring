package simple_blog.LeeJerry.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;

@Getter
public class InvalidException extends AbstractException {
    private final ErrorCode error;

    public InvalidException(ErrorCode error) {
        super(BAD_REQUEST, error.getMessage());
        this.error = error;
    }

}
