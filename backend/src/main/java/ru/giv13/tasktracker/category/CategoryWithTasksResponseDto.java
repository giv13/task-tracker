package ru.giv13.tasktracker.category;

import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.color.ColorResponseDto;
import ru.giv13.tasktracker.task.TaskResponseDto;

import java.util.List;

@Getter
@Setter
public class CategoryWithTasksResponseDto {
    private Integer id;
    private String name;
    private ColorResponseDto color;
    private List<TaskResponseDto> tasks;
}
