package com.everhomes.policy;


import java.util.List;

public interface PolicyRecordProvider {

    PolicyRecord createPolicyRecord(PolicyRecord policyRecord);
    List<PolicyRecord> searchPolicyRecords(Integer namespaceId, String ownerType, Long ownerId, Long beginDate, Long endDate, String category, String keyword);
}
