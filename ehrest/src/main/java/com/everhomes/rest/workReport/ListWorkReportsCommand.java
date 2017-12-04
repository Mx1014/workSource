package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>reportStatus: 汇报状态 0-无效 1-未启用 2-启用 参考{@link com.everhomes.rest.workReport.WorkReportStatus}</li>
 * <li>pageAnchor: 分页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListWorkReportsCommand {

    private Long ownerId;

    private String ownerType;

    private Byte reportStatus;

    private Long pageAnchor;

    private Integer pageSize;

    public ListWorkReportsCommand() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Byte getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Byte reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
