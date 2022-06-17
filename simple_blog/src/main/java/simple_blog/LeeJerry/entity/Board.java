package simple_blog.LeeJerry.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import simple_blog.LeeJerry.dto.BoardReq;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARDS")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;


    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List <FavoriteEntity> favorites = new ArrayList<>();

    private String title;

    private String body;

    @Column(name = "VIEW_COUNT")
    private Integer viewCount = 0;

    @Column(name = "FAVORITE_COUNT")
    private Integer favoriteCount = 0;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "CREATED_DATE")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public void viewed() {
        this.viewCount++;
    }

    public void increseFavorite() {this.favoriteCount++;}

    public void decreseFavorite() {this.favoriteCount--;}

    public void updateTitleAndBody(BoardReq boardReq) {
        this.title = boardReq.getTitle() != null ? boardReq.getTitle() : title;
        this.body = boardReq.getBody() != null ? boardReq.getBody() : body;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
