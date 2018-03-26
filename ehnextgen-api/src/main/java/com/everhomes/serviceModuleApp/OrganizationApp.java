package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhOrganizationApps;
import com.everhomes.util.StringHelper;

public class OrganizationApp extends EhOrganizationApps {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
