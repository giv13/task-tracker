package ru.giv13.tasktracker.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.color.ColorResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDto {
    private Integer id;

    private String name;

    private String notes;

    private boolean isDone;

    private ColorResponseDto color;

    @JsonProperty("category")
    private Integer categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
