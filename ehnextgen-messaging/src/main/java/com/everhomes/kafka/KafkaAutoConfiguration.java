package com.everhomes.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

import java.util.Collection;

@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(ExtendKafkaProperties.class)
public class KafkaAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAutoConfiguration.class);

    private final ExtendKafkaProperties properties;

    public KafkaAutoConfiguration(ExtendKafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(
            ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener,
            Collection<NewTopic> newTopicCollection) {

        if (properties.isEnabled()) {
            KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
            kafkaTemplate.setProducerListener(kafkaProducerListener);
            kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());

            // 自动创建 topic
            createTopicIfNeeded(newTopicCollection);

            return kafkaTemplate;
        }
        return new NoopKafkaTemplate();
    }

    private void createTopicIfNeeded(Collection<NewTopic> newTopicCollection) {
        AdminClient adminClient = AdminClient.create(properties.buildAdminProperties());
        CreateTopicsResult result = adminClient.createTopics(newTopicCollection);
        result.values().forEach((s, voidKafkaFuture) -> LOGGER.info("Create kafka topic: [{}]", s));
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        if (properties.isEnabled()) {
            return new DefaultKafkaConsumerFactory<>(
                    properties.buildConsumerProperties());
        } else {
            return new NoopKafkaConsumerFactory();
        }
    }

    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        if (properties.isEnabled()) {
            return new DefaultKafkaProducerFactory<>(
                    properties.buildProducerProperties());
        } else {
            return new NoopKafkaProducerFactory();
        }
    }

    @Bean
    public NewTopic systemEventTopic() {
        return new NewTopic("system-event", 100, (short) 1);
    }
}