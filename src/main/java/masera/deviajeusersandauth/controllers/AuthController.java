package masera.deviajeusersandauth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.post.ForgotPasswordRequest;
import masera.deviajeusersandauth.dtos.post.LoginRequest;
import masera.deviajeusersandauth.dtos.post.PasswordChangeRequest;
import masera.deviajeusersandauth.dtos.post.RefreshTokenRequest;
import masera.deviajeusersandauth.dtos.post.ResetPasswordRequest;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.responses.JwtResponse;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.security.services.UserDetailsImpl;
import masera.deviajeusersandauth.services.interfaces.AuthService;
import masera.deviajeusersandauth.services.interfaces.PasswordService;
import masera.deviajeusersandauth.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  private final UserService userService;

  private final PasswordService passwordService;

  /**
   * Metodo para autenticar un usuario.
   *
   * @param loginRequest representa la solicitud de inicio de sesión.
   * @return una respuesta con el token JWT.
   */
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticateUser(
          @Valid @RequestBody LoginRequest loginRequest) {
    JwtResponse response = authService.authenticateUser(loginRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para registrar un nuevo usuario.
   *
   * @param signupRequest representa la solicitud de registro.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(
          @Valid @RequestBody SignupRequest signupRequest) {
    MessageResponse response = userService.registerUser(signupRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para verificar el token de refresco.
   *
   * @param refreshTokenRequest representa el token de verificación.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<JwtResponse> refreshToken(
          @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    JwtResponse response = authService.refreshToken(refreshTokenRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para cerrar sesión.
   *
   * @param authentication representa la autenticación del usuario.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logoutUser(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    MessageResponse response = authService.logoutUser(userDetails.getId());
    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para cambiar la contraseña del usuario.
   *
   * @param authentication representa la autenticación del usuario.
   * @param passwordChangeRequest representa la solicitud de cambio de contraseña.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/change-password")
  public ResponseEntity<MessageResponse> changePassword(
          Authentication authentication,
          @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    MessageResponse response = passwordService
            .changePassword(userDetails.getId(), passwordChangeRequest);

    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para enviar un correo de restablecimiento de contraseña.
   *
   * @param forgotPasswordRequest representa la solicitud de restablecimiento de contraseña.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/forgot-password")
  public ResponseEntity<MessageResponse> forgotPassword(
          @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    MessageResponse response = passwordService.forgotPassword(forgotPasswordRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * Metodo para restablecer la contraseña del usuario.
   *
   * @param resetPasswordRequest representa la solicitud de restablecimiento de contraseña.
   * @return una respuesta con el mensaje de éxito.
   */
  @PostMapping("/reset-password")
  public ResponseEntity<MessageResponse> resetPassword(
          @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    MessageResponse response = passwordService.resetPassword(resetPasswordRequest);
    return ResponseEntity.ok(response);
  }
}

// Código para obtener la contraseña de la base de datos
/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    String password = "Admin123!";
    String hashedPassword = encoder.encode(password);
    System.out.println("Contraseña hasheada: " + hashedPassword);*/