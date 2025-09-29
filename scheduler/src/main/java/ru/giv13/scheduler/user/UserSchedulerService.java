package ru.giv13.scheduler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.giv13.common.event.UserTaskSummaryEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSchedulerService {
    private final UserRepository userRepository;
    private final KafkaTemplate<Integer, UserTaskSummaryEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.user.task-summary}")
    private String userTaskSummaryTopicName;

    @Scheduled(cron = "0 0 0 * * ?")
    public void taskSummaries() {
        List<User> users = userRepository.findAllWithTaskSummaries(Instant.now().minus(Duration.ofDays(1)));
        for (User user : users) {
            if (!user.getCompleted().isEmpty() || !user.getUncompleted().isEmpty()) {
                UserTaskSummaryEvent userTaskSummaryEvent = new UserTaskSummaryEvent()
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .setCompleted(user.getCompleted().stream().map(task ->
                                new UserTaskSummaryEvent.Task()
                                        .setName(task.getName())
                                        .setNotes(task.getNotes())
                        ).toList())
                        .setUncompleted(user.getUncompleted().stream().map(task ->
                                new UserTaskSummaryEvent.Task()
                                        .setName(task.getName())
                                        .setNotes(task.getNotes())
                        ).toList());
                kafkaTemplate.send(userTaskSummaryTopicName, user.getId(), userTaskSummaryEvent);
            }
        }
    }
}
