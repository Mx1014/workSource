package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>applierIds: 申请人ids</li>
 * <li>receiverIds: 接收人ids</li>
 * <li>reportId: 工作汇报id</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>readStatus: 阅读状态 0-未读 参考{@link com.everhomes.rest.workReport.WorkReportReadStatus}</li>
 * <li>pageAnchor: 分页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListWorkReportsValCommand {

    private Long ownerId;

    private String ownerType;

    @ItemType(Long.class)
    private List<Long> applierIds;

    @ItemType(Long.class)
    private List<Long> receiverIds;

    private Long reportId;

    private Timestamp startTime;

    private Timestamp endTime;

    private Byte readStatus;

    private Long pageAnchor;

    private Integer pageSize;

    public ListWorkReportsValCommand() {
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

    public List<Long> getApplierIds() {
        return applierIds;
    }

    public void setApplierIds(List<Long> applierIds) {
        this.applierIds = applierIds;
    }

    public List<Long> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<Long> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Byte getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Byte readStatus) {
        this.readStatus = readStatus;
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
