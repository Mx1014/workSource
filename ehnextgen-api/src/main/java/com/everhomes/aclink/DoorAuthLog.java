package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorAuthLogs;
import com.everhomes.util.StringHelper;

public class DoorAuthLog extends EhDoorAuthLogs {
    /**
     * 
     */
    private static final long serialVersionUID = -3162979816726667992L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
