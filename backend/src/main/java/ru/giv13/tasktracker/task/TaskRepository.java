package ru.giv13.tasktracker.task;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.giv13.tasktracker.security.UserAwareRepository;

import java.util.List;

public interface TaskRepository extends UserAwareRepository<Task, Integer> {
    @EntityGraph(attributePaths = "color")
    List<Task> findAllByUserId(Integer userId, Sort sort);

    @Query("SELECT COALESCE(MAX(t.index) + 1, 0) FROM Task t WHERE t.user.id = :userId")
    Integer getNextIndexByUserId(@Param("userId") Integer userId);
}