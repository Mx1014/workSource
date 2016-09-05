// @formatter:off
package com.everhomes.approval;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.ApprovalFlowDTO;
import com.everhomes.rest.approval.ApprovalFlowLevelDTO;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalPerson;
import com.everhomes.rest.approval.ApprovalRuleDTO;
import com.everhomes.rest.approval.ApprovalTargetType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowNameCommand;
import com.everhomes.rest.approval.CreateApprovalFlowNameResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.RuleFlowMap;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowNameCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowNameResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ApprovalServiceImpl implements ApprovalService {
	
	@Autowired
	private NamespaceProvider namespaceProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalFlowProvider approvalFlowProvider;
	
	@Autowired
	private ApprovalFlowLevelProvider approvalFlowLevelProvider;
	
	@Autowired
	private ApprovalRuleFlowMapProvider approvalRuleFlowMapProvider;
	
	@Autowired
	private ApprovalRuleProvider approvalRuleProvider;
	
	@Autowired
	private LocaleTemplateProvider localeTemplateProvider;
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public CreateApprovalCategoryResponse createApprovalCategory(CreateApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getCategoryName()) || cmd.getApprovalType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		
		ApprovalCategory category = ConvertHelper.convert(cmd, ApprovalCategory.class);
		category.setApprovalType(cmd.getApprovalType());
		category.setStatus(CommonStatus.ACTIVE.getCode());
		category.setCreatorUid(userId);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		approvalCategoryProvider.createApprovalCategory(category);
		return new CreateApprovalCategoryResponse(ConvertHelper.convert(category, ApprovalCategoryDTO.class));
	}

	private void checkApprovalType(Byte approvalType) {
		if (ApprovalType.fromCode(approvalType) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approvalType");
		}
	}

	@Override
	public UpdateApprovalCategoryResponse updateApprovalCategory(UpdateApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null || StringUtils.isBlank(cmd.getCategoryName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id and categoryName cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		
		ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
		category.setCategoryName(cmd.getCategoryName());
		approvalCategoryProvider.updateApprovalCategory(category);
		
		return new UpdateApprovalCategoryResponse(ConvertHelper.convert(category, ApprovalCategoryDTO.class));
	}
	
	private ApprovalCategory checkCategoryExist(Long id, Integer namespaceId, String ownerType, Long ownerId, Byte approvalType){
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(id);
		if (category == null || category.getNamespaceId().intValue() != namespaceId.intValue() || !ownerType.equals(category.getOwnerType())
				|| category.getOwnerId().longValue() != ownerId.longValue() || category.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()
				|| category.getApprovalType().byteValue() != approvalType.byteValue()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist category: categoryId="+id+", namespaceId="+namespaceId+", ownerType="+ownerType+", ownerId="+ownerId);
		}
		return category;
	}
	
	@Override
	public ListApprovalCategoryResponse listApprovalCategory(ListApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		
		List<ApprovalCategory> categoryList = approvalCategoryProvider.listApprovalCategory(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
		
		return new ListApprovalCategoryResponse(categoryList.stream().map(c->ConvertHelper.convert(c, ApprovalCategoryDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public void deleteApprovalCategory(DeleteApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
		category.setStatus(CommonStatus.INACTIVE.getCode());
		approvalCategoryProvider.updateApprovalCategory(category);
	}

	@Override
	public CreateApprovalFlowNameResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"name cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		ApprovalFlow approvalFlow = ConvertHelper.convert(cmd, ApprovalFlow.class);
		approvalFlow.setStatus(CommonStatus.ACTIVE.getCode());
		approvalFlow.setCreatorUid(userId);
		approvalFlow.setOperatorUid(userId);
		approvalFlow.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalFlow.setUpdateTime(approvalFlow.getCreateTime());
		approvalFlowProvider.createApprovalFlow(approvalFlow);
		
		return new CreateApprovalFlowNameResponse(ConvertHelper.convert(approvalFlow, ApprovalFlowDTO.class));
	}

	@Override
	public UpdateApprovalFlowNameResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null || StringUtils.isBlank(cmd.getName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id and name cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		approvalFlow.setName(cmd.getName());
		approvalFlow.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalFlow.setOperatorUid(userId);
		approvalFlowProvider.updateApprovalFlow(approvalFlow);
		
		return new UpdateApprovalFlowNameResponse(ConvertHelper.convert(approvalFlow, ApprovalFlowDTO.class));
	}

	private ApprovalFlow checkApprovalFlowExist(Long id, Integer namespaceId, String ownerType, Long ownerId) {
		ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowById(id);
		if (approvalFlow == null || approvalFlow.getNamespaceId().intValue() != namespaceId.intValue() || !ownerType.equals(approvalFlow.getOwnerType())
				|| approvalFlow.getOwnerId().longValue() != ownerId.longValue() || approvalFlow.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist approvalFlow: approvalFlowId="+id+", namespaceId="+namespaceId+", ownerType="+ownerType+", ownerId="+ownerId);
		}
		return approvalFlow;
	}

	@Override
	public CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd) {
		Long userId = getUserId();
		if (cmd.getFlowId() == null || cmd.getLevel() == null || ListUtils.isEmpty(cmd.getApprovalPersonList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (checkCurrentLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"current level exists, so you cannot create it again: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
		}
		if (!checkPreviousLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"previous level does not exist, so you cannot create current level: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
		}
		
		dbProvider.execute(s->{
			createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalPersonList());
			return true;
		});
		
		processTargetName(cmd.getApprovalPersonList());
		
		return new CreateApprovalFlowLevelResponse(cmd.getFlowId(), new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalPersonList()));
	}

	private void createApprovalFlowLevel(Long flowId, Byte level, List<ApprovalPerson> approvalPersonList) {
		approvalPersonList.forEach(a->{
			if (ApprovalTargetType.fromCode(a.getTargetType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"error target type: targetType="+a.getTargetType()+", targetId="+a.getTargetId());
			}
			ApprovalFlowLevel ap = new ApprovalFlowLevel();
			ap.setFlowId(flowId);
			ap.setLevel(level);
			ap.setTargetType(a.getTargetType());
			ap.setTargetId(a.getTargetId());
			approvalFlowLevelProvider.createApprovalFlowLevel(ap);
		});
	}

	private void processTargetName(List<ApprovalPerson> approvalPersonList) {
		if (ListUtils.isEmpty(approvalPersonList)) {
			return;
		}
		approvalPersonList.forEach(a->{
			a.setTargetName(getTargetName(a.getTargetType(), a.getTargetId()));
		});
	}
	
	private String getTargetName(Byte targetType, Long targetId){
		ApprovalTargetType approvalTargetType = ApprovalTargetType.fromCode(targetType);
		switch (approvalTargetType) {
		case USER:
			User user = userProvider.findUserById(targetId);
			if (user != null) {
				return user.getNickName();
			}
			break;
		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error target type: targetType="+targetType+", targetId="+targetId);
		}
		return "";
	}

	private boolean checkCurrentLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, level);
		if (ListUtils.isNotEmpty(approvalFlowLevelList)) {
			return true;
		}
		return false;
	}

	private boolean checkPreviousLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, (byte) (level-1));
		if (ListUtils.isNotEmpty(approvalFlowLevelList)) {
			return true;
		}
		return false;
	}

	private boolean checkNextLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, (byte) (level+1));
		if (ListUtils.isNotEmpty(approvalFlowLevelList)) {
			return true;
		}
		return false;
	}

	@Override
	public UpdateApprovalFlowLevelResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd) {
		Long userId = getUserId();
		if (cmd.getFlowId() == null || cmd.getLevel() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (!checkCurrentLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"current level does not exist, so you cannot update it: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
		}
		
		dbProvider.execute(s->{
			approvalFlowLevelProvider.deleteApprovalLevels(cmd.getFlowId(), cmd.getLevel());
			if (ListUtils.isNotEmpty(cmd.getApprovalPersonList())) {
				createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalPersonList());
			}else {
				if (checkNextLevelExist(cmd.getFlowId(), cmd.getLevel())) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"next level exists, so you cannot update this level to empty: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
				}
			}
			return true;
		});

		processTargetName(cmd.getApprovalPersonList());
		
		return new UpdateApprovalFlowLevelResponse(cmd.getFlowId(), new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalPersonList()));
	}

	@Override
	public ListApprovalFlowResponse listApprovalFlow(ListApprovalFlowCommand cmd) {
		Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<ApprovalFlow> approvalFlowList = approvalFlowProvider.listApprovalFlow(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);
		
		Long nextPageAnchor = null;
		if (approvalFlowList.size() > pageSize) {
			approvalFlowList.remove(approvalFlowList.size()-1);
			nextPageAnchor = approvalFlowList.get(approvalFlowList.size()-1).getId();
		}
		
		//处理每个审批流程中每级设置的人
		List<ApprovalFlowDTO> list = processApprovalFlowLevelList(approvalFlowList);
		
		return new ListApprovalFlowResponse(nextPageAnchor, list);
	}

	private List<ApprovalFlowDTO> processApprovalFlowLevelList(final List<ApprovalFlow> approvalFlowList) {
		final List<ApprovalFlowDTO> resultList = new ArrayList<>();
		final List<Long> flowIdList = new ArrayList<>();
		final Map<Long, ApprovalFlowDTO> flowMap = new HashMap<>();
		approvalFlowList.forEach(a->{
			ApprovalFlowDTO ad = ConvertHelper.convert(a, ApprovalFlowDTO.class);
			flowIdList.add(a.getId());
			//注意，resultList与flowMap中存储的是同样的对象
			resultList.add(ad);
			flowMap.put(a.getId(), ad);
		});
		
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByFlowIds(flowIdList);
		//把平行的记录分组，先按flowId分组，再按level分组，最后的小分组中存储的就是每个flowId每个level中的人员
		Map<Long, Map<Byte,List<ApprovalFlowLevel>>> map = approvalFlowLevelList.stream().collect(Collectors.groupingBy(ApprovalFlowLevel::getFlowId, Collectors.groupingBy(ApprovalFlowLevel::getLevel)));
		map.forEach((k1, v1)->{
			//k1为flowId
			final List<ApprovalFlowLevelDTO> levelList = new ArrayList<>();
			v1.forEach((k2,v2)->{
				//k2为level
				List<ApprovalPerson> approvalPersonList = v2.stream().map(a->{
																ApprovalPerson approvalPerson = new ApprovalPerson();
																approvalPerson.setTargetType(a.getTargetType());
																approvalPerson.setTargetId(a.getId());
																approvalPerson.setTargetName(getTargetName(a.getTargetType(), a.getTargetId()));
																return approvalPerson;
															}).collect(Collectors.toList());
				levelList.add(new ApprovalFlowLevelDTO(k2, approvalPersonList));
			});
			flowMap.get(k1).setLevelList(levelList);
		});
		
		//因为resultList与flowMap中存储的是同样的对象，所以flowMap处理完了，resultList也就处理完了
		return resultList;
	}

	@Override
	public void deleteApprovalFlow(DeleteApprovalFlowCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleFlowMapExist(cmd.getId());
		approvalFlow.setStatus(CommonStatus.INACTIVE.getCode());
		approvalFlow.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalFlowProvider.updateApprovalFlow(approvalFlow);
	}

	private void checkApprovalRuleFlowMapExist(Long flowId) {
		ApprovalRuleFlowMap approvalRuleFlowMap= approvalRuleFlowMapProvider.findOneApprovalRuleFlowMapByFlowId(flowId);
		if (approvalRuleFlowMap != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"This flow has related to some rules, so you cannot delete it: flowId="+flowId);
		}
	}

	@Override
	public CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName()) || ListUtils.isEmpty(cmd.getRuleFlowMapList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleNameRepetition(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
		final ApprovalRule approvalRule = ConvertHelper.convert(cmd, ApprovalRule.class);
		dbProvider.execute(s->{
			createApprovalRule(userId, approvalRule);
			createApprovalRuleFlowMap(approvalRule.getId(), cmd.getRuleFlowMapList());
			return true;
		});
		
		processApprovalTypeName(cmd.getRuleFlowMapList());
		
		ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(approvalRule, ApprovalRuleDTO.class);
		approvalRuleDTO.setRuleFlowMapList(cmd.getRuleFlowMapList());
		
		return new CreateApprovalRuleResponse(approvalRuleDTO);
	}

	private void checkApprovalRuleNameRepetition(Long userId, Integer namespaceId, String ownerType, Long ownerId,
			String ruleName) {
		ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleByName(namespaceId, ownerType, ownerId, ruleName);
		if (approvalRule != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"repeated rule name, ruleName="+ruleName);
		}
	}

	private void processApprovalTypeName(List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r->{
			LocaleTemplate localeTemplate = localeTemplateProvider.findLocaleTemplateByScope(0, ApprovalTypeTemplateCode.SCOPE, r.getApprovalType(), UserContext.current().getUser().getLocale());
			if (localeTemplate != null) {
				r.setApprovalTypeName(localeTemplate.getText());
			}
		});
	}

	private void createApprovalRuleFlowMap(final Long ruleId, List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r->{
			if (ApprovalType.fromCode(r.getApprovalType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist approval type: approvalType="+r.getApprovalType()+", flowId="+r.getFlowId());
			}
			ApprovalRuleFlowMap approvalRuleFlowMap = new ApprovalRuleFlowMap();
			approvalRuleFlowMap.setRuleId(ruleId);
			approvalRuleFlowMap.setApprovalType(r.getApprovalType());
			approvalRuleFlowMap.setFlowId(r.getFlowId());
			approvalRuleFlowMap.setStatus(CommonStatus.ACTIVE.getCode());
			approvalRuleFlowMapProvider.createApprovalRuleFlowMap(approvalRuleFlowMap);
		});
	}

	private void createApprovalRule(Long userId, ApprovalRule approvalRule) {
		approvalRule.setStatus(CommonStatus.ACTIVE.getCode());
		approvalRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalRule.setUpdateTime(approvalRule.getCreateTime());
		approvalRule.setCreatorUid(userId);
		approvalRule.setOperatorUid(userId);
		approvalRuleProvider.createApprovalRule(approvalRule);
	}
	

	@Override
	public UpdateApprovalRuleResponse updateApprovalRule(UpdateApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName()) || ListUtils.isEmpty(cmd.getRuleFlowMapList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		dbProvider.execute(s->{
			approvalRule.setName(cmd.getName());
			updateApprovalRule(approvalRule, userId);
			deleteApprovalRuleFlowMap(cmd.getId());
			createApprovalRuleFlowMap(cmd.getId(), cmd.getRuleFlowMapList());
			return true;
		});

		processApprovalTypeName(cmd.getRuleFlowMapList());
		
		ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(approvalRule, ApprovalRuleDTO.class);
		approvalRuleDTO.setRuleFlowMapList(cmd.getRuleFlowMapList());
		
		return new UpdateApprovalRuleResponse(approvalRuleDTO);
	}


	private void deleteApprovalRuleFlowMap(Long ruleId) {
		approvalRuleFlowMapProvider.deleteRuleFlowMapByRuleId(ruleId);
	}

	private void updateApprovalRule(ApprovalRule approvalRule, Long userId) {
		approvalRule.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalRule.setOperatorUid(userId);
		approvalRuleProvider.updateApprovalRule(approvalRule);
	}

	private ApprovalRule checkApprovalRuleExist(Long id, Integer namespaceId, String ownerType, Long ownerId) {
		ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleById(id);
		if (approvalRule == null || approvalRule.getNamespaceId().intValue() != namespaceId.intValue()
				|| !ownerType.equals(approvalRule.getOwnerType()) || approvalRule.getOwnerId().longValue() != ownerId.longValue()
				|| approvalRule.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval rule: ruleId="+id+", namespaceId="+namespaceId+", ownerType="+ownerType+", ownerId="+ownerId);
		}
		return approvalRule;
	}
	

	@Override
	public void deleteApprovalRule(DeleteApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		dbProvider.execute(s->{
			approvalRule.setStatus(CommonStatus.INACTIVE.getCode());
			updateApprovalRule(approvalRule, userId);
			updateApprovalRuleFlowMap(cmd.getId());
			return true;
		});
	}

	private void updateApprovalRuleFlowMap(Long ruleId) {
		approvalRuleFlowMapProvider.updateApprovalRuleFlowMapByRuleId(ruleId);
	}
	

	@Override
	public ListApprovalRuleResponse listApprovalRule(ListApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<ApprovalRule> approvalRuleList = approvalRuleProvider.listApprovalRule(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);
		
		Long nextPageAnchor = null;
		if (approvalRuleList.size() > pageSize) {
			approvalRuleList.remove(approvalRuleList.size()-1);
			nextPageAnchor = approvalRuleList.get(approvalRuleList.size()-1).getId();
		}
		
		//处理审批类型
		List<ApprovalRuleDTO> list = processApprovalRuleFlowMapList(approvalRuleList);
		
		return new ListApprovalRuleResponse(nextPageAnchor, list);
	}
	
	private List<ApprovalRuleDTO> processApprovalRuleFlowMapList(List<ApprovalRule> approvalRuleList) {
		List<ApprovalRuleDTO> resultList = new ArrayList<>();
		List<Long> ruleIdList = new ArrayList<>();
		Map<Long, ApprovalRuleDTO> ruleMap = new HashMap<>();
		approvalRuleList.forEach(a->{
			ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(a, ApprovalRuleDTO.class);
			ruleIdList.add(a.getId());
			resultList.add(approvalRuleDTO);
			ruleMap.put(a.getId(), approvalRuleDTO);
		});
		
		List<ApprovalRuleFlowMap> ruleList = approvalRuleFlowMapProvider.listApprovalRuleFlowMapByRuleIds(ruleIdList);
		Map<Long, List<ApprovalRuleFlowMap>> map = ruleList.stream().collect(Collectors.groupingBy(ApprovalRuleFlowMap::getRuleId));
		map.forEach((key,value)->{
			List<RuleFlowMap> ruleFlowMapList = value.stream().map(v->ConvertHelper.convert(v, RuleFlowMap.class)).collect(Collectors.toList());
			processApprovalTypeName(ruleFlowMapList);
			ruleMap.get(key).setRuleFlowMapList(ruleFlowMapList);
		});
		
		return resultList;
	}

	private Long getUserId(){
		return UserContext.current().getUser().getId();
	}
	
	private void checkPrivilege(Long userId, Integer namespaceId, String ownerType, Long ownerId){
		if (namespaceId == null || StringUtils.isBlank(ownerType) || ownerId == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, namespaceId="+namespaceId+", ownerType="+ownerType+", ownerId="+ownerId);
		}
		
		checkNamespaceExist(namespaceId);
		checkOwnerExist(namespaceId, ownerType, ownerId);
	}

	private void checkNamespaceExist(Integer namespaceId) {
		if (namespaceProvider.findNamespaceById(namespaceId) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist namespace: "+namespaceId);
		}
	}

	private void checkOwnerExist(Integer namespaceId, String ownerType, Long ownerId) {
		ApprovalOwnerType approvalOwnerType = null;

		if ((approvalOwnerType = ApprovalOwnerType.fromCode(ownerType)) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist ownerType: "+ownerType);
		}
		
		switch (approvalOwnerType) {
		case ORGANIZATION:
			Organization organization = organizationProvider.findOrganizationById(ownerId);
			if (organization == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Not exist organization: "+ownerId);
			}
			if (organization.getNamespaceId().intValue() != namespaceId.intValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"the organization is beyond the namespace: namespaceId="+namespaceId+", organizationId"+ownerId);
			}
			break;
		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error ownerType: "+ownerType);
		}
		
	}
}
