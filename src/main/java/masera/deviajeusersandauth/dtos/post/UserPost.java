package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos para el registro de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPost {

  @NotBlank(message = "Username is required")
  @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private String dni;
  private Integer dniTypeId;
  private String avatarUrl;
}
