package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhRemindShares;
import com.everhomes.util.StringHelper;

public class RemindShare extends EhRemindShares {
    private static final long serialVersionUID = -1560997430L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
