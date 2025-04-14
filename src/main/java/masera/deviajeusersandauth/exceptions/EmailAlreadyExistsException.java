package masera.deviajeusersandauth.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Excepción lanzada cuando se intenta registrar un usuario con un correo electrónico que ya existe.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la excepción.
   *
   * @param message Mensaje de error que describe la excepción.
   */
  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
