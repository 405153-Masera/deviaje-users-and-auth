package masera.deviajeusersandauth.repositories;

import java.util.Optional;
import masera.deviajeusersandauth.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Conecta la aplicación con la base de datos para manejar roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

  /**
   * Busca un rol por su descripción.
   *
   * @param description el nombre del rol.
   * @return un {@link RoleEntity}
   */
  Optional<RoleEntity> findByDescription(String description);
}
