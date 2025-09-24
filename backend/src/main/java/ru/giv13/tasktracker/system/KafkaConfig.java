package ru.giv13.tasktracker.system;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topics.user.registered}")
    private String userRegisteredTopicName;

    @Value("${spring.kafka.topics.user.logged-in}")
    private String userLoggedInTopicName;

    private final Map<String, String> configs = Map.of("min.insync.replicas", "2");

    @Bean
    public NewTopic userRegisteredTopic() {
        return new NewTopic(userRegisteredTopicName, 3, (short) 3).configs(configs);
    }

    @Bean
    public NewTopic userLoggedInTopic() {
        return new NewTopic(userLoggedInTopicName, 3, (short) 3).configs(configs);
    }
}
