package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
