// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhWorkPlatformApps;
import com.everhomes.util.StringHelper;

public class WorkPlatformApp extends EhWorkPlatformApps{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
