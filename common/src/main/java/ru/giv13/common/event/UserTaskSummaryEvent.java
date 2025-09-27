package ru.giv13.common.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserTaskSummaryEvent {
    private String name;
    private String email;
    private Integer completed;
    private Integer uncompleted;
}
