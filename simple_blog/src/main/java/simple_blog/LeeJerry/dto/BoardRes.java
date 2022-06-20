package simple_blog.LeeJerry.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import simple_blog.LeeJerry.entity.BoardEntity;

@Getter
@NoArgsConstructor
public class BoardRes {

    private Long id;

    private Long userId;

    private String title;

    private String body;

    private String imageUrl;

    private Integer viewCount;

    private Integer favoriteCount;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Builder
    public BoardRes(Long id, Long userId, String title, String body, String imageUrl,
        Integer viewCount, Integer favoriteCount, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


    public static BoardRes toRes(BoardEntity boardEntity) {
        return BoardRes.builder().
            id(boardEntity.getId())
            .userId(boardEntity.getUserEntity().getId())
            .title(boardEntity.getTitle())
            .body(boardEntity.getBody())
            .imageUrl(boardEntity.getImageUrl())
            .viewCount(boardEntity.getViewCount())
            .favoriteCount(boardEntity.getFavoriteCount())
            .createdDate(boardEntity.getCreatedDate())
            .modifiedDate(boardEntity.getModifiedDate())
            .build();
    }
}
