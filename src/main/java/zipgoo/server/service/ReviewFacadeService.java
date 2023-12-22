package zipgoo.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;
import zipgoo.server.domain.User;
import zipgoo.server.dto.ImageUploadResponse;
import zipgoo.server.dto.ReviewDto;
import zipgoo.server.dto.ReviewWithImageDto;
import zipgoo.server.exception.InternalServerException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewFacadeService {
    private final ReviewService reviewService;
    private final ImageStorageService imageStorageService;

    public Long createReview(MultipartFile[] files, ReviewDto reviewDto) {
        ImageUploadResponse imageUploadResponse = imageStorageService.uploadFiles(files);
                // Review Address Title관련 로직 필요
        try {
            return reviewService.createReview(reviewDto, imageUploadResponse.urls());
        } catch (Exception e) {
            imageStorageService.deleteFiles(imageUploadResponse.fileNames());
            throw e;
        }
    }

    public List<Review> findUserReviews(User user) {
       return reviewService.findUserReviews(user);
    }

    public ReviewWithImageDto findReview(Long id){
        return reviewService.findReviewWithImage(id);
    }
}
