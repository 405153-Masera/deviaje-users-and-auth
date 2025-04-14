package masera.deviajeusersandauth.dtos.put;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO que representa una solicitud de actualización de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPut {

  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private String dni;
  private Integer dniTypeId;
  private Set<Integer> roleIds;
  private Integer membershipId;
  private String avatarUrl;
  private Boolean active;
  private Integer lastUpdatedUser;
}
