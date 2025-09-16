package ru.giv13.tasktracker.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.giv13.tasktracker.color.Color;
import ru.giv13.tasktracker.user.User;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uq_category_name_user_id", columnNames = { "name", "user_id" })
})
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @ColumnDefault("0")
    private Integer index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_category_color_id"))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_category_user_id"))
    @JsonIgnore
    private User user;
}
