package ru.giv13.tasktracker.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "{ru.giv13.infocrm.validation.Password.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
