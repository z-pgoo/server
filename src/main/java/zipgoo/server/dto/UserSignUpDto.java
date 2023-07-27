package zipgoo.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String email;
    private String nickname;
    private String birthdate;
    private int age;

}
