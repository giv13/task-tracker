package ru.giv13.tasktracker.category;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.giv13.tasktracker.security.UserAwareRepository;

import java.util.List;

public interface CategoryRepository extends UserAwareRepository<Category, Integer> {
    @Override
    @EntityGraph(attributePaths = "color")
    List<Category> findAllByUserId(Integer userId);

    boolean existsByNameAndIdNotAndUserId(String name, Integer id, Integer userId);
}
