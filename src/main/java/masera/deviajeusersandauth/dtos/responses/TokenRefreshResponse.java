package masera.deviajeusersandauth.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de un refresco de token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshResponse {

  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
}
