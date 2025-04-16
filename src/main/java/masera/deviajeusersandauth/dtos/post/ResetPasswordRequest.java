package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para restablecer la contraseña de un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

  @NotBlank(message = "Token cannot be empty")
  private String token;

  @NotBlank(message = "New password cannot be empty")
  @Size(min = 6, max = 40, message = "New password must be between 6 and 40 characters")
  private String newPassword;

  @NotBlank(message = "Confirm password cannot be empty")
  private String confirmPassword;
}
