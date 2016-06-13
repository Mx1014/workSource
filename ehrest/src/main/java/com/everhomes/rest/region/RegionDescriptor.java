// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

public class RegionDescriptor {
    private Byte regionScope;
    private Long regionId;
    
    public RegionDescriptor() {
    }

    public Byte getRegionScope() {
        return regionScope;
    }

    public void setRegionScope(Byte regionScope) {
        this.regionScope = regionScope;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
