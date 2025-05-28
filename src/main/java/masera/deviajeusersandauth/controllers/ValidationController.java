package masera.deviajeusersandauth.controllers;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.services.impl.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para realizar validaciones en tiempo real de
 * email, username y número de pasaporte. Permite al
 * frontend verificar si estos datos ya existen en la base de datos mientras el usuario los ingresa.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
public class ValidationController {

  /**
   * Servicio de verificación de datos unicos.
   */
  private final VerificationService verificationService;

  /**
   * Verifica si el nombre de usuario es único.
   *
   * @param username Nombre de usuario a verificar.
   * @return Respuesta con el resultado de la verificación.
   */
  @GetMapping("/username")
  public ResponseEntity<Map<String, Boolean>> usernameUnique(@RequestParam String username) {
    boolean isUnique = verificationService.isUsernameUnique(username);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isUnique", isUnique);
    return ResponseEntity.ok(response);
  }

  /**
   * Verifica si el correo electrónico es único.
   *
   * @param email Correo electrónico a verificar.
   * @return Respuesta con el resultado de la verificación.
   */
  @GetMapping("/email")
  public ResponseEntity<Map<String, Boolean>> emailUnique(@RequestParam String email) {
    boolean isUnique = verificationService.isEmailUnique(email);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isUnique", isUnique);
    return ResponseEntity.ok(response);
  }

  /**
   * Verifica si el número de pasaporte es único.
   *
   * @param passportNumber Número de pasaporte a verificar.
   * @return Respuesta con el resultado de la verificación.
   */
  @GetMapping("/passportNumber")
  public ResponseEntity<Map<String, Boolean>> passportUnique(@RequestParam String passportNumber) {
    boolean isUnique = verificationService.isPassportUnique(passportNumber);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isUnique", isUnique);
    return ResponseEntity.ok(response);
  }
}