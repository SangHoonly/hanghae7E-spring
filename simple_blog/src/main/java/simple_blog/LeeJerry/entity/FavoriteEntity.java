package simple_blog.LeeJerry.entity;

import java.util.Date;
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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Builder
@Table(name = "FAVORITES")
public class FavoriteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAVORITE_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOARD_ID")
    private Board board;

    @CreatedDate
    private Date created_date;

    @LastModifiedDate
    private Date modified_date;

}
