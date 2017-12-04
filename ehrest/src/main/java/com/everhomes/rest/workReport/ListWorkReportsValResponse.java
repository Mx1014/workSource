package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>reportVals: 汇报值列表 参考{@link com.everhomes.rest.workReport.WorkReportValDTO}</li>
 * </ul>
 */
public class ListWorkReportsValResponse {

    private Long nextPageAnchor;

    @ItemType(WorkReportValDTO.class)
    private List<WorkReportValDTO> reportVals;

    public ListWorkReportsValResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
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
