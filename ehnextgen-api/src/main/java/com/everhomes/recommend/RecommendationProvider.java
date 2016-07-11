package com.everhomes.recommend;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface RecommendationProvider {
    void createRecommendation(Recommendation recommend);
    void updateRecommendation(Recommendation recommend);
    List<Recommendation> queryRecommendsByUserId(long userId, ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    void ignoreRecommend(Long userId, Integer suggestType, Long sourceId, Integer sourceType);
}
