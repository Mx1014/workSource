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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Controller;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.rest.approval.ApprovalFlowDTO;
import com.everhomes.rest.approval.ApprovalFlowLevelDTO;
import com.everhomes.rest.approval.ApprovalFlowOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogAndFlowOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogTitleTemplateCode;
import com.everhomes.rest.approval.ApprovalNotificationTemplateCode;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalQueryType;
import com.everhomes.rest.approval.ApprovalRequestCondition;
import com.everhomes.rest.approval.ApprovalRuleDTO;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalTargetType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.ApprovalUser;
import com.everhomes.rest.approval.ApprovalUserDTO;
import com.everhomes.rest.approval.ApproveApprovalRequesBySceneCommand;
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
import com.everhomes.rest.approval.GetTargetApprovalRuleCommand;
import com.everhomes.rest.approval.GetTargetApprovalRuleResponse;
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
import com.everhomes.rest.approval.ListMyApprovalsBySceneCommand;
import com.everhomes.rest.approval.ListTargetUsersCommand;
import com.everhomes.rest.approval.RejectApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.RejectApprovalRequestCommand;
import com.everhomes.rest.approval.RequestDTO;
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
import com.everhomes.rest.approval.UpdateTargetApprovalRuleCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.ui.user.ContactSignUpStatus;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.techpark.punch.PunchRuleOwnerMap;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;

import freemarker.core.ReturnInstruction.Return;

@Component
public class ApprovalServiceImpl implements ApprovalService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalServiceImpl.class);
	@Autowired
	private NamespaceProvider namespaceProvider;
	
    
	@Autowired
	private LocaleTemplateService localeTemplateService;

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
	private PunchProvider punchProvider;

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
	private OrganizationService organizationService;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private MessagingService messagingService;
	
	public static ApprovalCategoryDTO defaultCategory = new ApprovalCategoryDTO(0L,ApprovalType.ABSENCE.getCode(),"事假");
	@Override
	public CreateApprovalCategoryResponse createApprovalCategory(CreateApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getCategoryName()) || cmd.getApprovalType() == null || cmd.getCategoryName().equals(defaultCategory.getCategoryName())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.CATEGORY_EMPTY_NAME,
					"Invalid parameters: cmd=" + cmd);
		}
		if (cmd.getCategoryName().length() > 8) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.CATEGORY_NAME_LENGTH_GREATER_EIGHT,
					"length of name cannot be greater than 8 words, name=" + cmd.getCategoryName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		checkApprovalCategoryNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType(),
				cmd.getCategoryName(), null);

		ApprovalCategory category = ConvertHelper.convert(cmd, ApprovalCategory.class);
		category.setApprovalType(cmd.getApprovalType());
		category.setStatus(CommonStatus.ACTIVE.getCode());
		category.setCreatorUid(userId);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setUpdateTime(category.getCreateTime());
		category.setOperatorUid(userId);

		approvalCategoryProvider.createApprovalCategory(category);
		return new CreateApprovalCategoryResponse(ConvertHelper.convert(category, ApprovalCategoryDTO.class));
	}

	private void checkApprovalCategoryNameDuplication(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType,
			String categoryName, Long id) {
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryByName(namespaceId, ownerType, ownerId, approvalType,
				categoryName);
		if (category != null && (id == null || id.longValue() != category.getId().longValue())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.CATEGORY_EXIST_NAME,
					"exist category name: categoryName=" + categoryName);
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
		if (cmd.getId() == null || StringUtils.isBlank(cmd.getCategoryName())|| cmd.getId().equals(defaultCategory.getId())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id and categoryName cannot be empty");
		}
		if (cmd.getCategoryName().length() > 8 || cmd.getCategoryName().equals(defaultCategory.getCategoryName())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.CATEGORY_NAME_LENGTH_GREATER_EIGHT,
					"length of name cannot be greater than 8 words, name=" + cmd.getCategoryName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalType(cmd.getApprovalType());
		checkApprovalCategoryNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType(),
				cmd.getCategoryName(), cmd.getId());

		Tuple<ApprovalCategory, Boolean> tuple = coordinationProvider.getNamedLock(
				CoordinationLocks.UPDATE_APPROVAL_CATEGORY.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId(), cmd.getApprovalType());
					category.setCategoryName(cmd.getCategoryName());
					category.setUpdateTime(category.getCreateTime());
					category.setOperatorUid(userId);
					approvalCategoryProvider.updateApprovalCategory(category);
					return category;
				});

		return new UpdateApprovalCategoryResponse(ConvertHelper.convert(tuple.first(), ApprovalCategoryDTO.class));
	}
	@Override
	public ApprovalCategory findApprovalCategoryById(Long id){
		if(id.equals(defaultCategory.getId()))
			return ConvertHelper.convert(defaultCategory, ApprovalCategory.class);
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(id);
		return category;
		
	}
	private ApprovalCategory checkCategoryExist(Long id, Integer namespaceId, String ownerType, Long ownerId, Byte approvalType) {
		if(id.equals(defaultCategory.getId()))
			return ConvertHelper.convert(defaultCategory, ApprovalCategory.class);
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(id);
		if (category == null || category.getNamespaceId().intValue() != namespaceId.intValue()
				|| !ownerType.equals(category.getOwnerType()) || category.getOwnerId().longValue() != ownerId.longValue()
				|| category.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()
				|| category.getApprovalType().byteValue() != approvalType.byteValue()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist category: categoryId=" + id + ", namespaceId=" + namespaceId + ", ownerType=" + ownerType
							+ ", ownerId=" + ownerId);
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

		List<ApprovalCategory> categoryList = approvalCategoryProvider.listApprovalCategory(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getApprovalType());

		ListApprovalCategoryResponse resp =  new ListApprovalCategoryResponse(categoryList.stream().map(c -> ConvertHelper.convert(c, ApprovalCategoryDTO.class))
				.collect(Collectors.toList()) );
		resp.getCategoryList().add(defaultCategory);
		return resp;
	}

	@Override
	public void deleteApprovalCategory(DeleteApprovalCategoryCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "id cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_CATEGORY.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalCategory category = checkCategoryExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId(), cmd.getApprovalType());
					category.setStatus(CommonStatus.INACTIVE.getCode());
					category.setUpdateTime(category.getCreateTime());
					category.setOperatorUid(userId);
					approvalCategoryProvider.updateApprovalCategory(category);
					return null;
				});
	}

	@Override
	public CreateApprovalFlowInfoResponse createApprovalFlowInfo(CreateApprovalFlowInfoCommand cmd) {
		Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.APPROVAL_FLOW_EMPTY_NAME,
					"name cannot be empty");
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVAL_FLOW_NAME_LENGTH_GREATER_EIGHT,
					"length of name cannot be greater than 8 words, name=" + cmd.getName());
		}

		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName(), null);

		ApprovalFlow approvalFlow = ConvertHelper.convert(cmd, ApprovalFlow.class);
		approvalFlow.setStatus(CommonStatus.ACTIVE.getCode());
		approvalFlow.setCreatorUid(userId);
		approvalFlow.setOperatorUid(userId);
		approvalFlow.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalFlow.setUpdateTime(approvalFlow.getCreateTime());
		approvalFlowProvider.createApprovalFlow(approvalFlow);

		return new CreateApprovalFlowInfoResponse(ConvertHelper.convert(approvalFlow, BriefApprovalFlowDTO.class));
	}

	private void checkApprovalFlowNameDuplication(Integer namespaceId, String ownerType, Long ownerId, String name, Long id) {
//		ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowByName(namespaceId, ownerType, ownerId, name);
//		if (approvalFlow != null && (id == null || id.longValue() != approvalFlow.getId().longValue())) {
//			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.APPROVAL_FLOW_EXIST_NAME,
//					"exist name, name=" + name);
//		}
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
					"length of name cannot be greater than 8 words, name=" + cmd.getName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowNameDuplication(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName(), cmd.getId());

		Tuple<ApprovalFlow, Boolean> tuple = coordinationProvider.getNamedLock(
				CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId());
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
		if (approvalFlow == null || approvalFlow.getNamespaceId().intValue() != namespaceId.intValue()
				|| !ownerType.equals(approvalFlow.getOwnerType()) || approvalFlow.getOwnerId().longValue() != ownerId.longValue()
				|| approvalFlow.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist approvalFlow: approvalFlowId=" + id + ", namespaceId=" + namespaceId + ", ownerType=" + ownerType
							+ ", ownerId=" + ownerId);
		}
		return approvalFlow;
	}

	@Override
	public CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd) {
		Long userId = getUserId();
		if (cmd.getFlowId() == null || cmd.getLevel() == null || ListUtils.isEmpty(cmd.getApprovalUserList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd=" + cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (checkCurrentLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"current level exists, so you cannot create it again: flowId" + cmd.getFlowId() + ", level=" + cmd.getLevel());
		}
		if (cmd.getLevel().byteValue() != (byte) 1 && !checkPreviousLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(
					ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"previous level does not exist, so you cannot create current level: flowId" + cmd.getFlowId() + ", level="
							+ cmd.getLevel());
		}

		dbProvider.execute(s -> {
			createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalUserList());
			return true;
		});

		processTargetName(cmd.getApprovalUserList(), cmd.getOwnerType(), cmd.getOwnerId());

		return new CreateApprovalFlowLevelResponse(cmd.getFlowId(),
				new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalUserList()));
	}

	private void createApprovalFlowLevel(Long flowId, Byte level, List<ApprovalUser> approvalPersonList) {
		approvalPersonList.forEach(a -> {
			if (ApprovalTargetType.fromCode(a.getTargetType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"error target type: targetType=" + a.getTargetType() + ", targetId=" + a.getTargetId());
			}
			ApprovalFlowLevel ap = new ApprovalFlowLevel();
			ap.setFlowId(flowId);
			ap.setLevel(level);
			ap.setTargetType(a.getTargetType());
			ap.setTargetId(a.getTargetId());
			approvalFlowLevelProvider.createApprovalFlowLevel(ap);
		});
	}

	private void processTargetName(List<ApprovalUser> approvalPersonList, String ownerType, Long ownerId) {
		if (ListUtils.isEmpty(approvalPersonList)) {
			return;
		}
		approvalPersonList.forEach(a -> {
			a.setTargetName(getTargetName(a.getTargetType(), a.getTargetId(), ownerType, ownerId));
		});
	}

	private String getTargetName(Byte targetType, Long targetId, String ownerType, Long ownerId) {
		ApprovalTargetType approvalTargetType = ApprovalTargetType.fromCode(targetType);
		switch (approvalTargetType) {
		case USER:
			return getUserName(targetId, ownerId); // 目前只有组织，所以此处直接传ownerId作为组织id
		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error target type: targetType=" + targetType + ", targetId=" + targetId);
		}
	}

	@Override
	public String getUserName(Long userId, Long organizationId) {
		OrganizationMember organizationMember = getOrganizationMember(userId, organizationId);
		if (organizationMember != null) {
			return organizationMember.getContactName();
		}

		User user = userProvider.findUserById(userId);
		if (user != null) {
			return user.getNickName();
		}
		return "";
	}

	private OrganizationMember getOrganizationMember(Long userId, Long organizationId) {
		return organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
	}

	private boolean checkCurrentLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, level);
		if (ListUtils.isNotEmpty(approvalFlowLevelList)) {
			return true;
		}
		return false;
	}

	private boolean checkPreviousLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, (byte) (level - 1));
		if (ListUtils.isNotEmpty(approvalFlowLevelList)) {
			return true;
		}
		return false;
	}

	private boolean checkNextLevelExist(Long flowId, Byte level) {
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(flowId, (byte) (level + 1));
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
					"Invalid parameters: cmd=" + cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (!checkCurrentLevelExist(cmd.getFlowId(), cmd.getLevel())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"current level does not exist, so you cannot update it: flowId" + cmd.getFlowId() + ", level=" + cmd.getLevel());
		}

		dbProvider.execute(s -> {
			approvalFlowLevelProvider.deleteApprovalLevels(cmd.getFlowId(), cmd.getLevel());
			if (ListUtils.isNotEmpty(cmd.getApprovalUserList())) {
				createApprovalFlowLevel(cmd.getFlowId(), cmd.getLevel(), cmd.getApprovalUserList());
			} else {
				if (checkNextLevelExist(cmd.getFlowId(), cmd.getLevel())) {
					throw RuntimeErrorException.errorWith(
							ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"next level exists, so you cannot update this level to empty: flowId" + cmd.getFlowId() + ", level="
									+ cmd.getLevel());
				}
			}
			return true;
		});

		processTargetName(cmd.getApprovalUserList(), cmd.getOwnerType(), cmd.getOwnerId());

		return new UpdateApprovalFlowLevelResponse(cmd.getFlowId(),
				new ApprovalFlowLevelDTO(cmd.getLevel(), cmd.getApprovalUserList()));
	}

	@Override
	public ListApprovalFlowResponse listApprovalFlow(ListApprovalFlowCommand cmd) {
		Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<ApprovalFlow> approvalFlowList = approvalFlowProvider.listApprovalFlow(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getPageAnchor(), pageSize + 1);

		Long nextPageAnchor = null;
		if (approvalFlowList.size() > pageSize) {
			approvalFlowList.remove(approvalFlowList.size() - 1);
			nextPageAnchor = approvalFlowList.get(approvalFlowList.size() - 1).getId();
		}

		// 处理每个审批流程中每级设置的人
		List<ApprovalFlowDTO> list = processApprovalFlowLevelList(approvalFlowList, cmd.getOwnerType(), cmd.getOwnerId());

		return new ListApprovalFlowResponse(nextPageAnchor, list);
	}

	private List<ApprovalFlowDTO> processApprovalFlowLevelList(final List<ApprovalFlow> approvalFlowList, String ownerType,
			Long ownerId) {
		if (ListUtils.isEmpty(approvalFlowList)) {
			return new ArrayList<ApprovalFlowDTO>();
		}
		final List<ApprovalFlowDTO> resultList = new ArrayList<>();
		final List<Long> flowIdList = new ArrayList<>();
		final Map<Long, ApprovalFlowDTO> flowMap = new HashMap<>();
		approvalFlowList.forEach(a -> {
			ApprovalFlowDTO ad = ConvertHelper.convert(a, ApprovalFlowDTO.class);
			flowIdList.add(a.getId());
			// 注意，resultList与flowMap中存储的是同样的对象
				resultList.add(ad);
				flowMap.put(a.getId(), ad);
			});

		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByFlowIds(flowIdList);
		// 把平行的记录分组，先按flowId分组，再按level分组，最后的小分组中存储的就是每个flowId每个level中的人员
		Map<Long, Map<Byte, List<ApprovalFlowLevel>>> map = approvalFlowLevelList.stream().collect(
				Collectors.groupingBy(ApprovalFlowLevel::getFlowId, Collectors.groupingBy(ApprovalFlowLevel::getLevel)));
		map.forEach((k1, v1) -> {
			// k1为flowId
			final List<ApprovalFlowLevelDTO> levelList = new ArrayList<>();
			v1.forEach((k2, v2) -> {
				// k2为level
				List<ApprovalUser> approvalPersonList = v2.stream().map(a -> {
					ApprovalUser approvalPerson = new ApprovalUser();
					approvalPerson.setTargetType(a.getTargetType());
					approvalPerson.setTargetId(a.getTargetId());
					approvalPerson.setTargetName(getTargetName(a.getTargetType(), a.getTargetId(), ownerType, ownerId));
					return approvalPerson;
				}).collect(Collectors.toList());
				levelList.add(new ApprovalFlowLevelDTO(k2, approvalPersonList));
			});
			flowMap.get(k1).setLevelList(levelList);
		});

		// 因为resultList与flowMap中存储的是同样的对象，所以flowMap处理完了，resultList也就处理完了
		return resultList;
	}

	@Override
	public void deleteApprovalFlow(DeleteApprovalFlowCommand cmd) {
		Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "id cannot be empty");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalFlow approvalFlow = checkApprovalFlowExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId());
					checkApprovalRuleFlowMapExist(cmd.getId());
					approvalFlow.setStatus(CommonStatus.INACTIVE.getCode());
					approvalFlow.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					approvalFlowProvider.updateApprovalFlow(approvalFlow);
					return null;
				});
	}

	private void checkApprovalRuleFlowMapExist(Long flowId) {
		ApprovalRuleFlowMap approvalRuleFlowMap = approvalRuleFlowMapProvider.findOneApprovalRuleFlowMapByFlowId(flowId);
		if (approvalRuleFlowMap != null) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVAL_FLOW_EXIST_APPROVAL_RULE_WHEN_DELETE,
					"This flow has related to some rules, so you cannot delete it: flowId=" + flowId);
		}
	}

	@Override
	public ListBriefApprovalFlowResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd) {
		Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		List<ApprovalFlow> approvalFlowList = approvalFlowProvider.listApprovalFlow(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId());

		return new ListBriefApprovalFlowResponse(approvalFlowList.stream()
				.map(a -> ConvertHelper.convert(a, BriefApprovalFlowDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (StringUtils.isBlank(cmd.getName()) || ListUtils.isEmpty(cmd.getRuleFlowMapList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.APPROVAL_RULE_EMPTY_NAME,
					"Invalid parameters: cmd=" + cmd);
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVAL_RULE_NAME_LENGTH_GREATER_EIGHT, "length of name cannot be greater than 8: name="
							+ cmd.getName());
		}

		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleNameDuplication(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName(), null);
		checkRuleFlowMap(cmd.getRuleFlowMapList());

		final ApprovalRule approvalRule = ConvertHelper.convert(cmd, ApprovalRule.class);
		dbProvider.execute(s -> {
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

	private void checkRuleFlowMap(List<RuleFlowMap> list) {
		// 1. 每一个对象中的approvalType和flowId必须不为空且存在
		list.forEach(r -> {
			if (r.getApprovalType() == null || r.getFlowId() == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"approvalType and flowId cannot be empty: approvalType=" + r.getApprovalType() + ", flowId=" + r.getFlowId());
			}
			if (ApprovalType.fromCode(r.getApprovalType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist approvalType: approvalType=" + r.getApprovalType());
			}
			if (approvalFlowProvider.findApprovalFlowById(r.getFlowId()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist flow: flowId=" + r.getFlowId());
			}
		});

		// 2. list中不能存在相同的审批类型
		Set<Byte> set = new HashSet<>();
		list.forEach(r -> set.add(r.getApprovalType()));
		if (list.size() > set.size()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cannot contain same approval type");
		}

		// 3. list中必须存在产品规定必填的审批规则
		checkMustExistApprovalType(list, ApprovalType.ABSENCE.getCode());
		checkMustExistApprovalType(list, ApprovalType.EXCEPTION.getCode());
	}

	private void checkMustExistApprovalType(List<RuleFlowMap> list, Byte approvalType) {
		for (RuleFlowMap ruleFlowMap : list) {
			if (ruleFlowMap.getApprovalType().byteValue() == approvalType.byteValue()) {
				return;
			}
		}
		if (ApprovalType.ABSENCE.getCode() == approvalType.byteValue()) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVAL_RULE_EMPTY_ABSENCE,
					"parameters must contain specific approval type, approvalType=" + approvalType);
		} else if (ApprovalType.EXCEPTION.getCode() == approvalType.byteValue()) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVAL_RULE_EMPTH_EXCEPTION,
					"parameters must contain specific approval type, approvalType=" + approvalType);
		}
	}

	private void checkApprovalRuleNameDuplication(Long userId, Integer namespaceId, String ownerType, Long ownerId, String ruleName,
			Long id) {
		ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleByName(namespaceId, ownerType, ownerId, ruleName);
		if (approvalRule != null && (id == null || id.longValue() != approvalRule.getId().longValue())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.APPROVAL_RULE_EXIST_NAME,
					"repeated rule name, ruleName=" + ruleName);
		}
	}

	private void processApprovalTypeName(List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r -> {
			LocaleString localeString = localeStringProvider.find(ApprovalTypeTemplateCode.SCOPE, r.getApprovalType().toString(),
					UserContext.current().getUser().getLocale());
			if (localeString != null) {
				r.setApprovalTypeName(localeString.getText());
			}
		});
	}

	private void processFlowName(List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r -> {
			ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowById(r.getFlowId());
			if (approvalFlow != null) {
				r.setFlowName(approvalFlow.getName());
			}
		});
	}

	private void createApprovalRuleFlowMap(final Long ruleId, List<RuleFlowMap> ruleFlowMapList) {
		ruleFlowMapList.forEach(r -> {
			if (ApprovalType.fromCode(r.getApprovalType()) == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist approval type: approvalType=" + r.getApprovalType() + ", flowId=" + r.getFlowId());
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
					"Invalid parameters: cmd=" + cmd);
		}
		if (cmd.getName().length() > 8) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"length of name cannot be greater than 8: name=" + cmd.getName());
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkApprovalRuleNameDuplication(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName(),
				cmd.getId());
		checkRuleFlowMap(cmd.getRuleFlowMapList());

		Tuple<ApprovalRule, Boolean> tuple = coordinationProvider.getNamedLock(
				CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId());
					dbProvider.execute(s -> {
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

	private void deleteApprovalRuleFlowMapAndFlowAndLevel(Long ruleId) {
		List<ApprovalRuleFlowMap> maps =  approvalRuleFlowMapProvider.listRuleFlowMapsByRuleId(ruleId);
		List<Long> flowIds = maps.stream().map(r -> {
			return r.getFlowId();
		}).collect(Collectors.toList());
		approvalFlowLevelProvider.deleteApprovalLevels(flowIds);
		approvalFlowProvider.deleteApprovalFlows(flowIds);
		approvalRuleFlowMapProvider.deleteRuleFlowMapByRuleId(ruleId);
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
					"not exist approval rule: ruleId=" + id + ", namespaceId=" + namespaceId + ", ownerType=" + ownerType
							+ ", ownerId=" + ownerId);
		}
		return approvalRule;
	}

	@Override
	public void deleteApprovalRule(DeleteApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: cmd=" + cmd);
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_FLOW.getCode() + cmd.getId()).enter(
				() -> {
					ApprovalRule approvalRule = checkApprovalRuleExist(cmd.getId(), cmd.getNamespaceId(), cmd.getOwnerType(),
							cmd.getOwnerId());
					dbProvider.execute(s -> {
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

		List<ApprovalRule> approvalRuleList = approvalRuleProvider.listApprovalRule(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getPageAnchor(), pageSize + 1);

		Long nextPageAnchor = null;
		if (approvalRuleList.size() > pageSize) {
			approvalRuleList.remove(approvalRuleList.size() - 1);
			nextPageAnchor = approvalRuleList.get(approvalRuleList.size() - 1).getId();
		}

		// 处理审批类型
		List<ApprovalRuleDTO> list = processApprovalRuleFlowMapList(approvalRuleList);

		return new ListApprovalRuleResponse(nextPageAnchor, list);
	}

	private List<ApprovalRuleDTO> processApprovalRuleFlowMapList(List<ApprovalRule> approvalRuleList) {
		List<ApprovalRuleDTO> resultList = new ArrayList<>();
		List<Long> ruleIdList = new ArrayList<>();
		Map<Long, ApprovalRuleDTO> ruleMap = new HashMap<>();
		approvalRuleList.forEach(a -> {
			ApprovalRuleDTO approvalRuleDTO = ConvertHelper.convert(a, ApprovalRuleDTO.class);
			ruleIdList.add(a.getId());
			resultList.add(approvalRuleDTO);
			ruleMap.put(a.getId(), approvalRuleDTO);
		});

		List<ApprovalRuleFlowMap> ruleList = approvalRuleFlowMapProvider.listApprovalRuleFlowMapByRuleIds(ruleIdList);
		Map<Long, List<ApprovalRuleFlowMap>> map = ruleList.stream().collect(Collectors.groupingBy(ApprovalRuleFlowMap::getRuleId));
		map.forEach((key, value) -> {
			List<RuleFlowMap> ruleFlowMapList = value.stream().map(v -> ConvertHelper.convert(v, RuleFlowMap.class))
					.collect(Collectors.toList());
			processApprovalTypeName(ruleFlowMapList);
			processFlowName(ruleFlowMapList);
			ruleMap.get(key).setRuleFlowMapList(ruleFlowMapList);
		});

		return resultList;
	}

	private Long getUserId() {
		return UserContext.current().getUser().getId();
	}

	private void checkPrivilege(Long userId, Integer namespaceId, String ownerType, Long ownerId) {
		if (namespaceId == null || StringUtils.isBlank(ownerType) || ownerId == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, namespaceId=" + namespaceId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
		}

		checkNamespaceExist(namespaceId);
		checkOwnerExist(namespaceId, ownerType, ownerId);
	}

	private void checkPrivilege(Long userId, ApprovalOwnerInfo ownerInfo) {
		checkPrivilege(userId, ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId());
	}

	private void checkNamespaceExist(Integer namespaceId) {
		if (namespaceId.intValue() != 0 && namespaceProvider.findNamespaceById(namespaceId) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist namespace: " + namespaceId);
		}
	}

	private void checkOwnerExist(Integer namespaceId, String ownerType, Long ownerId) {
		ApprovalOwnerType approvalOwnerType = null;

		if ((approvalOwnerType = ApprovalOwnerType.fromCode(ownerType)) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not exist ownerType: " + ownerType);
		}

		switch (approvalOwnerType) {
		case ORGANIZATION:
			Organization organization = organizationProvider.findOrganizationById(ownerId);
			if (organization == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Not exist organization: " + ownerId);
			}
			if (organization.getNamespaceId().intValue() != namespaceId.intValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"the organization is beyond the namespace: namespaceId=" + namespaceId + ", organizationId" + ownerId);
			}
			break;
		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "error ownerType: "
					+ ownerType);
		}

	}

	@Override
	public ListBriefApprovalRuleResponse listBriefApprovalRule(ListBriefApprovalRuleCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		List<ApprovalRule> approvalRuleList = approvalRuleProvider.listApprovalRule(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId());

		return new ListBriefApprovalRuleResponse(approvalRuleList.stream()
				.map(a -> ConvertHelper.convert(a, BriefApprovalRuleDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public GetApprovalBasicInfoOfRequestResponse getApprovalBasicInfoOfRequest(GetApprovalBasicInfoOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getRequestId());

		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());

		ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequestDTO = handler.processApprovalBasicInfoOfRequest(approvalRequest);

		return new GetApprovalBasicInfoOfRequestResponse(approvalBasicInfoOfRequestDTO);
	}

	private ApprovalRequest checkApprovalRequestExist(Integer namespaceId, String ownerType, Long ownerId, Long requestId) {
		ApprovalRequest approvalRequest = approvalRequestProvider.findApprovalRequestById(requestId);
		if (approvalRequest == null || approvalRequest.getNamespaceId().intValue() != namespaceId.intValue()
				|| approvalRequest.getOwnerId().longValue() != ownerId.longValue()
				|| !ownerType.equals(approvalRequest.getOwnerType())
				|| approvalRequest.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval request, requestId=" + requestId);
		}
		return approvalRequest;
	}

	private ApprovalRequest checkApprovalRequestExist(ApprovalOwnerInfo ownerInfo, String requestToken) {
		return checkApprovalRequestExist(ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId(),
				getRequestIdFromToken(requestToken));
	}

	private Long getRequestIdFromToken(String requestToken) {
		if (StringUtils.isBlank(requestToken)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"requestToken cannot be empty");
		}
		return WebTokenGenerator.getInstance().fromWebToken(requestToken, Long.class);
	}

	@Override
	public ListApprovalLogAndFlowOfRequestResponse listApprovalLogAndFlowOfRequest(ListApprovalLogAndFlowOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getRequestId());

		return new ListApprovalLogAndFlowOfRequestResponse(listApprovalLogAndFlow(approvalRequest));
	}

	private List<ApprovalLogAndFlowOfRequestDTO> listApprovalLogAndFlow(ApprovalRequest approvalRequest) {
		List<ApprovalLogOfRequestDTO> logList = listApprovalLog(approvalRequest);

		// 合并这两个列表为一个
		List<ApprovalLogAndFlowOfRequestDTO> resultList = new ArrayList<>();
		// 日志全部加入
		resultList.addAll(logList.stream()
				.map(l -> {
					ApprovalLogAndFlowOfRequestDTO approvalLogAndFlowOfRequestDTO = ConvertHelper.convert(l,ApprovalLogAndFlowOfRequestDTO.class);
					approvalLogAndFlowOfRequestDTO.setType((byte) 1);
					approvalLogAndFlowOfRequestDTO.setContentJson(JSON.toJSONString(l));
					approvalLogAndFlowOfRequestDTO.setTitle(processApprovalLogTitle(l));
					return approvalLogAndFlowOfRequestDTO;
				}).collect(Collectors.toList()));

		// 如果没有后续流程了，则不用再取流程
		if (approvalRequest.getNextLevel() != null) {
			List<ApprovalFlowOfRequestDTO> flowList = listApprovalFlowUser(approvalRequest.getFlowId(),
					approvalRequest.getCurrentLevel(), approvalRequest.getOwnerType(), approvalRequest.getOwnerId());
			// 流程只取当前进行到的level后面的
			boolean flag = false;
			if (approvalRequest.getCurrentLevel().byteValue() == (byte) 0) {
				flag = true;
			}
			for (ApprovalFlowOfRequestDTO approvalFlowOfRequestDTO : flowList) {
				if (flag) {
					ApprovalLogAndFlowOfRequestDTO approvalLogAndFlowOfRequestDTO =  ConvertHelper.convert(approvalFlowOfRequestDTO,ApprovalLogAndFlowOfRequestDTO.class);
					approvalLogAndFlowOfRequestDTO.setType((byte) 2);
					approvalLogAndFlowOfRequestDTO.setContentJson(JSON.toJSONString(approvalFlowOfRequestDTO));
					approvalLogAndFlowOfRequestDTO.setTitle(processAprrovalFlowTitle(approvalFlowOfRequestDTO));
					resultList.add(approvalLogAndFlowOfRequestDTO);
				}
				if (approvalFlowOfRequestDTO.getCurrentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
					flag = true;
				}
			}
		}
		return resultList;
	}

	private String processAprrovalFlowTitle(ApprovalFlowOfRequestDTO approvalFlowOfRequestDTO) {
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.APPROVING_FLOW;
		Map<String, Object> map = new HashMap<>();
		List<String> nickNames = approvalFlowOfRequestDTO.getNickNameList();
		StringBuffer nickNameString = new StringBuffer();
		if (nickNames == null || nickNames.size() == 0)
			return null;
		if (nickNames.size() == 1)
			map.put("nickNames", nickNames.get(0));
		else {
			for (int i = 0; i < nickNames.size(); i++) {
				if (i > 0) {
					if (i == nickNames.size() - 1) {
						nickNameString.append("或");
					} else {
						nickNameString.append("、");
					}
				}
				nickNameString.append(nickNames.get(i));
			}
			map.put("nickNames", nickNameString.toString());
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}

	/**
	 * 为审批日志拼接第一行信息
	 * */
	private String processApprovalLogTitle(ApprovalLogOfRequestDTO l) {
		String result = null;
		Map<String, Object> map = new HashMap<>();
		if (ApprovalStatus.WAITING_FOR_APPROVING.equals(ApprovalStatus.fromCode(l.getApprovalStatus()))) {
			// 初次提交
			String scope = ApprovalLogTitleTemplateCode.SCOPE;
			int code = ApprovalLogTitleTemplateCode.WAITING_FOR_APPROVING;
			map.put("nickName", l.getNickName());
			map.put("approvalType",
					localeStringProvider.find(ApprovalTypeTemplateCode.SCOPE, l.getApprovalType().toString(), UserContext.current()
							.getUser().getLocale()).getText());
			result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");

		} else {
			// agree or reject
			result = l.getNickName()
					+ localeStringProvider.find(ApprovalStatus.class.getSimpleName(), l.getApprovalStatus().toString(), UserContext
							.current().getUser().getLocale()).getText();
		}
		return result;
	}

	@Override
	public ListApprovalLogOfRequestResponse listApprovalLogOfRequest(ListApprovalLogOfRequestCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getRequestId());

		return new ListApprovalLogOfRequestResponse(listApprovalLog(approvalRequest));
	}

	private List<ApprovalLogOfRequestDTO> listApprovalLog(ApprovalRequest approvalRequest) {
		List<ApprovalLogOfRequestDTO> resultList = new ArrayList<>();

		List<ApprovalOpRequest> approvalOpRequestList = approvalOpRequestProvider.listApprovalOpRequestByRequestId(approvalRequest
				.getId());
		for (int i = 0; i < approvalOpRequestList.size(); i++) {
			ApprovalOpRequest approvalOpRequest = approvalOpRequestList.get(i);
			ApprovalLogOfRequestDTO approvalLog = new ApprovalLogOfRequestDTO();
			approvalLog.setCreateTime(approvalOpRequest.getCreateTime());
			approvalLog.setNickName(getUserName(approvalOpRequest.getOperatorUid(), approvalRequest.getOwnerId()));

			approvalLog.setRemark(approvalOpRequest.getProcessMessage());
			approvalLog.setApprovalStatus(approvalOpRequest.getApprovalStatus());

			approvalLog.setApprovalType(approvalRequest.getApprovalType());
			if (i == 0) {
				if (approvalRequest.getCategoryId() != null) {
					ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest
							.getCategoryId());
					if (approvalCategory != null) {
						approvalLog.setCategoryName(approvalCategory.getCategoryName());
					}
				}
				if (approvalRequest.getAttachmentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
					approvalLog.setAttachmentList(getAttachments(approvalRequest.getId()));
				}
			}
			resultList.add(approvalLog);
		}

		return resultList;
	}

	private List<AttachmentDescriptor> getAttachments(Long requestId) {
		List<Attachment> attachmentList = attachmentProvider.listAttachmentByOwnerId(EhApprovalAttachments.class, requestId);
		return attachmentList.stream().map(a -> {
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
		ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getRequestId());

		return new ListApprovalFlowOfRequestResponse(listApprovalFlowUser(approvalRequest.getFlowId(),
				approvalRequest.getCurrentLevel(), cmd.getOwnerType(), cmd.getOwnerId()));
	}

	private List<ApprovalFlowOfRequestDTO> listApprovalFlowUser(Long flowId, Byte currentLevel, String ownerType, Long ownerId) {
		List<ApprovalFlowOfRequestDTO> resultList = new ArrayList<>();
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByFlowId(flowId);

		Map<Byte, List<ApprovalFlowLevel>> map = approvalFlowLevelList.stream().collect(
				Collectors.groupingBy(ApprovalFlowLevel::getLevel));
		// key为level
		map.forEach((key, value) -> {
			ApprovalFlowOfRequestDTO approvalFlowOfRequestDTO = new ApprovalFlowOfRequestDTO();
			approvalFlowOfRequestDTO.setCurrentFlag(currentLevel.byteValue() == key.byteValue() ? TrueOrFalseFlag.TRUE.getCode()
					: TrueOrFalseFlag.FALSE.getCode());
			approvalFlowOfRequestDTO.setNickNameList(getNickNameList(value, ownerType, ownerId));
			resultList.add(approvalFlowOfRequestDTO);
		});

		return resultList;
	}

	private List<String> getNickNameList(List<ApprovalFlowLevel> approvalFlowLevelList, String ownerType, Long ownerId) {
		return approvalFlowLevelList.stream().map(a -> getTargetName(a.getTargetType(), a.getTargetId(), ownerType, ownerId))
				.collect(Collectors.toList());
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
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		ListApprovalLogAndFlowOfRequestBySceneResponse result = new ListApprovalLogAndFlowOfRequestBySceneResponse(approvalRequest.getApprovalType(),approvalRequest.getApprovalStatus(),
				handler.ApprovalLogAndFlowOfRequestResponseTitle(approvalRequest) ,listApprovalLogAndFlow(approvalRequest));
		handler.processListApprovalLogAndFlowOfRequestBySceneResponse(result, approvalRequest);
		return result ;
		
	}

	@Override
	public ListApprovalLogOfRequestBySceneResponse listApprovalLogOfRequestByScene(ListApprovalLogOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());

		return new ListApprovalLogOfRequestBySceneResponse(listApprovalLog(approvalRequest));
	}

	@Override
	public ListApprovalFlowOfRequestBySceneResponse listApprovalFlowOfRequestByScene(ListApprovalFlowOfRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());

		return new ListApprovalFlowOfRequestBySceneResponse(listApprovalFlowUser(approvalRequest.getFlowId(),
				approvalRequest.getCurrentLevel(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId()));
	}

	private ApprovalOwnerInfo getOwnerInfoFromSceneToken(String sceneTokenString) {
		if (StringUtils.isBlank(sceneTokenString)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"sceneToken cannot be null");
		}
		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(sceneTokenString, SceneTokenDTO.class);
		Long organizationId = null;
		Long communityId = null;
		SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());
		switch (sceneType) {
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
			if (family != null) {
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

		// 鉴于目前审批的owner只有organization，所以这里只取organizationId
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
		condition.setCreatorUid(userId);
		condition.setPageAnchor(cmd.getPageAnchor());
		condition.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));

		List<ApprovalRequest> approvalRequestList = approvalRequestProvider.listApprovalRequestByCondition(condition);

		Long nextPageAnchor = null;
		if (approvalRequestList.size() > condition.getPageSize().intValue()) {
			approvalRequestList.remove(approvalRequestList.size() - 1);
			nextPageAnchor = approvalRequestList.get(approvalRequestList.size() - 1).getId();
		}

		ApprovalRequestHandler handler = getApprovalRequestHandler(cmd.getApprovalType());

		List<BriefApprovalRequestDTO> resultList = approvalRequestList.stream().map(a -> {
			BriefApprovalRequestDTO briefApprovalRequestDTO = handler.processApprovalRequestByScene(a);
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
		CreateApprovalRequestBySceneResponse response = new CreateApprovalRequestBySceneResponse();
		ApprovalRequest result = dbProvider.execute(s -> {
			// 1. 申请表增加一条记录
			// 前置处理器，处理一些特定审批类型的数据检查等操作
				ApprovalRequest approvalRequest = handler.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
				if (approvalRequest.getId() == null) {
					approvalRequestProvider.createApprovalRequest(approvalRequest);
				} else {
					approvalRequestProvider.updateApprovalRequest(approvalRequest);
				}

				// 2. 处理附件，如果有的话
				if (ListUtils.isNotEmpty(cmd.getAttachmentList())) {
					createAttachment(userId, approvalRequest.getId(), cmd.getAttachmentList());
				}

				// 3. 处理日志
				createApprovalOpRequest(userId, approvalRequest, cmd.getReason());

				// 4. 后置处理器，处理时间，回调考勤接口更新打卡相关接口
				String flowCaseUrl = handler.postProcessCreateApprovalRequest(userId, ownerInfo, approvalRequest, cmd);
				response.setFlowCaseUrl(flowCaseUrl);

				// 5. 发消息给第一级审批者
				List<ApprovalFlowLevel> nextLevelUser = approvalFlowLevelProvider.listApprovalFlowLevel(approvalRequest.getFlowId(),
						approvalRequest.getNextLevel());
				sendMessageToNextLevel(nextLevelUser, approvalRequest);

				return approvalRequest;
			});
		response.setApprovalRequest(handler.processBriefApprovalRequest(result)); 
		return response;
	}
	private void createApprovalOpRequest(Long userId, ApprovalRequest approvalRequest, String processMessage) {
		ApprovalOpRequest approvalOpRequest = new ApprovalOpRequest();
		approvalOpRequest.setRequestId(approvalRequest.getId());
		approvalOpRequest.setProcessMessage(processMessage);
		approvalOpRequest.setOperatorUid(userId);
		approvalOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalOpRequest.setApprovalStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		approvalOpRequest.setFlowId(approvalRequest.getFlowId());
		approvalOpRequest.setLevel(approvalRequest.getCurrentLevel());
		approvalOpRequestProvider.createApprovalOpRequest(approvalOpRequest);
	}

	private List<Attachment> createAttachment(Long userId, Long ownerId, List<AttachmentDescriptor> attachmentList) {
		List<Attachment> attachments = attachmentList.stream().map(a -> {
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
	public ApprovalFlow getApprovalFlowByUser(String ownerType, Long ownerId, Long userId, Byte approvalType) {
		ApprovalRule approvalRule = punchService.getApprovalRule(ownerType, ownerId, userId);
		if (approvalRule == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist approval rule");
		}
		ApprovalRuleFlowMap approvalRuleFlowMap = approvalRuleFlowMapProvider.findConcreteApprovalRuleFlowMap(approvalRule.getId(),
				approvalType);
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

		List<ApprovalCategory> categoryList = approvalCategoryProvider.listApprovalCategory(ownerInfo.getNamespaceId(),
				ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), cmd.getApprovalType());

		ListApprovalCategoryBySceneResponse resp = new ListApprovalCategoryBySceneResponse(categoryList.stream()
				.map(c -> ConvertHelper.convert(c, ApprovalCategoryDTO.class)).collect(Collectors.toList()));
		resp.getCategoryList().add(defaultCategory);
		return resp;
	}

	@Override
	public void cancelApprovalRequestByScene(CancelApprovalRequestBySceneCommand cmd) {
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		Long requestId = WebTokenGenerator.getInstance().fromWebToken(cmd.getRequestToken(), Long.class);

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_REQUEST.getCode() + requestId).enter(
				() -> {
					dbProvider.execute(s -> {
						ApprovalRequest approvalRequest = checkApprovalRequestExist(ownerInfo, cmd.getRequestToken());
						if (approvalRequest.getApprovalStatus().byteValue() != ApprovalStatus.WAITING_FOR_APPROVING.getCode()) {
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									"error status");
						}
						if (approvalRequest.getCreatorUid().longValue() != userId.longValue()) {
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									"you cannot cancel other's request");
						}
						approvalRequest.setStatus(CommonStatus.INACTIVE.getCode());
						updateApprovalRequest(userId, approvalRequest);
						ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
						handler.processCancelApprovalRequest(approvalRequest);
						return true;
					});
					return null;
				});
	}

	@Override
	public void approveApprovalRequest(ApproveApprovalRequestCommand cmd) {
		final Long userId = getUserId();
		if (ListUtils.isEmpty(cmd.getRequestIdList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"request id cannot be null");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		dbProvider.execute(s -> {
			cmd.getRequestIdList().forEach(
					r -> {
						coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_REQUEST.getCode() + r).enter(
								() -> {
									ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(),
											cmd.getOwnerType(), cmd.getOwnerId(), r);
									ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
									checkCurrentUserExistInLevel(userId, approvalRequest.getFlowId(), approvalRequest.getNextLevel());
									List<ApprovalFlowLevel> nextLevelUser = approvalFlowLevelProvider.listApprovalFlowLevel(
											approvalRequest.getFlowId(), (byte) (approvalRequest.getNextLevel() + 1));
									if (ListUtils.isEmpty(nextLevelUser)) {
										// 如果不存在下一级别的审批人，说明此单审批结束
										// 1. 修改审批状态为同意
//										approvalRequest.setCurrentLevel(approvalRequest.getNextLevel());
//										approvalRequest.setNextLevel(null);
//										approvalRequest.setApprovalStatus(ApprovalStatus.AGREEMENT.getCode());
//										updateApprovalRequest(userId, approvalRequest);
										// 2. 发消息给申请单创建者
										sendMessageToCreator(approvalRequest, null);
										// 3. 最终同意回调业务方法
										handler.processFinalApprove(approvalRequest);
										
										// 4.对于请假的,要计算入每个月的考勤统计
										handler.calculateRangeStat(approvalRequest);
									} else {
										// 如果存在下一级别的审批人，说明此单审批未结束
										// 1. 修改审批状态为同意
										approvalRequest.setCurrentLevel(approvalRequest.getNextLevel());
										approvalRequest.setNextLevel((byte) (approvalRequest.getNextLevel() + 1));
										updateApprovalRequest(userId, approvalRequest);
										// 2. 发消息下一级别审批者
										sendMessageToNextLevel(nextLevelUser, approvalRequest);
									}
									// 添加日志
									ApprovalOpRequest approvalOpRequest = new ApprovalOpRequest();
									approvalOpRequest.setRequestId(approvalRequest.getId());
									approvalOpRequest.setOperatorUid(userId);
									approvalOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
									approvalOpRequest.setApprovalStatus(ApprovalStatus.AGREEMENT.getCode());
									approvalOpRequest.setFlowId(approvalRequest.getFlowId());
									approvalOpRequest.setLevel(approvalRequest.getCurrentLevel());
									approvalOpRequestProvider.createApprovalOpRequest(approvalOpRequest);
									return null;
								});
					});
			return true;
		});
	}
	public void finishApproveApprovalRequest(ApprovalRequest approvalRequest){
		// 1. 修改审批状态为同意
		approvalRequest.setCurrentLevel(approvalRequest.getNextLevel());
		approvalRequest.setNextLevel(null);
		approvalRequest.setApprovalStatus(ApprovalStatus.AGREEMENT.getCode());
		updateApprovalRequest(getUserId(), approvalRequest);
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		// 3. 最终同意回调业务方法
		handler.processFinalApprove(approvalRequest);
		
		// 4.对于请假的,要计算入每个月的考勤统计
		handler.calculateRangeStat(approvalRequest);
		ApprovalOpRequest approvalOpRequest = new ApprovalOpRequest();
		approvalOpRequest.setRequestId(approvalRequest.getId());
		approvalOpRequest.setOperatorUid(UserContext.current().getUser().getId());
		approvalOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalOpRequest.setApprovalStatus(ApprovalStatus.AGREEMENT.getCode());
		approvalOpRequest.setFlowId(approvalRequest.getFlowId());
		approvalOpRequest.setLevel(approvalRequest.getCurrentLevel());
		approvalOpRequestProvider.createApprovalOpRequest(approvalOpRequest); 
	}
	private void sendMessageToNextLevel(List<ApprovalFlowLevel> nextLevelUser, ApprovalRequest approvalRequest) {
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		String body = handler.processMessageToNextLevelBody(approvalRequest);
		nextLevelUser.forEach(n -> sendMessageToUser(n.getTargetId(), body, null));
	}

	private void sendMessageToCreator(ApprovalRequest approvalRequest, String reason) {
		// 分为四种情况，同意请假、驳回请假、同意异常、驳回异常
		ApprovalRequestHandler handler = getApprovalRequestHandler(approvalRequest.getApprovalType());
		String body = handler.processMessageToCreatorBody(approvalRequest, reason);
		sendMessageToUser(approvalRequest.getCreatorUid(), body, null);
	}

	private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_UID);
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
		messageDto
				.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
		messageDto.setBodyType(MessageBodyType.TEXT.getCode());
		messageDto.setBody(content);
		messageDto.setMetaAppId(AppConstants.APPID_GROUP);
		if (null != meta && meta.size() > 0) {
			messageDto.getMeta().putAll(meta);
		}
		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
				uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	private void checkCurrentUserExistInLevel(Long userId, Long flowId, Byte level) {
		ApprovalFlowLevel approvalFlowLevel = approvalFlowLevelProvider.findApprovalFlowLevel(ApprovalTargetType.USER.getCode(),
				userId, flowId, level);
		if (approvalFlowLevel == null) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.APPROVAL_LEVEL_APPROVED,
					"already approved");
		}
	}

	@Override
	public void rejectApprovalRequest(RejectApprovalRequestCommand cmd) {
		final Long userId = getUserId();
		if (StringUtils.isEmpty(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVE_OR_REJECT_EMPTY_REASON, "reason cannot be null");
		}
		if (ListUtils.isEmpty(cmd.getRequestIdList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE,
					ApprovalServiceErrorCode.APPROVE_OR_REJECT_EMPTY_REQUEST, "request id cannot be null");
		}
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());

		dbProvider.execute(s -> {
			cmd.getRequestIdList().forEach(
					r -> {
						coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_APPROVAL_REQUEST.getCode() + r).enter(
								() -> {
									ApprovalRequest approvalRequest = checkApprovalRequestExist(cmd.getNamespaceId(),
											cmd.getOwnerType(), cmd.getOwnerId(), r);
									checkCurrentUserExistInLevel(userId, approvalRequest.getFlowId(), approvalRequest.getNextLevel());

									// 直接把审批状态改为已拒绝
									// 1. 修改审批状态为同意
									approvalRequest.setCurrentLevel(approvalRequest.getNextLevel());
									approvalRequest.setNextLevel(null);
									approvalRequest.setApprovalStatus(ApprovalStatus.REJECTION.getCode());
									updateApprovalRequest(userId, approvalRequest);
									// 2. 发消息给申请单创建者
									sendMessageToCreator(approvalRequest, cmd.getReason());

									// 3. 添加日志
									ApprovalOpRequest approvalOpRequest = new ApprovalOpRequest();
									approvalOpRequest.setRequestId(approvalRequest.getId());
									approvalOpRequest.setOperatorUid(userId);
									approvalOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
									approvalOpRequest.setProcessMessage(cmd.getReason());
									approvalOpRequest.setApprovalStatus(ApprovalStatus.REJECTION.getCode());
									approvalOpRequest.setFlowId(approvalRequest.getFlowId());
									approvalOpRequest.setLevel(approvalRequest.getCurrentLevel());
									approvalOpRequestProvider.createApprovalOpRequest(approvalOpRequest);
									return null;
								});
					});
			return true;
		});
	}

	private void updateApprovalRequest(Long userId, ApprovalRequest approvalRequest) {
		approvalRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalRequest.setOperatorUid(userId);
		approvalRequestProvider.updateApprovalRequest(approvalRequest);
	}

	// 每新增一种申请就要加一个handler 这种设计真的好蠢
	/**
	 * 后台查询当前用户可以审批和已审批的所有approval
	 * */
	@Override
	public ListApprovalRequestResponse listApprovalRequest(ListApprovalRequestCommand cmd) {
		List<ApprovalRequest> resultList = new ArrayList<ApprovalRequest>();
		Long nextPageAnchor = listApprovalRequest(resultList,cmd);
		if(null == resultList || resultList.size() == 0)
			return new ListApprovalRequestResponse();
		ApprovalRequestHandler handler = getApprovalRequestHandler(cmd.getApprovalType());
		List<RequestDTO> listJson = handler.processListApprovalRequest(resultList);

		return new ListApprovalRequestResponse(nextPageAnchor, listJson);
	}

	private Long listApprovalRequest(List<ApprovalRequest> resultList, ListApprovalRequestCommand cmd) {

		if (ApprovalQueryType.fromCode(cmd.getQueryType()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"query type error, query type=" + cmd.getQueryType());
		}
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
//		checkApprovalType(cmd.getApprovalType());
		if (cmd.getCategoryId() != null) {
			checkCategoryExist(cmd.getCategoryId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getApprovalType());
		}

		// 根据用户关键字查询匹配的用户id
		List<Long> userIdList = null;
		if (StringUtils.isNotBlank(cmd.getNickName())) {
			userIdList = getMatchedUser(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getNickName());
			if (ListUtils.isEmpty(userIdList)) {
				return null;
			}
		}

		// 查询当前用户具有的哪些审批流程及审批级别
		List<ApprovalFlowLevel> approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevelByTarget(cmd.getNamespaceId(),
				cmd.getOwnerType(), cmd.getOwnerId(), ApprovalTargetType.USER.getCode(), userId);
		if (ListUtils.isEmpty(approvalFlowLevelList)) {
			return null;
		}

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		Long nextPageAnchor = null;
		// 查询待审批的，根据当前用户拥有的流程及级别来查申请表中nextLevel与之相同的记录
		if (cmd.getQueryType().byteValue() == ApprovalQueryType.WAITING_FOR_APPROVE.getCode()) {
			resultList.addAll( approvalRequestProvider.listApprovalRequestWaitingForApproving(cmd.getNamespaceId(), cmd.getOwnerType(),
					cmd.getOwnerId(), cmd.getApprovalType(), cmd.getCategoryId(), cmd.getFromDate(), cmd.getEndDate(),
					approvalFlowLevelList, userIdList, cmd.getPageAnchor(), pageSize + 1));

			if (ListUtils.isNotEmpty(resultList) && resultList.size() > pageSize) {
				resultList.remove(resultList.size() - 1);
				// 未审批的按id排序，所以未审批的可以按锚点分页
				nextPageAnchor = resultList.get(resultList.size() - 1).getId();
			}
		} else if (cmd.getQueryType().byteValue() == ApprovalQueryType.APPROVED.getCode()) {
			// 查询已审批的，已审批的需要把我所在的流程中其他人审批的也查出来，只能从日志表中查询了
			resultList.addAll(approvalRequestProvider.listApprovalRequestApproved(cmd.getNamespaceId(), cmd.getOwnerType(),
					cmd.getOwnerId(), cmd.getApprovalType(), cmd.getCategoryId(), cmd.getFromDate(), cmd.getEndDate(),
					approvalFlowLevelList, userIdList, cmd.getPageAnchor(), pageSize + 1));

			if (ListUtils.isNotEmpty(resultList) && resultList.size() > pageSize) {
				resultList.remove(resultList.size() - 1);
				// 已审批的按最后更新时间排序，所以已审批的只能按照正常的分页
				nextPageAnchor = (cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor()) + 1;
			}
		}
		return nextPageAnchor;
	}

	@Override
	public ListApprovalUserResponse listApprovalUser(ListApprovalUserCommand cmd) {
		final Long userId = getUserId();
		checkPrivilege(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		List<ApprovalUserDTO> checkedUser = new ArrayList<ApprovalUserDTO>();
		List<ApprovalFlowLevel> approvalFlowLevelList = new ArrayList<ApprovalFlowLevel>();
		if(cmd.getFlowId() != null ){
			if ( cmd.getLevel() == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"invalid parameters, cmd=" + cmd);
			}
			checkApprovalFlowExist(cmd.getFlowId(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
			approvalFlowLevelList = approvalFlowLevelProvider.listApprovalFlowLevel(cmd.getFlowId(),
					cmd.getLevel());
			checkedUser = approvalFlowLevelList.stream().map(a -> {
				String nickName = getTargetName(a.getTargetType(), a.getTargetId(), cmd.getOwnerType(), cmd.getOwnerId());
				if (StringUtils.isNotBlank(cmd.getKeyword()) && !nickName.contains(cmd.getKeyword())) {
					return null;
				}
				ApprovalUserDTO approvalUserDTO = new ApprovalUserDTO();
				approvalUserDTO.setCheckedFlag(TrueOrFalseFlag.TRUE.getCode());
				approvalUserDTO.setDepartmentName(getDepartmentNames(a.getTargetId(), cmd.getOwnerId()));
				approvalUserDTO.setNickName(nickName);
				approvalUserDTO.setUserId(a.getTargetId());
				return approvalUserDTO;
			}).filter(au -> au != null).collect(Collectors.toList());
		}
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		Organization orgCommoand = new Organization();
		if (cmd.getDepartmentId() == null) {
			orgCommoand.setId(cmd.getOwnerId());
		} else {
			orgCommoand.setId(cmd.getDepartmentId());
		}
		orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
//<<<<<<< HEAD
//
//		List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(cmd.getKeyword(), orgCommoand,
//				ContactSignUpStatus.SIGNEDUP.getCode(), locator, 10000);
//=======
		
		List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(cmd.getKeyword(),orgCommoand, ContactSignUpStatus.SIGNEDUP.getCode(),null, locator, 10000);
//>>>>>>> 3.11.0
		processPinyin(organizationMembers);
		organizationMembers.sort((o1, o2) -> {
			return o1.getFullInitial().compareTo(o2.getFullInitial());
		});

		List<ApprovalUserDTO> resultList = new ArrayList<>();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long nextPageAnchor = null;
		int i = 0;
		if (cmd.getPageAnchor() == null || cmd.getPageAnchor().longValue() == 0L) {
			resultList.addAll(checkedUser);
			if (ListUtils.isEmpty(organizationMembers)) {
				return new ListApprovalUserResponse(null, resultList);
			}
		} else {
			i = cmd.getPageAnchor().intValue() + 1;
		}

		for (;; i++) {
			OrganizationMember organizationMember = organizationMembers.get(i);
			// 如果用户已在已选择列表中，则不出现
			if (!checkUserInCheckedUser(organizationMember, approvalFlowLevelList)) {
				ApprovalUserDTO approvalUserDTO = new ApprovalUserDTO();
				approvalUserDTO.setCheckedFlag(TrueOrFalseFlag.FALSE.getCode());
				approvalUserDTO.setDepartmentName(getDepartmentNames(organizationMember.getTargetId(), cmd.getOwnerId()));
				approvalUserDTO.setNickName(getTargetName(ApprovalTargetType.USER.getCode(), organizationMember.getTargetId(),
						cmd.getOwnerType(), cmd.getOwnerId()));
				approvalUserDTO.setUserId(organizationMember.getTargetId());
				resultList.add(approvalUserDTO);
			}

			// 如果取满pageSize条，或取完了则返回
			if (resultList.size() == pageSize || i == organizationMembers.size() - 1) {
				break;
			}
		}

		// 如果取完了，则无下页，否则下页从大于i的地方开始
		if (i == organizationMembers.size() - 1) {
			nextPageAnchor = null;
		} else {
			nextPageAnchor = Long.valueOf(i);
		}

		return new ListApprovalUserResponse(nextPageAnchor, resultList);
	}

	private boolean checkUserInCheckedUser(OrganizationMember organizationMember, List<ApprovalFlowLevel> approvalFlowLevelList) {
		if (approvalFlowLevelList == null || approvalFlowLevelList.size() == 0 )
			return false;
		for (ApprovalFlowLevel approvalFlowLevel : approvalFlowLevelList) {
			if (organizationMember.getTargetId().longValue() == approvalFlowLevel.getTargetId().longValue()) {
				return true;
			}
		}
		return false;
	}

	private String getDepartmentNames(Long userId, Long organizationId) {
		OrganizationMember organizationMember = getOrganizationMember(userId, organizationId);
		Organization organization = organizationProvider.findOrganizationById(organizationId);
		if (organization == null || organizationMember == null) {
			return "";
		}
		List<OrganizationDTO> organizationList = organizationService.getOrganizationMemberGroups(OrganizationGroupType.DEPARTMENT,
				organizationMember.getContactToken(), organization.getPath());
		StringBuilder sb = new StringBuilder();
		organizationList.forEach(o -> sb.append(o.getName()).append(","));
		if (sb.length() == 0) {
			return "";
		}
		return sb.substring(0, sb.length() - 1);
	}

	private void processPinyin(List<OrganizationMember> organizationMembers) {
		organizationMembers.forEach(o -> {
			String pinyin = PinYinHelper.getPinYin(o.getContactName());
			o.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
		});
	}

	private List<Long> getMatchedUser(String ownerType, Long ownerId, String nickName) {
		CrossShardListingLocator locator = new CrossShardListingLocator();

		Organization orgCommoand = new Organization();
		orgCommoand.setId(ownerId);
		orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
//<<<<<<< HEAD
//
//		List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(nickName, orgCommoand,
//				ContactSignUpStatus.SIGNEDUP.getCode(), locator, 10000);
//
//		return organizationMembers.stream().map(o -> o.getTargetId()).collect(Collectors.toList());
//=======
		
		List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(nickName, orgCommoand, ContactSignUpStatus.SIGNEDUP.getCode(),null, locator, 10000);
		
		return organizationMembers.stream().map(o->o.getTargetId()).collect(Collectors.toList());
//>>>>>>> 3.11.0
	}

	public ApprovalRequestHandler getApprovalRequestHandler(Byte approvalType) {
		if (approvalType != null) {
			ApprovalRequestHandler handler = PlatformContext.getComponent(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX
					+ approvalType);
			if (handler != null) {
				return handler;
			}
		}

		return PlatformContext.getComponent(ApprovalRequestDefaultHandler.APPROVAL_REQUEST_DEFAULT_HANDLER_NAME);
	}

	@Override
	public List<TimeRange> listTimeRangeByRequestId(Long requestId) {
		List<ApprovalTimeRange> approvalTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(requestId);
		List<TimeRange> timeRangeList = approvalTimeRangeList.stream().map(a -> {
			TimeRange timeRange = new TimeRange();
			timeRange.setFromTime(a.getFromTime().getTime());
			timeRange.setEndTime(a.getEndTime().getTime());
			timeRange.setActualResult(a.getActualResult());
			timeRange.setType(a.getType());
			return timeRange;
		}).collect(Collectors.toList());

		return timeRangeList;
	}

	@Override
	public List<AttachmentDescriptor> listAttachmentByRequestId(Long requestId) {
		return getAttachments(requestId);
	}

	@Override
	public GetTargetApprovalRuleResponse getTargetApprovalRule(GetTargetApprovalRuleCommand cmd) {

		if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid target type or  Id parameter in the command");
		}
		GetTargetApprovalRuleResponse resp = new GetTargetApprovalRuleResponse();
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getTargetType(), cmd.getTargetId());
		if (null == map || map.getReviewRuleId() == null) {
			return null;
		}

		ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleById(map.getReviewRuleId());
		if (approvalRule == null) {
			return null;
		}

		List<Long> ruleIdList = new ArrayList<Long>();
		ruleIdList.add(approvalRule.getId());
		List<ApprovalRuleFlowMap> ruleList = approvalRuleFlowMapProvider.listApprovalRuleFlowMapByRuleIds(ruleIdList);

		List<RuleFlowMap> ruleFlowMapList = ruleList.stream().map(v -> {
			
			RuleFlowMap result = ConvertHelper.convert(v, RuleFlowMap.class);

			ApprovalFlow approvalFlow = approvalFlowProvider.findApprovalFlowById(v.getFlowId());
			if (approvalFlow == null || approvalFlow.getStatus().byteValue() == CommonStatus.INACTIVE.getCode()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist approval flow");
			}
			List<ApprovalFlow> approvalFlowList  = new ArrayList<ApprovalFlow>();
			approvalFlowList.add(approvalFlow); 
			List<ApprovalFlowDTO> afDTO = processApprovalFlowLevelList(approvalFlowList,cmd.getOwnerType(), cmd.getOwnerId());
			result.setLevelList(afDTO.get(0).getLevelList());
			return result;
			})
				.collect(Collectors.toList());
		processApprovalTypeName(ruleFlowMapList);
		processFlowName(ruleFlowMapList);
		resp.setRuleFlowMapList(ruleFlowMapList);

		return resp;
	}

	/* (non-Javadoc)
	 * @see com.everhomes.approval.ApprovalService#updateTargetApprovalRule(com.everhomes.rest.approval.UpdateTargetApprovalRuleCommand)
	 */
	@Override
	public void updateTargetApprovalRule(UpdateTargetApprovalRuleCommand cmd) {

		if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid target type or  Id parameter in the command");
		}
		final Long userId = getUserId();
		final Integer namespaceId = UserContext.getCurrentNamespaceId();
		checkPrivilege(userId, namespaceId, cmd.getOwnerType(), cmd.getOwnerId());

		coordinationProvider.getNamedLock(
				CoordinationLocks.UPDATE_APPROVAL_TARGET_RULE.getCode() + cmd.getTargetId()).enter(
				() -> {

					PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
							cmd.getTargetType(), cmd.getTargetId());
					ApprovalRule approvalRule = ConvertHelper.convert(cmd, ApprovalRule.class);
					if (null != map && approvalRuleProvider.findApprovalRuleById(map.getReviewRuleId()) != null) {
						approvalRule = this.approvalRuleProvider.findApprovalRuleById(map.getReviewRuleId());
					}
					else{
						approvalRule.setName(cmd.getTargetId() + "approval rule");
						approvalRule.setNamespaceId(namespaceId);
						this.createApprovalRule(userId, approvalRule);
						if(null == map ){
							map = new PunchRuleOwnerMap();
							map.setReviewRuleId(approvalRule.getId());
							map.setOwnerId(cmd.getOwnerId());
							map.setOwnerType(cmd.getOwnerType());
							map.setTargetId(cmd.getTargetId());
							map.setTargetType(cmd.getTargetType());
							map.setCreatorUid(UserContext.current().getUser().getId());
							map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
							this.punchProvider.createPunchRuleOwnerMap(map);
						}
						else{
							map.setReviewRuleId(approvalRule.getId());
							this.punchProvider.updatePunchRuleOwnerMap(map);
						}
					}
					 
					// 删除之前的审批流程
					deleteApprovalRuleFlowMap(approvalRule.getId());
					// 创建新审批流程
					 for ( RuleFlowMap r : cmd.getRuleFlowMapList()){
						if (ApprovalType.fromCode(r.getApprovalType()) == null) {
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									"not exist approval type: approvalType=" + r.getApprovalType() + ", flowId=" + r.getFlowId());
						}
						
						ApprovalFlow approvalFlow = ConvertHelper.convert(cmd, ApprovalFlow.class); 
						approvalFlow.setName(cmd.getOwnerId()+" approval rule");
						approvalFlow.setStatus(CommonStatus.ACTIVE.getCode());
						approvalFlow.setNamespaceId(namespaceId);
						approvalFlow.setCreatorUid(userId);
						approvalFlow.setOperatorUid(userId);
						approvalFlow.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						approvalFlow.setUpdateTime(approvalFlow.getCreateTime());
						approvalFlowProvider.createApprovalFlow(approvalFlow);
						
						ApprovalRuleFlowMap approvalRuleFlowMap = new ApprovalRuleFlowMap();
						approvalRuleFlowMap.setRuleId(approvalRule.getId());
						approvalRuleFlowMap.setApprovalType(r.getApprovalType());
						approvalRuleFlowMap.setFlowId(approvalFlow.getId());
						approvalRuleFlowMap.setStatus(CommonStatus.ACTIVE.getCode());
						approvalRuleFlowMapProvider.createApprovalRuleFlowMap(approvalRuleFlowMap);
						if(null !=  r.getLevelList()){
							for(ApprovalFlowLevelDTO fl : r.getLevelList()){
								if (fl.getApprovalUserList() == null)
									continue;
								ApprovalFlowLevel ap = new ApprovalFlowLevel();
								ap.setFlowId(approvalFlow.getId());
								ap.setLevel(fl.getLevel());
								for(ApprovalUser a : fl.getApprovalUserList()){
									ap.setTargetType(a.getTargetType());
									ap.setTargetId(a.getTargetId());
									approvalFlowLevelProvider.createApprovalFlowLevel(ap);
								}
							}
						}
					}
					 return null;
				});
	}

	@Override
	public void deleteTargetApprovalRule(GetTargetApprovalRuleCommand cmd) {
		if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid target type or  Id parameter in the command");
		}
		if(cmd.getTargetType().equals(cmd.getOwnerType())&&cmd.getTargetId().equals(cmd.getOwnerId())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"不能删除公司规则");
		}
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getTargetType(), cmd.getTargetId());

		if (null != map) {
			// 删除规则
			dbProvider.execute(s -> {
				deleteReviewRules(cmd.getOwnerType(), cmd.getOwnerId(), map.getReviewRuleId());
				map.setReviewRuleId(null);
				map.setCreatorUid(UserContext.current().getUser().getId());
				map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				this.punchProvider.updatePunchRuleOwnerMap(map);
				return true;
			});
		}
	}

	private void deleteReviewRules(String ownerType, Long ownerId, Long reviewRuleId) {
		ApprovalRule approvalRule = this.approvalRuleProvider.findApprovalRuleById(reviewRuleId);
		if (null == approvalRule)
			return;
		dbProvider.execute(s -> {
			// 删除之前的审批流程
				deleteApprovalRuleFlowMap(approvalRule.getId());
				approvalRule.setStatus(CommonStatus.INACTIVE.getCode());
				updateApprovalRule(approvalRule, getUserId());
				return true;
			});
	}

	@Override
	public ListApprovalRequestBySceneResponse listMyApprovalsByScene(ListMyApprovalsBySceneCommand cmd) { 
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ListApprovalRequestCommand cmd2 = ConvertHelper.convert(cmd, ListApprovalRequestCommand.class);
		cmd2.setNamespaceId(ownerInfo.getNamespaceId());
		cmd2.setOwnerId(ownerInfo.getOwnerId());
		cmd2.setOwnerType(ownerInfo.getOwnerType()); 
		List<ApprovalRequest> resultList = new ArrayList<ApprovalRequest>();
		Long nextPageAnchor = listApprovalRequest(resultList,cmd2);
		if(null == resultList || resultList.size() == 0)
			return null; 

		return new ListApprovalRequestBySceneResponse(nextPageAnchor, resultList.stream().map(a -> {
			ApprovalRequestHandler handler = getApprovalRequestHandler(a.getApprovalType());
			BriefApprovalRequestDTO briefApprovalRequestDTO = handler.processBriefApprovalRequest(a);
			return briefApprovalRequestDTO;
		}).collect(Collectors.toList()));
		  
	}

	@Override
	public void approveApprovalRequest(ApproveApprovalRequesBySceneCommand cmd) { 
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		ApproveApprovalRequestCommand cmd2 = ConvertHelper.convert(ownerInfo, ApproveApprovalRequestCommand.class);
		List<Long> requestIdList = new ArrayList<Long>();
		requestIdList.add(getRequestIdFromToken(cmd.getRequestToken()));
		cmd2.setRequestIdList(requestIdList);
		this.approveApprovalRequest(cmd2);
		
	}

	@Override
	public void rejectApprovalRequest(RejectApprovalRequestBySceneCommand cmd) {  
		final Long userId = getUserId();
		ApprovalOwnerInfo ownerInfo = getOwnerInfoFromSceneToken(cmd.getSceneToken());
		checkPrivilege(userId, ownerInfo);
		RejectApprovalRequestCommand cmd2 = ConvertHelper.convert(ownerInfo, RejectApprovalRequestCommand.class);
		List<Long> requestIdList = new ArrayList<Long>();
		requestIdList.add(getRequestIdFromToken(cmd.getRequestToken()));
		cmd2.setRequestIdList(requestIdList);
		cmd2.setReason(cmd.getReason());
		this.rejectApprovalRequest(cmd2);
		
	}

	@Override
	public List<OrganizationMemberDTO> listTargetUsers(
			ListTargetUsersCommand cmd) {
		List<PunchRuleOwnerMap> maps = this.punchProvider.queryPunchRuleOwnerMaps(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getListType());
		if (null == maps )
			return null;
		
		return organizationService.listOrganizationMemberDTOs(cmd.getOwnerId(),
				maps.stream().map(r ->r.getTargetId()).collect(Collectors.toList())  );
	}

}
