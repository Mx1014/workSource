package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhLeaseProjects;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/17.
 */
public class LeaseProject extends EhLeaseProjects {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
