package simple_blog.LeeJerry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorRes {

    private Integer status;
    private String message;

}
