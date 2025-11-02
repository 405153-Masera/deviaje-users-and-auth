package masera.deviajeusersandauth.validatons.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

/**
 * Validador de contraseñas que implementa las reglas de seguridad
 * definidas en la anotación ValidPassword.
 * Utiliza la biblioteca Passay para validar las contraseñas según las reglas especificadas.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(ValidPassword arg0) {

  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                //new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                //new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                //new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
                new WhitespaceRule()
        ));
    RuleResult result = validator.validate(new PasswordData(password));

    if (result.isValid()) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(
                        String.join(", ", validator.getMessages(result)))
                .addConstraintViolation();
    return false;
  }
}
