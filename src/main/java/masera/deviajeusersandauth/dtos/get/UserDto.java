package masera.deviajeusersandauth.dtos.get;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Integer id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private String dni;
  private DniTypeDto dniType;
  private Boolean active;
  private String avatarUrl;
  private List<RoleDto> roles;
  private UserMembershipDto membership;
}
