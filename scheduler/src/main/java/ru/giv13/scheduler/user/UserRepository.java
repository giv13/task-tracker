package ru.giv13.scheduler.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = """
        SELECT t.id, t.name, t.notes, (CASE WHEN t.completed_at IS NULL THEN false ELSE true END) AS is_done,
               u.id AS user_id, u.name AS user_name, u.email AS user_email
        FROM task t
        LEFT JOIN "user" u
        ON u.id = t.user_id
        WHERE u.is_unsubscribed = false
        AND (DATE_TRUNC('hour', NOW() AT TIME ZONE u.timezone) + FLOOR(EXTRACT(MINUTE FROM NOW() AT TIME ZONE u.timezone) / 30) * INTERVAL '30 minutes')::time = '00:00:00'
        AND (t.completed_at IS NULL OR t.completed_at >= DATE_TRUNC('hour', NOW()) + FLOOR(EXTRACT(MINUTE FROM NOW()) / 30) * INTERVAL '30 minutes' - INTERVAL '1 day')
    """, resultSetExtractorClass = UserWithTaskSummariesExtractor.class)
    List<User> findAllWithTaskSummaries();
}
