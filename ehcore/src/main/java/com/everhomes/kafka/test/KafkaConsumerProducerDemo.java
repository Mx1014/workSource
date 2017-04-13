package com.everhomes.kafka.test;

public class KafkaConsumerProducerDemo {
	public static void main(String[] args) {
        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");
        Producer producerThread = new Producer(KafkaProperties.TOPIC, isAsync);
        producerThread.start();

        Consumer consumerThread = new Consumer(KafkaProperties.TOPIC);
        consumerThread.start();
            Consumer2 consumerThread2 = new Consumer2(KafkaProperties.TOPIC);
            consumerThread2.start();
    }

//    public static long getLastOffset(SimpleConsumer consumer, String topic, int partition,
//                                     long whichTime, String clientName) {
//        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
//        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
//        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
//        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(),clientName);
//        OffsetResponse response = consumer.getOffsetsBefore(request);
//
//        if (response.hasError()) {
//            System.out.println("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition) );
//            return 0;
//        }
//        long[] offsets = response.offsets(topic, partition);
//        return offsets[0];
//    }
}
