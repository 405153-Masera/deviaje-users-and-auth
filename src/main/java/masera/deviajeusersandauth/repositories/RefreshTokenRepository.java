package masera.deviajeusersandauth.repositories;

import java.util.Optional;
import masera.deviajeusersandauth.entities.RefreshTokenEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


/**
 * Conecta la aplicación con la base de datos para manejar tokens de refresco.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

  /**
   * Busca un token de refresco por su valor.
   *
   * @param token el token a buscar.
   * @return un {@link RefreshTokenEntity}
   */
  Optional<RefreshTokenEntity> findByToken(String token);

  /**
   *  Elimina todos los tokens de refresco asociados a un usuario.
   *
   * @param user el usuario al que pertenece los tokens.
   * @return un {@link int} que representa la cantidad de tokens eliminados.
   */
  @Modifying
  int deleteByUser(UserEntity user);
  //@Modifying indica que el metodo realiza una operación de modificación en la base de datos
  //sin la anotación @Modifying, Spring Data JPA asume que el metodo es de solo lectura
}
