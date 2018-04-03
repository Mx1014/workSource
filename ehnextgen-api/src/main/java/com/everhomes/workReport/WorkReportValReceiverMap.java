package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportValReceiverMap;
import com.everhomes.util.StringHelper;

public class WorkReportValReceiverMap extends EhWorkReportValReceiverMap {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
