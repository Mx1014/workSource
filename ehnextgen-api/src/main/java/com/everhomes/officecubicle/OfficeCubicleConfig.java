package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleConfigs;
import com.everhomes.util.StringHelper;

public class OfficeCubicleConfig extends EhOfficeCubicleConfigs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
