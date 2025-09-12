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
@PasswordConfirmation(groups = UserRequestDto.FirstGroupRegister.class)
public class UserRequestDto implements PasswordConfirmable {
    @GroupSequence({ UserRequestDto.FirstGroupRegister.class, UserRequestDto.SecondGroup.class })
    public interface register {}
    @GroupSequence({ UserRequestDto.FirstGroupLogin.class })
    public interface login {}

    public interface FirstGroupRegister extends FirstGroupLogin {}
    public interface FirstGroupLogin {}
    public interface SecondGroup {}

    @NotBlank(groups = FirstGroupRegister.class)
    @Size(max=50, groups = FirstGroupRegister.class)
    private String name;

    @NotBlank(groups = FirstGroupLogin.class)
    @Email(groups = FirstGroupLogin.class)
    @Size(max=50, groups = FirstGroupLogin.class)
    @Unique(repository=UserRepository.class, field="email", groups = SecondGroup.class)
    private String email;

    @NotBlank(groups = FirstGroupLogin.class)
    @Password(groups = FirstGroupRegister.class)
    private String password;

    private String passwordConfirmation;
}
