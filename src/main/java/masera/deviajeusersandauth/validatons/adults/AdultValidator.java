package masera.deviajeusersandauth.validatons.adults;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

/**
 * Validador personalizado para verificar si una fecha de
 * nacimiento corresponde a un adulto (18 años o más).
 * Si la fecha de nacimiento es nula, se considera válida
 * (puedes cambiar esto según tus necesidades).
 */
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
    if (birthDate == null) {
      return true;
    }

    return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
  }
}