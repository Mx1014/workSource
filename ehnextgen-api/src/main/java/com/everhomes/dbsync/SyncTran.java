package com.everhomes.dbsync;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhSyncTrans;

public class SyncTran extends EhSyncTrans {

    /**
     * 
     */
    private static final long serialVersionUID = 5314712572134821932L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
