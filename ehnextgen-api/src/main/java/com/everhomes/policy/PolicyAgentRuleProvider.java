package com.everhomes.policy;

public interface PolicyAgentRuleProvider {

    PolicyAgentRule createPolicyAgentRule(PolicyAgentRule bean);
    PolicyAgentRule updatePolicyAgentRule(PolicyAgentRule bean);
    PolicyAgentRule searchPolicyAgentRuleById(Integer namespaceId, String ownerType, Long ownerId, Long id);

}
