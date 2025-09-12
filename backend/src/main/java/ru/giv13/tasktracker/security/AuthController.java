package ru.giv13.tasktracker.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.giv13.tasktracker.user.User;
import ru.giv13.tasktracker.user.UserRequestDto;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public User register(@Valid @RequestBody UserRequestDto userRequestDto) {
        return authService.register(userRequestDto);
    }
}
