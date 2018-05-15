package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;

import java.util.List;

public interface PolicyRecordService {
    PolicyRecordDTO createPolicyRecord(PolicyRecord policyRecord);
    List<PolicyRecordDTO> searchPolicyRecords(GetPolicyRecordCommand cmd);
}
