package masera.deviajeusersandauth.dtos.reviews;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para estadísticas de reviews.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewStatsDto {

  private Long totalReviews;
  private Double averageRating;
  private Map<String, Double> averageRatingByCategory;
  private Map<Integer, Long> ratingDistribution;
}