package zipgoo.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    public String snsType;
    public String accessToken;
    public String nickname;

}
