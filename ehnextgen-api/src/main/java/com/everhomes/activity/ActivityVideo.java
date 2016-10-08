package com.everhomes.activity;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhActivityVideo;

public class ActivityVideo extends EhActivityVideo {

    /**
     * 
     */
    private static final long serialVersionUID = -2229142568742160261L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
