package ru.giv13.mailer.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;
import ru.giv13.common.event.UserPasswordChangedEvent;
import ru.giv13.common.event.UserRegisteredEvent;
import ru.giv13.common.event.UserTaskSummaryEvent;
import ru.giv13.mailer.mail.Mail;
import ru.giv13.mailer.mail.MailService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEventHandler {
    private final MailService mailService;

    @Value("${encryptor.password}")
    private String encryptorPassword;

    @Value("${encryptor.salt}")
    private String encryptorSalt;

    @KafkaListener(topics = "${spring.kafka.topics.user.registered}")
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        mailService.sendEmail(() -> new Mail(
                "user/registered.ftlh",
                userRegisteredEvent.getEmail(),
                "Добро пожаловать!",
                Map.of(
                        "userName", userRegisteredEvent.getName(),
                        "userEmail", userRegisteredEvent.getEmail(),
                        "userPassword", Encryptors.delux(encryptorPassword, encryptorSalt).decrypt(userRegisteredEvent.getPassword())
                )
        ));
    }

    @KafkaListener(topics = "${spring.kafka.topics.user.password-changed}")
    public void userPasswordChanged(UserPasswordChangedEvent userPasswordChangedEvent) {
        mailService.sendEmail(() -> new Mail(
                "user/password-changed.ftlh",
                userPasswordChangedEvent.getEmail(),
                "Ваш пароль изменен!",
                Map.of(
                        "userName", userPasswordChangedEvent.getName(),
                        "userEmail", userPasswordChangedEvent.getEmail(),
                        "userPassword", Encryptors.delux(encryptorPassword, encryptorSalt).decrypt(userPasswordChangedEvent.getPassword())
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
