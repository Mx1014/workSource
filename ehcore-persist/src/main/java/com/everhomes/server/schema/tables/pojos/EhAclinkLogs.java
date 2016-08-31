/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhAclinkLogs implements Serializable {
    private static final long serialVersionUID = 794332554L;
    private Long id;
    private Integer namespaceId;
    private Long eventType;
    private Byte doorType;
    private Long doorId;
    private String hardwareId;
    private String doorName;
    private Byte ownerType;
    private Long ownerId;
    private String ownerName;
    private Long userId;
    private Long keyId;
    private Long authId;
    private String userName;
    private String userIdentifier;
    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private String stringTag4;
    private String stringTag5;
    private String stringTag6;
    private Long integralTag1;
    private Long integralTag2;
    private Long integralTag3;
    private Long integralTag4;
    private Long integralTag5;
    private Long integralTag6;
    private String remark;
    private Long logTime;
    private Timestamp createTime;

    public EhAclinkLogs() {
    }

    public EhAclinkLogs(Long id, Integer namespaceId, Long eventType, Byte doorType, Long doorId, String hardwareId,
            String doorName, Byte ownerType, Long ownerId, String ownerName, Long userId, Long keyId, Long authId,
            String userName, String userIdentifier, String stringTag1, String stringTag2, String stringTag3,
            String stringTag4, String stringTag5, String stringTag6, Long integralTag1, Long integralTag2,
            Long integralTag3, Long integralTag4, Long integralTag5, Long integralTag6, String remark, Long logTime,
            Timestamp createTime) {
        this.id = id;
        this.namespaceId = namespaceId;
        this.eventType = eventType;
        this.doorType = doorType;
        this.doorId = doorId;
        this.hardwareId = hardwareId;
        this.doorName = doorName;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.userId = userId;
        this.keyId = keyId;
        this.authId = authId;
        this.userName = userName;
        this.userIdentifier = userIdentifier;
        this.stringTag1 = stringTag1;
        this.stringTag2 = stringTag2;
        this.stringTag3 = stringTag3;
        this.stringTag4 = stringTag4;
        this.stringTag5 = stringTag5;
        this.stringTag6 = stringTag6;
        this.integralTag1 = integralTag1;
        this.integralTag2 = integralTag2;
        this.integralTag3 = integralTag3;
        this.integralTag4 = integralTag4;
        this.integralTag5 = integralTag5;
        this.integralTag6 = integralTag6;
        this.remark = remark;
        this.logTime = logTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return this.namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getEventType() {
        return this.eventType;
    }

    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }

    public Byte getDoorType() {
        return this.doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    public Long getDoorId() {
        return this.doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public String getHardwareId() {
        return this.hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getDoorName() {
        return this.doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public Byte getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getKeyId() {
        return this.keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public Long getAuthId() {
        return this.authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getStringTag1() {
        return this.stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return this.stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return this.stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public String getStringTag4() {
        return this.stringTag4;
    }

    public void setStringTag4(String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public String getStringTag5() {
        return this.stringTag5;
    }

    public void setStringTag5(String stringTag5) {
        this.stringTag5 = stringTag5;
    }

    public String getStringTag6() {
        return this.stringTag6;
    }

    public void setStringTag6(String stringTag6) {
        this.stringTag6 = stringTag6;
    }

    public Long getIntegralTag1() {
        return this.integralTag1;
    }

    public void setIntegralTag1(Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public Long getIntegralTag2() {
        return this.integralTag2;
    }

    public void setIntegralTag2(Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public Long getIntegralTag3() {
        return this.integralTag3;
    }

    public void setIntegralTag3(Long integralTag3) {
        this.integralTag3 = integralTag3;
    }

    public Long getIntegralTag4() {
        return this.integralTag4;
    }

    public void setIntegralTag4(Long integralTag4) {
        this.integralTag4 = integralTag4;
    }

    public Long getIntegralTag5() {
        return this.integralTag5;
    }

    public void setIntegralTag5(Long integralTag5) {
        this.integralTag5 = integralTag5;
    }

    public Long getIntegralTag6() {
        return this.integralTag6;
    }

    public void setIntegralTag6(Long integralTag6) {
        this.integralTag6 = integralTag6;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getLogTime() {
        return this.logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
