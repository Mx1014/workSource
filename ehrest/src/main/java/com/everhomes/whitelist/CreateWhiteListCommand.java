// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间[必填]</li>
 *     <li>phoneNumber: 白名单手机号[必填]</li>
 * </ul>
 */
public class CreateWhiteListCommand {

    @NotNull
    private Integer namespaceId;
    @NotNull
    private String phoneNumber;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
