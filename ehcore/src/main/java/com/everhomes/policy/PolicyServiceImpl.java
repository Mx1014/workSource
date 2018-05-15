package com.everhomes.policy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.policy.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import jdk.nashorn.internal.runtime.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyServiceImpl implements PolicyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    private PolicyProvider policyProvider;

    @Override
    public PolicyDTO createPolicy(CreatePolicyCommand cmd) {
        User user = UserContext.current().getUser();
        Policy policy = ConvertHelper.convert(cmd,Policy.class);
        policy.setCreatorUid(user.getId());
        Policy result = policyProvider.createPolicy(policy);
        return ConvertHelper.convert(result,PolicyDTO.class);
    }

    @Override
    public PolicyDTO updatePolicy(UpdatePolicyCommand cmd) {
        User user = UserContext.current().getUser();
        if(null == cmd.getId()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"id is empty");
        }
        Policy policy = policyProvider.searchPolicyById(cmd.getId());
        if(null != cmd.getTitle())
            policy.setTitle(cmd.getTitle());
        if(StringUtils.isNotEmpty(cmd.getOutline()))
            policy.setOutline(cmd.getOutline());
        if(StringUtils.isNotEmpty(cmd.getContent()))
            policy.setContent(cmd.getContent());
        policy.setCreatorUid(user.getId());
        Policy result = policyProvider.updatePolicy(policy);
        return ConvertHelper.convert(result,PolicyDTO.class);
    }

    @Override
    public void deletePolicy(DeletePolicyCommand cmd) {
        if(null != cmd.getId()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"id is empty");
        }
        policyProvider.deletePolicyById(cmd.getId());
    }

    @Override
    public List<PolicyDTO> listPoliciesByTitle(listPoliciesCommand cmd) {

        return null;
    }

    @Override
    public List<PolicyDTO> searchPolicies(GetPolicyCommand cmd) {
        return null;
    }

//    private void checkNamespaceNOwnerId(){
//        if(null == ){}
//
//    }
}
