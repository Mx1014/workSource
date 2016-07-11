package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.util.StringHelper;

public class DoorAccess extends EhDoorAccess {
    /**
     * 
     */
    private static final long serialVersionUID = -9099421900054795087L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
