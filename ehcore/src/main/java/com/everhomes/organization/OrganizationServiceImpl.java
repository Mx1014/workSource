// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.CancelLikeTopicCommand;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.GetTopicCommand;
import com.everhomes.forum.LikeTopicCommand;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.ListTopicCommand;
import com.everhomes.forum.ListTopicCommentCommand;
import com.everhomes.forum.NewCommentCommand;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostContentType;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.forum.PostPrivacy;
import com.everhomes.forum.QueryOrganizationTopicCommand;
import com.everhomes.group.Group;
import com.everhomes.launchpad.ItemKind;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.pm.AssginPmTopicCommand;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.CommunityPmMember;
import com.everhomes.organization.pm.CommunityPmTasks;
import com.everhomes.organization.pm.PmMemberGroup;
import com.everhomes.organization.pm.PmMemberStatus;
import com.everhomes.organization.pm.PmMemberTargetType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.organization.pm.PropertyServiceErrorCode;
import com.everhomes.organization.pm.SetPmTopicStatusCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.visibility.VisibleRegionType;

@Component
public class OrganizationServiceImpl implements OrganizationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
	private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

	@Autowired
	private PropertyMgrService propertyMgrService;

	@Autowired
	private ForumService forumService ;

	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private SmsProvider smsProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private UserService userService;

	@Autowired
	private FamilyProvider familyProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private UserActivityProvider userActivityProvider;

	private int getPageCount(int totalCount, int pageSize){
		int pageCount = totalCount/pageSize;

		if(totalCount % pageSize != 0){
			pageCount ++;
		}
		return pageCount;
	}

	@Override
	public void createOrganization(CreateOrganizationCommand cmd) {
		User user  = UserContext.current().getUser();

		//权限控制
		//先判断，后台管理员才能创建。状态直接设为正常
		OrganizationType organizationType = OrganizationType.fromCode(cmd.getOrganizationType());
		cmd.setOrganizationType(organizationType.getCode());
		Organization department  = ConvertHelper.convert(cmd, Organization.class);
		if(cmd.getParentId() == null)
		{
			department.setParentId(0L);
		}
		if(cmd.getLevel() == null){
			department.setLevel(0);
		}
		if(cmd.getAddressId() == null){
			department.setAddressId(0L);
		}
		department.setStatus(OrganizationStatus.ACTIVE.getCode());
		organizationProvider.createOrganization(department);


	}
	
	@Override
	public void applyOrganizationMember(ApplyOrganizationMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		
		Long communityId = user.getCommunityId();
		if(cmd.getCommunityId() == null){
			LOGGER.error("ApplyOrganizationMemberCommand communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"ApplyOrganizationMemberCommand communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		if(!communityId .equals( cmd.getCommunityId())){
			LOGGER.error("you not belong to the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"you not belong to the community.");
		}
		
		GetOrgDetailCommand command = new GetOrgDetailCommand();
		command.setCommunityId(cmd.getCommunityId());
		command.setOrganizationType(cmd.getOrganizationType());
		OrganizationDTO organization = getOrganizationByComunityidAndOrgType(command);
		
		OrganizationMember member = createOrganizationMember(user,organization.getId(),cmd.getContactDescription());
		organizationProvider.createOrganizationMember(member);
	}

	private OrganizationMember createOrganizationMember(User user, Long organizationId, String contactDescription) {
		UserIdentifier identifier = null; 
		List<UserIdentifier> userIndIdentifiers  = userProvider.listUserIdentifiersOfUser(user.getId());
		if(userIndIdentifiers != null && userIndIdentifiers.size() > 0){
			for (UserIdentifier userIdentifier : userIndIdentifiers) {
				if(userIdentifier.getIdentifierType() == IdentifierType.MOBILE.getCode()){
					identifier = userIdentifier;
					break;
				}
			}
		}
		OrganizationMember member = new OrganizationMember();
		member.setContactDescription(contactDescription);
		member.setContactName(user.getAccountName());
		if(identifier != null){
			member.setContactToken(identifier.getIdentifierToken());
			member.setContactType(identifier.getIdentifierType());
		}
		member.setMemberGroup(OrganizationGroup.MANAGER.getCode());
		member.setOrganizationId(organizationId);
		member.setStatus(OrganizationMemberStatus.CONFIRMING.getCode());
		member.setTargetId(user.getId());
		member.setTargetType(OrganizationMemberTargetType.USER.getCode());
		
		return member;
	}

	@Override
	public void createOrganizationMember(CreateOrganizationMemberCommand cmd) {
		User user  = UserContext.current().getUser();

		//权限控制
		//先判断，后台管理员才能创建。状态直接设为正常
		PmMemberTargetType targetType = PmMemberTargetType.fromCode(cmd.getTargetType());
		if(targetType == PmMemberTargetType.USER){
			//添加已注册用户为管理员。
			Long addUserId =  cmd.getTargetId();
			if(addUserId != null && addUserId != 0){
				List<OrganizationMember> list = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), addUserId, 1, 20);
				if(list == null || list.size() == 0)
				{
					UserIdentifier identifier = null; 
					List<UserIdentifier> userIndIdentifiers  = userProvider.listUserIdentifiersOfUser(addUserId);
					if(userIndIdentifiers != null && userIndIdentifiers.size() > 0){
						for (UserIdentifier userIdentifier : userIndIdentifiers) {
							if(userIdentifier.getIdentifierType() == IdentifierType.MOBILE.getCode()){
								identifier = userIdentifier;
								break;
							}
						}
					}
					User addUser = userProvider.findUserById(addUserId);
					PmMemberGroup memberGroup = PmMemberGroup.fromCode(cmd.getMemberGroup());
					cmd.setMemberGroup(memberGroup.getCode());
					OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
					departmentMember.setStatus(PmMemberStatus.ACTIVE.getCode());
					departmentMember.setContactName(addUser.getAccountName());
					departmentMember.setContactToken(identifier.getIdentifierToken());
					departmentMember.setContactType(IdentifierType.MOBILE.getCode());
					organizationProvider.createOrganizationMember(departmentMember);
				}
				else{
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"the user is  Organization member.");
				}
			}
		}
		else{
			//添加未注册用户为管理员。
			PmMemberGroup memberGroup = PmMemberGroup.fromCode(cmd.getMemberGroup());
			cmd.setMemberGroup(memberGroup.getCode());
			OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
			departmentMember.setStatus(PmMemberStatus.ACTIVE.getCode());
			departmentMember.setContactType(IdentifierType.MOBILE.getCode());
			departmentMember.setTargetId(0l);
			organizationProvider.createOrganizationMember(departmentMember);
		}
	}

	@Override
	public void createOrganizationCommunity(CreateOrganizationCommunityCommand cmd) {
		User user  = UserContext.current().getUser();

		//权限控制
		//先判断，后台管理员才能创建。状态直接设为正常
		Organization organization = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the organization.");
		}
		List<Long> communityIds = cmd.getCommunityIds();
		if(communityIds != null && communityIds.size() > 0){
			for (Long id : communityIds) {
				Community community = communityProvider.findCommunityById(id);
				if(community == null) {
					LOGGER.error("Unable to find the community.communityId=" + id);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"Unable to find the community.");
				}
				OrganizationCommunity departmentCommunity = new OrganizationCommunity();
				departmentCommunity.setCommunityId(id);
				departmentCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(departmentCommunity);
			}
		}
	}

	@Override
	public void deleteOrganizationCommunity(DeleteOrganizationCommunityCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getCommunityIds() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid organizationId or communityIds paramter.");
		}
		try{
			List<Long> communityIds = cmd.getCommunityIds();
			communityIds.forEach(id ->{
				OrganizationCommunity orgCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(cmd.getOrganizationId(), id);
				if(orgCommunity == null){
					LOGGER.error("OrganizationCommunity is not found,organizationId=" + cmd.getOrganizationId() + ",communityId=" + id);
					return;
				}
				organizationProvider.deleteOrganizationCommunity(orgCommunity);
			});
		}catch(Exception e){
			LOGGER.error("Fail to delete OrganizationCommunity,organizationId=" + cmd.getOrganizationId() + "," + e.getMessage());
		}
	}

	@Override
	public void createPropertyOrganization(CreatePropertyOrganizationCommand cmd) {
		if(cmd.getCommunityId() == null){
			LOGGER.error("organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organizationId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		Organization organization = new Organization();
		organization.setLevel(0);
		organization.setAddressId(0l);
		organization.setName(community.getName()+"物业");
		organization.setOrganizationType(OrganizationType.PM.getCode());
		organization.setParentId(0l);
		organization.setStatus(OrganizationStatus.ACTIVE.getCode());
		organizationProvider.createOrganization(organization);
		OrganizationCommunity departmentCommunity = new OrganizationCommunity();
		departmentCommunity.setCommunityId(community.getId());
		departmentCommunity.setOrganizationId(organization.getId());
		organizationProvider.createOrganizationCommunity(departmentCommunity);
	}

	@Override
	public ListOrganizationsCommandResponse listOrganizations(ListOrganizationsCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		ListOrganizationsCommandResponse response = new ListOrganizationsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int totalCount = organizationProvider.countOrganizations(cmd.getName());
		if(totalCount == 0) return response;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		List<Organization> result = organizationProvider.listOrganizations(cmd.getOrganizationType(),cmd.getName(), cmd.getPageOffset(), pageSize);
		response.setMembers( result.stream()
				.map(r->{ return ConvertHelper.convert(r,OrganizationDTO.class); })
				.collect(Collectors.toList()));
		response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return response;
	}

	@Override
	public ListOrganizationMemberCommandResponse getUserOwningOrganizations() {
		User user  = UserContext.current().getUser();
		//权限控制
		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
		List<OrganizationMember> reslut = organizationProvider.listOrganizationMembers (user.getId());
		if(reslut != null && reslut.size() > 0){
			List<OrganizationMemberDTO> members =  new ArrayList<OrganizationMemberDTO>();
			for (OrganizationMember organizationMember : reslut) {
				Organization organization = organizationProvider.findOrganizationById(organizationMember.getOrganizationId());
				if(OrganizationType.PM != OrganizationType.fromCode(organization.getOrganizationType())){
					OrganizationMemberDTO dto =ConvertHelper.convert(organizationMember,OrganizationMemberDTO.class);
					dto.setOrganizationName(organization.getName());
					members.add(dto);
				}
			}
			if(members != null && members.size() > 0){
				response.setMembers(members);
				UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), "currentOrganizationName");
				if(userProfile == null){
					Long organizationId  = members.get(0).getOrganizationId();
					Organization organization = organizationProvider.findOrganizationById(organizationId);
					userProfile = new UserProfile();
					userProfile.setItemKind(ItemKind.ENTITY.getCode());
					userProfile.setOwnerId(user.getId());
					userProfile.setItemName("currentOrganizationName");
					userProfile.setItemValue(String.valueOf(organization.getId()));
					userActivityProvider.addUserProfile(userProfile);
				}
			}
		}
		return response;
	}

	@Override
	public UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd) {
		UserTokenCommandResponse commandResponse = new UserTokenCommandResponse();
		User user = userService.findUserByIndentifier(cmd.getUserIdentifier());
		if(user != null)
		{
			List<String> phones = new ArrayList<String>();
			phones.add(cmd.getUserIdentifier());
			UserInfo userInfo = ConvertHelper.convert(user, UserInfo.class);
			userInfo.setPhones(phones);
			commandResponse.setUser(userInfo);
		}
		return commandResponse;
	}

	@Override
	public ListOrganizationMemberCommandResponse listOrganizationMembers(ListOrganizationMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int totalCount = organizationProvider.countOrganizationMembers(cmd.getOrganizationId(), null);
		if(totalCount == 0) return response;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		List<OrganizationMember> reslut = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), null, cmd.getPageOffset(), pageSize);
		response.setMembers(reslut.stream()
				.map(r->{ 
					OrganizationMemberDTO dto =  ConvertHelper.convert(r,OrganizationMemberDTO.class); 
					Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
					dto.setOrganizationName(organization.getName());
					return dto;
				})
				.collect(Collectors.toList()));
		response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return response;
	}

	@Override
	public PostDTO createTopic(NewTopicCommand cmd) {
		/*User user  = UserContext.current().getUser();
		if(cmd.getVisibleRegionId() == null){
			LOGGER.error("organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organizationId paramter can not be null or empty");
		}
		Organization organization = organizationProvider.findOrganizationById(cmd.getVisibleRegionId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getVisibleRegionId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the organization.");
		}
		//权限控制
		cmd.setCreatorTag(PostEntityTag.fromCode(organization.getOrganizationType()).getCode());
		if(organization.getOrganizationType() == OrganizationType.PM.getCode() || organization.getOrganizationType() == OrganizationType.GARC.getCode()){
			//如果是物业或者业委会发帖  VisibleRegionType = VisibleRegionType.COMMUNITY.getCode()  getVisibleRegionId = communityId 物业和业委会对应一个小区
			cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
			Community community = propertyMgrService.findPropertyOrganizationcommunity(organization.getId());
			if(community != null){
				cmd.setVisibleRegionId(community.getId());
			}
			else{
				LOGGER.error("Unable to find the organization community.organizationId=" + cmd.getVisibleRegionId());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Unable to find the organization community.");
			}
		}
		else{
			//如果是居委会或者公安发帖  VisibleRegionType = VisibleRegionType.REGION.getCode()  getVisibleRegionId = organizationId
			cmd.setVisibleRegionType(VisibleRegionType.REGION.getCode());
			cmd.setVisibleRegionId(organization.getId());
		}

		if(cmd.getPrivateFlag() == null|| "".equals(cmd.getCreatorTag())){
			cmd.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
		}
		if(cmd.getTargetTag() == null || "".equals(cmd.getCreatorTag())){
			cmd.setTargetTag(PostEntityTag.USER.getCode());
		}*/
		PostDTO post = forumService.createTopic(cmd);
		return post;
	}

	@Override
	public ListPostCommandResponse  queryTopicsByCategory(QueryOrganizationTopicCommand cmd) {
		return this.forumService.queryOrganizationTopics(cmd);
		//		ListPropPostCommandResponse response = new ListPropPostCommandResponse();
		//		User user  = UserContext.current().getUser();
		//    	Long communityId = cmd.getCommunityId();
		//    	if(communityId == null){
		//    		LOGGER.error("organizationId paramter can not be null or empty");
		//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
		//                    "organizationId paramter can not be null or empty");
		//    	}
		//    	Community community = communityProvider.findCommunityById(communityId);
		//    	if(community == null){
		//    		LOGGER.error("Unable to find the community.communityId=" + communityId);
		//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
		//                     "Unable to find the community.");
		//    	}
		//    	
		//    	//权限控制
		//    	int totalCount = propertyMgrProvider.countCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus());
		//    	if(totalCount == 0) return response;
		//    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		//    	int pageCount = getPageCount(totalCount, pageSize);
		//    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		//    	List<PropertyPostDTO> results = new ArrayList<PropertyPostDTO>();
		//    	List<CommunityPmTasks> tasks = propertyMgrProvider.listCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus(), cmd.getPageOffset(), pageSize);
		//    	if(tasks != null && tasks.size() > 0){
		//    		for (CommunityPmTasks task : tasks) {
		//				PostDTO post = forumService.getTopicById(task.getEntityId(), communityId, false);		
		//				PropertyPostDTO dto = ConvertHelper.convert(post, PropertyPostDTO.class);
		//				dto.setCommunityId(task.getOrganizationId());
		//				dto.setEntityType(task.getEntityType());
		//				dto.setEntityId(task.getEntityId());
		//				dto.setTargetType(task.getTargetType());
		//				dto.setTargetId(task.getTargetId());
		//				dto.setTaskType(task.getTaskType());
		//				dto.setTaskStatus(task.getTaskStatus());
		//				results.add(dto);
		//			}
		//    	}
		//    	response.setPosts(results);
		//    	response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		//    	return response;
	}

	@Override
	public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organizationId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		//权限控制
		return forumService.listTopics(cmd);
	}

	@Override
	public PostDTO getTopic(GetTopicCommand cmd) {

		//权限控制
		return forumService.getTopic(cmd);
	}

	@Override
	public PostDTO createComment(NewCommentCommand cmd) {

		//权限控制
		return forumService.createComment(cmd);
	}

	@Override
	public ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd) {

		//权限控制
		return forumService.listTopicComments(cmd);
	}

	@Override
	public void likeTopic(LikeTopicCommand cmd) {

		//权限控制
		forumService.likeTopic(cmd);
	}

	@Override
	public void cancelLikeTopic(CancelLikeTopicCommand cmd) {

		//权限控制
		forumService.cancelLikeTopic(cmd);
	}

	@Override
	public void assignPMTopics(AssginPmTopicCommand cmd) {
		User user = UserContext.current().getUser();
		long userId = user.getId();
		if(cmd.getTopicId() == null || cmd.getTopicId() == 0)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid topicIds paramter.");
		if(cmd.getCommunityId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid communityId paramter.");
		long communityId = cmd.getCommunityId();

		List<CommunityPmMember> pmMemberList = this.propertyMgrProvider.findPmMemberByCommunityAndTarget(communityId, 
				PmMemberTargetType.USER.getCode(), cmd.getUserId());
		if(pmMemberList == null || pmMemberList.isEmpty()){
			LOGGER.error("User is not the community pm member.userId=" + cmd.getUserId()+",communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid userId paramter,user is not the community pm member.");
		}

		long topicId = cmd.getTopicId();

		Post topic = this.forumProvider.findPostById(topicId);
		if(topic == null){
			LOGGER.error("Unable to find the topic.topicId=" + topicId+",communityId=" + communityId);
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TOPIC, 
					"Unable to find the topic.");
		}

		CommunityPmTasks task = this.propertyMgrProvider.findPmTaskByEntityId(communityId, topicId, 
				EntityType.TOPIC.getCode());
		if(task == null){
			LOGGER.error("Unable to find the topic task.topicId=" + topicId +",communityId=" + communityId);
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK, 
					"Unable to find the topic task.");
		}

		dbProvider.execute((status) -> {
			task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
			this.propertyMgrProvider.updatePmTask(task);

			//发送评论
			sendComment(topicId,topic.getForumId(),userId,topic.getCategoryId());
			//发送短信
			sendMSMToUser(topicId,cmd.getUserId(),topic.getCreatorUid(),topic.getCategoryId());

			return null;
		});


	}

	@Caching(evict={@CacheEvict(value="ForumPostById", key="#topicId")})
	private void sendComment(long topicId, long forumId, long userId, long category) {
		Post comment = new Post();
		comment.setParentPostId(topicId);
		comment.setForumId(forumId);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		comment.setCreatorUid(userId);
		comment.setContentType(PostContentType.TEXT.getCode());
		String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_COMMENT, "");
		if(StringUtils.isEmpty(template)){
			template = "该物业已在处理";
		}
		if (!StringUtils.isEmpty(template)) {
			comment.setContent(template);
			forumProvider.createPost(comment);
		}

	}

	/**
	 * 
	 * @param topicId
	 * @param userId 维修人员id
	 * @param owerId 业主id
	 * @param category 分类
	 */
	private void sendMSMToUser(long topicId, long userId, long owerId, long category) {
		//给维修人员发送短信是否显示业主地址
		String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
		List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
		userList.stream().filter((u) -> {
			if(u.getIdentifierType() != IdentifierType.MOBILE.getCode())
				return false;
			return true;
		});
		if(userList == null || userList.isEmpty()) return ;
		String cellPhone = userList.get(0).getIdentifierToken();
		if(StringUtils.isEmpty(template)){
			template = "该物业已在处理";
		}
		if (!StringUtils.isEmpty(template)) {
			this.smsProvider.sendSms(cellPhone, template);
		}
	}

	@Override
	public void setPMTopicStatus(SetPmTopicStatusCommand cmd){
		User user  = UserContext.current().getUser();
		long userId = user.getId();
		if(cmd.getCommunityId() == null){
			LOGGER.error("organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organizationId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		if(cmd.getTopicId() == null || cmd.getStatus() == null ){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid topicIds or status  paramter.");
		}
		if(cmd.getStatus() == OrganizationTaskStatus.PROCESSING.getCode()){
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK_STATUS, 
					"Invalid topic task status  paramter. please assign task");
		}
		long topicId = cmd.getTopicId();
		Post topic = this.forumProvider.findPostById(topicId);
		if(topic == null){ 
			LOGGER.error("Topic is not found.topicId=" + topicId + ",userId=" + userId);
		}
		CommunityPmTasks task = this.propertyMgrProvider.findPmTaskByEntityId(cmd.getCommunityId(), 
				topicId, EntityType.TOPIC.getCode());
		if(task == null){
			LOGGER.error("Pm task is not found.topicId=" + topicId + ",userId=" + userId);
		}
		task.setTaskStatus(cmd.getStatus());
		this.propertyMgrProvider.updatePmTask(task);
		if(cmd.getStatus() == OrganizationTaskStatus.PROCESSING.getCode()){
			//发送评论
			sendComment(topicId,topic.getForumId(),userId,topic.getCategoryId());
		}

	}

	@Override
	public ListOrganizationCommunityCommandResponse listOrganizationCommunities(ListOrganizationCommunityCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		ListOrganizationCommunityCommandResponse response = new ListOrganizationCommunityCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int totalCount = organizationProvider.countOrganizationCommunitys(cmd.getOrganizationId());
		if(totalCount == 0) return response;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId(),cmd.getPageOffset(),pageSize);
		List<OrganizationCommunityDTO> members = new ArrayList<OrganizationCommunityDTO>();
		if(result != null && result.size() > 0){
			for (OrganizationCommunity organizationCommunity : result) {
				OrganizationCommunityDTO dto = ConvertHelper.convert(organizationCommunity, OrganizationCommunityDTO.class);
				Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
				dto.setCommunityName(community.getName());
				members.add(dto);
			}
		}
		response.setCommunities(members);
		response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return response;
	}

	@Override
	public List<Long> getOrganizationCommunityIdById(Long organizationId) {
		List<Long> communityIdList = new ArrayList<Long>();
		if(organizationId == null) {
			LOGGER.error("Invalid organization id, organizationId=" + organizationId);
			return communityIdList;
		}

		List<OrganizationCommunity> communityList = this.organizationProvider.listOrganizationCommunities(organizationId);
		if(communityList != null && communityList.size() > 0) {
			for(OrganizationCommunity community : communityList) {
				communityIdList.add(community.getCommunityId());
			}
		}

		return communityIdList;
	}

	@Override
	public void createOrgContact(CreateOrganizationContactCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		if(cmd.getContactType() == null){
			cmd.setContactType(IdentifierType.MOBILE.getCode());
		}
		else{
			IdentifierType type = IdentifierType.fromCode(cmd.getContactType());
			cmd.setContactType(type.getCode());
		}
		CommunityPmContact contact = ConvertHelper.convert(cmd, CommunityPmContact.class);
		propertyMgrProvider.createPropContact(contact);
	}

	@Override
	public void updateOrgContact(UpdateOrganizationContactCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		if(cmd.getContactType() == null){
			cmd.setContactType(IdentifierType.MOBILE.getCode());
		}
		else{
			IdentifierType type = IdentifierType.fromCode(cmd.getContactType());
			cmd.setContactType(type.getCode());
		}
		CommunityPmContact contact = ConvertHelper.convert(cmd, CommunityPmContact.class);
		propertyMgrProvider.updatePropContact(contact);
	}

	@Override
	public void deleteOrgContact(DeleteOrganizationIdCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		CommunityPmContact contact = propertyMgrProvider.findPropContactById(cmd.getId());
		if(contact == null){ 
			LOGGER.error("contact is not found.contactId=" + cmd.getId() + ",userId=" + user.getId());
		}
		else{
			propertyMgrProvider.deletePropContact(cmd.getId());
		}
	}

	@Override
	public ListOrganizationContactCommandResponse listOrgContact(ListOrganizationContactCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();
		//	    	int totalCount = organizationProvider.countOrganizations(cmd.getName());
		//	    	if(totalCount == 0) return response;
		//	    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		//	    	int pageCount = getPageCount(totalCount, pageSize);

		List<CommunityPmContact> result = propertyMgrProvider.listCommunityPmContacts(cmd.getOrganizationId());
		response.setMembers( result.stream()
				.map(r->{ return ConvertHelper.convert(r,OrganizationContactDTO.class); })
				.collect(Collectors.toList()));
		return response;
	}

	@Override
	public void deleteOrganization(DeleteOrganizationIdCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		Organization organization = organizationProvider.findOrganizationById(cmd.getId());
		if(organization == null){ 
			LOGGER.error("organization is not found.organizationId=" + cmd.getId() + ",userId=" + user.getId());
		}
		else{
			organizationProvider.deleteOrganizationById(cmd.getId());
		}

	}

	@Override
	public void deleteOrganizationMember(DeleteOrganizationIdCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		OrganizationMember member = organizationProvider.findOrganizationMemberById(cmd.getId());
		if(member == null){ 
			LOGGER.error("organization member is not found.contactId=" + cmd.getId() + ",userId=" + user.getId());
		}
		else{
			organizationProvider.deleteOrganizationMemberById(cmd.getId());
		}

	}

	@Override
	public void sendOrgMessage(SendOrganizationMessageCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		if(cmd.getCommunityIds() == null){
			LOGGER.error("organization communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organization communityId paramter can not be null or empty");
		}
		List<Long> familyIds = new ArrayList<Long>();
		List<Long> communityIds = cmd.getCommunityIds();
		if(communityIds == null || communityIds.size() == 0){
			List<OrganizationCommunity> organizationCommunityList =  organizationProvider.listOrganizationCommunities(cmd.getOrganizationId());
			if(organizationCommunityList != null && organizationCommunityList.size() > 0){
				for (OrganizationCommunity organizationCommunity : organizationCommunityList) {
					communityIds.add(organizationCommunity.getCommunityId());
				}
			}
		}
		if(communityIds != null && communityIds.size() > 0){
			for (Long communityId : communityIds) {
				OrganizationCommunity organizationCommunity =  organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(cmd.getOrganizationId(), communityId);
				if(organizationCommunity != null){
					List<Group> familyList = familyProvider.listCommunityFamily(communityId);
					addFamilyIds(familyIds,familyList);
				}
				else{
					LOGGER.error("the community is not belong to the organization.communityId="+communityId+".organizationId="+cmd.getOrganizationId());
				}
			}
		}

		if(familyIds != null && familyIds.size() > 0){
			for (Long familyId : familyIds) {
				sendNoticeToFamilyById(familyId, cmd.getMessage());
			}
		}
	}

	private void addFamilyIds(List<Long> familyIds, List<Group> familyList) {
		if(familyList != null && familyList.size() > 0){
			for (Group group : familyList) {
				familyIds.add(group.getId());
			}
		}
	}

	public void sendNoticeToFamilyById(Long familyId,String message){
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_FAMILY);
		messageDto.setSenderUid(UserContext.current().getUser().getId());
		messageDto.setChannels(new MessageChannel("group", String.valueOf(familyId)));
		messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
		messageDto.setBody(message);

		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_FAMILY, "group", 
				String.valueOf(familyId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
	}

	@Override
	public void setCurrentOrganization(SetCurrentOrganizationCommand cmd) {
		User user  = UserContext.current().getUser();
		//权限控制
		String organizationId = String.valueOf(cmd.getOrganizationId());
		//		List<UserProfile> userProfiles = userActivityProvider.findProfileByUid(user.getId());
		//		String organizationId = String.valueOf(cmd.getOrganizationId());
		//		
		//		if(userProfiles != null && userProfiles.size() > 0){
		//			for (UserProfile profile : userProfiles) {
		//				if(organizationId.equals(profile.getItemValue())){
		//					userProfile = profile;
		//				}
		//			}
		//			UserProfile userProfile = userProfiles.get(0) ;
		//			userProfile.setItemValue(organizationId);
		//			
		//		}
		userActivityProvider.updateUserProfile(user.getId(), "currentOrganizationName", organizationId);

	}
	@Override
	public  OrganizationDTO getUserCurrentOrganization() {
		User user  = UserContext.current().getUser();
		//权限控制
		OrganizationDTO dto = null;
		List<UserProfile> userProfiles = userActivityProvider.findProfileByUid(user.getId());
		UserProfile userProfile = null;
		if(userProfiles != null && userProfiles.size() > 0){
			for (UserProfile profile : userProfiles) {
				if("currentOrganizationName".equals(profile.getItemName())){
					userProfile = profile;
				}
			}
			//			userProfile = userProfiles.get(0) ;

		}
		if(userProfile != null){
			Long organizationId = Long.parseLong((userProfile.getItemValue()));
			Organization organization = organizationProvider.findOrganizationById(organizationId);
			if(organization != null){
				dto = ConvertHelper.convert(organization, OrganizationDTO.class);
				if(dto.getOrganizationType().equals(OrganizationType.PM.getCode())){
					Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(dto.getId());
					OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(dto.getId());
					if(orgComm != null){
						Community community = this.communityProvider.findCommunityById(orgComm.getCommunityId());
						if(community != null){
							dto.setCommunityId(orgComm.getCommunityId());
							dto.setCommunityName(community.getName());
						}
					}
				}
			}
		}
		return dto;
	}

	public Map<String, Long> getOrganizationRegionMap(Long communityId) {
		Long userId = -1L;
		User user  = UserContext.current().getUser();
		if(user != null) {
			userId = user.getId();
		}

		Map<String, Long> map = new HashMap<String, Long>();

		List<Organization> list = this.organizationProvider.findOrganizationByCommunityId(communityId);
		for(Organization organization : list) {
			OrganizationType type = OrganizationType.fromCode(organization.getOrganizationType());
			if(type != null) {
				// 对于物业和业委，其region为小区ID；对于居委和公安，其region为机构ID
				switch(type) {
				case PM:
				case GARC:
					map.put(type.getCode(), communityId);
					break;
				case GANC:
				case GAPS:
					map.put(type.getCode(), organization.getId());
					break;
				default:
					LOGGER.error("Organization type not supported, userId=" + userId + ", communityId=" + communityId 
							+ ", organizationId=" + organization.getId() + ", organizationType=" + organization.getOrganizationType());
				}
			} else {
				LOGGER.error("Organization type is null, userId=" + userId + ", communityId=" + communityId 
						+ ", organizationId=" + organization.getId() + ", organizationType=" + organization.getOrganizationType());
			}
		}

		return map;
	}

	@Override
	public List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd) {
		User user = UserContext.current().getUser();
		if(user == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,"user is null");

		List<OrganizationSimpleDTO> orgs = new ArrayList<OrganizationSimpleDTO>();
		List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(user.getId());

		if(orgMembers != null && !orgMembers.isEmpty()){
			orgMembers.stream().map(orgMember -> {

				Organization org = this.organizationProvider.findOrganizationById(orgMember.getOrganizationId());
				if (org != null){
					
					if(cmd.getOrganiztionType() != null && !cmd.getOrganiztionType().equals("")){
						if(org.getOrganizationType().equals(cmd.getOrganiztionType())){
							OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
							//物业或业委增加小区Id和小区name信息
							if(org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())){
								this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
							}
							
							orgs.add(tempSimpleOrgDTO);
						}
					}
					else{
						OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
						//物业或业委增加小区Id和小区name信息
						if(org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())){
							this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
						}
						
						orgs.add(tempSimpleOrgDTO);
					}
					
				}
				return null;
			}).toArray();
		}
		
		return orgs;
	}

	private void addCommunityInfoToUserRelaltedOrgsByOrgId(OrganizationSimpleDTO org) {
		OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(org.getId());
		if(orgComm != null){
			Long communityId = orgComm.getCommunityId();
			Community community = this.communityProvider.findCommunityById(communityId);
			if(community != null){
				org.setCommunityId(communityId);
				org.setCommunityName(community.getName());
			}
		}
	}

	@Override
	public OrganizationDTO getOrganizationByComunityidAndOrgType(
			GetOrgDetailCommand cmd) {
		List<OrganizationCommunityDTO> orgCommunitys = this.organizationProvider.findOrganizationCommunityByCommunityId(cmd.getCommunityId());

		if(orgCommunitys != null && !orgCommunitys.isEmpty()){
			for(int i=0;i<orgCommunitys.size();i++){
				OrganizationDTO org = this.organizationProvider.findOrganizationByIdAndOrgType(orgCommunitys.get(i).getOrganizationId(),cmd.getOrganizationType());
				if(org != null){
					User user = UserContext.current().getUser();
					OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(),org.getId());

					if(member != null && member.getStatus() !=null)
						org.setMemberStatus(member.getStatus());
					else
						org.setMemberStatus(OrganizationMemberStatus.INACTIVE.getCode());

					return org;
				}
			}
		}

		return null;
	}

	@Override
	public int rejectOrganization(RejectOrganizationCommand cmd) {

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}

		List<OrganizationCommunity> orgComunitys = this.organizationProvider.listOrganizationByCommunityId(cmd.getCommunityId());

		if(orgComunitys != null && !orgComunitys.isEmpty()){

			Long organizationId = 0L;
			for(int i=0;i<orgComunitys.size();i++){
				Organization org = this.organizationProvider.findOrganizationById(orgComunitys.get(i).getOrganizationId());
				if(org != null && org.getOrganizationType().equals(cmd.getOrganizationType())){
					organizationId = org.getId();
					break;
				}
			}

			if(organizationId != 0){
				User user = UserContext.current().getUser();
				OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organizationId);
				if(member != null){
					member.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
					this.organizationProvider.updateOrganizationMember(member);
					return 1;
				}
				else{
					LOGGER.error("Unable to reject organization ，because userId = " + user.getId() +" is not in the organization : " + organizationId);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Unable to reject organization ，because user is not in the organization");
				}
			}
			else{
				LOGGER.error("Unable to reject organization ，because organization not found.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Unable to find the organization so can not reject");
			}
		}
		else{
			LOGGER.error("Unable to find the organization by communityId : " + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization by communityId : " + cmd.getCommunityId());
		}

	}

	@Override
	public int userExitOrganization(UserExitOrganizationCommand cmd) {
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}

		List<OrganizationCommunity> orgComunitys = this.organizationProvider.listOrganizationByCommunityId(cmd.getCommunityId());

		if(orgComunitys != null && !orgComunitys.isEmpty()){
			Long organizationId = 0L;
			for(int i=0;i<orgComunitys.size();i++){
				Organization org = this.organizationProvider.findOrganizationById(orgComunitys.get(i).getOrganizationId());
				if(org != null && org.getOrganizationType().equals(cmd.getOrganizationType())){
					organizationId = org.getId();
					break;
				}
			}

			//根据communityId找到该小区下的物业:organizationId
			if(organizationId != 0){
				User user = UserContext.current().getUser();
				OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organizationId);
				if(member != null){
					this.organizationProvider.deleteOrganizationMember(member);
					return 1;
				}
				else{
					LOGGER.error("Unable to exit organization ，because user is not in the organization. userId=" + user.getId() + ",organizationId=" + organizationId);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Unable to exit organization ，because user is not in the organization");
				}
			}
			else{
				LOGGER.error("Unable to exit the organization.Because organization not found.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Unable to exit the organization.Because organization not found.");
			}
		}
		else{
			LOGGER.error("Unable to find the organization by communityId : " + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization by communityId");
		}
	}
}
