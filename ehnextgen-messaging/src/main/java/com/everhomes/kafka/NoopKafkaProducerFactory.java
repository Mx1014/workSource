package com.everhomes.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.kafka.core.ProducerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NoopKafkaProducerFactory implements ProducerFactory {
    @Override
    public Producer createProducer() {
        return new Producer() {
            @Override
            public void initTransactions() {

            }

            @Override
            public void beginTransaction() throws ProducerFencedException {

            }

            @Override
            public void sendOffsetsToTransaction(Map offsets, String consumerGroupId) throws ProducerFencedException {

            }

            @Override
            public void commitTransaction() throws ProducerFencedException {

            }

            @Override
            public void abortTransaction() throws ProducerFencedException {

            }

            @Override
            public Future<RecordMetadata> send(ProducerRecord record) {
                return null;
            }

            @Override
            public Future<RecordMetadata> send(ProducerRecord record, Callback callback) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public List<PartitionInfo> partitionsFor(String topic) {
                return null;
            }

            @Override
            public Map<MetricName, ? extends Metric> metrics() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public void close(long timeout, TimeUnit unit) {

            }
        };
    }
}
