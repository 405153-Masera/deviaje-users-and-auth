package masera.deviajeusersandauth.dtos.post.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para crear un usuario.
 * Este registro lo realiza el Administrador y Gerente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserCreateRequest extends UserBase {

  @NotEmpty(message = "The role IDs cannot be empty")
  private Set<Integer> roleIds;

  @NotNull(message = "The created user ID cannot be null")
  private Integer createdUser;
}
