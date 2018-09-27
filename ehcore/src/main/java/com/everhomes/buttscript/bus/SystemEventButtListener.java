package com.everhomes.buttscript.bus;


import com.everhomes.bus.LocalEvent;
import com.everhomes.buttscript.engine.ButtScriptSchedulerMain;
import com.everhomes.util.StringHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SystemEventButtListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemEventButtListener.class);

    @Autowired
    private ButtScriptSchedulerMain buttScriptSchedulerMain ;

    /**
     * 监听传到 kafka 中的事件
     * @param record
     */
    @KafkaListener(topicPattern = "system-event")
    public void consume(ConsumerRecord<?, String> record) {
        LocalEvent event = (LocalEvent) StringHelper.fromJsonString(record.value(), LocalEvent.class);
        LOGGER.debug("event:" + event);
        buttScriptSchedulerMain.doButtScriptMethod(event);
    }
}
