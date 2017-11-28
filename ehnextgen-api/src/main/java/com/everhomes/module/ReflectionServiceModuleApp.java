package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhReflectionServiceModuleApps;
import com.everhomes.util.StringHelper;

public class ReflectionServiceModuleApp extends EhReflectionServiceModuleApps{
    private static final long serialVersionUID = -4995895050475309252L;

    public ReflectionServiceModuleApp() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
