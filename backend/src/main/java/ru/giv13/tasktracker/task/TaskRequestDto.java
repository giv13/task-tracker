package ru.giv13.tasktracker.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {
    @NotBlank
    private String name;

    private String notes;

    private boolean isDone;

    private Integer color;

    @NotNull
    private Integer category;
}
