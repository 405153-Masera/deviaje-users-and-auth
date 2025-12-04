package masera.deviajeusersandauth.repositories;

import java.util.List;
import masera.deviajeusersandauth.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la reviews de la plataforma.
 */
@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  /**
   * Obtiene todas las reviews ordenadas por fecha de creación descendente.
   */
  List<ReviewEntity> findAllByOrderByCreatedDatetimeDesc();

  /**
   * Obtiene reviews por categoría.
   */
  List<ReviewEntity> findByCategoryOrderByCreatedDatetimeDesc(
          ReviewEntity.ReviewCategory category);

  /**
   * Obtiene reviews por usuario.
   */
  List<ReviewEntity> findByUserIdOrderByCreatedDatetimeDesc(Integer userId);

  /**
   * Calcula el rating promedio de todas las reviews.
   */
  @Query("SELECT AVG(r.rating) FROM ReviewEntity r")
  Double calculateAverageRating();

  /**
   * Calcula el rating promedio por categoría.
   */
  @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.category = :category")
  Double calculateAverageRatingByCategory(
          @Param("category") ReviewEntity.ReviewCategory category);

  /**
   * Cuenta el total de reviews.
   */
  @Query("SELECT COUNT(r) FROM ReviewEntity r")
  Long countAllReviews();
}