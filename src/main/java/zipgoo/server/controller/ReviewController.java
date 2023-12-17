package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;
import zipgoo.server.service.ReviewFacadeService;
import zipgoo.server.service.response.CommonResponse;
import zipgoo.server.service.response.ResponseService;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewFacadeService reviewFacadeService;
    private final ResponseService responseService;
    @PostMapping
    public CommonResponse<Long> create(@RequestPart("review") Review review,
                                       @RequestPart(value = "images", required = false) MultipartFile[] imageFiles){
        if (imageFiles == null) {
            imageFiles = new MultipartFile[0];
        }
        Long reviewId = reviewFacadeService.createReview(imageFiles, review);
        return responseService.getCommonResponse(reviewId);
    }
}
