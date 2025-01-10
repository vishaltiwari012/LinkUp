package com.cw.linkedin.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic addUserNodeTopic() {
        return new NewTopic("add-user-node-topic", 3, (short) 1);
    }

}
