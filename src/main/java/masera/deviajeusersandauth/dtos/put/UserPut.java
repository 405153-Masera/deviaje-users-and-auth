package masera.deviajeusersandauth.dtos.put;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa una solicitud de actualización de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPut {

  private String firstName;
  private String lastName;
  private String phone;
  private LocalDate birthDate;
  private String dni;
  private Integer dniTypeId;
  private String avatarUrl;
}
