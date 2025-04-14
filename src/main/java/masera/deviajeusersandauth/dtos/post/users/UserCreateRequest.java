package masera.deviajeusersandauth.dtos.post.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO que representa la solicitud para crear un usuario.
 * Este registro lo realiza el Administrador y Gerente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserCreateRequest extends UserBase {

  private Set<Integer> roleIds;
  private Integer createdUser;
}
