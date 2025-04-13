package masera.deviajeusersandauth.dtos.get;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una membresía.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {

  private Integer id;
  private String description;
  private BigDecimal cost;
  private BigDecimal discountPercentage;
}
