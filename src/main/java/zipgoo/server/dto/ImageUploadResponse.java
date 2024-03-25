package zipgoo.server.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public record ImageUploadResponse(List<String> urls, List<String> fileNames) {
}
