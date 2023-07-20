package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zipgoo.server.domain.SocialType;
import zipgoo.server.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
