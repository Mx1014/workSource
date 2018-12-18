package com.everhomes.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class NoopKafkaConsumerFactory implements ConsumerFactory {
    @Override
    public Consumer createConsumer() {
        return new Consumer() {
            @Override
            public Set<TopicPartition> assignment() {
                return null;
            }

            @Override
            public Set<String> subscription() {
                return null;
            }

            @Override
            public void subscribe(Collection topics) {

            }

            @Override
            public void subscribe(Collection topics, ConsumerRebalanceListener callback) {

            }

            @Override
            public void assign(Collection collection) {

            }

            @Override
            public void subscribe(Pattern pattern, ConsumerRebalanceListener callback) {

            }

            @Override
            public void unsubscribe() {

            }

            @Override
            public ConsumerRecords poll(long timeout) {
                return null;
            }

            @Override
            public void commitSync() {

            }

            @Override
            public void commitSync(Map offsets) {

            }

            @Override
            public void commitAsync() {

            }

            @Override
            public void commitAsync(OffsetCommitCallback callback) {

            }

            @Override
            public void commitAsync(Map offsets, OffsetCommitCallback callback) {

            }

            @Override
            public void seek(TopicPartition partition, long offset) {

            }

            @Override
            public void seekToBeginning(Collection collection) {

            }

            @Override
            public void seekToEnd(Collection collection) {

            }

            @Override
            public long position(TopicPartition partition) {
                return 0;
            }

            @Override
            public OffsetAndMetadata committed(TopicPartition partition) {
                return null;
            }

            @Override
            public Map<MetricName, ? extends Metric> metrics() {
                return null;
            }

            @Override
            public List<PartitionInfo> partitionsFor(String topic) {
                return null;
            }

            @Override
            public Map<String, List<PartitionInfo>> listTopics() {
                return null;
            }

            @Override
            public Set<TopicPartition> paused() {
                return null;
            }

            @Override
            public void pause(Collection collection) {

            }

            @Override
            public void resume(Collection collection) {

            }

            @Override
            public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map timestampsToSearch) {
                return null;
            }

            @Override
            public Map<TopicPartition, Long> beginningOffsets(Collection collection) {
                return null;
            }

            @Override
            public Map<TopicPartition, Long> endOffsets(Collection collection) {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public void close(long timeout, TimeUnit unit) {

            }

            @Override
            public void wakeup() {

            }
        };
    }

    @Override
    public boolean isAutoCommit() {
        return false;
    }
}
