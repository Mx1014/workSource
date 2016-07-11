package com.everhomes.recommend;

import java.util.List;

import javax.validation.Valid;

import com.everhomes.rest.recommend.CreateRecommendConfig;
import com.everhomes.rest.recommend.ListRecommendConfigCommand;
import com.everhomes.rest.recommend.ListRecommendConfigResponse;

public interface RecommendationService {
    RecommendationConfig createConfig(@Valid CreateRecommendConfig cmd);
    void createRecommendation(Recommendation recommend);
    void communityNotify(Long userId,Long addressId, Long communityId);
    List<Recommendation> getRecommendsByUserId(Long userId, int sourceType, int pageSize);
    void ignoreRecommend(Long userId, Integer suggestType, Long sourceId, Integer sourceType);
    ListRecommendConfigResponse listRecommendConfigsBySource(ListRecommendConfigCommand cmd);
    
}
