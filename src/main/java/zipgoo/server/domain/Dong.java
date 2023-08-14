package zipgoo.server.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "AREAS")
@AllArgsConstructor
public class Dong {
    @Id
    @Column(name = "area_id", columnDefinition = "BIGINT AUTO_INCREMENT")
    private long id;
    private String areaDo;
    private String areaSi;
    private String areaGu;
    private String areaDong;
}
