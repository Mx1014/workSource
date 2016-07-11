package com.everhomes.pushmessage;

import com.everhomes.server.schema.tables.pojos.EhPushMessageResults;
import com.everhomes.util.StringHelper;

public class PushMessageResult extends EhPushMessageResults {
    /**
     * 
     */
    private static final long serialVersionUID = 7544816091702528720L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
