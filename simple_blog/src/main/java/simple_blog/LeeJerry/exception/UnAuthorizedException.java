package simple_blog.LeeJerry.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends AbstractException {
    private final ErrorCode error;

    public UnAuthorizedException(ErrorCode error) {
        super(UNAUTHORIZED, error.getMessage());
        this.error = error;
    }
}
