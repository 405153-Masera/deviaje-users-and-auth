package masera.deviajeusersandauth.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO que representa la membrecía de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMembershipDto {

  private Integer id;
  private Integer userId;
  private MembershipDto membership;
  private Integer currentPoints;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
