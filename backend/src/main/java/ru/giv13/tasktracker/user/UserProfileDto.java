package ru.giv13.tasktracker.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.validation.Password;
import ru.giv13.tasktracker.validation.PasswordConfirmable;
import ru.giv13.tasktracker.validation.PasswordConfirmation;
import ru.giv13.tasktracker.validation.Timezone;

@Getter
@Setter
@PasswordConfirmation
public class UserProfileDto implements PasswordConfirmable {
    @NotBlank
    @Size(max=50)
    private String name;

    @NotBlank
    @Size(max=50)
    @Timezone
    private String timezone;

    @Password
    private String password;

    private String passwordConfirmation;

    @NotNull
    private boolean isUnsubscribed;

    public void setPassword(String password) {
        this.password = password == null || password.isBlank() ? null : password;
    }
}
