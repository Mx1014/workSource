package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhOrganizationApps;
import com.everhomes.util.StringHelper;

public class OrganizationApp extends EhOrganizationApps {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
