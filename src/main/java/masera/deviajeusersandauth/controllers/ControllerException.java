package masera.deviajeusersandauth.controllers;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import masera.deviajeusersandauth.dtos.common.ErrorApi;
import masera.deviajeusersandauth.exceptions.EmailAlreadyExistsException;
import masera.deviajeusersandauth.exceptions.InvalidResetTokenException;
import masera.deviajeusersandauth.exceptions.PassportAlreadyExistsException;
import masera.deviajeusersandauth.exceptions.PasswordMismatchException;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.exceptions.TokenRefreshException;
import masera.deviajeusersandauth.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * Clase para el manejo global de excepciones.
 */
@ControllerAdvice
@Data
public class ControllerException {

  /**
   * Manejador para ResourceNotFoundException (404).
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorApi> handleResourceNotFoundException(ResourceNotFoundException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.NOT_FOUND);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Manejador para UsernameAlreadyExistsException (409).
   */
  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ErrorApi> handleUsernameAlreadyExistsException(
          UsernameAlreadyExistsException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.CONFLICT);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Manejador para EmailAlreadyExistsException (409).
   */
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorApi> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.CONFLICT);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Manejador para PassportAlreadyExistsException (409).
   */
  @ExceptionHandler(PassportAlreadyExistsException.class)
  public ResponseEntity<ErrorApi> handlePassportAlreadyExistsException(
                                  PassportAlreadyExistsException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.CONFLICT);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Manejador para TokenRefreshException (403).
   */
  @ExceptionHandler(TokenRefreshException.class)
  public ResponseEntity<ErrorApi> handleTokenRefreshException(TokenRefreshException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.FORBIDDEN);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  /**
   * Manejador para PasswordMismatchException (400).
   */
  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<ErrorApi> handlePasswordMismatchException(PasswordMismatchException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Manejador para InvalidResetTokenException (400).
   */
  @ExceptionHandler(InvalidResetTokenException.class)
  public ResponseEntity<ErrorApi> handleInvalidResetTokenException(InvalidResetTokenException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Manejador para BadCredentialsException (401).
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorApi> handleBadCredentialsException(BadCredentialsException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.UNAUTHORIZED);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  /**
   * Manejador para AccessDeniedException (403).
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorApi> handleAccessDeniedException(AccessDeniedException e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.FORBIDDEN);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  /**
   * Manejador para errores de validación (400).
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
          MethodArgumentNotValidException e) {
    Map<String, String> fieldErrors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())));
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
    response.put("validationErrors", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Manejador para excepciones HTTP específicas.
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorApi> handleError(ResponseStatusException e) {
    ErrorApi error = buildError(e.getReason(), HttpStatus.valueOf(e.getStatusCode().value()));
    return ResponseEntity.status(e.getStatusCode()).body(error);
  }

  /**
   * Manejador global para cualquier otra excepción (500).
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorApi> handleError(Exception e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  /**
   * Metodo para construir un objeto error API.
   *
   * @param message mensaje de error a arrojar.
   * @param status  código de HTTP.
   * @return un ErrorApi.
   */
  private ErrorApi buildError(String message, HttpStatus status) {
    return ErrorApi.builder()
            .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
            .error(status.getReasonPhrase())
            .status(status.value())
            .message(message)
            .build();
  }
}
