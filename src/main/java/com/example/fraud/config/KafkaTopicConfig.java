package com.example.fraud.config;

import com.example.fraud.event.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
