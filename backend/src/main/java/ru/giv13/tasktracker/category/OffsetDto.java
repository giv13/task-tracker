package ru.giv13.tasktracker.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffsetDto {
    @NotNull
    private Integer offset;
}
