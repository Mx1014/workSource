package com.everhomes.launchpad;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为PHONE_CALL时拨电话
 * <li>phone: 电话号码/li>
 * </ul>
 */
public class LaunchPadPhoneCallActionData implements Serializable{

    private static final long serialVersionUID = -5364366676212368720L;
    //{"phone": "15875301110"}  
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
