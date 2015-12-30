package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>userCount:用户数</li>
 *         <li>contacts:{@link Contact}</li>
 *         </ul>
 */
public class CommunityStatusResponse {
    private Integer userCount;
    @ItemType(Contact.class)
    private List<Contact> contacts;

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
