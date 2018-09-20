package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchNotifications;
import com.everhomes.util.StringHelper;

public class PunchNotification extends EhPunchNotifications {
    private static final long serialVersionUID = 1351193643L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
