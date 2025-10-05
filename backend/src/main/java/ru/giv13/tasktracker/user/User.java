package ru.giv13.tasktracker.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "\"user\"")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 9, nullable = false)
    @ColumnDefault("'UTC+00:00'")
    private String timezone = "UTC+00:00";

    @Column(length = 100, nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private String password;

    @ToString.Exclude
    @JsonIgnore
    private String refresh;

    @ColumnDefault("false")
    private boolean isUnsubscribed = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
