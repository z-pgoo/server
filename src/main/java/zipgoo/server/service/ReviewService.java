package zipgoo.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zipgoo.server.domain.Image;
import zipgoo.server.domain.Review;
import zipgoo.server.repository.ImageRepository;
import zipgoo.server.repository.ReviewRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    public Long createReview(Review review, List<String> imageUrls){
        Review save = reviewRepository.save(review);
        List<Image> images = imageUrls.stream()
                .map(url -> new Image(review, url))
                .toList();
        imageRepository.saveAll(images);
        return review.getId();
    }
}
