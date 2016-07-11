package com.everhomes.pushmessage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.pushmessage.ListPushMessageResultCommand;
import com.everhomes.rest.pushmessage.PushMessageResultDTO;

public interface PushMessageService {
    public void createPushMessage(PushMessage pushMessage);
    public boolean updatePushMessage(PushMessage pushMessage);
    public PushMessage getPushMessageById(Long id);
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count);
    public PushMessageResult getPushMessageResultById(Long id);
    public void deleteByPushMesageId(Long id);
    public List<PushMessageResult> queryPushMessageResultByIdentify(CrossShardListingLocator locator, int count, Long targetUserId);
    List<PushMessageResultDTO> queryPushMessageResult(CrossShardListingLocator locator, ListPushMessageResultCommand cmd);
}
