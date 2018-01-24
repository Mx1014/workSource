package com.everhomes.message;

import java.util.List;

public interface MessageProvider {
    void createMessageRecord(MessageRecord messageRecord);
    void updateMessageRecord(MessageRecord messageRecord);
    void deleteMessageRecordById(Long id);
    List<MessageRecord> listMessageRecords(Integer namespaceId);

}
