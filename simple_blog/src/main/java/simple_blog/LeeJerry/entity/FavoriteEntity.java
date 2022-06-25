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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "FAVORITES")
public class FavoriteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAVORITE_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOARD_ID")
    private BoardEntity boardEntity;

    @Column(name = "CREATED_DATE")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public FavoriteEntity(UserEntity userEntity, BoardEntity boardEntity) {
        this.userEntity = userEntity;
        this.boardEntity = boardEntity;
    }
}
