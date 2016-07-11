package com.everhomes.recommend;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;


public interface RecommendationConfigProvider {
    void createRecommendConfig(RecommendationConfig config);
    List<RecommendationConfig> listRecommendConfigs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
}
