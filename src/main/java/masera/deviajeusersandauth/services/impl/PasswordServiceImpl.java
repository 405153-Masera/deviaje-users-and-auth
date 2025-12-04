package masera.deviajeusersandauth.services.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.post.ForgotPasswordRequest;
import masera.deviajeusersandauth.dtos.post.PasswordChangeRequest;
import masera.deviajeusersandauth.dtos.post.ResetPasswordRequest;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.entities.PasswordResetTokenEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.exceptions.InvalidResetTokenException;
import masera.deviajeusersandauth.exceptions.PasswordMismatchException;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.repositories.PasswordResetTokenRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.services.interfaces.EmailService;
import masera.deviajeusersandauth.services.interfaces.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de gestión de contraseñas.
 * Proporciona funcionalidades para cambiar contraseñas,
 * recuperar contraseñas olvidadas y restablecer contraseñas.
 */
@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

  private final UserRepository userRepository;

  private final PasswordResetTokenRepository resetTokenRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  @Value("${deviaje.app.frontend-url}")
  private String frontendUrl;

  private final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);

  @Override
  @Transactional
  public MessageResponse changePassword(Integer userId, PasswordChangeRequest request) {
    // Validar que las contraseñas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new PasswordMismatchException("Las contraseñas no coinciden");
    }

    // Obtener usuario
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con id: " + userId));

    // Verificar contraseña actual
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new PasswordMismatchException("La contraseña actual es incorrecta");
    }

    // Actualizar contraseña
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setIsTemporaryPassword(false);
    userRepository.save(user);

    return new MessageResponse("Password cambiada exitosamente", true);
  }

  @Override
  @Transactional
  public MessageResponse forgotPassword(ForgotPasswordRequest request) {
    try {
      logger.info("Iniciando proceso de recuperación de contraseña para: {}", request.getEmail());

      // Buscar usuario por email
      UserEntity user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new ResourceNotFoundException(
                      "Usuario no encontrado con el email: " + request.getEmail()));

      logger.info("Usuario encontrado con ID: {}", user.getId());

      // Eliminar tokens existentes para este usuario
      logger.info("Eliminando tokens antiguos para usuario ID: {}", user.getId());
      resetTokenRepository.deleteByUserId(user.getId());

      // Crear nuevo token
      PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
      resetToken.setUser(user);
      resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));

      resetToken = resetTokenRepository.save(resetToken);
      this.sendEmailForgotPassword(user.getEmail(), resetToken.getToken());

      return new MessageResponse(
              "Email enviado exitosamente a: " + user.getEmail(), true);
    } catch (ResourceNotFoundException e) {
      logger.info("Intento de recuperación para un email inexistente: {}", request.getEmail());
      return new MessageResponse("El email no esta registrado", false);
    } catch (Exception e) {
      logger.error("Error en forgotPassword", e);
      return new MessageResponse("Ocurrió un error al procesar la solicitud de "
              + "recuperación de contraseña. Por favor, inténtelo más tarde.", false);
    }
  }


  @Override
  @Transactional
  public MessageResponse resetPassword(ResetPasswordRequest request) {
    // Validar que las contraseñas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new PasswordMismatchException("Las contraseñas no coinciden");
    }

    // Buscar token
    PasswordResetTokenEntity resetToken = resetTokenRepository.findByToken(request.getToken())
            .orElseThrow(() ->
                    new InvalidResetTokenException("El token de "
                            + "restablecimiento de contraseña no es válido"));

    // Verificar que el token no ha expirado y no ha sido usado
    if (resetToken.isExpired() || resetToken.getUsed()) {
      throw new InvalidResetTokenException("El token de restablecimiento "
              + "de contraseña ha expirado o ya ha sido utilizado");
    }

    // Obtener usuario
    UserEntity user = resetToken.getUser();

    // Actualizar contraseña
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    // Marcar token como usado
    resetToken.setUsed(true);
    resetTokenRepository.save(resetToken);

    try {
      String emailSubject = "Confirmación de cambio de contraseña - DeViaje";

      String emailContent =
              "<html>"
                      + "<head>"
                      + "<style>"
                      + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                      + ".container { max-width: 600px; margin: 0 auto; }"
                      + ".header { background-color: #8B5CF6; padding: 20px; text-align: center; }"
                      + ".header h1 { color: white; margin: 0; }"
                      + ".content { padding: 20px; border: 1px solid #ddd; border-top: none; }"
                      + ".success-icon { color: #10B981; font-size: 48px; "
                      + "text-align: center; margin: 20px 0; }"
                      + ".button { display: inline-block; "
                      + "background-color: #8B5CF6; color: white; "
                      + "padding: 10px 20px; text-decoration: none; border-radius: 5px; }"
                      + ".footer { margin-top: 30px; font-size: 12px; "
                      + "color: #666; text-align: center; }"
                      + "</style>"
                      + "</head>"
                      + "<body>"
                      + "<div class='container'>"
                      + "<div class='header'><h1>DeViaje</h1></div>"
                      + "<div class='content'>"
                      + "<h2>Contraseña actualizada exitosamente</h2>"
                      + "<div class='success-icon'>✓</div>"
                      + "<p>Hemos actualizado exitosamente la contraseña de su cuenta.</p>"
                      + "<p>Si usted no realizó este cambio, por "
                      + "favor contáctenos inmediatamente.</p>"
                      + "<div style='text-align: center; margin: 30px 0;'>"
                      + "<a href='" + frontendUrl
                      + "/user/login' class='button'>Iniciar sesión</a>"
                      + "</div>"
                      + "</div>"
                      + "<div class='footer'>"
                      + "<p>Este es un correo automático, por favor no responder.<br>"
                      + "© " + java.time.Year.now().getValue()
                      + " DeViaje. Todos los derechos reservados.</p>"
                      + "</div>"
                      + "</div>"
                      + "</body>"
                      + "</html>";

      emailService.sendEmail(user.getEmail(), emailSubject, emailContent);
      logger.info("Correo de confirmación de cambio de contraseña enviado a: {}", user.getEmail());
    } catch (Exception e) {
      // Log del error pero no interrumpir el flujo
      logger.error("Error al enviar correo de confirmación de cambio de contraseña", e);
    }

    return new MessageResponse("Contraseña restablecida exitosamente", true);
  }

  private void sendEmailForgotPassword(String email, String token) throws Exception {

    String resetUrl = frontendUrl + "/user/reset-password?token=" + token;
    String emailSubject = "Recuperación de contraseña - DeViaje";
    String emailContent =
            "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                    + ".container { max-width: 600px; margin: 0 auto; }"
                    + ".header { background-color: #8B5CF6; padding: 20px; text-align: center; }"
                    + ".header h1 { color: white; margin: 0; }"
                    + ".content { padding: 20px; border: 1px solid #ddd; border-top: none; }"
                    + ".button { display: inline-block; background-color: #8B5CF6; color: white; "
                    + "padding: 10px 20px; text-decoration: none; border-radius: 5px; }"
                    + ".footer { margin-top: 30px; font-size: 12px; "
                    + "color: #666; text-align: center; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'><h1>DeViaje</h1></div>"
                    + "<div class='content'>"
                    + "<h2>Recuperación de Contraseña</h2>"
                    + "<p>Hemos recibido una solicitud "
                    + "para restablecer la contraseña de su cuenta.</p>"
                    + "<p>Si no solicitó un restablecimiento de "
                    + "contraseña, puede ignorar este correo "
                    + "electrónico o contactarnos si tiene alguna inquietud.</p>"
                    + "<p>Para restablecer su contraseña, haga clic en el siguiente enlace:</p>"
                    + "<div style='text-align: center; margin: 30px 0;'>"
                    + "<a href='" + resetUrl + "' class='button'>Restablecer mi contraseña</a>"
                    + "</div>"
                    + "<p>Este enlace expirará en 24 horas.</p>"
                    + "<p>Si el botón no funciona, también puede "
                    + "copiar y pegar la siguiente URL en su navegador:</p>"
                    + "<p style='word-break: break-all;'>" + resetUrl + "</p>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "<p>Este es un correo automático, por favor no responder.<br>"
                    + "© " + java.time.Year.now().getValue()
                    + " DeViaje. Todos los derechos reservados.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

    emailService.sendEmail(email, emailSubject, emailContent);
  }
}
