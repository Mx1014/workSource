// @formatter:off
package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.util.StringHelper;

public class CommunityGeoPoint extends EhCommunityGeopoints {
    private static final long serialVersionUID = 6160406157584577667L;

    public CommunityGeoPoint() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
