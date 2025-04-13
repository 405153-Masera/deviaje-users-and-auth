package masera.deviajeusersandauth.repositories;

import java.util.List;
import java.util.Optional;
import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Conecta la aplicación con la base de datos para manejar usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

  /**
   * Busca un usuario por su nombre de usuario.
   *
   * @param username el nombre de usuario.
   * @return un {@link UserEntity}
   */
  Optional<UserEntity> findByUsername(String username);

  /**
   * Busca un usuario por su email.
   *
   * @param email el email del usuario.
   * @return un {@link UserEntity}
   */
  Optional<UserEntity> findByEmail(String email);

  /**
   * Verifica si existe un usuario por su nombre de usuario.
   *
   * @param username el nombre de usuario.
   * @return true si existe, false en caso contrario.
   */
  Boolean existsByUsername(String username);

  /**
   * Verifica si existe un usuario por su email.
   *
   * @param email el email del usuario.
   * @return true si existe, false en caso contrario.
   */
  Boolean existsByEmail(String email);

  /**
   * Busca un usuario por su id y lo carga junto con sus roles.
   *
   * @param id el identificador de un usuario.
   * @return un {@link UserEntity}
   */
  @Query("SELECT u FROM UserEntity u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.id = :id")
  Optional<UserEntity> findByIdWithRoles(Integer id);

  /**
   * Busca una lista de usuarios por el rol.
   *
   * @param roleName el nombre del rol.
   * @return una lista de {@link UserEntity}
   */
  @Query("SELECT DISTINCT u FROM UserEntity u JOIN FETCH u.userRoles ur "
          + "JOIN FETCH ur.role r WHERE r.description = :roleName")
  List<UserEntity> findAllByRoleName(String roleName);
}
