package zipgoo.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zipgoo.server.domain.type.Pay;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    User user;
    String Address;
    LocalDateTime visitDate;
    LocalDateTime moveDate;
    Pay payType;
    Long deposit;
    Long monthlyPrice;
    Long maintenanceCost;
}
