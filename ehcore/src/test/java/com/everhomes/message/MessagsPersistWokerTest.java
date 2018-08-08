package com.everhomes.message;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagePersistType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MessagePersistWorker;
import org.junit.Test;

import java.util.HashMap;

public class MessagsPersistWokerTest extends CoreServerTestCase {


    @Test
    public void testLogPersist() {
        MessageRecordDto dto = new MessageRecordDto();
        dto.setBody("message body");
        dto.setBodyType(MessageBodyType.TEXT.getCode());
        dto.setIndexId(1L);
        dto.setSenderUid(2L);
        dto.setMeta(new HashMap<>());
        dto.getMeta().put(MessageMetaConstant.PERSIST_TYPE, MessagePersistType.LOG.getCode()+"");

        MessagePersistWorker.getQueue().offer(dto);
        MessagePersistWorker.getQueue().offer(ConvertHelper.convert(dto, MessageRecordDto.class));
        MessagePersistWorker.getQueue().offer(ConvertHelper.convert(dto, MessageRecordDto.class));
        MessagePersistWorker.getQueue().offer(ConvertHelper.convert(dto, MessageRecordDto.class));
        MessagePersistWorker.getQueue().offer(ConvertHelper.convert(dto, MessageRecordDto.class));
        try {
            Thread.sleep(11000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MessagePersistWorker.getQueue().offer(ConvertHelper.convert(dto, MessageRecordDto.class));
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
