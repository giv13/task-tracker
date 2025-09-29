package ru.giv13.tasktracker.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.validation.Password;
import ru.giv13.tasktracker.validation.PasswordConfirmable;
import ru.giv13.tasktracker.validation.PasswordConfirmation;

@Getter
@Setter
@PasswordConfirmation
public class UserProfileDto implements PasswordConfirmable {
    @NotBlank
    @Size(max=50)
    private String name;

    @Password
    private String password;

    private String passwordConfirmation;

    @NotNull
    private boolean isUnsubscribed;
}
