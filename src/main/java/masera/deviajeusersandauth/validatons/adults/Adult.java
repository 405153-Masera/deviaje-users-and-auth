package masera.deviajeusersandauth.validatons.adults;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para validar que un usuario es mayor de edad.
 */
@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {

  /**
   * Mensaje de error que se mostrará si la validación falla.
   *
   * @return el mensaje de error
   */
  String message() default "User must be at least 18 years old";

  /**
   * Grupos de validación a los que pertenece esta anotación.
   *
   * @return los grupos de validación
   */
  Class<?>[] groups() default {};

  /**
   * Carga de datos adicional que se puede asociar a esta anotación.
   *
   * @return la carga de datos
   */
  Class<? extends Payload>[] payload() default {};
}
