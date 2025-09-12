package ru.giv13.tasktracker.user;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.validation.Password;
import ru.giv13.tasktracker.validation.PasswordConfirmable;
import ru.giv13.tasktracker.validation.PasswordConfirmation;
import ru.giv13.tasktracker.validation.Unique;

@Getter
@Setter
@GroupSequence({ UserRequestDto.class, UserRequestDto.FirstGroup.class, UserRequestDto.SecondGroup.class })
@PasswordConfirmation(groups = UserRequestDto.FirstGroup.class)
public class UserRequestDto implements PasswordConfirmable {
    public interface FirstGroup {}
    public interface SecondGroup {}

    @NotBlank(groups = FirstGroup.class)
    @Size(max=50, groups = FirstGroup.class)
    private String name;

    @NotBlank(groups = FirstGroup.class)
    @Email(groups = FirstGroup.class)
    @Size(max=50, groups = FirstGroup.class)
    @Unique(repository=UserRepository.class, field="email", groups = SecondGroup.class)
    private String email;

    @NotBlank(groups = FirstGroup.class)
    @Password(groups = FirstGroup.class)
    private String password;

    private String passwordConfirmation;
}
