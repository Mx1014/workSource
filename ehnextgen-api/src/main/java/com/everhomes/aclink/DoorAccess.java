package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.util.StringHelper;

public class DoorAccess extends EhDoorAccess {
    /**
     * 
     */
    private static final long serialVersionUID = -9099421900054795087L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
    public String getDisplayNameNotEmpty() {
        if(this.getDisplayName() == null) {
            return this.getName();
        }
        
        return this.getDisplayName();
    }
    
    public boolean isVip() {
        if(this.getName() != null && this.getName().toLowerCase().indexOf("vip") >= 0) {
            return true;
        }
        
        return false;
    }
}
