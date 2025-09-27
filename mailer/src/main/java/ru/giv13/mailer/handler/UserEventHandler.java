package ru.giv13.mailer.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.giv13.common.event.UserLoggedInEvent;
import ru.giv13.common.event.UserRegisteredEvent;
import ru.giv13.common.event.UserTaskSummaryEvent;
import ru.giv13.mailer.mail.Mail;
import ru.giv13.mailer.mail.MailService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEventHandler {
    private final MailService mailService;

    @KafkaListener(topics = "${spring.kafka.topics.user.registered}")
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        mailService.sendEmail(() -> new Mail(
                "user/registered.ftlh",
                userRegisteredEvent.getEmail(),
                "Добро пожаловать!",
                Map.of(
                        "userName", userRegisteredEvent.getName(),
                        "userEmail", userRegisteredEvent.getEmail(),
                        "userPassword", userRegisteredEvent.getPassword()
                )
        ));
    }

    @KafkaListener(topics = "${spring.kafka.topics.user.logged-in}")
    public void userLoggedIn(UserLoggedInEvent userLoggedInEvent) {
        mailService.sendEmail(() -> new Mail(
                "user/logged-in.ftlh",
                userLoggedInEvent.getEmail(),
                "Уведомление о входе",
                Map.of(
                        "userName", userLoggedInEvent.getName(),
                        "userIp", userLoggedInEvent.getIp()
                )
        ));
    }

    @KafkaListener(topics = "${spring.kafka.topics.user.task-summary}")
    public void userTaskSummary(UserTaskSummaryEvent userTaskSummaryEvent) {
        mailService.sendEmail(() -> new Mail(
                "user/task-summary.ftlh",
                userTaskSummaryEvent.getEmail(),
                "Итоги дня",
                Map.of(
                        "userName", userTaskSummaryEvent.getName(),
                        "completed", userTaskSummaryEvent.getCompleted().toString(),
                        "uncompleted", userTaskSummaryEvent.getUncompleted().toString()
                )
        ));
    }
}
