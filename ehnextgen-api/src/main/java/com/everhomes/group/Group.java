// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.util.StringHelper;

public class Group extends EhGroups {
    private static final long serialVersionUID = -3345498176442950871L;

    public Group() {
    }
    
    public Long getOwningForumId() {
        return GroupCustomField.GROUP_FORUM.getIntegralValue(this);
    }
    
    public void setOwningForumId(Long forumId) {
        GroupCustomField.GROUP_FORUM.setIntegralValue(this, forumId);
    }
    
    public String getEnterpriseContact(){
    	return GroupCustomField.ENTERPRISE_CONTACTS_PHONE.getStringValue(this);
    }
    
    public void setEnterpriseContact(String cantact){
    	GroupCustomField.ENTERPRISE_CONTACTS_PHONE.setStringValue(this, cantact);
    }
    
    public String getEnterpriseAddress(){
    	return GroupCustomField.ENTERPRISE_ADDRESS_SEQUENCE.getStringValue(this);
    }
    
    public void setEnterpriseAddress(String address){
    	GroupCustomField.ENTERPRISE_ADDRESS_SEQUENCE.setStringValue(this, address);
    }
    
    public Long getFamilyAddressId(){
    	return GroupCustomField.FAMILY_ADDRESS_ID.getIntegralValue(this);
    }
    
    public void setFamilyAddressId(Long address){
    	GroupCustomField.FAMILY_ADDRESS_ID.setIntegralValue(this, address);
    }
    
    public Long getFamilyCommunityId(){
    	return GroupCustomField.FAMILY_COMMUNITY_ID.getIntegralValue(this);
    }
    
    public void setFamilyCommunityId(Long address){
    	GroupCustomField.FAMILY_COMMUNITY_ID.setIntegralValue(this, address);
    }
    

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
