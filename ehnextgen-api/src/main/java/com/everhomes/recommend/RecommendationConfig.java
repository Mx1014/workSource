package com.everhomes.recommend;

import com.everhomes.server.schema.tables.pojos.EhRecommendationConfigs;
import com.everhomes.util.StringHelper;

public class RecommendationConfig extends EhRecommendationConfigs {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8076015310929053049L;

    public RecommendationConfig() {
        
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
