package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;
import zipgoo.server.domain.User;
import zipgoo.server.domain.type.Role;
import zipgoo.server.dto.ReviewDto;
import zipgoo.server.dto.ReviewWithImageDto;
import zipgoo.server.exception.BadRequestException;
import zipgoo.server.service.ReviewFacadeService;
import zipgoo.server.service.UserService;
import zipgoo.server.service.response.CommonResponse;
import zipgoo.server.service.response.ResponseService;
import zipgoo.server.utils.CurrentUser;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private final ReviewFacadeService reviewFacadeService;
    @Autowired
    private final ResponseService responseService;
    @PostMapping("/create/review")
    public CommonResponse<Long> create(@RequestPart("review") ReviewDto review,
                                       @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
                                       @CurrentUser User user){
        if (imageFiles == null) {
            imageFiles = new MultipartFile[0];
        }
        if(user == null){
            throw new BadRequestException("허가되지 않은 사용자 입니다.");
        }
        review.setUser(user);
        Long reviewId = reviewFacadeService.createReview(imageFiles, review);
        return responseService.getCommonResponse(reviewId);
    }

    @GetMapping("/reviews")
    public CommonResponse<List<Review>> findAllReview(@CurrentUser User user) {
        List<Review> reviews = reviewFacadeService.findUserReviews(user);
        return responseService.getCommonResponse(reviews);
    }

    @GetMapping("/reviews/{id}")
    public CommonResponse<ReviewWithImageDto> findReview(@PathVariable Long id, @CurrentUser User user) {
        ReviewWithImageDto review = reviewFacadeService.findReview(id);
        User author = review.getReview().getUser();
        if (!user.equals(author) || user.getRole() != Role.ADMIN) {
            throw new BadRequestException("해당 리뷰를 열람할 권한이 없습니다.");
        }
        return responseService.getCommonResponse(review);
    }
}
