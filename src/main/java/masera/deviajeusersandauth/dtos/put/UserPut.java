package masera.deviajeusersandauth.dtos.put;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE or OTHER")
  private String gender;

  private String phone;
  private LocalDate birthDate;
  private Set<Integer> roleIds;
  private Integer membershipId;
  private String avatarUrl;
  private Boolean active;
  private Integer lastUpdatedUser;
}
