package masera.deviajeusersandauth.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase {@code MembershipEntity} representa una membresía.
 * Referencia a la tabla llamada "memberships".
 */
@Entity
@Table(name = "memberships")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String description;

  private BigDecimal cost;

  @Column(name = "discount_percentage")
  private BigDecimal discountPercentage;

  @Column(name = "created_datetime")
  private LocalDateTime createdDatetime;

  @Column(name = "created_user")
  private Integer createdUser;

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime;

  @Column(name = "last_updated_user")
  private Integer lastUpdatedUser;

  @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
  private Set<UserMembershipEntity> userMemberships = new HashSet<>();

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
