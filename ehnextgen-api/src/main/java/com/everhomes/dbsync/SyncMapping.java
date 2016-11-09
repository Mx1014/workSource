package com.everhomes.dbsync;
import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhSyncMapping;

public class SyncMapping extends EhSyncMapping {
    /**
     * 
     */
    private static final long serialVersionUID = 5400876288264639185L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
