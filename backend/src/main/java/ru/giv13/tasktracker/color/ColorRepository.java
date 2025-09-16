package ru.giv13.tasktracker.color;

import ru.giv13.tasktracker.security.UserAwareRepository;

public interface ColorRepository extends UserAwareRepository<Color, Integer> {
    boolean existsByHexAndIdNotAndUserId(String color, Integer id, Integer userId);
}
