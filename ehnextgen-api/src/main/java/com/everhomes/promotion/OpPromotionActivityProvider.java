package com.everhomes.promotion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface OpPromotionActivityProvider {

    Long createOpPromotionActivity(OpPromotionActivity obj);

    void updateOpPromotionActivity(OpPromotionActivity obj);

    void deleteOpPromotionActivity(OpPromotionActivity obj);

    OpPromotionActivity getOpPromotionActivityById(Long id);

    List<OpPromotionActivity> queryOpPromotionActivities(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);
    
    List<OpPromotionActivity> listOpPromotion(ListingLocator locator, int count);

    List<OpPromotionActivity> listOpPromotionByPriceRange(ListingLocator locator, int count, Long value);

    List<OpPromotionActivity> searchOpPromotionByKeyword(ListingLocator locator, int count, String keyword);

}
