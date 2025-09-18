package ru.giv13.tasktracker.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public TaskResponseDto create(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        return taskService.create(taskRequestDto);
    }

    @PutMapping("{id}")
    public TaskResponseDto update(@PathVariable("id") Integer id, @Valid @RequestBody TaskRequestDto taskRequestDto) {
        return taskService.update(id, taskRequestDto);
    }

    @PatchMapping("{id}")
    public void sort(@PathVariable("id") Integer id, @Valid @RequestBody TaskSortDto taskSortDto) {
        taskService.sort(id, taskSortDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
        taskService.delete(id);
    }
}
