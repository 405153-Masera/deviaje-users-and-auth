package masera.deviajeusersandauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una respuesta a una review.
 * Cualquier usuario registrado puede responder.
 */
@Entity
@Table(name = "reviews_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "review_id", nullable = false)
  private ReviewEntity review;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String comment;

  private LocalDateTime createdDatetime;

  private LocalDateTime lastUpdatedDatetime;

  /**
   * Metodo que se ejecuta al crear en la base de datos.
   */
  @PrePersist
  protected void onCreate() {
    createdDatetime = LocalDateTime.now();
    lastUpdatedDatetime = LocalDateTime.now();
  }

  /**
   * Metodo que se ejecuta al actualizar en la base de datos.
   */
  @PreUpdate
  protected void onUpdate() {
    lastUpdatedDatetime = LocalDateTime.now();
  }
}