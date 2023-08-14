package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Dong;
import zipgoo.server.domain.User;

import java.util.List;
import java.util.Optional;

public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByAreaDongStartingWith(String prefix);

}
