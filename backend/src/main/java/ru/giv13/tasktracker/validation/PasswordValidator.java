package ru.giv13.tasktracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Properties;
import java.util.ResourceBundle;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static String bundleBaseName;

    @Value("${spring.messages.basename}")
    private void setBundleBaseName(String value) {
        bundleBaseName = value;
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) return true;
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(
                getMessageResolver(),
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        );
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        validator.getMessages(result).forEach(message -> {
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        });
        return false;
    }

    private static MessageResolver getMessageResolver() {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleBaseName, LocaleContextHolder.getLocale());
        Properties properties = new Properties();
        bundle.keySet()
                .stream()
                .filter(key -> key.startsWith("org.passay."))
                .forEach(key -> properties.setProperty(key.replace("org.passay.", ""), bundle.getString(key)));
        return new PropertiesMessageResolver(properties);
    }
}
