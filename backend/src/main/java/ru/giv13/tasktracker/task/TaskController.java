package ru.giv13.tasktracker.task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("tasks")
public class TaskController {
    @GetMapping
    public Map<String, String> getAll() {
        return Map.of("test", "test");
    }
}
