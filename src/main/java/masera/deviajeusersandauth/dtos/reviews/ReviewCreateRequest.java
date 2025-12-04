package masera.deviajeusersandauth.dtos.reviews;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear una nueva review de la plataforma.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {

  @NotNull(message = "El rating es obligatorio")
  @Min(value = 1, message = "El rating mínimo es 1")
  @Max(value = 5, message = "El rating máximo es 5")
  private Integer rating;

  @NotBlank(message = "El comentario es obligatorio")
  private String comment;

  @NotNull(message = "La categoría es obligatoria")
  private String category; // USABILITY, SEARCHES, BOOKING_PROCESS, PERFORMANCE, GENERAL
}