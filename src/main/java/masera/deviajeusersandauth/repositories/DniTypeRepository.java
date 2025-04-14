package masera.deviajeusersandauth.repositories;

import java.util.Optional;
import masera.deviajeusersandauth.entities.DniTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Conecta la aplicación con la base de datos para manejar tipos de DNI.
 */
@Repository
public interface DniTypeRepository extends JpaRepository<DniTypeEntity, Integer> {

  /**
   * Busca un tipo de DNI por su descripción.
   *
   * @param description la descripción del tipo de DNI.
   * @return un {@link DniTypeEntity}
   */
  Optional<DniTypeEntity> findByDescription(String description);
}
