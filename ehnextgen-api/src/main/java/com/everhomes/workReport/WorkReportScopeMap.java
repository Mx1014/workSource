package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMap;
import com.everhomes.util.StringHelper;

public class WorkReportScopeMap extends EhWorkReportScopeMap {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
