package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>creatorName: 申请人</li>
 * <li>creatorDepartment: 申请人部门</li>
 * <li>creatorDepartmentId: 申请人部门id</li>
 * <li>creatorMobile: 申请人手机号</li>
 * <li>createTime: 申请时间</li>
 * <li>approvalType: 审批类型</li>
 * <li>approvalNo: 审批编号</li>
 * <li>approvalStatus: 审批状态 参考{@link com.everhomes.rest.flow.FlowCaseStatus}</li>
 * <li>flowCaseId: 工作流id</li>
 * </ul>
 */
public class GeneralApprovalRecordDTO {
    private Long id;

    private Integer namespaceId;

    private Long moduleId;

    private String moduleType;

    private String creatorName;

    private String creatorDepartment;

    private Long creatorDepartmentId;

    private String creatorMobile;

    private String createTime;

    private String approvalType;

    private Long approvalNo;

    private Byte approvalStatus;

    private Long flowCaseId;

    public GeneralApprovalRecordDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorDepartment() {
        return creatorDepartment;
    }

    public void setCreatorDepartment(String creatorDepartment) {
        this.creatorDepartment = creatorDepartment;
    }

    public Long getCreatorDepartmentId() {
        return creatorDepartmentId;
    }

    public void setCreatorDepartmentId(Long creatorDepartmentId) {
        this.creatorDepartmentId = creatorDepartmentId;
    }

    public String getCreatorMobile() {
        return creatorMobile;
    }

    public void setCreatorMobile(String creatorMobile) {
        this.creatorMobile = creatorMobile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public Long getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(Long approvalNo) {
        this.approvalNo = approvalNo;
    }

    public Byte getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Byte approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
