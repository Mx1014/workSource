// @formatter:off
package com.everhomes.organization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.Address;
import com.everhomes.address.AddressAdminStatus;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.AppConstants;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.AttachmentDescriptor;
import com.everhomes.forum.CancelLikeTopicCommand;
import com.everhomes.forum.ForumConstants;
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
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.PmMemberGroup;
import com.everhomes.organization.pm.PmMemberStatus;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.organization.pm.PropertyServiceErrorCode;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.IdentifierClaimStatus;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserStatus;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.visibility.VisibleRegionType;

@Component
public class OrganizationServiceImpl implements OrganizationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
	private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";

	
	ExecutorService pool = Executors.newFixedThreadPool(3);
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

	@Autowired
	private AddressProvider addressProvider;

	@Autowired
	private RegionProvider regionProvider;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private CategoryProvider categoryProvider;

	private int getPageCount(int totalCount, int pageSize){
		int pageCount = totalCount/pageSize;

		if(totalCount % pageSize != 0){
			pageCount ++;
		}
		return pageCount;
	}

	@Override
	public void createOrganization(CreateOrganizationCommand cmd) {
		//先判断，后台管理员才能创建。状态直接设为正常
		OrganizationType organizationType = OrganizationType.fromCode(cmd.getOrganizationType());
		cmd.setOrganizationType(organizationType.getCode());
		Organization department  = ConvertHelper.convert(cmd, Organization.class);
		if(cmd.getParentId() == null)
		{
			department.setParentId(0L);
			department.setPath("/"+department.getName());
			department.setLevel(1);
		}
		else{
			Organization parOrg = this.checkOrganization(cmd.getParentId());
			department.setPath(parOrg.getPath()+"/"+department.getName());
			department.setLevel(parOrg.getLevel()+1);
		}
		if(cmd.getAddressId() == null){
			department.setAddressId(0L);
		}
		department.setStatus(OrganizationStatus.ACTIVE.getCode());
		organizationProvider.createOrganization(department);
	}



	private Organization checkOrganization(Long orgId) {
		Organization org = organizationProvider.findOrganizationById(orgId);
		if(org == null){
			LOGGER.error("Unable to find the organization.organizationId=" + orgId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		return org;
	}

	@Override
	public void applyOrganizationMember(ApplyOrganizationMemberCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		User user  = UserContext.current().getUser();
		this.checkCommunityIdIsEqual(user.getCommunityId().longValue(),cmd.getCommunityId().longValue());
		Organization organization = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

		this.checkUserInOrg(user.getId(), organization.getId());
		OrganizationMember member = this.createOrganizationMember(user,organization.getId(),cmd.getContactDescription());
		organizationProvider.createOrganizationMember(member);
	}

	private OrganizationMember checkUserNotInOrg(Long userId, Long orgId) {
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(member == null){
			LOGGER.error("User is not in the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"User is not in the organization.");
		}
		return member;
	}

	private void checkUserInOrg(Long userId, Long orgId) {
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(member != null){
			LOGGER.error("User is in the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"User is in the organization.");
		}
	}

	private Organization checkOrganizationByCommIdAndOrgType(Long communityId,String orgType) {
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, orgType);
		if(org == null) {
			LOGGER.error("organization can not find by communityId and orgType.communityId="+communityId+",orgType="+orgType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organization can not find by communityId and orgType.");
		}
		return org;
	}

	private void checkCommunityIdIsNull(Long communityId) {
		if(communityId == null){
			LOGGER.error("communityId paramter is empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"communityId paramter is empty");
		}

	}

	private void checkCommunityIdIsEqual(long longValue, long longValue2) {
		if(longValue != longValue2){
			LOGGER.error("communityId not equal.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"communityId not equal.");
		}
	}

	private OrganizationMember createOrganizationMember(User user, Long organizationId, String contactDescription) {
		OrganizationMember member = new OrganizationMember();

		member.setContactDescription(contactDescription);
		member.setContactName(user.getNickName());
		member.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		member.setOrganizationId(organizationId);
		member.setStatus(OrganizationMemberStatus.CONFIRMING.getCode());
		member.setTargetId(user.getId());
		member.setTargetType(OrganizationMemberTargetType.USER.getCode());

		UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
		if(identifier != null){
			member.setContactToken(identifier.getIdentifierToken());
			member.setContactType(identifier.getIdentifierType());
		}

		return member;
	}

	private UserIdentifier getUserMobileIdentifier(Long userId) {
		List<UserIdentifier> userIndIdentifiers  = userProvider.listUserIdentifiersOfUser(userId);
		if(userIndIdentifiers != null && userIndIdentifiers.size() > 0){
			for (UserIdentifier userIdentifier : userIndIdentifiers) {
				if(userIdentifier.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode()){
					return userIdentifier;
				}
			}
		}
		return null;
	}

	@Override
	public void createOrganizationMember(CreateOrganizationMemberCommand cmd) {
		//先判断，后台管理员才能创建。状态直接设为正常
		this.convertCreateOrganizationMemberCommand(cmd);
		OrganizationMemberTargetType targetType = OrganizationMemberTargetType.fromCode(cmd.getTargetType());
		if(targetType == OrganizationMemberTargetType.USER){//添加已注册用户为管理员。
			Long addUserId =  cmd.getTargetId();
			if(addUserId != null && addUserId.longValue() != 0){
				this.checkUserInOrg(addUserId, cmd.getOrganizationId());
				User addUser = userProvider.findUserById(addUserId);

				OrganizationMemberGroupType memberGroup = OrganizationMemberGroupType.fromCode(cmd.getMemberGroup());
				cmd.setMemberGroup(memberGroup.getCode());

				OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
				departmentMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
				departmentMember.setContactName(addUser.getNickName());

				UserIdentifier identifier = this.getUserMobileIdentifier(addUserId);
				if(identifier != null){
					departmentMember.setContactToken(identifier.getIdentifierToken());
				}
				departmentMember.setContactType(IdentifierType.MOBILE.getCode());
				organizationProvider.createOrganizationMember(departmentMember);
			}
		}
		else{//添加未注册用户为管理员。
			OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
			departmentMember.setStatus(PmMemberStatus.ACTIVE.getCode());
			departmentMember.setContactType(IdentifierType.MOBILE.getCode());
			departmentMember.setTargetId(0l);
			organizationProvider.createOrganizationMember(departmentMember);
		}
	}

	private void convertCreateOrganizationMemberCommand(CreateOrganizationMemberCommand cmd) {
		OrganizationMemberGroupType memberGroup = OrganizationMemberGroupType.fromCode(cmd.getMemberGroup());
		if(memberGroup == null)
			cmd.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		if(cmd.getTargetType() != null)
			cmd.setTargetType(cmd.getTargetType().toUpperCase());
	}

	@Override
	public void createOrganizationCommunity(CreateOrganizationCommunityCommand cmd) {
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		if(organization.getOrganizationType().equals(OrganizationType.PM.getCode()) || organization.getOrganizationType().equals(OrganizationType.GARC.getCode())){
			LOGGER.error("pm or garc could not create community mapping.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"pm or garc could not create community mapping.");
		}
		List<Long> communityIds = cmd.getCommunityIds();
		if(communityIds != null && communityIds.size() > 0){
			for (Long id : communityIds) {
				this.checkCommunity(id);
				OrganizationCommunity departmentCommunity = new OrganizationCommunity();
				departmentCommunity.setCommunityId(id);
				departmentCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(departmentCommunity);
			}
		}
	}

	@Override
	public void createOrganizationCommunityByAdmin(CreateOrganizationCommunityCommand cmd) {
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		if(organization.getOrganizationType().equals(OrganizationType.PM.getCode()) || organization.getOrganizationType().equals(OrganizationType.GARC.getCode())){
			OrganizationCommunity  orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(cmd.getOrganizationId());
			if(orgComm != null){
				LOGGER.error("pm or garc community mapping was created.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"pm or garc community mapping was created.");
			}
		}
		List<Long> communityIds = cmd.getCommunityIds();
		if(communityIds != null && communityIds.size() > 0){
			for (Long id : communityIds) {
				this.checkCommunity(id);
				OrganizationCommunity departmentCommunity = new OrganizationCommunity();
				departmentCommunity.setCommunityId(id);
				departmentCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(departmentCommunity);
			}
		}

	}

	@Override
	public void deleteOrganizationCommunity(DeleteOrganizationCommunityCommand cmd) {
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		this.checkCommunityIdsIsNull(cmd.getCommunityIds());
		this.dbProvider.execute(s -> {
			for(Long id : cmd.getCommunityIds()){
				OrganizationCommunity orgCommunity = this.checkOrgCommuByOrgIdAndCommId(cmd.getOrganizationId(), id);
				organizationProvider.deleteOrganizationCommunity(orgCommunity);
			}
			return s;
		});

	}

	private OrganizationCommunity checkOrgCommuByOrgIdAndCommId(Long organizationId, Long communityId) {
		OrganizationCommunity orgCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(organizationId, communityId);
		if(orgCommunity == null){
			LOGGER.error("organization community not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"organization community not found.");
		}
		return orgCommunity;
	}

	private void checkCommunityIdsIsNull(List<Long> communityIds) {
		if(communityIds == null || communityIds.isEmpty()){
			LOGGER.error("communityIds is empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"communityIds is empty.");
		}

	}

	@Override
	public void createPropertyOrganization(CreatePropertyOrganizationCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		Community community = this.checkCommunity(cmd.getCommunityId());

		this.dbProvider.execute(s -> {
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

			return s;
		});
	}

	@Override
	public ListOrganizationsCommandResponse listOrganizations(ListOrganizationsCommand cmd) {
		ListOrganizationsCommandResponse response = new ListOrganizationsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int totalCount = organizationProvider.countOrganizations(cmd.getOrganizationType(),cmd.getName());
		if(totalCount == 0) return response;

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<Organization> result = organizationProvider.listOrganizations(cmd.getOrganizationType(),cmd.getName(), cmd.getPageOffset(), pageSize);
		response.setMembers( result.stream()
				.map(r->{ return ConvertHelper.convert(r,OrganizationDTO.class); }).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return response;
	}

	@Override
	public ListOrganizationMemberCommandResponse getUserOwningOrganizations() {
		User user  = UserContext.current().getUser();
		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
		List<OrganizationMember> result = organizationProvider.listOrganizationMembers(user.getId());
		if(result != null && result.size() > 0){
			List<OrganizationMemberDTO> members =  new ArrayList<OrganizationMemberDTO>();
			for (OrganizationMember organizationMember : result) {
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
	public ListOrganizationMemberCommandResponse listOrgMembers(ListOrganizationMemberCommand cmd) {
		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();

		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset().longValue(), pageSize);

		List<OrganizationMember> result = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), null, offset, pageSize+1);
		if(result != null && !result.isEmpty()){
			if(result.size() == pageSize+1){
				result.remove(result.size()-1);
				response.setNextPageOffset(cmd.getPageOffset()+1);
			}

			response.setMembers(result.stream()
					.map(r->{ 
						OrganizationMemberDTO dto =  ConvertHelper.convert(r,OrganizationMemberDTO.class); 
						Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
						dto.setOrganizationName(organization.getName());
						return dto;
					}).collect(Collectors.toList()));
		}
		return response;
	}

	@Override
	public PostDTO createTopic(NewTopicCommand cmd) {
		if(cmd.getForumId() == null || 
				cmd.getVisibleRegionId() == null || 
				cmd.getVisibleRegionType() == null || 
				cmd.getContentCategory() == null ||
				cmd.getCreatorTag() == null || cmd.getCreatorTag().equals("") ||
				cmd.getTargetTag() == null || cmd.getTargetTag().equals("") || 
				cmd.getSubject() == null || cmd.getSubject().equals("")){
			LOGGER.error("ForumId or visibleRegionId or visibleRegionType or creatorTag or targetTag or subject is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ForumId or visibleRegionId or visibleRegionType or creatorTag or targetTag or subject is null or empty.");
		}
		this.convertNewTopicCommand(cmd);
		Organization organization = getOrganization(cmd);
		if(organization == null){
			LOGGER.error("Unable to find the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
					"Unable to find the organization.");
		}
		this.checkUserHaveRightToNewTopic(cmd,organization);
		/*if(cmd.getEmbeddedAppId() == null)
			cmd.setEmbeddedAppId(AppConstants.APPID_ORGTASK);*/
		PostDTO post = forumService.createTopic(cmd);
		return post;
	}

	private void convertNewTopicCommand(NewTopicCommand cmd) {
		//convert attachments contentType to upper case.
		List<AttachmentDescriptor> attachments = cmd.getAttachments();
		if(attachments != null && !attachments.isEmpty()){
			for(AttachmentDescriptor r : attachments){
				if(r.getContentType() != null && !r.getContentType().equals(""))
					r.setContentType(r.getContentType().toUpperCase());
			}
		}

	}

	private void checkUserHaveRightToNewTopic(NewTopicCommand cmd,Organization organization) {
		User user = UserContext.current().getUser();
		PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
		if(creatorTag == null){
			LOGGER.error("creatorTag format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag format is wrong.");
		}
		if(!creatorTag.getCode().equals(PostEntityTag.USER.getCode())){
			OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organization.getId());
			if(member == null){
				LOGGER.error("could not found member in the organization.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"could not found member in the organization.");
			}
		}
	}


	private Organization getOrganization(NewTopicCommand cmd) {
		PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
		PostEntityTag targetTag = PostEntityTag.fromCode(cmd.getTargetTag());

		Organization organization = null;
		if(creatorTag == null) {
			LOGGER.error("creatorTag could not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag could not be null.");
		}
		if(targetTag == null) {
			LOGGER.error("targetTag could not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"targetTag could not be null.");
		}
		//PostEntityTag.USER
		switch(targetTag){
		case USER:
			if(creatorTag.getCode().equals(PostEntityTag.PM.getCode()) || creatorTag.getCode().equals(PostEntityTag.GARC.getCode())){
				organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(cmd.getVisibleRegionId(), cmd.getCreatorTag());break;
			}
			else if(!creatorTag.getCode().equals(PostEntityTag.USER.getCode())){
				organization = this.organizationProvider.findOrganizationById(cmd.getVisibleRegionId());break;
			}
		case PM:
		case GARC:
			organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(cmd.getVisibleRegionId(), cmd.getTargetTag());break;
		case GANC:
		case GAPS:
		case GACW:
			organization = this.organizationProvider.findOrganizationById(cmd.getVisibleRegionId());break;
		default:
			LOGGER.error("creatorTag or targetTag format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag or targetTag format is wrong.");
		}

		return organization;
	}

	@Override
	public ListPostCommandResponse  queryTopicsByCategory(QueryOrganizationTopicCommand cmd) {
		return this.forumService.queryOrganizationTopics(cmd);
	}

	@Override
	public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
		Long communityId = cmd.getCommunityId();
		this.checkCommunityIdIsNull(communityId);
		this.checkCommunity(communityId);
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

	@Caching(evict={@CacheEvict(value="ForumPostById", key="#topicId")})
	private void sendComment(long topicId, long forumId, long orgId, long userId, long category) {

		Post comment = new Post();
		comment.setParentPostId(topicId);
		comment.setForumId(forumId);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		comment.setCreatorUid(userId);
		comment.setContentType(PostContentType.TEXT.getCode());
		String template = this.getOrganizationAssignTopicForCommentTemplate(orgId,userId);
		if(StringUtils.isEmpty(template)){
			template = "该请求已安排人员处理";
		}
		if(LOGGER.isDebugEnabled()){
			try {
				LOGGER.error("sendComment_template="+(new String(template.getBytes(),"UTF-8")));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e.getMessage());
			}
		}
		if (!StringUtils.isEmpty(template)) {
			comment.setContent(template);
			forumProvider.createPost(comment);
		}
	}

	private String getOrganizationAssignTopicForCommentTemplate(long orgId,long userId) {

		Map<String,Object> map = new HashMap<String, Object>();
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
		if(member != null){
			map.put("memberName", member.getContactName());
			map.put("memberContactToken", member.getContactToken());
		}

		User user = userProvider.findUserById(member.getTargetId());
		String locale = user.getLocale();
		if(locale == null) 
			locale = "zh_CN";

		String notifyText = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_ASSIGN_TOPIC_FOR_COMMENT, locale, map, "");

		return notifyText;
	}

	@Override
	public ListOrganizationCommunityCommandResponse listOrganizationCommunities(ListOrganizationCommunityCommand cmd) {
		ListOrganizationCommunityCommandResponse response = new ListOrganizationCommunityCommandResponse();

		cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
		int totalCount = organizationProvider.countOrganizationCommunitys(cmd.getOrganizationId());
		if(totalCount == 0) return response;

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId(),cmd.getPageOffset(),pageSize);
		List<OrganizationCommunityDTO> orgComms = new ArrayList<OrganizationCommunityDTO>();
		if(result != null && result.size() > 0){
			for (OrganizationCommunity organizationCommunity : result) {
				OrganizationCommunityDTO dto = ConvertHelper.convert(organizationCommunity, OrganizationCommunityDTO.class);
				Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
				dto.setCommunityName(community.getName());
				dto.setCityName(community.getCityName());
				dto.setAreaName(community.getAreaName());
				dto.setStatus(community.getStatus());
				orgComms.add(dto);
			}
		}
		response.setCommunities(orgComms);
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
		CommunityPmContact contact = propertyMgrProvider.findPropContactById(cmd.getId());
		if(contact == null){ 
			LOGGER.error("organization contact not found.");
		}
		else{
			propertyMgrProvider.deletePropContact(cmd.getId());
		}
	}

	@Override
	public ListOrganizationContactCommandResponse listOrgContact(ListOrganizationContactCommand cmd) {
		//权限控制
		ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();

		List<CommunityPmContact> result = propertyMgrProvider.listCommunityPmContacts(cmd.getOrganizationId());
		response.setMembers( result.stream()
				.map(r->{ return ConvertHelper.convert(r,OrganizationContactDTO.class); })
				.collect(Collectors.toList()));
		return response;
	}

	@Override
	public void deleteOrganization(DeleteOrganizationIdCommand cmd) {
		this.checkOrganizationIdIsNull(cmd.getId());
		this.checkOrganization(cmd.getId());
		organizationProvider.deleteOrganizationById(cmd.getId());
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
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		this.checkCommunityIdsIsNull(cmd.getCommunityIds());
		this.checkOrganization(cmd.getOrganizationId());

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
		messageDto.setChannels(new MessageChannel(MessageChannelType.GROUP.getCode(), String.valueOf(familyId)));
		messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
		messageDto.setBody(message);

		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_FAMILY, MessageChannelType.GROUP.getCode(), 
				String.valueOf(familyId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
	}

	@Override
	public void setCurrentOrganization(SetCurrentOrganizationCommand cmd) {
		User user  = UserContext.current().getUser();
		String organizationId = String.valueOf(cmd.getOrganizationId());
		userActivityProvider.updateUserProfile(user.getId(), "currentOrganizationName", organizationId);
	}

	@Override
	public  OrganizationDTO getUserCurrentOrganization() {
		User user  = UserContext.current().getUser();

		OrganizationDTO dto = new OrganizationDTO();
		UserProfile userProfile = this.getUserProfileByUidAndItemName(user.getId(),"currentOrganizationName");

		if(userProfile != null){
			Long organizationId = Long.parseLong((userProfile.getItemValue()));
			Organization organization = organizationProvider.findOrganizationById(organizationId);
			if(organization != null){
				dto = ConvertHelper.convert(organization, OrganizationDTO.class);
				if(dto.getOrganizationType().equals(OrganizationType.PM.getCode()) || dto.getOrganizationType().equals(OrganizationType.GARC.getCode())){
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

	private UserProfile getUserProfileByUidAndItemName(Long userId, String itemName) {
		List<UserProfile> userProfiles = userActivityProvider.findProfileByUid(userId);
		UserProfile userProfile = null;
		if(userProfiles != null && userProfiles.size() > 0){
			for (UserProfile profile : userProfiles) {
				if(itemName.equals(profile.getItemName())){
					userProfile = profile;
				}
			}
		}
		return userProfile;
	}

	public Map<String, Long> getOrganizationRegionMap(Long communityId) {
		Long userId = -1L;
		User user  = UserContext.current().getUser();
		if(user != null) {
			userId = user.getId();
		}

		Map<String, Long> map = new HashMap<String, Long>();

		List<Organization> list = this.organizationProvider.findOrganizationByCommunityId(communityId);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getOrganizationRegionMap-orgs="+StringHelper.toJsonString(list));
		
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
				case GACW:
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

		// 为了兼容没有机构的情况，需要为其提供默认值
		OrganizationType[] values = OrganizationType.values();
		for(OrganizationType value : values) {
			Long organizatioinId = map.get(value.getCode());
			if(organizatioinId == null) {
				if(value == OrganizationType.PM || value == OrganizationType.GARC) {
					map.put(value.getCode(), communityId);
				} else {
					map.put(value.getCode(), 0L);
				}
			}
		}

		return map;
	}

	@Override
	public List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd) {
		User user = UserContext.current().getUser();

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
	public OrganizationDTO getOrganizationByComunityidAndOrgType(GetOrgDetailCommand cmd) {
		List<OrganizationCommunityDTO> orgCommunitys = this.organizationProvider.findOrganizationCommunityByCommunityId(cmd.getCommunityId());
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getOrganizationByComunityidAndOrgType-orgCommunitys="+StringHelper.toJsonString(orgCommunitys));
		
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
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Organization organization = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

		User user = UserContext.current().getUser();
		OrganizationMember member = this.checkUserNotInOrg(user.getId(), organization.getId());
		member.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
		this.organizationProvider.updateOrganizationMember(member);
		return 1;
	}

	@Override
	public int userExitOrganization(UserExitOrganizationCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

		Long organizationId = org.getId();
		User user = UserContext.current().getUser();
		OrganizationMember member = this.checkUserNotInOrg(user.getId(), organizationId);
		this.organizationProvider.deleteOrganizationMember(member);
		return 1;
	}

	@Override
	public void approveOrganizationMember(OrganizationMemberCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getMemberId() == null){
			LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or memberId paramter can not be null or empty");
		}
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(), cmd.getOrganizationId());
		User user = UserContext.current().getUser();
		OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getOrganizationId());

		dbProvider.execute((status) -> {
			communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
			organizationProvider.updateOrganizationMember(communityPmMember);

			//给用户发审核结果通知
			String templateToUser = this.getOrganizationMemberApproveForApplicant(operOrgMember.getContactName(),organization.getName(),communityPmMember.getTargetId());
			if(templateToUser == null)
				templateToUser = "管理员已同意您加入组织";
			sendOrganizationNotificationToUser(communityPmMember.getTargetId(),templateToUser);
			//给其他管理员发通知
			List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
			if(orgMembers != null && !orgMembers.isEmpty()){
				for(OrganizationMember member : orgMembers){
					if(member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())){
						String templateToManager = this.getOrganizationMemberApproveForManager(operOrgMember.getContactName(),communityPmMember.getContactName(),organization.getName(),member.getTargetId());
						if(templateToManager == null)
							templateToManager = "管理员同意用户加入组织";
						sendOrganizationNotificationToUser(member.getTargetId(),templateToManager);
					}
				}
			}
			return status;
		});
	}

	private String getOrganizationMemberApproveForManager(String operName, String userName, String orgName, Long managerId) {
		User user = this.userProvider.findUserById(managerId);
		String locale = user.getLocale();
		if(locale == null)
			locale = "zh_CN";

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberName", operName);
		map.put("userName", userName);
		map.put("orgName", orgName);

		String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
				OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_APPROVE_FOR_MANAGER, locale, map, "");
		if(LOGGER.isDebugEnabled()){
			LOGGER.error("approveForManager_template="+template);
		}
		return template;
	}

	private String getOrganizationMemberApproveForApplicant(String operName,String orgName,long userId) {
		User user = this.userProvider.findUserById(userId);
		String locale = user.getLocale();
		if(locale == null)
			locale = "zh_CN";

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberName", operName);
		map.put("orgName", orgName);

		String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
				OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_APPROVE_FOR_APPLICANT, locale, map, "");
		if(LOGGER.isDebugEnabled()){
			LOGGER.error("approveForApplicant_template="+template);
		}
		return template;
	}

	private void sendOrganizationNotificationToUser(Long userId,String message) {
		if(message != null && message.length() != 0) {
			String channelType = MessageChannelType.USER.getCode();
			String channelToken = String.valueOf(userId);
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(new MessageChannel(channelType, channelToken));
			messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
			messageDto.setBody(message);
			messageDto.setMetaAppId(AppConstants.APPID_DEFAULT);
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType,
					channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
		}
	}

	@Override
	public void rejectOrganizationMember(OrganizationMemberCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getMemberId() == null){
			LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or memberId paramter can not be null or empty");
		}
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(), cmd.getOrganizationId());
		User user = UserContext.current().getUser();
		OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getOrganizationId());

		dbProvider.execute((status) -> {
			organizationProvider.deleteOrganizationMember(communityPmMember);

			//给用户发审核结果通知
			String templateToUser = this.getOrganizationMemberRejectForApplicant(operOrgMember.getContactName(),organization.getName(),communityPmMember.getTargetId());
			if(templateToUser == null)
				templateToUser = "管理员拒绝您加入组织";
			sendOrganizationNotificationToUser(communityPmMember.getTargetId(),templateToUser);
			//给其他管理员发通知
			List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
			if(orgMembers != null && !orgMembers.isEmpty()){
				for(OrganizationMember member : orgMembers){
					if(member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())){
						String templateToManager = this.getOrganizationMemberRejectForManager(operOrgMember.getContactName(),communityPmMember.getContactName(),organization.getName(),member.getTargetId());
						if(templateToManager == null)
							templateToManager = "管理员拒绝用户加入组织";
						sendOrganizationNotificationToUser(member.getTargetId(),templateToManager);
					}
				}
			}
			return status;
		});
	}

	private String getOrganizationMemberRejectForManager(String operName,String userName, String orgName, Long managerId) {
		User user = this.userProvider.findUserById(managerId);
		String locale = user.getLocale();
		if(locale == null)
			locale = "zh_CN";

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberName", operName);
		map.put("userName", userName);
		map.put("orgName", orgName);

		String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
				OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_REJECT_FOR_MANAGER, locale, map, "");
		if(LOGGER.isDebugEnabled()){
			LOGGER.error("rejectForManager_template="+template);
		}
		return template;
	}

	private String getOrganizationMemberRejectForApplicant(String operName,String orgName, Long userId) {
		User user = this.userProvider.findUserById(userId);
		String locale = user.getLocale();
		if(locale == null)
			locale = "zh_CN";

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberName", operName);
		map.put("orgName", orgName);

		String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
				OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_REJECT_FOR_APPLICANT, locale, map, "");
		if(LOGGER.isDebugEnabled()){
			LOGGER.error("rejectForApllicant_template="+template);
		}
		return template;
	}

	@Override
	public ListTopicsByTypeCommandResponse listTopicsByType(ListTopicsByTypeCommand cmd) {
		User user = UserContext.current().getUser();
		Long commuId = user.getCommunityId();
		Organization organization = this.checkOrganization(cmd.getOrganizationId());

		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);

		ListTopicsByTypeCommandResponse response = new ListTopicsByTypeCommandResponse();
		List<OrganizationTaskDTO2> list = new ArrayList<OrganizationTaskDTO2>();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

		List<OrganizationTask> orgTaskList = this.organizationProvider.listOrganizationTasksByOrgIdAndType(organization.getId(),cmd.getTaskType(),cmd.getTaskStatus(),pageSize+1,offset);
		if(orgTaskList != null && !orgTaskList.isEmpty()){
			if(orgTaskList.size() == pageSize+1){
				response.setNextPageOffset(cmd.getPageOffset()+1);
				orgTaskList.remove(orgTaskList.size()-1);
			}
			for(OrganizationTask task : orgTaskList){
				try{
					PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(),commuId,false);
					OrganizationTaskDTO2 taskDto = ConvertHelper.convert(dto, OrganizationTaskDTO2.class);
					this.convertTaskToDto(task,taskDto);
					list.add(taskDto);
				}
				catch(Exception e){
					LOGGER.error("could not found topic by task's applyEntityId.taskId="+task.getId()+",applyEntityId="+task.getApplyEntityId());
				}
			}
		}

		response.setRequests(list);
		return response;
	}

	private void convertTaskToDto(OrganizationTask task,
			OrganizationTaskDTO2 taskDto) {
		taskDto.setTaskId(task.getId());
		taskDto.setOrganizationId(task.getOrganizationId());
		taskDto.setOrganizationType(task.getOrganizationType());
		taskDto.setApplyEntityType(task.getApplyEntityType());
		taskDto.setApplyEntityId(task.getApplyEntityId());
		taskDto.setTargetType(task.getTargetType());
		taskDto.setTargetId(task.getTargetId());
		taskDto.setTaskType(task.getTaskType());
		taskDto.setDescription(task.getDescription());
		taskDto.setTaskStatus(task.getTaskStatus());
		taskDto.setOperatorUid(task.getOperatorUid());
		taskDto.setOperateTime(task.getOperateTime());
		taskDto.setTaskCreatorUid(task.getCreatorUid());
		taskDto.setTaskCreateTime(task.getCreateTime());
		taskDto.setUnprocessedTime(task.getUnprocessedTime());
		taskDto.setProcessingTime(task.getProcessingTime());
		taskDto.setProcessedTime(task.getProcessedTime());
	}

	private void checkOrganizationIdIsNull(Long organizationId) {
		if(organizationId == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
	}

	@Override
	public void assignOrgTopic(AssginOrgTopicCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getTopicId() == null || cmd.getUserId() == null){
			LOGGER.error("propterty organizationId or topicId or userId paramter can not be null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or topicId or userId paramter can not be null or empty.");
		}
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		Post topic = this.checkTopic(cmd.getTopicId());
		OrganizationMember desOrgMember = this.checkDesOrgMember(cmd.getUserId(),organization.getId());
		User user = UserContext.current().getUser();
		OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(),organization.getId());
		OrganizationTask task = this.checkOrgTask(organization.getId(), cmd.getTopicId());

		dbProvider.execute((status) -> {
			task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
			task.setOperateTime(new Timestamp(System.currentTimeMillis()));
			task.setOperatorUid(desOrgMember.getTargetId());;
			task.setProcessingTime(new Timestamp(System.currentTimeMillis()));
			this.organizationProvider.updateOrganizationTask(task);
			//发送评论
			sendComment(cmd.getTopicId(),topic.getForumId(),organization.getId(),desOrgMember.getTargetId(),topic.getCategoryId());
			//给分配的处理人员发任务分配短信
			sendSmToOrgMemberForAssignOrgTopic(organization,operOrgMember,desOrgMember,task);
			return status;
		});
	}

	private OrganizationTask checkOrgTask(Long orgId, Long topicId) {
		OrganizationTask task = this.organizationProvider.findOrgTaskByOrgIdAndEntityId(orgId,topicId);
		if(task == null){
			LOGGER.error("Unable to find the topic task.");
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK, 
					"Unable to find the topic task.");
		}
		return task;
	}

	private OrganizationMember checkOperOrgMember(Long userId, Long orgId) {
		OrganizationMember operOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(operOrgMember == null){
			LOGGER.error("Operator not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Operator not found.");
		}
		return operOrgMember;
	}

	private OrganizationMember checkDesOrgMember(Long userId, Long orgId) {
		OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(desOrgMember == null){
			LOGGER.error("User is not in the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"User is not in the organization.");
		}
		return desOrgMember;
	}

	private Post checkTopic(Long topicId) {
		Post topic = this.forumProvider.findPostById(topicId);
		if(topic == null){
			LOGGER.error("Unable to find the topic.");
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TOPIC, 
					"Unable to find the topic.");
		}
		return topic;
	}

	private void sendSmToOrgMemberForAssignOrgTopic(Organization organization,OrganizationMember operOrgMember, OrganizationMember desOrgMember,OrganizationTask task) {

		String locale = "zh_CN";
		String template = null;

		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getCreatorUid(), organization.getId());
		//组织代发求助帖
		if(member != null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgName", organization.getName());
			map.put("topicType",OrganizationTaskType.fromCode(task.getTaskType()).getName());
			map.put("phone", operOrgMember.getContactToken());
			template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
					OrganizationNotificationTemplateCode.ORGANIZATION_ASSIGN_TOPIC_BY_MANAGER_FOR_MEMBER, locale, map, "");
		}
		else{//用户自己发求助帖
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("topicType",OrganizationTaskType.fromCode(task.getTaskType()).getName());
			User user = this.userProvider.findUserById(task.getCreatorUid());
			if(user != null){
				UserIdentifier identify = this.getUserMobileIdentifier(user.getId());
				if(identify != null){
					map.put("phone", identify.getIdentifierToken());
				}
			}
			template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_ASSIGN_TOPIC_FOR_MEMBER, locale, map, "");
		}

		if(template == null || template.equals("")) template = "您有新的任务";

		this.smsProvider.sendSms(desOrgMember.getContactToken(), template);
	}

	@Override
	public void setOrgTopicStatus(SetOrgTopicStatusCommand cmd){
		if(cmd.getOrganizationId() == null || cmd.getStatus() == null || cmd.getTopicId() == null){
			LOGGER.error("propterty organizationId or status or topicId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or status or topicId paramter can not be null or empty");
		}
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		User user  = UserContext.current().getUser();
		this.checkOperOrgMember(user.getId(), organization.getId());
		Post topic = this.checkTopic(cmd.getTopicId());
		OrganizationTask task = this.checkOrgTask(organization.getId(),topic.getId());
		OrganizationTaskStatus taskSatus = this.checkTaskStatus(cmd.getStatus());

		dbProvider.execute((status) -> {
			task.setTaskStatus(taskSatus.getCode());
			task.setOperatorUid(user.getId());
			task.setOperateTime(new Timestamp(System.currentTimeMillis()));
			this.setOrgTaskTimeByStatus(task,taskSatus.getCode());
			this.organizationProvider.updateOrganizationTask(task);
			/*if(cmd.getStatus() == OrganizationTaskStatus.PROCESSING.getCode()){
				//发送评论
				sendComment(topic.getId(),topic.getForumId(),organization.getId(),user.getId(),topic.getCategoryId());
			}*/
			return status;
		});
	}

	private void setOrgTaskTimeByStatus(OrganizationTask task,byte code) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		if(OrganizationTaskStatus.UNPROCESSED.getCode() == code)
			task.setUnprocessedTime(currentTimestamp);
		if(OrganizationTaskStatus.PROCESSING.getCode() == code)
			task.setProcessingTime(currentTimestamp);
		if(OrganizationTaskStatus.PROCESSED.getCode() == code)
			task.setProcessedTime(currentTimestamp);
	}

	private OrganizationTaskStatus checkTaskStatus(Byte status) {
		OrganizationTaskStatus taskStatus = OrganizationTaskStatus.fromCode(status);
		if(taskStatus == null){
			LOGGER.error("task status is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"task status is wrong.");
		}
		return taskStatus;
	}

	@Override
	public void userJoinOrganization(UserJoinOrganizationCommand cmd) {
		User user = UserContext.current().getUser();
		if(cmd.getName() == null || cmd.getName().isEmpty() || cmd.getOrgType() == null || cmd.getOrgType().isEmpty()){
			LOGGER.error("propterty name or orgType paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty name or orgType paramter can not be null or empty");
		}

		this.dbProvider.execute(status -> {
			Organization org = null;
			Community community = null;
			Address address = null;
			Region city = null;
			Region area = null;

			org = this.organizationProvider.findOrganizationByName(cmd.getName());
			if(org == null){//组织不存在，先创建组织用户再加入组织
				//地址不为空，先查找地址，不存在则创建
				if(cmd.getAddress() != null && !cmd.getAddress().isEmpty()){
					address = addressProvider.findAddressByAddress(cmd.getAddress());
					if(address == null){
						city = this.checkCity(cmd.getCityId());
						area = this.checkArea(cmd.getAreaId());
						//创建地址
						address = new Address();
						address.setAddress(cmd.getAddress());
						address.setCityId(city.getId());
						address.setCityName(city.getName());
						address.setAreaId(area.getId());
						address.setAreaName(area.getName());
						address.setCreateTime(new Timestamp(System.currentTimeMillis()));
						address.setCreatorUid(user.getId());
						address.setStatus(AddressAdminStatus.CONFIRMING.getCode());
						this.addressProvider.createAddress(address);
					}
				}
				//创建组织
				org = new Organization();
				if(address != null)
					org.setAddressId(address.getId());
				else
					org.setAddressId(0L);
				org.setName(cmd.getName());
				org.setOrganizationType(cmd.getOrgType());
				if(cmd.getParentId() != null){
					Organization parOrg = this.checkOrganization(cmd.getParentId());
					org.setLevel(parOrg.getLevel()+1);
					org.setParentId(parOrg.getId());
					org.setPath(parOrg.getPath()+"/"+cmd.getName());
				}
				else{
					org.setLevel(0);
					org.setParentId(0L);
					org.setPath("/"+cmd.getName());
				}
				org.setStatus(OrganizationStatus.INACTIVE.getCode());
				org.setDescription(cmd.getDescription());
				this.organizationProvider.createOrganization(org);
				//创建组织小区关联
				if(cmd.getOrgType().equals(OrganizationType.PM.getCode()) || cmd.getOrgType().equals(OrganizationType.GARC.getCode())){
					community = this.checkCommunity(cmd.getCommunityId());
					OrganizationCommunity orgComm = new OrganizationCommunity();
					orgComm.setCommunityId(community.getId());
					orgComm.setOrganizationId(org.getId());
					this.organizationProvider.createOrganizationCommunity(orgComm);
				}
			}
			//创建组织成员
			if(cmd.isUserJoin()){
				OrganizationMember orgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), org.getId());
				if(orgMember != null){
					LOGGER.error("user have be organization member.");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"user have be organization member.");
				}
				orgMember = new OrganizationMember();
				orgMember.setContactDescription(cmd.getDescription());
				orgMember.setContactName(user.getNickName());
				UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
				if(identifier != null){
					orgMember.setContactToken(identifier.getIdentifierToken());
					orgMember.setContactType(identifier.getIdentifierType());
				}
				if(cmd.getMemberType() == null || cmd.getMemberType().trim().equals(""))
					orgMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
				else {
					OrganizationMemberGroupType type = OrganizationMemberGroupType.fromCode(cmd.getMemberType());
					if(type != null)	orgMember.setMemberGroup(type.getCode());
					else	orgMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
				}
				orgMember.setOrganizationId(org.getId());
				orgMember.setStatus(OrganizationMemberStatus.CONFIRMING.getCode());
				orgMember.setTargetId(user.getId());
				orgMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
				this.organizationProvider.createOrganizationMember(orgMember);
			}

			return status;
		});
	}

	private Region checkArea(Long areaId) {
		if(areaId == null){
			LOGGER.error("areaId could not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"areaId could not be null");
		}
		Region area = regionProvider.findRegionById(areaId);
		if(area == null){
			LOGGER.error("area not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"area not found.");
		}
		return area;
	}

	private Region checkCity(Long cityId) {
		if(cityId == null){
			LOGGER.error("cityId could not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cityId could not be null");
		}
		Region city = regionProvider.findRegionById(cityId);
		if(city == null){
			LOGGER.error("city not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city not found.");
		}
		return city;	
	}

	private void checkAddressIsNull(String addressName) {
		if(addressName == null){
			LOGGER.error("address could not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address could not be null");
		}
	}

	private Community checkCommunity(Long communityId) {
		Community community = this.communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("community not found");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"community not found");
		}
		return community;
	}

	@Override
	public void deleteOrgMember(OrganizationMemberCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getMemberId() == null){
			LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or memberId paramter can not be null or empty");
		}
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(),cmd.getOrganizationId());
		User user = UserContext.current().getUser();
		OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getOrganizationId());

		dbProvider.execute((status) -> {
			organizationProvider.deleteOrganizationMember(communityPmMember);
			//给用户发审核结果通知
			String templateToUser = this.getOrganizationMemberRejectForApplicant(operOrgMember.getContactName(),organization.getName(),communityPmMember.getTargetId());
			if(templateToUser == null)
				templateToUser = "管理员拒绝您加入组织";
			sendOrganizationNotificationToUser(communityPmMember.getTargetId(),templateToUser);
			//给其他管理员发通知
			List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
			if(orgMembers != null && !orgMembers.isEmpty()){
				for(OrganizationMember member : orgMembers){
					if(member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())){
						String templateToManager = this.getOrganizationMemberDeleteForManager(operOrgMember.getContactName(),communityPmMember.getContactName(),organization.getName(),member.getTargetId());
						if(templateToManager == null)
							templateToManager = "管理员拒绝用户加入组织";
						sendOrganizationNotificationToUser(member.getTargetId(),templateToManager);
					}
				}
			}
			return status;
		});

	}

	@Override
	public void updateTopicPrivacy(UpdateTopicPrivacyCommand cmd) {
		Long forumId = cmd.getForumId();
		Long topicId = cmd.getTopicId();
		PostPrivacy privacy = PostPrivacy.fromCode(cmd.getPrivacy());
		this.forumService.updatePostPrivacy(forumId, topicId, privacy);
	}

	private String getOrganizationMemberDeleteForManager(String operName,String userName, String orgName, Long managerId) {
		User user = this.userProvider.findUserById(managerId);	
		String locale = user.getLocale();
		if(locale == null)
			locale = "zh_CN";

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("memberName", operName);
		map.put("userName", userName);
		map.put("orgName", orgName);

		String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, 
				OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_DELETE_FOR_MANAGER, locale, map, "");
		return template;
	}

	@Override
	public void createOrganizationByAdmin(CreateOrganizationByAdminCommand cmd) {
		this.checkOrgNameIsNull(cmd.getName());
		OrganizationType organizationType = this.checkOrgType(cmd.getOrganizationType());
		//先判断，后台管理员才能创建。状态直接设为正常
		cmd.setOrganizationType(organizationType.getCode());
		Organization org  = ConvertHelper.convert(cmd, Organization.class);
		if(cmd.getParentId() == null)
		{
			org.setParentId(0L);
			org.setPath("/"+org.getName());
			org.setLevel(1);
		}
		else{
			Organization parOrg = this.checkOrganization(cmd.getParentId());
			org.setPath(parOrg.getPath()+"/"+org.getName());
			org.setLevel(parOrg.getLevel()+1);
		}
		if(cmd.getAddressId() == null){
			org.setAddressId(0L);
		}
		org.setStatus(OrganizationStatus.ACTIVE.getCode());
		this.dbProvider.execute(s -> {
			organizationProvider.createOrganization(org);
			if(cmd.getOrganizationType().equals(OrganizationType.PM.getCode()) || cmd.getOrganizationType().equals(OrganizationType.GARC.getCode())){
				this.checkCommunityIdIsNull(cmd.getCommunityId());
				this.checkCommunity(cmd.getCommunityId());
				OrganizationCommunity orgComm = new OrganizationCommunity();
				orgComm.setCommunityId(cmd.getCommunityId());
				orgComm.setOrganizationId(org.getId());
				organizationProvider.createOrganizationCommunity(orgComm);
			}
			return s;
		});
	}

	private OrganizationType checkOrgType(String orgType) {
		OrganizationType type = OrganizationType.fromCode(orgType);
		if(type == null){
			LOGGER.error("orgType is wrong.orgType=" + orgType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orgType is wrong.");
		}
		return type;

	}

	private void checkOrgNameIsNull(String orgName) {
		if(orgName == null || orgName.equals("")){
			LOGGER.error("orgName is empty.orgName=" + orgName);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orgName is empty.");
		}
	}

	@Override
	public void addOrgAddress(AddOrgAddressCommand cmd) {
		User user = UserContext.current().getUser();
		this.checkOrganizationIdIsNull(cmd.getOrgId());
		this.checkAddressIsNull(cmd.getAddress());
		Organization org = this.checkOrganization(cmd.getOrgId());
		this.dbProvider.execute(s -> {
			Address address = addressProvider.findAddressByAddress(cmd.getAddress());
			if(address == null){
				this.checkArea(cmd.getAreaId());
				this.checkCity(cmd.getCityId());
				Region city = this.checkCity(cmd.getCityId());
				Region area = this.checkArea(cmd.getAreaId());
				//创建地址
				address = new Address();
				address.setAddress(cmd.getAddress());
				address.setCityId(city.getId());
				address.setCityName(city.getName());
				address.setAreaId(area.getId());
				address.setAreaName(area.getName());
				address.setCreateTime(new Timestamp(System.currentTimeMillis()));
				address.setCreatorUid(user.getId());
				address.setStatus(AddressAdminStatus.ACTIVE.getCode());
				this.addressProvider.createAddress(address);
			}
			org.setAddressId(address.getId());
			this.organizationProvider.updateOrganization(org);
			return s;
		});
	}

	@Override
	public void importOrganization(MultipartFile[] files) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		this.checkFilesIsNull(files,user.getId());
		String rootPath = System.getProperty("user.dir");
		String filePath = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		LOGGER.error("importOrganization-filePath="+filePath);
		//将原文件暂存在服务器中
		try {
			this.storeFile(files[0],filePath);
		} catch (Exception e) {
			LOGGER.error("importOrganization-store file fail.message="+e.getMessage());
		}
		ImportOrganizationRunnable r = new ImportOrganizationRunnable(this, filePath, userId);
		pool.execute(r);
	}
	
	@Override
	public void executeImportOrganization(String filePath, Long userId) {
		long parseBeginTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-parseBeginTime="+parseBeginTime);
		ArrayList resultList = new ArrayList();
		try {
			File file = new File(filePath);
			if(!file.exists())
				LOGGER.error("executeImportOrganization-file is not exist.filePath="+filePath);
			InputStream in = new FileInputStream(file);
			resultList = PropMrgOwnerHandler.processorExcel(in);
		} catch (IOException e) {
			LOGGER.error("executeImportOrganization-parse file fail.message="+e.getMessage());
		} /*finally {
			File file = new File(filePath);
			if(file.exists())
				file.delete();
		}*/

		List<OrganizationDTO2> list = this.convertToOrganizations(resultList,userId);
		if(list == null || list.isEmpty()){
			LOGGER.error("organizations list is empty.userId="+userId);
			return;
		}

		for(OrganizationDTO2 r:list){
			this.checkCityNameIsNull(r.getCityName());
			this.checkAreaNameIsNull(r.getAreaName());
			Region city = this.checkCityName(r.getCityName());
			r.setCityId(city.getId());
			Region area = this.checkAreaName(r.getAreaName(),city.getId());
			r.setAreaId(area.getId());
			this.setTokenList(r,r.getTokens());
			this.setCommunityIdsByCommunityName(r,r.getCommunityNames(),r.getCityId(),r.getAreaId());
		}
		long parseEndTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-parseElapse="+(parseEndTime-parseBeginTime));

		List<OrganizationVo> orgVos = new ArrayList<OrganizationVo>();
		int orgCount = 0;
		int orgCommCount = 0;
		int orgContactCount = 0;
		int addressCount = 0;
		
		long listBeginTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-listBeginTime="+listBeginTime);
		for(OrganizationDTO2 r:list){
			OrganizationVo orgVo = new OrganizationVo();
			//地址
			Address address = addressProvider.findAddressByRegionAndAddress(r.getCityId(),r.getAreaId(),r.getAddressName());
			if(LOGGER.isDebugEnabled())
				LOGGER.info("address not found.cityId="+r.getCityId()+",areaId="+r.getAreaId());

			if(address == null){
				//创建地址
				Region city = this.regionProvider.findRegionById(r.getCityId());
				Region area = this.regionProvider.findRegionById(r.getAreaId());
				address = new Address();
				address.setAddress(r.getAddressName());
				address.setCityId(city.getId());
				address.setCityName(city.getName());
				address.setAreaId(area.getId());
				address.setAreaName(area.getName());
				address.setLongitude(r.getLongitude());
				address.setLatitude(r.getLatitude());
				address.setCreateTime(new Timestamp(System.currentTimeMillis()));
				address.setCreatorUid(userId);
				address.setStatus(AddressAdminStatus.ACTIVE.getCode());
				orgVo.setAddress(address);
				addressCount++;
				//this.addressProvider.createAddress(address);
			}
			else{
				r.setAddressId(address.getId());
			}
			//机构
			Organization org = this.convertOrgDTO2ToOrg(r);
			orgVo.setOrg(org);
			orgCount++;
			//this.organizationProvider.createOrganization(org);
			//机构小区
			for(Long comId : r.getCommunityIds()){
				OrganizationCommunity orgComm = new OrganizationCommunity();
				orgComm.setCommunityId(comId);
				if(orgVo.getOrgComms()==null || orgVo.getOrgComms().isEmpty())
					orgVo.setOrgComms(new ArrayList<OrganizationCommunity>());
				orgVo.getOrgComms().add(orgComm);
				orgCommCount++;
				//orgComm.setOrganizationId(org.getId());
				//this.organizationProvider.createOrganizationCommunity(orgComm);
			}
			//机构电话
			for(String token : r.getTokenList()){
				CommunityPmContact orgContact = new CommunityPmContact();
				orgContact.setContactName(null);
				orgContact.setContactToken(token);
				orgContact.setContactType(IdentifierType.MOBILE.getCode());
				orgContact.setCreateTime(new Timestamp(System.currentTimeMillis()));
				orgContact.setCreatorUid(userId);
				if(orgVo.getOrgContacts()==null)
					orgVo.setOrgContacts(new ArrayList<CommunityPmContact>());
				orgVo.getOrgContacts().add(orgContact);
				orgContactCount++;
				//				orgContact.setOrganizationId(org.getId());
				//				this.propertyMgrProvider.createPropContact(orgContact);
			}
			orgVos.add(orgVo);
		}
		long listEndTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-listElapse="+(listEndTime-listBeginTime));
		LOGGER.info("executeImportOrganization:orgCount="+orgCount+",addressCount="+addressCount+",orgCommCount="+orgCommCount+",orgContactCount="+orgContactCount);

		long beginTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-executeBeginTime="+beginTime);
		this.dbProvider.execute(s -> {
			for(OrganizationVo r:orgVos){
				if(r.getAddress()!=null){
					this.addressProvider.createAddress(r.getAddress());
					r.getOrg().setAddressId(r.getAddress().getId());
				}
				this.organizationProvider.createOrganization(r.getOrg());
				if(r.getOrgComms() != null && !r.getOrgComms().isEmpty()){
					for(OrganizationCommunity om : r.getOrgComms()){
						om.setOrganizationId(r.getOrg().getId());
						this.organizationProvider.createOrganizationCommunity(om);
					}
				}
				if(r.getOrgContacts()!=null && !r.getOrgContacts().isEmpty()){
					for(CommunityPmContact oc:r.getOrgContacts()){
						oc.setOrganizationId(r.getOrg().getId());
						this.propertyMgrProvider.createPropContact(oc);
					}
				}
			}
			return s;
		});
		
		long endTime = System.currentTimeMillis();
		LOGGER.info("executeImportOrganization-executeElapse="+(endTime-beginTime));
	}

	private void checkAreaNameIsNull(String areaName) {
		if(areaName == null || areaName.trim().equals("")){
			LOGGER.error("areaName is empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"areaName is empty.");
		}
	}

	private void setAreaId(OrganizationDTO2 r, String areaName, Long cityId) {
		Region area = this.checkAreaName(areaName,cityId);
		r.setAreaId(area.getId());
	}

	private Region checkAreaName(String areaName, Long cityId) {
		List<Region> areas = this.regionProvider.listRegionByName(cityId, RegionScope.AREA, null, null, areaName);
		if(areas == null || areas.isEmpty()){
			LOGGER.error("area is not found by areaName.areaName="+areaName+",cityId="+cityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"area is not found by areaName.");
		}
		else if(areas.size() != 1){
			LOGGER.error("area is not unique found by areaName.areaName="+areaName+",cityId="+cityId);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city is not unique found by cityName.");*/
		}
		return areas.get(0);
	}

	private Organization convertOrgDTO2ToOrg(OrganizationDTO2 r) {
		Organization org = new Organization();
		org.setAddressId(r.getAddressId());
		org.setLevel(1);
		org.setName(r.getOrgName());
		org.setOrganizationType(r.getOrgType());
		org.setParentId(0L);
		org.setPath("/"+r.getOrgName());
		org.setStatus(OrganizationStatus.ACTIVE.getCode());
		return org;
	}

	private void setCommunityIdsByCommunityName(OrganizationDTO2 r,String communityNames,Long cityId, Long areaId) {
		String [] commNames = communityNames.split(",");
		for(String communityName : commNames){
			Community comm = this.findCommunityByCommName(communityName, cityId,areaId);
			if(r.getCommunityIds() == null)
				r.setCommunityIds(new ArrayList<Long>());
			r.getCommunityIds().add(comm.getId());
		}
	}

	private Community findCommunityByCommName(String communityName, Long cityId, Long areaId) {
		List<Community> comms = this.communityProvider.findCommunitiesByNameCityIdAreaId(communityName, cityId,null);
		if(comms == null || comms.isEmpty()){
			LOGGER.error("community is not found by communityName.communityName="+communityName+",cityId="+cityId+",areaId="+null);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"community is not found by communityName.");
		}
		else if(comms.size() != 1){
			LOGGER.error("community is not unique found by communityName.communityName="+communityName+",cityId="+cityId+",areaId="+null);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"community is not unique found by communityName.");*/
		}
		return comms.get(0);
	}

	private void setAddressId(OrganizationDTO2 r, String addressName) {
		Address address = this.checkAddressByAddressName(addressName);
		r.setAddressId(address.getId());
	}

	private Address checkAddressByAddressName(String addressName) {
		Address address = addressProvider.findAddressByAddress(addressName);
		if(address == null){
			LOGGER.error("address is not found by addressName.addressName="+addressName);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address is not found by addressName.");
		}
		return address;
	}

	private void setTokenList(OrganizationDTO2 r, String tokens) {
		String [] strList = tokens.split(",");
		for(String token : strList){
			if(r.getTokenList() == null)
				r.setTokenList(new ArrayList<String>());
			r.getTokenList().add(token);
		}
	}

	private void setCityId(OrganizationDTO2 r,String cityName) {
		Region city = this.checkCityName(cityName);
		r.setCityId(city.getId());
	}

	private Region checkCityName(String cityName) {
		List<Region> citys = this.regionProvider.listRegionByName(null, RegionScope.CITY, null, null, cityName);
		if(citys == null || citys.isEmpty()){
			LOGGER.error("city is not found by cityName.cityName="+cityName);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city is not found by cityName.");
		}
		else if(citys.size() != 1){
			LOGGER.error("city is not unique found by cityName.cityName="+cityName);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city is not unique found by cityName.");*/
		}
		return citys.get(0);
	}

	private void checkCityNameIsNull(String cityName) {
		if(cityName == null || cityName.trim().equals("")){
			LOGGER.error("cityName is empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cityName is empty.");
		}
	}

	private List<OrganizationDTO2> convertToOrganizations(ArrayList list,Long userId) {
		if(list == null || list.isEmpty()){
			LOGGER.error("resultList is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"resultList is empty.");
		}
		List<OrganizationDTO2> result = new ArrayList<OrganizationDTO2>();
		for(int rowIndex=1;rowIndex<list.size();rowIndex++){
			RowResult r = (RowResult)list.get(rowIndex);
			if(r.getA() == null || r.getA().trim().equals("")){
				LOGGER.error("have row is empty.rowIndex="+(rowIndex+1));
				break;
			}
			OrganizationDTO2 dto = new OrganizationDTO2();
			dto.setCityName(this.getCityName(r.getA()));
			dto.setAreaName(this.setAreaName(r.getB()));
			dto.setOrgName(this.getOrgName(r.getC()));
			dto.setOrgType(this.getOrgType(r.getD()));
			dto.setTokens(this.getTokens(r.getE()));
			dto.setAddressName(this.getAddressName(r.getF()));
			dto.setLongitude(this.getLongitude(r.getG()));
			dto.setLatitude(this.getLatitude(r.getH()));
			dto.setCommunityNames(this.getCommunityNames(r.getI()));
			result.add(dto);
		}
		return result;
	}

	private String setAreaName(String areaName) {
		return areaName==null?null:areaName.trim();
	}

	private String getCommunityNames(String comName) {
		return comName==null?null:comName.trim();
	}

	private Double getLatitude(String latitude) {
		return Double.valueOf(latitude ==null?null:latitude.trim());
	}

	private Double getLongitude(String longitude) {
		return Double.valueOf(longitude == null?null:longitude.trim());
	}

	private String getAddressName(String addressName) {
		return addressName == null?null:addressName.trim();
	}

	private String getTokens(String tokens) {
		return tokens == null?null:tokens.trim();
	}

	private String getOrgType(String orgType) {
		return orgType == null?null:orgType.trim();
	}

	private String getOrgName(String orgName) {
		return orgName == null?null:orgName.trim();
	}

	private String getCityName(String cityName) {

		return cityName == null?null:cityName.trim();
	}

	private void checkFilesIsNull(MultipartFile[] files,Long userId) {
		if(files == null){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"files is null");
		}
	}
	
	private void storeFile(MultipartFile file, String filePath1)throws Exception{
		OutputStream out = new FileOutputStream(new File(filePath1));
		InputStream in = file.getInputStream();
		byte [] buffer = new byte [1024];
		while(in.read(buffer) != -1){ 
			out.write(buffer);
		}
		out.close();
		in.close();
	}

	@Override
	public void importOrgPost(MultipartFile[] files) {
		User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		this.checkFilesIsNull(files,manaUser.getId());
		
		String rootPath = System.getProperty("user.dir");
		String filePath = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		LOGGER.error("importOrgPost-filePath="+filePath);
		//将原文件暂存在服务器中
		try {
			this.storeFile(files[0],filePath);
		} catch (Exception e) {
			LOGGER.error("importOrgPost-store file fail.message="+e.getMessage());
		}
		ImportOrgPostRunnable r = new ImportOrgPostRunnable(this, filePath, userId);
		pool.execute(r);
	}
	
	@Override
	public void executeImportOrgPost(String filePath, Long userId) {
		String password = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
		long parseBeginTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-parseBeginTime="+parseBeginTime);
		
		ArrayList resultList = new ArrayList();
		try {
			File file = new File(filePath);
			if(!file.exists())
				LOGGER.error("executeImportOrgPost-file is not exist.filePath="+filePath);
			InputStream in = new FileInputStream(file);
			resultList = PropMrgOwnerHandler.processorExcel(in);
		} catch (IOException e) {
			LOGGER.error("executeImportOrgPost-parse file fail.message="+e.getMessage());
		} finally {
			File file = new File(filePath);
			if(file.exists())
				file.delete();
		}

		List<OrganizationPostDTO> list = this.convertToOrgPostDto(resultList,userId);

		if(list == null || list.isEmpty()){
			LOGGER.error("orgPost list is empty.userId="+userId);
			return;
		}
		long parseEndTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-parseElapse="+(parseEndTime-parseBeginTime));
		
		List<OrganizationPostVo> orgPostVos = new ArrayList<OrganizationPostVo>();
		int postCount = 0;
		int taskCount = 0;
		int userCount = 0;
		int userIdenCount = 0;

		long listBeginTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-listBeginTime="+listBeginTime);
		for(OrganizationPostDTO r:list){
			OrganizationPostVo orgPostVo = new OrganizationPostVo();

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
			//createTime
			Date createDate = null;
			if(r.getCreateTime() != null){
				try {
					createDate = format.parse(r.getCreateTime());
				}
				catch (ParseException e) {
					LOGGER.error("post create date not format to MM/dd/yy.createDateStr="+r.getCreateTime());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"post create date not format to MM/dd/yy.");
				}
			}
			else{
				createDate = new Date();
			}
			
			createDate = this.getRadomTime(createDate);
			Timestamp currentTime = new Timestamp(createDate.getTime());

			//city
			this.checkCityNameIsNull(r.getCityName());
			Region city = this.checkCityName(r.getCityName());
			r.setCityId(city.getId());
			//org
			List<Organization> orgs = this.checkOrganizationByName(r.getOrgName(),r.getOrgType());
			Organization org = this.checkOrgAddressCity(orgs,r.getCityId());
			//token
			User tokenUser = null;
			if(r.getToken() != null && !r.getToken().trim().equals("")){
				tokenUser = this.userService.findUserByIndentifier(r.getToken());
				if(tokenUser == null){
					User user = new User();
					user.setAccountName(r.getToken());
					user.setNickName(r.getUserName());
					user.setStatus(UserStatus.ACTIVE.getCode());
					user.setPoints(0);
					user.setLevel((byte)1);
					user.setGender((byte)1);
					//password
					String salt = EncryptionUtils.createRandomSalt();
					user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", password,salt)));
					user.setSalt(salt);
					userCount++;
					orgPostVo.setUser(user);
					//this.userProvider.createUser(user);
					UserIdentifier userIden = new UserIdentifier();
					userIden.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
					userIden.setIdentifierToken(r.getToken());
					userIden.setIdentifierType(IdentifierType.MOBILE.getCode());
					/*userIden.setOwnerUid(user.getId());*/
					userIdenCount++;
					orgPostVo.setUserIden(userIden);
					//this.userProvider.createIdentifier(userIden);
				}
				else{
					orgPostVo.setUser(null);
					orgPostVo.setUserIden(null);
				}
			}

			//post
			Post post = new Post();
			post.setAppId(AppConstants.APPID_FORUM);
			post.setForumId(ForumConstants.SYSTEM_FORUM);
			post.setParentPostId(0L);
			post.setSubject(r.getSubject());
			post.setContent(r.getContent());
			post.setContentType(PostContentType.TEXT.getCode());
			post.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
			post.setCreateTime(currentTime);
			post.setUpdateTime(currentTime);
			if(r.getToken() == null || r.getToken().trim().equals(""))//token is null then use the loginUser id
				post.setCreatorUid(userId);
			else if(tokenUser != null && tokenUser.getId() != null)//token not null and use is found by token
				post.setCreatorUid(tokenUser.getId());

			if(org.getOrganizationType().equals(OrganizationType.PM.getCode()) || org.getOrganizationType().equals(OrganizationType.GARC.getCode())){
				OrganizationCommunity orgComm = this.checkOrgCommByOrgId(org.getId());
				post.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
				post.setVisibleRegionId(orgComm.getCommunityId());
			}
			else{
				post.setVisibleRegionType(VisibleRegionType.REGION.getCode());
				post.setVisibleRegionId(org.getId());
			}

			Category category = this.checkCategory(r.getPostType());
			post.setCategoryId(category.getId());
			post.setCategoryPath(category.getPath());

			OrganizationTask task = null;
			if(r.getPostType().longValue() == CategoryConstants.CATEGORY_ID_NOTICE){//公告
				post.setCreatorTag(org.getOrganizationType());
				post.setTargetTag(PostEntityTag.USER.getCode());
				orgPostVo.setTask(null);
			}
			else{
				post.setCreatorTag(PostEntityTag.USER.getCode());
				post.setTargetTag(org.getOrganizationType());
				task = new OrganizationTask();
				task.setOrganizationId(org.getId());
				task.setOrganizationType(org.getOrganizationType());
				task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
				task.setApplyEntityId(0L); // 还没有帖子ID
				task.setTargetType(org.getOrganizationType());
				task.setTargetId(org.getId());
				if(r.getToken() == null || r.getToken().trim().equals(""))//token is null then use the loginUser id
					task.setCreatorUid(userId);
				if(tokenUser != null && tokenUser.getId() != null)//token not null and use is found by token
					task.setCreatorUid(tokenUser.getId());

				task.setCreateTime(currentTime);
				task.setUnprocessedTime(currentTime);

				OrganizationTaskType taskType = this.getOrganizationTaskType(r.getPostType());
				if(taskType != null) {
					task.setTaskType(taskType.getCode());
				}

				task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
				task.setOperatorUid(0L);
				taskCount++;
				orgPostVo.setTask(task);
				//this.organizationProvider.createOrganizationTask(task);
				/*post.setEmbeddedAppId(27L);
				post.setEmbeddedId(task.getId());*/
			}

			postCount++;
			orgPostVo.setPost(post);
			orgPostVos.add(orgPostVo);
			//this.forumProvider.createPost(post);
			/*if(task != null){
				task.setApplyEntityId(post.getId());
				this.organizationProvider.updateOrganizationTask(task);
			}*/
		}
		long listEndTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-listElapse="+(listEndTime-listBeginTime));
		LOGGER.info(parseBeginTime+"executeImportOrgPost-count:userCount="+userCount+";userIdenCount="+userIdenCount+";taskCount="+taskCount+";postCount="+postCount);

		if(orgPostVos == null ||orgPostVos.isEmpty())
			return;

		long beginTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-executeBeginTime="+beginTime);	
		this.dbProvider.execute(s -> {
			for(OrganizationPostVo r:orgPostVos){
				if(r.getUser() != null&&r.getUserIden() != null){
					this.userProvider.createUser(r.getUser());
					r.getUserIden().setOwnerUid(r.getUser().getId());//use the create user id
					this.userProvider.createIdentifier(r.getUserIden());
				}
				if(r.getTask() != null && r.getPost() != null){
					if(r.getUser() !=null){//use the create user id
						r.getTask().setCreatorUid(r.getUser().getId());
						r.getPost().setCreatorUid(r.getUser().getId());
					}
					this.organizationProvider.createOrganizationTask(r.getTask());
					r.getPost().setEmbeddedAppId(27L);
					r.getPost().setEmbeddedId(r.getTask().getId());
					this.forumProvider.createPost(r.getPost());
					r.getTask().setApplyEntityId(r.getPost().getId());
					this.organizationProvider.updateOrganizationTask(r.getTask());
				}
				else{
					if(r.getUser() !=null)//use the create user id
						r.getPost().setCreatorUid(r.getUser().getId());
					this.forumProvider.createPost(r.getPost());
				}
			}
			return s;
		});
		long endTime = System.currentTimeMillis();
		LOGGER.info(parseBeginTime+"executeImportOrgPost-executeElapse="+(endTime-beginTime));
	}

	private Date getRadomTime(Date createDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(createDate);
		int hour = (int)(Math.random()*24);
		if(hour < 8 )
			hour = 10;
		else if(hour > 17)
			hour = 15;
		
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getRadomTime-hour="+hour);
			
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE,(int)(Math.random()*60));
		cal.set(Calendar.SECOND,(int)(Math.random()*60));
		
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getRadomTime-time="+cal.getTime());
		return cal.getTime();
	}

	private Category checkCategory(Long postType) {
		Category category = this.categoryProvider.findCategoryById(postType);
		if(category == null){
			LOGGER.error("category not found.categoryId="+postType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"category not found.");
		}
		return category;
	}

	private OrganizationCommunity checkOrgCommByOrgId(Long orgId) {
		List<OrganizationCommunity> orgComms = this.organizationProvider.listOrganizationCommunities(orgId);
		if(orgComms == null || orgComms.isEmpty()){
			LOGGER.error("organization community not found.orgId="+orgId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organization community not found.");
		}
		if(orgComms.size() != 1)
			LOGGER.error("organization community not unique found.orgId="+orgId);

		if(LOGGER.isDebugEnabled())
			LOGGER.error("checkOrgCommByOrgId-orgComms="+StringHelper.toJsonString(orgComms));

		return orgComms.get(0);
	}

	private OrganizationTaskType getOrganizationTaskType(Long contentCategoryId) {
		if(contentCategoryId != null) {
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_NOTICE) {
				return OrganizationTaskType.NOTICE;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_REPAIRS) {
				return OrganizationTaskType.REPAIRS;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
				return OrganizationTaskType.CONSULT_APPEAL;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
				return OrganizationTaskType.COMPLAINT_ADVICE;
			}
		}
		LOGGER.error("Content category is not matched in organization type.contentCategoryId=" + contentCategoryId);
		return null;
	}

	private Organization checkOrgAddressCity(List<Organization> orgs, Long cityId) {
		if(orgs == null || orgs.isEmpty())
			return null;
		for(Organization r:orgs){
			Address address = this.addressProvider.findAddressById(r.getAddressId());
			if(address == null || address.getCityId().longValue() != cityId.longValue())
				continue;
			return r;
		}
		LOGGER.error("address cityId not equal to city id.orgs="+StringHelper.toJsonString(orgs)+",cityId="+cityId.longValue());
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"address cityId not equal to city id.");
	}

	private List<Organization> checkOrganizationByName(String orgName, String orgType) {
		List<Organization> orgs = this.organizationProvider.listOrganizationByName(orgName,orgType);

		if(orgs == null || orgs.isEmpty()){
			LOGGER.error("organization not found by orgName.orgName="+orgName);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organization not found by orgName.");
		}

		/*if(LOGGER.isDebugEnabled())
			LOGGER.error("checkOrganizationByName-orgs="+StringHelper.toJsonString(orgs));*/

		return orgs;
	}

	private List<OrganizationPostDTO> convertToOrgPostDto(ArrayList list,Long userId) {
		if(list == null || list.isEmpty()){
			LOGGER.error("resultList is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"resultList is empty.");
		}
		List<OrganizationPostDTO> result = new ArrayList<OrganizationPostDTO>();
		for(int rowIndex=1;rowIndex<list.size();rowIndex++){
			RowResult r = (RowResult)list.get(rowIndex);
			if(r.getA() == null || r.getA().trim().equals("")){
				LOGGER.error("have row is empty.rowIndex="+(rowIndex+1));
				break;
			}
			OrganizationPostDTO dto = new OrganizationPostDTO();
			dto.setCityName(r.getA());
			dto.setOrgName(r.getB());
			dto.setOrgType(r.getC());
			dto.setPostType(Long.valueOf(r.getD()));
			dto.setSubject(r.getE());
			dto.setContent(r.getF());
			dto.setUserName(r.getG());
			dto.setToken(r.getH());
			dto.setCreateTime(r.getI());
			result.add(dto);
		}
		return result;
	}

}
