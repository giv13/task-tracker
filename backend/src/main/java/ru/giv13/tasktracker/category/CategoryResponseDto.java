package ru.giv13.tasktracker.category;

import lombok.Getter;
import lombok.Setter;
import ru.giv13.tasktracker.color.ColorResponseDto;

@Getter
@Setter
public class CategoryResponseDto {
    private Integer id;
    private String name;
    private ColorResponseDto color;
}
