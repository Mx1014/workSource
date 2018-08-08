package com.everhomes.policy;

import com.everhomes.rest.policy.GetPolicyByIdCommand;
import com.everhomes.rest.policy.GetPolicyRecordCommand;
import com.everhomes.rest.policy.PolicyRecordDTO;
import com.everhomes.rest.policy.PolicyRecordResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PolicyRecordService {
    PolicyRecordDTO createPolicyRecord(PolicyRecord policyRecord);
    PolicyRecordDTO searchPolicyRecordById(GetPolicyByIdCommand cmd);
    PolicyRecordResponse searchPolicyRecords(GetPolicyRecordCommand cmd);
    void exportPolicyRecords(GetPolicyRecordCommand cmd, HttpServletResponse resp);
}
