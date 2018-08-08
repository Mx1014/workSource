package com.everhomes.policy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.policy.*;
import com.everhomes.server.schema.tables.pojos.EhPolicyCategories;
import com.everhomes.settings.PaginationConfigHelper;
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
    private PolicyCategoryService policyCategoryService;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private PolicyRecordService policyRecordService;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public PolicyDTO createPolicy(CreatePolicyCommand cmd) {
        User user = UserContext.current().getUser();
        Policy policy = ConvertHelper.convert(cmd,Policy.class);
        policy.setCreatorUid(user.getId());
        Policy result = policyProvider.createPolicy(policy);
        policyCategoryService.setPolicyCategory(result.getId(),cmd.getCategoryIds());
        return ConvertHelper.convert(result,PolicyDTO.class);
    }

    @Override
    public PolicyDTO updatePolicy(UpdatePolicyCommand cmd) {
        User user = UserContext.current().getUser();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid Id parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid Id parameter.");
        }
        Policy policy = policyProvider.searchPolicyById(cmd.getId());
        if(StringUtils.isNotEmpty(cmd.getTitle()))
            policy.setTitle(cmd.getTitle());
        if(StringUtils.isNotEmpty(cmd.getOutline()))
            policy.setOutline(cmd.getOutline());
        if(StringUtils.isNotEmpty(cmd.getContent()))
            policy.setContent(cmd.getContent());
        policy.setCreatorUid(user.getId());
        Policy result = policyProvider.updatePolicy(policy);
        policyCategoryService.setPolicyCategory(result.getId(),cmd.getCategoryIds());
        PolicyDTO dto = ConvertHelper.convert(result,PolicyDTO.class);
        List<PolicyCategory> ctgs = policyCategoryService.searchPolicyCategoryByPolicyId(dto.getId(),(byte) 1);
        dto.setCategoryIds(ctgs.stream().map(EhPolicyCategories::getCategoryId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void deletePolicy(GetPolicyByIdCommand cmd) {
        if(null == cmd.getId()){
            LOGGER.error("Invalid Id parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid Id parameter.");
        }
        policyProvider.deletePolicyById(cmd.getId());
        policyCategoryService.deletePolicyCategoryByPolicyId(cmd.getId());
    }

    @Override
    public PolicyDTO searchPolicyById(GetPolicyByIdCommand cmd) {
        Policy result = policyProvider.searchPolicyById(cmd.getId());
        if(null == cmd.getNamespaceId() && result.getNamespaceId() != cmd.getNamespaceId()){
            LOGGER.error("Invalid namespaceId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid namespaceId parameter.");
        }
        PolicyDTO dto = ConvertHelper.convert(result,PolicyDTO.class);
        List<PolicyCategory> ctgs = policyCategoryService.searchPolicyCategoryByPolicyId(dto.getId(),(byte) 1);
        dto.setCategoryIds(ctgs.stream().map(EhPolicyCategories::getCategoryId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public PolicyResponse listPoliciesByTitle(listPoliciesCommand cmd) {
        this.checkNamespaceNOwnerId(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
        List<Long> ownerIds = this.getOwnerIds(cmd.getOwnerId(),cmd.getCurrentPMId());
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider,cmd.getPageSize()));
        PolicyResponse resp = new PolicyResponse();
        List<Policy> results = policyProvider.listPoliciesByTitle(cmd.getNamespaceId(),cmd.getOwnerType(), ownerIds, cmd.getTitle(), cmd.getPageAnchor(), cmd.getPageSize());
        if(results.size() > 0)
            resp.setNextPageAnchor(results.get(results.size() - 1).getId());
        resp.setDtos(results.stream().map(r -> {
            PolicyDTO dto = ConvertHelper.convert(r,PolicyDTO.class);
            List<PolicyCategory> ctgs = policyCategoryService.searchPolicyCategoryByPolicyId(dto.getId(),(byte) 1);
            dto.setCategoryIds(ctgs.stream().map(EhPolicyCategories::getCategoryId).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public PolicyResponse searchPolicies(GetPolicyCommand cmd) {

        cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider,cmd.getPageSize()));

        if(null == cmd.getPageAnchor()) {
            PolicyRecord record = ConvertHelper.convert(cmd,PolicyRecord.class);
            policyRecordService.createPolicyRecord(record);
        }

        PolicyResponse resp = new PolicyResponse();
        List<Long> ids = policyCategoryService.searchPolicyByCategory(cmd.getCategoryId()).stream().map(PolicyCategory::getPolicyId).collect(Collectors.toList());
        List<Policy> results = policyProvider.searchPoliciesByIds(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),ids,cmd.getPageAnchor(),cmd.getPageSize());
        if(results.size() > 0)
            resp.setNextPageAnchor(results.get(0).getId());
        resp.setDtos(results.stream().map(r -> {
            PolicyDTO dto = ConvertHelper.convert(r,PolicyDTO.class);
            List<PolicyCategory> ctgs = policyCategoryService.searchPolicyCategoryByPolicyId(dto.getId(),(byte) 1);
            dto.setCategoryIds(ctgs.stream().map(EhPolicyCategories::getCategoryId).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList()));
        return resp;
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
        ownerIds.add(-1L);
        if(null == ownerId || -1L == ownerId){
            ListUserRelatedProjectByModuleCommand cmd = new ListUserRelatedProjectByModuleCommand();
            cmd.setModuleId(41900L);
//			cmd.setAppId(cmd.getAppId());
            cmd.setOrganizationId(orgId);
            List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd);
            ownerIds.addAll(dtos.stream().map(elem -> elem.getProjectId()).collect(Collectors.toList()));
        } else {
            ownerIds.add(ownerId);
        }
        return ownerIds;
    }
}
