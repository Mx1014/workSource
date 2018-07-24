package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyAgentRuleCommand;
import com.everhomes.rest.policy.PolicyAgentRuleDTO;
import com.everhomes.rest.policy.SetPolicyAgentRuleCommand;

public interface PolicyAgentRuleService {
    PolicyAgentRuleDTO setPolicyAgentRule(SetPolicyAgentRuleCommand cmd);
    PolicyAgentRuleDTO searchPolicyAgentRuleById(GetPolicyAgentRuleCommand cmd);
}
