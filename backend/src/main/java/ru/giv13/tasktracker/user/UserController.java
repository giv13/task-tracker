package ru.giv13.tasktracker.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    public UserResponseDto update(@Valid @RequestBody UserProfileDto userProfileDto) {
        return userService.update(userProfileDto);
    }
}
