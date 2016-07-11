package com.everhomes.aclink;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAclinks;

public class Aclink extends EhAclinks {
    /**
     * 
     */
    private static final long serialVersionUID = -3789709481172132329L;

    public Long getLinglingDoorId() {
        return AclinkCustomField.AUTH_LINGLING_DOOR_ID.getIntegralValue(this);
    }
    
    public void setLinglingDoorId(Long doorId) {
        AclinkCustomField.AUTH_LINGLING_DOOR_ID.setIntegralValue(this, doorId);
    }

    public String getLinglingSDKKey() {
        return AclinkCustomField.AUTH_LINGLING_SDK_KEY.getStringValue(this);
    }
    
    public void setLinglingSDKKey(String key) {
        AclinkCustomField.AUTH_LINGLING_SDK_KEY.setStringValue(this, key);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
