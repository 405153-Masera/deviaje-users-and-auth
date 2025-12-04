package masera.deviajeusersandauth.dtos.put;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajeusersandauth.dtos.post.users.PassportRequest;
import masera.deviajeusersandauth.validatons.adults.Adult;

/**
 * DTO para actualizar datos de un usuario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPut {

  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
  private String firstName;

  @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
  private String lastName;

  private String email;

  @Pattern(
          regexp = "^(MALE|FEMALE|UNSPECIFIED)$",
          message = "Gender must be MALE, FEMALE, or UNSPECIFIED"
  )
  private String gender;

  private String countryCallingCode;

  private String phone;

  @Adult
  private LocalDate birthDate;

  @Valid
  private PassportRequest passport;
}