package ru.giv13.tasktracker.security;

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
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public UserResponseDto register(@Validated(UserRequestDto.register.class) @RequestBody UserRequestDto userRequestDto) {
        return authService.register(userRequestDto);
    }

    @PostMapping("login")
    public UserResponseDto login(@Validated(UserRequestDto.login.class) @RequestBody UserRequestDto userRequestDto) {
        return authService.login(userRequestDto);
    }

    @PostMapping("refresh")
    public void refresh(HttpServletRequest request) {
        authService.refresh(request);
    }

    @PostMapping("logout")
    public void logout() {
        authService.logout();
    }
}
