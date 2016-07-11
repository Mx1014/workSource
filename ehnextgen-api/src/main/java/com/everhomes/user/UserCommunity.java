package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserCommunities;
import com.everhomes.util.StringHelper;

public class UserCommunity extends EhUserCommunities {
    private static final long serialVersionUID = -6613452434674014716L;

    public UserCommunity() {
    }
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
