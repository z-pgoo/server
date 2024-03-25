package zipgoo.server.domain;

import lombok.*;
import zipgoo.server.domain.type.Pay;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;
    private String address;
    private String gpsX;
    private String gpsY;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image mainImage;
    private String addressTitle;
    private LocalDateTime visitDate;
    private LocalDateTime moveDate;
    private Pay payType;
    private Long deposit;
    private Long monthlyPrice;
    private Long maintenanceCost;
    private Boolean containAllMaintenance;

    public void setMainImage(Image image) {
        this.mainImage = image;
    }
}
