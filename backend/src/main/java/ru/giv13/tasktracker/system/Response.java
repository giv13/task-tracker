package ru.giv13.tasktracker.system;

import org.springframework.http.HttpStatus;

public record Response<T>(boolean success, int status, T error, T data, long timestamp) {
    public static <T> Response<T> ok(T data) {
        return new Response<>(true, HttpStatus.OK.value(), null, data, System.currentTimeMillis());
    }

    public static <T> Response<T> er(T error, HttpStatus status) {
        return new Response<>(false, status.value(), error, null, System.currentTimeMillis());
    }
}
