package masera.deviajeusersandauth.dtos.post.users;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de pasaporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassportRequest {

  @NotBlank(message = "Passport number is required")
  private String passportNumber;

  private LocalDate expiryDate;

  private String issuanceCountry;

  private String nationality;
}