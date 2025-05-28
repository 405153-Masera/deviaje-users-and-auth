package masera.deviajeusersandauth.services.impl;

import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.repositories.PassportRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Implementación de la interfaz VerificationService,
 * contiene la lógica de verificación.
 */
@Service
@RequiredArgsConstructor
public class VerificationService {

  private final UserRepository userRepository;

  private final PassportRepository passportRepository;


  /**
   * Verifica si el nombre de usuario es único.
   *
   * @param username Nombre de usuario a verificar.
   * @return {@code true} si el nombre de usuario es único, {@code false} en caso contrario.
   */
  public boolean isUsernameUnique(String username) {
    return !userRepository.existsByUsername(username);
  }

  /**
   * Verifica si el correo electrónico es único.
   *
   * @param email Correo electrónico a verificar.
   * @return {@code true} si el correo electrónico es único, {@code false} en caso contrario.
   */
  public boolean isEmailUnique(String email) {
    return !userRepository.existsByEmail(email);
  }

  /**
   * Verifica si el número de DNI pasaporte es único.
   *
   * @param passportNumber Número de pasaporte a verificar.
   * @return {@code true} si el número de pasaporte es único,
   *{@code false} en caso contrario.
   */
  public boolean isPassportUnique(String passportNumber) {
    return !passportRepository.existsByPassportNumber(passportNumber);
  }
}
