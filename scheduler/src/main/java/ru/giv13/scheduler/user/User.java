package ru.giv13.scheduler.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
    private final List<Task> completed = new ArrayList<>();
    private final List<Task> uncompleted = new ArrayList<>();

    @Getter
    @Setter
    public static class Task {
        @Id
        private Integer id;
        private String name;
        private String notes;
    }
}
