package zipgoo.server.domain;

import lombok.*;
import zipgoo.server.domain.type.Role;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", columnDefinition = "BIGINT AUTO_INCREMENT")
    private long id;
    private String email;
    private String password;
    @Column(unique = true)
    private String nickname;
    private String birthDate;
    private int ageRange;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken; // 리프레시 토큰

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}

