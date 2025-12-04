package masera.deviajeusersandauth.dtos.reviews;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear una respuesta a una review.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseCreateRequest {

  @NotBlank(message = "El comentario es obligatorio")
  private String comment;
}