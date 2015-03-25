// @formatter:off
package com.everhomes.region;

import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.util.StringHelper;

public class Region extends EhRegions {
    private static final long serialVersionUID = -1733028032033126228L;

    public Region() {
    }
    
    public RegionAdminStatus getStatusEnum() {
        if(getStatus() != null)
            return RegionAdminStatus.fromCode(getStatus());
        
        return null;
    }
    
    public void setStatusEnum(RegionAdminStatus status) {
        if(status != null)
            setStatus(status.getCode());
        else
            setStatus(null);
    }
    
    public RegionScope getScopeEnum() {
        if(getScopeCode() != null)
            return RegionScope.fromCode(getScopeCode());
        return null;
    }
    
    public void setScopeEnum(RegionScope scope) {
        if(scope != null)
            setScopeCode(scope.getCode());
        else
            setScopeCode(null);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
