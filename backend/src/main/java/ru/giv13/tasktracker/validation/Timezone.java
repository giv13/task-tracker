package ru.giv13.tasktracker.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimezoneValidator.class)
public @interface Timezone {
    String message() default "{ru.giv13.infocrm.validation.Timezone.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
