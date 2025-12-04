package masera.deviajeusersandauth.services.interfaces;

import java.util.List;
import masera.deviajeusersandauth.dtos.reviews.ReviewCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewStatsDto;
import org.springframework.stereotype.Service;

/**
 * Interfaz del servicio de reviews de la plataforma.
 */
@Service
public interface ReviewService {

  /**
   * Crea una nueva review.
   * Solo CLIENTE y AGENTE pueden crear reviews.
   *
   * @param request datos de la review.
   * @param userId username del usuario que crea la review.
   * @return la review creada.
   */
  ReviewDto createReview(ReviewCreateRequest request, Integer userId);

  /**
   * Obtiene todas las reviews ordenadas por fecha.
   *
   * @return lista de reviews.
   */
  List<ReviewDto> getAllReviews();

  /**
   * Obtiene reviews por categoría.
   *
   * @param category categoría de la review.
   * @return lista de reviews de esa categoría.
   */
  List<ReviewDto> getReviewsByCategory(String category);

  /**
   * Obtiene reviews de un usuario específico.
   *
   * @param userId ID del usuario.
   * @return lista de reviews del usuario.
   */
  List<ReviewDto> getReviewsByUser(Integer userId);

  /**
   * Obtiene una review por ID con sus respuestas.
   *
   * @param id ID de la review.
   * @return la review con sus respuestas.
   */
  ReviewDto getReviewById(Long id);

  /**
   * Elimina una review.
   * Sin control de permisos, cualquiera puede borrar.
   *
   * @param id id de la review a eliminar.
   */
  void deleteReview(Long id);

  /**
   * Crea una respuesta a una review.
   * Cualquier usuario registrado puede responder.
   *
   * @param reviewId ID de la review.
   * @param request datos de la respuesta.
   * @param username username del usuario que responde.
   * @return la respuesta creada.
   */
  ReviewResponseDto createReviewResponse(
          Long reviewId, ReviewResponseCreateRequest request, String username);

  /**
   * Elimina una respuesta a una review.
   * Sin control de permisos, cualquiera puede borrar.
   *
   * @param responseId id de la respuesta a eliminar.
   */
  void deleteReviewResponse(Long responseId);

  /**
   * Obtiene estadísticas de reviews.
   *
   * @return estadísticas generales.
   */
  ReviewStatsDto getReviewStats();
}