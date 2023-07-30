package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Board;
import zipgoo.server.domain.Category;
import zipgoo.server.domain.User;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<List<Board>> findBoardsByTitle(String title);
    Optional<List<Board>> findBoardsByUser_Nickname(String nickName);
    Optional<List<Board>> findBoardsByAddress_City(String city);
    Optional<List<Board>> findBoardsByCategory(Category category);
}
