package masera.deviajeusersandauth.dtos.post.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

/**
 * Clase base que representa la solicitud para crear un usuario.
 * No se debe instanciar directamente.
 */
@Data
public class UserBase {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
  private String password;

  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private String dni;
  private Integer dniTypeId;
}
