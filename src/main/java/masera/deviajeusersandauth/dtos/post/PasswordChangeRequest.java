package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajeusersandauth.validatons.password.ValidPassword;

/**
 * DTO que representa una solicitud de cambio de contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

  @NotBlank(message = "Current password is required")
  private String currentPassword;

  @NotBlank(message = "New password is required")
  @ValidPassword
  private String newPassword;

  @NotBlank(message = "Confirm password cannot be empty")
  private String confirmPassword;
}
