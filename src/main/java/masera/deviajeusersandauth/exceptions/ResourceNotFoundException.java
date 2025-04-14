package masera.deviajeusersandauth.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un recurso no fue encontrado.
 * Esta excepción se utiliza para manejar errores de tipo 404 Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la excepción ResourceNotFoundException.
   *
   * @param message el mensaje de error que describe la causa de la excepción
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
