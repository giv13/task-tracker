package ru.giv13.tasktracker.color;

import jakarta.persistence.*;
import lombok.Data;
import ru.giv13.tasktracker.user.User;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uq_color_hex_user_id", columnNames = { "hex", "user_id" })
})
@Data
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String hex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_color_user_id"))
    private User user;
}
