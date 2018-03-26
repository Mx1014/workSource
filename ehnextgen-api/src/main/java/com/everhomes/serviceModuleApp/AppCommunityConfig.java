package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhAppCommunityConfig;
import com.everhomes.util.StringHelper;

public class AppCommunityConfig extends EhAppCommunityConfig {
    /**
     * 
     */
    private static final long serialVersionUID = 7035883890825621724L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
