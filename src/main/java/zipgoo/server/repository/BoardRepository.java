package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Post;
import zipgoo.server.domain.Category;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findBoardsByTitle(String title);
    Optional<List<Post>> findBoardsByUser_Nickname(String nickName);
    Optional<List<Post>> findBoardsByAddress_City(String city);
    Optional<List<Post>> findBoardsByCategory(Category category);
}
