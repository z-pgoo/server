package zipgoo.server.service.response;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CommonResponse<T> {
    T data;
}
