package ru.giv13.tasktracker.timezone;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class TimeZoneService {
    public List<String> getAll() {
        LocalDateTime now = LocalDateTime.now();
        return ZoneId
                .getAvailableZoneIds()
                .stream()
                .map(zoneId -> String.format(
                        "%s%s",
                        "UTC",
                        now.atZone(ZoneId.of(zoneId)).getOffset().getId().replace("Z", "+00:00")
                ))
                .distinct()
                .sorted()
                .toList();
    }
}
