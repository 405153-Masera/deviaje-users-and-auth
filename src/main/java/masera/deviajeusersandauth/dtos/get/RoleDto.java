package masera.deviajeusersandauth.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un rol.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

  private Integer id;
  private String description;
}
