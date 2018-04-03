package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>reports: 汇报列表，参考 {@link com.everhomes.rest.workReport.WorkReportDTO}</li>
 * </ul>
 */
public class ListWorkReportsResponse {

    private Long nextPageAnchor;

    @ItemType(WorkReportDTO.class)
    private List<WorkReportDTO> reports;

    public ListWorkReportsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WorkReportDTO> getReports() {
        return reports;
    }

    public void setReports(List<WorkReportDTO> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
