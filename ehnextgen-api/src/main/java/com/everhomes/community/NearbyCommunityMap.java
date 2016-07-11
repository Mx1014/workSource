// @formatter:off
package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhNearbyCommunityMap;
import com.everhomes.util.StringHelper;

public class NearbyCommunityMap extends EhNearbyCommunityMap {
    private static final long serialVersionUID = 6160406157584577667L;

    public NearbyCommunityMap() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
