package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhCommunityBizs;
import com.everhomes.util.StringHelper;

public class CommunityBiz extends EhCommunityBizs {

    private static final long serialVersionUID = -305804887870538989L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
