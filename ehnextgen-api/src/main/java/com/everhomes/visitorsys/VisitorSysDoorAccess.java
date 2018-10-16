package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysDoorAccess;
import com.everhomes.util.StringHelper;

public class VisitorSysDoorAccess extends EhVisitorSysDoorAccess {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
