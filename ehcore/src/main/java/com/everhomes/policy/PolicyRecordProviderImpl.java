package com.everhomes.policy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyRecordProviderImpl implements PolicyRecordProvider {
    @Override
    public PolicyRecord createPolicyRecord(PolicyRecord policyRecord) {
        return null;
    }

    @Override
    public List<PolicyRecord> searchPolicyRecords(Integer namespaceId, String ownerType, Long ownerId, Long beginDate, Long endDate, String category, String keyword) {
        return null;
    }
}
