package simple_blog.LeeJerry.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    AbstractException(HttpStatus httpStatus, String message) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
