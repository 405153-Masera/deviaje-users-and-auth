package masera.deviajeusersandauth.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.reviews.ReviewCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewStatsDto;
import masera.deviajeusersandauth.entities.ReviewEntity;
import masera.deviajeusersandauth.entities.ReviewResponseEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.repositories.ReviewRepository;
import masera.deviajeusersandauth.repositories.ReviewResponseRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.services.interfaces.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de reviews de la plataforma.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewResponseRepository reviewResponseRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ReviewDto createReview(ReviewCreateRequest request, Integer userId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado"));

    ReviewEntity review = ReviewEntity.builder()
            .user(user)
            .rating(request.getRating())
            .comment(request.getComment())
            .category(ReviewEntity.ReviewCategory.valueOf(request.getCategory().toUpperCase()))
            .build();

    ReviewEntity savedReview = reviewRepository.save(review);
    return mapToDto(savedReview);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReviewDto> getAllReviews() {
    return reviewRepository.findAllByOrderByCreatedDatetimeDesc().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReviewDto> getReviewsByCategory(String category) {
    ReviewEntity.ReviewCategory categoryEnum =
            ReviewEntity.ReviewCategory.valueOf(category.toUpperCase());
    return reviewRepository.findByCategoryOrderByCreatedDatetimeDesc(categoryEnum).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReviewDto> getReviewsByUser(Integer userId) {
    return reviewRepository.findByUserIdOrderByCreatedDatetimeDesc(userId).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewDto getReviewById(Long id) {
    ReviewEntity review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Review no encontrada con id: " + id));
    return mapToDto(review);
  }

  @Override
  @Transactional
  public void deleteReview(Long id) {
    ReviewEntity review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Review no encontrada con id: " + id));

    // Eliminar primero las respuestas
    reviewResponseRepository.deleteByReviewId(id);

    // Luego eliminar la review
    reviewRepository.delete(review);
  }

  @Override
  @Transactional
  public ReviewResponseDto createReviewResponse(
          Long reviewId, ReviewResponseCreateRequest request, String username) {

    ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Review no encontrada con id: " + reviewId));

    UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado: " + username));

    ReviewResponseEntity response = ReviewResponseEntity.builder()
            .review(review)
            .user(user)
            .comment(request.getComment())
            .build();

    ReviewResponseEntity savedResponse = reviewResponseRepository.save(response);
    return mapResponseToDto(savedResponse);
  }

  @Override
  @Transactional
  public void deleteReviewResponse(Long responseId) {
    ReviewResponseEntity response = reviewResponseRepository.findById(responseId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Respuesta no encontrada con id: " + responseId));

    reviewResponseRepository.delete(response);
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewStatsDto getReviewStats() {
    Long totalReviews = reviewRepository.countAllReviews();
    Double averageRating = reviewRepository.calculateAverageRating();

    // Calcular promedio por categoría
    Map<String, Double> averageByCategory = new HashMap<>();
    for (ReviewEntity.ReviewCategory category : ReviewEntity.ReviewCategory.values()) {
      Double avg = reviewRepository.calculateAverageRatingByCategory(category);
      averageByCategory.put(category.name(), avg != null ? avg : 0.0);
    }

    // Calcular distribución de ratings
    Map<Integer, Long> ratingDistribution = new HashMap<>();
    List<ReviewEntity> allReviews = reviewRepository.findAll();
    for (int i = 1; i <= 5; i++) {
      final int rating = i;
      long count = allReviews.stream()
              .filter(r -> r.getRating() == rating)
              .count();
      ratingDistribution.put(rating, count);
    }

    return ReviewStatsDto.builder()
            .totalReviews(totalReviews)
            .averageRating(averageRating != null ? averageRating : 0.0)
            .averageRatingByCategory(averageByCategory)
            .ratingDistribution(ratingDistribution)
            .build();
  }

  // Métodos auxiliares de mapeo

  private ReviewDto mapToDto(ReviewEntity review) {
    List<ReviewResponseDto> responses = review.getResponses().stream()
            .map(this::mapResponseToDto)
            .collect(Collectors.toList());

    return ReviewDto.builder()
            .id(review.getId())
            .userId(review.getUser().getId())
            .username(review.getUser().getUsername())
            .userFirstName(review.getUser().getFirstName())
            .userLastName(review.getUser().getLastName())
            .rating(review.getRating())
            .comment(review.getComment())
            .category(review.getCategory().name())
            .createdDatetime(review.getCreatedDatetime())
            .lastUpdatedDatetime(review.getLastUpdatedDatetime())
            .responses(responses)
            .responsesCount(responses.size())
            .build();
  }

  private ReviewResponseDto mapResponseToDto(ReviewResponseEntity response) {
    return ReviewResponseDto.builder()
            .id(response.getId())
            .reviewId(response.getReview().getId())
            .userId(response.getUser().getId())
            .username(response.getUser().getUsername())
            .userFirstName(response.getUser().getFirstName())
            .userLastName(response.getUser().getLastName())
            .comment(response.getComment())
            .createdDatetime(response.getCreatedDatetime())
            .lastUpdatedDatetime(response.getLastUpdatedDatetime())
            .build();
  }
}