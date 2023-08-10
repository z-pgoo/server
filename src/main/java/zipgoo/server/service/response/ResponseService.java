package zipgoo.server.service.response;

import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    public<T> CommonResponse<T> getCommonResponse (T data) {
        CommonResponse CommonResponse = new CommonResponse();
        CommonResponse.data = data;
        return CommonResponse;
    }
}
