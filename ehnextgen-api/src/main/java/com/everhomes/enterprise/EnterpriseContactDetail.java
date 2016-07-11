package com.everhomes.enterprise;

import com.everhomes.util.StringHelper;

public class EnterpriseContactDetail extends EnterpriseContact {

    /**
     * 
     */
    private static final long serialVersionUID = 1355908849128824460L;
    
    private String phone;
    private String groupName;
    
    public String getPhone() {
        return phone;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getGroupName() {
        return groupName;
    }



    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
