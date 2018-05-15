package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyByIdCommand;
import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyRecordServiceImpl implements PolicyRecordService {
    @Override
    public PolicyRecordDTO createPolicyRecord(PolicyRecord policyRecord) {
        return null;
    }

    @Override
    public PolicyRecordDTO searchPolicyRecordById(GetPolicyByIdCommand cmd) {
        return null;
    }

    @Override
    public List<PolicyRecordDTO> searchPolicyRecords(GetPolicyRecordCommand cmd) {
        return null;
    }
}
