package ru.giv13.tasktracker.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConfirmationValidator.class)
public @interface PasswordConfirmation {
    String message() default "{ru.giv13.infocrm.validation.PasswordConfirmation.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
