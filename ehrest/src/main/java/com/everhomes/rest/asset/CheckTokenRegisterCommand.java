package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * contactNo: 催缴手机号
 */
public class CheckTokenRegisterCommand {

    private String contactNo;

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
