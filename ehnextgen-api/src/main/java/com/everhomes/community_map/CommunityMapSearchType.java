package com.everhomes.community_map;

import com.everhomes.server.schema.tables.pojos.EhCommunityMapSearchTypes;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapSearchType extends EhCommunityMapSearchTypes {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
