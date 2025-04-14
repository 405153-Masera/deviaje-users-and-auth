package masera.deviajeusersandauth.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "deviaje.app")
public class JwtConfig {
  private String jwtSecret;
  private int jwtExpirationMs;
  private int jwtRefreshExpirationMs;

  public String getJwtSecret() {
    return jwtSecret;
  }

  public void setJwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  public int getJwtExpirationMs() {
    return jwtExpirationMs;
  }

  public void setJwtExpirationMs(int jwtExpirationMs) {
    this.jwtExpirationMs = jwtExpirationMs;
  }

  public int getJwtRefreshExpirationMs() {
    return jwtRefreshExpirationMs;
  }

  public void setJwtRefreshExpirationMs(int jwtRefreshExpirationMs) {
    this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
  }
}