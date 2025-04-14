package masera.deviajeusersandauth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.post.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  private final UserService userService;

  private final PasswordService passwordService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    String password = "Admin123!";
    String hashedPassword = encoder.encode(password);
    System.out.println("Contraseña hasheada: " + hashedPassword);*/
    JwtResponse response = authService.authenticateUser(loginRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
    MessageResponse response = userService.registerUser(signupRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
    JwtResponse response = authService.refreshToken(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logoutUser(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    MessageResponse response = authService.logoutUser(userDetails.getId());
    return ResponseEntity.ok(response);
  }


  @PostMapping("/change-password")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MessageResponse> changePassword(
          Authentication authentication,
          @Valid @RequestBody PasswordChangeRequest request) {

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    MessageResponse response = passwordService.changePassword(userDetails.getId(), request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    MessageResponse response = passwordService.forgotPassword(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    MessageResponse response = passwordService.resetPassword(request);
    return ResponseEntity.ok(response);
  }
}