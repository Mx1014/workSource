package com.everhomes.dbsync;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhSyncApps;

public class SyncApp extends EhSyncApps {
    /**
     * 
     */
    private static final long serialVersionUID = -5411253324488329900L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
