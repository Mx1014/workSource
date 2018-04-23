package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhRemindSettings;
import com.everhomes.util.StringHelper;

public class RemindSetting extends EhRemindSettings {
    private static final long serialVersionUID = 1188688293L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
