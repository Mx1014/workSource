package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页起始页码</li>
 * <li>reportVals: 汇报值列表 参考{@link com.everhomes.rest.workReport.WorkReportValDTO}</li>
 * </ul>
 */
public class ListWorkReportsValResponse {

    private Integer nextPageOffset;

    @ItemType(WorkReportValDTO.class)
    private List<WorkReportValDTO> reportVals;

    public ListWorkReportsValResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<WorkReportValDTO> getReportVals() {
        return reportVals;
    }

    public void setReportVals(List<WorkReportValDTO> reportVals) {
        this.reportVals = reportVals;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
