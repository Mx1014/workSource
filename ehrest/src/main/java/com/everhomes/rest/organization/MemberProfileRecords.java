package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by R on 2017/5/22.
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>operationTime: 修改时间</li>
 * <li>operationType: 新增、修改、删除</li>
 * <li>operatorUid: 操作人员编号</li>
 * <li>auditContent: 修改内容</li>
 * </ul>
 */
public class MemberProfileRecords {

    private Long memberId;

    private String operationTime;

    private String operationType;

    private Long operatorUid;

    private String auditContent;

    public MemberProfileRecords() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
