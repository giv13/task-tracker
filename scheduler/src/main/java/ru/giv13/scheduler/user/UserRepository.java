package ru.giv13.scheduler.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("""
        SELECT u.id, u.name, u.email,
               COUNT(CASE WHEN t.completed_at > :date THEN 1 END) as completed,
               COUNT(CASE WHEN t.completed_at IS NULL THEN 1 END) as uncompleted
        FROM "user" u
        LEFT JOIN task t
        ON u.id = t.user_id
        GROUP BY u.id
    """)
    List<User> findAllWithTaskSummaries(@Param("date") Instant date);
}
