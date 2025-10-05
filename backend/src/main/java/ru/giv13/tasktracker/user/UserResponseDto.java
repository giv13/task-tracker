package ru.giv13.tasktracker.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;
    private String timezone;
    private boolean isUnsubscribed;
}
