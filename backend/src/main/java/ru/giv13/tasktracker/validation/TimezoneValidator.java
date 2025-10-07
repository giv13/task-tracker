package ru.giv13.tasktracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;

public class TimezoneValidator implements ConstraintValidator<Timezone, String> {
    @Override
    public boolean isValid(String timezone, ConstraintValidatorContext context) {
        if (timezone == null || timezone.isBlank()) {
            return true;
        }
        return ZoneId.getAvailableZoneIds().contains(timezone);
    }
}
