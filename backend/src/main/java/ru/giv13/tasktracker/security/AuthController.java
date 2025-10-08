package ru.giv13.tasktracker.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.giv13.tasktracker.user.UserRequestDto;
import ru.giv13.tasktracker.user.UserResponseDto;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    @Operation(summary = "Регистрация в системе")
    public UserResponseDto register(@Validated(UserRequestDto.register.class) @RequestBody UserRequestDto userRequestDto) {
        return authService.register(userRequestDto);
    }

    @PostMapping("login")
    @Operation(summary = "Вход в систему")
    public UserResponseDto login(@Validated(UserRequestDto.login.class) @RequestBody UserRequestDto userRequestDto) {
        return authService.login(userRequestDto);
    }

    @PostMapping("refresh")
    @Operation(summary = "Получение нового JWT по Refresh-токену")
    public void refresh(HttpServletRequest request) {
        authService.refresh(request);
    }

    @PostMapping("logout")
    @Operation(summary = "Выход из системы")
    public void logout() {
        authService.logout();
    }
}
