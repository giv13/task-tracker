package ru.giv13.tasktracker.color;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import ru.giv13.tasktracker.validation.Unique;

@Getter
@GroupSequence({ ColorRequestDto.class, ColorRequestDto.FirstGroup.class, ColorRequestDto.SecondGroup.class })
public class ColorRequestDto {
    public interface FirstGroup {}
    public interface SecondGroup {}

    @NotBlank(groups = FirstGroup.class)
    @Pattern(regexp = "^#[A-Fa-f0-9]{6}$", message = "Должно быть HEX-кодом", groups = FirstGroup.class)
    @Unique(repository=ColorRepository.class, field="color", userConstraint = true, groups = SecondGroup.class)
    private String color;

    public void setColor(String color) {
        this.color = color.toLowerCase();
    }
}
