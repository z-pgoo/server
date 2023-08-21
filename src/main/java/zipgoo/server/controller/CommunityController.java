package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.domain.Dong;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.repository.DongRepository;
import zipgoo.server.service.CommunityService;
import zipgoo.server.service.UserService;
import zipgoo.server.service.response.ResponseService;
import zipgoo.server.utils.RandomNameGenerator.RandomNameGeneratorUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final DongRepository dongRepository;

    @PostMapping("/search-dong")
    public List<Dong> searchDongByKeyword(@RequestBody Map<String, String> requestBody) {
        String keyword = requestBody.get("keyword");
        List<Dong> dongs = dongRepository.findByAreaDongStartingWith(keyword);
        return dongs;
    }

}
