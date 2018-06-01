package com.everhomes.message;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface MessageProvider {
    void createMessageRecord(MessageRecord messageRecord);
    void createMessageRecords(List<MessageRecord> messageRecords);
    void updateMessageRecord(MessageRecord messageRecord);
    void deleteMessageRecordById(Long id);
    List<MessageRecord> listMessageRecords(Integer namespaceId);

    /**
     * 获取消息记录
     * @param pageSize  每页多少条
     * @param locator   分页
     * @param callback  条件回调
     * @return  记录列表
     */
    List<MessageRecord> listMessageRecords(int pageSize, ListingLocator locator, ListingQueryBuilderCallback callback);

    List<MessageRecord> listMessageRecords();
    Long getMaxMessageIndexId();
    Long getNextMessageIndexId();

}
