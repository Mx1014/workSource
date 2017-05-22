package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>operationTime: 修改时间</li>
 * <li>personChangeType: 人员变动类型，只记录入职、转正、离职、变更部门、岗位、职级</li>
 * <li>reason: 操作备注</li>
 * </ul>
 */
public class MemberJobRecordsDTO {

    private Long memberId;

    private String operationTime;

    private String personChangeType;

    private String reason;

    public MemberJobRecordsDTO() {
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

    public String getPersonChangeType() {
        return personChangeType;
    }

    public void setPersonChangeType(String personChangeType) {
        this.personChangeType = personChangeType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
