package com.everhomes.policy;

import java.util.List;

public interface PolicyProvider {
    Policy createPolicy(Policy policy);
    Policy updatePolicy(Policy policy);
    void deletePolicyById(Long id);
    Policy searchPolicyById(Long id);
    List<Policy> listPoliciesByTitle(Integer namespaceId, String ownerType, List<Long> ownerId, String title, Long pageAnchor, Integer pageSize);
    List<Policy> searchPoliciesByIds(Integer namespaceId, String ownerType,Long ownerId, List<Long> ids, Long pageAnchor, Integer pageSize);
}
