// @formatter:off
package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactName：当前用户真实姓名</li>
 * </ul>
 */
public class CurrentUserInfoDTO {

    private String contactName;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
