package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhReminds;
import com.everhomes.util.StringHelper;

public class Remind extends EhReminds {
    private static final long serialVersionUID = -554065632L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
