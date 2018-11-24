package com.everhomes.bus;

import org.apache.commons.collections.ExtendedProperties;
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
@EnableConfigurationProperties(ExtendKafkaProperties.class)
public class KafkaAutoConfiguration {

    private final ExtendKafkaProperties properties;

    public KafkaAutoConfiguration(ExtendKafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(
            ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
                kafkaProducerFactory);
        if (properties.isEnabled()) {
            kafkaTemplate.setProducerListener(kafkaProducerListener);
            kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
            return kafkaTemplate;
        }
        return new NoopKafkaTemplate();
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<Object, Object>();
    }

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        if (properties.isEnabled()) {
            return new DefaultKafkaConsumerFactory<Object, Object>(
                    properties.buildConsumerProperties());
        } else {
            return new DefaultKafkaConsumerFactory<Object, Object>(
                    new ExtendKafkaProperties().buildConsumerProperties());
        }
    }

    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        if (properties.isEnabled()) {
            return new DefaultKafkaProducerFactory<Object, Object>(
                    properties.buildProducerProperties());
        } else {
            return new DefaultKafkaProducerFactory<Object, Object>(
                    new ExtendKafkaProperties().buildProducerProperties());
        }
    }
}