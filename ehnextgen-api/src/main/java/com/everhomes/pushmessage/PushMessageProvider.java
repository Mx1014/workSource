package com.everhomes.pushmessage;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface PushMessageProvider {
    public void createPushMessage(PushMessage pushMessage);
    public void updatePushMessage(PushMessage pushMessage);
    public PushMessage getPushMessageById(Long id);
    public void deleteByPushMesageId(Long id);
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count);
}
