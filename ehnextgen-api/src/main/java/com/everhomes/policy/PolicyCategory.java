package com.everhomes.policy;

import com.everhomes.server.schema.tables.pojos.EhPolicyCategories;
import com.everhomes.util.StringHelper;

public class PolicyCategory extends EhPolicyCategories {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
