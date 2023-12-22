package zipgoo.server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import zipgoo.server.domain.Image;
import zipgoo.server.domain.User;
import zipgoo.server.domain.type.Pay;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ReviewDto {
    private final String address;
    private final String gpsX;
    private final String gpsY;
    private final LocalDateTime visitDate;
    private final LocalDateTime moveDate;
    private final Pay payType;
    private final Long deposit;
    private final Long monthlyPrice;
    private final Long maintenanceCost;
    private final Boolean containAllMaintenance;

    private User user;
}
