package com.everhomes.policy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyProviderImpl implements PolicyProvider {
    @Override
    public Policy createPolicy(Policy policy) {
        return null;
    }

    @Override
    public Policy updatePolicy(Policy policy) {
        return null;
    }

    @Override
    public void deletePolicyById(Long id) {

    }

    @Override
    public List<Policy> listPoliciesByTitle(Integer namespaceId, String ownerType, Long ownerId, String title) {
        return null;
    }

    @Override
    public List<Policy> searchPoliciesByCategory(Integer namespaceId, String ownerType, Long ownerId, String category) {
        return null;
    }
}
