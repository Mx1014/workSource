package com.everhomes.activity;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhYzbDevices;

public class YzbDevice extends EhYzbDevices {
    /**
     * 
     */
    private static final long serialVersionUID = -4084867254699206534L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
