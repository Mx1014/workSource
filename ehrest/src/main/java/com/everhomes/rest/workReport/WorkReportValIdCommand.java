package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportValId: 工作汇报单id</li>
 * <li>reportId: 工作汇报id</li>
 * <li>originFieldFlag: 1:fieldValue显示原始值，即json格式，0:仅显示值</li>
 * </ul>
 */
public class WorkReportValIdCommand {

    private Long reportValId;

    private Long reportId;
    private Byte originFieldFlag;

    public WorkReportValIdCommand() {
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Byte getOriginFieldFlag() {
        return originFieldFlag;
    }

    public void setOriginFieldFlag(Byte originFieldFlag) {
        this.originFieldFlag = originFieldFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
