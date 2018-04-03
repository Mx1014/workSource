package com.everhomes.rest.general_approval;

/**
 * <ul>
 * <li>startTime: 申请开始时间</li>
 * <li>endTime: 申请结束时间</li>
 * <li>approvalStatus: 审批状态 {@link com.everhomes.rest.flow.FlowCaseStatus }</li>
 * <li>approvalType: 审批类型</li>
 * <li>creatorDepartmentId: 申请人部门id</li>
 * <li>creatorName: 申请人</li>
 * <li>approvalNo: 审批编号</li>
 * <li>pageAnchor: 下一页id</li>
 * <li>pageSize: 页面大小</li>
 * </ul>
 */
public class  ListGeneralApprovalRecordsCommand {

    private Long organizationId;

    private Long moduleId;

    private Long startTime;

    private Long endTime;

    private Byte approvalStatus;

    private String approvalType;

    private Long creatorDepartmentId;

    private String creatorName;

    private Long approvalNo;

    private Long pageAnchor;

    private Integer pageSize;

    public ListGeneralApprovalRecordsCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Byte getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Byte approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public Long getCreatorDepartmentId() {
        return creatorDepartmentId;
    }

    public void setCreatorDepartmentId(Long creatorDepartmentId) {
        this.creatorDepartmentId = creatorDepartmentId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(Long approvalNo) {
        this.approvalNo = approvalNo;
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
}
