package com.everhomes.bus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaAutoConfiguration {

    private final KafkaProperties properties;

    public KafkaAutoConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(
            ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
                kafkaProducerFactory);
        //如果没有kafka相关配置或者有配置但为disable,那么将不启用kafka
        if (!"disable".equals(properties.getClientId())
                && properties.getClientId() != null) {
            kafkaTemplate.setProducerListener(kafkaProducerListener);
            kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
            return kafkaTemplate;
        }
        return null;
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<Object, Object>();
    }

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<Object, Object>(
                this.properties.buildConsumerProperties());
    }

    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<Object, Object>(
                this.properties.buildProducerProperties());
    }
}