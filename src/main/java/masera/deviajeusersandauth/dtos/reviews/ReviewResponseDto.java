package masera.deviajeusersandauth.dtos.reviews;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para una respuesta a review.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

  private Long id;
  private Long reviewId;
  private Integer userId;
  private String username;
  private String userFirstName;
  private String userLastName;
  private String comment;
  private LocalDateTime createdDatetime;
  private LocalDateTime lastUpdatedDatetime;
}