package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinessVisibleScopes;
import com.everhomes.util.StringHelper;

public class BusinessVisibleScope extends EhBusinessVisibleScopes{

    private static final long serialVersionUID = -2247269616356104893L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
