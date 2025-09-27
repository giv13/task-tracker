package ru.giv13.scheduler.system;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topics.user.task-summary}")
    private String userTaskSummaryTopicName;

    private final Map<String, String> configs = Map.of("min.insync.replicas", "2");

    @Bean
    public NewTopic userRegisteredTopic() {
        return new NewTopic(userTaskSummaryTopicName, 3, (short) 3).configs(configs);
    }
}
