package com.everhomes.rest.recommend;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListRecommendConfigResponse {
    private Long nextPageAnchor;

    @ItemType(RecommendConfigDTO.class)
    private List<RecommendConfigDTO> recommendConfigs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RecommendConfigDTO> getRecommendConfigs() {
        return recommendConfigs;
    }

    public void setRecommendConfigs(List<RecommendConfigDTO> recommendConfigs) {
        this.recommendConfigs = recommendConfigs;
    }
    
    
}
