package ru.giv13.mailer.handler;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.giv13.common.event.UserLoggedInEvent;
import ru.giv13.common.event.UserRegisteredEvent;

@Component
public class UserEventHandler {
    @KafkaListener(topics = "${spring.kafka.topics.user.registered}")
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        System.out.println("Новая регистрация: " + userRegisteredEvent.toString());
    }

    @KafkaListener(topics = "${spring.kafka.topics.user.logged-in}")
    public void userLoggedIn(UserLoggedInEvent userLoggedInEvent) {
        System.out.println("Вход в систему: " + userLoggedInEvent.toString());
    }
}
