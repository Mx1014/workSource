package com.everhomes.policy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.policy.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PolicyServiceImpl implements PolicyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    private PolicyProvider policyProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

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
    public void deletePolicy(GetPolicyByIdCommand cmd) {
        if(null != cmd.getId()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"id is empty");
        }
        policyProvider.deletePolicyById(cmd.getId());
    }

    @Override
    public PolicyDTO searchPolicyById(GetPolicyByIdCommand cmd) {
        return null;
    }

    @Override
    public PolicyResponse listPoliciesByTitle(listPoliciesCommand cmd) {
        this.checkNamespaceNOwnerId(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

        return null;
    }

    @Override
    public PolicyResponse searchPolicies(GetPolicyCommand cmd) {
        return null;
    }

    private void checkNamespaceNOwnerId(Integer namespaceId, String ownerType, Long ownerId){
        if(null == namespaceId){
            LOGGER.error("Invalid namespaceId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid namespaceId parameter.");
        }

        if(StringUtils.isEmpty(ownerType)){
            LOGGER.error("Invalid ownerType parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid ownerType parameter.");
        }

        if(null == ownerId){
            LOGGER.error("Invalid ownerId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid ownerId parameter.");
        }

    }

    private List<Long> getOwnerIds(Long ownerId,Long orgId){
        List<Long> ownerIds = new ArrayList<>();
        if(null == ownerId || -1L == ownerId){
            ListUserRelatedProjectByModuleCommand cmd = new ListUserRelatedProjectByModuleCommand();
            cmd.setModuleId(41900L);
//			cmd.setAppId(cmd.getAppId());
            cmd.setOrganizationId(orgId);
            List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd);
            ownerIds.addAll(dtos.stream().map(elem ->{return elem.getProjectId();}).collect(Collectors.toList()));
        } else {
            ownerIds.add(ownerId);
        }
        return ownerIds;
    }
}
