package zipgoo.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;
import zipgoo.server.dto.ImageUploadResponse;
import zipgoo.server.exception.InternalServerException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewFacadeService {
    private final ReviewService reviewService;
    private final ImageStorageService imageStorageService;

    public Long createReview(MultipartFile[] files, Review review) {
        ImageUploadResponse imageUploadResponse = imageStorageService.uploadFiles(files);
        try {
            return reviewService.createReview(review, imageUploadResponse.urls());
        } catch (Exception e) {
            imageStorageService.deleteFiles(imageUploadResponse.fileNames());
            throw e;
        }
    }
    public List<String> createImage(MultipartFile[] files){
        return imageStorageService.uploadFiles(files).urls();
    }
}
