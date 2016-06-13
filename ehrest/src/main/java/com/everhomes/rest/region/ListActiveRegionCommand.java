// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scope: 范围，参考{@link com.everhomes.rest.region.RegionScope}</li>
 * </ul>
 */
public class ListActiveRegionCommand {
    private Byte scope;
    
    public ListActiveRegionCommand() {
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
