package ru.giv13.scheduler.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
    private boolean isUnsubscribed;
    private Integer completed;
    private Integer uncompleted;
}
