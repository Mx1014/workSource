package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhLaunchPadLayouts;
import com.everhomes.util.StringHelper;

public class LaunchPadLayout extends EhLaunchPadLayouts {
    
    private static final long serialVersionUID = -3033171693003646212L;
    
    public LaunchPadLayout() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
