package simple_blog.LeeJerry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simple_blog.LeeJerry.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
