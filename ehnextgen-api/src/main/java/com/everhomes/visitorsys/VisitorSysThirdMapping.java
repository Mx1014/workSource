package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysThirdMapping;
import com.everhomes.util.StringHelper;

public class VisitorSysThirdMapping extends EhVisitorSysThirdMapping {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
