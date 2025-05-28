package masera.deviajeusersandauth.dtos.get;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un pasaporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportDto {
  private Integer id;
  private String passportNumber;
  private LocalDate expiryDate;
  private String issuanceCountry;
  private String nationality;
}