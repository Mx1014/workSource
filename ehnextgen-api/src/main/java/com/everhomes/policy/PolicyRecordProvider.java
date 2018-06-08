package com.everhomes.policy;


import java.util.List;

public interface PolicyRecordProvider {

    PolicyRecord createPolicyRecord(PolicyRecord policyRecord);
    PolicyRecord searchPolicyRecordById(Long id);
    List<PolicyRecord> searchPolicyRecords(Integer namespaceId, String ownerType, List<Long> ownerIds, Long beginDate, Long endDate, Long category, String keyword, Long pageAnchor, Integer pageSize);
}
