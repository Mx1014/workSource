package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * <li>operationTime: 修改时间</li>
 * <li>operationType: 新增、修改、删除</li>
 * <li>operatorUid: 操作人员编号</li>
 * <li>auditContent: 修改内容</li>
 * </ul>
 */
public class MemberRecordChangesByProfileDTO {

    private Long detailId;

    private String operationTime;

    private String operationType;

    private Long operatorUid;

    private String auditContent;

    public MemberRecordChangesByProfileDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
