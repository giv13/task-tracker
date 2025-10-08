package ru.giv13.tasktracker.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Создать задачу")
    public TaskResponseDto create(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        return taskService.create(taskRequestDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Обновить задачу")
    public TaskResponseDto update(
            @PathVariable("id") @Parameter(description = "Идентификатор задачи") Integer id,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        return taskService.update(id, taskRequestDto);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Сортировать задачу", description = "Изменение порядка (индекса) задачи, а также ее родительской категории")
    public void sort(
            @PathVariable("id") @Parameter(description = "Идентификатор задачи") Integer id,
            @Valid @RequestBody TaskSortDto taskSortDto
    ) {
        taskService.sort(id, taskSortDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить задачу")
    public void delete(@PathVariable("id") @Parameter(description = "Идентификатор задачи") Integer id) {
        taskService.delete(id);
    }
}
