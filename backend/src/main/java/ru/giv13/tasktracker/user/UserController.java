package ru.giv13.tasktracker.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Профиль")
public class UserController {
    private final UserService userService;

    @PutMapping
    @Operation(summary = "Обновить профиль")
    public UserResponseDto update(@Valid @RequestBody UserProfileDto userProfileDto) {
        return userService.update(userProfileDto);
    }
}
