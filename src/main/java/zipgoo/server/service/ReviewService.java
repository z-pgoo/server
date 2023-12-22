package zipgoo.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zipgoo.server.domain.Image;
import zipgoo.server.domain.Review;
import zipgoo.server.domain.User;
import zipgoo.server.dto.ReviewDto;
import zipgoo.server.dto.ReviewWithImageDto;
import zipgoo.server.exception.BadRequestException;
import zipgoo.server.repository.ImageRepository;
import zipgoo.server.repository.ReviewRepository;
import zipgoo.server.repository.UserRepository;
import zipgoo.server.utils.CurrentUser;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    private final UserRepository userRepository;
    @Transactional
    public Long createReview(ReviewDto reviewDto, List<String> imageUrls){
        Review review = Review.builder()
                .address(reviewDto.getAddress())
                .gpsX(reviewDto.getGpsX())
                .gpsY(reviewDto.getGpsY())
                .addressTitle("test")
                .visitDate(reviewDto.getVisitDate())
                .moveDate(reviewDto.getMoveDate())
                .payType(reviewDto.getPayType())
                .deposit(reviewDto.getDeposit())
                .monthlyPrice(reviewDto.getMonthlyPrice())
                .maintenanceCost(reviewDto.getMaintenanceCost())
                .containAllMaintenance(reviewDto.getContainAllMaintenance())
                .user(reviewDto.getUser())
                .build();
        if(!imageUrls.isEmpty()){
            review.setMainImage(new Image(review, imageUrls.get(0)));
        }
        List<Image> images = imageUrls.stream()
                .map(url -> new Image(review, url))
                .toList();
        imageRepository.saveAll(images);
        reviewRepository.save(review);
        return review.getId();
    }

    public List<Review> findUserReviews(User user){
        return reviewRepository.findAllByUserId(user.getId());
    }
    public ReviewWithImageDto findReviewWithImage(Long reviewId){
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if(reviewOptional.isEmpty()){
            throw new BadRequestException("존재한지 않는 리뷰입니다.");
        }
        List<Image> images = imageRepository.findImagesByReviewId(reviewId);
        return new ReviewWithImageDto(reviewOptional.get(), images);
    }
}
