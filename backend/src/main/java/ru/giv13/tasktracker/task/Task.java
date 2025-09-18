package ru.giv13.tasktracker.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.giv13.tasktracker.category.Category;
import ru.giv13.tasktracker.color.Color;
import ru.giv13.tasktracker.user.User;

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
    private Integer index;

    @ColumnDefault("false")
    private boolean isDone;

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
}
