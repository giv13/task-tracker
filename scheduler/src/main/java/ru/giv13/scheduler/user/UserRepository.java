package ru.giv13.scheduler.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = """
        SELECT t.id, t.name, t.notes, (CASE WHEN t.completed_at IS NULL THEN false ELSE true END) AS is_done,
               u.id AS user_id, u.name AS user_name, u.email AS user_email
        FROM task t
        LEFT JOIN "user" u
        ON u.id = t.user_id
        WHERE u.is_unsubscribed = false AND (t.completed_at IS NULL OR t.completed_at > :date)
    """, resultSetExtractorClass = UserWithTaskSummariesExtractor.class)
    List<User> findAllWithTaskSummaries(@Param("date") Instant date);
}
