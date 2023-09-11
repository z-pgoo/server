package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.domain.Area;
import zipgoo.server.repository.AreaRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final AreaRepository dongRepository;

    @PostMapping("/search-dong")
    public List<Area> searchDongByKeyword(@RequestBody Map<String, String> requestBody) {
        String keyword = requestBody.get("keyword");
        List<Area> dongs = dongRepository.findByAreaDongStartingWith(keyword);
        return dongs;
    }

    public List<Area> searchDongByKeyword(@RequestBody String keyword){
        List<Area> dongs = dongRepository.findByAreaDongStartingWith(keyword);
        return dongs;
    }
}
