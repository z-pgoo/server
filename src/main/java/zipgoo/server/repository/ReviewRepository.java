package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zipgoo.server.domain.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
}
