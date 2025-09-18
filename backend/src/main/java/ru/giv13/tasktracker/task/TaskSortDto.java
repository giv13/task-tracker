package ru.giv13.tasktracker.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSortDto {
    @NotNull
    private Integer offset;
    private Integer category;
}
