package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleEntries;
import com.everhomes.util.StringHelper;

public class ServiceModuleEntry extends EhServiceModuleEntries {


    private static final long serialVersionUID = 4304794540057005280L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
