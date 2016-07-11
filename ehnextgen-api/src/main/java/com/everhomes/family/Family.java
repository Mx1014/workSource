// @formatter:off
package com.everhomes.family;

import com.everhomes.group.Group;
import com.everhomes.group.GroupCustomField;
import com.everhomes.util.StringHelper;

public class Family extends Group {
    private static final long serialVersionUID = -8036291734478614467L;

    public Family() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
    public Long getAddressId() {
        return GroupCustomField.FAMILY_ADDRESS_ID.getIntegralValue(this);
    }
    
    public void setAddressId(Long addressId) {
        GroupCustomField.FAMILY_ADDRESS_ID.setIntegralValue(this, addressId);
    }
    
    public Long getCommunityId() {
        return GroupCustomField.FAMILY_COMMUNITY_ID.getIntegralValue(this);
    }
    
    public void setCommunityId(Long communityId) {
        GroupCustomField.FAMILY_COMMUNITY_ID.setIntegralValue(this, communityId);
    }
    
    public Long getCityId(){
        return GroupCustomField.FAMILY_CITY_ID.getIntegralValue(this);
    }
    
    public void setCityId(Long cityId) {
        GroupCustomField.FAMILY_CITY_ID.setIntegralValue(this, cityId);
    }
    
}
