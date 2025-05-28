package masera.deviajeusersandauth.dtos.get;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private Integer id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private Boolean active;
  private String avatarUrl;
  private List<String> roles;
  private UserMembershipDto membership;
  private PassportDto passport;
}
