package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 创建门禁日志
 * <li>eventType: com.everhomes.rest.aclink.AclinkLogEventType 0: PHONE_BLE_OPEN, 1: PHONE_QR_OPEN</li>
 * <li>logTime: The creating time of aclink log</li>
 * <li>doorId: doorAccess Id</li>
 * <li>remark: addition message for this log</li>
 * <li>authId: the auth Id</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkLogItem {
    @NotNull
    private Long     eventType;
    
    @NotNull
    private Long     logTime;
    
    @NotNull
    private Long     doorId;
    
    @NotNull
    private Integer     namespaceId;
    
    
    private String     remark;
    
    @NotNull
    private Long     authId;
    
    @NotNull
    private Long userId;
    
    private Long keyId;

    public Long getEventType() {
        return eventType;
    }

    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
