package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>contactToken：手机号</li>
 * </ul>
 */
public class GetPersonelInfoByTokenCommand {
    private String contactToken;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }
}
