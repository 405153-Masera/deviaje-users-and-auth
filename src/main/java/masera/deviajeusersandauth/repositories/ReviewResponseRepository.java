package masera.deviajeusersandauth.repositories;

import java.util.List;
import masera.deviajeusersandauth.entities.ReviewResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las respuestas a reviews.
 */
@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponseEntity, Long> {

  /**
   * Obtiene todas las respuestas de una review específica.
   */
  List<ReviewResponseEntity> findByReviewIdOrderByCreatedDatetimeAsc(Long reviewId);

  /**
   * Obtiene todas las respuestas de un usuario.
   */
  List<ReviewResponseEntity> findByUserIdOrderByCreatedDatetimeDesc(Integer userId);

  /**
   * Elimina todas las respuestas de una review (útil cuando se borra una review).
   */
  void deleteByReviewId(Long reviewId);
}