package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysHkwsUser;
import com.everhomes.util.StringHelper;

public class HKWSUser extends EhVisitorSysHkwsUser {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
