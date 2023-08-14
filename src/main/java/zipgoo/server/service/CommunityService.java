package zipgoo.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {
    @Transactional
    public ResponseEntity<Map<String, Object>> similarArea(String dong) throws Exception {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        return ResponseEntity.badRequest().build();
    }
}
