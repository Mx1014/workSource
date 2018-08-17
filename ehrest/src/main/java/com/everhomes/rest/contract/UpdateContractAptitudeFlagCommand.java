package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class UpdateContractAptitudeFlagCommand {
    Integer namespaceId;
    Long ownerId;
    Byte aptitudeFlag;
    Long id;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getAptitudeFlag() {
        return aptitudeFlag;
    }

    public void setAptitudeFlag(Byte aptitudeFlag) {
        this.aptitudeFlag = aptitudeFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
