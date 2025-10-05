package ru.giv13.tasktracker.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.giv13.tasktracker.category.Category;
import ru.giv13.tasktracker.color.Color;
import ru.giv13.tasktracker.user.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String notes;

    @ColumnDefault("0")
    private Integer index = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_color_id"))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_category_id"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_user_id"))
    private User user;

    @CreationTimestamp
    private Instant createdAt;

    private Instant completedAt;

    public void setDone(boolean done) {
        completedAt = done ? Instant.now() : null;
    }

    public boolean getDone() {
        return completedAt != null;
    }

    public LocalDateTime getCreatedAt() {
        if (createdAt == null) return null;
        return createdAt.atZone(ZoneId.of(user.getTimezone())).toLocalDateTime();
    }

    public LocalDateTime getCompletedAt() {
        if (completedAt == null) return null;
        return completedAt.atZone(ZoneId.of(user.getTimezone())).toLocalDateTime();
    }
}
