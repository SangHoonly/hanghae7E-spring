package simple_blog.LeeJerry.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import simple_blog.LeeJerry.dto.BoardReq;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BOARDS")
@NoArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    private String title;

    private String body;

    @Column(name = "VIEW_COUNT")
    @ColumnDefault("0")
    private Integer viewCount;

    @Column(name = "FAVORITE_COUNT")
    @ColumnDefault("0")
    private Integer favoriteCount;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "CREATED_DATE")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public BoardEntity(Long id, UserEntity userEntity, String title, String body, Integer viewCount, Integer favoriteCount, String imageUrl) {
        this.id = id;
        this.userEntity = userEntity;
        this.title = title;
        this.body = body;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.imageUrl = imageUrl;
    }

    public void viewed() {
        this.viewCount++;
    }

    public void increaseFavorite() {this.favoriteCount++;}

    public void decreaseFavorite() {this.favoriteCount--;}

    public void updateTitleAndBody(BoardReq boardReq) {
        this.title = boardReq.getTitle() != null ? boardReq.getTitle() : title;
        this.body = boardReq.getBody() != null ? boardReq.getBody() : body;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
