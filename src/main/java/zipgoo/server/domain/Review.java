package zipgoo.server.domain;

import zipgoo.server.domain.type.Pay;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Review {
    @Id @GeneratedValue
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
