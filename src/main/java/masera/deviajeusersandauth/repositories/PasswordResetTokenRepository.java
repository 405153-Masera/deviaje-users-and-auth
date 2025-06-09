package masera.deviajeusersandauth.repositories;

import java.util.Optional;
import masera.deviajeusersandauth.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar los tokens de restablecimiento de contraseña.
 * Proporciona métodos para buscar por token y eliminar por ID de usuario.
 */
@Repository
public interface PasswordResetTokenRepository extends
        JpaRepository<PasswordResetTokenEntity, Integer> {

  /**
   * Busca un token de restablecimiento de contraseña por su valor.
   *
   * @param token el token a buscar
   * @return un Optional que contiene el token si se encuentra, o vacío si no existe
   */
  Optional<PasswordResetTokenEntity> findByToken(String token);

  /**
   * Elimina todos los tokens de restablecimiento de contraseña asociados a un usuario específico.
   *
   * @param userId el ID del usuario cuyos tokens se desean eliminar
   */
  void deleteByUserId(Integer userId);
}