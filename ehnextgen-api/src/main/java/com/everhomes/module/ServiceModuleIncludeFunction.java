package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleIncludeFunctions;
import com.everhomes.util.StringHelper;

public class ServiceModuleIncludeFunction extends EhServiceModuleIncludeFunctions {
    private static final long serialVersionUID = -7147087623243961017L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
