package masera.deviajeusersandauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un pasaporte de usuario.
 * Contiene información sobre el pasaporte, incluyendo el número, fecha de expiración,
 * país de emisión, nacionalidad y metadatos de creación y actualización.
 */
@Entity
@Table(name = "passports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(name = "passport_number", nullable = false, unique = true)
  private String passportNumber;

  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  @Column(name = "issuance_country", length = 2)
  private String issuanceCountry;

  @Column(name = "nationality", length = 2)
  private String nationality;

  @Column(name = "created_datetime")
  private LocalDateTime createdDatetime;

  @Column(name = "created_user")
  private Integer createdUser;

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime;

  @Column(name = "last_updated_user")
  private Integer lastUpdatedUser;

  /**
   * Metodo que se ejecuta antes de persistir la entidad.
   */
  @PrePersist
  protected void onCreate() {
    createdDatetime = LocalDateTime.now();
    lastUpdatedDatetime = LocalDateTime.now();
  }

  /**
   * Metodo que se ejecuta antes de actualizar la entidad.
   */
  @PreUpdate
  protected void onUpdate() {
    lastUpdatedDatetime = LocalDateTime.now();
  }
}