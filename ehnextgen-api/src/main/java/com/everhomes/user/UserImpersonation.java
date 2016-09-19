package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserImpersonations;
import com.everhomes.util.StringHelper;

public class UserImpersonation extends EhUserImpersonations {
    /**
     * 
     */
    private static final long serialVersionUID = -3204238277241691206L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
