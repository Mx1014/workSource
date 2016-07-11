package com.everhomes.pushmessage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface PushMessageResultProvider {
    public void createPushMessageResult(PushMessageResult pushMessageResult);
    public void updatePushMessageResult(PushMessageResult pushMessageResult);
    public PushMessageResult getPushMessageResultById(Long id);
    public List<PushMessageResult> queryPushMessageResult(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    public List<PushMessageResult> queryPushMessageResultByIdentify(CrossShardListingLocator locator, int count, Long targetUserId);
}
