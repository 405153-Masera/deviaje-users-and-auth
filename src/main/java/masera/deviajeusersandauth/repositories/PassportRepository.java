package masera.deviajeusersandauth.repositories;

import java.util.Optional;
import masera.deviajeusersandauth.entities.PassportEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para manejar las operaciones de acceso a datos relacionadas con los pasaportes.
 * Extiende JpaRepository para proporcionar métodos CRUD y consultas personalizadas.
 */
@Repository
public interface PassportRepository extends JpaRepository<PassportEntity, Integer> {

  /**
   * Encuentra el pasaporte asociado a un usuario.
   * @param user Usuario del cual se desea encontrar el pasaporte.
   * @return el pasaporte asociado al usuario, si existe.
   */
  Optional<PassportEntity> findByUser(UserEntity user);

  /**
   * Encuentra un pasaporte por su número.
   *
   * @param passportNumber Número de pasaporte a buscar.
   * @return el pasaporte con el número especificado, si existe.
   */
  Optional<PassportEntity> findByPassportNumber(String passportNumber);

  /**
   * Verifica si existe un pasaporte con el número especificado.
   *
   * @param passportNumber Número de pasaporte a verificar.
   * @return true si existe un pasaporte con ese número, false en caso contrario.
   */
  boolean existsByPassportNumber(String passportNumber);
}