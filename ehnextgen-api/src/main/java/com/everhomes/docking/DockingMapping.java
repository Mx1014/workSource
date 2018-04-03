package com.everhomes.docking;

import com.everhomes.server.schema.tables.pojos.EhDockingMappings;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/3/2.
 */
public class DockingMapping extends EhDockingMappings {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
