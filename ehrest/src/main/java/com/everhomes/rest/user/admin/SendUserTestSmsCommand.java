package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

public class SendUserTestSmsCommand {
	private Integer namespaceId;
    
	private String phoneNumber;

    private Integer regionCode;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
