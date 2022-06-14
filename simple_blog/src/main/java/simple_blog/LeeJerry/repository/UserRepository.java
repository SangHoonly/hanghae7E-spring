package simple_blog.LeeJerry.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import simple_blog.LeeJerry.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
