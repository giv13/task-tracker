package ru.giv13.common.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserRegisteredEvent {
    private String name;

    private String email;

    private String password;
}
