package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>operationTime: 修改时间</li>
 * <li>personChangeType: 人员变动类型，参考{@link PersonChangeType}</li>
 * <li>reason: 操作备注</li>
 * </ul>
 */
public class MemberRecordChangesByJobDTO {


    private String operationTime;

    private String personChangeType;

    private String reason;

    public MemberRecordChangesByJobDTO() {
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
