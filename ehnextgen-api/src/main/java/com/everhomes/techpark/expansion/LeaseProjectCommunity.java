package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhLeaseProjectCommunities;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/18.
 */
public class LeaseProjectCommunity extends EhLeaseProjectCommunities {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
