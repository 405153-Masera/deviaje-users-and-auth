package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajeusersandauth.validatons.password.ValidPassword;

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
  @ValidPassword
  private String newPassword;

  @NotBlank(message = "Confirm password cannot be empty")
  private String confirmPassword;
}
