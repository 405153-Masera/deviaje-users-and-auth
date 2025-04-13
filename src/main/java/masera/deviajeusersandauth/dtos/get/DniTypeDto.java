package masera.deviajeusersandauth.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un tipo de dni.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DniTypeDto {

  private Integer id;
  private String description;
}
