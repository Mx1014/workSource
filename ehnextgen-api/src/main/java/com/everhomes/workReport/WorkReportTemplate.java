package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportTemplates;
import com.everhomes.util.StringHelper;

public class WorkReportTemplate extends EhWorkReportTemplates{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
