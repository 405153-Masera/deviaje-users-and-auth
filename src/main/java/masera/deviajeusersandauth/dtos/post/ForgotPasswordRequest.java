package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para restablecer la contraseña de un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Email should be valid")
  private String email;
}
