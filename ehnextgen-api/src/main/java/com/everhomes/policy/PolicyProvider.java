package com.everhomes.policy;

import java.util.List;

public interface PolicyProvider {
    Policy createPolicy(Policy policy);
    Policy updatePolicy(Policy policy);
    void deletePolicyById(Long id);

    List<Policy> listPoliciesByTitle(Integer namespaceId, String ownerType, Long ownerId, String title);
    List<Policy> searchPoliciesByCategory(Integer namespaceId, String ownerType, Long ownerId, String category);
}
