package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhSyncDataErrors;
import com.everhomes.util.StringHelper;

public class SyncDataError extends EhSyncDataErrors {
    private static final long serialVersionUID = -70660425959289752L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
