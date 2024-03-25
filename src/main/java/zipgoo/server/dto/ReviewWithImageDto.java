package zipgoo.server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import zipgoo.server.domain.Image;
import zipgoo.server.domain.Review;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ReviewWithImageDto {
    private final Review review;
    private final List<Image> imageList;
}
