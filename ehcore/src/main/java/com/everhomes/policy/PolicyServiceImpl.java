package com.everhomes.policy;

import com.everhomes.rest.policy.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyServiceImpl implements PolicyService {
    @Override
    public PolicyDTO createPolicy(CreatePolicyCommand cmd) {
        return null;
    }

    @Override
    public PolicyDTO updatePolicy(UpdatePolicyCommand cmd) {
        return null;
    }

    @Override
    public void deletePolicy(DeletePolicyCommand cmd) {

    }

    @Override
    public List<PolicyDTO> listPoliciesByTitle(listPoliciesCommand cmd) {
        return null;
    }

    @Override
    public List<PolicyDTO> searchPolicies(GetPolicyCommand cmd) {
        return null;
    }
}
