package ru.giv13.tasktracker.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank
    @Size(max=50)
    private String name;

    @NotBlank
    @Email
    @Size(max=50)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
