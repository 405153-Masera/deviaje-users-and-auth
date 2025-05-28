package masera.deviajeusersandauth.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta
 * registrar un usuario con un número de pasaporte que ya existe.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PassportAlreadyExistsException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la excepción.
   *
   * @param message Mensaje de error que describe la excepción.
   */
  public PassportAlreadyExistsException(String message) {
    super(message);
  }
}