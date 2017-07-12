// @formatter:off
package com.everhomes.region;

import com.everhomes.server.schema.tables.pojos.EhRegionCodes;
import com.everhomes.util.StringHelper;

public class RegionCodes extends EhRegionCodes {

    private static final long serialVersionUID = -1733028032033126228L;

    public RegionCodes() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
