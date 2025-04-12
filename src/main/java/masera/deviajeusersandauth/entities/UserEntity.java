package masera.deviajeusersandauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class {@code UserEntity} represents a user in the app.
 * It is mapped to the "users" table in the database.
 */
@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

  @Id
  private Integer id;
}
