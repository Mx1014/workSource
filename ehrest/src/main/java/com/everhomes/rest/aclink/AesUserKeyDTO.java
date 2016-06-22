package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class AesUserKeyDTO {
    private Byte     status;
    private Byte     keyId;
    private Long     createTimeMs;
    private Long     creatorUid;
    private Byte     keyType;
    private Long     userId;
    private String     secret;
    private Long     expireTimeMs;
    private Long     doorId;
    private Long     id;
    private String hardwareId;
    private String doorName;
    private Long authId;

    

    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public Byte getKeyId() {
        return keyId;
    }



    public void setKeyId(Byte keyId) {
        this.keyId = keyId;
    }



    public Long getCreateTimeMs() {
        return createTimeMs;
    }



    public void setCreateTimeMs(Long createTimeMs) {
        this.createTimeMs = createTimeMs;
    }



    public Long getCreatorUid() {
        return creatorUid;
    }



    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }



    public Byte getKeyType() {
        return keyType;
    }



    public void setKeyType(Byte keyType) {
        this.keyType = keyType;
    }



    public Long getUserId() {
        return userId;
    }



    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public String getSecret() {
        return secret;
    }



    public void setSecret(String secret) {
        this.secret = secret;
    }



    public Long getExpireTimeMs() {
        return expireTimeMs;
    }



    public void setExpireTimeMs(Long expireTimeMs) {
        this.expireTimeMs = expireTimeMs;
    }



    public Long getDoorId() {
        return doorId;
    }



    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getHardwareId() {
        return hardwareId;
    }



    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }



    public String getDoorName() {
        return doorName;
    }



    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }



    public Long getAuthId() {
        return authId;
    }



    public void setAuthId(Long authId) {
        this.authId = authId;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
