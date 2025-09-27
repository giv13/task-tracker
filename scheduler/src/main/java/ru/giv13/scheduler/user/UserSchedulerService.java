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
    private final KafkaTemplate<Integer, UserTaskSummaryEvent> kafkaUserRegisteredTemplate;

    @Value("${spring.kafka.topics.user.task-summary}")
    private String userTaskSummaryTopicName;

    @Scheduled(cron = "0 0 0 * * ?")
    public void taskSummaries() {
        List<User> users = userRepository.findAllWithTaskSummaries(Instant.now().minus(Duration.ofDays(1)));
        for (User user : users) {
            if (user.getCompleted() > 0 || user.getUncompleted() > 0) {
                UserTaskSummaryEvent userTaskSummaryEvent = new UserTaskSummaryEvent()
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .setCompleted(user.getCompleted())
                        .setUncompleted(user.getUncompleted());
                kafkaUserRegisteredTemplate.send(userTaskSummaryTopicName, user.getId(), userTaskSummaryEvent);
            }
        }
    }
}
