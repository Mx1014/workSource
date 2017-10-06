package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhLeasePromotionCommunities;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/4.
 */
public class LeasePromotionCommunity extends EhLeasePromotionCommunities {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
