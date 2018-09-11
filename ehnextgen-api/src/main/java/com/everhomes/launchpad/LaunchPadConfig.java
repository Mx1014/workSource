package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhLaunchPadConfigs;
import com.everhomes.util.StringHelper;

public class LaunchPadConfig extends EhLaunchPadConfigs {


    private static final long serialVersionUID = -3346908880187723605L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
