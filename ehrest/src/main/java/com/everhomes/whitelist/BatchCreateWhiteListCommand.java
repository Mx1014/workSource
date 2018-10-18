// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>phoneNumbers: 白名单手机号码组,英文逗号分隔</li>
 * </ul>
 */
public class BatchCreateWhiteListCommand {

    @NotNull
    private Integer namespaceId;
    @NotNull
    private String phoneNumbers;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
