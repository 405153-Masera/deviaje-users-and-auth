package masera.deviajeusersandauth.dtos.post.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para crear un usuario.
 * Este registro lo realiza el Cliente.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignupRequest extends UserBase{
}
