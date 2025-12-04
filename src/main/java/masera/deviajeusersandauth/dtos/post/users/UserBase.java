package masera.deviajeusersandauth.dtos.post.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;
import masera.deviajeusersandauth.validatons.adults.Adult;
import masera.deviajeusersandauth.validatons.password.ValidPassword;

/**
 * Clase base que representa la solicitud para crear un usuario.
 * No se debe instanciar directamente.
 */
@Data
public class UserBase {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @ValidPassword
  private String password;

  private String firstName;
  private String lastName;

  @Pattern(
          regexp = "^(MALE|FEMALE|UNSPECIFIED)$",
          message = "Gender must be MALE, FEMALE, UNSPECIFIED"
  )
  private String gender;

  private String phone;

  @Adult
  private LocalDate birthDate;

  private PassportRequest passport;
}
