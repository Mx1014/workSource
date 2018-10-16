package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMsg;
import com.everhomes.util.StringHelper;

public class WorkReportScopeMsg extends EhWorkReportScopeMsg {

    private static final long serialVersionUID = 5392190212820234497L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
