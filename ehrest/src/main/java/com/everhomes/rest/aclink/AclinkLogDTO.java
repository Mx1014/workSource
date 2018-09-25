package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *     <li>userName:用户姓名</li>
 *     <li>userIdentifier:手机号码</li>
 *     <li>doorName:门禁名称</li>
 *     <li>authType:授权类型 (0为常规授权，1为临时授权)</li>
 *     <li>eventType:开门方式 {@link com.everhomes.rest.aclink.AclinkLogEventType}</li>
 *     <li>logTime:开门时间 </li>
 * </ul>
 */
public class AclinkLogDTO {
    private Long     keyId;
    private Long     eventType;
    private Byte     ownerType;
    private String     ownerName;
    private Long     logTime;
    private Long     id;
    private String     doorName;
    private String     hardwareId;
    private Timestamp     createTime;
    private Long     doorId;
    private Long     userId;
    private Byte     doorType;
    private Integer     namespaceId;
    private String     userName;
    private String     remark;
    private Long     authId;
    private String     userIdentifier;
    private Long     ownerId;
    private Byte     authType;

    public Long getKeyId() {
        return keyId;
    }


    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }


    public Long getEventType() {
        return eventType;
    }


    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }


    public Byte getOwnerType() {
        return ownerType;
    }


    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }


    public String getOwnerName() {
        return ownerName;
    }


    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    public Long getLogTime() {
        return logTime;
    }


    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getDoorName() {
        return doorName;
    }


    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }


    public String getHardwareId() {
        return hardwareId;
    }


    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Long getDoorId() {
        return doorId;
    }


    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Byte getDoorType() {
        return doorType;
    }


    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }


    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
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


    public String getUserIdentifier() {
        return userIdentifier;
    }


    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }


    public Long getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Byte getAuthType() {
        return authType;
    }


    public void setAuthType(Byte authType) {
        this.authType = authType;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
