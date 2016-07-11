package com.everhomes.promotion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface OpPromotionMessageProvider {

    Long createOpPromotionMessage(OpPromotionMessage obj);

    void updateOpPromotionMessage(OpPromotionMessage obj);

    void deleteOpPromotionMessage(OpPromotionMessage obj);

    OpPromotionMessage getOpPromotionMessageById(Long id);

    List<OpPromotionMessage> queryOpPromotionMessages(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    OpPromotionMessage findTargetByPromotionId(Long userId, Long promotionId);

}
