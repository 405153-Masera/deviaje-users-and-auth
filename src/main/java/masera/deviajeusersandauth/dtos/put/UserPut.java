package masera.deviajeusersandauth.dtos.put;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajeusersandauth.validatons.adults.Adult;

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

  @Pattern(
          regexp = "^(MALE|FEMALE|UNSPECIFIED)$",
          message = "Gender must be MALE, FEMALE, UNSPECIFIED"
  )
  private String gender;

  private String phone;

  @Adult
  private LocalDate birthDate;
  private Set<Integer> roleIds;
  private Integer membershipId;
  private String avatarUrl;
  private Boolean active;
  private Integer lastUpdatedUser;
}
