package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zipgoo.server.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
