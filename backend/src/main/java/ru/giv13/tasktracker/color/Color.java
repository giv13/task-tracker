package ru.giv13.tasktracker.color;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import ru.giv13.tasktracker.user.User;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uq_color_color_user_id", columnNames = { "color", "user_id" })
})
@Data
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_color_user_id"))
    @JsonIgnore
    private User user;
}
