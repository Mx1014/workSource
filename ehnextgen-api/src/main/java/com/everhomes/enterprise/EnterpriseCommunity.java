package com.everhomes.enterprise;

import com.everhomes.community.Community;
import com.everhomes.util.StringHelper;

public class EnterpriseCommunity extends Community {
    /**
     * 
     */
    private static final long serialVersionUID = 3805834666005091155L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
