package masera.deviajeusersandauth.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.*;


/**
 * La clase {@code RoleEntity} representa un rol.
 * Referencia a la tabla llamada "roles".
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String description;

  @Column(name = "created_datetime")
  private LocalDateTime createdDatetime;

  @Column(name = "created_user")
  private Integer createdUser;

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime;

  @Column(name = "last_updated_user")
  private Integer lastUpdatedUser;

  @OneToMany(mappedBy = "role")
  private Set<UserRoleEntity> userRoles = new HashSet<>();

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
