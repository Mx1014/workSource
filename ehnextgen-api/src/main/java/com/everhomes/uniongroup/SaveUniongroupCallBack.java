package com.everhomes.uniongroup;

import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;

public interface SaveUniongroupCallBack {
    UnionPolicyObject policyProcess(SaveUniongroupConfiguresCommand cmd);
}
