package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class AclinkUndoKeyDTO {
    private Byte     status;
    private Byte     keyId;
    private Long     createTimeMs;
    private Long     expireTimeMs;
    private Long     doorId;
    private Long     id;


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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

