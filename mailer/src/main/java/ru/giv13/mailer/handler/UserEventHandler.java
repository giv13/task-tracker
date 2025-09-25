package ru.giv13.mailer.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.giv13.common.event.UserLoggedInEvent;
import ru.giv13.common.event.UserRegisteredEvent;
import ru.giv13.mailer.mail.Mail;
import ru.giv13.mailer.mail.MailService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEventHandler {
    private final MailService mailService;

    @KafkaListener(topics = "${spring.kafka.topics.user.registered}")
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        Map<String, String> model = new HashMap<>();
        model.put("userName", userRegisteredEvent.getName());
        model.put("userEmail", userRegisteredEvent.getEmail());
        model.put("userPassword", userRegisteredEvent.getPassword());
        Mail mail = new Mail(
                "user/registered.ftlh",
                userRegisteredEvent.getEmail(),
                "Добро пожаловать!",
                model
        );
        try {
            mailService.sendEmail(mail);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.user.logged-in}")
    public void userLoggedIn(UserLoggedInEvent userLoggedInEvent) {
        Map<String, String> model = new HashMap<>();
        model.put("userName", userLoggedInEvent.getName());
        model.put("userIp", userLoggedInEvent.getIp());
        Mail mail = new Mail(
                "user/logged-in.ftlh",
                userLoggedInEvent.getEmail(),
                "Уведомление о входе",
                model
        );
        try {
            mailService.sendEmail(mail);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
