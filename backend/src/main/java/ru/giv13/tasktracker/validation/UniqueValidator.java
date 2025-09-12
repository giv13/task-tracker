package ru.giv13.tasktracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, String> {
    private final ApplicationContext applicationContext;
    private JpaRepository<?, ?> repository;
    private String spelExpression;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.repository = applicationContext.getBean(constraintAnnotation.repository());
        this.spelExpression = "#repository.existsBy" + StringUtils.capitalize(constraintAnnotation.field()) + "AndIdNot(#value, #id)";
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlank()) {
            return true;
        }
        try {
            StandardEvaluationContext spelContext = new StandardEvaluationContext();
            spelContext.setVariable("repository", repository);
            spelContext.setVariable("value", value);
            spelContext.setVariable("id", getIdFromRequest());
            Expression expression = new SpelExpressionParser().parseExpression(spelExpression);
            Object result = expression.getValue(spelContext);
            if (result instanceof Boolean) {
                return !(Boolean) result;
            }
            throw new ValidationException();
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate("{ru.giv13.infocrm.validation.Unique.error}")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }
    }

    private String getIdFromRequest() {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) RequestContextHolder.currentRequestAttributes().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (pathVariables != null && pathVariables.containsKey("id")) {
            return pathVariables.get("id");
        }
        return "0";
    }
}
