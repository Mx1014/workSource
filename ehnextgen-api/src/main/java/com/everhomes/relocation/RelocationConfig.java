package com.everhomes.relocation;

import com.everhomes.server.schema.tables.pojos.EhRelocationConfigs;
import com.everhomes.util.StringHelper;

public class RelocationConfig extends EhRelocationConfigs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
