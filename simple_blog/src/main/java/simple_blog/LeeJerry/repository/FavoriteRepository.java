package simple_blog.LeeJerry.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import simple_blog.LeeJerry.entity.FavoriteEntity;

public interface FavoriteRepository extends JpaRepository <FavoriteEntity, Long>{
    Optional<FavoriteEntity> findByBoardIdAndUserEntityId(Long boardId, Long userId);
}
