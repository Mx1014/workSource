package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchOvertimeRules;
import com.everhomes.util.StringHelper;

public class PunchOvertimeRule extends EhPunchOvertimeRules {
    private static final long serialVersionUID = -749828928L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
