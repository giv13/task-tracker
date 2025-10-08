package ru.giv13.tasktracker.timezone;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("timezones")
@RequiredArgsConstructor
@Tag(name = "Часовые пояса")
public class TimeZoneController {
    private final TimeZoneService timeZoneService;

    @GetMapping
    @Operation(summary = "Получить список часовых поясов")
    public Map<String, String> getAll() {
        return timeZoneService.getAll();
    }
}
