package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.domain.Area;
import zipgoo.server.repository.AreaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final AreaRepository dongRepository;
    static{

    }

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

    public ResponseEntity<Map<String, Object>> checkStatus(@RequestParam boolean ignoreValid){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        if(ignoreValid){
            data.put("remainCount", 1);
        }
        else{
            data.put("address", "주소 받아서 넣기");
        }
        result.put("data", data);
        return ResponseEntity.ok().body(result);
    }
}
