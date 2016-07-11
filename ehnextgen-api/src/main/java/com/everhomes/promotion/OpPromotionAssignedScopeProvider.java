package com.everhomes.promotion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface OpPromotionAssignedScopeProvider {

    Long createOpPromotionAssignedScope(OpPromotionAssignedScope obj);

    void updateOpPromotionAssignedScope(OpPromotionAssignedScope obj);

    void deleteOpPromotionAssignedScope(OpPromotionAssignedScope obj);

    OpPromotionAssignedScope getOpPromotionAssignedScopeById(Long id);

    List<OpPromotionAssignedScope> queryOpPromotionAssignedScopes(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<OpPromotionAssignedScope> getOpPromotionScopeByPromotionId(Long promotionId);

}
