// @formatter:off
package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppEntries;
import com.everhomes.util.StringHelper;

public class ServiceModuleAppEntry extends EhServiceModuleAppEntries{
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
