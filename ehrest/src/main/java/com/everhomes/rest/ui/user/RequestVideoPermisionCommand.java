package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>phone: 可以为空，默认用当前用户的手机号。</li>
 * <li>manufacturerType: YZB </li>
 * </ul>
 * @author janson
 *
 */
public class RequestVideoPermisionCommand {
    private String manufacturerType;
    private String phone;
    
    public String getManufacturerType() {
        return manufacturerType;
    }
    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
