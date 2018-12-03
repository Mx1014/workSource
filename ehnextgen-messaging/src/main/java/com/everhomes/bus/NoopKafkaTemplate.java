package com.everhomes.bus;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoopKafkaTemplate extends KafkaTemplate {

    public NoopKafkaTemplate() {
        super(new DefaultKafkaProducerFactory(new HashMap<>(1)));
    }

    public NoopKafkaTemplate(ProducerFactory producerFactory) {
        super(producerFactory);
    }

    public NoopKafkaTemplate(ProducerFactory producerFactory, boolean autoFlush) {
        super(producerFactory, autoFlush);
    }

    @Override
    public String getDefaultTopic() {
        return super.getDefaultTopic();
    }

    @Override
    public void setDefaultTopic(String defaultTopic) {

    }

    @Override
    public void setProducerListener(ProducerListener producerListener) {

    }

    @Override
    public MessageConverter getMessageConverter() {
        return super.getMessageConverter();
    }

    @Override
    public void setMessageConverter(RecordMessageConverter messageConverter) {

    }

    @Override
    public ListenableFuture<SendResult> sendDefault(Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> sendDefault(Object key, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> send(String topic, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> send(String topic, Object key, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> sendDefault(int partition, Object key, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> send(String topic, int partition, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> send(String topic, int partition, Object key, Object data) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    protected ListenableFuture<SendResult> doSend(ProducerRecord producerRecord) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public ListenableFuture<SendResult> send(Message message) {
        return new ListenableFutureTask<SendResult>(() -> {return null;});
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return new ArrayList<>();
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return new HashMap<>();
    }

    @Override
    public Object execute(ProducerCallback callback) {
        return null;
    }

    @Override
    public void flush() {

    }
}
