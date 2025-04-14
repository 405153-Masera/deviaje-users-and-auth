package masera.deviajeusersandauth.services.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordResetTokenRepository resetTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService; // Servicio para enviar emails

  private Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);

  @Override
  @Transactional
  public MessageResponse changePassword(Integer userId, PasswordChangeRequest request) {
    // Validar que las contraseñas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new PasswordMismatchException("New password and confirm password do not match");
    }

    // Obtener usuario
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    // Verificar contraseña actual
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new PasswordMismatchException("Current password is incorrect");
    }

    // Actualizar contraseña
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    return new MessageResponse("Password changed successfully");
  }

  @Override
  @Transactional
  public MessageResponse forgotPassword(ForgotPasswordRequest request) {
    try {
      logger.info("Iniciando proceso de recuperación de contraseña para: {}", request.getEmail());

      // Buscar usuario por email
      UserEntity user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

      logger.info("Usuario encontrado con ID: {}", user.getId());

      // Eliminar tokens existentes para este usuario
      logger.info("Eliminando tokens antiguos para usuario ID: {}", user.getId());
      resetTokenRepository.deleteByUserId(user.getId());

      // Crear nuevo token
      PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
      resetToken.setUser(user);
      resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));

      resetToken = resetTokenRepository.save(resetToken);
      logger.info("Token creado con ID: {} y valor: {}", resetToken.getId(), resetToken.getToken());

      // SIMULACIÓN: Imprimir el token en consola en lugar de enviar email
      System.out.println("========== RECUPERACIÓN DE CONTRASEÑA ==========");
      System.out.println("Email: " + user.getEmail());
      System.out.println("Token: " + resetToken.getToken());
      System.out.println("URL de recuperación: https://deviaje.com/reset-password?token=" + resetToken.getToken());
      System.out.println("Expira en: " + resetToken.getExpiryDate());
      System.out.println("=================================================");

      // Verificar que el token sigue existiendo después de la "simulación de envío"
      boolean tokenExists = resetTokenRepository.findById(resetToken.getId()).isPresent();
      logger.info("¿El token aún existe después de la simulación de envío? {}", tokenExists);

      // Devolver el token en la respuesta (solo para demo)
      return new MessageResponse("For demonstration purposes, check console for password reset token. Token: " + resetToken.getToken());
    } catch (Exception e) {
      logger.error("Error en forgotPassword", e);
      throw e;
    }
  }


  @Override
  @Transactional
  public MessageResponse resetPassword(ResetPasswordRequest request) {
    // Validar que las contraseñas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new PasswordMismatchException("New password and confirm password do not match");
    }

    // Buscar token
    PasswordResetTokenEntity resetToken = resetTokenRepository.findByToken(request.getToken())
            .orElseThrow(() -> new InvalidResetTokenException("Invalid or expired password reset token"));

    // Verificar que el token no ha expirado y no ha sido usado
    if (resetToken.isExpired() || resetToken.getUsed()) {
      throw new InvalidResetTokenException("Invalid or expired password reset token");
    }

    // Obtener usuario
    UserEntity user = resetToken.getUser();

    // Actualizar contraseña
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    // Marcar token como usado
    resetToken.setUsed(true);
    resetTokenRepository.save(resetToken);

    return new MessageResponse("Password has been reset successfully");
  }
}
