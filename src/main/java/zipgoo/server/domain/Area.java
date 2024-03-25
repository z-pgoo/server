package zipgoo.server.domain;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "AREAS")
@AllArgsConstructor
public class Area {
    @Id
    @Column(name = "area_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String si;
    private String gu;
    private String dong;
}
