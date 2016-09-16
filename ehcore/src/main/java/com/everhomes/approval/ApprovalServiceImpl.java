// @formatter:off
package com.everhomes.approval;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.rest.approval.ApprovalFlowDTO;
import com.everhomes.rest.approval.ApprovalFlowLevelDTO;
import com.everhomes.rest.approval.ApprovalFlowOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogAndFlowOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalRequestCondition;
import com.everhomes.rest.approval.ApprovalRuleDTO;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalTargetType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.ApprovalUser;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.approval.BriefApprovalFlowDTO;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.BriefApprovalRuleDTO;
import com.everhomes.rest.approval.CancelApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.CreateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneResponse;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneCommand;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalRequestCommand;
import com.everhomes.rest.approval.ListApprovalRequestResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalUserCommand;
import com.everhomes.rest.approval.ListApprovalUserResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.RejectApprovalRequestCommand;
import com.everhomes.rest.approval.RuleFlowMap;
import com.everhomes.rest.approval.TimeRange;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;

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
	private ApprovalRequestProvider approvalRequestProvider;
	
	@Autowired
	private ApprovalOpRequestProvider approvalOpRequestProvider;
	
	@Autowired
	private LocaleStringProvider localeStringProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private AttachmentProvider attachmentProvider;

	@Autowired
	private ApprovalTimeRangeProvider approvalTimeRangeProvider;
	
	@Autowired
	private FamilyProvider familyProvider;
	
	@Autowired
	private PunchService punchService;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Override
	public CreateApprovalCategoryResponse createApprovalCategory(CreateApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getCategoryName()) || cmd.getApprovalType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		checkApprovalCategoryNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType(), cmd.getCategoryName());
		
		ApprovalCategory category = ConvertHelper.convert(cmd, ApprovalCategory.class);
		category.setApprovalType(cmd.getApprovalType());
		category.setStatus(CommonStatus.ACTIVE.getCode());
		category.setCreatorUid(userId);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		approvalCategoryProvider.createApprovalCategory(category);
		return new CreateApprovalCategoryResponse(ConvertHelper.convert(category, ApprovalCategoryDTO.class));
	}

	private void checkApprovalCategoryNameDuplication(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType,
			String categoryName) {
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryByName(namespaceId, ownerType, ownerId, approvalType, categoryName);
		if (category != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"exist category name: categoryName="+categoryName);
		}
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
		
		Tuple<ApprovalCategory, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_CATEGORY.getCode()+cmd.getId()).enter(()->{
			ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
			category.setCategoryName(cmd.getCategoryName());
			approvalCategoryProvider.updateApprovalCategory(category);
			return category;
		});
		
		return new UpdateApprovalCategoryResponse(ConvertHelper.convert(tuple.first(), ApprovalCategoryDTO.class));
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
	
	private ApprovalCategory checkCategoryExist(Long id, ApprovalOwnerInfo ownerInfo, Byte approvalType) {
		return checkCategoryExist(id, ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), approvalType);
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
		

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_CATEGORY.getCode()+cmd.getId()).enter(()->{
			ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
			category.setStatus(CommonStatus.INACTIVE.getCode());
			approvalCategoryProvider.updateApprovalCategory(category);
			return null;
		});
	}

	@Override
	public CreateApprovalFlowInfoResponse createApprovalFlowInfo(CreateApprovalFlowInfoCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"name cannot be empty");
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"length of name cannot be greater than 8 words, name="+cmd.getName());
		}
		
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
		
		ApprovalFlow approvalFlow = ConvertHelper.convert(cmd, ApprovalFlow.class);
		approvalFlow.setStatus(CommonStatus.ACTIVE.getCode());
		approvalFlow.setCreatorUid(userId);
		approvalFlow.setOperatorUid(userId);
		approvalFlow.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalFlow.setUpdateTime(approvalFlow.getCreateTime());
		approvalFlowProvider.createApprovalFlow(approvalFlow);
		
		return new CreateApprovalFlowInfoResponse(ConvertHelper.convert(approvalFlow, BriefApprovalFlowDTO.class));
	}

	private void checkApprovalFlowNameDuplication(Integer namespaceId, String ownerType, Long ownerId, String name) {
		ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowByName(namespaceId, ownerType, ownerId, name);
		if (approvalFlow != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"exist name, name="+name);
		}
	}

	@Override
	public UpdateApprovalFlowInfoResponse updateApprovalFlowInfo(UpdateApprovalFlowInfoCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null || StringUtils.isBlank(cmd.getName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id and name cannot be empty");
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"length of name cannot be greater than 8 words, name="+cmd.getName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
		
		Tuple<ApprovalFlow, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode()+cmd.getId()).enter(()->{
			ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
			approvalFlow.setName(cmd.getName());
			approvalFlow.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			approvalFlow.setOperatorUid(userId);
			approvalFlowProvider.updateApprovalFlow(approvalFlow);
			return approvalFlow;
		});
		
		return new UpdateApprovalFlowInfoResponse(ConvertHelper.convert(tuple.first(), BriefApprovalFlowDTO.class));
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
		if (cmd.getFlowId() == null || cmd.getLevel() == null || ListUtils.isEmpty(cmd.getApprovalUserList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (checkCurrentLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"current level exists, so you cannot create it again: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
		}
		if (cmd.getLevel().byteValue() != (byte)1 && !checkPreviousLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"previous level does not exist, so you cannot create current level: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
		}
		
		dbProvider.execute(s->{
			createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalUserList());
			return true;
		});
		
		processTargetName(cmd.getApprovalUserList());
		
		return new CreateApprovalFlowLevelResponse(cmd.getFlowId(), new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalUserList()));
	}

	private void createApprovalFlowLevel(Long flowId, Byte level, List<ApprovalUser> approvalPersonList) {
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

	private void processTargetName(List<ApprovalUser> approvalPersonList) {
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
			if (ListUtils.isNotEmpty(cmd.getApprovalUserList())) {
				createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalUserList());
			}else {
				if (checkNextLevelExist(cmd.getFlowId(), cmd.getLevel())) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"next level exists, so you cannot update this level to empty: flowId"+cmd.getFlowId()+", level="+cmd.getLevel());
				}
			}
			return true;
		});

		processTargetName(cmd.getApprovalUserList());
		
		return new UpdateApprovalFlowLevelResponse(cmd.getFlowId(), new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalUserList()));
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
				List<ApprovalUser> approvalPersonList = v2.stream().map(a->{
					ApprovalUser approvalPerson = new ApprovalUser();
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

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode()+cmd.getId()).enter(()->{
			ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
			checkApprovalRuleFlowMapExist(cmd.getId());
			approvalFlow.setStatus(CommonStatus.INACTIVE.getCode());
			approvalFlow.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			approvalFlowProvider.updateApprovalFlow(approvalFlow);
			return null;
		});
	}

	private void checkApprovalRuleFlowMapExist(Long flowId) {
		ApprovalRuleFlowMap approvalRuleFlowMap= approvalRuleFlowMapProvider.findOneApprovalRuleFlowMapByFlowId(flowId);
		if (approvalRuleFlowMap != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"This flow has related to some rules, so you cannot delete it: flowId="+flowId);
		}
	}
	
	@Override
	public ListBriefApprovalFlowResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd) {
		Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		List<ApprovalFlow> approvalFlowList = approvalFlowProvider.listApprovalFlow(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		return new ListBriefApprovalFlowResponse(approvalFlowList.stream().map(a->ConvertHelper.convert(a, BriefApprovalFlowDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName()) || ListUtils.isEmpty(cmd.getRuleFlowMapList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd="+cmd);
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"length of name cannot be greater than 8: name="+cmd.getName());
		}
		
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleNameDuplication(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
		checkRuleFlowMap(cmd.getRuleFlowMapList());
		
		final ApprovalRule approvalRule = ConvertHelper.convert(cmd, ApprovalRule.class);
		dbProvider.execute(s->{
			createApprovalRule(userId, approvalRule);
			createApprovalRuleFlowMap(approvalRule.getId(), cmd.getRuleFlowMapList());
			return true;
		});
		
		processApprovalTypeName(cmd.getRuleFlowMapList());
		processFlowName(cmd.getRuleFlowMapList());
		
		ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(approvalRule, ApprovalRuleDTO.class);
		approvalRuleDTO.setRuleFlowMapList(cmd.getRuleFlowMapList());
		
		return new CreateApprovalRuleResponse(approvalRuleDTO);
	}
	
	private void checkRuleFlowMap(List<RuleFlowMap> list){
		//1. 每一个对象中的approvalType和flowId必须不为空且存在
		list.forEach(r->{
			if (r.getApprovalType() == null || r.getFlowId() == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"approvalType and flowId cannot be empty: approvalType="+r.getApprovalType()+", flowId="+r.getFlowId());
			}
			if (ApprovalType.fromCode(r.getApprovalType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist approvalType: approvalType="+r.getApprovalType());
			}
			if (approvalFlowProvider.findApprovalFlowById(r.getFlowId()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist flow: flowId="+r.getFlowId());
			}
		});
		
		//2. list中不能存在相同的审批类型
		Set<Byte> set = new HashSet<>();
		list.forEach(r->set.add(r.getApprovalType()));
		if (list.size() > set.size()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cannot contain same approval type");
		}
		
		//3. list中必须存在产品规定必填的审批规则
		checkMustExistApprovalType(list, ApprovalType.ABSENCE.getCode());
		checkMustExistApprovalType(list, ApprovalType.EXCEPTION.getCode());
	}
	
	private void checkMustExistApprovalType(List<RuleFlowMap> list, Byte approvalType){
		for (RuleFlowMap ruleFlowMap : list) {
			if (ruleFlowMap.getApprovalType().byteValue() == approvalType.byteValue()) {
				return;
			}
		}
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"parameters must contain specific approval type, approvalType="+approvalType);
	}

	private void checkApprovalRuleNameDuplication(Long userId, Integer namespaceId, String ownerType, Long ownerId,
			String ruleName) {
		ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleByName(namespaceId, ownerType, ownerId, ruleName);
		if (approvalRule != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"repeated rule name, ruleName="+ruleName);
		}
	}

	private void processApprovalTypeName(List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r->{
			LocaleString localeString = localeStringProvider.find(ApprovalTypeTemplateCode.SCOPE, r.getApprovalType().toString(), UserContext.current().getUser().getLocale());
			if (localeString != null) {
				r.setApprovalTypeName(localeString.getText());
			}
		});
	}

	private void processFlowName(List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r->{
			ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowById(r.getFlowId());
			if (approvalFlow != null) {
				r.setFlowName(approvalFlow.getName());
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
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"length of name cannot be greater than 8: name="+cmd.getName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleNameDuplication(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
		checkRuleFlowMap(cmd.getRuleFlowMapList());

		Tuple<ApprovalRule, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode()+cmd.getId()).enter(()->{
			ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
			dbProvider.execute(s->{
				approvalRule.setName(cmd.getName());
				updateApprovalRule(approvalRule, userId);
				deleteApprovalRuleFlowMap(cmd.getId());
				createApprovalRuleFlowMap(cmd.getId(), cmd.getRuleFlowMapList());
				return true;
			});
			return approvalRule;
		});

		processApprovalTypeName(cmd.getRuleFlowMapList());
		processFlowName(cmd.getRuleFlowMapList());
		
		ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(tuple.first(), ApprovalRuleDTO.class);
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

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode()+cmd.getId()).enter(()->{
			ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
			dbProvider.execute(s->{
				approvalRule.setStatus(CommonStatus.INACTIVE.getCode());
				updateApprovalRule(approvalRule, userId);
				updateApprovalRuleFlowMap(cmd.getId());
				return true;
			});
			return null;
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
			processFlowName(ruleFlowMapList);
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
	
	private void checkPrivilege(Long userId, ApprovalOwnerInfo ownerInfo) {
		checkPrivilege(userId, ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId());
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
	
	@Override
	public ListBriefApprovalRuleResponse listBriefApprovalRule(ListBriefApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		List<ApprovalRule> approvalRuleList = approvalRuleProvider.listApprovalRule(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		return new ListBriefApprovalRuleResponse(approvalRuleList.stream().map(a->ConvertHelper.convert(a, BriefApprovalRuleDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public GetApprovalBasicInfoOfRequestResponse getApprovalBasicInfoOfRequest(
			GetApprovalBasicInfoOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getRequestId());
		
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		
		ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequestDTO = handler.processApprovalBasicInfoOfRequest(approvalRequest);
		
		return new GetApprovalBasicInfoOfRequestResponse(approvalBasicInfoOfRequestDTO);
	}

	private ApprovalRequest checkApprovalRequestExist(Integer namespaceId, String ownerType, Long ownerId, Long requestId) {
		ApprovalRequest approvalRequest = approvalRequestProvider.findApprovalRequestById(requestId);
		if (approvalRequest == null || approvalRequest.getNamespaceId().intValue() != namespaceId.intValue()
				|| approvalRequest.getOwnerId().longValue() != ownerId.longValue()
				|| !ownerType.equals(approvalRequest.getOwnerType())
				|| approvalRequest.getStatus().byteValue() != CommonStatus.INACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval request, requestId="+requestId);
		}
		return approvalRequest;
	}
	
	private ApprovalRequest checkApprovalRequestExist(ApprovalOwnerInfo ownerInfo, String requestToken) {
		return checkApprovalRequestExist(ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), getRequestIdFromToken(requestToken));
	}

	private Long getRequestIdFromToken(String requestToken){
		if (StringUtils.isBlank(requestToken)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"requestToken cannot be empty");
		}
		return WebTokenGenerator.getInstance().fromWebToken(requestToken, Long.class);
	}
	
	@Override
	public ListApprovalLogAndFlowOfRequestResponse listApprovalLogAndFlowOfRequest(
			ListApprovalLogAndFlowOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getRequestId());
		
		return new ListApprovalLogAndFlowOfRequestResponse(listApprovalLogAndFlow(approvalRequest));
	}

	private List<ApprovalLogAndFlowOfRequestDTO> listApprovalLogAndFlow(ApprovalRequest approvalRequest) {
		List<ApprovalLogOfRequestDTO> logList = listApprovalLog(approvalRequest);
		
		//合并这两个列表为一个
		List<ApprovalLogAndFlowOfRequestDTO> resultList = new ArrayList<>();
		
		//日志全部加入
		resultList.addAll(logList.stream().map(l->{
			ApprovalLogAndFlowOfRequestDTO approvalLogAndFlowOfRequestDTO = new ApprovalLogAndFlowOfRequestDTO();
			approvalLogAndFlowOfRequestDTO.setType((byte) 1);
			approvalLogAndFlowOfRequestDTO.setContentJson(JSON.toJSONString(l));
			return approvalLogAndFlowOfRequestDTO;
		}).collect(Collectors.toList()));
		
		//如果没有后续流程了，则不用再取流程
		if (approvalRequest.getNextLevel() == null) {
			List<ApprovalFlowOfRequestDTO> flowList = listApprovalFlowUser(approvalRequest.getFlowId(), approvalRequest.getCurrentLevel());
			//流程只取当前进行到的level后面的
			boolean flag = false;
			if (approvalRequest.getCurrentLevel().byteValue() == (byte)0) {
				flag = true;
			}
			for (ApprovalFlowOfRequestDTO approvalFlowOfRequestDTO : flowList) {
				if (flag) {
					ApprovalLogAndFlowOfRequestDTO approvalLogAndFlowOfRequestDTO = new ApprovalLogAndFlowOfRequestDTO();
					approvalLogAndFlowOfRequestDTO.setType((byte) 2);
					approvalLogAndFlowOfRequestDTO.setContentJson(JSON.toJSONString(approvalFlowOfRequestDTO));
					resultList.add(approvalLogAndFlowOfRequestDTO);
				}
				if (approvalFlowOfRequestDTO.getCurrentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
					flag = true;
				}
			}
		}
		return resultList;
	}

	@Override
	public ListApprovalLogOfRequestResponse listApprovalLogOfRequest(ListApprovalLogOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getRequestId());
		
		return new ListApprovalLogOfRequestResponse(listApprovalLog(approvalRequest));
	}

	private List<ApprovalLogOfRequestDTO> listApprovalLog(ApprovalRequest approvalRequest) {
		List<ApprovalLogOfRequestDTO> resultList = new ArrayList<>();
		
		List<ApprovalOpRequest> approvalOpRequestList = approvalOpRequestProvider.listApprovalOpRequestByRequestId(approvalRequest.getId());
		for (int i = 0; i < approvalOpRequestList.size(); i++) {
			ApprovalOpRequest approvalOpRequest = approvalOpRequestList.get(i);
			ApprovalLogOfRequestDTO approvalLog = new ApprovalLogOfRequestDTO();
			approvalLog.setCreateTime(approvalOpRequest.getCreateTime());
			User user = userProvider.findUserById(approvalOpRequest.getOperatorUid());
			if (user != null) {
				approvalLog.setNickName(user.getNickName());
			}
			
			approvalLog.setRemark(approvalOpRequest.getProcessMessage());
			approvalLog.setApprovalStatus(approvalOpRequest.getApprovalStatus());
			
			if (i == 0) {
				approvalLog.setApprovalType(approvalRequest.getApprovalType());
				if (approvalRequest.getCategoryId() != null) {
					ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
					if (approvalCategory != null) {
						approvalLog.setCategoryName(approvalCategory.getCategoryName());
					}
				}
				if (approvalRequest.getAttachmentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
					approvalLog.setAttachmentList(getAttachments(approvalRequest.getId()));
				}
			}
		}
		
		return resultList;
	}

	private List<AttachmentDescriptor> getAttachments(Long requestId) {
		List<Attachment> attachmentList = attachmentProvider.listAttachmentByOwnerId(EhApprovalAttachments.class, requestId);
		return attachmentList.stream().map(a->{
			AttachmentDescriptor attachmentDescriptor = ConvertHelper.convert(a, AttachmentDescriptor.class);
			attachmentDescriptor.setContentUrl(getUrl(a.getContentUri(), a.getOwnerId()));
			return attachmentDescriptor;
		}).collect(Collectors.toList());
	}

	private String getUrl(String uri, Long ownerId) {
		if (uri != null && uri.length() > 0) {
			try {
				return contentServerService.parserUri(uri, "", ownerId);
			} catch (Exception e) {
				
			}
		}
		return "";
	}	
	
	@Override
	public ListApprovalFlowOfRequestResponse listApprovalFlowOfRequest(ListApprovalFlowOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getRequestId());
		
		return new ListApprovalFlowOfRequestResponse(listApprovalFlowUser(approvalRequest.getFlowId(), approvalRequest.getCurrentLevel()));
	}

	private List<ApprovalFlowOfRequestDTO> listApprovalFlowUser(Long flowId, Byte currentLevel) {
		List<ApprovalFlowOfRequestDTO> resultList = new ArrayList<>();
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByFlowId(flowId);
		
		Map<Byte, List<ApprovalFlowLevel>> map = approvalFlowLevelList.stream().collect(Collectors.groupingBy(ApprovalFlowLevel::getLevel));
		//key为level
		map.forEach((key, value)->{
			ApprovalFlowOfRequestDTO approvalFlowOfRequestDTO = new ApprovalFlowOfRequestDTO();
			approvalFlowOfRequestDTO.setCurrentFlag(currentLevel.byteValue()==key.byteValue()?TrueOrFalseFlag.TRUE.getCode():TrueOrFalseFlag.FALSE.getCode());
			approvalFlowOfRequestDTO.setNickNameList(getNickNameList(value));
			resultList.add(approvalFlowOfRequestDTO);
		});
		
		return resultList;
	}	

	private List<String> getNickNameList(List<ApprovalFlowLevel> approvalFlowLevelList) {
		return approvalFlowLevelList.stream().map(a->getTargetName(a.getTargetType(), a.getTargetId())).collect(Collectors.toList());
	}

	@Override
	public GetApprovalBasicInfoOfRequestBySceneResponse getApprovalBasicInfoOfRequestByScene(
			GetApprovalBasicInfoOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());
		
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		
		ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequestDTO = handler.processApprovalBasicInfoOfRequest(approvalRequest);
		
		return new GetApprovalBasicInfoOfRequestBySceneResponse(approvalBasicInfoOfRequestDTO);
	}	

	@Override
	public ListApprovalLogAndFlowOfRequestBySceneResponse listApprovalLogAndFlowOfRequestByScene(
			ListApprovalLogAndFlowOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());
		
		return new ListApprovalLogAndFlowOfRequestBySceneResponse(listApprovalLogAndFlow(approvalRequest));
	}

	@Override
	public ListApprovalLogOfRequestBySceneResponse listApprovalLogOfRequestByScene(
			ListApprovalLogOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());
		
		return new ListApprovalLogOfRequestBySceneResponse(listApprovalLog(approvalRequest));
	}

	@Override
	public ListApprovalFlowOfRequestBySceneResponse listApprovalFlowOfRequestByScene(
			ListApprovalFlowOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());
		
		return new ListApprovalFlowOfRequestBySceneResponse(listApprovalFlowUser(approvalRequest.getFlowId(), approvalRequest.getCurrentLevel()));
	}	
	
	private ApprovalOwnerInfo getOwnerInfoFromSceneToken(String sceneTokenString){
		if (StringUtils.isBlank(sceneTokenString)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"sceneToken cannot be null");
		}
		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(sceneTokenString, SceneTokenDTO.class);
		Long organizationId = null;
		Long communityId = null;
	    SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());
	    switch(sceneType) {
	    case DEFAULT:
	    case PARK_TOURIST:
	        communityId = sceneTokenDTO.getEntityId();
			List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
			if (list != null && list.size() > 0) {
				organizationId = list.get(0).getOrganizationId();
			}
	        break;
	    case FAMILY:
	        FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
	        if(family != null) {
	            communityId = family.getCommunityId();
	            list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
				if (list != null && list.size() > 0) {
					organizationId = list.get(0).getOrganizationId();
				}
	        }
	        break;
        case ENTERPRISE: 
        case ENTERPRISE_NOAUTH: 
        	organizationId = sceneTokenDTO.getEntityId();
            break;
        case PM_ADMIN:
        	organizationId = sceneTokenDTO.getEntityId();
            break;
	    default:
	        break;
	    }
	    
	    //鉴于目前审批的owner只有organization，所以这里只取organizationId
	    return new ApprovalOwnerInfo(sceneTokenDTO.getNamespaceId(), ApprovalOwnerType.ORGANIZATION.getCode(), organizationId);
	}

	@Override
	public ListApprovalRequestBySceneResponse listApprovalRequestByScene(ListApprovalRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		checkApprovalType(cmd.getApprovalType());
		if (cmd.getCategoryId() != null) {
			checkCategoryExist(cmd.getCategoryId(), ownerInfo, cmd.getApprovalType());
		}
		ApprovalRequestCondition condition = new ApprovalRequestCondition();
		condition.setOwnerInfo(ownerInfo);
		condition.setApprovalType(cmd.getApprovalType());
		condition.setCategoryId(cmd.getCategoryId());
		condition.setPageAnchor(cmd.getPageAnchor());
		condition.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
		
		List<ApprovalRequest> approvalRequestList = approvalRequestProvider.listApprovalRequestByCondition(condition);
		
		Long nextPageAnchor = null;
		if (approvalRequestList.size() > condition.getPageSize().intValue()) {
			approvalRequestList.remove(approvalRequestList.size()-1);
			nextPageAnchor = approvalRequestList.get(approvalRequestList.size()-1).getId();
		}
		
		ApprovalRequestHandler handler = getApprovalRequestHandler(cmd.getApprovalType());
		
		List<BriefApprovalRequestDTO> resultList = approvalRequestList.stream().map(a->{
			BriefApprovalRequestDTO briefApprovalRequestDTO = handler.processBriefApprovalRequest(a);
			return briefApprovalRequestDTO;
		}).collect(Collectors.toList());
		
		return new ListApprovalRequestBySceneResponse(nextPageAnchor, resultList);
	}

	@Override
	public CreateApprovalRequestBySceneResponse createApprovalRequestByScene(CreateApprovalRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		checkApprovalType(cmd.getApprovalType());
		if (cmd.getCategoryId() != null) {
			checkCategoryExist(cmd.getCategoryId(), ownerInfo, cmd.getApprovalType());
		}
		
		ApprovalRequestHandler handler = getApprovalRequestHandler(cmd.getApprovalType());
		
		ApprovalRequest result = dbProvider.execute(s->{
			//1. 申请表增加一条记录
			//前置处理器，处理一些特定审批类型的数据检查等操作
			ApprovalRequest approvalRequest = handler.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
			if (approvalRequest.getId() == null) {
				approvalRequestProvider.createApprovalRequest(approvalRequest);
			} else {
				approvalRequestProvider.updateApprovalRequest(approvalRequest);
			}
			
			//2. 处理附件，如果有的话
			if (ListUtils.isEmpty(cmd.getAttachmentList())) {
				createAttachment(userId, approvalRequest.getId(), cmd.getAttachmentList());
			}
			
			//3. 处理日志
			createApprovalOpRequest(userId, approvalRequest.getId(), cmd.getReason());
			
			//4. 后置处理器，处理时间，回调考勤接口更新打卡相关接口
			handler.postProcessCreateApprovalRequest(userId, ownerInfo, approvalRequest, cmd);

			return approvalRequest;
		});
		
		return new CreateApprovalRequestBySceneResponse(handler.processBriefApprovalRequest(result));
	}

	private void createApprovalOpRequest(Long userId, Long requestId, String processMessage) {
		ApprovalOpRequest approvalOpRequest = new ApprovalOpRequest();
		approvalOpRequest.setRequestId(requestId);
		approvalOpRequest.setProcessMessage(processMessage);
		approvalOpRequest.setOperatorUid(userId);
		approvalOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalOpRequest.setApprovalStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		approvalOpRequestProvider.createApprovalOpRequest(approvalOpRequest);
	}

	private List<Attachment> createAttachment(Long userId, Long ownerId, List<AttachmentDescriptor> attachmentList) {
		List<Attachment> attachments = attachmentList.stream().map(a->{
			Attachment attachment = new Attachment();
			attachment.setOwnerId(ownerId);
			attachment.setContentType(a.getContentType());
			attachment.setContentUri(a.getContentUri());
			attachment.setCreatorUid(userId);
			return attachment;
		}).collect(Collectors.toList());
		
		attachmentProvider.createAttachments(EhApprovalAttachments.class, attachments);
		
		return attachments;
	}

	@Override
	public ApprovalFlow getApprovalFlowByUser(String ownerType, Long ownerId, Long userId, Byte approvalType){
		ApprovalRule approvalRule= punchService.getApprovalRule(ownerType, ownerId, userId);
		if (approvalRule == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval rule");
		}
		ApprovalRuleFlowMap approvalRuleFlowMap = approvalRuleFlowMapProvider.findConcreteApprovalRuleFlowMap(approvalRule.getId(), approvalType);
		if (approvalRuleFlowMap == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval rule flow map");
		}
		ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowById(approvalRuleFlowMap.getFlowId());
		if (approvalFlow == null || approvalFlow.getStatus().byteValue() == CommonStatus.INACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval flow");
		}
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByFlowId(approvalFlow.getId());
		if (ListUtils.isEmpty(approvalFlowLevelList)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval flow level");
		}
		return approvalFlow;
	}

	@Override
	public ListApprovalCategoryBySceneResponse listApprovalCategoryByScene(ListApprovalCategoryBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		checkApprovalType(cmd.getApprovalType());
		
		List<ApprovalCategory> categoryList = approvalCategoryProvider.listApprovalCategory(ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), cmd.getApprovalType());
		
		return new ListApprovalCategoryBySceneResponse(categoryList.stream().map(c->ConvertHelper.convert(c, ApprovalCategoryDTO.class)).collect(Collectors.toList()));
	}
	
	@Override
	public void approveApprovalRequest(ApproveApprovalRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		dbProvider.execute(s->{
			
			return true;
		});
	}

	@Override
	public void rejectApprovalRequest(RejectApprovalRequestCommand cmd) {
	}

	@Override
	public void cancelApprovalRequestByScene(CancelApprovalRequestBySceneCommand cmd) {
	}

	@Override
	public ListApprovalUserResponse listApprovalUser(ListApprovalUserCommand cmd) {
		return null;
	}

	@Override
	public ListApprovalRequestResponse listApprovalRequest(ListApprovalRequestCommand cmd) {
		if (cmd.getQueryType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"query type cannot be empty");
		}
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		if (cmd.getCategoryId() != null) {
			checkCategoryExist(cmd.getCategoryId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
		}
		
		return null;
	}


	

	
	
	
	
	
	
	
	
	


	private ApprovalRequestHandler getApprovalRequestHandler(Byte approvalType){
		if (approvalType != null) {
			ApprovalRequestHandler handler = PlatformContext.getComponent(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + approvalType);
			if (handler != null) {
				return handler;
			}
		}
		
		return PlatformContext.getComponent(ApprovalRequestDefaultHandler.APPROVAL_REQUEST_DEFAULT_HANDLER_NAME);
	}

	@Override
	public List<TimeRange> listTimeRangeByRequestId(Long requestId) {
		List<ApprovalTimeRange> approvalTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(requestId);
		List<TimeRange> timeRangeList = approvalTimeRangeList.stream().map(a->{
			return ConvertHelper.convert(a, TimeRange.class);
		}).collect(Collectors.toList());
		
		return timeRangeList;
	}

	@Override
	public List<AttachmentDescriptor> listAttachmentByRequestId(Long requestId) {
		return getAttachments(requestId);
	}




	
}
