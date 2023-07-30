package zipgoo.server.domain;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@RequiredArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;
    private String detail;
}
