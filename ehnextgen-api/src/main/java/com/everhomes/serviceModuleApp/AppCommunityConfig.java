package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhAppCommunityConfigs;
import com.everhomes.util.StringHelper;

public class AppCommunityConfig extends EhAppCommunityConfigs {
    /**
     * 
     */
    private static final long serialVersionUID = 7035883890825621724L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
