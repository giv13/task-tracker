package ru.giv13.tasktracker.task;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.giv13.tasktracker.security.UserAwareRepository;

public interface TaskRepository extends UserAwareRepository<Task, Integer> {
    @Query("SELECT COALESCE(MAX(t.index) + 1, 0) FROM Task t WHERE t.category.id = :categoryId")
    Integer getNextIndexByCategoryId(@Param("categoryId") Integer categoryId);

    @Query(value = "SELECT MAX(indexes.index) FROM (SELECT c.index FROM task c WHERE c.index > :index AND c.category_id = :categoryId ORDER BY c.index ASC LIMIT :offset) indexes", nativeQuery = true)
    Integer getMaxOffsetIndex(@Param("index") Integer index, @Param("offset") Integer offset, @Param("categoryId") Integer categoryId);

    @Query(value = "SELECT MIN(indexes.index) FROM (SELECT c.index FROM task c WHERE c.index < :index AND c.category_id = :categoryId ORDER BY c.index DESC LIMIT :offset) indexes", nativeQuery = true)
    Integer getMinOffsetIndex(@Param("index") Integer index, @Param("offset") Integer offset, @Param("categoryId") Integer categoryId);

    @Modifying
    @Query(value = "UPDATE task SET index = index + :change WHERE index BETWEEN :from AND :to AND category_id = :categoryId", nativeQuery = true)
    void changeIndexes(@Param("change") Integer change, @Param("from") Integer from, @Param("to") Integer to, @Param("categoryId") Integer categoryId);
}