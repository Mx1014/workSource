package com.everhomes.policy;

import com.everhomes.rest.policy.*;

import java.util.List;

public interface PolicyService {

    PolicyDTO createPolicy(CreatePolicyCommand cmd);
    PolicyDTO updatePolicy(UpdatePolicyCommand cmd);
    void deletePolicy(GetPolicyByIdCommand cmd);
    PolicyDTO searchPolicyById(GetPolicyByIdCommand cmd);

    List<PolicyDTO> listPoliciesByTitle(listPoliciesCommand cmd);
    List<PolicyDTO> searchPolicies(GetPolicyCommand cmd);
}
