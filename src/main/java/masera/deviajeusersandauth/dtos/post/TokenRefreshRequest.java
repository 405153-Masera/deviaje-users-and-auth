package masera.deviajeusersandauth.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una solicitud de refresco de token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

  @NotBlank(message = "Refresh token is required")
  private String refreshToken;
}
