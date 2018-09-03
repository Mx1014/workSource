package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhSyncDataErrors;
import com.everhomes.util.StringHelper;

public class SyncDataErrors extends EhSyncDataErrors {

    private static final long serialVersionUID = -8653483028406906580L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
