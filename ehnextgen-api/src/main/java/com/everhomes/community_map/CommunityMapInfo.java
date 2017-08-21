package com.everhomes.community_map;

import com.everhomes.server.schema.tables.pojos.EhCommunityMapInfos;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/21.
 */
public class CommunityMapInfo extends EhCommunityMapInfos {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
