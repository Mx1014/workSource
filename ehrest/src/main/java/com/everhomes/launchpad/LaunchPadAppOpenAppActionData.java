package com.everhomes.launchpad;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为APPOPEN_APP时启动第三方应用
 * <li>platform: 平台/li>
 * <li>embedded_json: 参数/li>
 * </ul>
 */
public class LaunchPadAppOpenAppActionData implements Serializable{

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
