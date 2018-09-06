package com.everhomes.buttscript.bus;


import com.everhomes.util.StringHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SystemEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemEventListener.class);


    /*@KafkaListener(topicPattern = "system-event")
    public void consume(ConsumerRecord<?, String> record) {
        LocalEvent event = (LocalEvent) StringHelper.fromJsonString(record.value(), LocalEvent.class);
        LOGGER.debug("event:" + event);
    }*/
}
