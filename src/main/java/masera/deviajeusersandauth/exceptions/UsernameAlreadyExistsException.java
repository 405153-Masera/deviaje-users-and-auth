package masera.deviajeusersandauth.exceptions;


import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el nombre de usuario ya existe en la base de datos.
 * Esta excepción se utiliza para indicar que el nombre de usuario proporcionado
 * ya está en uso y no puede ser registrado nuevamente.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la excepción.
   *
   * @param message Mensaje de error que describe la excepción.
   */
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
