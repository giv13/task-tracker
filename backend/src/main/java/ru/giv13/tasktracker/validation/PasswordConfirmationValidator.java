package ru.giv13.tasktracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, PasswordConfirmable> {
    @Override
    public boolean isValid(PasswordConfirmable passwordConfirmable, ConstraintValidatorContext context) {
        if (passwordConfirmable.getPassword() == null || passwordConfirmable.getPassword().equals(passwordConfirmable.getPasswordConfirmation())) {
            return true;
        }
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("passwordConfirmation")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
