package zipgoo.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginDto {
    public String snsType;
    public String accessToken;
}

