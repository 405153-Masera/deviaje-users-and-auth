package masera.deviajeusersandauth.controllers;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.dtos.reviews.ReviewCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseCreateRequest;
import masera.deviajeusersandauth.dtos.reviews.ReviewResponseDto;
import masera.deviajeusersandauth.dtos.reviews.ReviewStatsDto;
import masera.deviajeusersandauth.services.interfaces.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para gestionar reviews de la plataforma.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  /**
   * Crea una nueva review.
   * Solo CLIENTE y AGENTE pueden crear reviews.
   *
   * @param request datos de la review.
   * @param userId usuario autenticado.
   * @return la review creada.
   */
  @PostMapping
  public ResponseEntity<ReviewDto> createReview(
          @Valid @RequestBody ReviewCreateRequest request,
          @RequestParam Integer userId) {
    ReviewDto review = reviewService.createReview(request, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(review);
  }

  /**
   * Obtiene todas las reviews o filtra por categoría.
   *
   * @param category categoría opcional para filtrar.
   * @return lista de reviews.
   */
  @GetMapping
  public ResponseEntity<List<ReviewDto>> getReviews(
          @RequestParam(required = false) String category) {
    List<ReviewDto> reviews;
    if (category != null && !category.isEmpty()) {
      reviews = reviewService.getReviewsByCategory(category);
    } else {
      reviews = reviewService.getAllReviews();
    }
    return ResponseEntity.ok(reviews);
  }

  /**
   * Obtiene una review por ID.
   *
   * @param id ID de la review.
   * @return la review.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
    ReviewDto review = reviewService.getReviewById(id);
    return ResponseEntity.ok(review);
  }

  /**
   * Obtiene reviews de un usuario específico.
   *
   * @param userId ID del usuario.
   * @return lista de reviews del usuario.
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Integer userId) {
    List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
    return ResponseEntity.ok(reviews);
  }

  /**
   * Elimina una review.
   * Sin control de permisos.
   *
   * @param id id de la review a eliminar.
   * @return mensaje de confirmación.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteReview(@PathVariable Long id) {
    reviewService.deleteReview(id);
    return ResponseEntity.ok(new MessageResponse(
            "Review eliminada correctamente", true));
  }

  /**
   * Crea una respuesta a una review.
   * Cualquier usuario registrado puede responder.
   *
   * @param reviewId ID de la review.
   * @param request datos de la respuesta.
   * @param principal usuario autenticado.
   * @return la respuesta creada.
   */
  @PostMapping("/{reviewId}/responses")
  public ResponseEntity<ReviewResponseDto> createReviewResponse(
          @PathVariable Long reviewId,
          @Valid @RequestBody ReviewResponseCreateRequest request,
          Principal principal) {
    ReviewResponseDto response = reviewService.createReviewResponse(
            reviewId, request, principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Elimina una respuesta a una review.
   * Sin control de permisos.
   *
   * @param responseId id de la respuesta a eliminar.
   * @return mensaje de confirmación.
   */
  @DeleteMapping("/responses/{responseId}")
  public ResponseEntity<MessageResponse> deleteReviewResponse(@PathVariable Long responseId) {
    reviewService.deleteReviewResponse(responseId);
    return ResponseEntity.ok(new MessageResponse(
            "Respuesta eliminada correctamente", true));
  }

  /**
   * Obtiene estadísticas de reviews.
   *
   * @return estadísticas generales.
   */
  @GetMapping("/stats")
  public ResponseEntity<ReviewStatsDto> getReviewStats() {
    ReviewStatsDto stats = reviewService.getReviewStats();
    return ResponseEntity.ok(stats);
  }
}