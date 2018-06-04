package com.everhomes.policy;

import com.everhomes.server.schema.tables.pojos.EhPolicies;
import com.everhomes.util.StringHelper;

public class Policy extends EhPolicies {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
