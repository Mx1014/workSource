package com.everhomes.community_map;

import com.everhomes.server.schema.tables.pojos.EhCommunityMapShops;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/25.
 */
public class CommunityMapShopDetail extends EhCommunityMapShops {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
