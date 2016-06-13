package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>name:名字</li>
 *         <li>phone:手机</li>
 *         </ul>
 */
public class Contact {
    private String contactPhone;
    private String contactName;

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

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
