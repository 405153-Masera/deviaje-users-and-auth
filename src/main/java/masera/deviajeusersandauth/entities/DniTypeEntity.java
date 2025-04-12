package masera.deviajeusersandauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase {@code DniTypeEntity} representa un tipo de DNI en la aplicación.
 */
@Entity
@Table(name = "dni_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DniTypeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String description;

  @Column(name = "created_datetime")
  private LocalDateTime createdDateTime;

  @Column(name = "created_user")
  private Integer createdUser;

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDateTime;

  @Column(name = "last_updated_user")
  private Integer lastUpdatedUser;
}
