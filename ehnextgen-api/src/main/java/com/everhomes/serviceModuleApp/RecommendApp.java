// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhRecommendApps;
import com.everhomes.util.StringHelper;


public class RecommendApp extends EhRecommendApps {


    private static final long serialVersionUID = -2706203467412662085L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}