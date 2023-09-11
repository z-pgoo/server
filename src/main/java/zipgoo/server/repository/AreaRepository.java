package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Area;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByAreaDongStartingWith(String prefix);

}
