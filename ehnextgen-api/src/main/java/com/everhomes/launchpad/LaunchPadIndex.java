package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhLaunchPadIndexs;
import com.everhomes.util.StringHelper;

public class LaunchPadIndex extends EhLaunchPadIndexs {
    /**
     * 
     */
    private static final long serialVersionUID = 7742877707886977940L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
