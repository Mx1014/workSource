package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroups;
import com.everhomes.util.StringHelper;

public class EnterpriseContactGroup extends EhEnterpriseContactGroups {
    /**
     * 
     */
    private static final long serialVersionUID = 3428627741934274064L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    

    public String getApplyGroup() {
        return EnterpriseContactGroupCustomField.APPLYGROUP.getStringValue(this);
    }
    
    public void setApplyGroup(String applyGroup) {
    	EnterpriseContactGroupCustomField.APPLYGROUP.setStringValue(this, applyGroup);
    }
    
}
