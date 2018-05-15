package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyByIdCommand;
import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;

import java.util.List;

public interface PolicyRecordService {
    PolicyRecordDTO createPolicyRecord(PolicyRecord policyRecord);
    PolicyRecordDTO searchPolicyRecordById(GetPolicyByIdCommand cmd);
    List<PolicyRecordDTO> searchPolicyRecords(GetPolicyRecordCommand cmd);
}
