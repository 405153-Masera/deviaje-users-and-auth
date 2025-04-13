package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Size(min = 8, message = "New password must be at least 8 characters long")
  private String newPassword;
}
