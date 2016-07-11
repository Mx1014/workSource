package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhOwnerDoors;
import com.everhomes.util.StringHelper;

public class OwnerDoor extends EhOwnerDoors {
    /**
     * 
     */
    private static final long serialVersionUID = -2883185865023063373L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
