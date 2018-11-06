// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class SendVerificationCodeCommand {
    private Integer namespaceId;

    private String phoneNumber;

    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
