package masera.deviajeusersandauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una review sobre el uso de la plataforma DeViaje.
 * Solo CLIENTE y AGENTE pueden crear reviews.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(nullable = false)
  private Integer rating; // 1 a 5

  @Column(columnDefinition = "TEXT", nullable = false)
  private String comment;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ReviewCategory category;

  private LocalDateTime createdDatetime;

  private LocalDateTime lastUpdatedDatetime;

  @OneToMany(mappedBy = "review")
  @Builder.Default
  private List<ReviewResponseEntity> responses = new ArrayList<>();

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

  /**
   * Categorias d reviews.
   */
  public enum ReviewCategory {
    USABILITY,
    SEARCHES,
    BOOKING_PROCESS,
    PERFORMANCE,
    GENERAL
  }
}