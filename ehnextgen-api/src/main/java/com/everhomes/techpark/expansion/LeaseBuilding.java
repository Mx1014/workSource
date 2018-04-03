package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/8/3.
 */
public class LeaseBuilding extends EhLeaseBuildings{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
