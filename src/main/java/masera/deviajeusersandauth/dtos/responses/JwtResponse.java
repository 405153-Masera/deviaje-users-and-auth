package masera.deviajeusersandauth.dtos.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de un JWT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

  private String token;
  private String refreshToken;

  @Builder.Default
  private String type = "Bearer";
  private Integer id;
  private String username;
  private String email;
  private List<String> roles;
  private Boolean isTemporaryPassword;
}
