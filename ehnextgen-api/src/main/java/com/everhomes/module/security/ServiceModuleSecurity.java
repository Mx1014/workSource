package com.everhomes.module.security;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleSecurities;
import com.everhomes.util.StringHelper;

public class ServiceModuleSecurity extends EhServiceModuleSecurities {
    private static final long serialVersionUID = 1740980116L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
