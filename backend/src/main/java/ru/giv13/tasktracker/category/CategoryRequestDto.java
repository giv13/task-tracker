package ru.giv13.tasktracker.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.validation.Unique;

@Getter
@Setter
public class CategoryRequestDto {
    @NotBlank
    @Unique(repository=CategoryRepository.class, field="name", userConstraint = true)
    private String name;

    private Integer color;
}
