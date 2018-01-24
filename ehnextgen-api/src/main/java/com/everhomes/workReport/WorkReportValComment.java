package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportValComments;
import com.everhomes.util.StringHelper;

public class WorkReportValComment extends EhWorkReportValComments {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
