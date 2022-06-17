package simple_blog.LeeJerry.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import simple_blog.LeeJerry.entity.Board;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRes {

    private Long id;

    private Long userId;

    private String title;

    private String body;

    private String imageUrl;

    private Integer viewCount;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;


    public static BoardRes toRes(Board board) {
        return BoardRes.builder().
            id(board.getId())
            .userId(board.getUserEntity().getId())
            .title(board.getTitle())
            .body(board.getBody())
            .imageUrl(board.getImageUrl())
            .viewCount(board.getViewCount())
            .createdDate(board.getCreatedDate())
            .modifiedDate(board.getModifiedDate())
            .build();
    }
}
