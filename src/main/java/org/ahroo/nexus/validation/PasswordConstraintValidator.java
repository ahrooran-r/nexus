package org.ahroo.nexus.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.ahroo.nexus.validation.annotation.Password;
import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(

                // 1. at least 8 characters
                new LengthRule(8, 20),

                // 2. at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // 3. at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // 4. at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // 5. at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // 6. no whitespace
                new WhitespaceRule()

        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        log.trace("Validation messages: {}", messages);

        String messageTemplate = String.join(",", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}