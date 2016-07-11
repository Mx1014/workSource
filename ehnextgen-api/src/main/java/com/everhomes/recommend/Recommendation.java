package com.everhomes.recommend;

import com.everhomes.server.schema.tables.pojos.EhRecommendations;
import com.everhomes.util.StringHelper;

public class Recommendation extends EhRecommendations {
    /**
     * 
     */
    private static final long serialVersionUID = 6100223570394783094L;
    
    public Recommendation() {
        
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
