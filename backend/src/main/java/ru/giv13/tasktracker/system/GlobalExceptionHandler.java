package ru.giv13.tasktracker.system;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Map<String, List<String>>> onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(StringUtils.capitalize(Optional.ofNullable(error.getDefaultMessage()).orElse("")));
        });
        return Response.er(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (body instanceof Response) {
            response.setStatusCode(HttpStatus.valueOf(((Response<?>) body).status()));
            return body;
        }
        return Response.ok(body);
    }
}
