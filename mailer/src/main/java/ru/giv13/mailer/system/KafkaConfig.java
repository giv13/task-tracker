package ru.giv13.mailer.system;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import ru.giv13.mailer.exception.NonRetryableException;
import ru.giv13.mailer.exception.RetryableException;

import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topics.user.dlt}")
    private String userDltTopicName;

    private final Map<String, String> configs = Map.of("min.insync.replicas", "2");

    @Bean
    public NewTopic userDltTopic() {
        return new NewTopic(userDltTopicName, 1, (short) 3).configs(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, Object> kafkaListenerContainerFactory(
            ConsumerFactory<Integer, Object> consumerFactory,
            KafkaTemplate<Integer, Object> kafkaTemplate
    ) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate, (consumerRecord, exception) -> new TopicPartition(userDltTopicName, -1)),
                new FixedBackOff(3000, 3)
        );
        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);

        ConcurrentKafkaListenerContainerFactory<Integer, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
