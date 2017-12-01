package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportVals;
import com.everhomes.util.StringHelper;

public class WorkReportVals extends EhWorkReportVals{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
