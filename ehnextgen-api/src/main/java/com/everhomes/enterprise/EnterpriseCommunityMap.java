package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseCommunityMap;
import com.everhomes.util.StringHelper;

public class EnterpriseCommunityMap extends EhEnterpriseCommunityMap {
    /**
     * 
     */
    private static final long serialVersionUID = 1819638678632899486L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
