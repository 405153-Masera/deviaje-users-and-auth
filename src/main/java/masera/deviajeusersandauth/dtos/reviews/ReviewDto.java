package masera.deviajeusersandauth.dtos.reviews;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseDto;

/**
 * DTO de respuesta para una review.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

  private Long id;
  private Integer userId;
  private String username;
  private String userFirstName;
  private String userLastName;
  private Integer rating;
  private String comment;
  private String category;
  private LocalDateTime createdDatetime;
  private LocalDateTime lastUpdatedDatetime;
  private List<ReviewResponseDto> responses;
  private Integer responsesCount;
}