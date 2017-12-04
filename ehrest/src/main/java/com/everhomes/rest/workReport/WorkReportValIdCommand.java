package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

public class WorkReportValIdCommand {

    private Long reportValId;

    public WorkReportValIdCommand() {
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
