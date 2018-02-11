package com.everhomes.message;

import java.util.List;

public interface MessageProvider {
    void createMessageRecord(MessageRecord messageRecord);
    void createMessageRecords(List<MessageRecord> messageRecords);
    void updateMessageRecord(MessageRecord messageRecord);
    void deleteMessageRecordById(Long id);
    List<MessageRecord> listMessageRecords(Integer namespaceId);
    Long getMaxMessageIndexId();
    Long getNextMessageIndexId();

}
