package masera.deviajeusersandauth.repositories;

import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Conecta la aplicación con la base de datos para manejar los roles de los usuarios.
 */
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

  /**
   * Busca los roles de un usuario por su id.
   *
   * @param userId el identificador de un usuario.
   * @return una lista de {@link UserRoleEntity}
   */
  List<UserRoleEntity> findByUserId(Integer userId);

  /**
   * Borra los roles de un usuario por su id.
   *
   * @param user el usuario.
   * @param roleId el id del rol.
   */
  void deleteByUserAndRoleId(UserEntity user, Integer roleId);

  /**
   * Busca los roles de un usuario por su id y los carga junto con el rol.
   * @param userId el identificador de un usuario.
   * @return una lista de {@link UserRoleEntity}
   */
  @Query("SELECT ur FROM UserRoleEntity ur JOIN FETCH ur.role WHERE ur.user.id = :userId")
  List<UserRoleEntity> findByUserIdWithRole(Integer userId);
}
