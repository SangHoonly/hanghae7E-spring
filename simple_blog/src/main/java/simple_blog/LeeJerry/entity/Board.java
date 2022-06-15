package simple_blog.LeeJerry.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
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

    @OneToOne(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private ImageEntity imageEntity;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List <FavoriteEntity> favorites = new ArrayList<>();

    private String title;

    private String body;

    private Integer view_count = 0;

    @CreatedDate
    private Date created_date;

    @LastModifiedDate
    private Date modified_date;

    public void viewed() {
        this.view_count++;
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void update(String title, String body, ImageEntity imageEntity) {
        this.title = title;
        this.body = body;
        this.imageEntity = imageEntity;
    }
}
