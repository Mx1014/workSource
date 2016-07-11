package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserLikes;
import com.everhomes.util.StringHelper;

public class UserLike extends EhUserLikes {
    private static final long serialVersionUID = -6613452434674014716L;

    public UserLike() {
    }
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
