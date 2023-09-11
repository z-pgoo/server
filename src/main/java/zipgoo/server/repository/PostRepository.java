package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.Post;
import zipgoo.server.domain.Category;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findPostsByTitleContains(String title);
    Optional<List<Post>> findPostsByAddress_City(String city);
    Optional<List<Post>> findPostsByUserNickname(String nickName);
    Optional<List<Post>> findPostsByCategory(Category category);
}
