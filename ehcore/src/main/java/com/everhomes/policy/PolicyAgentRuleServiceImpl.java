package com.everhomes.policy;

import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import com.everhomes.rest.policy.GetPolicyAgentRuleCommand;
import com.everhomes.rest.policy.PolicyAgentRuleDTO;
import com.everhomes.rest.policy.SetPolicyAgentRuleCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PolicyAgentRuleServiceImpl implements PolicyAgentRuleService {

    @Autowired
    private PolicyAgentRuleProvider policyAgentRuleProvider;

    @Override
    public PolicyAgentRuleDTO setPolicyAgentRule(SetPolicyAgentRuleCommand cmd) {
        User user = UserContext.current().getUser();
        PolicyAgentRule result = policyAgentRuleProvider.searchPolicyAgentRuleById(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
        if(null != result){
            if(null != cmd.getAgentPhone())
                result.setAgentPhone(cmd.getAgentPhone());
            if(null != cmd.getAgentInfo())
                result.setAgentInfo(cmd.getAgentInfo());
            if(null != cmd.getAgentFlag()){
                result.setAgentFlag(cmd.getAgentFlag());
//                if(cmd.getAgentFlag().equals((byte) 0)){
//                    result.setAgentPhone("");
//                    result.setAgentInfo("");
//                }
            }
            result.setUpdaterUid(user.getId());
            policyAgentRuleProvider.updatePolicyAgentRule(result);
        } else {
            result = ConvertHelper.convert(cmd, PolicyAgentRule.class);
            result.setCreatorId(user.getId());
            policyAgentRuleProvider.createPolicyAgentRule(result);
        }
        return ConvertHelper.convert(result,PolicyAgentRuleDTO.class);
    }

    @Override
    public PolicyAgentRuleDTO searchPolicyAgentRuleById(GetPolicyAgentRuleCommand cmd) {

        PolicyAgentRule result = policyAgentRuleProvider.searchPolicyAgentRuleById(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
        return ConvertHelper.convert(result,PolicyAgentRuleDTO.class);
    }
}
