package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyByIdCommand;
import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;
import com.everhomes.rest.policy.PolicyRecordResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
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
    public PolicyRecordResponse searchPolicyRecords(GetPolicyRecordCommand cmd) {
        return null;
    }

    @Override
    public void exportPolicyRecords(GetPolicyRecordCommand cmd, HttpServletResponse resp) {
        
    }
}
