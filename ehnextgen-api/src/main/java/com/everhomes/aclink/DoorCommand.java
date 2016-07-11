package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorCommand;
import com.everhomes.util.StringHelper;

public class DoorCommand extends EhDoorCommand {
    /**
     * 
     */
    private static final long serialVersionUID = 8213305547856223754L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
