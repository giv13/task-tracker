package ru.giv13.tasktracker.timezone;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("timezones")
@RequiredArgsConstructor
public class TimeZoneController {
    private final TimeZoneService timeZoneService;

    @GetMapping
    public List<String> getAll() {
        return timeZoneService.getAll();
    }
}
