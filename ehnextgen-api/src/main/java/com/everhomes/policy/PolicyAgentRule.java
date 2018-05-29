package com.everhomes.policy;

import com.everhomes.server.schema.tables.pojos.EhPolicyAgentRules;
import com.everhomes.util.StringHelper;

public class PolicyAgentRule extends EhPolicyAgentRules {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
