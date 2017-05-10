package com.everhomes.kafka.test;

public class KafkaProperties {
	public static final String TOPIC = "my-replicated-topic";
    public static final String KAFKA_SERVER_URL = "10.1.10.102";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String TOPIC2 = "topic2";
    public static final String TOPIC3 = "topic3";
    public static final String CLIENT_ID = "SimpleConsumerDemoClient";

    private KafkaProperties() {}
}
