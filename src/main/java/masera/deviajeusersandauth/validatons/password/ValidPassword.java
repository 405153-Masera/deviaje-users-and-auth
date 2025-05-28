package masera.deviajeusersandauth.validatons.password;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para validar contraseñas.
 * Esta anotación se utiliza para aplicar validaciones específicas a los campos de contraseña.
 */
@Documented//para que esta interfaz sea usada como una anotación
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidPassword {

  /**
   * Mensaje de error que se mostrará si la validación falla.
   *
   * @return El mensaje de error por defecto.
   */
  String message() default "Invalid Password";

  /**
   * Permite agrupar las validaciones.
   *
   * @return Un arreglo de clases de grupos de validación.
  */
  Class<?>[] groups() default {};

  /**
   * Permite asociar metadatos adicionales a la anotación.
   *
   * @return Un arreglo de clases de carga de datos.
   */
  Class<? extends Payload>[] payload() default {};
}
