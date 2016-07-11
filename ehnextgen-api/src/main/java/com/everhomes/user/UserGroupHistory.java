package com.everhomes.user;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhUserGroupHistories;

public class UserGroupHistory extends EhUserGroupHistories {
    /**
     * 
     */
    private static final long serialVersionUID = -5412673070829024986L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
