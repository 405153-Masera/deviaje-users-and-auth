package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.dtos.post.ForgotPasswordRequest;
import masera.deviajeusersandauth.dtos.post.PasswordChangeRequest;
import masera.deviajeusersandauth.dtos.post.ResetPasswordRequest;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar operaciones relacionadas con contraseñas.
 */
@Service
public interface PasswordService {

  /**
   * Cambia la contraseña del usuario.
   *
   * @param userId id del usuario cuya contraseña se cambiará.
   * @param request Contiene la nueva contraseña y la contraseña actual.
   * @return Respuesta con el resultado de la operación.
   */
  MessageResponse changePassword(Integer userId, PasswordChangeRequest request);

  /**
   * Inicia el proceso de recuperación de contraseña.
   *
   * @param request contiene el email del usuario que solicita la recuperación.
   * @return Respuesta con el resultado de la operación.
   */
  MessageResponse forgotPassword(ForgotPasswordRequest request);

  /**
   * Resetea la contraseña del usuario.
   *
   * @param request contiene el token de recuperación y la nueva contraseña.
   * @return Respuesta con el resultado de la operación.
   */
  MessageResponse resetPassword(ResetPasswordRequest request);
}
