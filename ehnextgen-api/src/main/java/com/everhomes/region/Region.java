// @formatter:off
package com.everhomes.region;

import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.util.StringHelper;

public class Region extends EhRegions {
    private static final long serialVersionUID = -1733028032033126228L;

    public Region() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
