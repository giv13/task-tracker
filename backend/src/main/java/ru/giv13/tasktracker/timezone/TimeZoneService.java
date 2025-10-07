package ru.giv13.tasktracker.timezone;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class TimeZoneService {
    public Map<String, String> getAll() {
        LocalDateTime now = LocalDateTime.now();
        return ZoneId
                .getAvailableZoneIds()
                .stream()
                .collect(Collectors.toMap(
                        zoneId -> String.format(
                                "UTC%s %s",
                                now.atZone(ZoneId.of(zoneId)).getOffset().getId().replace("Z", "+00:00"),
                                zoneId
                        ),
                        zoneId -> zoneId,
                        (oldValue, newValue) -> oldValue,
                        TreeMap::new
                ));
    }
}
