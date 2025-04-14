package masera.deviajeusersandauth.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar errores relacionados con la renovación de tokens.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la excepción.
   *
   * @param token el token que causó el error.
   * @param message el mensaje de error.
   */
  public TokenRefreshException(String token, String message) {
    super(String.format("Failed for [%s]: %s", token, message));
  }
}
