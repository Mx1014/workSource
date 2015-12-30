package com.everhomes.rest.recommend;

import java.util.List;

import com.everhomes.discover.ItemType;

public class RecommendConfigResponse {
    private Long nextPageAnchor;
    
    @ItemType(RecommendConfigDTO.class)
    private List<RecommendConfigDTO> configs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RecommendConfigDTO> getConfigs() {
        return configs;
    }

    public void setConfigs(List<RecommendConfigDTO> configs) {
        this.configs = configs;
    }
    
    
}
