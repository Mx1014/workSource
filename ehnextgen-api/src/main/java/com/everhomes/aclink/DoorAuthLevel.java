package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorAuthLevel;
import com.everhomes.util.StringHelper;

public class DoorAuthLevel extends EhDoorAuthLevel {
    /**
     * 
     */
    private static final long serialVersionUID = 1189362834314030890L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
