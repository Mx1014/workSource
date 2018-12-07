// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppEntryProfiles;
import com.everhomes.util.StringHelper;

public class ServiceModuleAppEntryProfile extends EhServiceModuleAppEntryProfiles{
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
