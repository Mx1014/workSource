package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportVals;
import com.everhomes.util.StringHelper;

public class WorkReportVal extends EhWorkReportVals{

    private Byte readStatus;

    public WorkReportVal() {
    }

    public Byte getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Byte readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
