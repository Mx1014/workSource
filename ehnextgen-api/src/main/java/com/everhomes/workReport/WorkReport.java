package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.util.StringHelper;

public class WorkReport extends EhWorkReports{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
