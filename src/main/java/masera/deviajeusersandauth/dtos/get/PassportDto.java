package masera.deviajeusersandauth.dtos.get;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate expiryDate;
  private String issuanceCountry;
  private String nationality;
}