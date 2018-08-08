package com.everhomes.search;

import com.everhomes.message.MessageRecord;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;

import java.util.List;

public interface MessageRecordSearcher {
    void deleteById(Long id);

    void bulkUpdate(List<MessageRecord> messageRecords);

    void feedDoc(MessageRecord messageRecord);

    void syncMessageRecordIndexs();

    void syncMessageRecordsByNamespace(Integer namespaceId);

    void syncMessageRecords();

    List queryMessage(SearchMessageRecordCommand cmd);

    List queryMessageByIndex(SearchMessageRecordCommand cmd);

    void deleteAll();

    void refresh();
}
