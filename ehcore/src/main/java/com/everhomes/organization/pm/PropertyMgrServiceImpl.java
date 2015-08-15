// @formatter:off
package com.everhomes.organization.pm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressDTO;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.address.ApartmentDTO;
import com.everhomes.address.BuildingDTO;
import com.everhomes.address.ListAddressByKeywordCommand;
import com.everhomes.address.ListBuildingByKeywordCommand;
import com.everhomes.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.family.ApproveMemberCommand;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyBillingAccount;
import com.everhomes.family.FamilyBillingTransactionDTO;
import com.everhomes.family.FamilyBillingTransactions;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyMemberDTO;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.family.RejectMemberCommand;
import com.everhomes.family.RevokeMemberCommand;
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
import com.everhomes.forum.PropertyPostDTO;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.AccountType;
import com.everhomes.organization.BillTransactionResult;
import com.everhomes.organization.BillingAccountHelper;
import com.everhomes.organization.BillingAccountType;
import com.everhomes.organization.GetOrgDetailCommand;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationBillingAccount;
import com.everhomes.organization.OrganizationBillingTransactionDTO;
import com.everhomes.organization.OrganizationBillingTransactions;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationDTO;
import com.everhomes.organization.OrganizationOrder;
import com.everhomes.organization.OrganizationOrderStatus;
import com.everhomes.organization.OrganizationOrderType;
import com.everhomes.organization.OrganizationOwners;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationStatus;
import com.everhomes.organization.OrganizationTaskStatus;
import com.everhomes.organization.OrganizationType;
import com.everhomes.organization.PaidType;
import com.everhomes.organization.TxType;
import com.everhomes.organization.VendorType;
import com.everhomes.organization.pm.pay.BaseVo;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.RestUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.SetCurrentCommunityCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateStatisticHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.handler.ProcessBillModel1;
import com.everhomes.util.excel.handler.PropMgrBillHandler;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.visibility.VisibleRegionType;

/*
 * //物业和组织共用同一张表。所有的逻辑都由以前的communityId 转移到 organizationId。
 */
@Component 
public class PropertyMgrServiceImpl implements PropertyMgrService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);
	private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
	private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";
	private static final String PROP_MESSAGE_BILL = "prop.message.bill";
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AddressProvider addressProvider;

	@Autowired
	private SmsProvider smsProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private FamilyProvider familyProvider;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupProvider groupProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private FamilyService familySerivce;

	@Autowired
	private  ForumService forumService;

	@Autowired
	private  OrganizationProvider organizationProvider;

	@Autowired
	private  OrganizationService organizationService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public void applyPropertyMember(applyPropertyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = user.getCommunityId();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
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
		//物业和组织共用同一张表。所有的逻辑都由以前的communityId 转移到 organizationId。
		long organizationId = findPropertyOrganizationId(communityId);
		CommunityPmMember communityPmMember = createCommunityPmMember(organizationId,cmd.getContactDescription(),user);
		propertyMgrProvider.createPropMember(communityPmMember);

	}

	public CommunityPmMember createCommunityPmMember(Long communityId,String description,User user) {
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
		CommunityPmMember communityPmMember = new CommunityPmMember();
		communityPmMember.setOrganizationId(communityId);
		communityPmMember.setContactName(user.getNickName());
		if(identifier != null){
			communityPmMember.setContactToken(identifier.getIdentifierToken());
			communityPmMember.setContactType(identifier.getIdentifierType());
		}
		communityPmMember.setMemberGroup(PmMemberGroup.MANAGER.getCode());
		communityPmMember.setStatus(PmMemberStatus.CONFIRMING.getCode());
		communityPmMember.setTargetType(PmMemberTargetType.USER.getCode());
		communityPmMember.setTargetId(user.getId());
		communityPmMember.setContactDescription(description);
		return communityPmMember;
	}

	@Override
	public void createPropMember(CreatePropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		//先判断，如果不属于这个小区的物业，才添加物业成员。状态直接设为正常
		Long addUserId =  cmd.getTargetId();
		//添加已注册用户为管理员。
		if(addUserId != null && addUserId != 0){
			List<CommunityPmMember> list = propertyMgrProvider.findPmMemberByCommunityAndTarget(cmd.getCommunityId(), cmd.getTargetType(), cmd.getTargetId());
			if(list == null || list.size() == 0)
			{
				PmMemberGroup memberGroup = PmMemberGroup.fromCode(cmd.getMemberGroup());
				cmd.setMemberGroup(memberGroup.getCode());
				CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
				communityPmMember.setOrganizationId(organizationId);
				communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
				propertyMgrProvider.createPropMember(communityPmMember);
			}
		}
	}

	@Override
	public ListPropMemberCommandResponse getUserOwningProperties() {
		ListPropMemberCommandResponse commandResponse = new ListPropMemberCommandResponse();
		User user  = UserContext.current().getUser();


		List<CommunityPmMember> entityResultList = propertyMgrProvider.listUserCommunityPmMembers(user.getId());
		List<PropertyMemberDTO> members =  new ArrayList<PropertyMemberDTO>();
		//    	commandResponse.setMembers( entityResultList.stream()
		//                 .map(r->{ 
		//                	 PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
		//                	 Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
		//                	 if(organization != null && OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM ){
		//                		 dto.setOrganizationName(organization.getName());
		//                		 Community community = findPropertyOrganizationcommunity(organization.getId());
		//                		 dto.setCommunityId(community.getId());
		//                		 dto.setCommunityName(community.getName());
		//                	 }
		//                	 return dto; })
		//                 .collect(Collectors.toList()));
		if(entityResultList != null && entityResultList.size() > 0){
			for (CommunityPmMember communityPmMember : entityResultList) {
				Organization organization = organizationProvider.findOrganizationById(communityPmMember.getOrganizationId());
				if(OrganizationType.PM == OrganizationType.fromCode(organization.getOrganizationType())){
					PropertyMemberDTO dto =ConvertHelper.convert(communityPmMember,PropertyMemberDTO.class);
					dto.setOrganizationName(organization.getName());
					Community community = findPropertyOrganizationcommunity(organization.getId());
					dto.setCommunityId(community.getId());
					dto.setCommunityName(community.getName());
					members.add(dto);
				}
			}
		}
		commandResponse.setMembers(members);
		return commandResponse;
	}

	@Override
	public ListPropMemberCommandResponse listCommunityPmMembers(ListPropMemberCommand cmd) {
		ListPropMemberCommandResponse commandResponse = new ListPropMemberCommandResponse();
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = propertyMgrProvider.countCommunityPmMembers(organizationId, null);
		if(totalCount == 0) return commandResponse;

		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId(), null, cmd.getPageOffset(),pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ 
					PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
					Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
					dto.setOrganizationName(organization.getName());
					if(organization != null && OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM ){
						dto.setOrganizationName(organization.getName());
						dto.setCommunityId(community.getId());
						dto.setCommunityName(community.getName());
					}
					return dto; })
					.collect(Collectors.toList()));
		commandResponse.setPageCount(pageCount);
		return commandResponse;
	}

	@Override
	public void importPMAddressMapping(PropCommunityIdCommand cmd) {
		User user  = UserContext.current().getUser();
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		//只需要导入一次。先查询总数如果没有，继续导入。
		List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), 1, 10);
		if(entityResultList == null || entityResultList.size() == 0)
		{
			ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
			comand.setCommunityId(community.getId());
			comand.setKeyword("");
			comand.setPageSize(Integer.MAX_VALUE);
			List<AddressDTO> addresses= addressService.listAddressByKeyword(comand).getRequests();
			if(addresses != null && addresses.size() > 0)
			{
				for (AddressDTO address : addresses) {
					CommunityAddressMapping m = new CommunityAddressMapping();
					m.setAddressId(address.getId());
					m.setOrganizationId(organizationId);
					m.setCommunityId(community.getId());
					m.setOrganizationAddress(address.getAddress());
					m.setLivingStatus((byte)0);
					propertyMgrProvider.createPropAddressMapping(m);
				}
			}
		}

	}

	@Override
	public ListPropAddressMappingCommandResponse ListAddressMappings(
			ListPropAddressMappingCommand cmd) {
		ListPropAddressMappingCommandResponse commandResponse = new ListPropAddressMappingCommandResponse();
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		int totalCount = propertyMgrProvider.countCommunityAddressMappings(cmd.getCommunityId(),(byte)0);
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);
		List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), cmd.getPageOffset(), pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ 
					PropAddressMappingDTO dto = ConvertHelper.convert(r, PropAddressMappingDTO.class);
					Address address = addressProvider.findAddressById(dto.getAddressId());
					if(address != null)
						dto.setAddressName(address.getAddress());
					return  dto;})
					.collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}

	@Override
	public ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd) {
		ListPropBillCommandResponse commandResponse = new ListPropBillCommandResponse();
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		int totalCount = propertyMgrProvider.countCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);
		List<CommunityPmBill> entityResultList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress(), cmd.getPageOffset(), pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ 
					PropBillDTO dto = ConvertHelper.convert(r, PropBillDTO.class);
					List<CommunityPmBillItem> itemList = propertyMgrProvider.listCommunityPmBillItems(dto.getId());
					List<PropBillItemDTO> itemDtoList = new ArrayList<PropBillItemDTO>();
					for (CommunityPmBillItem item : itemList) {
						PropBillItemDTO itemDto = ConvertHelper.convert(item, PropBillItemDTO.class);
						itemDtoList.add(itemDto);
					}
					dto.setItemList(itemDtoList);
					return  dto;})
					.collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}



	@Override
	public ListPropOwnerCommandResponse  listPMPropertyOwnerInfo(ListPropOwnerCommand cmd) {
		if(cmd.getCommunityId() == null){
			LOGGER.error("communityId paramter is null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"communityId paramter is null or empty");
		}
		Community community = this.checkCommunity(cmd.getCommunityId());

		ListPropOwnerCommandResponse commandResponse = new ListPropOwnerCommandResponse();
		User user  = UserContext.current().getUser();
		//权限控制
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(community.getId(),OrganizationType.PM.getCode());
		//将communityId转为organizationId
		cmd.setCommunityId(org.getId());
		int totalCount = propertyMgrProvider.countCommunityPmOwners(cmd.getCommunityId(),cmd.getAddress(),cmd.getContactToken());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);
		List<CommunityPmOwner> entityResultList = propertyMgrProvider.listCommunityPmOwners(cmd.getCommunityId(),cmd.getAddress(),cmd.getContactToken(), cmd.getPageOffset(), pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ return ConvertHelper.convert(r, PropOwnerDTO.class); })
				.collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}

	private Community checkCommunity(Long communityId) {
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		return community;
	}

	private int getPageCount(int totalCount, int pageSize){
		int pageCount = totalCount/pageSize;

		if(totalCount % pageSize != 0){
			pageCount ++;
		}
		return pageCount;
	}



	@Caching(evict={@CacheEvict(value="ForumPostById", key="#topicId")})
	private void sendComment(long topicId, long forumId, long userId, long category) {
		Post comment = new Post();
		comment.setParentPostId(topicId);
		comment.setForumId(forumId);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		comment.setCreatorUid(userId);
		comment.setContentType(PostContentType.TEXT.getCode());
		String template = configProvider.getValue(ASSIGN_TASK_AUTO_COMMENT, "");
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
		String template = configProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
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
	public ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd) {
		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid communityId paramter,community is not found.");
		}

		//??权限控制
		Long pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();

		Long pageSize = cmd.getPageSize() == null ? this.configProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize(); 
		long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);

		return this.propertyMgrProvider.listInvitedUsers(cmd.getCommunityId(),
				cmd.getContactToken(),offset,pageSize);
	}

	@Override
	public void approvePropMember(CommunityPropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
		{
			LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the property member or the property member status is not confirming.");
		}
		communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
		propertyMgrProvider.updatePropMember(communityPmMember);

		//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
		long userId = communityPmMember.getTargetId();
		if(userId != 0)
		{

		}
	}

	@Override
	public void rejectPropMember(CommunityPropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
		{
			LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the property member or the property member status is not confirming.");
		}
		long userId = communityPmMember.getTargetId();
		propertyMgrProvider.deletePropMember(communityPmMember);

		//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
		if(userId != 0)
		{

		}
	}

	@Override
	public void revokePMGroupMember(DeletePropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null)
		{
			LOGGER.error("Unable to find the property member.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the property member.");
		}
		long userId = communityPmMember.getTargetId();
		propertyMgrProvider.deletePropMember(communityPmMember);

		//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
		if(userId != 0)
		{

		}

	}

	@Override
	public void approvePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
		if(family == null ||family.getCommunityId() != cmd.getCommunityId())
		{
			LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		//familyProvider.approveMember(cmd.getFamilyId(),cmd.getUserId(),Role.ResourceAdmin);
		ApproveMemberCommand comand = new ApproveMemberCommand();
		comand.setId(cmd.getFamilyId());
		comand.setMemberUid(cmd.getUserId());
		comand.setOperatorRole(Role.ResourceAdmin);
		familySerivce.approveMember(comand);


	}

	@Override
	public void rejectPropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
		if(family == null ||family.getCommunityId() != cmd.getCommunityId())
		{
			LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		String reason = "";
		//    	familyProvider.rejectMember(cmd.getFamilyId(), cmd.getUserId(), reason, Role.ResourceAdmin);

		RejectMemberCommand command = new RejectMemberCommand();
		command.setId(cmd.getFamilyId());
		command.setMemberUid(cmd.getUserId());
		command.setOperatorRole(Role.ResourceAdmin);
		command.setReason(reason);
		familySerivce.rejectMember(command );
	}

	@Override
	public void revokePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
		if(family == null ||family.getCommunityId() != cmd.getCommunityId())
		{
			LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		String reason = "";
		//    	familyProvider.revokeMember(cmd.getFamilyId(), cmd.getUserId(), reason,Role.ResourceAdmin);
		RevokeMemberCommand comand = new RevokeMemberCommand();
		comand.setId(cmd.getFamilyId());
		comand.setMemberUid(cmd.getUserId());
		comand.setOperatorRole(Role.ResourceAdmin);
		comand.setReason(reason);
		familySerivce.revokeMember(comand );
	}

	@Override
	public Tuple<Integer, List<BuildingDTO>> listPropBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制

		return addressService.listBuildingsByKeyword(cmd);
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
	public void setPropCurrentCommunity(SetCurrentCommunityCommand cmd) {
		User user  = UserContext.current().getUser();
		//根据communityId 和userId 判断权限

		userService.setUserCurrentCommunity(cmd.getCommunityId());
	}

	@Override
	public ListPropFamilyWaitingMemberCommandResponse listPropFamilyWaitingMember(ListPropFamilyWaitingMemberCommand cmd) {
		ListPropFamilyWaitingMemberCommandResponse commandResponse = new ListPropFamilyWaitingMemberCommandResponse();

		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		int totalCount = familyProvider.countWaitApproveFamily(cmd.getCommunityId());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);
		List<FamilyDTO>  entityResultList = familyProvider.listWaitApproveFamily(cmd.getCommunityId(), new Long(cmd.getPageOffset()), new Long(pageSize));
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ return r; })
				.collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}

	@Override
	public void setApartmentStatus(SetPropAddressStatusCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getOrganizationId() == null || cmd.getAddressId() == null || cmd.getStatus() == null){
			LOGGER.error("propterty organizationId or addressId or status paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty organizationId or addressId or status paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}

		CommunityAddressMapping mapping = propertyMgrProvider.findOrganiztionAddressMappingByAddressId(cmd.getOrganizationId(), cmd.getAddressId());
		if(mapping == null){
			LOGGER.error("address is not find.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address is not find.");
		}
		mapping.setLivingStatus(cmd.getStatus());
		propertyMgrProvider.updateOrganizationAddressMapping(mapping);

	}

	@Override
	public PropFamilyDTO findFamilyByAddressId(ListPropCommunityAddressCommand cmd) {
		Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制--admin角色
		if(family == null)
		{
			LOGGER.error("family is not existed.communityId=" + cmd.getCommunityId()+",addressId=" + cmd.getAddressId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the family.");
		}
		if(family.getCommunityId() != cmd.getCommunityId())
		{
			LOGGER.error("family is not belong to the community.communityId=" + cmd.getCommunityId()+",addressId=" + cmd.getAddressId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"family is not belong to the community.");
		}
		PropFamilyDTO dto = new PropFamilyDTO();
		if(family != null){
			dto.setName(family.getDisplayName());
			dto.setId(family.getId());
			dto.setMemberCount(family.getMemberCount());
			dto.setAddressId(family.getAddressId());
			dto.setAddress(family.getName());
		}
		CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(cmd.getCommunityId(), cmd.getAddressId());
		if(mapping != null){
			dto.setLivingStatus(mapping.getLivingStatus());
		}
		else{
			dto.setLivingStatus(PmAddressMappingStatus.LIVING.getCode());
		}
		return dto;
	}

	@Override
	public List<FamilyMemberDTO> listFamilyMembersByFamilyId(ListPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		List<GroupMember> entityResultList = groupProvider.findGroupMemberByGroupId(cmd.getFamilyId());
		List<FamilyMemberDTO> results = new ArrayList<FamilyMemberDTO>();

		for (GroupMember member : entityResultList) {
			FamilyMemberDTO dto = new FamilyMemberDTO();
			dto.setId(member.getId());
			dto.setFamilyId(member.getGroupId());
			dto.setMemberUid(member.getMemberId());
			dto.setMemberName(member.getMemberNickName());
			dto.setMemberAvatarUri((member.getMemberAvatar()));
			dto.setCreateTime(member.getCreateTime());
			results.add(dto);
		}
		return results;
	}

	@Override
	public void importPMPropertyOwnerInfo(@Valid PropCommunityIdCommand cmd,
			MultipartFile[] files) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CommunityPmOwner> contactList = PropMrgOwnerHandler.processorPropMgrContact(user.getId(), communityId, resultList);
		ListPropAddressMappingCommand command = new ListPropAddressMappingCommand();
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());

		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listCommunityAddressMappings(organizationId);
		long addressId = 0;
		if(contactList != null && contactList.size() > 0)
		{
			for (CommunityPmOwner contact : contactList)
			{
				if(mappingList != null && mappingList.size() > 0)
				{
					for (CommunityAddressMapping mapping : mappingList)
					{
						if(contact != null && contact.getAddress().equals(mapping.getOrganizationAddress()))
						{
							addressId = mapping.getAddressId();
							break;
						}
					}

				}
				contact.setOrganizationId(organizationId);
				contact.setAddressId(addressId);
				propertyMgrProvider.createPropOwner(contact);

			}
		}

	}

	@Override
	public void importPropertyBills(@Valid PropCommunityIdCommand cmd, MultipartFile[] files) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CommunityPmBill> billList = PropMgrBillHandler.processorPropBill(user.getId(), communityId, resultList, ProcessBillModel1.PROCESS_TO_OBJECT_PER_ROW, ProcessBillModel1.PROCESS_PREVIOUS_ROW_TITLE);
		ListPropAddressMappingCommand command = new ListPropAddressMappingCommand();
		command.setCommunityId(communityId);
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());

		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listCommunityAddressMappings(organizationId);
		long addressId = 0;
		if(billList != null && billList.size() > 0)
		{
			for (CommunityPmBill bill : billList)
			{
				if(mappingList != null && mappingList.size() > 0)
				{
					for (CommunityAddressMapping mapping : mappingList)
					{
						if(bill != null && bill.getAddress().equals(mapping.getOrganizationAddress()));
						{
							addressId = mapping.getAddressId();
							break;
						}
					}

				}
				bill.setOrganizationId(organizationId);
				bill.setEntityId(addressId);
				bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
				createPropBill(bill);
			}
		}
	}

	@Override
	public void createPropBill(CommunityPmBill bill) {
		User user  = UserContext.current().getUser();
		this.dbProvider.execute((status) ->{
			propertyMgrProvider.createPropBill(bill);
			System.out.println(bill);
			List<CommunityPmBillItem> itemList =  bill.getItemList();
			if(itemList != null && itemList.size() > 0){
				for (CommunityPmBillItem communityPmBillItem : itemList) {
					communityPmBillItem.setBillId(bill.getId());
					propertyMgrProvider.createPropBillItem(communityPmBillItem);
				}
			}
			return status;
		});
	}

	@Override
	public void sendPropertyBillById(PropCommunityBillIdCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		CommunityPmBill bill = propertyMgrProvider.findPropBillById(cmd.getBillId());
		sendPropertyBillById(bill);

	}

	@Override
	public void sendPropertyBillByMonth(PropCommunityBillDateCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		List<CommunityPmBill> billList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr());
		sendPropertyBills(billList);


	}


	public void sendPropertyBills(List<CommunityPmBill> billList) {
		if(billList != null && billList.size() > 0){
			for (CommunityPmBill communityPmBill : billList) {
				Address address = addressProvider.findAddressById(communityPmBill.getEntityId());
				if(address != null){
					Family family = familyProvider.findFamilyByAddressId(address.getId());
					if(family != null){
						String message = buildBillMessage(communityPmBill);
						sendNoticeToFamilyById(family.getId(), message);
					}
				}
			}
		}

	}

	public String buildBillMessage(CommunityPmBill bill) {
		String template = configProvider.getValue(PROP_MESSAGE_BILL, "");
		String content = "\n" + template +"账单时间：" + bill.getDateStr() +"\n";
		List<CommunityPmBillItem> itemList  = propertyMgrProvider.listCommunityPmBillItems(bill.getId());
		if(itemList != null && itemList.size() > 0)
		{
			for (CommunityPmBillItem item : itemList)
			{
				content += item.getItemName()+": " + item.getTotalAmount() + "元\n";
			}
		}
		//content += "总费用：" + bill.getTotalAmount();
		return content;
	}

	public void sendPropertyBillById(CommunityPmBill bill) {
		if(bill == null)
		{
			LOGGER.error("Unable to find the bill." );
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_BILL, 
					"Unable to find the bill.");
		}
		Address address = addressProvider.findAddressById(bill.getEntityId());
		if(address == null)
		{
			LOGGER.error("Unable to find the address.addressId=" + bill.getEntityId());
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_ADDRESS, 
					"Unable to find the address.");
		}
		Family family = familyProvider.findFamilyByAddressId(address.getId());
		if(family == null)
		{
			LOGGER.error("Unable to find the family.familyId=" +address.getId());
			throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_FAMILY, 
					"Unable to find the family.");
		}
		String message = buildBillMessage(bill);
		sendNoticeToFamilyById(family.getId(), message);
	}

	@Override
	public void sendNoticeToFamily(PropCommunityBuildAddessCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		List<String> buildingNames = cmd.getBuildingNames();
		List<Long> addressIds = cmd.getAddressIds();

		//物业发通知机制： 对于已注册的发消息 -familyIds   未注册的发短信-userIds。
		//已注册的user 分三种： 1-已加入家庭，发家庭消息。 2-还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。 3-家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
		List<CommunityPmOwner> owners  = new ArrayList<CommunityPmOwner>();
		List<Long> familyIds = new ArrayList<Long>();


		//按小区发送: buildingNames 和 addressIds 为空。
		if((buildingNames == null || buildingNames.size() == 0) && (addressIds == null || addressIds.size() == 0)){
			ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
			comand.setCommunityId(cmd.getCommunityId());
			comand.setKeyword("");
			comand.setPageSize(Integer.MAX_VALUE);
			List<AddressDTO> addresses= addressService.listAddressByKeyword(comand).getRequests();
			if(addresses != null && addresses.size() > 0){
				for (AddressDTO address : addresses) {
					Family family = familyProvider.findFamilyByAddressId(address.getId());
					if(family != null){
						familyIds.add(family.getId());
					}
					List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, address.getId());
					owners.addAll(ownerList);

				}
			}
		}

		//按地址发送：
		else if(addressIds != null && addressIds.size()  > 0){
			for (Long addressId : addressIds) {
				Family family = familyProvider.findFamilyByAddressId(addressId);
				if(family != null){
					familyIds.add(family.getId());
				}
				List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, addressId);
				owners.addAll(ownerList);
			}
		}

		//按楼栋发送：
		else if((addressIds == null || addressIds.size()  == 0 )  && (buildingNames != null && buildingNames.size() > 0)){
			for (String buildingName : buildingNames) {
				int pageSize = configProvider.getIntValue("pagination.page.size",  AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
				List<ApartmentDTO> addresses =  addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, pageSize);
				if(addresses != null && addresses.size() > 0){
					for (ApartmentDTO address : addresses) {
						Family family = familyProvider.findFamilyByAddressId(address.getAddressId());
						if(family != null){
							familyIds.add(family.getId());
						}
						List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, address.getAddressId());
						owners.addAll(ownerList);
					}
				}
			}
		}

		if(familyIds != null && familyIds.size() > 0){
			for (Long familyId : familyIds) {
				sendNoticeToFamilyById(familyId, cmd.getMessage());
			}
		}

		//处理业主信息表 :1- 是user，已加入家庭，发家庭消息已包含该user。 2- 是user，还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。 3-不是user，发短信。  4：是user，家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
		processCommunityPmOwner(communityId,owners,cmd.getMessage());
	}

	private void processCommunityPmOwner(Long communityId,List<CommunityPmOwner> owners,String message) {
		List<String> phones = new ArrayList<String>();
		List<Long> userIds = new ArrayList<Long>();
		if(owners != null && owners.size() > 0){
			for (CommunityPmOwner communityPmOwner : owners) {
				User userPhone = userService.findUserByIndentifier(communityPmOwner.getContactToken());
				if(userPhone == null){
					// 3-不是user，发短信
					phones.add(communityPmOwner.getContactToken());
				}
				else{
					Family family = familyProvider.findFamilyByAddressId(communityPmOwner.getAddressId());
					if(family != null){
						GroupMember member = groupProvider.findGroupMemberByMemberInfo(family.getId(), GroupDiscriminator.FAMILY.getCode(), userPhone.getId());
						if(member == null){
							// 2- 是user，还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。
							userIds.add(userPhone.getId());
						}
						//1- 是user，已加入家庭，发家庭消息已包含该user。 已经发过family。
					}
					else{
						//4：是user，家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
						userIds.add(userPhone.getId());
					}
				}
			}
		}

		//是user，还未加入家庭，发个人信息.
		if(userIds != null && userIds.size() > 0){
			for (Long userId : userIds) {
				sendNoticeToUserById(userId, message);
			}
		}

		//不是user，发短信。
		if(phones != null && phones.size() > 0 ){
			for (String phone : phones) {
				smsProvider.sendSms(phone, message);
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

	public void sendNoticeToUserById(Long userId,String message){
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_FAMILY);
		messageDto.setSenderUid(UserContext.current().getUser().getId());
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), String.valueOf(userId)));
		messageDto.setMetaAppId(AppConstants.APPID_USER);
		messageDto.setBody(message);

		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_USER, MessageChannelType.USER.getCode(), 
				String.valueOf(userId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
	}

	@Override
	public void sendMsgToPMGroup(PropCommunityIdMessageCommand cmd) {
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		List<CommunityPmMember> memberList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId());
		if(memberList != null && memberList.size() > 0){
			for (CommunityPmMember communityPmMember : memberList) {
				sendNoticeToUserById(communityPmMember.getTargetId(), cmd.getMessage());
			}
		}
	}

	@Override
	public PostDTO createTopic(NewTopicCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getVisibleRegionId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getVisibleRegionId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getVisibleRegionId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		if(cmd.getCreatorTag() == null || "".equals(cmd.getCreatorTag())){
			cmd.setCreatorTag(PostEntityTag.PM.getCode());
		}
		if(cmd.getPrivateFlag() == null|| "".equals(cmd.getCreatorTag())){
			cmd.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
		}
		if(cmd.getVisibleRegionType() == null || cmd.getVisibleRegionType() == 0){
			cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
		}
		if(cmd.getTargetTag() == null || "".equals(cmd.getCreatorTag())){
			cmd.setTargetTag(PostEntityTag.USER.getCode());
		}
		PostDTO post = forumService.createTopic(cmd);
		return post;
	}

	@Override
	public ListPropPostCommandResponse  queryTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
		ListPropPostCommandResponse response = new ListPropPostCommandResponse();
		User user  = UserContext.current().getUser();

		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		int totalCount = 0; //propertyMgrProvider.countCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus());
		if(totalCount == 0) return response;
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		List<PropertyPostDTO> results = new ArrayList<PropertyPostDTO>();
		List<CommunityPmTasks> tasks = null; //propertyMgrProvider.listCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus(), cmd.getPageOffset(), pageSize);
		if(tasks != null && tasks.size() > 0){
			for (CommunityPmTasks task : tasks) {
				PostDTO post = forumService.getTopicById(task.getApplyEntityId(), communityId, false);		
				PropertyPostDTO dto = ConvertHelper.convert(post, PropertyPostDTO.class);
				dto.setCommunityId(task.getOrganizationId());
				dto.setEntityType(task.getApplyEntityType());
				dto.setEntityId(task.getApplyEntityId());
				dto.setTargetType(task.getTargetType());
				dto.setTargetId(task.getTargetId());
				dto.setTaskType(task.getTaskType());
				dto.setTaskStatus(task.getTaskStatus());
				results.add(dto);
			}
		}
		response.setPosts(results);
		response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return response;
	}

	@Override
	public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
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
	public ListPropTopicStatisticCommandResponse getPMTopicStatistics(ListPropTopicStatisticCommand cmd) {
		ListPropTopicStatisticCommandResponse response = new ListPropTopicStatisticCommandResponse();
		String taskType = null; // OrganizationTaskType.fromCode(cmd.getCategoryId()).getCode();
		String startStrTime = cmd.getStartStrTime();
		String endStrTime = cmd.getEndStrTime();
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		Long communityId = cmd.getCommunityId();
		/** 当天数量列表*/
		List<Integer> todayList = new ArrayList<Integer>();

		/** z昨天数量列表*/
		List<Integer> yesterdayList = new ArrayList<Integer>();

		/** 上周数量列表*/
		List<Integer> weekList = new ArrayList<Integer>(); 

		/** 上月数量列表*/
		List<Integer> monthList = new ArrayList<Integer>();

		/** 时间点数量列表*/
		List<Integer> dateList = new ArrayList<Integer>();

		Date date = DateStatisticHelper.getCurrentUTCTime();
		Date currentStartDate = DateStatisticHelper.getCurrent0Hour();
		Date weekStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 7, false);
		Date yesterdayStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 1, false);
		Date monthStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 30, false);
		createList(communityId,taskType,todayList,currentStartDate.getTime(), date.getTime());
		createList(communityId,taskType,yesterdayList,yesterdayStartDate.getTime(), currentStartDate.getTime());
		createList(communityId,taskType,weekList,weekStartDate.getTime(), date.getTime());
		createList(communityId,taskType,monthList,monthStartDate.getTime(), date.getTime());

		if(!StringUtils.isEmpty(startStrTime) && !StringUtils.isEmpty(endStrTime))
		{
			Date startTime;
			Date endTime;
			try {
				startTime = DateStatisticHelper.parseDateStr(startStrTime);
				endTime = DateStatisticHelper.parseDateStr(endStrTime);
				createList(communityId,taskType,dateList,startTime.getTime(), endTime.getTime());
			} catch (ParseException e) {
				LOGGER.error("failed to parse date.startStrTime=" + startStrTime +",endStrTime=" + endStrTime);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"failed to parse date");
			}

		}
		response.setDateList(dateList);
		response.setMonthList(monthList);
		response.setTodayList(todayList);
		response.setWeekList(weekList);
		response.setYesterdayList(yesterdayList);
		return response;
	}

	private void createList(Long communityId,String taskType,List<Integer> todayList, long startTime, long endTime)
	{
		int todayCount = propertyMgrProvider.countCommunityPmTasks(communityId, taskType,null,String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
		todayList.add(todayCount);
		int num = OrganizationTaskStatus.PROCESSED.getCode();
		for (int i = 0; i <= num ; i++)
		{
			int count = propertyMgrProvider.countCommunityPmTasks(communityId, taskType,(byte)i,String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
			todayList.add(count);
		}
	}

	@Override
	public PropAptStatisticDTO getApartmentStatistics(PropCommunityIdCommand cmd) {
		PropAptStatisticDTO dto = new PropAptStatisticDTO();
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}

		//权限控制
		int familyCount = familyProvider.countFamiliesByCommunityId(communityId);
		int userCount = familyProvider.countUserByCommunityId(communityId);
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		communityId = cmd.getCommunityId();
		int defaultCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.DEFAULT.getCode());
		int liveCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.LIVING.getCode());
		int rentCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.RENT.getCode());
		int freeCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.FREE.getCode());
		int decorateCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.DECORATE.getCode());
		int unsaleCount = propertyMgrProvider.countCommunityAddressMappings(communityId, PmAddressMappingStatus.UNSALE.getCode());
		dto.setAptCount(community.getAptCount()==null ?0 : community.getAptCount());
		dto.setFamilyCount(familyCount);
		dto.setUserCount(userCount);
		dto.setDefaultCount(defaultCount);
		dto.setLiveCount(liveCount);
		dto.setRentCount(rentCount);
		dto.setFreeCount(freeCount);
		dto.setDecorateCount(decorateCount);
		dto.setUnsaleCount(unsaleCount);
		return dto;
	}

	@Override
	public OrganizationDTO findPropertyOrganization(PropCommunityIdCommand cmd) {
		OrganizationDTO dto = new OrganizationDTO();
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the community.");
		}
		//权限控制
		List<OrganizationCommunity>   organizationCommunityList = organizationProvider.listOrganizationByCommunityId(cmd.getCommunityId());
		if(organizationCommunityList != null && organizationCommunityList.size() > 0){
			for (OrganizationCommunity organizationCommunity : organizationCommunityList) {
				Organization organization  = organizationProvider.findOrganizationById(organizationCommunity.getOrganizationId());
				if(organization != null && OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM){
					dto = ConvertHelper.convert(organization, OrganizationDTO.class);
					break;
				}
			}
		}
		if(dto == null){
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
			dto = ConvertHelper.convert(organization, OrganizationDTO.class);
		}
		return dto;
	}

	@Override
	public Long findPropertyOrganizationId(Long communityId) {
		PropCommunityIdCommand command = new PropCommunityIdCommand();
		command.setCommunityId(communityId);
		OrganizationDTO dto = findPropertyOrganization(command);
		if(dto != null){
			return dto.getId();
		}
		else{
			return 0l;
		}
	}

	@Override
	public Community findPropertyOrganizationcommunity(Long organizationId) {
		Community community = new Community();
		OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationPropertyCommunity(organizationId);
		if(organizationCommunity != null){
			community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
		}
		return community;
	}

	public List<PropCommunityContactDTO> listPropertyCommunityContacts(ListPropCommunityContactCommand cmd){

		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty communityId paramter can not be null or empty");
		}
		GetOrgDetailCommand c = new GetOrgDetailCommand();
		c.setCommunityId(cmd.getCommunityId());
		c.setOrganizationType(cmd.getOrganizationType());
		OrganizationDTO organizationDTO = this.organizationService.getOrganizationByComunityidAndOrgType(c);
		if(organizationDTO == null){
			LOGGER.error("Property organization is not exists.communityId="+ cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Property organization is not exists.");
		}
		long organizationId = organizationDTO.getId();
		List<CommunityPmContact> communityPmContacts = propertyMgrProvider.listCommunityPmContacts(organizationId);
		if(communityPmContacts == null || communityPmContacts.isEmpty()){
			LOGGER.error("Property community contacts is not exists.communityId="+ cmd.getCommunityId());
			return null;
		}
		List<PropCommunityContactDTO> propCommunityContactdtos = communityPmContacts.stream().map(r ->{
			return ConvertHelper.convert(r, PropCommunityContactDTO.class);
		}).collect(Collectors.toList());
		return propCommunityContactdtos;

	}

	@Override
	public List<PropFamilyDTO> listPropApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd) {
		List<PropFamilyDTO> list = new ArrayList<PropFamilyDTO>();
		User user  = UserContext.current().getUser();

		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}

		OrganizationCommunity  orgCom = this.propertyMgrProvider.findOrganizationCommunityByOrgId(cmd.getOrganizationId());
		if(orgCom == null){
			LOGGER.error("Unable to find the community by organizationId="+cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community by organizationId");
		}
		cmd.setCommunityId(orgCom.getCommunityId());

		//权限控制
		Tuple<Integer,List<ApartmentDTO>> apts = addressService.listApartmentsByKeyword(cmd);
		List<ApartmentDTO> aptList = apts.second();
		for (ApartmentDTO apartmentDTO : aptList) {
			PropFamilyDTO dto = new PropFamilyDTO();
			Family family = familyProvider.findFamilyByAddressId(apartmentDTO.getAddressId());
			if(family != null )
			{
				dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
				dto.setName(apartmentDTO.getApartmentName());
				dto.setAddressId(family.getAddressId());
				dto.setId(family.getId());
				dto.setMemberCount(family.getMemberCount());
			}
			else
			{
				dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
				dto.setName(apartmentDTO.getApartmentName());
				dto.setAddressId(apartmentDTO.getAddressId());
				dto.setId(0l);
				dto.setMemberCount(0l);
			}
			CommunityAddressMapping mapping = propertyMgrProvider.findOrganiztionAddressMappingByAddressId(cmd.getOrganizationId(),apartmentDTO.getAddressId());
			if(mapping != null){
				dto.setLivingStatus(mapping.getLivingStatus());
			}
			else{
				dto.setLivingStatus(PmAddressMappingStatus.LIVING.getCode());
			}

			//判断公寓是否欠费
			CommunityPmBill bill =  this.propertyMgrProvider.findFamilyNewestBill(dto.getAddressId(), cmd.getOrganizationId());
			if(bill != null){
				BigDecimal paidAmount = this.countBillTxAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0)
					dto.setOwed(OwedType.OWED.getCode());
				else
					dto.setOwed(OwedType.NO_OWED.getCode());
			}
			else
				dto.setOwed(OwedType.NO_OWED.getCode());

			list.add(dto);
		}
		return list;
	}

	private BigDecimal countBillTxAmount(Long billId) {
		List<OrganizationOrder> orders = this.organizationProvider.listOrganizationOrdersByBillId(billId,OrganizationOrderStatus.PAID.getCode());
		BigDecimal paidAmount = BigDecimal.ZERO;
		if(orders != null && !orders.isEmpty()){
			for(OrganizationOrder order : orders){
				paidAmount = paidAmount.add(order.getAmount());
			}
		}
		return paidAmount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void importPmBills(Long orgId, MultipartFile[] files) {
		if(orgId == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(orgId);
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + orgId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		if(files == null){
			LOGGER.error("files is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"files is null");
		}

		String rootPath = System.getProperty("user.dir");
		String filePath1 = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		String filePath2 = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		/*int index = rootPath.lastIndexOf(File.separator);
		String jarDirPath = rootPath.substring(0, index);*/
		String jarPath =rootPath+File.separator+"ehparser-0.0.1-SNAPSHOT.jar";
		LOGGER.error("jarPath="+jarPath);
		
		StringBuilder builder = new StringBuilder();
		//builder.append("java -jar D:/dev_documents/workspace2/ehparser/target/ehparser-0.0.1-SNAPSHOT.jar");
		
		builder.append("java -jar"+" "+jarPath);
		builder.append(" ");
		builder.append(orgId);
		builder.append(" ");
		builder.append(filePath1);
		builder.append(" ");
		builder.append(filePath2);
		String command = builder.toString();

		try {
			//将原文件暂存在服务器中
			OutputStream out = new FileOutputStream(new File(filePath1));
			InputStream in = files[0].getInputStream();
			byte [] buffer = new byte [1024];
			while(in.read(buffer) != -1){ 
				out.write(buffer);
			}
			out.close();
			in.close();
			//启动进程解析原文件
			Runtime.getRuntime().exec(command);
			//读取解析后的文件
			int i = 0;
			File file2 = null;
			while(i < 5){
				file2 = new File(filePath2);
				if(!file2.exists())	Thread.sleep(1000);
				else	break;
				i++;
			}
			if(file2 == null || !file2.exists()){
				LOGGER.error("parse file failure.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"parse file failure.");
			}
			List<CommunityPmBill> bills = this.convertExcelFileToPmBills(file2);
			createPmBills(bills, orgId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					e.getMessage());
		} finally{
			File file = new File(filePath1);
			File file2 = new File(filePath2);
			if(file.exists())	file.delete();
			if(file2.exists())	file2.delete();
		}
	}

	private List<CommunityPmBill> convertExcelFileToPmBills(File file) {
		List<CommunityPmBill> bills = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			for(int i = sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);
				CommunityPmBill bill = new CommunityPmBill();
				bill.setStartDate(new java.sql.Date(format.parse(row.getCell(0).getStringCellValue().trim()).getTime()));
				bill.setEndDate(new java.sql.Date(format.parse(row.getCell(1).getStringCellValue().trim()).getTime()));
				bill.setPayDate(new java.sql.Date(format.parse(row.getCell(2).getStringCellValue().trim()).getTime()));
				bill.setAddress(row.getCell(3).getStringCellValue().trim());
				//dueAmount column
				BigDecimal dueAmount = new BigDecimal(row.getCell(4).getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
				bill.setDueAmount(dueAmount);
				//oweAmount column
				Cell oweAmountCell = row.getCell(5);
				if(oweAmountCell != null){
					BigDecimal oweAmount = new BigDecimal(oweAmountCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
					bill.setOweAmount(oweAmount);
				}
				//description column
				Cell descriptionCell = row.getCell(6);
				if(descriptionCell != null)
					bill.setDescription(descriptionCell.getStringCellValue().trim());
				bills.add(bill);
			}
			wb.close();
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return bills;
	}

	/**
	 * 将数据插入数据库
	 */
	public void createPmBills(List<CommunityPmBill> bills,Long orgId){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listOrganizationAddressMappingsByOrgId(orgId);
		if(bills != null && bills.size() > 0){
			this.dbProvider.execute( s -> {
				for (CommunityPmBill bill : bills){
					if(mappingList != null && mappingList.size() > 0)
					{
						CommunityAddressMapping mapping = null;
						for (CommunityAddressMapping m : mappingList){
							if(bill != null && bill.getAddress().equals(m.getOrganizationAddress())){
								mapping = m;
								break;
							}
						}
						if(mapping == null){
							LOGGER.error(bill.getAddress() + " not find in address mapping.");
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
									bill.getAddress() + " not find in address mapping.");
						}

						CommunityPmBill existedBill = this.propertyMgrProvider.findFamilyPmBillInStartDateToEndDate(mapping.getAddressId(),bill.getStartDate(),bill.getEndDate());
						if(existedBill != null){
							LOGGER.error("the bill is exist.please don't import repeat data.address="+bill.getAddress()+",startDate=" + format.format(bill.getStartDate())+",endDate="+format.format(bill.getEndDate()));
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
									"the bill is exist.please don't import repeat data.address="+bill.getAddress()+",startDate=" + format.format(bill.getStartDate())+",endDate="+format.format(bill.getEndDate()));
						}

						bill.setOrganizationId(orgId);
						bill.setEntityId(mapping.getAddressId());
						bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
						bill.setNotifyCount(0);
						bill.setNotifyTime(null);

						cal.setTimeInMillis(bill.getStartDate().getTime());
						StringBuilder builder = new StringBuilder();
						builder.append(cal.get(Calendar.YEAR) +"-");
						if(cal.get(Calendar.MONTH)<9)
							builder.append("0"+(cal.get(Calendar.MONTH)+1));
						else
							builder.append(cal.get(Calendar.MONTH)+1);
						bill.setDateStr(builder.toString());
						bill.setName(bill.getDateStr() + "月账单");
						bill.setCreatorUid(user.getId());
						bill.setCreateTime(timeStamp);
						//往期欠款处理
						if(bill.getOweAmount() == null){
							CommunityPmBill beforeBill = this.propertyMgrProvider.findFamilyNewestBill(mapping.getAddressId(), orgId);
							if(beforeBill != null){
								//payAmount为负
								BigDecimal payedAmount = this.countBillTxAmount(beforeBill.getId());
								BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(payedAmount);
								bill.setOweAmount(oweAmount);
							}
							else
								bill.setOweAmount(BigDecimal.ZERO);
						}

						propertyMgrProvider.createPropBill(bill);
						List<CommunityPmBillItem> itemList =  bill.getItemList();
						if(itemList != null && itemList.size() > 0){
							for (CommunityPmBillItem communityPmBillItem : itemList) {
								communityPmBillItem.setBillId(bill.getId());
								communityPmBillItem.setCreateTime(timeStamp);
								communityPmBillItem.setCreatorUid(user.getId());
								propertyMgrProvider.createPropBillItem(communityPmBillItem);
							}
						}
					}
				}

				return s;
			});

		}
	}

	@Override
	public ListPmBillsByConditionsCommandResponse listPmBillsByConditions(ListPmBillsByConditionsCommand cmd) {
		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Unable to find the organization.");
		}
		remoteRefreshOrgOrderStatus();

		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);
		ListPmBillsByConditionsCommandResponse result = new ListPmBillsByConditionsCommandResponse();
		List<PmBillsDTO> billList = new ArrayList<PmBillsDTO>();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
		java.sql.Date startDate = null;
		java.sql.Date endDate = null;
		if(cmd.getBillDate() != null && !cmd.getBillDate().equals("")){
			startDate = this.getFirstDayOfMonthByStr(cmd.getBillDate());
			endDate = this.getLastDayOfMonthByStr(cmd.getBillDate());
		}
		else{//没有查询条件返回最新月份的账单
			if(cmd.getAddress() == null || cmd.getAddress().isEmpty()){
				CommunityPmBill tempBill = this.propertyMgrProvider.findOneNewestPmBillByOrgId(organization.getId());
				if(tempBill != null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					String billDate = format.format(tempBill.getStartDate());
					startDate = this.getFirstDayOfMonthByStr(billDate);
					endDate = this.getLastDayOfMonthByStr(billDate);
				}
			}
		}

		List<CommunityPmBill> comBillList = this.propertyMgrProvider.listOrganizationBillsByAddressAndDate(organization.getId(),cmd.getAddress(),startDate,endDate,offset,pageSize+1);
		if(comBillList != null && !comBillList.isEmpty()){
			if(comBillList.size() == pageSize+1){
				comBillList.remove(comBillList.size()-1);
				result.setNextPageOffset(cmd.getPageOffset()+1);
			}
			for(CommunityPmBill comBill : comBillList){
				PmBillsDTO billDto = ConvertHelper.convert(comBill, PmBillsDTO.class);
				billDto.setEndDate(comBill.getEndDate().getTime());
				billDto.setPayDate(comBill.getPayDate().getTime());
				billDto.setStartDate(comBill.getStartDate().getTime());
				BigDecimal payedAmount = this.countBillTxAmount(billDto.getId());
				billDto.setPayedAmount(payedAmount);
				billDto.setWaitPayAmount(billDto.getDueAmount().add(billDto.getOweAmount()).subtract(payedAmount));
				billDto.setTotalAmount(billDto.getDueAmount().add(billDto.getOweAmount()));
				billList.add(billDto);
			}
		}
		result.setRequests(billList);
		return result;	
	}

	private void remoteRefreshOrgOrderStatus() {
		try{
			List<OrganizationOrder> orders = this.organizationProvider.listOrganizationOrdersByStatus(OrganizationOrderStatus.WAITING_FOR_PAY.getCode());
			if(orders != null && !orders.isEmpty()){
				for(OrganizationOrder order : orders){
					String orderNo = this.convertOrderIdToOrderNo(order.getId());
					this.remoteUpdateOrgOrderByOrderNo(orderNo);
				}
			}
		}
		catch(Exception e){
			LOGGER.error("remote refresh organization order fail.message="+e.getMessage());
		}
	}

	private java.sql.Date getLastDayOfMonthByStr(String billDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

		Date date = null;
		try {
			date = format.parse(billDate);
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());
			return startDate;
		} catch (ParseException e) {
			LOGGER.error("date format is wrong.must be yyyy-MM");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"date format is wrong.must be yyyy-MM");
		}
	}

	private java.sql.Date getFirstDayOfMonthByStr(String billDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		try {
			Date date = format.parse(billDate);
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			java.sql.Date endDate = new java.sql.Date(cal.getTimeInMillis());
			return endDate;
		} catch (ParseException e) {
			LOGGER.error("date format is wrong.must be yyyy-MM");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"date format is wrong.must be yyyy-MM");
		}
	}

	@Override
	public void deletePmBills(DeletePmBillsCommand cmd) {
		if(cmd.getIds() == null || cmd.getIds().isEmpty()){
			LOGGER.error("ids paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ids paramter can not be null or empty");
		}
		this.dbProvider.execute(s -> {
			if(cmd.getIds() != null && !cmd.getIds().isEmpty()){
				for(Long billId : cmd.getIds()){
					CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(billId);
					if(communBill != null){
						this.organizationProvider.deleteOrganizationBillsById(communBill.getId());
					}
				}
			}
			return true;
		});

	}

	@Override
	public void insertPmBills(InsertPmBillsCommand cmd) {
		if(cmd.getInsertList() == null || cmd.getInsertList().isEmpty()){
			LOGGER.error("insertList paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"insertList paramter can not be null or empty");
		}

		if(cmd.getInsertList() != null && !cmd.getInsertList().isEmpty()){
			Calendar cal = Calendar.getInstance();
			User user  = UserContext.current().getUser();
			Timestamp timeStamp = new Timestamp(new Date().getTime());

			for (UpdatePmBillsDto insertBill : cmd.getInsertList()){
				if(insertBill.getOrganizationId() == null || insertBill.getAddress() == null){
					LOGGER.error("propterty organizationId or address paramter can not be null or empty");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"propterty organizationId or address paramter can not be null or empty");
				}
				Organization organization = this.organizationProvider.findOrganizationById(insertBill.getOrganizationId());
				if(organization == null){
					LOGGER.error("Insert failure.Unable to find the organization.organizationId=" + insertBill.getOrganizationId());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Insert failure.Unable to find the organization.");
				}

				CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(insertBill.getOrganizationId(),insertBill.getAddress());
				if(addressMapping == null){
					LOGGER.error("Insert failure.the address not find");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Insert failure.the address not find");
				}

				CommunityPmBill bill = ConvertHelper.convert(insertBill, CommunityPmBill.class);
				bill.setEntityId(addressMapping.getAddressId());
				bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
				bill.setAddress(addressMapping.getOrganizationAddress());
				bill.setCreatorUid(user.getId());
				bill.setCreateTime(timeStamp);
				bill.setEndDate(new java.sql.Date(insertBill.getEndDate()));
				bill.setPayDate(new java.sql.Date(insertBill.getPayDate()));
				bill.setStartDate(new java.sql.Date(insertBill.getStartDate()));

				cal.setTimeInMillis(bill.getStartDate().getTime());
				StringBuilder builder = new StringBuilder();
				builder.append(cal.get(Calendar.YEAR) +"-");
				if(cal.get(Calendar.MONTH)<9)
					builder.append("0"+(cal.get(Calendar.MONTH)+1));
				else
					builder.append(cal.get(Calendar.MONTH)+1);
				bill.setDateStr(builder.toString());
				bill.setName(bill.getDateStr() + "月账单");
				//往期欠款处理
				if(bill.getOweAmount() == null){
					CommunityPmBill beforeBill = this.propertyMgrProvider.findFamilyNewestBill(bill.getEntityId(), bill.getOrganizationId());
					if(beforeBill != null){
						//payAmount为负
						BigDecimal payAmount = this.countBillTxAmount(beforeBill.getId());
						BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(payAmount);
						bill.setOweAmount(oweAmount);
					}
					else
						bill.setOweAmount(BigDecimal.ZERO);
				}
				createPropBill(bill);
			}
		}

	}

	@Override
	public int updatePmBills(UpdatePmBillsCommand cmd) {
		if(cmd.getUpdateList() == null || cmd.getUpdateList().isEmpty()){
			LOGGER.error("updateList paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"updateList paramter can not be null or empty");
		}

		if(cmd.getUpdateList() != null && !cmd.getUpdateList().isEmpty()){
			this.dbProvider.execute(s -> {
				for(UpdatePmBillsDto bill : cmd.getUpdateList()){
					CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(bill.getId());
					if(communBill != null){
						if(bill.getEndDate() != null)
							communBill.setEndDate(new java.sql.Date(bill.getEndDate()));
						if(bill.getPayDate() != null)
							communBill.setPayDate(new java.sql.Date(bill.getPayDate()));
						if(bill.getStartDate() != null)
							communBill.setStartDate(new java.sql.Date(bill.getStartDate()));
						if(!(bill.getDescription() == null || bill.getDescription().equals("")))
							communBill.setDescription(bill.getDescription());
						if(bill.getDueAmount() != null)
							communBill.setDueAmount(bill.getDueAmount());
						if(bill.getOweAmount() != null)
							communBill.setOweAmount(bill.getOweAmount());
						if(bill.getAddress() != null && !bill.getAddress().equals("")){
							CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(communBill.getOrganizationId(), bill.getAddress());
							if(addressMapping != null){
								communBill.setAddress(bill.getAddress());
								communBill.setEntityId(addressMapping.getAddressId());
							}
							else{
								LOGGER.error("update bill failure.because address not found..address="+bill.getAddress());
								throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
										"update bill failure.because address not found.address="+bill.getAddress());
							}
						}
						this.organizationProvider.updateOrganizationBill(communBill);
					}
				}
				return s;
			});
		}
		return 1;
	}

	@Override
	public void deletePmBill(DeletePmBillCommand cmd) {
		if(cmd.getId() == null){
			LOGGER.error("id paramter can not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id paramter can not be null");
		}
		CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(cmd.getId());
		if(communBill != null)
			this.organizationProvider.deleteOrganizationBillsById(communBill.getId());
	}

	@Override
	public void updatePmBill(UpdatePmBillCommand bill) {
		if(bill.getId() == null || bill.getOrganizationId() == null){
			LOGGER.error("id or organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id or organizationId paramter can not be null or empty");
		}

		CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(bill.getId());
		if(communBill != null){
			if(bill.getEndDate() != null)
				communBill.setEndDate(new java.sql.Date(bill.getEndDate()));
			if(bill.getPayDate() != null)
				communBill.setPayDate(new java.sql.Date(bill.getPayDate()));
			if(bill.getStartDate() != null)
				communBill.setStartDate(new java.sql.Date(bill.getStartDate()));
			if(!(bill.getDescription() == null || bill.getDescription().equals("")))
				communBill.setDescription(bill.getDescription());
			if(bill.getDueAmount() != null)
				communBill.setDueAmount(bill.getDueAmount());
			if(bill.getOweAmount() != null)
				communBill.setOweAmount(bill.getOweAmount());
			if(bill.getAddress() != null && !bill.getAddress().equals("")){
				CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(communBill.getOrganizationId(), bill.getAddress());
				if(addressMapping != null){
					communBill.setAddress(bill.getAddress());
					communBill.setEntityId(addressMapping.getAddressId());
				}
				else{
					LOGGER.error("update bill failure.because address not found..address="+bill.getAddress());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"update bill failure.because address not found.address="+bill.getAddress());
				}
			}
			this.organizationProvider.updateOrganizationBill(communBill);
		}

	}

	@Override
	public void insertPmBill(InsertPmBillCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getAddress() == null){
			LOGGER.error("propterty organizationId or address paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or address paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Insert failure.Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Insert failure.Unable to find the organization.");
		}
		if(cmd.getAddress() == null || cmd.getAddress().equals("") || cmd.getDueAmount() == null || cmd.getEndDate() == null || 
				cmd.getPayDate() == null || cmd.getStartDate() == null){
			LOGGER.error("address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
		}

		Calendar cal = Calendar.getInstance();
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(new Date().getTime());

		CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(cmd.getOrganizationId(), cmd.getAddress());
		if(addressMapping == null){
			LOGGER.error("Insert failure.the address not find");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Insert failure.the address not find");
		}

		CommunityPmBill bill = ConvertHelper.convert(cmd, CommunityPmBill.class);
		bill.setEntityId(addressMapping.getAddressId());
		bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
		bill.setAddress(addressMapping.getOrganizationAddress());
		bill.setCreatorUid(user.getId());
		bill.setCreateTime(timeStamp);
		bill.setEndDate(new java.sql.Date(cmd.getEndDate()));
		bill.setPayDate(new java.sql.Date(cmd.getPayDate()));
		bill.setStartDate(new java.sql.Date(cmd.getStartDate()));

		cal.setTimeInMillis(bill.getStartDate().getTime());
		StringBuilder builder = new StringBuilder();
		builder.append(cal.get(Calendar.YEAR) +"-");
		if(cal.get(Calendar.MONTH)<9)
			builder.append("0"+(cal.get(Calendar.MONTH)+1));
		else
			builder.append(cal.get(Calendar.MONTH)+1);
		bill.setDateStr(builder.toString());
		bill.setName(bill.getDateStr() + "月账单");
		//往期欠款处理
		if(bill.getOweAmount() == null){
			CommunityPmBill beforeBill = this.propertyMgrProvider.findFamilyNewestBill(bill.getEntityId(), bill.getOrganizationId());
			if(beforeBill != null){
				BigDecimal paidAmount = this.countBillTxAmount(beforeBill.getId());
				BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(paidAmount);
				bill.setOweAmount(oweAmount);
			}
			else
				bill.setOweAmount(BigDecimal.ZERO);
		}
		createPropBill(bill);
	}

	@Override
	public ListOrgBillingTransactionsByConditionsCommandResponse listOrgBillingTransactionsByConditions(ListOrgBillingTransactionsByConditionsCommand cmd) {
		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);

		ListOrgBillingTransactionsByConditionsCommandResponse result = new ListOrgBillingTransactionsByConditionsCommandResponse();
		List<OrganizationBillingTransactionDTO> orgbillTxList = new ArrayList<OrganizationBillingTransactionDTO>();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
		Timestamp startTime = null;
		Timestamp endTime = null;
		if(cmd.getPayDate() != null && !cmd.getPayDate().equals("")){
			startTime = new Timestamp(this.getFirstDayOfMonthByStr(cmd.getPayDate()).getTime());
			endTime =  new Timestamp(this.getLastDayOfMonthByStr(cmd.getPayDate()).getTime());
		}
		orgbillTxList = this.organizationProvider.listOrgBillTxByAddressAndTime(startTime,endTime,cmd.getAddress(),offset,pageSize+1);
		if(orgbillTxList != null && orgbillTxList.size() == pageSize+1){
			orgbillTxList.remove(orgbillTxList.size()-1);
			result.setNextPageOffset(cmd.getPageOffset()+1);
		}
		if(orgbillTxList != null && !orgbillTxList.isEmpty()){
			//设置业主电话
			for(OrganizationBillingTransactionDTO orgbillTx : orgbillTxList){
				List<CommunityPmOwner> orgOwnerList = this.organizationProvider.listOrgOwnerByOrgIdAndAddressId(orgbillTx.getOrganizationId(),orgbillTx.getAddressId());
				if(orgOwnerList != null && !orgOwnerList.isEmpty()){
					orgbillTx.setOwnerTelephone(orgOwnerList.get(0).getContactToken());
				}
			}
		}

		result.setRequests(orgbillTxList);
		return result;
	}

	@Override
	public ListOweFamilysByConditionsCommandResponse listOweFamilysByConditions(ListOweFamilysByConditionsCommand cmd) {
		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}

		ListOweFamilysByConditionsCommandResponse response = new ListOweFamilysByConditionsCommandResponse();
		List<OweFamilyDTO> familyList = new ArrayList<OweFamilyDTO>();

		List<CommunityPmBill> billList = this.propertyMgrProvider.listOweFamilyBillsByOrgIdAndAddress(organization.getId(),cmd.getAddress());
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countBillTxAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0){//该家庭欠费
					OweFamilyDTO family = new OweFamilyDTO();
					family.setAddress(bill.getAddress());
					family.setAddressId(bill.getEntityId());
					family.setBillDescription(bill.getDescription());
					family.setBillId(bill.getId());
					this.setOweFamilyDTOLastTransactionInfo(family,bill.getId());
					family.setOweAmount(waitPayAmount);
					this.setOweFamilyOwnerInfo(family,bill.getEntityId(),bill.getOrganizationId());
					//判断欠费家庭是否在最后缴费时间查询条件范围内
					if(cmd.getLastPayDate() != null && !cmd.getLastPayDate().isEmpty()){
						if(family.getLastPayTime() != null){
							Date startDate = this.getFirstDayOfMonthByStr(cmd.getLastPayDate());
							Date endDate = this.getLastDayOfMonthByStr(cmd.getLastPayDate());
							if(family.getLastPayTime().compareTo(startDate.getTime()) >= 0 && family.getLastPayTime().compareTo(endDate.getTime()) <= 0){
								familyList.add(family);
							}
						}
					}
					else
						familyList.add(family);
				}
			}
		}
		if(familyList != null && !familyList.isEmpty()){
			familyList.sort(new Comparator<OweFamilyDTO>() {
				public int compare(OweFamilyDTO o1, OweFamilyDTO o2){
					if(o1.getLastPayTime() == null || o2.getLastPayTime() == null){
						if(o2.getLastPayTime() != null)
							return 1;
						else if(o1.getLastPayTime() != null)
							return -1;
						else
							return 0;
					}
					return o2.getLastPayTime().compareTo(o1.getLastPayTime());
				}
			});
		}

		response.setRequests(familyList);
		return response;
	}

	private void setOweFamilyOwnerInfo(OweFamilyDTO family, Long addressId, Long organizationId) {
		List<CommunityPmOwner> orgMemberList = this.organizationProvider.findOrganizationMemberByAddressIdAndOrgId(addressId,organizationId);
		if(orgMemberList != null && !orgMemberList.isEmpty()){
			family.setOwnerTelephone(orgMemberList.get(0).getContactToken());
		}
	}

	private void setOweFamilyDTOLastTransactionInfo(OweFamilyDTO family, Long billId) {
		List<OrganizationOrder> orders = this.organizationProvider.listOrganizationOrdersByBillId(billId, OrganizationOrderStatus.PAID.getCode());
		if(orders != null && !orders.isEmpty()){
			OrganizationOrder order = orders.get(0);
			for(int i=1;i<orders.size();i++){
				if(order.getPaidTime().compareTo(orders.get(i).getPaidTime()) < 0)
					order = orders.get(i);
			}
			family.setLastPayTime(order.getPaidTime().getTime());
		}
	}

	@Override
	public ListFamilyBillingTransactionsByFamilyIdCommandResponse listFamilyBillingTransactionByFamilyId(
			ListFamilyBillingTransactionsByFamilyIdCommand cmd) {
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);

		ListFamilyBillingTransactionsByFamilyIdCommandResponse response = new ListFamilyBillingTransactionsByFamilyIdCommandResponse();
		List<FamilyBillingTransactionDTO> transactionList = new ArrayList<FamilyBillingTransactionDTO>();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		Long addresssId = family.getIntegralTag1();
		List<FamilyBillingTransactions> familyTransactionList = this.familyProvider.listFamilyBillingTransactionByAddressId(addresssId,pageSize+1,offset);
		if(familyTransactionList != null && familyTransactionList.size() == pageSize+1){
			response.setNextPageOffset(cmd.getPageOffset()+1);
			familyTransactionList.remove(familyTransactionList.size()-1);
		}
		if(familyTransactionList != null && !familyTransactionList.isEmpty()){
			familyTransactionList.stream().map(r -> {
				FamilyBillingTransactionDTO fBillTxdto = new FamilyBillingTransactionDTO();
				fBillTxdto.setBillType(r.getTxType());
				fBillTxdto.setChargeAmount(r.getChargeAmount().negate());
				fBillTxdto.setCreateTime(r.getCreateTime().getTime());
				fBillTxdto.setDescription(r.getDescription());
				fBillTxdto.setId(r.getId());
				transactionList.add(fBillTxdto);
				return null;
			}).toArray();
		}

		response.setRequests(transactionList);
		return response;
	}

	@Override
	public PmBillsDTO findFamilyBillByFamilyIdAndTime(FindFamilyBillByFamilyIdAndTimeCommand cmd) {
		if(cmd.getFamilyId() == null || cmd.getBillDate() == null || cmd.getBillDate().equals("")){
			LOGGER.error("propterty familyId or billDate paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId or billDate paramter can not be null or empty");
		}

		PmBillsDTO billDto = new PmBillsDTO();

		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		java.sql.Date startDate = null;
		java.sql.Date endDate = null;
		if(cmd.getBillDate() != null && !cmd.getBillDate().equals("")){
			startDate = new java.sql.Date(this.getFirstDayOfMonthByStr(cmd.getBillDate()).getTime());
			endDate =  new java.sql.Date(this.getLastDayOfMonthByStr(cmd.getBillDate()).getTime());
		}
		CommunityPmBill communityBill = this.propertyMgrProvider.findPmBillByAddressAndDate(family.getIntegralTag1(),startDate,endDate);
		if(communityBill != null){
			billDto = ConvertHelper.convert(communityBill, PmBillsDTO.class);
			billDto.setStartDate(communityBill.getStartDate().getTime());
			billDto.setEndDate(communityBill.getEndDate().getTime());
			billDto.setPayDate(communityBill.getPayDate().getTime());
			BigDecimal payedAmount = this.countBillTxAmount(billDto.getId());
			billDto.setPayedAmount(payedAmount);
			billDto.setWaitPayAmount(billDto.getDueAmount().add(billDto.getOweAmount()).subtract(payedAmount));
			billDto.setTotalAmount(billDto.getDueAmount().add(billDto.getOweAmount()));
		}

		return billDto;

	}

	@Override
	public PmBillsDTO findFamilyBillAndPaysByFamilyIdAndTime(FindFamilyBillAndPaysByFamilyIdAndTimeCommand cmd) {
		if(cmd.getFamilyId() == null || cmd.getBillDate() == null || cmd.getBillDate().equals("")){
			LOGGER.error("propterty familyId or billDate paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId or billDate paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}

		PmBillsDTO billDto = new PmBillsDTO();
		java.sql.Date startDate = null;
		java.sql.Date endDate = null;
		if(cmd.getBillDate() != null && !cmd.getBillDate().equals("")){
			startDate = new java.sql.Date(this.getFirstDayOfMonthByStr(cmd.getBillDate()).getTime());
			endDate =  new java.sql.Date(this.getLastDayOfMonthByStr(cmd.getBillDate()).getTime());
		}
		CommunityPmBill communityPmBill = this.propertyMgrProvider.findPmBillByAddressAndDate(family.getIntegralTag1(),startDate,endDate);
		if(communityPmBill != null){
			billDto = this.convertBillToDto(communityPmBill);
			//账单缴费记录
			this.setBillTx(billDto,family.getId());
		}
		return billDto;
	}

	private PmBillsDTO convertBillToDto(CommunityPmBill communityPmBill) {
		PmBillsDTO billDto = ConvertHelper.convert(communityPmBill, PmBillsDTO.class);
		billDto.setStartDate(communityPmBill.getStartDate().getTime());
		billDto.setEndDate(communityPmBill.getEndDate().getTime());
		billDto.setPayDate(communityPmBill.getPayDate().getTime());
		return billDto;
	}

	private void setBillTx(PmBillsDTO billDto,Long familyId) {
		List<FamilyBillingTransactionDTO> payDtoList = new ArrayList<FamilyBillingTransactionDTO>();
		BigDecimal totalAmount = billDto.getDueAmount().add(billDto.getOweAmount());
		BigDecimal payedAmount = BigDecimal.ZERO;
		List<OrganizationOrder> orders = this.organizationProvider.listOrganizationOrdersByBillId(billDto.getId(),OrganizationOrderStatus.PAID.getCode());
		if(orders != null && !orders.isEmpty()){
			for(OrganizationOrder r : orders){
				FamilyBillingTransactions fBillTx = this.familyProvider.findFamilyBillTxByOrderId(r.getId(),familyId);
				if(fBillTx != null){
					FamilyBillingTransactionDTO fBillTxDto = new FamilyBillingTransactionDTO();
					fBillTxDto.setChargeAmount(r.getAmount());
					fBillTxDto.setCreateTime(r.getPaidTime().getTime());
					fBillTxDto.setId(fBillTx.getId());
					fBillTxDto.setBillType(fBillTx.getTxType());
					fBillTxDto.setDescription(fBillTx.getDescription());
					payedAmount = payedAmount.add(r.getAmount());
					payDtoList.add(fBillTxDto);
				}
			}
		}

		if(payDtoList != null && !payDtoList.isEmpty()){
			payDtoList.sort(new Comparator<FamilyBillingTransactionDTO>() {
				public int compare(FamilyBillingTransactionDTO o1,FamilyBillingTransactionDTO o2) {
					if(o1.getCreateTime() == null)
						return -1;
					if(o2.getCreateTime() == null)
						return 1;
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				}
			});
		}

		billDto.setPayedAmount(payedAmount);
		billDto.setWaitPayAmount(totalAmount.subtract(payedAmount));
		billDto.setTotalAmount(totalAmount);
		billDto.setPayList(payDtoList);
	}

	@Override
	public ListFamilyBillsAndPaysByFamilyIdCommandResponse listFamilyBillsAndPaysByFamilyId(
			ListFamilyBillsAndPaysByFamilyIdCommand cmd) {
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		remoteRefreshOrgOrderStatus();
		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

		ListFamilyBillsAndPaysByFamilyIdCommandResponse response = new ListFamilyBillsAndPaysByFamilyIdCommandResponse();
		List<PmBillsDTO> billList = new ArrayList<PmBillsDTO>();
		response.setBillDate(cmd.getBillDate());

		List<CommunityPmBill> commBillList = this.organizationProvider.listOrganizationBillsByAddressId(family.getIntegralTag1(), offset, pageSize+1);
		if(commBillList != null && !commBillList.isEmpty()){
			if(commBillList.size()==pageSize+1){
				commBillList.remove(commBillList.size()-1);
				response.setNextPageOffset(cmd.getPageOffset()+1);
			}
			for(CommunityPmBill commBill : commBillList){
				PmBillsDTO billDto = this.convertBillToDto(commBill);
				//账单缴费记录
				this.setBillTx(billDto,family.getId());
				billList.add(billDto);
			}
		}
		//物业名称和电话
		Organization organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(family.getIntegralTag2(), OrganizationType.PM.getCode());
		if(organization != null){
			response.setOrgName(organization.getName());
			List<CommunityPmContact> contacts = this.propertyMgrProvider.listCommunityPmContacts(organization.getId());
			if(contacts != null && !contacts.isEmpty()){
				for(CommunityPmContact contact : contacts)
					if(contact.getContactType().compareTo(IdentifierType.MOBILE.getCode()) == 0)
						response.setOrgTelephone(contact.getContactToken());
			}
		}
		//左邻名称和电话
		response.setZlName("深圳市永佳天成科技发展有限公司");
		response.setZlTelephone("4008384688");

		response.setRequests(billList);
		return response;
	}

	private BigDecimal countFamilyPmBillDueAndOweAmountInYear(Long orgId,Long addressId) {
		BigDecimal totalDueAmount = this.propertyMgrProvider.countFamilyPmBillDueAmountInYear(orgId,addressId);
		CommunityPmBill bill = this.propertyMgrProvider.findFamilyFirstPmBillInYear(orgId,addressId);
		if(totalDueAmount == null)
			totalDueAmount = BigDecimal.ZERO;
		if(bill != null && bill.getOweAmount() != null)
			totalDueAmount = totalDueAmount.add(bill.getOweAmount());
		return totalDueAmount;

	}

	@Override
	public int payPmBillByFamilyId(PayPmBillByFamilyIdCommand cmd) {
		if(cmd.getPaidType() == null || cmd.getPayAmount() == null || cmd.getPayTime() == null || cmd.getTxType() == null){
			LOGGER.error("propterty paidType or payAmount or payTime or txType paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty paidType or payAmount or payTime or txType paramter can not be null or empty");
		}
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(family.getIntegralTag2(),OrganizationType.PM.getCode());
		if(org == null){
			LOGGER.error("have not pm organization in the community.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"have not pm organization in the community.");
		}
		CommunityPmBill bill = this.propertyMgrProvider.findFamilyNewestBill(family.getIntegralTag1(), org.getId());
		if(bill == null){
			LOGGER.error("the bill is not exist by addressId="+family.getIntegralTag1());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill is not exist");
		}

		User user = UserContext.current().getUser();
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());//当前时间
		Timestamp paidTimeStamp = new Timestamp(cmd.getPayTime());//支付时间
		String uuidStr = UUID.randomUUID().toString();//uuid
		StringBuilder builder = new StringBuilder();
		if(cmd.getDescription() != null && !cmd.getDescription().isEmpty())
			builder.append(cmd.getDescription()+" ");
		if(cmd.getTelephone() != null && !cmd.getTelephone().isEmpty())
			builder.append(cmd.getTelephone()+" ");
		if(cmd.getOwnerName() != null && !cmd.getOwnerName().isEmpty())
			builder.append(cmd.getOwnerName());
		String description = builder.toString();//描述

		this.dbProvider.execute(s -> {
			//创建物业订单
			OrganizationOrder order = new OrganizationOrder();
			order.setAmount(cmd.getPayAmount());
			order.setBillId(bill.getId());
			order.setCreateTime(currentTimestamp);
			order.setDescription(description);
			order.setOwnerId(user.getId());
			order.setPaidTime(paidTimeStamp);
			order.setPayerId(user.getId());
			order.setStatus(OrganizationOrderStatus.PAID.getCode());
			this.organizationProvider.createOrganizationOrder(order);

			FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(bill.getEntityId());
			if(fAccount == null){
				fAccount = new FamilyBillingAccount();
				fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
				fAccount.setBalance(BigDecimal.ZERO);
				fAccount.setCreateTime(currentTimestamp);
				fAccount.setOwnerId(bill.getEntityId());
				this.familyProvider.createFamilyBillingAccount(fAccount);
			}

			OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(bill.getOrganizationId());
			if(oAccount == null){
				oAccount = new OrganizationBillingAccount();
				oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
				oAccount.setBalance(BigDecimal.ZERO);
				oAccount.setCreateTime(currentTimestamp);
				oAccount.setOwnerId(bill.getOrganizationId());
				this.organizationProvider.createOrganizationBillingAccount(oAccount);
			}

			FamilyBillingTransactions familyTx = new FamilyBillingTransactions();
			familyTx.setOrderId(order.getId());
			familyTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			familyTx.setChargeAmount(cmd.getPayAmount().negate());
			familyTx.setCreateTime(paidTimeStamp);
			familyTx.setDescription(builder.toString());
			familyTx.setOperatorUid(user.getId());
			familyTx.setOwnerAccountId(fAccount.getId());
			familyTx.setOwnerId(bill.getEntityId());
			familyTx.setPaidType(cmd.getPaidType());
			familyTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//familyTx.setResultCodeScope("test");
			familyTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			familyTx.setTargetAccountId(oAccount.getId());
			familyTx.setTargetAccountType(AccountType.ORGANIZATION.getCode());
			familyTx.setTxSequence(uuidStr);
			familyTx.setTxType(cmd.getTxType());
			familyTx.setVendor(cmd.getVendor());
			this.familyProvider.createFamilyBillingTransaction(familyTx);

			OrganizationBillingTransactions orgTx = new OrganizationBillingTransactions();
			orgTx.setOrderId(order.getId());
			orgTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			orgTx.setChargeAmount(cmd.getPayAmount());
			orgTx.setCreateTime(paidTimeStamp);
			orgTx.setDescription(builder.toString());
			orgTx.setOperatorUid(user.getId());
			orgTx.setOwnerAccountId(oAccount.getId());
			orgTx.setOwnerId(bill.getOrganizationId());
			orgTx.setPaidType(cmd.getPaidType());
			orgTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//orgTx.setResultCodeScope("test");
			orgTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			orgTx.setTargetAccountId(fAccount.getId());
			orgTx.setTargetAccountType(AccountType.FAMILY.getCode());
			orgTx.setTxSequence(uuidStr);
			orgTx.setTxType(cmd.getTxType());
			orgTx.setVendor(cmd.getVendor());
			this.organizationProvider.createOrganizationBillingTransaction(orgTx);
			return true;
		});

		return 1;
	}

	@Override
	public GetPmPayStatisticsCommandResponse getPmPayStatistics(GetPmPayStatisticsCommand cmd) {
		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}

		GetPmPayStatisticsCommandResponse result = new GetPmPayStatisticsCommandResponse();
		int oweFamilyCount = 0;
		BigDecimal waitPayAmounts = BigDecimal.ZERO;
		BigDecimal yearIncomeAmount = BigDecimal.ZERO;

		List<CommunityPmBill> billList = this.propertyMgrProvider.listOweFamilyBillsByOrgIdAndAddress(cmd.getOrganizationId(), null);
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countBillTxAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0){//该家庭欠费
					waitPayAmounts = waitPayAmounts.add(waitPayAmount);
					oweFamilyCount++;
				}
			}
		}
		yearIncomeAmount = this.propertyMgrProvider.countPmYearIncomeByOrganizationId(cmd.getOrganizationId());
		if(yearIncomeAmount == null)
			yearIncomeAmount = BigDecimal.ZERO;

		result.setOweFamilyCount(oweFamilyCount);
		result.setUnPayAmount(waitPayAmounts);
		result.setYearIncomeAmount(yearIncomeAmount);
		return result;
	}

	@Override
	public void sendPmPayMessageToOneOweFamily(
			SendPmPayMessageToOneOweFamilyCommand cmd) {
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(family.getIntegralTag2(),OrganizationType.PM.getCode());
		if(org == null){
			LOGGER.error("have not pm organization in the community.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"have not pm organization in the community.");
		}
		CommunityPmBill bill = this.propertyMgrProvider.findFamilyNewestBill(family.getIntegralTag1(), org.getId());
		if(bill == null){
			LOGGER.error("the bill is not exist by addressId="+family.getIntegralTag1());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill is not exist");
		}

		BigDecimal payAmount = this.countBillTxAmount(bill.getId());
		BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(payAmount);
		if(waitPayAmount.compareTo(BigDecimal.ZERO) <= 0){//不欠费
			LOGGER.error("The family don't owed pm fee.Should not send pm pay message.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"The family don't owed pm fee.Should not send pm pay message.");
		}
		//短信发送物业缴费通知
		this.dbProvider.execute(s -> {
			String message = this.getPmPayMessage(bill,waitPayAmount,payAmount);
			this.sendPmPayMessageToUnRegisterUserInFamily(org.getId(),family.getIntegralTag1(),message);
			bill.setNotifyCount((bill.getNotifyCount() == null?0:bill.getNotifyCount())+1);
			bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
			this.organizationProvider.updateOrganizationBill(bill);

			return s;
		});
	}

	private void sendPmPayMessageToUnRegisterUserInFamily(Long organizationId,Long addressId,String message) {
		List<OrganizationOwners> orgOwnerList = this.organizationProvider.listOrganizationOwnersByOrgIdAndAddressId(organizationId,addressId);
		if(orgOwnerList != null && !orgOwnerList.isEmpty()){
			for(OrganizationOwners orgOwner : orgOwnerList){
				if(orgOwner.getContactToken() != null)
					smsProvider.sendSms(orgOwner.getContactToken(), message);
			}
		}
	}

	@Override
	public void sendPmPayMessageToAllOweFamilies(SendPmPayMessageToAllOweFamiliesCommand cmd) {
		if(cmd.getOrganizationId() == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}

		List<CommunityPmBill> billList = this.propertyMgrProvider.listOweFamilyBillsByOrgIdAndAddress(organization.getId(),null);
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countBillTxAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0){//欠费
					Family family = this.familyProvider.findFamilyByAddressId(bill.getEntityId());
					if(family != null){
						//短信发送物业缴费通知
						this.dbProvider.execute(s -> {
							String message = this.getPmPayMessage(bill, waitPayAmount, paidAmount);
							this.sendPmPayMessageToUnRegisterUserInFamily(organization.getId(),family.getIntegralTag1(),message);
							bill.setNotifyCount((bill.getNotifyCount() == null?0:bill.getNotifyCount())+1);
							bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
							this.organizationProvider.updateOrganizationBill(bill);
							return s;
						});
					}
				}
			}

		}
	}

	private String getPmPayMessage(CommunityPmBill bill, BigDecimal balance, BigDecimal payAmount){
		//String str = "您2015年07月物业账单为，本月金额：300.00,往期欠款：200.00，本月实付金额：100.00，应付金额：400.00 ，+ 账单说明。 请尽快使用左邻缴纳物业费。";
		StringBuilder builder = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bill.getStartDate());
		builder.append("您"+cal.get(Calendar.YEAR)+"年");
		int month = cal.get(Calendar.MONTH)+1;
		if(month < 10)
			builder.append("0"+month+"月");
		else
			builder.append(month+"月");
		builder.append("物业账单为，本月金额：");
		builder.append(bill.getDueAmount());
		builder.append("，往期欠款：");
		builder.append(bill.getOweAmount());
		builder.append("，本月实付金额：");
		builder.append(payAmount);
		builder.append("，应付金额：");
		builder.append(balance);
		if(bill.getDescription() != null && !bill.getDescription().isEmpty())
			builder.append("，"+bill.getDescription());
		builder.append("。");
		builder.append("请尽快使用左邻缴纳物业费。");
		return builder.toString();
	}

	@Override
	public GetFamilyStatisticCommandResponse getFamilyStatistic(
			GetFamilyStatisticCommand cmd) {
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(family.getIntegralTag2(), OrganizationType.PM.getCode());
		if(org == null){
			LOGGER.error("the family PM organization is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family PM organization is not exist");
		}

		GetFamilyStatisticCommandResponse response = new GetFamilyStatisticCommandResponse();
		BigDecimal totalDueOweAmount = this.countFamilyPmBillDueAndOweAmountInYear(org.getId(),family.getIntegralTag1());
		BigDecimal totalPaidAmount = this.familyProvider.countFamilyBillTxChargeAmountInYear(family.getIntegralTag1());
		BigDecimal nowWaitPayAmount = BigDecimal.ZERO;

		CommunityPmBill bill = this.propertyMgrProvider.findFamilyNewestBill(family.getIntegralTag1(), org.getId());
		if(bill != null){
			BigDecimal paidAmount = this.countBillTxAmount(bill.getId());
			nowWaitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
		}

		response.setNowWaitPayAmount(nowWaitPayAmount);
		response.setTotalDueOweAmount(totalDueOweAmount);
		response.setTotalPaidAmount(totalPaidAmount.negate());
		return response;
	}

	@Override
	public PmBillsDTO findFamilyNewestBillByFamilyId(FindFamilyNewestBillByFamilyIdCommand cmd) {
		if(cmd.getFamilyId() == null){
			LOGGER.error("propterty familyId paramter can not be null or empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty familyId paramter can not be null or empty");
		}
		Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(family.getIntegralTag2(), OrganizationType.PM.getCode());
		if(org == null){
			LOGGER.error("Unable to find the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		PmBillsDTO billDto = new PmBillsDTO();

		CommunityPmBill communityBill = this.propertyMgrProvider.findFamilyNewestBill(family.getIntegralTag1(), org.getId());
		if(communityBill != null){
			billDto = ConvertHelper.convert(communityBill, PmBillsDTO.class);
			BigDecimal payedAmount = this.countBillTxAmount(billDto.getId());
			billDto.setPayedAmount(payedAmount);
			billDto.setWaitPayAmount(billDto.getDueAmount().add(billDto.getOweAmount()).subtract(payedAmount));
			billDto.setTotalAmount(billDto.getDueAmount().add(billDto.getOweAmount()));
		}

		return billDto;
	}

	@Override
	public void onlinePayPmBill(OnlinePayPmBillCommand cmd) {
		//fail
		if(cmd.getPayStatus().equals("fail"))
			this.onlinePayPmBillFail(cmd);
		//success
		if(cmd.getPayStatus().equals("success"))
			this.onlinePayPmBillSuccess(cmd);
	}

	private void onlinePayPmBillFail(OnlinePayPmBillCommand cmd) {
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);

		Date cunnentTime = new Date();
		Timestamp timestamp = new Timestamp(cunnentTime.getTime());

		this.updateOrderStatus(order,0L,timestamp,OrganizationOrderStatus.INACTIVE.getCode());
	}

	private void onlinePayPmBillSuccess(OnlinePayPmBillCommand cmd) {

		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());
		this.checkPayTimeIsNull(cmd.getPayTime());
		this.checkPayAccountIsNull(cmd.getPayAccount());

		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);
		BigDecimal waitPayAmount = new BigDecimal(cmd.getPayAmount());//支付金额
		//this.checkAmountIsEqual(order.getAmount(),waitPayAmount);
		CommunityPmBill bill = this.checkPmBill(order.getBillId());
		this.checkVendorTypeFormat(cmd.getVendorType());
		CommunityPmBill bill2 = this.propertyMgrProvider.findFamilyNewestBill(bill.getEntityId(), bill.getOrganizationId());
		if(bill2 == null || bill2.getId().compareTo(bill.getId()) != 0){
			LOGGER.error("the bill is invalid.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
					"the bill is invalid.");
		}
		
		Long payTime = Long.valueOf(cmd.getPayTime());
		Timestamp payTimeStamp = new Timestamp(payTime);//支付时间

		Date cunnentTime = new Date();
		Timestamp timestamp = new Timestamp(cunnentTime.getTime());

		this.dbProvider.execute(s -> {
			order.setAmount(waitPayAmount);
			this.updateOrderStatus(order,0L,payTimeStamp,OrganizationOrderStatus.PAID.getCode());

			FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(bill.getEntityId());
			if(fAccount == null)	fAccount = this.newFBillAcount(bill.getEntityId(),timestamp);

			OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(bill.getOrganizationId());
			if(oAccount == null)	oAccount = this.newOBillAccount(bill.getOrganizationId(),timestamp);

			String uuidStr = UUID.randomUUID().toString();

			FamilyBillingTransactions familyTx = new FamilyBillingTransactions();
			familyTx.setOrderId(order.getId());
			familyTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			familyTx.setChargeAmount(waitPayAmount.negate());
			familyTx.setCreateTime(payTimeStamp);
			if(cmd.getDescription() != null && !cmd.getDescription().isEmpty())
				familyTx.setDescription(cmd.getDescription());
			familyTx.setOperatorUid(0L);
			familyTx.setOwnerAccountId(fAccount.getId());
			familyTx.setOwnerId(bill.getEntityId());
			familyTx.setPaidType(PaidType.SELFPAY.getCode());
			familyTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//familyTx.setResultCodeScope("test");
			familyTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			familyTx.setTargetAccountId(oAccount.getId());
			familyTx.setTargetAccountType(AccountType.ORGANIZATION.getCode());
			familyTx.setTxSequence(uuidStr);
			familyTx.setTxType(TxType.ONLINE.getCode());
			familyTx.setVendor(cmd.getVendorType());
			familyTx.setPayAccount(cmd.getPayAccount());
			this.familyProvider.createFamilyBillingTransaction(familyTx);

			OrganizationBillingTransactions orgTx = new OrganizationBillingTransactions();
			orgTx.setOrderId(order.getId());
			orgTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			orgTx.setChargeAmount(waitPayAmount);
			orgTx.setCreateTime(payTimeStamp);
			if(cmd.getDescription() != null && !cmd.getDescription().isEmpty())
				orgTx.setDescription(cmd.getDescription());
			orgTx.setOperatorUid(0L);
			orgTx.setOwnerAccountId(oAccount.getId());
			orgTx.setOwnerId(bill.getOrganizationId());
			orgTx.setPaidType(PaidType.SELFPAY.getCode());
			orgTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//orgTx.setResultCodeScope("test");
			orgTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			orgTx.setTargetAccountId(fAccount.getId());
			orgTx.setTargetAccountType(AccountType.FAMILY.getCode());
			orgTx.setTxSequence(uuidStr);
			orgTx.setTxType(TxType.ONLINE.getCode());
			orgTx.setVendor(cmd.getVendorType());
			orgTx.setPayAccount(cmd.getPayAccount());
			this.organizationProvider.createOrganizationBillingTransaction(orgTx);

			//线上支付,将金额存到物业账号中
			oAccount.setBalance(oAccount.getBalance().add(waitPayAmount));
			oAccount.setUpdateTime(timestamp);
			this.organizationProvider.updateOrganizationBillingAccount(oAccount);

			return true;
		});

	}

	private OrganizationBillingAccount newOBillAccount(Long orgId,Timestamp timestamp) {
		OrganizationBillingAccount oAccount = new OrganizationBillingAccount();
		oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
		oAccount.setBalance(BigDecimal.ZERO);
		oAccount.setCreateTime(timestamp);
		oAccount.setOwnerId(orgId);
		this.organizationProvider.createOrganizationBillingAccount(oAccount);
		return oAccount;
	}

	private FamilyBillingAccount newFBillAcount(Long entityId,Timestamp timestamp) {
		FamilyBillingAccount fAccount = new FamilyBillingAccount();
		fAccount = new FamilyBillingAccount();
		fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
		fAccount.setBalance(BigDecimal.ZERO);
		fAccount.setCreateTime(timestamp);
		fAccount.setOwnerId(entityId);
		this.familyProvider.createFamilyBillingAccount(fAccount);
		return fAccount;
	}

	private void updateOrderStatus(OrganizationOrder order, long payerId,Timestamp payTimeStamp, byte code) {
		order.setPayerId(0L);
		order.setPaidTime(payTimeStamp);
		order.setStatus(code);
		this.organizationProvider.updateOrganizationOrder(order);
	}

	private void checkVendorTypeFormat(String vendorType) {
		if(VendorType.fromCode(vendorType) == null){
			LOGGER.error("vendor type is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}

	}

	private CommunityPmBill checkPmBill(Long billId) {
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(billId);
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}
		return bill;
	}

	private void checkAmountIsEqual(BigDecimal amount, BigDecimal waitPayAmount) {
		if(amount.compareTo(waitPayAmount) != 0){
			LOGGER.error("order amount not equal payAmount.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"order amount not equal payAmount.");
		}

	}

	private OrganizationOrder checkOrder(Long orderId) {
		OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
		if(order == null){
			LOGGER.error("the order not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}

	private void checkPayAccountIsNull(String payAccount) {
		if(payAccount == null || payAccount.trim().equals("")){
			LOGGER.error("payAccount is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAccount is null or empty.");
		}

	}

	private void checkPayTimeIsNull(String payTime) {
		if(payTime == null || payTime.trim().equals("")){
			LOGGER.error("payTime is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payTime is null or empty.");
		}

	}

	private void checkPayAmountIsNull(String payAmount) {
		if(payAmount == null || payAmount.trim().equals("")){
			LOGGER.error("payAmount is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAmount or is null or empty.");
		}

	}

	private void checkVendorTypeIsNull(String vendorType) {
		if(vendorType == null || vendorType.trim().equals("")){
			LOGGER.error("vendorType is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendorType is null or empty.");
		}

	}

	private void checkOrderNoIsNull(String orderNo) {
		if(orderNo == null || orderNo.trim().equals("")){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}

	}

	private Long convertOrderNoToOrderId(String orderNo) {
		return Long.valueOf(orderNo);
	}

	private String convertOrderIdToOrderNo(Long orderId) {
		return Long.toString(orderId);
	}

	@Override
	public PmBillForOrderNoDTO findPmBillByOrderNo(FindPmBillByOrderNoCommand cmd) {
		if(cmd.getOrderNo() == null || cmd.getOrderNo().isEmpty()){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
		if(order == null){
			LOGGER.error("the order not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(order.getBillId());
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}

		BigDecimal payedAmount = this.countBillTxAmount(bill.getId());
		BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(payedAmount);

		PmBillForOrderNoDTO billDto = new PmBillForOrderNoDTO();
		billDto.setBillName(bill.getName());
		billDto.setPayAmount(waitPayAmount);
		return billDto;
	}

	@Override
	public OrganizationOrderDTO createPmBillOrder(CreatePmBillOrderCommand cmd) {
		if(cmd.getBillId() == null || cmd.getAmount() == null){
			LOGGER.error("billId or amount is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"billId or amount is null.");
		}
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(cmd.getBillId());
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}
		CommunityPmBill nowBill = this.propertyMgrProvider.findFamilyNewestBill(bill.getEntityId(), bill.getOrganizationId());
		if(nowBill == null || nowBill.getId().compareTo(bill.getId()) != 0){
			LOGGER.error("the bill is invalid.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill is invalid.");
		}

		User user = UserContext.current().getUser();
		OrganizationOrder order = new OrganizationOrder();
		order.setAmount(cmd.getAmount());
		order.setBillId(bill.getId());
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		if(cmd.getDescription() != null && !cmd.getDescription().equals(""))
			order.setDescription(cmd.getDescription());
		order.setOwnerId(user.getId());
		order.setPayerId(user.getId());
		order.setStatus(OrganizationOrderStatus.WAITING_FOR_PAY.getCode());
		this.organizationProvider.createOrganizationOrder(order);

		OrganizationOrderDTO dto = new OrganizationOrderDTO();
		String orderNo = this.convertOrderIdToOrderNo(order.getId());
		dto.setOrderNo(orderNo);
		dto.setAmount(cmd.getAmount());
		dto.setName(bill.getName());
		dto.setDescription(order.getDescription());
		return dto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public void remoteUpdateOrgOrderByOrderNo(String orderNo) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String restUrl = "http://alitestserver.zuolin.com:8081/EDS_PAY/rest/pay_common/payInfo_record/get_payInfo_record_by_orderNo_and_orderType";

		try {
			BaseVo<Map> bvo=new BaseVo<Map>();
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderNo", orderNo);
			map.put("orderType", "wuye");
			bvo.setBody(map);
			String json=RestUtil.restWan(GsonUtil.toJson(bvo), restUrl);

			ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
			this.checkResultHolderIsNull(resultHolder,orderNo);
			if(resultHolder.isSuccess()){
				Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
				String payStatus = (String) data.get("payStatus");//waiting:未支付、succes:支付成功、fail:支付失败
				this.checkPayStatusIsNull(payStatus,orderNo);
				if(payStatus.equals("fail")){
					OnlinePayPmBillCommand command = new OnlinePayPmBillCommand();
					command.setPayStatus(payStatus);
					command.setOrderNo(orderNo);
					this.onlinePayPmBill(command);
				}
				else if(payStatus.equals("success")){
					//verdorType
					String verdorTypeStr = (String) data.get("onlinePayStyleNo");//alipay、支付宝  wechat、微信
					String verdorType = this.convertToVerdorType(verdorTypeStr);
					//amount
					Double amountDouble = (Double) data.get("payAmount");
					String amount = Double.toString(amountDouble);
					//patTime
					String payTimeStr = (String) data.get("payDate");
					String payTime = Long.toString(format.parse(payTimeStr).getTime());
					//patAccount
					String payAccount = (String) data.get("payAccount");
					if(payAccount == null)
						payAccount = "test account";
					String payObj = "{testPayObj}";

					OnlinePayPmBillCommand command = new OnlinePayPmBillCommand();
					command.setPayStatus(payStatus);
					command.setOrderNo(orderNo);
					command.setPayAccount(payAccount);
					command.setPayAmount(amount);
					command.setPayObj(payObj);
					command.setPayTime(payTime);
					command.setVendorType(verdorType);
					this.onlinePayPmBill(command);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					e.getMessage());
		}
	}

	private String convertToVerdorType(String verdorTypeStr) {
		if(verdorTypeStr.equals("alipay"))
			return "10001";
		return "10002";
	}

	private void checkPayStatusIsNull(String payStatus,String orderNo) {
		if(payStatus == null){
			LOGGER.error("payStatus is null.orderNo="+orderNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payStatus is null.");
		}
	}

	private void checkResultHolderIsNull(ResultHolder resultHolder,String orderNo) {
		if(resultHolder == null){
			LOGGER.error("remote search pay order return null.orderNo="+orderNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"remote search pay order return null.");
		}
	}

}
