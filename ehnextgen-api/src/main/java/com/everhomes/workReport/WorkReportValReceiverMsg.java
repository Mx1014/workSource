package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportValReceiverMsg;
import com.everhomes.util.StringHelper;

public class WorkReportValReceiverMsg extends EhWorkReportValReceiverMsg {

    private static final long serialVersionUID = -3390733125436054128L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
