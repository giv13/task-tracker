package ru.giv13.common.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class UserTaskSummaryEvent {
    private String name;
    private String email;
    private List<Task> completed;
    private List<Task> uncompleted;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Task {
        private String name;
        private String notes;
    }
}
