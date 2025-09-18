package ru.giv13.tasktracker.category;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.giv13.tasktracker.security.UserAwareRepository;

import java.util.List;

public interface CategoryRepository extends UserAwareRepository<Category, Integer> {
    @EntityGraph(attributePaths = { "color", "tasks.color" })
    List<Category> findAllByUserId(Integer userId, Sort sort);

    @Query("SELECT COALESCE(MAX(c.index) + 1, 0) FROM Category c WHERE c.user.id = :userId")
    Integer getNextIndexByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT MAX(indexes.index) FROM (SELECT c.index FROM category c WHERE c.index > :index AND c.user_id = :userId ORDER BY c.index ASC LIMIT :offset) indexes", nativeQuery = true)
    Integer getMaxOffsetIndex(@Param("index") Integer index, @Param("offset") Integer offset, @Param("userId") Integer userId);

    @Query(value = "SELECT MIN(indexes.index) FROM (SELECT c.index FROM category c WHERE c.index < :index AND c.user_id = :userId ORDER BY c.index DESC LIMIT :offset) indexes", nativeQuery = true)
    Integer getMinOffsetIndex(@Param("index") Integer index, @Param("offset") Integer offset, @Param("userId") Integer userId);

    @Modifying
    @Query(value = "UPDATE category SET index = index + :change WHERE index BETWEEN :from AND :to AND user_id = :userId", nativeQuery = true)
    void changeIndexes(@Param("change") Integer change, @Param("from") Integer from, @Param("to") Integer to, @Param("userId") Integer userId);

    boolean existsByNameAndIdNotAndUserId(String name, Integer id, Integer userId);
}