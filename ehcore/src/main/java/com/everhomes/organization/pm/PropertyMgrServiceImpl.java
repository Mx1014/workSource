// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.acl.Role;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.*;
import com.everhomes.entity.EntityType;
import com.everhomes.family.*;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.*;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.promotion.PromotionService;
import com.everhomes.pushmessage.*;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.address.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapType;
import com.everhomes.rest.family.*;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.GetRequestInfoCommand;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.OrganizationOwnerCarSearcher;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerCars;
import com.everhomes.server.schema.tables.pojos.EhParkingCardCategories;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.ProcessBillModel1;
import com.everhomes.util.excel.handler.PropMgrBillHandler;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import net.greghaines.jesque.Job;
import org.apache.poi.ss.usermodel.*;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Null;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 物业和组织共用同一张表。所有的逻辑都由以前的communityId 转移到 organizationId。
 */
@Component 
public class PropertyMgrServiceImpl implements PropertyMgrService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);
	private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
	private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";
	private static final String PROP_MESSAGE_BILL = "prop.message.bill";
	
	@Autowired
    JesqueClientFactory jesqueClientFactory;
	
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

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
	private  ForumService forumService;

	@Autowired
	private  OrganizationProvider organizationProvider;

	@Autowired
	private  OrganizationService organizationService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private AuditLogProvider auditLogProvider;

	@Autowired
	private EnterpriseProvider enterpriseProvider;

	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;

	@Autowired
	private AppProvider appProvider;
	
	@Autowired
	private OrderUtil commonOrderUtil;
	
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    private PromotionService promotionService;
    
    @Autowired
    private ContentServerService contentServerService;
    
	@Autowired
	private PMOwnerSearcher pmOwnerSearcher;

	@Autowired
	private OrganizationOwnerCarSearcher ownerCarSearcher;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private PushMessageProvider pushMessageProvider;

    @Autowired
    private PushMessageResultProvider pushMessageResultProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private GroupMemberLogProvider groupMemberLogProvider;

	@Autowired
	private FieldProvider fieldProvider;
    
    private String queueName = "property-mgr-push";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }

	@Override
	public void applyPropertyMember(applyPropertyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		Long communityId = user.getCommunityId();
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		this.checkCommunityIdIsEqual(cmd.getCommunityId().longValue(), communityId.longValue());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		long organizationId = org.getId();
		this.checkUserInOrg(user.getId(),organizationId);
		CommunityPmMember communityPmMember = this.createCommunityPmMember(organizationId,cmd.getContactDescription(),user);
		propertyMgrProvider.createPropMember(communityPmMember);
	}

	private void checkUserInOrg(Long userId, Long orgId) {
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(member != null){
			LOGGER.error("User is in the organization.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"User is in the organization.");
		}
	}

	private void checkCurrentUserNotInOrg(Long orgId) {
        /*Long userId = UserContext.current().getUser().getId();
		if (orgId == null) {
			LOGGER.error("Invalid parameter organizationId [ {} ]", orgId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter organizationId [ %s ]", orgId);
		}
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
		if(member == null){
			LOGGER.error("User is not in the organization.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"User is not in the organization.");
		}*/
	}

	public CommunityPmMember createCommunityPmMember(Long orgId,String description,User user) {

		CommunityPmMember communityPmMember = new CommunityPmMember();
		communityPmMember.setOrganizationId(orgId);
		communityPmMember.setContactName(user.getNickName());
		communityPmMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		communityPmMember.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
		communityPmMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
		communityPmMember.setTargetId(user.getId());
		communityPmMember.setContactDescription(description);
		UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
		if(identifier != null){
			communityPmMember.setContactToken(identifier.getIdentifierToken());
			communityPmMember.setContactType(identifier.getIdentifierType());
		}
		return communityPmMember;
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
	public void createPropMember(CreatePropMemberCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		this.convertCreatePropMemberCommand(cmd);
		//权限控制
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		Long organizationId = org.getId();
		cmd.setCommunityId(organizationId);
		//先判断，如果不属于这个小区的物业，才添加物业成员。状态直接设为正常
		Long addUserId =  cmd.getTargetId();
		//添加已注册用户为管理员。
		if(addUserId != null && addUserId != 0){
			List<CommunityPmMember> list = propertyMgrProvider.findPmMemberByCommunityAndTarget(cmd.getCommunityId(), cmd.getTargetType(), cmd.getTargetId());
			if(list == null || list.size() == 0) {
				if(cmd.getContactName() == null || cmd.getContactName().trim().equals("")){
					User user = this.userProvider.findUserById(cmd.getTargetId());
					cmd.setContactName(user.getNickName());
				}
				OrganizationMemberGroupType memberGroup = OrganizationMemberGroupType.fromCode(cmd.getMemberGroup());
				cmd.setMemberGroup(memberGroup.getCode());
				CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
				communityPmMember.setOrganizationId(organizationId);
				communityPmMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
				propertyMgrProvider.createPropMember(communityPmMember);
			}
		}
	}

	private void convertCreatePropMemberCommand(CreatePropMemberCommand cmd) {
		cmd.setTargetType(cmd.getTargetType().toUpperCase());
	}

	@Override
	public ListPropMemberCommandResponse getUserOwningProperties() {
		ListPropMemberCommandResponse commandResponse = new ListPropMemberCommandResponse();
		User user  = UserContext.current().getUser();

		List<CommunityPmMember> entityResultList = propertyMgrProvider.listUserCommunityPmMembers(user.getId());
		List<PropertyMemberDTO> members =  new ArrayList<PropertyMemberDTO>();
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
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		Community community = this.checkCommunity(cmd.getCommunityId());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		Long organizationId = org.getId();

		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = propertyMgrProvider.countCommunityPmMembers(organizationId, null);
		if(totalCount == 0) return commandResponse;

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(organizationId, null, cmd.getPageOffset(),pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ 
					PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
					Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
					if(organization != null){
						dto.setOrganizationName(organization.getName());
						if(OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM){
							dto.setCommunityId(community.getId());
							dto.setCommunityName(community.getName());
						}
					}
					return dto; 
				}).collect(Collectors.toList()));
		commandResponse.setPageCount(pageCount);
		return commandResponse;
	}

	@Override
	public void importPMAddressMapping(PropCommunityIdCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		Community community = this.checkCommunity(cmd.getCommunityId());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		long organizationId = org.getId();
		List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(organizationId, 1, 10);
		if(entityResultList == null || entityResultList.size() == 0) {
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
					m.setLivingStatus(AddressMappingStatus.DEFAULT.getCode());
					propertyMgrProvider.createPropAddressMapping(m);
				}
			}
		}

	}

	@Override
	public ListPropAddressMappingCommandResponse listAddressMappings(ListPropAddressMappingCommand cmd) {
		ListPropAddressMappingCommandResponse commandResponse = new ListPropAddressMappingCommandResponse();
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		long organizationId = org.getId();

		int totalCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, null, null);
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);

		List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(organizationId, cmd.getPageOffset(), pageSize);
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ 
					PropAddressMappingDTO dto = ConvertHelper.convert(r, PropAddressMappingDTO.class);
					Address address = addressProvider.findAddressById(dto.getAddressId());
					if(address != null)
						dto.setAddressName(address.getAddress());
					return  dto;
				}).collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}

	@Override
	public ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd) {
		ListPropBillCommandResponse commandResponse = new ListPropBillCommandResponse();
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}
		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		int totalCount = propertyMgrProvider.countCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
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
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		Community community = this.checkCommunity(cmd.getCommunityId());

		ListPropOwnerCommandResponse commandResponse = new ListPropOwnerCommandResponse();
		Organization org = this.checkOrganizationByCommIdAndOrgType(community.getId(),OrganizationType.PM.getCode());
		long organizationId = org.getId();

		int totalCount = propertyMgrProvider.countCommunityPmOwners(organizationId,cmd.getAddress(),cmd.getContactToken());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int pageCount = getPageCount(totalCount, pageSize);

		List<CommunityPmOwner> entityResultList = propertyMgrProvider.listCommunityPmOwners(organizationId,cmd.getAddress(),cmd.getContactToken(), cmd.getPageOffset(), pageSize);
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
		//		String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
		List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
		userList.stream().filter((u) -> {
			if(u.getIdentifierType() != IdentifierType.MOBILE.getCode())
				return false;
			return true;
		});
		if(userList == null || userList.isEmpty()) return ;
		String cellPhone = userList.get(0).getIdentifierToken();
		//		if(StringUtils.isEmpty(template)){
		//			template = "该物业已在处理";
		//		}
		//		if (!StringUtils.isEmpty(template)) {
		//			this.smsProvider.sendSms(cellPhone, template);
		//		}
		String templateScope = SmsTemplateCode.SCOPE;
		int templateId = SmsTemplateCode.VERIFICATION_CODE;
		String templateLocale = currentLocale();
		smsProvider.sendSms( userList.get(0).getNamespaceId(), cellPhone, templateScope, templateId, templateLocale, null);
	}

	@Override
	public ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());

		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		if(cmd.getPageSize() == null)
			cmd.setPageSize(20L);

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize().intValue());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

		ListPropInvitedUserCommandResponse response = new ListPropInvitedUserCommandResponse();
		List<PropInvitedUserDTO> users = new ArrayList<PropInvitedUserDTO>();

		List<OrganizationMember> result = this.organizationProvider.listOrganizationMembers(org.getId(), null, offset, pageSize+1);
		if(result != null && !result.isEmpty()){
			if(result.size() == pageSize+1){
				result.remove(result.size()-1);
				response.setNextPageAnchor(cmd.getPageOffset()+1);
			}
			for(OrganizationMember r : result){
				PropInvitedUserDTO user = new PropInvitedUserDTO();
				user.setContactType(r.getContactType());
				user.setContactToken(r.getContactToken());
				user.setInvitorName(r.getContactName());

				if(r.getTargetId() != null && r.getTargetId().longValue() != 0L){
					User u = this.userProvider.findUserById(r.getTargetId());
					user.setUserId(u.getId());
					user.setUserName(u.getAccountName());
					user.setInviteType(u.getInviteType());
					user.setRegisterTime(u.getCreateTime());
					user.setInvitorId(u.getInvitorUid());
				}
				users.add(user);
			}
		}

		response.setMembers(users);
		return response;
	}

	@Override
	public void approvePropMember(CommunityPropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getCommunityId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
		{
			LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
		{
			LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}
		//权限控制--admin角色
		CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
		if(communityPmMember == null)
		{
			LOGGER.error("Unable to find the property member.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Group family = this.checkFamily(cmd.getFamilyId());
		this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(),cmd.getCommunityId().longValue());

		ApproveMemberCommand comand = new ApproveMemberCommand();
		comand.setId(cmd.getFamilyId());
		comand.setMemberUid(cmd.getUserId());
		comand.setOperatorRole(Role.SystemAdmin);
		familyService.approveMember(comand);
	}

	private void checkCommunityIdIsEqual(long longValue, long longValue2) {
		if(longValue != longValue2){
			LOGGER.error("communityId not equal.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"communityId not equal.");
		}
	}

	@Override
	public void rejectPropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		Group family = this.checkFamily(cmd.getFamilyId());
		this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(),cmd.getCommunityId().longValue());

		String reason = "";

		RejectMemberCommand command = new RejectMemberCommand();
		command.setId(cmd.getFamilyId());
		command.setMemberUid(cmd.getUserId());
		command.setOperatorRole(Role.SystemAdmin);
		command.setReason(reason);
		familyService.rejectMember(command );
	}

	private void checkCommunityIdIsNull(Long communityId) {
		if(communityId == null){
			LOGGER.error("communityId paramter is empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"communityId paramter is empty");
		}

	}

	@Override
	public void revokePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		this.checkFamilyIdIsNull(cmd.getFamilyId());
		Group family = this.checkFamily(cmd.getFamilyId());
		this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(),cmd.getCommunityId().longValue());

		String reason = "";
		RevokeMemberCommand comand = new RevokeMemberCommand();
		comand.setId(cmd.getFamilyId());
		comand.setMemberUid(cmd.getUserId());
		comand.setOperatorRole(Role.ResourceAdmin);
		comand.setReason(reason);
		familyService.revokeMember(comand );
	}

	@Override
	public Tuple<Integer, List<BuildingDTO>> listPropBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		return addressService.listBuildingsByKeyword(cmd);
	}

	@Override
	public UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd) {
		User operator = UserContext.current().getUser();
		Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
		if(operator != null) {
			namespaceId = operator.getNamespaceId();
		}

		UserTokenCommandResponse commandResponse = new UserTokenCommandResponse();
		User user = userService.findUserByIndentifier(namespaceId, cmd.getUserIdentifier());
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
		userService.setUserCurrentCommunity(cmd.getCommunityId());
	}

	@Override
	public ListPropFamilyWaitingMemberCommandResponse listPropFamilyWaitingMember(ListPropFamilyWaitingMemberCommand cmd) {
		ListPropFamilyWaitingMemberCommandResponse commandResponse = new ListPropFamilyWaitingMemberCommandResponse();
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());

		int totalCount = familyProvider.countWaitApproveFamily(cmd.getCommunityId());
		if(totalCount == 0) return commandResponse;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		long offset = PaginationHelper.offsetFromPageOffset(Long.valueOf(cmd.getPageOffset()), pageSize);
		int pageCount = getPageCount(totalCount, pageSize);

		List<FamilyDTO>  entityResultList = familyProvider.listWaitApproveFamily(cmd.getCommunityId(), offset, new Long(pageSize));
		commandResponse.setMembers( entityResultList.stream()
				.map(r->{ return r; })
				.collect(Collectors.toList()));
		commandResponse.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
		return commandResponse;
	}

	@Override
	public void setApartmentStatus(SetPropAddressStatusCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getAddressId() == null || cmd.getStatus() == null){
			LOGGER.error("propterty organizationId or addressId or status paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or addressId or status paramter can not be null or empty");
		}

		this.checkOrganization(cmd.getOrganizationId());
		CommunityAddressMapping mapping = this.checkAddressMappingByAddressId(cmd.getAddressId());

		mapping.setLivingStatus(cmd.getStatus());
		propertyMgrProvider.updateOrganizationAddressMapping(mapping);
	}

	private CommunityAddressMapping checkAddressMappingByAddressId(Long addressId) {
		CommunityAddressMapping mapping = this.propertyMgrProvider.findAddressMappingByAddressId(addressId);
		if(mapping == null){
			LOGGER.error("address mapping is not find.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address mapping is not find.");
		}
		return mapping;
	}

	@Override
	public PropFamilyDTO findFamilyByAddressId(ListPropCommunityAddressCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkCommunity(cmd.getCommunityId());
		Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
		if(family == null) {
			LOGGER.error("family is not existed.communityId=" + cmd.getCommunityId()+",addressId=" + cmd.getAddressId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the family.");
		}
		this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(), cmd.getCommunityId().longValue());

		PropFamilyDTO dto = new PropFamilyDTO();
		dto.setName(family.getDisplayName());
		dto.setId(family.getId());
		dto.setMemberCount(family.getMemberCount());
		dto.setAddressId(family.getAddressId());
		dto.setAddress(family.getName());
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(),OrganizationType.PM.getCode());
		CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(org.getId(), cmd.getAddressId());
		if(mapping != null){
			dto.setLivingStatus(mapping.getLivingStatus());
		}
		else{
			dto.setLivingStatus(AddressMappingStatus.LIVING.getCode());
		}
		return dto;
	}

	private Organization checkOrganizationByCommIdAndOrgType(Long communityId,String orgType) {
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, orgType);
		if(org == null) {
			LOGGER.error("organization can not find by communityId and orgType.communityId="+communityId+",orgType="+orgType);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organization can not find by communityId and orgType.");
		}
		return org;
	}

	@Override
	public void importPropertyBills(@Valid PropCommunityIdCommand cmd, MultipartFile[] files) {
		User user  = UserContext.current().getUser();
		Long communityId = cmd.getCommunityId();
		if(communityId == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}

		ArrayList resultList = processorExcel(files[0]);
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
				this.createPropBill(bill);
			}
		}
	}

	@Override
	public void createPropBill(CommunityPmBill bill) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		this.dbProvider.execute((status) ->{
			propertyMgrProvider.createPropBill(bill);
			List<CommunityPmBillItem> itemList =  bill.getItemList();
			if(itemList != null && itemList.size() > 0){
				for (CommunityPmBillItem communityPmBillItem : itemList) {
					communityPmBillItem.setBillId(bill.getId());
					communityPmBillItem.setCreateTime(currentTimeStamp);
					communityPmBillItem.setCreatorUid(user.getId());
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
						sendNoticeToFamilyById(family.getId(), message, MessageBodyType.TEXT.getCode());
					}
				}
			}
		}

	}

	public String buildBillMessage(CommunityPmBill bill) {
		String template = configurationProvider.getValue(PROP_MESSAGE_BILL, "");
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
			throw errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_BILL,
					"Unable to find the bill.");
		}
		Address address = addressProvider.findAddressById(bill.getEntityId());
		if(address == null)
		{
			LOGGER.error("Unable to find the address.addressId=" + bill.getEntityId());
			throw errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_ADDRESS,
					"Unable to find the address.");
		}
		Family family = familyProvider.findFamilyByAddressId(address.getId());
		if(family == null)
		{
			LOGGER.error("Unable to find the family.familyId=" +address.getId());
			throw errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_INVALID_FAMILY,
					"Unable to find the family.");
		}
		String message = buildBillMessage(bill);
		sendNoticeToFamilyById(family.getId(), message, MessageBodyType.TEXT.getCode());
	}

	@Override
	public void sendNoticeToFamily(PropCommunityBuildAddessCommand cmd) {

		this.checkCommunityIdIsNull(cmd.getCommunityId());

		/*List<String> buildingNames = cmd.getBuildingNames();
		List<Long> buildingIds = cmd.getBuildingIds();
		List<Long> addressIds = cmd.getAddressIds();
		List<String> phones = cmd.getMobilePhones();
		Integer namespaceId = UserContext.getCurrentNamespaceId(null);

		if(null != addressIds && 0 != addressIds.size()){

		}else if(null != buildingIds && 0 != buildingIds.size()){

		}else if(null != buildingNames && 0 != buildingNames.size()){

		}else if(null != phones && 0 != phones.size()){

		}else if(null != cmd.getCommunityId()){
			LOGGER.debug("All Park push message, cmd = {}", cmd);

			OpPromotionRegionPushingCommand command = new OpPromotionRegionPushingCommand();
			Date now = new Date();
			command.setScopeCode(OpPromotionScopeType.COMMUNITY.getCode());
			command.setScopeId(cmd.getCommunityId());
			command.setNamespaceId(namespaceId);
			command.setContent(cmd.getMessage());
			command.setStartTime(now.getTime());
			command.setEndTime(DateUtils.addDays(now, 1).getTime());
			promotionService.createRegionPushing(command);

			return;
		}*/

		LOGGER.debug("push message task scheduling, cmd = {}", cmd);

		// 调度执行一键推送
		Job job = new Job(
		        SendNoticeAction.class.getName(),
                StringHelper.toJsonString(cmd),
                String.valueOf(UserContext.current().getUser().getId()),
                UserContext.current().getScheme()
        );

        jesqueClientFactory.getClientPool().enqueue(queueName, job);
	}

	@Override
	public void pushMessage(PropCommunityBuildAddessCommand cmd,User user){
		Community community = this.checkCommunity(cmd.getCommunityId());

		if(community.getCommunityType() == CommunityType.RESIDENTIAL.getCode()) {
			sendNoticeToCommunityPmOwner(cmd, user);
		}
		else if(community.getCommunityType() == CommunityType.COMMERCIAL.getCode()) {
			// sendNoticeToEnterpriseContactor(cmd);
			this.sendNoticeToOrganizationMember(cmd, user);
		}
	}

	public void sendNoticeToEnterpriseContactor(PropCommunityBuildAddessCommand cmd) {

		Long communityId = cmd.getCommunityId();
		List<String> buildingNames = cmd.getBuildingNames();
		List<Long> addressIds = cmd.getAddressIds();

		List<Long> enterpriseIds = new ArrayList<Long>();
		List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();

		//未设定消息提类型则默认文本 sfyan by 20160729
		if (null == MessageBodyType.fromCode(cmd.getMessageBodyType())){
			cmd.setMessageBodyType(MessageBodyType.TEXT.getCode());
		}

		//按园区发送: buildingNames 和 addressIds 为空。
		if((buildingNames == null || buildingNames.size() == 0) && (addressIds == null || addressIds.size() == 0)){
			List<EnterpriseCommunityMap> enterpriseMapList = enterpriseProvider.queryEnterpriseMapByCommunityId(new ListingLocator(),
					communityId, Integer.MAX_VALUE - 1, (loc, query) -> {
						query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
						query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
						return null;
					});
			if(enterpriseMapList != null && enterpriseMapList.size() > 0) {
				for(EnterpriseCommunityMap enterpriseMap : enterpriseMapList) {
					enterpriseIds.add(enterpriseMap.getMemberId());
					List<EnterpriseContact> contactList = enterpriseContactProvider.queryContactByEnterpriseId(new ListingLocator(),
							enterpriseMap.getMemberId(), Integer.MAX_VALUE - 1, null);
					contacts.addAll(contactList);
				}
			}
		}

		//按地址发送：
		else if(addressIds != null && addressIds.size()  > 0){
			for (Long addressId : addressIds) {
				EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByAddressId(addressId);
				if(enterpriseAddress != null) {
					enterpriseIds.add(enterpriseAddress.getEnterpriseId());
					List<EnterpriseContact> contactList = enterpriseContactProvider.queryContactByEnterpriseId(new ListingLocator(),
							enterpriseAddress.getEnterpriseId(), Integer.MAX_VALUE - 1, null);
					contacts.addAll(contactList);
				}
			}
		}

		//按楼栋发送：
		else if((addressIds == null || addressIds.size()  == 0 )  && (buildingNames != null && buildingNames.size() > 0)){
			for (String buildingName : buildingNames) {
				List<ApartmentDTO> addresses =  addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, Integer.MAX_VALUE);
				if(addresses != null && addresses.size() > 0){
					for (ApartmentDTO address : addresses) {
						EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByAddressId(address.getAddressId());
						if(enterpriseAddress != null) {
							enterpriseIds.add(enterpriseAddress.getEnterpriseId());
							List<EnterpriseContact> contactList = enterpriseContactProvider.queryContactByEnterpriseId(new ListingLocator(),
									enterpriseAddress.getEnterpriseId(), Integer.MAX_VALUE - 1, null);
							contacts.addAll(contactList);
						}
					}
				}
			}
		}

		//		if(enterpriseIds != null && enterpriseIds.size() > 0){
		//			for (Long enterpriseId : enterpriseIds) {
		//				sendNoticeToFamilyById(enterpriseId, cmd.getMessage());
		//			}
		//		}

		processCommunityEnterpriseContactor(communityId,contacts,cmd.getMessage(), cmd.getMessageBodyType());
	}

	@Override
	public void sendNoticeToOrganizationMember(PropCommunityBuildAddessCommand cmd, User user) {
		Long communityId = cmd.getCommunityId();
		List<String> buildingNames = cmd.getBuildingNames();
		List<Long> buildingIds = cmd.getBuildingIds();
		List<Long> addressIds = cmd.getAddressIds();
		List<String> phones = cmd.getMobilePhones();
		List<Organization> orgs = new ArrayList<>();
		Integer namespaceId = user.getNamespaceId();

		// 全部企业的全部人员
		List<OrganizationMember> members = new ArrayList<>();

		LOGGER.debug("send notice to organizationMember , phones = {}, namespaceId = {}", phones, namespaceId);

		// 根据地址获取要推送的企业
		if(null != addressIds && 0 != addressIds.size()){
			for (Long addressId : addressIds) {
				OrganizationAddress orgAddress = organizationProvider.findOrganizationAddressByAddressId(addressId);
                if (null != orgAddress) {
                    orgs.add(organizationProvider.findOrganizationById(orgAddress.getOrganizationId()));
                }
            }
		}
        // 根据楼栋Id获取要推送的企业
		else if(null != buildingIds && 0 != buildingIds.size()){
			for (Long buildingId : buildingIds) {
				List<OrganizationAddress> orgAddresses = organizationProvider.listOrganizationAddressByBuildingId(buildingId, Integer.MAX_VALUE, null);
				for (OrganizationAddress organizationAddress : orgAddresses) {
                    if (null != organizationAddress) {
                        orgs.add(organizationProvider.findOrganizationById(organizationAddress.getOrganizationId()));
                    }
                }
			}
		}
        // 根据楼栋名称获取要推送的企业
		else if(null != buildingNames && 0 != buildingNames.size()){
			for (String buildingName : buildingNames) {
                List<OrganizationAddress> orgAddresses = organizationProvider.listOrganizationAddressByBuildingName(buildingName);
				for (OrganizationAddress organizationAddress : orgAddresses) {
                    if (null != organizationAddress) {
                        orgs.add(organizationProvider.findOrganizationById(organizationAddress.getOrganizationId()));
                    }
                }
			}
		}
		// 根据电话号码推送
		else if(null != phones && 0 != phones.size()){
			members = new ArrayList<>();
			for (String phone : phones) {
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
				OrganizationMember member = new OrganizationMember();
				member.setContactToken(phone);
				member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
				if(null != userIdentifier){
					member.setTargetId(userIdentifier.getOwnerUid());
					member.setTargetType(OrganizationMemberTargetType.USER.getCode());
				}
				members.add(member);
			}
		}
		// 根据小区获取要推送的企业
		else if(null != communityId) {
			List<OrganizationCommunityRequest> requests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(new CrossShardListingLocator(), communityId, 100000, null);
			for (OrganizationCommunityRequest req : requests) {
				orgs.add(organizationProvider.findOrganizationById(req.getMemberId()));
			}
		}

		if(0 != orgs.size()){
			// 获取全部企业的全部人员
			members = this.getOrganizationMembersByAddress(orgs);
		}

		LOGGER.debug("send message to organization member, members = {}", members);

		// 推送消息
		this.processSmsByMembers(members, cmd.getMessage(), cmd.getMessageBodyType(), cmd.getImgUri(), user);
	}

	/**
	 * 获取企业所有人员
	 */
	private List<OrganizationMember> getOrganizationMembersByAddress(List<Organization> orgs){
		List<OrganizationMember> members = new ArrayList<>();
		for (Organization organization : orgs) {
			if(null != organization){
				members.addAll(organizationProvider.listOrganizationMembersByOrgId(organization.getId()));
			}
		}
		return members;
	}

	private void processCommunityEnterpriseContactor(Long communityId,List<EnterpriseContact> contacts,String message, String messageBodyType) {

		List<String> phones = new ArrayList<String>();
		List<Long> userIds = new ArrayList<Long>();
		if(contacts != null && contacts.size() > 0){
			for (EnterpriseContact contact : contacts) {

				if(contact.getUserId() == 0){// 不是user，发短信
					List<EnterpriseContactEntry> entries = enterpriseContactProvider.queryContactEntryByContactId(contact, ContactType.MOBILE.getCode());
					if(entries != null && entries.size() > 0) {
						phones.add(entries.get(0).getEntryValue());
					}

				}
				else{//是user，发个人消息
					userIds.add(contact.getUserId());

				}
			}
		}

		//是user，发个人信息.
		if(userIds != null && userIds.size() > 0) {
			for (Long userId : userIds) {
				sendNoticeToUserById(userId, message, messageBodyType);
			}
		}

		List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_MSG, message);
		String templateScope = SmsTemplateCode.SCOPE;
		int templateId = SmsTemplateCode.WY_SEND_MSG_CODE;
		String templateLocale = currentLocale();
		String[] phoneArray = new String[phones.size()];
		phones.toArray(phoneArray);
		smsProvider.sendSms(UserContext.current().getUser().getNamespaceId(),phoneArray , templateScope, templateId, templateLocale, variables);
		//		//不是user，发短信。
		//		if(phones != null && phones.size() > 0 ){
		//			for (String phone : phones) {
		//				smsProvider.sendSms(phone, message);
		//			}
		//		}

	}

	private void processSmsByMembers(List<OrganizationMember> members,String message,String messageBodyType, String imgUri, User user) {

		List<String> phones = new ArrayList<>();
		List<Long> userIds = new ArrayList<>();

		// 区分是否是平台用户
		for (OrganizationMember member : members) {
			if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
				userIds.add(member.getTargetId());
			}else{
				phones.add(member.getContactToken());
			}
		}

		// 去重
        phones = phones.stream().distinct().collect(Collectors.toList());
        userIds = userIds.stream().distinct().collect(Collectors.toList());

        message = this.processMessage(message, messageBodyType, imgUri, EntityType.USER.getCode(), user.getId());
        if (message == null) {
            return;
        }

		// 平台用户就推送消息
		for (Long userId : userIds) {
			sendNoticeToUserById(userId, message, messageBodyType);
		}

        if (phones.size() > 0 && MessageBodyType.fromCode(messageBodyType) != MessageBodyType.IMAGE) {
            // 非平台用户就发短信
            List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_MSG, message);
            String templateScope = SmsTemplateCode.SCOPE;
            int templateId = SmsTemplateCode.WY_SEND_MSG_CODE;
            String[] phoneArray = new String[phones.size()];
            phones.toArray(phoneArray);
            smsProvider.sendSms(user.getNamespaceId(),phoneArray , templateScope, templateId, user.getLocale(), variables);
        }
	}

	private void sendNoticeToCommunityPmOwner(PropCommunityBuildAddessCommand cmd,User user) {

		Integer namespaceId = user.getNamespaceId();
        Long communityId = cmd.getCommunityId();
        List<String> buildingNames = cmd.getBuildingNames();
        List<Long> addressIds = cmd.getAddressIds();
        List<String> phones = cmd.getMobilePhones();

        this.checkOrganizationByCommIdAndOrgType(communityId, OrganizationType.PM.getCode());

		// 物业发通知机制：
        // 对于已注册的发消息 - familyIds
        // 未注册的发短信 - userIds。

		// 已注册的 user 分三种：
        // 1-已加入家庭，发家庭消息。
        // 2-还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。
        // 3-家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。

		List<CommunityPmOwner> owners  = new ArrayList<>();
		List<Long> familyIds = new ArrayList<>();

		//按电话号码发送
		if(phones != null && phones.size() > 0){
			List<OrganizationMember> members = new ArrayList<>();
			for (String phone : phones) {
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
				OrganizationMember member = new OrganizationMember();
				member.setContactToken(phone);
				member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
				if(null != userIdentifier){
					member.setTargetId(userIdentifier.getOwnerUid());
					member.setTargetType(OrganizationMemberTargetType.USER.getCode());
				}
				members.add(member);
			}
            if (members.size() > 0) {
                this.processSmsByMembers(members, cmd.getMessage(), cmd.getMessageBodyType(), cmd.getImgUri(), user);
            }
		}
        //按门牌地址发送：
		else if(addressIds != null && addressIds.size()  > 0){
			for (Long addressId : addressIds) {
				Family family = familyProvider.findFamilyByAddressId(addressId);
				if(family != null){
					familyIds.add(family.getId());
				}
				// List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(orgId, addressId);
				// owners.addAll(ownerList);
			}
		}
        //按楼栋发送：
		else if(buildingNames != null && buildingNames.size() > 0){
			for (String buildingName : buildingNames) {
				List<ApartmentDTO> addresses =  addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, Integer.MAX_VALUE);
				if(addresses != null && addresses.size() > 0){
					for (ApartmentDTO address : addresses) {
						Family family = familyProvider.findFamilyByAddressId(address.getAddressId());
						if(family != null){
							familyIds.add(family.getId());
						}
						// List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(orgId, address.getApartmentId());
						// owners.addAll(ownerList);
					}
				}
			}
		}
        //按小区发送
		else if(null != communityId){
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
					// List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(orgId, address.getId());
					// owners.addAll(ownerList);
				}
			}
		}

        String message = this.processMessage(cmd.getMessage(), cmd.getMessageBodyType(), cmd.getImgUri(), EntityType.USER.getCode(), user.getId());
        if (message == null) {
            return;
        }

        if(familyIds.size() > 0) {
			for (Long familyId : familyIds) {
				sendNoticeToFamilyById(familyId, message, cmd.getMessageBodyType());
			}
		}

		// 处理业主信息表:
        // 1-是user，已加入家庭，发家庭消息已包含该user。
        // 2-是user，还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。
        // 3-不是user，发短信。
        // 4-是user，家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
        if (owners.size() > 0) {
            processCommunityPmOwner(communityId, owners, cmd.getMessage(), cmd.getMessageBodyType(), user);
        }
    }

    private String processMessage(String message, String messageBodyType, String imgUri, String ownerType, Long ownerId) {
        if(MessageBodyType.fromCode(messageBodyType) == MessageBodyType.IMAGE){
            if(StringUtils.isEmpty(imgUri)){
                LOGGER.error("uri is null.");
                return null;
            }
            ImageBody imageBody = contentServerService.parserImageBody(imgUri, ownerType, ownerId);
            if(null == imageBody){
                LOGGER.error("image data error image uri = {}.", imgUri);
                return null;
            }
            message = StringHelper.toJsonString(imageBody);
        }
        return message;
    }

    @Override
    public void sendNoticeToPmAdmin(SendNoticeToPmAdminCommand cmd) {

        Job job = new Job(
                SendNoticeToPmAdminAction.class.getName(),
                cmd.toString(),
                System.currentTimeMillis()+"",
                String.valueOf(UserContext.current().getUser().getId()),
                UserContext.current().getScheme()
        );
        
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }

    @Override
    public void sendNoticeToPmAdmin(SendNoticeToPmAdminCommand cmd, Timestamp operateTime) {
        List<Long> pmAdminIds = new ArrayList<>();

        if (cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 0) {
            for (Long communityId : cmd.getCommunityIds()) {
                // 拿到小区下的所有企业
                ListEnterprisesCommand leCmd = new ListEnterprisesCommand();
                leCmd.setCommunityId(communityId);
                leCmd.setNamespaceId(currentNamespaceId());
                leCmd.setPageSize(100000);
                ListEnterprisesCommandResponse lecResponse = organizationService.listEnterprises(leCmd);
                if (lecResponse != null && lecResponse.getDtos() != null) {
                    // 获取企业管理员id列表
                    List<Long> organizationIds = lecResponse.getDtos().stream().map(OrganizationDetailDTO::getOrganizationId).collect(Collectors.toList());
                    pmAdminIds = this.getPmAdminIdsByOrganizationIds(organizationIds);
                }
            }
        } else if (cmd.getOrganizationIds() != null && cmd.getOrganizationIds().size() > 0) {
            // 获取企业管理员id列表
            pmAdminIds = this.getPmAdminIdsByOrganizationIds(cmd.getOrganizationIds());
        } else if (cmd.getPmAdminIds() != null && cmd.getPmAdminIds().size() > 0) {
            pmAdminIds = cmd.getPmAdminIds();
        }

        String message = this.processMessage(cmd.getMessage(), cmd.getMessageBodyType(), cmd.getImgUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
        if (message == null) {
            return;
        }

        if (pmAdminIds.size() > 0) {
            for (Long userId : pmAdminIds) {
                long pushCount = 0L;
                PushMessage pushMessage = this.buildPushMessage(message, userId, operateTime, pushCount);
                this.sendNoticeToUserById(userId, message, cmd.getMessageBodyType());
                this.insertPushMessageResult(pushMessage.getId(), userId, ++pushCount);
            }
            LOGGER.info("Finished to push message to pm admin, pm admin ids list = {}", pmAdminIds.toString());
        }
    }

    // 根据企业获取企业下的所有管理员
    private List<Long> getPmAdminIdsByOrganizationIds(List<Long> organizationIds) {
        List<Long> pmAdminIds = new ArrayList<>();
        for (Long organizationId : organizationIds) {
            ListServiceModuleAdministratorsCommand loaCmd = new ListServiceModuleAdministratorsCommand();
            loaCmd.setOrganizationId(organizationId);
            List<OrganizationContactDTO> contactDTOs = rolePrivilegeService.listOrganizationAdministrators(loaCmd);
            if (contactDTOs != null && contactDTOs.size() > 0) {
                pmAdminIds.addAll(contactDTOs.stream().map(OrganizationContactDTO::getTargetId).collect(Collectors.toList()));
            }
        }
        return pmAdminIds;
    }

    private void insertPushMessageResult(Long messageId, Long userId, Long pushCount) {
        PushMessageResult result = new PushMessageResult();
        result.setUserId(userId);
        result.setMessageId(messageId);
        result.setSendTime(Timestamp.valueOf(LocalDateTime.now()));
        UserIdentifier identifier = this.getUserMobileIdentifier(userId);
        result.setIdentifierToken(identifier != null ? identifier.getIdentifierToken() : null);
        pushMessageResultProvider.createPushMessageResult(result);

        PushMessage message = pushMessageProvider.getPushMessageById(messageId);
        message.setFinishTime(Timestamp.valueOf(LocalDateTime.now()));
        message.setStatus(PushMessageStatus.Finished.getCode());
        message.setPushCount(pushCount);
        pushMessageProvider.updatePushMessage(message);
    }

    private PushMessage buildPushMessage(String content, Long userId, Timestamp operateTime, Long pushCount) {
        PushMessage message = new PushMessage();
        message.setContent(content);
        message.setPushCount(pushCount);
        message.setMessageType(PushMessageType.NORMAL.getCode());
        message.setTargetType(PushMessageTargetType.USER.getCode());
        message.setTargetId(userId);
        message.setCreateTime(operateTime);
        message.setStartTime(Timestamp.from(Instant.now()));
        message.setStatus(PushMessageStatus.Processing.getCode());
        pushMessageProvider.createPushMessage(message);
        return message;
    }

    public void sendNoticeToFamilyById(Long familyId, String message, String messageBodyType){
		MessageDTO messageDto = new MessageDTO();
		//messageDto.setAppId(AppConstants.APPID_FAMILY);
        //messageDto.setSenderUid(UserContext.current().getUser().getId());
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
		messageDto.setChannels(new MessageChannel(MessageChannelType.GROUP.getCode(), String.valueOf(familyId)));
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
		messageDto.setBodyType(messageBodyType);
		messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
		messageDto.setBody(message);

		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.GROUP.getCode(),
				String.valueOf(familyId), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	public void sendNoticeToUserById(Long userId, String message, String messageBodyType){
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), String.valueOf(userId)));
		messageDto.setSenderUid(User.SYSTEM_UID);
		messageDto.setBodyType(messageBodyType);
		messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
		messageDto.setBody(message);

		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
				String.valueOf(userId), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	@Override
	public void sendMsgToPMGroup(PropCommunityIdMessageCommand cmd) {
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		long organizationId = org.getId();
		List<CommunityPmMember> memberList = propertyMgrProvider.listCommunityPmMembers(organizationId);
		if(memberList != null && memberList.size() > 0){
			for (CommunityPmMember communityPmMember : memberList) {
				sendNoticeToUserById(communityPmMember.getTargetId(), cmd.getMessage(), MessageBodyType.TEXT.getCode());
			}
		}
	}

	@Override
	public PostDTO createTopic(NewTopicCommand cmd) {
		User user  = UserContext.current().getUser();
		if(cmd.getVisibleRegionId() == null){
			LOGGER.error("propterty communityId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(cmd.getVisibleRegionId());
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + cmd.getVisibleRegionId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the community.");
		}

		//权限控制
		long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
		cmd.setCommunityId(organizationId);
		int totalCount = 0; //propertyMgrProvider.countCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus());
		if(totalCount == 0) return response;
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Unable to find the community.communityId=" + communityId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
		OrganizationTaskType taskTypeObj = this.convertContentCategoryToTaskType(cmd.getCategoryId());
		String taskType = taskTypeObj == null ? null:taskTypeObj.getCode();
		String startStrTime = cmd.getStartStrTime();
		String endStrTime = cmd.getEndStrTime();
		Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
		long organizationId = org.getId();

		Date date = DateStatisticHelper.getCurrentUTCTime();
		Date currentStartDate = DateStatisticHelper.getCurrent0Hour();
		Date weekStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 7, false);
		Date yesterdayStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 1, false);
		Date monthStartDate = DateStatisticHelper.getStartDateOfLastNDays(date, 30, false);

		/** 当天数量列表*/
		List<Integer> todayList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType,currentStartDate.getTime(), date.getTime());

		/** 上周数量列表*/
		List<Integer> weekList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType,weekStartDate.getTime(), date.getTime());

		/** z昨天数量列表*/
		List<Integer> yesterdayList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType,yesterdayStartDate.getTime(), currentStartDate.getTime());

		/** 上月数量列表*/
		List<Integer> monthList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType,monthStartDate.getTime(), date.getTime());

		/** 时间点数量列表*/
		List<Integer> dateList = null;

		if(!StringUtils.isEmpty(startStrTime) && !StringUtils.isEmpty(endStrTime))
		{
			Date startTime;
			Date endTime;
			try {
				startTime = DateStatisticHelper.parseDateStrToMin(startStrTime);
				endTime = DateStatisticHelper.parseDateStrToMax(endStrTime);
				dateList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType,startTime.getTime(), endTime.getTime());
			} catch (ParseException e) {
				LOGGER.error("failed to parse date.startStrTime=" + startStrTime +",endStrTime=" + endStrTime);
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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

	private OrganizationTaskType convertContentCategoryToTaskType(Long contentCategoryId) {
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
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_EMERGENCY_HELP) {
				return OrganizationTaskType.EMERGENCY_HELP;
			}
		}
		return null;
	}


	@Override
	public PropAptStatisticDTO getApartmentStatistics(PropCommunityIdCommand cmd) {
		PropAptStatisticDTO dto = new PropAptStatisticDTO();
		Long communityId = cmd.getCommunityId();
		this.checkCommunityIdIsNull(communityId);
		Community community = this.checkCommunity(communityId);

		int familyCount = familyProvider.countFamiliesByCommunityId(communityId);
		int userCount = familyProvider.countUserByCommunityId(communityId);
		Organization org = this.checkOrganizationByCommIdAndOrgType(communityId, OrganizationType.PM.getCode());
		long organizationId = org.getId();

		int defaultCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.DEFAULT.getCode());
		int liveCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.LIVING.getCode());
		int rentCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.RENT.getCode());
		int freeCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.FREE.getCode());
		int saledCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.SALED.getCode());
		int unsaleCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.UNSALE.getCode());
		int sum = defaultCount + liveCount + rentCount + freeCount + saledCount + unsaleCount;
		dto.setAptCount(sum);
		dto.setFamilyCount(familyCount);
		dto.setUserCount(userCount);
		dto.setDefaultCount(defaultCount);
		dto.setLiveCount(liveCount);
		dto.setRentCount(rentCount);
		dto.setFreeCount(freeCount);
		dto.setSaledCount(saledCount);
		dto.setUnsaleCount(unsaleCount);
		dto.setHasOwnerCount(liveCount + rentCount + saledCount);
		dto.setNoOwnerCount(freeCount + unsaleCount);
		return dto;
	}

	@Override
	public PropAptStatisticDTO getNewApartmentStatistics(PropCommunityIdCommand cmd) {
		PropAptStatisticDTO dto = new PropAptStatisticDTO();
		Long communityId = cmd.getCommunityId();
		this.checkCommunityIdIsNull(communityId);
		Community community = this.checkCommunity(communityId);

		int familyCount = familyProvider.countFamiliesByCommunityId(communityId);
		int userCount = familyProvider.countUserByCommunityId(communityId);
//		Organization org = this.checkOrganizationByCommIdAndOrgType(communityId, OrganizationType.PM.getCode());
//		long organizationId = org.getId();
//
//		int defaultCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.DEFAULT.getCode());
//		int liveCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.LIVING.getCode());
//		int rentCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.RENT.getCode());
//		int freeCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.FREE.getCode());
//		int decorateCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.DECORATE.getCode());
//		int unsaleCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, PmAddressMappingStatus.UNSALE.getCode());
		
		Map<Byte, Integer> result = addressProvider.countApartmentByLivingStatus(communityId);
		
//		dto.setAptCount(community.getAptCount()==null ?0 : community.getAptCount());
		dto.setFamilyCount(familyCount);
		dto.setUserCount(userCount);
		Integer temp = null;
		int sum = 0;
		int defaultCount = (temp = result.get(AddressMappingStatus.DEFAULT.getCode())) == null ? 0 : temp;
		dto.setDefaultCount(defaultCount);
		int livingCount = (temp = result.get(AddressMappingStatus.LIVING.getCode())) == null ? 0 : temp;
		dto.setLiveCount(livingCount);
		int rentCount = (temp = result.get(AddressMappingStatus.RENT.getCode())) == null ? 0 : temp;
		dto.setRentCount(rentCount);
		int freeCount = (temp = result.get(AddressMappingStatus.FREE.getCode())) == null ? 0 : temp;
		dto.setFreeCount(freeCount);
		int saledCount = (temp = result.get(AddressMappingStatus.SALED.getCode())) == null ? 0 : temp;
		dto.setSaledCount(saledCount);
		int unsaleCount = (temp = result.get(AddressMappingStatus.UNSALE.getCode())) == null ? 0 : temp;
		dto.setUnsaleCount(unsaleCount);
		int occupiedCount = (temp = result.get(AddressMappingStatus.OCCUPIED.getCode())) == null ? 0 : temp;
		dto.setOccupiedCount(occupiedCount);
		sum = defaultCount + livingCount + rentCount + freeCount + saledCount + unsaleCount + occupiedCount;
		
		// 科技园的从address表里统计，其它域空间还是按以前的方式统计
		if (sum == 0) {
			return getApartmentStatistics(cmd);
		}
		dto.setAptCount(sum);
		dto.setHasOwnerCount(livingCount + rentCount + saledCount);
		dto.setNoOwnerCount(freeCount + unsaleCount);
		
		return dto;
	}

	@Override
	public OrganizationDTO findPropertyOrganization(PropCommunityIdCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		Community community = this.checkCommunity(cmd.getCommunityId());

		OrganizationDTO dto = new OrganizationDTO();
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty communityId paramter can not be null or empty");
		}
		GetOrgDetailCommand c = new GetOrgDetailCommand();
		c.setCommunityId(cmd.getCommunityId());
		c.setOrganizationType(cmd.getOrganizationType());
		OrganizationDTO organizationDTO = this.organizationService.getOrganizationByComunityidAndOrgType(c);
		if(organizationDTO == null){
			LOGGER.error("Property organization is not exists.communityId="+ cmd.getCommunityId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
	
    private Long findOrganizationByCommunity(Community community) {
        if (community != null) {
            List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(community.getId());
            if (list != null && !list.isEmpty()) {
                return list.get(0).getOrganizationId();
            }
        }
        return null;
    }
    
	@Override
	public void createApartment(CreateApartmentCommand cmd) {
		if (cmd.getCommunityId() == null || cmd.getStatus() == null || StringUtils.isEmpty(cmd.getBuildingName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters");
		}
		if (StringUtils.isEmpty(cmd.getApartmentName())) {
			throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_APARTMENT_NAME_EMPTY, "apartment name cannot be empty");
		}
		Community community = checkCommunity(cmd.getCommunityId());
		Long organizationId = findOrganizationByCommunity(community);

        Building building = communityProvider.findBuildingByCommunityIdAndName(community.getId(), cmd.getBuildingName());

        if (null == building) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "not exists building name");
        }

        Address address = addressProvider.findAddressByCommunityAndAddress(community.getCityId(), community.getAreaId(), community.getId(), building.getName() + "-" + cmd.getApartmentName());
        if (address == null) {
        	address = new Address();
            address.setCommunityId(community.getId());
            address.setCityId(community.getCityId());
            address.setCityName(community.getCityName());
            address.setAreaId(community.getAreaId());
            address.setAreaName(community.getAreaName());
            address.setBuildingName(building.getName());
            address.setApartmentName(cmd.getApartmentName());
            address.setAreaSize(cmd.getAreaSize());
            address.setAddress(building.getName() + "-" + cmd.getApartmentName());

			address.setBuildArea(cmd.getBuildArea());
			address.setRentArea(cmd.getRentArea());
			address.setChargeArea(cmd.getChargeArea());
			address.setSharedArea(cmd.getSharedArea());
			if(cmd.getCategoryItemId() != null) {
				address.setCategoryItemId(cmd.getCategoryItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getCategoryItemId());
				if(item != null) {
					address.setCategoryItemName(item.getItemDisplayName());
				}
			}

			if(cmd.getSourceItemId() != null) {
				address.setSourceItemId(cmd.getSourceItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getSourceItemId());
				if(item != null) {
					address.setSourceItemName(item.getItemDisplayName());
				}
			}

			address.setDecorateStatus(cmd.getDecorateStatus());
			address.setOrientation(cmd.getOrientation());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setNamespaceId(community.getNamespaceId());
        	addressProvider.createAddress(address);
		}else if (AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
			address.setAreaSize(cmd.getAreaSize());

			address.setBuildArea(cmd.getBuildArea());
			address.setRentArea(cmd.getRentArea());
			address.setChargeArea(cmd.getChargeArea());
			address.setSharedArea(cmd.getSharedArea());
			if(cmd.getCategoryItemId() != null) {
				address.setCategoryItemId(cmd.getCategoryItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getCategoryItemId());
				if(item != null) {
					address.setCategoryItemName(item.getItemDisplayName());
				}
			}

			if(cmd.getSourceItemId() != null) {
				address.setSourceItemId(cmd.getSourceItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getSourceItemId());
				if(item != null) {
					address.setSourceItemName(item.getItemDisplayName());
				}
			}

			address.setDecorateStatus(cmd.getDecorateStatus());
			address.setOrientation(cmd.getOrientation());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            addressProvider.updateAddress(address);
		}else {
			throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_EXISTS_APARTMENT_NAME, "exists apartment name");
		}
        
        insertOrganizationAddressMapping(organizationId, community, address, cmd.getStatus());

		//门牌对应的楼栋和园区的sharedArea chargeArea buildArea rentArea都要增加相应的值 by xiongying 20170815
		if(address.getRentArea() != null) {
			Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
			building.setRentArea(buildingRentArea + address.getRentArea());

			Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
			community.setRentArea(communityRentArea + address.getRentArea());
		}
		if(address.getSharedArea() != null) {
			Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
			building.setSharedArea(buildingSharedArea + address.getSharedArea());

			Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
			community.setSharedArea(communitySharedArea + address.getSharedArea());
		}
		if(address.getBuildArea() != null) {
			Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
			building.setBuildArea(buildingBuildArea + address.getBuildArea());

			Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
			community.setBuildArea(communityBuildArea + address.getBuildArea());
		}
		if(address.getChargeArea() != null) {
			Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
			building.setChargeArea(buildingChargeArea + address.getChargeArea());

			Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
			community.setChargeArea(communityChargeArea + address.getChargeArea());
		}
		communityProvider.updateBuilding(building);
		communityProvider.updateCommunity(community);
	}

    @Override
	public void updateApartment(UpdateApartmentCommand cmd) {
    	Address address = addressProvider.findAddressById(cmd.getId());
    	if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters");
		}
		Community community = checkCommunity(address.getCommunityId());
		Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
    	if (cmd.getStatus() != null) {
    		Long organizationId = findOrganizationByCommunity(community);
    		CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
    		communityAddressMapping.setLivingStatus(cmd.getStatus());
    		organizationProvider.updateOrganizationAddressMapping(communityAddressMapping);
		}else {
			if (!StringUtils.isEmpty(cmd.getApartmentName())) {
	    		Address other = addressProvider.findAddressByBuildingApartmentName(address.getNamespaceId(), address.getCommunityId(), address.getBuildingName(), cmd.getApartmentName());
	    		if (other != null && other.getId() != cmd.getId()) {
	    			throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_EXISTS_APARTMENT_NAME, "exists apartment name");
	    		}
	    		address.setApartmentName(cmd.getApartmentName());
	    		address.setAddress(address.getBuildingName() + "-" + cmd.getApartmentName());
			}else if (cmd.getAreaSize() != null) {
				address.setAreaSize(cmd.getAreaSize());
			}else if (cmd.getSharedArea() != null) {
				Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
				Double oldAddressSharedArea = address.getSharedArea() == null ? 0.0 : address.getSharedArea();
				building.setSharedArea(buildingSharedArea - oldAddressSharedArea + cmd.getSharedArea());
				Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
				community.setSharedArea(communitySharedArea - oldAddressSharedArea + cmd.getSharedArea());

				address.setSharedArea(cmd.getSharedArea());
			}else if (cmd.getBuildArea() != null) {
				Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
				Double oldAddressBuildArea = address.getBuildArea() == null ? 0.0 : address.getBuildArea();
				building.setBuildArea(buildingBuildArea - oldAddressBuildArea + cmd.getBuildArea());
				Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
				community.setBuildArea(communityBuildArea - oldAddressBuildArea + cmd.getBuildArea());

				address.setBuildArea(cmd.getBuildArea());
			}else if (cmd.getRentArea() != null) {
				Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
				Double oldAddressRentArea = address.getRentArea() == null ? 0.0 : address.getRentArea();
				building.setRentArea(buildingRentArea - oldAddressRentArea + cmd.getRentArea());
				Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
				community.setRentArea(communityRentArea - oldAddressRentArea + cmd.getRentArea());

				address.setRentArea(cmd.getRentArea());
			}else if (cmd.getChargeArea() != null) {
				Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
				Double oldAddressChargeArea = address.getChargeArea() == null ? 0.0 : address.getChargeArea();
				building.setChargeArea(buildingChargeArea - oldAddressChargeArea + cmd.getChargeArea());
				Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
				community.setChargeArea(communityChargeArea - oldAddressChargeArea + cmd.getChargeArea());

				address.setChargeArea(cmd.getChargeArea());
			}else if (cmd.getCategoryItemId() != null) {
				address.setCategoryItemId(cmd.getCategoryItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getCategoryItemId());
				if(item != null) {
					address.setCategoryItemName(item.getItemDisplayName());
				}
			}else if (cmd.getSourceItemId() != null) {
				address.setSourceItemId(cmd.getSourceItemId());
				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getSourceItemId());
				if(item != null) {
					address.setSourceItemName(item.getItemDisplayName());
				}
			}else if (cmd.getDecorateStatus() != null) {
				address.setDecorateStatus(cmd.getDecorateStatus());
			}else if (cmd.getOrientation() != null) {
				address.setOrientation(cmd.getOrientation());
			}
	    	addressProvider.updateAddress(address);

			communityProvider.updateBuilding(building);
			communityProvider.updateCommunity(community);
		}
	}

	@Override
	public GetApartmentDetailResponse getApartmentDetail(GetApartmentDetailCommand cmd) {
		GetApartmentDetailResponse response = new GetApartmentDetailResponse();
		Address address = addressProvider.findAddressById(cmd.getId());
    	if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters");
		}
    	Community community = checkCommunity(address.getCommunityId());
		Long organizationId = findOrganizationByCommunity(community);
		CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
		
//		response.setBuildingName(address.getBuildingName());
//		response.setApartmentName(address.getApartmentName());
//		response.setAreaSize(address.getAreaSize());
		response = ConvertHelper.convert(address, GetApartmentDetailResponse.class);
		if (communityAddressMapping != null) {
			response.setStatus(communityAddressMapping.getLivingStatus());
		}else {
			response.setStatus(address.getLivingStatus());
		}
		
		if (CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
			OrganizationAddress organizationAddress = organizationProvider.findOrganizationAddressByAddressId(address.getId());
			if (organizationAddress != null) {
				Organization organization = organizationProvider.findOrganizationById(organizationAddress.getOrganizationId());
				if (organization != null) {
					response.setEnterpriseName(organization.getName());
				}
			}
		}else {
			List<OrganizationOwnerAddress> organizationOwnerAddresses = propertyMgrProvider.listOrganizationOwnerAddressByAddressId(address.getNamespaceId(), address.getId());
			if (organizationOwnerAddresses != null) {
				List<OrganizationOwnerDTO> owerList = organizationOwnerAddresses.stream().map(this::convert).collect(Collectors.toList());
				response.setOwerList(owerList);
			}
		}
		response.setCommunityType(community.getCommunityType());
		return response;
	}
	
	private OrganizationOwnerDTO convert(OrganizationOwnerAddress organizationOwnerAddress) {
		OrganizationOwnerDTO organizationOwnerDTO = new OrganizationOwnerDTO();
		OrganizationOwner organizationOwner = propertyMgrProvider.findOrganizationOwnerById(organizationOwnerAddress.getOrganizationOwnerId());
		if (organizationOwner != null) {
			organizationOwnerDTO.setContactName(organizationOwner.getContactName());
			organizationOwnerDTO.setContactToken(organizationOwner.getContactToken());
		}
		return organizationOwnerDTO;
	}

	@Override
	public void deleteApartment(DeleteApartmentCommand cmd) {
		Address address = addressProvider.findAddressById(cmd.getId());
    	if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters");
		}
    	address.setStatus(AddressAdminStatus.INACTIVE.getCode());
    	addressProvider.updateAddress(address);
    	addressProvider.updateOrganizationAddressMapping(address.getId());
    	addressProvider.updateOrganizationAddress(address.getId());
    	addressProvider.updateOrganizationOwnerAddress(address.getId());

		//门牌对应的楼栋和园区的sharedArea chargeArea buildArea rentArea都要减去相应的值 by xiongying 20170815
		Community community = checkCommunity(address.getCommunityId());
		Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
		if(address.getRentArea() != null) {
			Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
			building.setRentArea(buildingRentArea - address.getRentArea());

			Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
			community.setRentArea(communityRentArea - address.getRentArea());
		}
		if(address.getSharedArea() != null) {
			Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
			building.setSharedArea(buildingSharedArea - address.getSharedArea());

			Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
			community.setSharedArea(communitySharedArea - address.getSharedArea());
		}
		if(address.getBuildArea() != null) {
			Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
			building.setBuildArea(buildingBuildArea - address.getBuildArea());

			Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
			community.setBuildArea(communityBuildArea - address.getBuildArea());
		}
		if(address.getChargeArea() != null) {
			Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
			building.setChargeArea(buildingChargeArea - address.getChargeArea());

			Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
			community.setChargeArea(communityChargeArea - address.getChargeArea());
		}
		communityProvider.updateBuilding(building);
		communityProvider.updateCommunity(community);
	}

	private void insertOrganizationAddressMapping(Long organizationId, Community community, Address address, Byte livingStatus) {
		if (organizationId != null && community != null && address != null) {
            CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, community.getId(), address.getId());
            if (communityAddressMapping == null) {
                communityAddressMapping = new CommunityAddressMapping();
                communityAddressMapping.setOrganizationId(organizationId);
                communityAddressMapping.setCommunityId(community.getId());
                communityAddressMapping.setAddressId(address.getId());
                communityAddressMapping.setOrganizationAddress(address.getAddress());
                communityAddressMapping.setLivingStatus(livingStatus);
                communityAddressMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                communityAddressMapping.setUpdateTime(communityAddressMapping.getCreateTime());
                organizationProvider.createOrganizationAddressMapping(communityAddressMapping);
            }else {
				communityAddressMapping.setLivingStatus(livingStatus);
				communityAddressMapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				organizationProvider.updateOrganizationAddressMapping(communityAddressMapping);
			}
        }
    }

	@Override
	public ListApartmentsResponse listApartments(ListApartmentsCommand cmd) {

		CrossShardListingLocator locator = new CrossShardListingLocator();
		if(cmd.getPageAnchor() != null) {
			locator.setAnchor(cmd.getPageAnchor());
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		//取得门牌列表
		List<ApartmentAbstractDTO> aptList = addressProvider.listAddressByBuildingApartmentName(cmd.getNamespaceId(),
				cmd.getCommunityId(), cmd.getBuildingName(), cmd.getApartment(), cmd.getLivingStatus(), locator, pageSize + 1);
		ListApartmentsResponse response = new ListApartmentsResponse();
		List<ApartmentAbstractDTO> apartments = new ArrayList<>();
		LOGGER.info("listApartments aptList: {}", aptList);
		//设置门牌的入住状态
		if(aptList != null && aptList.size() > 0) {
			if(aptList.size() > pageSize) {
				aptList.remove(aptList.size() - 1);
				response.setNextPageAnchor(aptList.get(aptList.size() - 1).getId());
			}
			//门牌转化成门牌id列表
			List<Long> aptIdList = aptList.stream().map(a->a.getId()).collect(Collectors.toList());
			//处理小区地址关联表
			Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList);
			LOGGER.info("listApartments communityAddressMappingMap: {}", communityAddressMappingMap);
			aptList.forEach(apt -> {
				if (apt.getLivingStatus() == null) {
					CommunityAddressMapping mapping = communityAddressMappingMap.get(apt.getId());
					if(mapping != null){
						apt.setLivingStatus(mapping.getLivingStatus());
					}
					else{
						apt.setLivingStatus(AddressMappingStatus.LIVING.getCode());
					}
				}
				apartments.add(apt);
			});
			response.setApartments(apartments);
		}

		return response;
	}

	@Override
	public ListPropApartmentsResponse listNewPropApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd) {
		//检查参数
		checkListPropApartmentByKeywordParamters(cmd);
		
		//取得门牌列表
		List<ApartmentDTO> aptList = addressService.listApartmentsByKeyword(cmd).second();
		
		//门牌转化成门牌id列表
		List<Long> aptIdList = aptList.stream().map(a->a.getAddressId()).collect(Collectors.toList());
		
		//处理family
		Map<Long, Family> familyMap = familyProvider.mapFamilyByAddressIds(aptIdList);
		
		//处理每个门牌入住的人数
		Map<Long, Integer> ownerCountMap = propertyMgrProvider.mapOrganizationOwnerCountByAddressIds(UserContext.getCurrentNamespaceId(), aptIdList);
		
		//处理小区地址关联表
		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList);
		
		//判断是否欠费
		Map<Long, Byte> billOwedMap = mapBillOwedFlag(aptIdList);
		
		//门牌转化成要返回的列表
		List<PropFamilyDTO> resultList = aptList.stream().map(apartmentDTO->convertToPropFamilyDTO(cmd, apartmentDTO, familyMap, ownerCountMap, 
				communityAddressMappingMap, billOwedMap)).collect(Collectors.toList());
		
		PropAptStatisticDTO statistics = getStatistics(resultList);
		
		ListPropApartmentsResponse response = ConvertHelper.convert(statistics, ListPropApartmentsResponse.class);
		response.setResultList(resultList);
		
		return response;
	}
	
	private PropAptStatisticDTO getStatistics(List<PropFamilyDTO> resultList) {
		PropAptStatisticDTO statistics = new PropAptStatisticDTO();
		int userCount = 0;
		Map<Byte, Integer> map = new HashMap<>();
		map.put(AddressMappingStatus.DEFAULT.getCode(), 0);
		map.put(AddressMappingStatus.LIVING.getCode(), 0);
		map.put(AddressMappingStatus.RENT.getCode(), 0);
		map.put(AddressMappingStatus.FREE.getCode(), 0);
		map.put(AddressMappingStatus.SALED.getCode(), 0);
		map.put(AddressMappingStatus.UNSALE.getCode(), 0);
		map.put(AddressMappingStatus.OCCUPIED.getCode(), 0);

		for (PropFamilyDTO propFamilyDTO : resultList) {
			map.put(propFamilyDTO.getLivingStatus(), map.get(propFamilyDTO.getLivingStatus()) + 1);
			userCount += propFamilyDTO.getMemberCount();
		}
		
		statistics.setDefaultCount(map.get(AddressMappingStatus.DEFAULT.getCode()));
		statistics.setLiveCount(map.get(AddressMappingStatus.LIVING.getCode()));
		statistics.setRentCount(map.get(AddressMappingStatus.RENT.getCode()));
		statistics.setFreeCount(map.get(AddressMappingStatus.FREE.getCode()));
		statistics.setSaledCount(map.get(AddressMappingStatus.SALED.getCode()));
		statistics.setUnsaleCount(map.get(AddressMappingStatus.UNSALE.getCode()));
		statistics.setOccupiedCount(map.get(AddressMappingStatus.OCCUPIED.getCode()));
		statistics.setAptCount(statistics.getDefaultCount() + statistics.getLiveCount() + statistics.getRentCount() + statistics.getFreeCount() + statistics.getSaledCount() + statistics.getUnsaleCount());
		statistics.setUserCount(userCount);
		statistics.setHasOwnerCount(statistics.getLiveCount() + statistics.getRentCount() + statistics.getSaledCount());
		statistics.setNoOwnerCount(statistics.getFreeCount() + statistics.getUnsaleCount());
		
		return statistics;
	}

	private PropFamilyDTO convertToPropFamilyDTO(ListPropApartmentsByKeywordCommand cmd, ApartmentDTO apartmentDTO, Map<Long, Family> familyMap,
			Map<Long, Integer> ownerCountMap, Map<Long, CommunityAddressMapping> communityAddressMappingMap, Map<Long, Byte> billOwedMap) {
		Long addressId = apartmentDTO.getAddressId();
		
		PropFamilyDTO dto = new PropFamilyDTO();
		dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
		dto.setName(apartmentDTO.getApartmentName());
		dto.setAddressId(addressId);
		dto.setAreaSize(apartmentDTO.getAreaSize());
		dto.setEnterpriseName(apartmentDTO.getEnterpriseName());
		
		//设置家庭信息
		setFamilyInfo(dto, familyMap);
		//设置门牌入住的人数
		setOwnerCount(dto, ownerCountMap);
		//设置门牌的入住状态
		setLivingStatus(dto, communityAddressMappingMap, apartmentDTO.getLivingStatus());
		//设置公寓是否欠费
		setOwedFlag(dto, billOwedMap);
		
		return dto;
	}

	private void checkListPropApartmentByKeywordParamters(ListPropApartmentsByKeywordCommand cmd) {
		if(null == cmd.getCommunityId()){
			checkOrganization(cmd.getOrganizationId());

			OrganizationCommunity  orgCom = this.propertyMgrProvider.findPmCommunityByOrgId(cmd.getOrganizationId());
			if(orgCom == null){
				LOGGER.error("Unable to find the community by organizationId="+cmd.getOrganizationId());
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Unable to find the community by organizationId");
			}
			cmd.setCommunityId(orgCom.getCommunityId());
		}
	}

	private void setOwedFlag(PropFamilyDTO dto, Map<Long, Byte> billOwedMap) {
		Byte owedFlag = billOwedMap.get(dto.getAddressId());
		if (owedFlag == null) {
			dto.setOwed(OwedType.NO_OWED.getCode());
		}else {
			dto.setOwed(owedFlag);
		}
	}

	private void setLivingStatus(PropFamilyDTO dto, Map<Long, CommunityAddressMapping> communityAddressMappingMap, Byte addressLivingStatus) {
		if (addressLivingStatus != null) {
			dto.setLivingStatus(addressLivingStatus);
		}else {
			CommunityAddressMapping mapping = communityAddressMappingMap.get(dto.getAddressId());
			if(mapping != null){
				dto.setLivingStatus(mapping.getLivingStatus());
			}
			else{
				dto.setLivingStatus(AddressMappingStatus.LIVING.getCode());
			}
		}
	}

	private void setOwnerCount(PropFamilyDTO dto, Map<Long, Integer> ownerCountMap) {
		Integer ownerCount = ownerCountMap.get(dto.getAddressId());
		if (ownerCount == null) {
			dto.setMemberCount(0L);
		}else {
			dto.setMemberCount(ownerCount.longValue());
		}
	}

	private void setFamilyInfo(PropFamilyDTO dto, Map<Long, Family> familyMap) {
		Family family = familyMap.get(dto.getAddressId());
		if (family == null) {
			dto.setId(0L);
		}else {
			dto.setId(family.getId());
		}
	}

	//判断是否欠费
	private Map<Long, Byte> mapBillOwedFlag(List<Long> addressIds) {
		Map<Long, Byte> map = new HashMap<>(); 
		Map<Long, CommunityPmBill> billMap =  propertyMgrProvider.mapNewestBillByAddressIds(addressIds);
		if (!billMap.isEmpty()) {
			List<Long> billIds = billMap.entrySet().stream().map(r->r.getValue().getId()).collect(Collectors.toList());
			Map<Long, BigDecimal> paidMap = organizationProvider.mapOrgOrdersByBillIdAndStatus(billIds, OrganizationOrderStatus.PAID.getCode());
			billMap.forEach((addressId, bill)->{
				BigDecimal paidMoney = paidMap.get(bill.getId());
				if (paidMoney == null) {
					paidMoney = BigDecimal.ZERO;
				}
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidMoney);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0)
					map.put(addressId, OwedType.OWED.getCode());
				else
					map.put(addressId, OwedType.NO_OWED.getCode());
			});
		}
		return map;
	}
	
	@Override
	public List<PropFamilyDTO> listPropApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd) {
		// 优化门牌性能，by tt, 20170428
		return listNewPropApartmentsByKeyword(cmd).getResultList();
//		List<PropFamilyDTO> list = new ArrayList<PropFamilyDTO>();
//		User user  = UserContext.current().getUser();
//
//		long startTime = System.currentTimeMillis();
//
//		if(null == cmd.getCommunityId()){
//			Organization organization = this.checkOrganization(cmd.getOrganizationId());
//
//			OrganizationCommunity  orgCom = this.propertyMgrProvider.findPmCommunityByOrgId(cmd.getOrganizationId());
//			if(orgCom == null){
//				LOGGER.error("Unable to find the community by organizationId="+cmd.getOrganizationId());
//				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"Unable to find the community by organizationId");
//			}
//			cmd.setCommunityId(orgCom.getCommunityId());
//		}
//
//
//		//权限控制
//		Tuple<Integer,List<ApartmentDTO>> apts = addressService.listApartmentsByKeyword(cmd);
//
//		long getApartmentsTime = System.currentTimeMillis();
//		LOGGER.info("Get apartments time:{}", getApartmentsTime - startTime);
//
//		StringBuilder strBuilder = new StringBuilder("");
//		List<ApartmentDTO> aptList = apts.second();
//		for (ApartmentDTO apartmentDTO : aptList) {
//			if(strBuilder.length() > 0) {
//				strBuilder.append(", ");
//			}
//			PropFamilyDTO dto = new PropFamilyDTO();
//			long familyStartTime = System.currentTimeMillis();
//			Family family = familyProvider.findFamilyByAddressId(apartmentDTO.getAddressId());
//			long familyEndTime = System.currentTimeMillis();
//			strBuilder.append("family-").append(familyEndTime - familyStartTime);
//			long orgOwnerStartTime = System.currentTimeMillis();
//			if(family != null )
//			{
//				dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
//				dto.setName(apartmentDTO.getApartmentName());
//				dto.setAddressId(family.getAddressId());
//				dto.setId(family.getId());
//				// dto.setMemberCount(family.getMemberCount());
//                List<OrganizationOwnerDTO> organizationOwners = propertyMgrProvider.listOrganizationOwnersByAddressId(
//                        UserContext.getCurrentNamespaceId(), apartmentDTO.getAddressId(), record -> new OrganizationOwnerDTO());
//                dto.setMemberCount((long) organizationOwners.size());
//            }
//			else
//			{
//				dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
//				dto.setName(apartmentDTO.getApartmentName());
//				dto.setAddressId(apartmentDTO.getAddressId());
//				dto.setId(0L);
//				// dto.setMemberCount(0L);
//                List<OrganizationOwnerDTO> organizationOwners = propertyMgrProvider.listOrganizationOwnersByAddressId(
//                        UserContext.getCurrentNamespaceId(), apartmentDTO.getAddressId(), record -> new OrganizationOwnerDTO());
//                dto.setMemberCount((long) organizationOwners.size());
//			}
//			long orgOwnerEndTime = System.currentTimeMillis();
//			strBuilder.append("#owner-").append(orgOwnerEndTime - orgOwnerStartTime);
//
//			long addressMappingStartTime = System.currentTimeMillis();
//			if (apartmentDTO.getLivingStatus() != null) {
//				dto.setLivingStatus(apartmentDTO.getLivingStatus());
//			}else {
//				CommunityAddressMapping mapping = propertyMgrProvider.findAddressMappingByAddressId(apartmentDTO.getAddressId());
//				if(mapping != null){
//					dto.setLivingStatus(mapping.getLivingStatus());
//				}
//				else{
//					dto.setLivingStatus(PmAddressMappingStatus.LIVING.getCode());
//				}
//			}
//			long addressMappingEndTime = System.currentTimeMillis();
//			strBuilder.append("#mapping-").append(addressMappingEndTime - addressMappingStartTime);
//
//
//			long billStartTime = System.currentTimeMillis();
//			//判断公寓是否欠费
//			CommunityPmBill bill =  this.propertyMgrProvider.findNewestBillByAddressId(dto.getAddressId());
//			if(bill != null){
//				BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
//				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
//				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0)
//					dto.setOwed(OwedType.OWED.getCode());
//				else
//					dto.setOwed(OwedType.NO_OWED.getCode());
//			}
//			else
//				dto.setOwed(OwedType.NO_OWED.getCode());
//
//			long billEndTime = System.currentTimeMillis();
//			strBuilder.append("#bill-").append(billEndTime - billStartTime);
//			dto.setAreaSize(apartmentDTO.getAreaSize());
//			dto.setEnterpriseName(apartmentDTO.getEnterpriseName());
//			list.add(dto);
//		}
//
//		long populateApartmentsTime = System.currentTimeMillis();
//		LOGGER.info("Populate apartmentDtos time:{}, detail time: {}", populateApartmentsTime - getApartmentsTime, strBuilder.toString());
//		LOGGER.info("The total time:{}", populateApartmentsTime - startTime);
//
//		return list;
	}

	@Override
	public void importPmBills(Long orgId, MultipartFile[] files) {
		if(files == null){
			LOGGER.error("files is null");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"files is null");
		}
		this.checkOrganizationIdIsNull(orgId);
		this.checkOrganization(orgId);

		String rootPath = System.getProperty("user.dir");
		String filePath1 = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		String filePath2 = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		String jarPath = this.getJarPath();
		String command = this.setExecCommand(jarPath,orgId,filePath1,filePath2);

		try {
			//将原文件暂存在服务器中
			this.storeFile(files[0],filePath1);
			//启动进程解析原文件
			Process process = Runtime.getRuntime().exec(command);
			int i = 1;
			while(process.isAlive()){
				if(LOGGER.isDebugEnabled()){
					System.out.println("isAliveTime="+i*1000);
					LOGGER.info("isAliveTime="+i*1000);
				}
				Thread.sleep(i*1000);
				i++;
				if(i>10)	break;
			}
			File file2 = new File(filePath2);
			if(file2 == null || !file2.exists()){
				LOGGER.error("parse file failure.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"parse file failure.");
			}
			List<CommunityPmBill> bills = this.convertExcelFileToPmBills(file2);
			createPmBills(bills, orgId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					e.getMessage());
		} finally{
			File file = new File(filePath1);
			File file2 = new File(filePath2);
			if(file.exists())	file.delete();
			if(file2.exists())	file2.delete();
		}
	}

	/**
	 * 将数据插入数据库
	 */
	public void createPmBills(List<CommunityPmBill> bills,Long orgId){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listAddressMappingsByOrgId(orgId);

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
							continue;
							/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									bill.getAddress() + " not find in address mapping.");*/
						}

						CommunityPmBill existedBill = this.propertyMgrProvider.findPmBillByAddressIdAndTime(mapping.getAddressId(),bill.getStartDate(),bill.getEndDate());
						if(existedBill != null){
							LOGGER.error("the bill is exist.please don't import repeat data.address="+bill.getAddress()+",startDate=" + format.format(bill.getStartDate())+",endDate="+format.format(bill.getEndDate()));
							throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
							CommunityPmBill beforeBill = this.propertyMgrProvider.findNewestBillByAddressId(mapping.getAddressId());
							if(beforeBill != null){
								//payAmount为负
								BigDecimal payedAmount = this.countPmBillPaidAmount(beforeBill.getId());
								BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(payedAmount);
								bill.setOweAmount(oweAmount);
							}
							else
								bill.setOweAmount(BigDecimal.ZERO);
						}

						this.createPropBill(bill);
					}
				}

				return s;
			});

		}
	}

	@Override
	public ListPmBillsByConditionsCommandResponse listPmBillsByConditions(ListPmBillsByConditionsCommand cmd) {
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		Organization organization = this.checkOrganization(cmd.getOrganizationId());
		//向统一支付发请求,查询订单支付状态
		LOGGER.error("listPmBillsByConditions-remoteUpdate");
		//remoteRefreshOrgOrderStatus();

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
				List<CommunityPmBill> tempBills = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(), null);
				if(tempBills != null && !tempBills.isEmpty()){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					String billDate = format.format(tempBills.get(0).getStartDate());
					startDate = this.getFirstDayOfMonthByStr(billDate);
					endDate = this.getLastDayOfMonthByStr(billDate);
				}
			}
		}

		List<CommunityPmBill> comBillList = this.propertyMgrProvider.listPmBillsByOrgId(organization.getId(),cmd.getAddress(),startDate,endDate,offset,pageSize+1);
		if(comBillList != null && !comBillList.isEmpty()){
			if(comBillList.size() == pageSize+1){
				comBillList.remove(comBillList.size()-1);
				result.setNextPageOffset(cmd.getPageOffset()+1);
			}

			for(CommunityPmBill comBill : comBillList){
				PmBillsDTO billDto = ConvertHelper.convert(comBill, PmBillsDTO.class);
				this.convertBillDateToDto(comBill,billDto);
				BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
				this.setPmBillAmounts(billDto,payedAmount);
				billList.add(billDto);
			}
		}
		result.setRequests(billList);
		return result;
	}

	private BigDecimal countPmBillPaidAmount(Long billId) {
		List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billId,OrganizationOrderStatus.PAID.getCode());
		BigDecimal paidAmount = BigDecimal.ZERO;
		if(orders != null && !orders.isEmpty()){
			for(OrganizationOrder order : orders){
				paidAmount = paidAmount.add(order.getAmount());
			}
		}
		return paidAmount;
	}

	private String setExecCommand(String jarPath, Long orgId, String filePath1,String filePath2) {
		StringBuilder builder = new StringBuilder();
		//builder.append("java -jar D:/dev_documents/workspace2/ehparser/target/ehparser-0.0.1-SNAPSHOT.jar");
		builder.append("java -jar"+" "+jarPath);
		builder.append(" ");
		builder.append(orgId);
		builder.append(" ");
		builder.append(filePath1);
		builder.append(" ");
		builder.append(filePath2);
		return builder.toString();
	}

	private String getJarPath() {
		String rootPath = System.getProperty("user.dir");
		String jarPath = rootPath+File.separator+"ehparser-0.0.1-SNAPSHOT.jar";

		File dir = new File(rootPath);
		if(dir.isDirectory()){
			File [] fileList = dir.listFiles();
			if(fileList != null && fileList.length > 0){
				for(File file : fileList){
					String name = file.getName();
					if(LOGGER.isDebugEnabled())
						LOGGER.error("jarFileName="+name);

					if(name.startsWith("ehparser") && name.endsWith(".jar")){
						jarPath = rootPath+File.separator+file.getName();
						break;
					}
				}
			}
		}
		if(LOGGER.isDebugEnabled())
			LOGGER.error("jarPath="+jarPath);
		return jarPath;
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

	private void processCommunityPmOwner(Long communityId,List<CommunityPmOwner> owners,String message,String messageBodyType, User user) {
		User operator = user;
		Integer namespaceId = operator.getNamespaceId();

		List<String> phones = new ArrayList<String>();
		List<Long> userIds = new ArrayList<Long>();
		if(owners != null && owners.size() > 0){
			for (CommunityPmOwner communityPmOwner : owners) {
				User userPhone = userService.findUserByIndentifier(namespaceId, communityPmOwner.getContactToken());

				if(userPhone == null){// 3-不是user，发短信
					phones.add(communityPmOwner.getContactToken());
				}
				else{//是用户，未加入或创建家庭,给用户发个人消息
					Family family = familyProvider.findFamilyByAddressId(communityPmOwner.getAddressId());
					if(family != null){
						GroupMember member = groupProvider.findGroupMemberByMemberInfo(family.getId(), GroupDiscriminator.FAMILY.getCode(), userPhone.getId());
						if(member == null){// 2- 是user，还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。
							userIds.add(userPhone.getId());
						}
						//1- 是user，已加入家庭，发家庭消息已包含该user。 已经发过family。
					}
					else{//4：是user，家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
						userIds.add(userPhone.getId());
					}
				}
			}
		}

		//是user，还未加入家庭，发个人信息.
		if(userIds != null && userIds.size() > 0){
			for (Long userId : userIds) {
				sendNoticeToUserById(userId, message, messageBodyType);
			}
		}

		//不是user，发短信。

		List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_MSG, message);
		String templateScope = SmsTemplateCode.SCOPE;
		int templateId = SmsTemplateCode.WY_SEND_MSG_CODE;
		String templateLocale = user.getLocale();
		String[] phoneArray = new String[phones.size()];
		phones.toArray(phoneArray);
		smsProvider.sendSms(namespaceId,phoneArray , templateScope, templateId, templateLocale, variables);
		//		if(phones != null && phones.size() > 0 ){
		//			for (String phone : phones) {
		//				smsProvider.sendSms(phone, message);
		//			}
		//		}

	}

	private void createList(Long organizationId,String taskType,List<Integer> todayList, long startTime, long endTime)
	{
		int todayCount = propertyMgrProvider.countCommunityPmTasks(organizationId, taskType,null,String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
		todayList.add(todayCount);
		int num = OrganizationTaskStatus.OTHER.getCode();
		for (int i = 1; i <= num ; i++)
		{
			int count = propertyMgrProvider.countCommunityPmTasks(organizationId, taskType,(byte)i,String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
			todayList.add(count);
		}
	}

	private List<Integer> getTaskCounts(Long organizationId, Long communityId, String taskType, long startTime, long endTime){
		List<Integer> counts = new ArrayList<Integer>();
		int num = OrganizationTaskStatus.OTHER.getCode();
		List<OrganizationTask> tasks = propertyMgrProvider.communityPmTaskLists(organizationId, communityId, taskType, null, String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
		if(null != communityId){
			counts.add(this.getPostByCommunityId(communityId, tasks).size());
		}else{
			counts.add(tasks.size());
		}

		for (int i = 1; i <= num ; i++)
		{
			tasks = propertyMgrProvider.communityPmTaskLists(organizationId, communityId,taskType,(byte)i,String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
			if(null != communityId){
				counts.add(this.getPostByCommunityId(communityId, tasks).size());
			}else{
				counts.add(tasks.size());
			}
		}

		return counts;
	}

	private List<Post> getPostByCommunityId(Long communityId, List<OrganizationTask> tasks){
		Community community = communityProvider.findCommunityById(communityId);
		List<Post> posts = new ArrayList<Post>();
		for (OrganizationTask task : tasks) {
			Post post = forumProvider.findPostById(task.getApplyEntityId());
			if(null != post && post.getForumId().equals(community.getDefaultForumId())){
				posts.add(post);
			}
		}
		return posts;
	}

	private void checkOrganizationIdIsNull(Long organizationId) {
		if(organizationId == null){
			LOGGER.error("propterty organizationId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId paramter can not be null or empty");
		}
	}

	private Organization checkOrganization(Long orgId) {
		Organization org = organizationProvider.findOrganizationById(orgId);
		if(org == null){
			LOGGER.error("Unable to find the organization.organizationId=" + orgId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		return org;
	}

	private void convertBillDateToDto(CommunityPmBill comBill,PmBillsDTO billDto) {
		billDto.setEndDate(comBill.getEndDate().getTime());
		billDto.setPayDate(comBill.getPayDate().getTime());
		billDto.setStartDate(comBill.getStartDate().getTime());
	}

	private void setPmBillAmounts(PmBillsDTO billDto, BigDecimal payedAmount) {
		billDto.setPayedAmount(payedAmount);
		billDto.setWaitPayAmount(billDto.getDueAmount().add(billDto.getOweAmount()).subtract(payedAmount));
		billDto.setTotalAmount(billDto.getDueAmount().add(billDto.getOweAmount()));
	}

	private void remoteRefreshOrgOrderStatus() {
		try{
			List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByStatus(OrganizationOrderStatus.WAITING_FOR_PAY.getCode());
			if(orders != null && !orders.isEmpty()){
				for(OrganizationOrder order : orders){
					String orderNo = this.convertOrderIdToOrderNo(order.getId());
					LOGGER.error("remoteUpdate:orderNo="+orderNo);
					//this.remoteUpdateOrgOrderByOrderNo(orderNo);
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"date format is wrong.must be yyyy-MM");
		}
	}

	@Override
	public void deletePmBills(DeletePmBillsCommand cmd) {
		if(cmd.getIds() == null || cmd.getIds().isEmpty()){
			LOGGER.error("ids paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ids paramter can not be null or empty");
		}
		this.dbProvider.execute(s -> {
			if(cmd.getIds() != null && !cmd.getIds().isEmpty()){
				for(Long billId : cmd.getIds()){
					DeletePmBillCommand command = new DeletePmBillCommand();
					command.setId(billId);
					this.deletePmBill(command);
				}
			}
			return true;
		});

	}

	private void deletePmBillTrans(CommunityPmBill bill,String content) {
		this.dbProvider.execute(s -> {
			this.organizationProvider.deleteOrganizationBillById(bill.getId());
			this.createPmOperLog(bill.getId(),PmBillOperLogType.DELETE.getCode(),content);
			return true;
		});
	}

	private void createPmOperLog(Long billId,String type, String content) {
		User user = UserContext.current().getUser();
		AuditLog log = new AuditLog();
		log.setOperatorUid(user.getId());
		log.setOperationType(type);
		log.setReason(content);
		log.setResourceType("PMBILL");
		log.setResourceId(billId);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		auditLogProvider.createAuditLog(log);
	}

	@Override
	public void insertPmBills(InsertPmBillsCommand cmd) {
		if(cmd.getInsertList() == null || cmd.getInsertList().isEmpty()){
			LOGGER.error("insertList paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"insertList paramter can not be null or empty");
		}

		if(cmd.getInsertList() != null && !cmd.getInsertList().isEmpty()){
			for (UpdatePmBillsDto insertBill : cmd.getInsertList()){
				InsertPmBillCommand command = new InsertPmBillCommand();
				command.setAddress(insertBill.getAddress());
				command.setDescription(insertBill.getDescription());
				command.setDueAmount(insertBill.getDueAmount());
				command.setEndDate(insertBill.getEndDate());
				command.setOrganizationId(insertBill.getOrganizationId());
				command.setOweAmount(insertBill.getOweAmount());
				command.setPayDate(insertBill.getPayDate());
				command.setStartDate(insertBill.getStartDate());
				this.insertPmBill(command);
			}
		}

	}

	private void insertPmBillTrans(CommunityPmBill bill,String content) {
		this.dbProvider.execute(s -> {
			this.createPropBill(bill);
			this.createPmOperLog(bill.getId(),PmBillOperLogType.INSERT.getCode(),content);
			return true;
		});

	}

	@Override
	public int updatePmBills(UpdatePmBillsCommand cmd) {
		if(cmd.getUpdateList() == null || cmd.getUpdateList().isEmpty()){
			LOGGER.error("updateList paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"updateList paramter can not be null or empty");
		}

		if(cmd.getUpdateList() != null && !cmd.getUpdateList().isEmpty()){
			this.dbProvider.execute(s -> {
				for(UpdatePmBillsDto bill : cmd.getUpdateList()){
					UpdatePmBillCommand command = new UpdatePmBillCommand();
					command.setAddress(bill.getAddress());
					command.setDescription(bill.getDescription());
					command.setDueAmount(bill.getDueAmount());
					command.setEndDate(bill.getEndDate());
					command.setId(bill.getId());
					command.setOrganizationId(bill.getOrganizationId());
					command.setPayDate(bill.getPayDate());
					command.setStartDate(bill.getStartDate());
					this.updatePmBill(command);
				}
				return s;
			});
		}
		return 1;
	}

	private void updatePmBillTrans(CommunityPmBill bill, String content) {
		this.dbProvider.execute(s -> {
			this.organizationProvider.updateOrganizationBill(bill);
			this.createPmOperLog(bill.getId(),PmBillOperLogType.UPDATE.getCode(),content);
			return true;
		});
	}

	@Override
	public void deletePmBill(DeletePmBillCommand cmd) {
		CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(cmd.getId());
		if(communBill != null){
			User user = UserContext.current().getUser();
			String content = "用户"+user.getNickName()+"删除物业账单:"+communBill.getName();
			this.deletePmBillTrans(communBill,content);
		}
	}

	@Override
	public void updatePmBill(UpdatePmBillCommand bill) {
		if(bill.getId() == null || bill.getOrganizationId() == null){
			LOGGER.error("id or organizationId paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id or organizationId paramter can not be null or empty");
		}

		CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(bill.getId());
		if(communBill != null){
			User user = UserContext.current().getUser();
			StringBuilder builder = new StringBuilder();
			builder.append("用户");
			builder.append(user.getNickName());
			builder.append("更新物业账单:");
			builder.append(communBill.getName());
			builder.append(".");
			if(bill.getEndDate() != null){
				java.sql.Date endDate = new java.sql.Date(bill.getEndDate());
				builder.append("截止日期:");
				builder.append(communBill.getEndDate());
				builder.append("更新为");
				builder.append(endDate);
				builder.append(".");
				communBill.setEndDate(endDate);
			}
			if(bill.getPayDate() != null){
				java.sql.Date payDate = new java.sql.Date(bill.getPayDate());
				builder.append("还款日期:");
				builder.append(communBill.getPayDate());
				builder.append("更新为");
				builder.append(payDate);
				builder.append(".");
				communBill.setPayDate(payDate);
			}
			if(bill.getStartDate() != null){
				java.sql.Date startDate = new java.sql.Date(bill.getStartDate());
				builder.append("开始日期:");
				builder.append(communBill.getStartDate());
				builder.append("更新为");
				builder.append(startDate);
				builder.append(".");
				communBill.setStartDate(startDate);
			}
			if(!(bill.getDescription() == null || bill.getDescription().equals(""))){
				builder.append("描述:");
				builder.append(communBill.getDescription());
				builder.append("更新为");
				builder.append(bill.getDescription());
				builder.append(".");
				communBill.setDescription(bill.getDescription());
			}
			if(bill.getDueAmount() != null){
				builder.append("本月金额:");
				builder.append(communBill.getDueAmount());
				builder.append("更新为");
				builder.append(bill.getDueAmount());
				builder.append(".");
				communBill.setDueAmount(bill.getDueAmount());
			}
			if(bill.getOweAmount() != null){
				builder.append("往期欠款:");
				builder.append(communBill.getOweAmount());
				builder.append("更新为");
				builder.append(bill.getOweAmount());
				builder.append(".");
				communBill.setOweAmount(bill.getOweAmount());
			}
			if(bill.getAddress() != null && !bill.getAddress().equals("")){
				CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(communBill.getOrganizationId(), bill.getAddress());
				if(addressMapping != null){
					builder.append("楼栋-门牌号:");
					builder.append(communBill.getAddress());
					builder.append("更新为");
					builder.append(bill.getAddress());
					builder.append(".");
					communBill.setAddress(bill.getAddress());
					communBill.setEntityId(addressMapping.getAddressId());
				}
				else{
					LOGGER.error("update bill failure.because address not found..address="+bill.getAddress());
					throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"update bill failure.because address not found.address="+bill.getAddress());
				}
			}
			this.updatePmBillTrans(communBill,builder.toString());
		}

	}

	@Override
	public void insertPmBill(InsertPmBillCommand cmd) {
		if(cmd.getOrganizationId() == null || cmd.getAddress() == null){
			LOGGER.error("propterty organizationId or address paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"propterty organizationId or address paramter can not be null or empty");
		}
		Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(organization == null){
			LOGGER.error("Insert failure.Unable to find the organization.organizationId=" + cmd.getOrganizationId());
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Insert failure.Unable to find the organization.");
		}
		if(cmd.getAddress() == null || cmd.getAddress().equals("") || cmd.getDueAmount() == null || cmd.getEndDate() == null ||
				cmd.getPayDate() == null || cmd.getStartDate() == null){
			LOGGER.error("address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
		}

		Calendar cal = Calendar.getInstance();
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(new Date().getTime());

		CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(cmd.getOrganizationId(), cmd.getAddress());
		if(addressMapping == null){
			LOGGER.error("Insert failure.the address not find");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			CommunityPmBill beforeBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
			if(beforeBill != null){
				BigDecimal paidAmount = this.countPmBillPaidAmount(beforeBill.getId());
				BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(paidAmount);
				bill.setOweAmount(oweAmount);
			}
			else
				bill.setOweAmount(BigDecimal.ZERO);
		}
		String content = "用户"+user.getNickName()+"新增物业账单:"+bill.getName();
		this.insertPmBillTrans(bill,content);
	}

	@Override
	public ListOrgBillingTransactionsByConditionsCommandResponse listOrgBillingTransactionsByConditions(ListOrgBillingTransactionsByConditionsCommand cmd) {
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		Organization organization = this.checkOrganization(cmd.getOrganizationId());

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

		orgbillTxList = this.organizationProvider.listOrgBillTxByOrgId(organization.getId(),BillTransactionResult.SUCCESS.getCode(),startTime,endTime,cmd.getAddress(),offset,pageSize+1);
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
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		Organization organization = this.checkOrganization(cmd.getOrganizationId());

		ListOweFamilysByConditionsCommandResponse response = new ListOweFamilysByConditionsCommandResponse();
		List<OweFamilyDTO> familyList = new ArrayList<OweFamilyDTO>();

		List<CommunityPmBill> billList = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(),cmd.getAddress());
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
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
		this.sortOweFamilyDTOByPayTimeDesc(familyList);

		response.setRequests(familyList);
		return response;
	}

	private void sortOweFamilyDTOByPayTimeDesc(List<OweFamilyDTO> familyList) {
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
	}

	private void setOweFamilyOwnerInfo(OweFamilyDTO family, Long addressId, Long organizationId) {
		List<CommunityPmOwner> orgMemberList = this.organizationProvider.listOrganizationOwnerByAddressIdAndOrgId(addressId,organizationId);
		if(orgMemberList != null && !orgMemberList.isEmpty()){
			family.setOwnerTelephone(orgMemberList.get(0).getContactToken());
		}
	}

	private void setOweFamilyDTOLastTransactionInfo(OweFamilyDTO family, Long billId) {
		List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billId, OrganizationOrderStatus.PAID.getCode());
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
	public ListBillTxByAddressIdCommandResponse listBillTxByAddressId(ListBillTxByAddressIdCommand cmd) {
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkAddress(cmd.getAddressId());
		if(cmd.getPageOffset() == null)
			cmd.setPageOffset(1L);

		ListBillTxByAddressIdCommandResponse response = new ListBillTxByAddressIdCommandResponse();
		List<FamilyBillingTransactionDTO> transactionList = new ArrayList<FamilyBillingTransactionDTO>();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

		Long addressId = cmd.getAddressId();

		List<FamilyBillingTransactions> familyTransactionList = this.familyProvider.listFBillTx(BillTransactionResult.SUCCESS.getCode(),addressId,pageSize+1,offset);
		if(familyTransactionList != null && familyTransactionList.size() == pageSize+1){
			response.setNextPageOffset(cmd.getPageOffset()+1);
			familyTransactionList.remove(familyTransactionList.size()-1);
		}
		if(familyTransactionList != null && !familyTransactionList.isEmpty()){
			familyTransactionList.stream().map(r -> {
				FamilyBillingTransactionDTO fBillTxdto = new FamilyBillingTransactionDTO();
				this.convertFBillTxToDto(r,fBillTxdto);
				transactionList.add(fBillTxdto);
				return null;
			}).toArray();
		}

		response.setRequests(transactionList);
		return response;
	}

	private void convertFBillTxToDto(FamilyBillingTransactions r,FamilyBillingTransactionDTO fBillTxdto) {
		fBillTxdto.setBillType(r.getTxType());
		fBillTxdto.setChargeAmount(r.getChargeAmount().negate());
		fBillTxdto.setCreateTime(r.getCreateTime().getTime());
		fBillTxdto.setDescription(r.getDescription());
		fBillTxdto.setId(r.getId());
	}

	private Group checkFamily(Long familyId) {
		Group family = this.groupProvider.findGroupById(familyId);
		if(family == null){
			LOGGER.error("the family is not exist.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the family is not exist");
		}
		return family;
	}

	private void checkFamilyIdIsNull(Long familyId) {
		if(familyId == null){
			LOGGER.error("familyId paramter is null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"familyId paramter is null or empty");
		}
	}

	private void checkAddressIdIsNull(Long addressId) {
		if(addressId == null){
			LOGGER.error("addressId paramter is null or empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"addressId paramter is null or empty");
		}
	}

	@Override
	public PmBillsDTO findBillByAddressIdAndTime(FindBillByAddressIdAndTimeCommand cmd) {
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkBillDate(cmd.getBillDate());
		this.checkAddress(cmd.getAddressId());

		PmBillsDTO billDto = new PmBillsDTO();

		Long addressId = cmd.getAddressId();

		java.sql.Date startDate = null;
		java.sql.Date endDate = null;
		if(cmd.getBillDate() != null && !cmd.getBillDate().equals("")){
			startDate = new java.sql.Date(this.getFirstDayOfMonthByStr(cmd.getBillDate()).getTime());
			endDate =  new java.sql.Date(this.getLastDayOfMonthByStr(cmd.getBillDate()).getTime());
		}
		CommunityPmBill communityBill = this.propertyMgrProvider.findPmBillByAddressAndDate(addressId,startDate,endDate);
		if(communityBill != null){
			billDto = this.convertBillToDto(communityBill);
			BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
			this.setPmBillAmounts(billDto, payedAmount);
		}
		return billDto;
	}

	private void checkBillDate(String billDate) {
		if(billDate == null || billDate.equals("")){
			LOGGER.error("billDate paramter is empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"billDate paramter is empty");
		}

	}

	@Override
	public PmBillsDTO findFamilyBillAndPaysByFamilyIdAndTime(FindFamilyBillAndPaysByFamilyIdAndTimeCommand cmd) {
		this.checkFamilyIdIsNull(cmd.getFamilyId());
		Group family = this.checkFamily(cmd.getFamilyId());
		FindBillByAddressIdAndTimeCommand command = new FindBillByAddressIdAndTimeCommand();
		command.setBillDate(cmd.getBillDate());
		command.setAddressId(family.getIntegralTag1());
		PmBillsDTO billDto = this.findBillByAddressIdAndTime(command);
		if(billDto != null){
			//账单缴费记录
			this.setBillTx(billDto,cmd.getFamilyId());
		}
		return billDto;
	}

	private PmBillsDTO convertBillToDto(CommunityPmBill communityPmBill) {
		PmBillsDTO billDto = ConvertHelper.convert(communityPmBill, PmBillsDTO.class);
		this.convertBillDateToDto(communityPmBill, billDto);
		return billDto;
	}

	private void setBillTx(PmBillsDTO billDto,Long familyId) {
		List<FamilyBillingTransactionDTO> payDtoList = new ArrayList<FamilyBillingTransactionDTO>();
		BigDecimal payedAmount = BigDecimal.ZERO;
		List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billDto.getId(),OrganizationOrderStatus.PAID.getCode());
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
		this.sortFBillTxDtosByCreateTimeDesc(payDtoList);
		this.setPmBillAmounts(billDto, payedAmount);
		billDto.setPayList(payDtoList);
	}

	private void sortFBillTxDtosByCreateTimeDesc(List<FamilyBillingTransactionDTO> payDtoList) {
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

	}

	@Override
	public ListFamilyBillsAndPaysByFamilyIdCommandResponse listFamilyBillsAndPaysByFamilyId(ListFamilyBillsAndPaysByFamilyIdCommand cmd) {
		//this.checkFamilyIdIsNull(cmd.getFamilyId());
		ListFamilyBillsAndPaysByFamilyIdCommandResponse response = new ListFamilyBillsAndPaysByFamilyIdCommandResponse();
		List<PmBillsDTO> billList = new ArrayList<PmBillsDTO>();
		response.setBillDate(cmd.getBillDate());
		//左邻名称和电话
		this.setZlInfoToResponse(response);
		//物业名称和电话
		this.setPmInfoToResponse(response,cmd.getCommunityId());

		if(cmd.getFamilyId() == null){
			LOGGER.error("familyId paramter is null or empty");
		}
		else{
			//Group family = this.checkFamily(cmd.getFamilyId());
			Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
			if(family == null){
				LOGGER.error("the family is not exist.");
			}
			else{
				Long addressId = family.getIntegralTag1();
				//向统一支付发请求,查询订单支付状态
				LOGGER.error("listFamilyBillsAndPaysByFamilyId-remoteUpdate");
				//remoteRefreshOrgOrderStatus();

				if(cmd.getPageOffset() == null)
					cmd.setPageOffset(1L);
				int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
				long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

				List<CommunityPmBill> commBillList = this.organizationProvider.listOrganizationBillsByAddressId(addressId, offset, pageSize+1);
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
			}
		}

		response.setRequests(billList);
		return response;
	}

	private Organization checkOrganizationByAddressId(Long addressId) {
		Organization org = this.findOrganizationByAddressId(addressId);
		if(org == null){
			LOGGER.error("could not found organization by addressId.addressId="+addressId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"could not found organization by addressId.");
		}
		return org;
	}

	private void setZlInfoToResponse(ListFamilyBillsAndPaysByFamilyIdCommandResponse response) {
		response.setZlName("深圳市永佳天成科技发展有限公司");
		response.setZlTelephone("4008384688");
	}

	private void setPmInfoToResponse(ListFamilyBillsAndPaysByFamilyIdCommandResponse response,Long communityId) {
		if(communityId == null)
			return ;
		Organization organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, OrganizationType.PM.getCode());
		if(organization != null){
			response.setOrgIsExist(Byte.valueOf((byte)1));
			response.setOrgName(organization.getName());
			List<CommunityPmContact> contacts = this.propertyMgrProvider.listCommunityPmContacts(organization.getId());
			if(contacts != null && !contacts.isEmpty()){
				for(CommunityPmContact contact : contacts)
					if(contact.getContactType().compareTo(IdentifierType.MOBILE.getCode()) == 0)
						response.setOrgTelephone(contact.getContactToken());
			}
		}
		else
			response.setOrgIsExist(Byte.valueOf((byte)0));
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
	public int payPmBillByAddressId(PayPmBillByAddressIdCommand cmd) {
		if(cmd.getPaidType() == null || cmd.getPayAmount() == null || cmd.getPayTime() == null || cmd.getTxType() == null){
			LOGGER.error("paidType or payAmount or payTime or txType paramteris empty");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"paidType or payAmount or payTime or txType paramter is empty");
		}
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkAddress(cmd.getAddressId());
		Long addressId = cmd.getAddressId();

		this.checkOrganizationByAddressId(addressId);

		CommunityPmBill bill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
		if(bill == null){
			LOGGER.error("the bill is not exist by addressId="+addressId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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

			FamilyBillingAccount fAccount = this.findOrNewFBillAcount(bill.getEntityId(),currentTimestamp);
			OrganizationBillingAccount oAccount = this.findOrNewOBillAccount(bill.getOrganizationId(),currentTimestamp);

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
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		this.checkOrganization(cmd.getOrganizationId());

		GetPmPayStatisticsCommandResponse result = new GetPmPayStatisticsCommandResponse();
		int oweFamilyCount = 0;
		BigDecimal waitPayAmounts = BigDecimal.ZERO;
		BigDecimal yearIncomeAmount = BigDecimal.ZERO;

		List<CommunityPmBill> billList = this.propertyMgrProvider.listNewestPmBillsByOrgId(cmd.getOrganizationId(), null);
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0){//该家庭欠费
					waitPayAmounts = waitPayAmounts.add(waitPayAmount);
					oweFamilyCount++;
				}
			}
		}
		yearIncomeAmount = this.propertyMgrProvider.countPmYearIncomeByOrganizationId(cmd.getOrganizationId(),BillTransactionResult.SUCCESS.getCode());
		if(yearIncomeAmount == null)
			yearIncomeAmount = BigDecimal.ZERO;

		result.setOweFamilyCount(oweFamilyCount);
		result.setUnPayAmount(waitPayAmounts);
		result.setYearIncomeAmount(yearIncomeAmount);
		return result;
	}

	@Override
	public void sendPmPayMessageByAddressId(SendPmPayMessageByAddressIdCommand cmd) {
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkAddress(cmd.getAddressId());
		Long addressId = cmd.getAddressId();

		Organization org = this.checkOrganizationByAddressId(addressId);

		CommunityPmBill bill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
		if(bill == null){
			LOGGER.error("the bill is not exist by addressId="+addressId);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill is not exist");
		}

		BigDecimal payAmount = this.countPmBillPaidAmount(bill.getId());
		BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(payAmount);
		if(waitPayAmount.compareTo(BigDecimal.ZERO) <= 0){//不欠费
			LOGGER.error("The family don't owed pm fee.Should not send pm pay message.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"The family don't owed pm fee.Should not send pm pay message.");
		}
		//短信发送物业缴费通知
		this.dbProvider.execute(s -> {
			List<Tuple<String, Object>> variables  = this.getPmPayVariables(bill,waitPayAmount,payAmount);
			this.sendPmPayMessageToUnRegisterUserInFamily(org.getId(),addressId,variables);
			bill.setNotifyCount((bill.getNotifyCount() == null?0:bill.getNotifyCount())+1);
			bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
			this.organizationProvider.updateOrganizationBill(bill);

			return s;
		});
	}

	private Organization findOrganizationByAddressId(Long addressId) {
		CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByAddressId(addressId);
		if(addressMapping != null){
			return this.organizationProvider.findOrganizationById(addressMapping.getOrganizationId());
		}
		return null;
	}

	private void sendPmPayMessageToUnRegisterUserInFamily(Long organizationId,Long addressId,List<Tuple<String, Object>> variables) {
		List<OrganizationOwner> orgOwnerList = this.organizationProvider.listOrganizationOwnersByOrgIdAndAddressId(organizationId,addressId);
		if(orgOwnerList != null && !orgOwnerList.isEmpty()){
			for(OrganizationOwner orgOwner : orgOwnerList){
				if(orgOwner.getContactToken() != null){
					String templateScope = SmsTemplateCode.SCOPE;
					int templateId = SmsTemplateCode.WY_BILL_CODE;
					String templateLocale = currentLocale();
					smsProvider.sendSms(UserContext.current().getUser().getNamespaceId(), orgOwner.getContactToken(), templateScope, templateId, templateLocale, variables);
				}
			}
		}
	}

	@Override
	public void sendPmPayMessageToAllOweFamilies(SendPmPayMessageToAllOweFamiliesCommand cmd) {
		this.checkOrganizationIdIsNull(cmd.getOrganizationId());
		Organization organization = this.checkOrganization(cmd.getOrganizationId());

		List<CommunityPmBill> billList = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(),null);
		if(billList != null && !billList.isEmpty()){
			for(CommunityPmBill bill : billList){
				BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
				BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
				if(waitPayAmount.compareTo(BigDecimal.ZERO) > 0){//欠费
					Long addressId = bill.getEntityId();
					//短信发送物业缴费通知
					this.dbProvider.execute(s -> {
						List<Tuple<String, Object>> variables  = this.getPmPayVariables(bill,waitPayAmount,paidAmount);
						this.sendPmPayMessageToUnRegisterUserInFamily(organization.getId(),addressId,variables);
						bill.setNotifyCount((bill.getNotifyCount() == null?0:bill.getNotifyCount())+1);
						bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
						this.organizationProvider.updateOrganizationBill(bill);
						return s;
					});
				}
			}

		}
	}

	private  List<Tuple<String, Object>> getPmPayVariables (CommunityPmBill bill, BigDecimal balance, BigDecimal payAmount){Calendar cal = Calendar.getInstance();
	cal.setTime(bill.getStartDate());
	List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_YEAR,String.valueOf(cal.get(Calendar.YEAR)) );
	int month = cal.get(Calendar.MONTH)+1;
	if(month < 10)
		smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_MONTH, "0"+month);
	else
		smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_MONTH, month);
	smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DUEAMOUNT, bill.getDueAmount());
	smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_OWEAMOUNT, bill.getOweAmount());
	smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_PAYAMOUNT, payAmount);
	smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_BALANCE, balance);
	if(bill.getDescription() != null && !bill.getDescription().isEmpty()){
		if(bill.getDescription().length() > 15)
			smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, bill.getDescription().substring(0, 14)+"...");
		else
			smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, bill.getDescription()+"。");
	}
	else{
		smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, "" );
	}
	return variables;

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
		if(bill.getDescription() != null && !bill.getDescription().isEmpty()){
			if(bill.getDescription().length() > 15)
				builder.append("，"+bill.getDescription().substring(0, 14)+"...");
			else builder.append("，"+bill.getDescription());

		}
		builder.append("。");
		builder.append("请尽快使用左邻缴纳物业费。");
		return builder.toString();
	}
    @Override
	public GetFamilyStatisticCommandResponse getFamilyStatistic(GetFamilyStatisticCommand cmd) {
		this.checkFamilyIdIsNull(cmd.getFamilyId());
		Group family = this.checkFamily(cmd.getFamilyId());
		Long addressId = family.getIntegralTag1();

		Organization org = this.checkOrganizationByAddressId(addressId);

		GetFamilyStatisticCommandResponse response = new GetFamilyStatisticCommandResponse();
		BigDecimal totalDueOweAmount = this.countFamilyPmBillDueAndOweAmountInYear(org.getId(),addressId);
		BigDecimal totalPaidAmount = this.familyProvider.countFamilyBillTxChargeAmountInYear(addressId);
		BigDecimal nowWaitPayAmount = BigDecimal.ZERO;

		CommunityPmBill bill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
		if(bill != null){
			BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
			nowWaitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
		}

		response.setNowWaitPayAmount(nowWaitPayAmount);
		response.setTotalDueOweAmount(totalDueOweAmount);
		response.setTotalPaidAmount(totalPaidAmount.negate());
		return response;
	}


	@Override
	public PmBillsDTO findNewestBillByAddressId(FindNewestBillByAddressIdCommand cmd) {
		this.checkAddressIdIsNull(cmd.getAddressId());
		this.checkAddress(cmd.getAddressId());
		Long addressId = cmd.getAddressId();

		PmBillsDTO billDto = new PmBillsDTO();

		CommunityPmBill communityBill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
		if(communityBill != null){
			billDto = this.convertBillToDto(communityBill);
			BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
			this.setPmBillAmounts(billDto, payedAmount);
		}

		return billDto;
	}

	private Address checkAddress(Long addressId) {
		Address address = this.addressProvider.findAddressById(addressId);
		if(address == null){
			LOGGER.error("Unable to find the address.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the address.");
		}
		return address;

	}

	@Override
	public void onlinePayPmBill(OnlinePayPmBillCommand cmd) {
		//fail
		if(cmd.getPayStatus().toLowerCase().equals("fail"))
			this.onlinePayPmBillFail(cmd);
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
			this.onlinePayPmBillSuccess(cmd);
	}

	private void onlinePayPmBillFail(OnlinePayPmBillCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayPmBillFail");

		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);

		Date cunnentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
		this.updateOrderStatus(order,0L,currentTimestamp,OrganizationOrderStatus.INACTIVE.getCode());
	}

	private void onlinePayPmBillSuccess(OnlinePayPmBillCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayPmBillSuccess");

		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());

		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);
		BigDecimal payAmount = new BigDecimal(cmd.getPayAmount());//支付金额
		CommunityPmBill bill = this.checkPmBill(order.getBillId());
		this.checkVendorTypeFormat(cmd.getVendorType());

		Long payTime = System.currentTimeMillis();
		/*if(cmd.getPayTime() != null)
			payTime = Long.valueOf(cmd.getPayTime());*/
		Timestamp payTimeStamp = new Timestamp(payTime);//支付时间

		Long cunnentTime = System.currentTimeMillis();
		Timestamp currentTimestamp = new Timestamp(cunnentTime);

		if(LOGGER.isDebugEnabled()){
			LOGGER.error("before lock.order="+order.toString());
		}
		if(order.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()){
			String uuidStr = UUID.randomUUID().toString();
			//pre-set-parameter-familyTx
			FamilyBillingTransactions familyTx = new FamilyBillingTransactions();
			familyTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			familyTx.setChargeAmount(payAmount.negate());
			familyTx.setCreateTime(payTimeStamp);
			if(cmd.getDescription() != null && !cmd.getDescription().isEmpty())
				familyTx.setDescription(cmd.getDescription());
			familyTx.setOperatorUid(0L);
			familyTx.setOwnerId(bill.getEntityId());
			familyTx.setPaidType(PaidType.SELFPAY.getCode());
			familyTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//familyTx.setResultCodeScope("test");
			familyTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			familyTx.setTargetAccountType(AccountType.ORGANIZATION.getCode());
			familyTx.setTxSequence(uuidStr);
			familyTx.setTxType(TxType.ONLINE.getCode());
			familyTx.setVendor(cmd.getVendorType());
			familyTx.setPayAccount(cmd.getPayAccount());
			//pre-set-parameter-orgTx
			OrganizationBillingTransactions orgTx = new OrganizationBillingTransactions();
			orgTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			orgTx.setChargeAmount(payAmount);
			orgTx.setCreateTime(payTimeStamp);
			if(cmd.getDescription() != null && !cmd.getDescription().isEmpty())
				orgTx.setDescription(cmd.getDescription());
			orgTx.setOperatorUid(0L);
			orgTx.setOwnerId(bill.getOrganizationId());
			orgTx.setPaidType(PaidType.SELFPAY.getCode());
			orgTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//orgTx.setResultCodeScope("test");
			orgTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			orgTx.setTargetAccountType(AccountType.FAMILY.getCode());
			orgTx.setTxSequence(uuidStr);
			orgTx.setTxType(TxType.ONLINE.getCode());
			orgTx.setVendor(cmd.getVendorType());
			orgTx.setPayAccount(cmd.getPayAccount());

			Tuple<OrganizationOrder, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PM_ORDER.getCode()).enter(() -> {
				long lqStartTime = System.currentTimeMillis();
				OrganizationOrder order2 = this.organizationProvider.findOrganizationOrderById(orderId);
				long lqEndTime = System.currentTimeMillis();
				LOGGER.error("find pm order in the lock.elapse="+(lqEndTime-lqStartTime));

				if(LOGGER.isDebugEnabled()){
					LOGGER.error("in lock.order="+order2.toString());
				}

				if(order2.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()){
					long luStartTime = System.currentTimeMillis();
					this.dbProvider.execute(s -> {
						order2.setAmount(payAmount);
						this.updateOrderStatus(order2,0L,payTimeStamp,OrganizationOrderStatus.PAID.getCode());

						FamilyBillingAccount fAccount = this.findOrNewFBillAcountNoLock(bill.getEntityId(),currentTimestamp);
						OrganizationBillingAccount oAccount = this.findOrNewOBillAccountNoLock(bill.getOrganizationId(),currentTimestamp);

						familyTx.setOrderId(order2.getId());
						familyTx.setOwnerAccountId(fAccount.getId());
						familyTx.setTargetAccountId(oAccount.getId());
						this.familyProvider.createFamilyBillingTransaction(familyTx);

						orgTx.setOrderId(order2.getId());
						orgTx.setOwnerAccountId(oAccount.getId());
						orgTx.setTargetAccountId(fAccount.getId());
						this.organizationProvider.createOrganizationBillingTransaction(orgTx);

						if(LOGGER.isDebugEnabled()){
							LOGGER.error("check online pm pay amount.oAccountAmount="+oAccount.getBalance()+".payAmount="+payAmount+".order="+order2.toString());
						}

						//线上支付,将金额存到物业账号中
						oAccount.setBalance(oAccount.getBalance().add(payAmount));
						oAccount.setUpdateTime(currentTimestamp);
						this.organizationProvider.updateOrganizationBillingAccount(oAccount);

						return true;
					});
					long luEndTime = System.currentTimeMillis();
					LOGGER.error("update pm order in the lock.elapse="+(luEndTime-luStartTime));
				}
				return order2;
			});

			if(LOGGER.isDebugEnabled()){
				LOGGER.error("pm order lock finish.status="+result.second());
			}
		}
	}

	private OrganizationBillingAccount findOrNewOBillAccountNoLock(Long orgId,Timestamp currentTimestamp) {
		OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(orgId);
		if(oAccount == null){
			oAccount = new OrganizationBillingAccount();
			oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
			oAccount.setBalance(BigDecimal.ZERO);
			oAccount.setCreateTime(currentTimestamp);
			oAccount.setOwnerId(orgId);
			this.organizationProvider.createOrganizationBillingAccount(oAccount);
		}
		return oAccount;
	}

	private FamilyBillingAccount findOrNewFBillAcountNoLock(Long entityId,Timestamp currentTimestamp) {
		FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
		if(fAccount == null){
			fAccount = new FamilyBillingAccount();
			fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
			fAccount.setBalance(BigDecimal.ZERO);
			fAccount.setCreateTime(currentTimestamp);
			fAccount.setOwnerId(entityId);
			this.familyProvider.createFamilyBillingAccount(fAccount);
		}
		return fAccount;
	}

	private OrganizationBillingAccount findOrNewOBillAccount(Long orgId,Timestamp currentTimestamp) {
		OrganizationBillingAccount oAccount1 = this.organizationProvider.findOrganizationBillingAccount(orgId);
		if(oAccount1 == null){
			Tuple<OrganizationBillingAccount, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_O_BILL_ACCOUNT.getCode()).enter(() -> {
				OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(orgId);
				if(oAccount == null){
					oAccount = new OrganizationBillingAccount();
					oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
					oAccount.setBalance(BigDecimal.ZERO);
					oAccount.setCreateTime(currentTimestamp);
					oAccount.setOwnerId(orgId);
					this.organizationProvider.createOrganizationBillingAccount(oAccount);
				}
				return oAccount;
			});
			oAccount1 = result.first();
		}
		return oAccount1;
	}

	private FamilyBillingAccount findOrNewFBillAcount(Long entityId,Timestamp currentTimestamp) {
		FamilyBillingAccount fAccount1 = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
		if(fAccount1 == null){
			Tuple<FamilyBillingAccount, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_F_BILL_ACCOUNT.getCode()).enter(() -> {
				FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
				if(fAccount == null){
					fAccount = new FamilyBillingAccount();
					fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
					fAccount.setBalance(BigDecimal.ZERO);
					fAccount.setCreateTime(currentTimestamp);
					fAccount.setOwnerId(entityId);
					this.familyProvider.createFamilyBillingAccount(fAccount);
				}
				return fAccount;
			});
			fAccount1 = result.first();
		}
		return fAccount1;
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}
	}

	private CommunityPmBill checkPmBill(Long billId) {
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(billId);
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}
		return bill;
	}

	private void checkAmountIsEqual(BigDecimal amount, BigDecimal waitPayAmount) {
		if(amount.compareTo(waitPayAmount) != 0){
			LOGGER.error("order amount not equal payAmount.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"order amount not equal payAmount.");
		}

	}

	private OrganizationOrder checkOrder(Long orderId) {
		OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
		if(order == null){
			LOGGER.error("the order not found.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}

	private void checkPayAccountIsNull(String payAccount) {
		if(payAccount == null || payAccount.trim().equals("")){
			LOGGER.error("payAccount is null or empty.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAccount is null or empty.");
		}

	}

	private void checkPayTimeIsNull(String payTime) {
		if(payTime == null || payTime.trim().equals("")){
			LOGGER.error("payTime is null or empty.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payTime is null or empty.");
		}

	}

	private void checkPayAmountIsNull(String payAmount) {
		if(payAmount == null || payAmount.trim().equals("")){
			LOGGER.error("payAmount is null or empty.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAmount or is null or empty.");
		}

	}

	private void checkVendorTypeIsNull(String vendorType) {
		if(vendorType == null || vendorType.trim().equals("")){
			LOGGER.error("vendorType is null or empty.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendorType is null or empty.");
		}

	}

	private void checkOrderNoIsNull(String orderNo) {
		if(orderNo == null || orderNo.trim().equals("")){
			LOGGER.error("orderNo is null or empty.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
		if(order == null){
			LOGGER.error("the order not found.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(order.getBillId());
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}

		BigDecimal payedAmount = this.countPmBillPaidAmount(bill.getId());
		BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(payedAmount);

		PmBillForOrderNoDTO billDto = new PmBillForOrderNoDTO();
		billDto.setBillName(bill.getName());
		billDto.setPayAmount(waitPayAmount);
		return billDto;
	}

	@Override
	public OrganizationOrderDTO createPmBillOrder(CreatePmBillOrderCommand cmd){
		try{
			if(cmd.getBillId() == null || cmd.getAmount() == null){
				LOGGER.error("billId or amount is null.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"billId or amount is null.");
			}
			CommunityPmBill bill = this.organizationProvider.findOranizationBillById(cmd.getBillId());
			if(bill == null){
				LOGGER.error("the bill not found.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"the bill not found.");
			}
			CommunityPmBill nowBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
			if(nowBill == null || nowBill.getId().compareTo(bill.getId()) != 0){
				LOGGER.error("the bill is invalid.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			dto.setOrderType("wuye");
			dto.setOrderNo(orderNo);
			dto.setName(bill.getName());
			dto.setDescription(order.getDescription());
			dto.setAmount(cmd.getAmount());
			this.setSignatureParam(dto);
			return dto;
		}catch (Exception e){
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					e.getMessage());
		}
	}

	private void setSignatureParam(OrganizationOrderDTO dto)throws Exception {
		String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);

		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",dto.getAmount().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		dto.setAppKey(appKey);
		dto.setRandomNum(randomNum);
		dto.setSignature(URLEncoder.encode(signature,"UTF-8"));
		dto.setTimestamp(timestamp);
	}

	private String convertToVerdorType(String verdorTypeStr) {
		if(verdorTypeStr.equals("alipay"))
			return "10001";
		return "10002";
	}

	private void checkPayStatusIsNull(String payStatus,String orderNo) {
		if(payStatus == null){
			LOGGER.error("payStatus is null.orderNo="+orderNo);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payStatus is null.");
		}
	}

	private void checkResultHolderIsNull(ResultHolder resultHolder,String orderNo) {
		if(resultHolder == null){
			LOGGER.error("remote search pay order return null.orderNo="+orderNo);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"remote search pay order return null.");
		}
	}

	@Override
	public CommonOrderDTO createPmBillOrderDemo(CreatePmBillOrderCommand cmd) {
		try{
			//自定义接口处理
			if(cmd.getBillId() == null || cmd.getAmount() == null){
				LOGGER.error("billId or amount is null.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"billId or amount is null.");
			}
			CommunityPmBill bill = this.organizationProvider.findOranizationBillById(cmd.getBillId());
			if(bill == null){
				LOGGER.error("the bill not found.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"the bill not found.");
			}
			CommunityPmBill nowBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
			if(nowBill == null || nowBill.getId().compareTo(bill.getId()) != 0){
				LOGGER.error("the bill is invalid.");
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
			String orderNo = this.convertOrderIdToOrderNo(order.getId());
			//调用统一处理订单接口，返回统一订单格式
			CommonOrderCommand orderCmd = new CommonOrderCommand();
			orderCmd.setBody(bill.getName());
			orderCmd.setOrderNo(orderNo);
			orderCmd.setOrderType(OrderType.OrderTypeEnum.WUYETEST.getPycode());
			orderCmd.setSubject("物业订单简要描述");
			orderCmd.setTotalFee(cmd.getAmount());
			CommonOrderDTO dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
			return dto;
		}catch (Exception e){
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					e.getMessage());
		}
	}

    @Override
    public OrganizationOwnerDTO updateOrganizationOwner(UpdateOrganizationOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Tuple<CommunityPmOwner, Boolean> tuple =
                coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ORGANIZATION_OWNER.getCode() + cmd.getId()).enter(() -> {
            CommunityPmOwner owner = propertyMgrProvider.findPropOwnerById(cmd.getId());
            if (owner == null) {
                LOGGER.error("Organization owner are not exist, id = {}", cmd.getId());
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                        "Organization owner are not exist, id = %s", cmd.getId());
            }
            boolean needUpdateDoc = false;
            if (cmd.getContactName() != null) {
                owner.setContactName(cmd.getContactName());
                needUpdateDoc = true;
            }
            if (cmd.getAvatar() != null)
                owner.setAvatar(cmd.getAvatar());
            if (cmd.getBirthday() != null)
                owner.setBirthday(new java.sql.Date(cmd.getBirthday()));
            if (cmd.getOrgOwnerTypeId() != null)
                owner.setOrgOwnerTypeId(cmd.getOrgOwnerTypeId());
            if (cmd.getMaritalStatus() != null)
                owner.setMaritalStatus(cmd.getMaritalStatus());
            if (cmd.getJob() != null)
                owner.setJob(cmd.getJob());
            if (cmd.getCompany() != null)
                owner.setCompany(cmd.getCompany());
            if (cmd.getIdCardNumber() != null)
                owner.setIdCardNumber(cmd.getIdCardNumber());
            if (cmd.getRegisteredResidence() != null)
                owner.setRegisteredResidence(cmd.getRegisteredResidence());
            UserGender gender = UserGender.fromCode(cmd.getGender());
            if (gender != null) {
                owner.setGender(gender.getCode());
            }
            propertyMgrProvider.updatePropOwner(owner);
            if (needUpdateDoc) {
                pmOwnerSearcher.feedDoc(owner);
            }
            return owner;
        });
        return convertOwnerToDTO(tuple.first());
    }

    @Override
    public long createOrganizationOwnerByUser(User memberUser, String contactToken) {
        CommunityPmOwner newPmOwner = new CommunityPmOwner();
        newPmOwner.setContactName(memberUser.getNickName());
        newPmOwner.setNamespaceId(memberUser.getNamespaceId());
        newPmOwner.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        newPmOwner.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
        newPmOwner.setAvatar(memberUser.getAvatar());
        newPmOwner.setOrgOwnerTypeId(8L);// 没想到怎么做比较好, 先写死, 归类为"无"
        newPmOwner.setGender(memberUser.getGender());
        newPmOwner.setCommunityId(memberUser.getCommunityId());
        newPmOwner.setContactType(ContactType.MOBILE.getCode());
        newPmOwner.setContactToken(contactToken);
        newPmOwner.setBirthday(memberUser.getBirthday());
        newPmOwner.setCompany(memberUser.getCompany());
        Long organizationId = findPropertyOrganizationId(memberUser.getCommunityId());
        newPmOwner.setOrganizationId(organizationId);

        long ownerId = propertyMgrProvider.createPropOwner(newPmOwner);
        pmOwnerSearcher.feedDoc(newPmOwner);
        return ownerId;
    }

	@Override
	public OrganizationOwnerDTO createOrganizationOwner(CreateOrganizationOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(cmd.getOrgOwnerTypeId());
        if (ownerType == null) {
            invalidParameterException("orgOwnerTypeId", cmd.getOrgOwnerTypeId());
        }
        checkContactTokenUnique(cmd.getCommunityId(), cmd.getContactToken());

        User currentUser = UserContext.current().getUser();
        CommunityPmOwner owner = ConvertHelper.convert(cmd, CommunityPmOwner.class);
        if (cmd.getBirthday() != null) {
            owner.setBirthday(new java.sql.Date(cmd.getBirthday()));
        }
        owner.setOrgOwnerTypeId(ownerType.getId());
        owner.setNamespaceId(currentUser.getNamespaceId());
        owner.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
        UserGender gender = UserGender.fromCode(cmd.getGender());
        if (gender != null) {
            owner.setGender(gender.getCode());
        }

        this.dbProvider.execute(status -> {
            long ownerId = propertyMgrProvider.createPropOwner(owner);
            // 处理上传附件
            if (cmd.getOwnerAttachments() != null && !cmd.getOwnerAttachments().isEmpty()) {
                cmd.getOwnerAttachments().forEach(r -> {
                    r.setOrgOwnerId(ownerId);
                    r.setOrganizationId(cmd.getOrganizationId());
                    this.uploadOrganizationOwnerAttachment(r);
                });
            }
            for(OrganizationOwnerAddressCommand addressCmd : cmd.getAddresses()) {
                Address address = addressProvider.findAddressById(addressCmd.getAddressId());
                if(address != null) {
                    OrganizationOwnerAddress ownerAddress = new OrganizationOwnerAddress();
                    ownerAddress.setAddressId(address.getId());
                    ownerAddress.setNamespaceId(currentUser.getNamespaceId());
                    ownerAddress.setOrganizationOwnerId(ownerId);
                    ownerAddress.setLivingStatus(addressCmd.getLivingStatus());
					ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.INACTIVE.getCode());
                    if (addressCmd.getCheckInDate() != null) {
                        createOrganizationOwnerBehavior(ownerId, address.getId(), addressCmd.getCheckInDate(), OrganizationOwnerBehaviorType.IMMIGRATION);
                    }
                    // 如果小区里有该手机号的用户, 则自动审核当前客户
                    autoApprovalOrganizationOwnerAddress(cmd.getCommunityId(), cmd.getContactToken(), ownerAddress);
                    propertyMgrProvider.createOrganizationOwnerAddress(ownerAddress);
                    // getIntoFamily(address, cmd.getContactToken(), currentUser.getNamespaceId());
                } else {
                    LOGGER.error("CreateOrganizationOwner: address id is wrong! addressId = {}", addressCmd.getAddressId());
                }
            }
            pmOwnerSearcher.feedDoc(owner);
            return null;
        });
        return convertOwnerToDTO(owner);
    }

    // 如果小区里有该手机号的用户, 则自动审核当前客户
    private void autoApprovalOrganizationOwnerAddress(Long communityId, String contactToken, OrganizationOwnerAddress ownerAddress) {
        GroupMember member = getGroupMemberByContactToken(communityId, contactToken, ownerAddress);
        if (member != null) {
            ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
            // 审核当前用户
            ApproveMemberCommand cmd = new ApproveMemberCommand();
            cmd.setId(member.getGroupId());
            cmd.setMemberUid(member.getMemberId());
            cmd.setAddressId(ownerAddress.getAddressId());
            familyService.adminApproveMember(cmd);
        }
    }

    private GroupMember getGroupMemberByContactToken(Long communityId, String contactToken, OrganizationOwnerAddress ownerAddress) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(currentNamespaceId(), contactToken);
        if(userIdentifier != null) {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            if (user != null) {
                List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
                    query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
                    query.addConditions(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(ownerAddress.getAddressId()));
                    return query;
                });
                if (groups != null && groups.size() > 0) {
                    return groupProvider.findGroupMemberByMemberInfo(groups.get(0).getId(), EntityType.USER.getCode(), user.getId());
                }
            }
        }
        return null;
    }

    // 自动审核group member
    @Override
    public void autoApprovalGroupMember(Long userId, Long communityId, Long groupId, Long addressId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if(userIdentifier != null) {
            Integer namespaceId = currentNamespaceId();
            CommunityPmOwner pmOwner = propertyMgrProvider.findOrganizationOwnerByCommunityIdAndContactToken(namespaceId,
                    communityId, userIdentifier.getIdentifierToken());
            if (pmOwner != null) {
                OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(namespaceId, pmOwner.getId(), addressId);
                if (ownerAddress != null) {
                    ApproveMemberCommand approveCmd = new ApproveMemberCommand();
                    approveCmd.setMemberUid(userId);
                    approveCmd.setId(groupId);
                    approveCmd.setAddressId(ownerAddress.getAddressId());
                    familyService.adminApproveMember(approveCmd);
                }
            }
        }
    }

    private void createOrganizationOwnerBehavior(long ownerId, Long addressId, Long date, OrganizationOwnerBehaviorType behaviorType) {
        OrganizationOwnerBehavior behavior = new OrganizationOwnerBehavior();
        behavior.setAddressId(addressId);
        behavior.setOwnerId(ownerId);
        behavior.setBehaviorType(behaviorType.getCode());
        behavior.setNamespaceId(currentNamespaceId());
        if (date != null) {
            behavior.setBehaviorTime(new Timestamp(date));
        }
        behavior.setStatus(OrganizationOwnerBehaviorStatus.NORMAL.getCode());
        propertyMgrProvider.createOrganizationOwnerBehavior(behavior);
    }

    @Override
	public void deletePMPropertyOwnerAddress(DeletePropOwnerAddressCommand cmd) {
		CommunityPmOwner owner = propertyMgrProvider.findPropOwnerById(cmd.getId());
		if(owner.getCommunityId() == cmd.getCommunityId()) {
			this.dbProvider.execute((TransactionStatus status) -> {
				propertyMgrProvider.deletePropOwner(owner);
				pmOwnerSearcher.deleteById(owner.getId());
				//tuichujiating
				leaveFamily(owner.getAddressId(), owner.getContactToken(), owner.getNamespaceId());

				return null;
			});
		} else {
			LOGGER.error("deletePMPropertyOwnerAddress: id is not in the community! id = " + cmd.getId() + ", communityId = " + cmd.getCommunityId());
			throw errorWith(PropertyServiceErrorCode.SCOPE,PropertyServiceErrorCode.ERROR_OWNER_COMMUNITY,
					"id is not in the community!");
		}

	}

	@Override
	public void createPMPropertyOwnerAddress(CreatePropOwnerAddressCommand cmd) {
		Integer namespaceId = currentNamespaceId();

		CommunityPmOwner owner = new CommunityPmOwner();
		owner.setCommunityId(cmd.getCommunityId());
		owner.setContactName(cmd.getContactName());
		owner.setContactToken(cmd.getContactToken());
		owner.setContactType(cmd.getContactType());
		owner.setOrganizationId(cmd.getOrganizationId());
		owner.setNamespaceId(namespaceId);

		Address address = addressProvider.findAddressById(cmd.getAddressId());
		if(null != address) {
			owner.setAddressId(address.getId());
			owner.setAddress(address.getAddress());

			this.dbProvider.execute((TransactionStatus status) -> {
				propertyMgrProvider.createPropOwner(owner);
				pmOwnerSearcher.feedDoc(owner);
				//jiarujiating
				getIntoFamily(address, cmd.getContactToken(), namespaceId);

				return null;
			});

		} else {
			LOGGER.error("createOrUpdateOrganizationOwner: address id is wrong! addressId = " + cmd.getAddressId());
		}

	}

	private void getIntoFamily(Address address, String contactToken, Integer namespaceId) {
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, contactToken);
		if(null != userIdentifier) {
			User user = userProvider.findUserById(userIdentifier.getOwnerUid());
			if(null != user) {
				address.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
				familyService.getOrCreatefamily(address, user);
			}
		}
	}

	private void leaveFamily(Long addressId, String contactToken, Integer namespaceId) {
		if(null == addressId){
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid addressId parameter");
        }
        Address address = this.addressProvider.findAddressById(addressId);
        if(null == address){
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Invalid addressId parameter,address is not found");
        }

        Family family = this.familyProvider.findFamilyByAddressId(addressId);
        if(null != family){
        	LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
            leaveCmd.setId(family.getId());
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, contactToken);
    		if(null != userIdentifier) {
    			User user = userProvider.findUserById(userIdentifier.getOwnerUid());
    			if(null != user) {
    				familyService.leave(leaveCmd, user);
    			}
    		}
        }
	}

	@Override
	public void processUserForOwner(UserIdentifier identifier) {
		// TODO Auto-generated method stub
		List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwnersByToken(
				identifier.getNamespaceId(), identifier.getIdentifierToken());
		if(null != owners && owners.size() > 0) {
			for(CommunityPmOwner owner : owners) {
				Address address = addressProvider.findAddressById(owner.getAddressId());
				//jiarujiating
				getIntoFamily(address, identifier.getIdentifierToken(), identifier.getNamespaceId());
			}
		}
	}

	@Override
    public void updateOrganizationOwnerAddressStatus(UpdateOrganizationOwnerAddressStatusCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        if (OrganizationOwnerBehaviorType.fromCode(cmd.getBehaviorType()) == null) {
            invalidParameterException("behaviorType", cmd.getBehaviorType());
        }
        Address address = addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            LOGGER.error("The address {} is not exist.", cmd.getAddressId());
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "The address %s is not exist.", cmd.getAddressId());
        }
        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                currentNamespaceId(), cmd.getOrgOwnerId(), address.getId());
        if (ownerAddress == null) {
            LOGGER.error("The organization owner address is not exist.");
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_ADDRESS,
                    "The organization owner address is not exist.");
        }
        OrganizationOwnerBehaviorType behaviorType = OrganizationOwnerBehaviorType.fromCode(cmd.getBehaviorType());
        if (!Objects.equals(ownerAddress.getLivingStatus(), behaviorType.getLivingStatus())) {
            ownerAddress.setLivingStatus(behaviorType.getLivingStatus());
            dbProvider.execute(status -> {
                propertyMgrProvider.updateOrganizationOwnerAddress(ownerAddress);
                // 创建用户行为记录
                createOrganizationOwnerBehavior(cmd.getOrgOwnerId(), address.getId(), System.currentTimeMillis(), behaviorType);
                return null;
            });
        }
        //  如果该地址的状态已经是该状态,则提示用户不用修改了
        else {
            LOGGER.error("The organization owner address livingStatus already is {}", behaviorType.getCode());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_ADDRESS_ALREADY_IS_THIS_STATUS,
                    "The organization owner address livingStatus already is %s", behaviorType.getCode());
        }
    }

    @Override
    public List<OrganizationOwnerBehaviorDTO> listOrganizationOwnerBehaviors(ListOrganizationOwnerBehaviorsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        User user = UserContext.current().getUser();
        List<OrganizationOwnerBehavior> behaviorList = propertyMgrProvider.listOrganizationOwnerBehaviors(user.getNamespaceId(), cmd.getOrgOwnerId());

        List<OrganizationOwnerBehaviorDTO> dtoList = new ArrayList<>();
        if (behaviorList != null && behaviorList.size() > 0) {
            for (OrganizationOwnerBehavior behavior : behaviorList) {
                Address address = addressProvider.findAddressById(behavior.getAddressId());
                OrganizationOwnerBehaviorDTO dto = new OrganizationOwnerBehaviorDTO();
                dto.setApartment(address.getApartmentName());
                dto.setBehaviorTime(behavior.getBehaviorTime());
                dto.setBuilding(address.getBuildingName());
                dto.setId(behavior.getId());
                LocaleString behaviorLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.BEHAVIOR_SCOPE,
                        behavior.getBehaviorType(), user.getLocale());
                dto.setBehaviorType(behaviorLocale != null ? behaviorLocale.getText() : null);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public void deleteOrganizationOwnerBehavior(DeleteOrganizationOwnerBehaviorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerBehavior behavior = propertyMgrProvider.findOrganizationOwnerBehaviorById(
                currentNamespaceId(), cmd.getId());
        if (behavior == null) {
            LOGGER.error("Organization owner behavior is not exist, id = {}", cmd.getId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Organization owner behavior is not exist, id = %s", cmd.getId());
        }
		if (!Objects.equals(behavior.getStatus(), OrganizationOwnerBehaviorStatus.DELETE.getCode())) {
			propertyMgrProvider.deleteOrganizationOwnerBehavior(behavior);
		}
    }

    @Override
    public void deleteOrganizationOwnerAttachment(DeleteOrganizationOwnerAttachmentCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerAttachment attachment = propertyMgrProvider.findOrganizationOwnerAttachment(
                currentNamespaceId(), cmd.getId());
        if (attachment == null) {
            LOGGER.error("Organization owner attachment is not exist. id = {}", cmd.getId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Organization owner attachment is not exist. id = %s", cmd.getId());
        }
        propertyMgrProvider.deleteOrganizationOwnerAttachment(attachment);
        createAuditLog(attachment.getId(), attachment.getClass());
    }

    @Override
    public void deleteOrganizationOwnerCarAttachment(DeleteOrganizationOwnerCarAttachmentCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerCarAttachment attachment = propertyMgrProvider.findOrganizationOwnerCarAttachment(
                currentNamespaceId(), cmd.getId());
        if (attachment == null) {
            LOGGER.error("Organization owner car attachment is not exist. id = {}", cmd.getId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Organization owner car attachment is not exist. id = %s", cmd.getId());
        }
        propertyMgrProvider.deleteOrganizationOwnerCarAttachment(attachment);
        createAuditLog(attachment.getId(), attachment.getClass());
    }

    @Override
	public void deleteOrganizationOwner(DeleteOrganizationOwnerCommand cmd) {
        validate(cmd);
		checkCurrentUserNotInOrg(cmd.getOrganizationId());

		CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getId());
		if (pmOwner == null) {
			LOGGER.error("OrganizationOwner is not exist. ownerId = {}", cmd.getId());
			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
					"OrganizationOwner is not exist. ownerId = %s", cmd.getId());
		}
		dbProvider.execute(status -> {
            if (!Objects.equals(pmOwner.getStatus(), OrganizationOwnerStatus.DELETE.getCode())) {
                Integer namespaceId = currentNamespaceId();
                leaveOrganizationOwnerAddress(pmOwner.getId());
                pmOwner.setStatus(OrganizationOwnerStatus.DELETE.getCode());
                propertyMgrProvider.updatePropOwner(pmOwner);
                propertyMgrProvider.deleteOrganizationOwnerAddressByOwnerId(namespaceId, pmOwner.getId());
                propertyMgrProvider.deleteOrganizationOwnerAttachmentByOwnerId(namespaceId, pmOwner.getId());
                propertyMgrProvider.deleteOrganizationOwnerOwnerCarByOwnerId(namespaceId, pmOwner.getId());
                pmOwnerSearcher.deleteById(pmOwner.getId());
                // leaveFamily(owner.getApartmentId(), owner.getContactToken(), owner.getNamespaceId());
            }
			return null;
		});
        createAuditLog(pmOwner.getId(), pmOwner.getClass());
	}

    private void leaveOrganizationOwnerAddress(Long orgOwnerId) {
        List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(currentNamespaceId(), orgOwnerId);
        if (ownerAddressList != null && !ownerAddressList.isEmpty()) {
            ownerAddressList.forEach(r -> {
                Family family = familyProvider.findFamilyByAddressId(r.getAddressId());
                if (family != null) {
                    leaveFamilyByOwnerId(orgOwnerId, family.getId());
                }
            });
        }
    }

    @Override
    public ListOrganizationOwnerCarResponse searchOrganizationOwnerCars(SearchOrganizationOwnerCarCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return ownerCarSearcher.query(cmd);
    }

    @Override
    public List<OrganizationOwnerAddressDTO> listOrganizationOwnerAddresses(ListOrganizationOwnerAddressesCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer namespaceId = currentNamespaceId();
        List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(namespaceId, cmd.getOrgOwnerId());

        return ownerAddressList.stream().map(r -> {
            Address address = addressProvider.findAddressById(r.getAddressId());
            OrganizationOwnerAddressDTO dto = new OrganizationOwnerAddressDTO();
            String locale = currentLocale();
            dto.setBuilding(address.getBuildingName());
            dto.setAddress(address.getAddress());
            dto.setAddressId(address.getId());
            dto.setApartment(address.getApartmentName());
            LocaleString addressStatusLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.AUTH_TYPE_SCOPE,
                    String.valueOf(r.getAuthType()), locale);
            if (addressStatusLocale != null) {
                dto.setAuthType(addressStatusLocale.getText());
            }
            LocaleString livingStatusLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.LIVING_STATUS_SCOPE,
                    String.valueOf(r.getLivingStatus()), locale);
            if (livingStatusLocale != null) {
                dto.setLivingStatus(livingStatusLocale.getText());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrganizationOwnerAttachmentDTO> listOrganizationOwnerAttachments(ListOrganizationOwnerAttachmentsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        List<OrganizationOwnerAttachment> attachmentList = propertyMgrProvider
                .listOrganizationOwnerAttachments(currentNamespaceId(), cmd.getOrgOwnerId());

        return attachmentList.stream().map(r -> {
            OrganizationOwnerAttachmentDTO dto = ConvertHelper.convert(r, OrganizationOwnerAttachmentDTO.class);
            dto.setContentUrl(parserUri(r.getContentUri(), EhOrganizationOwners.class.getSimpleName(), r.getOwnerId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public OrganizationOwnerCarDTO createOrganizationOwnerCar(CreateOrganizationOwnerCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer namespaceId = currentNamespaceId();
        checkOrganizationOwnerCarUnique(cmd.getCommunityId(), cmd.getPlateNumber(), namespaceId);

        OrganizationOwnerCar newCar = ConvertHelper.convert(cmd, OrganizationOwnerCar.class);
        newCar.setNamespaceId(namespaceId);
        newCar.setStatus(OrganizationOwnerCarStatus.NORMAL.getCode());
        newCar.setUpdateUid(UserContext.current().getUser().getId());
        newCar.setParkingType(cmd.getParkingType());

        long carId = propertyMgrProvider.createOrganizationOwnerCar(newCar);

        // 处理上传附件
        if (cmd.getCarAttachments() != null && !cmd.getCarAttachments().isEmpty()) {
            cmd.getCarAttachments().forEach(r -> {
                r.setCarId(carId);
                r.setOrganizationId(cmd.getOrganizationId());
                this.uploadOrganizationOwnerCarAttachment(r);
            });
        }
        ownerCarSearcher.feedDoc(newCar);
        return convertOwnerCarToOwnerCarDTO(newCar);
    }

    private void checkOrganizationOwnerCarUnique(Long communityId, String plateNumber, Integer namespaceId) {
        // 检查车辆的唯一性, 根据同一个小区内的车牌号码判断
        List<OrganizationOwnerCar> car = propertyMgrProvider.findOrganizationOwnerCarByCommunityIdAndPlateNumber(
                namespaceId, communityId, plateNumber);
        if (car != null && car.size() > 0) {
            LOGGER.error("The organization owner car is exist, plateNumber = {}", plateNumber);
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_EXIST,
                    "The organization owner car already exist, plateNumber = %s", plateNumber);
        }
    }

    @Override
    public void deleteOrganizationOwnerCar(DeleteOrganizationOwnerCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerCar car = propertyMgrProvider.findOrganizationOwnerCar(currentNamespaceId(), cmd.getId());
        if (car == null) {
            LOGGER.error("The organization owner car are not exist, id = {}", cmd.getId());
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_EXIST,
                    "The organization owner car are not exist, id = %s", cmd.getId());
        }
        if (!Objects.equals(car.getStatus(), OrganizationOwnerCarStatus.DELETE.getCode())) {
            car.setStatus(OrganizationOwnerCarStatus.DELETE.getCode());
            dbProvider.execute(status -> {
                Integer namespaceId = currentNamespaceId();
                propertyMgrProvider.updateOrganizationOwnerCar(car);
                propertyMgrProvider.deleteOrganizationOwnerOwnerCarByCarId(namespaceId, car.getId());
                propertyMgrProvider.deleteOrganizationOwnerCarAttachmentByCarId(namespaceId, car.getId());
                return null;
            });
        }
        ownerCarSearcher.deleteById(car.getId());
        createAuditLog(car.getId(), car.getClass());
    }

    @Override
    public void setOrganizationOwnerAsCarPrimary(SetOrganizationOwnerAsCarPrimaryCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer namespaceId = currentNamespaceId();
        OrganizationOwnerOwnerCar ownerOwnerCar = propertyMgrProvider.findOrganizationOwnerOwnerCarByOwnerIdAndCarId(
                namespaceId, cmd.getOrgOwnerId(), cmd.getCarId());
        if (ownerOwnerCar != null) {
            CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getOrgOwnerId());
            OrganizationOwnerCar ownerCar = propertyMgrProvider.findOrganizationOwnerCar(namespaceId, cmd.getCarId());
            OrganizationOwnerOwnerCar primaryUser = propertyMgrProvider.findOrganizationOwnerCarPrimaryUser(namespaceId, cmd.getCarId());
            dbProvider.execute(status -> {
                // 如果现已经有首要联系人,则设置当前首要联系人为非首要联系人
                if (primaryUser != null) {
                    primaryUser.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.NORMAL.getCode());
                    propertyMgrProvider.updateOrganizationOwnerOwnerCar(primaryUser);
                }
                ownerOwnerCar.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode());
                propertyMgrProvider.updateOrganizationOwnerOwnerCar(ownerOwnerCar);

                ownerCar.setContactNumber(pmOwner.getContactToken());
                propertyMgrProvider.updateOrganizationOwnerCar(ownerCar);
                return null;
            });
        }
    }

    @Override
    public void deleteRelationOfOrganizationOwnerAndCar(DeleteRelationOfOrganizationOwnerAndCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer namespaceId = currentNamespaceId();
        OrganizationOwnerOwnerCar ownerOwnerCar = propertyMgrProvider.findOrganizationOwnerOwnerCarByOwnerIdAndCarId(
                namespaceId, cmd.getOrgOwnerId(), cmd.getCarId());
        if (ownerOwnerCar != null) {
            dbProvider.execute(status -> {
                // 移除车辆使用者
                propertyMgrProvider.deleteOrganizationOwnerOwnerCarByOwnerIdAndCarId(namespaceId, cmd.getOrgOwnerId(), cmd.getCarId());
                if (Objects.equals(ownerOwnerCar.getPrimaryFlag(), OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode())) {
                    // 移除的使用者是首要联系人的情况,设置下一位使用者为首要联系人
                    OrganizationOwnerCar ownerCar = propertyMgrProvider.findOrganizationOwnerCar(namespaceId, cmd.getCarId());
                    List<OrganizationOwnerDTO> ownerOwnerCars = propertyMgrProvider.listOrganizationOwnersByCar(namespaceId, cmd.getCarId(), record -> {
                        OrganizationOwnerDTO dto = new OrganizationOwnerDTO();
                        dto.setContactToken(record.getValue("contact_token", String.class));
                        dto.setId(record.getValue("ownerOwnerCarId", Long.class));
                        return dto;
                    });

                    if (ownerOwnerCars != null && ownerOwnerCars.size() > 0) {
                        OrganizationOwnerOwnerCar newPrimaryUserRecord = propertyMgrProvider.findOrganizationOwnerOwnerCarById(namespaceId, ownerOwnerCars.get(0).getId());
                        newPrimaryUserRecord.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode());
                        propertyMgrProvider.updateOrganizationOwnerOwnerCar(newPrimaryUserRecord);

                        // 更新车辆的联系方式为新的首要联系人的联系方式
                        ownerCar.setContactNumber(ownerOwnerCars.get(0).getContactToken());
                        propertyMgrProvider.updateOrganizationOwnerCar(ownerCar);
                        // ownerCarSearcher.feedDoc(ownerCar);
                    }
                }
                return null;
            });
            createAuditLog(ownerOwnerCar.getId(), ownerOwnerCar.getClass());
        }
    }

    @Override
    public void importOrganizationOwnerCars(ImportOrganizationOwnerCarsCommand cmd, MultipartFile[] file) {
        Long communityId = cmd.getCommunityId();
        this.checkCommunityIdIsNull(communityId);
        this.checkCommunity(communityId);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        ArrayList resultList = processorExcel(file[0]);
        List<OrganizationOwnerCar> carList = dbProvider.execute(status -> processorOrganizationOwnerCar(cmd.getCommunityId(), resultList));
        ownerCarSearcher.bulkUpdate(carList);
    }

    @Override
    public void exportOrganizationOwnerCars(ExportOrganizationOwnerCarsCommand cmd, HttpServletResponse response) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Community are not exist.");
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Community are not exist.");
        }

        List<OrganizationOwnerCar> ownerCars = propertyMgrProvider.listOrganizationOwnerCarsByCommunity(
                currentNamespaceId(), cmd.getCommunityId());

        if (ownerCars != null && ownerCars.size() > 0) {
            List<OrganizationOwnerCarDTO> ownerCarDTOs = ownerCars.stream().map(this::convertOwnerCarToOwnerCarDTO).collect(Collectors.toList());

            String fileName = String.format("车辆信息_%s_%s", community.getName(), DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
            ExcelUtils excelUtils = new ExcelUtils(response, fileName, "车辆信息");
            String[] propertyNames = {"plateNumber", "brand", "color", "parkingType", "parkingSpace", "contacts", "contactNumber"};
            String[] titleNames = {"车牌号码", "品牌型号", "车身颜色", "停车类型", "车位", "车主姓名", "联系电话"};
            int[] titleSizes = {20, 10, 10, 10, 10, 10, 20};
            excelUtils.writeExcel(propertyNames, titleNames, titleSizes, ownerCarDTOs);
        } else {
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_NOT_EXIST,
                    "Organization owner car are not exist.");
        }
    }

    private OrganizationOwnerCarDTO convertOwnerCarToOwnerCarDTO(OrganizationOwnerCar ownerCar) {
        OrganizationOwnerCarDTO dto = ConvertHelper.convert(ownerCar, OrganizationOwnerCarDTO.class);
        ParkingCardCategory category = propertyMgrProvider.findParkingCardCategory(ownerCar.getParkingType());
        dto.setParkingType(category != null ? category.getCategoryName() : "");
        return dto;
    }

    @Override
    public void exportOrganizationOwners(ExportOrganizationsOwnersCommand cmd, HttpServletResponse response) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Community is not exist.");
            throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Community is not exist.");
        }

        List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwnersByCommunity(
                currentNamespaceId(), cmd.getCommunityId());

        if (ownerList != null && ownerList.size() > 0) {
            List<OrganizationOwnerDTO> ownerDTOs = ownerList.stream().map(this::convertOwnerToDTO).collect(Collectors.toList());

            String fileName = String.format("客户信息_%s_%s", community.getName(), DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
            ExcelUtils excelUtils = new ExcelUtils(response, fileName, "客户信息");
            String[] propertyNames = {"contactName", "gender", "orgOwnerType", "contactToken", "birthday", "maritalStatus", "job", "company",
                    "idCardNumber", "registeredResidence"};
            String[] titleNames = {"姓名", "性别", "客户类型", "手机", "生日", "婚姻状况", "职业", "工作单位", "证件号码", "户口所在地"};
            int[] titleSizes = {20, 10, 10, 30, 20, 10, 20, 30, 40, 30};
            excelUtils.writeExcel(propertyNames, titleNames, titleSizes, ownerDTOs);
        } else {
            // LOGGER.error("Organization owner are not exist.");
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                    "Organization owner are not exist.");
        }
    }

    private Integer currentNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    private OrganizationOwnerDTO convertOwnerToDTO(CommunityPmOwner owner) {
        OrganizationOwnerDTO dto = ConvertHelper.convert(owner, OrganizationOwnerDTO.class);
        dto.setAvatarUrl(parserUri(dto.getAvatar(), EhOrganizationOwners.class.getSimpleName(), owner.getId()));
        LocaleString genderLocale = localeStringProvider.find(UserLocalStringCode.SCOPE,
                String.valueOf(owner.getGender()), currentLocale());
        if (genderLocale != null) {
            dto.setGender(genderLocale.getText());
        }
        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(owner.getOrgOwnerTypeId());
        if (ownerType != null) {
            dto.setOrgOwnerType(ownerType.getDisplayName());
        }
        if (owner.getBirthday() != null) {
            dto.setBirthday(owner.getBirthday().getTime());
        }
        return dto;
    }

    @Override
    public List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByGender(ListOrganizationOwnerStatisticCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer[] totalNum = {0};
        RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper = (r) -> {
            ListOrganizationOwnerStatisticDTO dto = new ListOrganizationOwnerStatisticDTO();
            LocaleString genderLocale = localeStringProvider.find(UserLocalStringCode.SCOPE, String.valueOf(r.getValue("gender", String.class)),
                    currentLocale());
            dto.setFirst(genderLocale != null ? genderLocale.getText() : "");
            dto.setSecond(r.getValue("count", Integer.class));
            totalNum[0] += r.getValue("count", Integer.class);
            return dto;
        };
        List<ListOrganizationOwnerStatisticDTO> statisticDTOList = propertyMgrProvider.listOrganizationOwnerStatisticByGender(
                cmd.getCommunityId(), cmd.getLivingStatus(), cmd.getOrgOwnerTypeIds(), mapper);
        statisticDTOList.forEach(r -> r.setThird((int)((Double.valueOf(r.getSecond()) / totalNum[0] * 100)) + "%"));
        return statisticDTOList;
    }

    @Override
    public ListOrganizationOwnerStatisticByAgeDTO listOrganizationOwnerStatisticByAge(ListOrganizationOwnerStatisticCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Integer[] maleTotalNum = {0};
        Integer[] femaleTotalNum = {0};
        Integer[] totalNum = {0};

        List<ListOrganizationOwnerStatisticDTO> maleDtoList = new ArrayList<>();
        List<ListOrganizationOwnerStatisticDTO> femaleDtoList = new ArrayList<>();
        Map<String, ListOrganizationOwnerStatisticDTO> totalDtoMap = new HashMap<>();

        RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper = (r) -> {
            ListOrganizationOwnerStatisticDTO dto = new ListOrganizationOwnerStatisticDTO();
            Byte gender = r.getValue("gender", Byte.class);
            Integer count = r.getValue("count", Integer.class);
            if (Objects.equals(gender, UserGender.MALE.getCode())) {
                maleDtoList.add(dto);
                maleTotalNum[0] += count;
            } else if (Objects.equals(gender, UserGender.FEMALE.getCode())) {
                femaleDtoList.add(dto);
                femaleTotalNum[0] += count;
            }
            totalNum[0] += count;
            dto.setFirst(r.getValue("ageGroups", String.class));
            dto.setSecond(count);
            ListOrganizationOwnerStatisticDTO ageGroup = totalDtoMap.get(dto.getFirst());
            if (ageGroup != null) {
                ageGroup.setSecond(ageGroup.getSecond() + count);
            } else {
                totalDtoMap.put(dto.getFirst(), ConvertHelper.convert(dto, ListOrganizationOwnerStatisticDTO.class));
            }
            return dto;
        };

        propertyMgrProvider.listOrganizationOwnerStatisticByAge(cmd.getCommunityId(), cmd.getLivingStatus(), cmd.getOrgOwnerTypeIds(), mapper);

        maleDtoList.forEach(r -> r.setThird((int)((Double.valueOf(r.getSecond()) / maleTotalNum[0] * 100)) + ""));
        femaleDtoList.forEach(r -> r.setThird((int)((Double.valueOf(r.getSecond()) / femaleTotalNum[0] * 100)) + ""));
        totalDtoMap.values().forEach(r -> r.setThird((int)((Double.valueOf(r.getSecond()) / totalNum[0] * 100)) + ""));

        // 为了把101+的放在最后面
        ListOrganizationOwnerStatisticDTO otherAgeDto = totalDtoMap.remove("101+");

        List<ListOrganizationOwnerStatisticDTO> totalList = totalDtoMap.values().stream().collect(Collectors.toList());
        totalList.sort(Comparator.comparing(ListOrganizationOwnerStatisticDTO::getFirst));
        if (otherAgeDto != null) {
            totalList.add(otherAgeDto);
        }
        return new ListOrganizationOwnerStatisticByAgeDTO(maleDtoList, femaleDtoList, totalList);
    }

    @Override
    public OrganizationOwnerCarDTO updateOrganizationOwnerCar(UpdateOrganizationOwnerCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Tuple<OrganizationOwnerCar, Boolean> tuple =
                coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ORGANIZATION_OWNER_CAR.getCode()).enter(() -> {
            OrganizationOwnerCar car = propertyMgrProvider.findOrganizationOwnerCar(currentNamespaceId(), cmd.getId());
            if (car == null) {
                LOGGER.error("Organization owner car are not exist, id = {}", cmd.getId());
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_NOT_EXIST,
                        "Organization owner car are not exist, id = %s", cmd.getId());
            }
            if (cmd.getParkingType() != null) {
                car.setParkingType(cmd.getParkingType());
            }
            if (cmd.getBrand() != null) {
                car.setBrand(cmd.getBrand());
            }
            if (cmd.getColor() != null) {
                car.setColor(cmd.getColor());
            }
            boolean needUpdateDoc = false;
            if (cmd.getContacts() != null) {
                car.setContacts(cmd.getContacts());
                needUpdateDoc = true;
            }
            if (cmd.getContactNumber() != null) {
                car.setContactNumber(cmd.getContactNumber());
            }
            if (cmd.getParkingSpace() != null) {
                car.setParkingSpace(cmd.getParkingSpace());
            }
            if (cmd.getContentUri() != null) {
                car.setContentUri(cmd.getContentUri());
            }
            propertyMgrProvider.updateOrganizationOwnerCar(car);
            if (needUpdateDoc) {
                ownerCarSearcher.feedDoc(car);
            }
            return car;
        });
        return convertOwnerCarToOwnerCarDTO(tuple.first());
    }

    @Override
    public OrganizationOwnerAttachmentDTO uploadOrganizationOwnerAttachment(UploadOrganizationOwnerAttachmentCommand cmd) {
        validate(cmd);
    	checkCurrentUserNotInOrg(cmd.getOrganizationId());

		OrganizationOwnerAttachment attachment = new OrganizationOwnerAttachment();
		attachment.setNamespaceId(currentNamespaceId());
		attachment.setOwnerId(cmd.getOrgOwnerId());
		attachment.setContentUri(cmd.getContentUri());
		attachment.setAttachmentName(cmd.getAttachmentName());
		propertyMgrProvider.createOrganizationOwnerAttachment(attachment);

        OrganizationOwnerAttachmentDTO dto = ConvertHelper.convert(attachment, OrganizationOwnerAttachmentDTO.class);
        dto.setContentUrl(parserUri(attachment.getContentUri(), EhOrganizationOwners.class.getSimpleName(), attachment.getOwnerId()));
        return dto;
	}

    @Override
    public OrganizationOwnerAddressDTO addOrganizationOwnerAddress(AddOrganizationOwnerAddressCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Address address = addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            LOGGER.error("The address is not exist, addressId = {}.", cmd.getAddressId());
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "The address is not exist, addressId = %s.", cmd.getAddressId());
        }
        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                currentNamespaceId(), cmd.getOrgOwnerId(), cmd.getAddressId());
        if (ownerAddress != null) {
            LOGGER.error("The organization owner {} already in address {}.", cmd.getOrgOwnerId(), cmd.getAddressId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_ADDRESS_EXIST,
                    "The organization owner %s already in address %s.", cmd.getOrgOwnerId(), cmd.getAddressId());
        }
        ownerAddress = createOrganizationOwnerAddress(address.getId(), cmd.getLivingStatus(), currentNamespaceId(),
                cmd.getOrgOwnerId(), OrganizationOwnerAddressAuthType.INACTIVE);
        // 自动审核用户与客户
        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getOrgOwnerId());
        if (pmOwner != null) {
            autoApprovalOrganizationOwnerAddress(address.getCommunityId(), pmOwner.getContactToken(), ownerAddress);
        }
        return buildOrganizationOwnerAddressDTO(cmd, address, ownerAddress);
    }

    @Override
    public OrganizationOwnerAddress createOrganizationOwnerAddress(Long addressId, Byte livingStatus, Integer namespaceId,
                                                                   Long ownerId, OrganizationOwnerAddressAuthType authType) {
		OrganizationOwnerAddress ownerAddress = new OrganizationOwnerAddress();
		ownerAddress.setAddressId(addressId);
		ownerAddress.setLivingStatus(livingStatus);
		ownerAddress.setOrganizationOwnerId(ownerId);
		ownerAddress.setNamespaceId(namespaceId);
		ownerAddress.setAuthType(authType.getCode());
		propertyMgrProvider.createOrganizationOwnerAddress(ownerAddress);
        return ownerAddress;
    }

	@Override
    public void deleteOrganizationOwnerAddress(DeleteOrganizationOwnerAddressCommand cmd) {
        validate(cmd);
		checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Address address = addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            LOGGER.error("The address is not exist, addressId = {}.", cmd.getAddressId());
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "The address is not exist, addressId = %s.", cmd.getAddressId());
        }
        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                currentNamespaceId(), cmd.getOrgOwnerId(), address.getId());
        if (ownerAddress == null) {
            LOGGER.error("The ownerAddress is not exist.");
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_ADDRESS,
                    "The ownerAddress is not exist.");
        }
        propertyMgrProvider.deleteOrganizationOwnerAddress(ownerAddress);
		// 创建删除行为记录
		createOrganizationOwnerBehavior(ownerAddress.getOrganizationOwnerId(), ownerAddress.getAddressId(),
				System.currentTimeMillis(), OrganizationOwnerBehaviorType.DELETE);

        // 如果当前用户在该地址下认证过,则移除认证状态
        Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
        if (family != null) {
            leaveFamilyByOwnerId(cmd.getOrgOwnerId(), family.getId());
        }

        createAuditLog(ownerAddress.getId(), ownerAddress.getClass());
    }

    private void createAuditLog(Long resourceId, Class<?> resourceType){
        AuditLog log = new AuditLog();
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setOperationType("DELETE");
        log.setOperatorUid(UserContext.current().getUser().getId());
        log.setResourceId(resourceId);
        log.setResourceType(resourceType.getSimpleName());
        auditLogProvider.createAuditLog(log);
    }

    @Override
    public List<OrganizationOwnerTypeDTO> listOrganizationOwnerTypes(ListOrganizationOwnerTypesCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<OrganizationOwnerType> typeList = propertyMgrProvider.listOrganizationOwnerType();
        return typeList.stream().map(type -> ConvertHelper.convert(type, OrganizationOwnerTypeDTO.class)).collect(Collectors.toList());
    }

    @Override
    public OrganizationOwnerCarDTO getOrganizationOwnerCar(GetOrganizationOwnerCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerCar car = propertyMgrProvider.findOrganizationOwnerCar(currentNamespaceId(), cmd.getId());
        if (car == null) {
            LOGGER.error("The organization owner car are not exist, id = {}.", cmd.getId());
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_EXIST,
                    "The organization owner car are not exist, id = %s.", cmd.getId());
        }
        return convertOwnerCarToOwnerCarDTO(car);
    }

    @Override
    public OrganizationOwnerCarAttachmentDTO uploadOrganizationOwnerCarAttachment(UploadOrganizationOwnerCarAttachmentCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerCarAttachment attachment = new OrganizationOwnerCarAttachment();
        attachment.setNamespaceId(currentNamespaceId());
        attachment.setOwnerId(cmd.getCarId());
        attachment.setContentUri(cmd.getContentUri());
        attachment.setAttachmentName(cmd.getAttachmentName());
        propertyMgrProvider.createOrganizationOwnerCarAttachment(attachment);

        OrganizationOwnerCarAttachmentDTO dto = ConvertHelper.convert(attachment, OrganizationOwnerCarAttachmentDTO.class);
        dto.setContentUrl(parserUri(attachment.getContentUri(), EhOrganizationOwners.class.getSimpleName(), attachment.getOwnerId()));
        return dto;
    }

    @Override
    public OrganizationOwnerDTO addOrganizationOwnerCarUser(AddOrganizationOwnerCarUserCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getOrgOwnerId());
        if (pmOwner == null) {
            LOGGER.error("OrganizationOwner are not exist, ownerId = {}.", cmd.getOrgOwnerId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                    "OrganizationOwner are not exist, ownerId = %s.", cmd.getOrgOwnerId());
        }
        Integer namespaceId = currentNamespaceId();
        OrganizationOwnerOwnerCar ownerOwnerCar = propertyMgrProvider.findOrganizationOwnerOwnerCarByOwnerIdAndCarId(
                namespaceId, cmd.getOrgOwnerId(), cmd.getCarId());
        if (ownerOwnerCar != null) {
            LOGGER.error("The organization owner {} already in car {} user list.", cmd.getOrgOwnerId(), cmd.getCarId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_USER_EXIST,
                    "The organization owner %s already in car %s user list.", cmd.getOrgOwnerId(), cmd.getCarId());
        }
        OrganizationOwnerOwnerCar newOwnerOwnerCar = new OrganizationOwnerOwnerCar();
        newOwnerOwnerCar.setCarId(cmd.getCarId());
        newOwnerOwnerCar.setOrganizationOwnerId(cmd.getOrgOwnerId());
        newOwnerOwnerCar.setNamespaceId(namespaceId);
        newOwnerOwnerCar.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.NORMAL.getCode());

        long id = propertyMgrProvider.createOrganizationOwnerOwnerCar(newOwnerOwnerCar);

        OrganizationOwnerDTO dto = new OrganizationOwnerDTO();
        LocaleString primaryFlagLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.PRIMARY_FLAG_SCOPE,
                String.valueOf(newOwnerOwnerCar.getPrimaryFlag()), currentLocale());
        dto.setPrimaryFlag(primaryFlagLocale == null ? "" : primaryFlagLocale.getText());
        dto.setContactName(pmOwner.getContactName());
        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(pmOwner.getOrgOwnerTypeId());
        dto.setOrgOwnerType(ownerType == null ? "" : ownerType.getDisplayName());
        dto.setContactToken(pmOwner.getContactToken());
        dto.setId(id);
        return dto;
    }

    @Override
    public List<OrganizationOwnerCarDTO> listOrganizationOwnerCarsByAddress(ListOrganizationOwnerCarsByAddressCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Address address = addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            LOGGER.error("The address {} is not exist.", cmd.getAddressId());
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "The address %s is not exist.", cmd.getAddressId());
        }
        Integer namespaceId = currentNamespaceId();
        List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByAddressId(
                namespaceId, address.getId());

        return ownerAddressList
                .stream().flatMap(r -> propertyMgrProvider.listOrganizationOwnerCarByOwnerId(namespaceId, r.getOrganizationOwnerId())
                        .stream().map(this::convertOwnerCarToOwnerCarDTO)).distinct().collect(Collectors.toList());
    }

    @Override
    public void syncOwnerIndex() {
        pmOwnerSearcher.syncFromDb();
    }

    @Override
    public void syncOwnerCarIndex() {
        ownerCarSearcher.syncFromDb();
    }

    @Override
    public void updateOrganizationOwnerAddressAuthType(Long ownerId, Long communityId, Long addressId, OrganizationOwnerAddressAuthType authType) {
        if (Stream.of(addressId, authType).allMatch(Objects::nonNull)) {
            OrganizationOwnerAddress ownerAddress = null;
            if (ownerId == null) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(UserContext.current().getUser().getId(), IdentifierType.MOBILE.getCode());
                if(userIdentifier != null) {
                    CommunityPmOwner pmOwner = propertyMgrProvider.findOrganizationOwnerByCommunityIdAndContactToken(currentNamespaceId(),
                            communityId, userIdentifier.getIdentifierToken());
                    if (pmOwner != null) {
                        ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                                currentNamespaceId(), pmOwner.getId(), addressId);
                    }
                }
            } else {
                ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(currentNamespaceId(), ownerId, addressId);
            }
            if (ownerAddress != null && ownerAddress.getAuthType() != authType.getCode()) {
                ownerAddress.setAuthType(authType.getCode());
                propertyMgrProvider.updateOrganizationOwnerAddress(ownerAddress);
            }
        }
    }

    @Override
    public List<ParkingCardCategoryDTO> listParkingCardCategories(ListParkingCardCategoriesCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EhParkingCardCategories> categories = propertyMgrProvider.listParkingCardCategories();
        return categories.stream().map(r -> ConvertHelper.convert(r, ParkingCardCategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteOrganizationOwnerAddressAuthStatus(UpdateOrganizationOwnerAddressAuthTypeCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(currentNamespaceId(), cmd.getOwnerId(), cmd.getAddressId());
        if (ownerAddress == null || ownerAddress.getAuthType() == OrganizationOwnerAddressAuthType.INACTIVE.getCode()) {
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_ADDRESS_ALREADY_INACTIVE,
                    "The organization owner address is already inactive.");
        }
        Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
        if(family == null){
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_ADDRESS_ALREADY_INACTIVE,
                    "The organization owner address is already inactive.");
        }
        dbProvider.execute(s -> {
            leaveFamilyByOwnerId(cmd.getOrgOwnerId(), family.getId());
            ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.INACTIVE.getCode());
            propertyMgrProvider.updateOrganizationOwnerAddress(ownerAddress);
            return true;
        });
    }

    private void leaveFamilyByOwnerId(Long ownerId, Long familyId) {
        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(ownerId);
        if (pmOwner != null) {
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(currentNamespaceId(), pmOwner.getContactToken());
            if(userIdentifier != null) {
                User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                if (user != null) {
                    LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
                    leaveCmd.setId(familyId);
                    familyService.leave(leaveCmd, user);
                }
            }
        }
    }

    private OrganizationOwnerAddressDTO buildOrganizationOwnerAddressDTO(AddOrganizationOwnerAddressCommand cmd, Address address, OrganizationOwnerAddress ownerAddress) {
        OrganizationOwnerAddressDTO dto = new OrganizationOwnerAddressDTO();
        String locale = currentLocale();
        LocaleString livingStatus = localeStringProvider.find(OrganizationOwnerLocaleStringScope.LIVING_STATUS_SCOPE,
                String.valueOf(cmd.getLivingStatus()), locale);
        if (livingStatus != null) {
            dto.setLivingStatus(livingStatus.getText());
        }
        LocaleString addressStatusLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.AUTH_TYPE_SCOPE,
                String.valueOf(ownerAddress.getAuthType()), locale);
        if (addressStatusLocale != null) {
            dto.setAuthType(addressStatusLocale.getText());
        }
        dto.setAddressId(address.getId());
        dto.setApartment(address.getApartmentName());
        dto.setBuilding(address.getBuildingName());
        dto.setAddress(address.getAddress());
        return dto;
    }

    @Override
    public List<OrganizationOwnerCarAttachmentDTO> listOrganizationOwnerCarAttachments(ListOrganizationOwnerCarAttachmentCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        List<OrganizationOwnerCarAttachment> carAttachmentList = propertyMgrProvider.listOrganizationOwnerCarAttachment(
                currentNamespaceId(), cmd.getCarId());
        if (carAttachmentList != null) {
            return carAttachmentList.stream().map(attachment -> {
                OrganizationOwnerCarAttachmentDTO dto = ConvertHelper.convert(attachment, OrganizationOwnerCarAttachmentDTO.class);
                dto.setContentUrl(parserUri(attachment.getContentUri(), EhOrganizationOwnerCars.class.getSimpleName(), cmd.getCarId()));
                return dto;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<OrganizationOwnerDTO> listOrganizationOwnersByAddress(ListOrganizationOwnersByAddressCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Address address = addressProvider.findAddressById(cmd.getAddressId());
		if (address == null) {
            LOGGER.error("The address {} is not exist.", cmd.getAddressId());
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "The address %s not exist.", cmd.getAddressId());
		}
		User user = UserContext.current().getUser();
		RecordMapper<Record, OrganizationOwnerDTO> mapper = (r) -> {
			OrganizationOwnerDTO dto = new OrganizationOwnerDTO();
			dto.setContactName(r.getValue("contact_name", String.class));
			dto.setId(r.getValue("ownerId", Long.class));// organization owner id

			LocaleString livingStatusLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.LIVING_STATUS_SCOPE,
					r.getValue("living_status", String.class), user.getLocale());
			dto.setLivingStatus(livingStatusLocale != null ? livingStatusLocale.getText() : null);

			LocaleString authTypeStatusLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.AUTH_TYPE_SCOPE,
					r.getValue("auth_type", String.class), user.getLocale());
			dto.setAuthType(authTypeStatusLocale != null ? authTypeStatusLocale.getText() : null);

			OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(r.getValue("org_owner_type_id", Long.class));
			dto.setOrgOwnerType(ownerType != null ? ownerType.getDisplayName() : null);
			return dto;
		};
		return propertyMgrProvider.listOrganizationOwnersByAddressId(user.getNamespaceId(), address.getId(), mapper);
	}

    @Override
    public OrganizationOwnerDTO getOrganizationOwner(GetOrganizationOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getId());
        if (pmOwner != null) {
            return convertOwnerToDTO(pmOwner);
        } else {
            LOGGER.error("The organization owner {} is not exist.", cmd.getId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                    "The organization owner %s is not exist.", cmd.getId());
        }
    }

    private String parserUri(String uri,String ownerType, long ownerId){
        try {
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri,ownerType,ownerId);
        } catch (Exception e) {
            LOGGER.error("Parser uri error.", e);
        }
        return null;
    }

    @Override
    public List<OrganizationOwnerDTO> listOrganizationOwnersByCar(ListOrganizationOwnersByCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        RecordMapper<Record, OrganizationOwnerDTO> mapper = (r) -> {
            OrganizationOwnerDTO dto = new OrganizationOwnerDTO();
            dto.setContactName(r.getValue("contact_name", String.class));
            dto.setContactToken(r.getValue("contact_token", String.class));
            Long ownerTypeId = r.getValue("org_owner_type_id", Long.class);
            dto.setOrgOwnerType(getOrganizationOwnerTypeDisplayName(ownerTypeId));
            dto.setId(r.getValue("ownerId", Long.class));// organization owner id
            LocaleString primaryFlagLocale = localeStringProvider.find(OrganizationOwnerLocaleStringScope.PRIMARY_FLAG_SCOPE,
                    r.getValue("primary_flag", String.class), currentLocale());
            dto.setPrimaryFlag(primaryFlagLocale != null ? primaryFlagLocale.getText() : "");
            return dto;
        };
        List<OrganizationOwnerDTO> dtoList = propertyMgrProvider.listOrganizationOwnersByCar(currentNamespaceId(), cmd.getCarId(), mapper);
        if (dtoList != null && dtoList.size() > 0) {
            dtoList.sort((o1, o2) -> o2.getPrimaryFlag().compareTo(o1.getPrimaryFlag()));
        }
        return dtoList;
    }

    private String currentLocale() {
        return UserContext.current().getUser().getLocale();
    }

    private String getOrganizationOwnerTypeDisplayName(Long ownerTypeId) {
        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(ownerTypeId);
        return ownerType != null ? ownerType.getDisplayName() : "";
    }

    @Override
    public List<OrganizationOwnerCarDTO> listOrganizationOwnerCarsByOrgOwner(ListOrganizationOwnerCarByOrgOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        List<OrganizationOwnerCar> ownerCars = propertyMgrProvider.listOrganizationOwnerCarByOwnerId(currentNamespaceId(), cmd.getOrgOwnerId());
        if (ownerCars != null) {
            return ownerCars.stream().map(this::convertOwnerCarToOwnerCarDTO).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
	public ListOrganizationOwnersResponse searchOrganizationOwners(SearchOrganizationOwnersCommand cmd){
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return pmOwnerSearcher.query(cmd);
    }
    
    @Override
	public List<OrganizationOwnerDTO> searchOrganizationOwnersBycondition(SearchOrganizationOwnersByconditionCommand cmd){
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        
        int namespaceId = UserContext.current().getUser().getNamespaceId();
        
        List<OrganizationOwnerDTO> result = new ArrayList<OrganizationOwnerDTO>();
        if (!StringUtils.isEmpty(cmd.getBuildingName()) && !StringUtils.isEmpty(cmd.getApartmentName())) {
        	ListPropApartmentsByKeywordCommand listPropApartmentsByKeywordCommand = new ListPropApartmentsByKeywordCommand();
            listPropApartmentsByKeywordCommand.setCommunityId(cmd.getCommunityId());
            listPropApartmentsByKeywordCommand.setOrganizationId(cmd.getOrganizationId());
            listPropApartmentsByKeywordCommand.setNamespaceId(namespaceId);
            listPropApartmentsByKeywordCommand.setBuildingName(cmd.getBuildingName());
            listPropApartmentsByKeywordCommand.setKeyword(cmd.getApartmentName());
            Tuple<Integer, List<ApartmentDTO>> apts = addressService.listApartmentsByKeyword(listPropApartmentsByKeywordCommand);
            List<ApartmentDTO> apartments = apts.second();
            if(apartments.size() == 0)
            	return result;
            List<OrganizationOwnerAddress> list = propertyMgrProvider.listOrganizationOwnerAddressByAddressId(namespaceId, apartments.get(0).getAddressId());
              result = list.stream().map(r -> {
            	  CommunityPmOwner organizationOwner = propertyMgrProvider.findPropOwnerById(r.getId());
                OrganizationOwnerDTO dto = ConvertHelper.convert(organizationOwner, OrganizationOwnerDTO.class);
                OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(organizationOwner.getOrgOwnerTypeId());
                dto.setOrgOwnerType(ownerType == null ? "" : ownerType.getDisplayName());
                LocaleString genderLocale = localeStringProvider.find(UserLocalStringCode.SCOPE, String.valueOf(organizationOwner.getGender()),
                        UserContext.current().getUser().getLocale());
                dto.setGender(genderLocale != null ? genderLocale.getText() : "");
                dto.setBirthday(null);
                
                List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(organizationOwner.getNamespaceId(), organizationOwner.getId());
                dto.setAddresses(addresses.stream().map(r2 -> {
                	OrganizationOwnerAddressDTO d = ConvertHelper.convert(r2, OrganizationOwnerAddressDTO.class);
                	Address address = addressProvider.findAddressById(r2.getAddressId());
                	d.setAddress(address.getAddress());
                	d.setApartment(address.getApartmentName());
                	d.setBuilding(address.getBuildingName());
                	return d;
                })
                		.collect(Collectors.toList()));
                return dto;
            }).collect(Collectors.toList());
        }
        if(!StringUtils.isEmpty(cmd.getContact())) {
            List<CommunityPmOwner> list = propertyMgrProvider.listOrganizationOwners(namespaceId, cmd.getCommunityId(), null, cmd.getContact(), cmd.getPageAnchor(), cmd.getPageSize());
            result = list.stream().map(r -> {
                OrganizationOwnerDTO dto = ConvertHelper.convert(r, OrganizationOwnerDTO.class);
                OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(r.getOrgOwnerTypeId());
                dto.setOrgOwnerType(ownerType == null ? "" : ownerType.getDisplayName());
                LocaleString genderLocale = localeStringProvider.find(UserLocalStringCode.SCOPE, String.valueOf(r.getGender()),
                        UserContext.current().getUser().getLocale());
                dto.setGender(genderLocale != null ? genderLocale.getText() : "");
                dto.setBirthday(null);
                
                List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(r.getNamespaceId(), r.getId());
                dto.setAddresses(addresses.stream().map(r2 -> {
                	OrganizationOwnerAddressDTO d = ConvertHelper.convert(r2, OrganizationOwnerAddressDTO.class);
                	Address address = addressProvider.findAddressById(r2.getAddressId());
                	d.setAddress(address.getAddress());
                	d.setApartment(address.getApartmentName());
                	d.setBuilding(address.getBuildingName());
                	return d;
                })
                		.collect(Collectors.toList()));
                return dto;
            }).collect(Collectors.toList());
        }
        
        return result;
    }

    /*@Override
    public List<FamilyMemberDTO> listFamilyMembersByFamilyId(ListPropFamilyMemberCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkFamilyIdIsNull(cmd.getFamilyId());
        this.checkCommunity(cmd.getCommunityId());

        List<GroupMember> entityResultList = groupProvider.findGroupMemberByGroupId(cmd.getFamilyId());
        List<FamilyMemberDTO> results = new ArrayList<FamilyMemberDTO>();

        for (GroupMember member : entityResultList) {
            FamilyMemberDTO dto = new FamilyMemberDTO();
            dto.setId(member.getId());
            dto.setFamilyId(member.getGroupId());
            dto.setMemberUid(member.getMemberId());
            dto.setMemberName(member.getMemberNickName());
            dto.setMemberAvatarUri((member.getMemberAvatar()));
            dto.setBehaviorTime(member.getBehaviorTime());
            results.add(dto);
        }
        return results;
    }*/

    @Override
    public void importOrganizationOwners(@Valid ImportOrganizationsOwnersCommand cmd, MultipartFile[] file) {
        User user = UserContext.current().getUser();
        Long communityId = cmd.getCommunityId();
        this.checkCommunityIdIsNull(communityId);
        this.checkCommunity(communityId);
		checkCurrentUserNotInOrg(cmd.getOrganizationId());

        ArrayList resultList = processorExcel(file[0]);
        List<CommunityPmOwner> ownerList = dbProvider.execute(status -> processorOrganizationOwner(user.getId(),
                cmd.getOrganizationId(), cmd.getCommunityId(), resultList));
        pmOwnerSearcher.bulkUpdate(ownerList);
    }

	private ArrayList processorExcel(MultipartFile file) {
		try {
            return PropMrgOwnerHandler.processorExcel(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
			LOGGER.error("Process excel error.", e);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Process excel error.");
        }
	}

	private List<CommunityPmOwner> processorOrganizationOwner(long userId, long organizationId, Long communityId, ArrayList resultList) {
		if(resultList != null && resultList.size() > 0) {
            List<CommunityPmOwner> ownerList = new ArrayList<>();
            List<String> contactTokenList = new ArrayList<>();
            int row = resultList.size();
            for (int rowIndex = 2; rowIndex < row ; rowIndex++) {
                RowResult result = (RowResult)resultList.get(rowIndex);
                if (Stream.of(result.getA(), result.getB(), result.getC(), result.getD(), result.getE(), result.getF()).anyMatch(StringUtils::isEmpty)) {
                    continue;
                }
                // 检查手机号的唯一性
                if (contactTokenList.contains(RowResult.trimString(result.getC()))) {
                	// 支持同一个人导入多行，但只能生成一个业主信息，add by tt, 170427
                	String contactToken = RowResult.trimString(result.getC());
                	List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwnersByToken(currentNamespaceId(), communityId, contactToken);
                	if (pmOwners != null && !pmOwners.isEmpty()) {
                		CommunityPmOwner pmOwner = pmOwners.get(0);
                		Address address = parseAddress(currentNamespaceId(), communityId, RowResult.trimString(result.getD()), RowResult.trimString(result.getE()));
                		Byte livingStatus = parseLivingStatus(RowResult.trimString(result.getF()));
        				createOrganizationOwnerAddress(address.getId(), livingStatus, currentNamespaceId(), pmOwner.getId(), OrganizationOwnerAddressAuthType.INACTIVE);
        				if (StringUtils.hasLength(result.getG())) {
        					long time = parseDate(RowResult.trimString(result.getG())).getTime();
        					createOrganizationOwnerBehavior(pmOwner.getId(), address.getId(), time, OrganizationOwnerBehaviorType.IMMIGRATION);
        				}
					}
                	continue;
                	// comment by tt, 170427
//                    LOGGER.error("Import organization owner contactToken repeat, contactToken = {}.", result.getC().trim());
//                    throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CONTACT_TOKEN_REPEAT,
//                            "Import organization owner contactToken repeat, contactToken = %s.", result.getC().trim());
                }
                contactTokenList.add(RowResult.trimString(result.getC()));
                checkContactTokenUnique(communityId, RowResult.trimString(result.getC()));

                CommunityPmOwner owner = new CommunityPmOwner();
                owner.setContactName(RowResult.trimString(result.getA()));
                owner.setContactToken(RowResult.trimString(result.getC()));
                Address address = parseAddress(currentNamespaceId(), communityId, RowResult.trimString(result.getD()), RowResult.trimString(result.getE()));
                owner.setGender(parseGender(RowResult.trimString(result.getH())));
                owner.setBirthday(parseDate(RowResult.trimString(result.getI())));
                owner.setOrgOwnerTypeId(parseOrgOwnerTypeId(RowResult.trimString(result.getB())));
                owner.setMaritalStatus(RowResult.trimString(result.getJ()));
                owner.setJob(RowResult.trimString(result.getK()));
                owner.setCompany(RowResult.trimString(result.getL()));
                owner.setIdCardNumber(RowResult.trimString(result.getM()));
                owner.setRegisteredResidence(RowResult.trimString(result.getN()));
                owner.setNamespaceId(currentNamespaceId());
                owner.setCreatorUid(userId);
                owner.setOrganizationId(organizationId);
                owner.setCommunityId(communityId);
                owner.setStatus(OrganizationOwnerStatus.NORMAL.getCode());

				long ownerId = propertyMgrProvider.createPropOwner(owner);

				Byte livingStatus = parseLivingStatus(RowResult.trimString(result.getF()));
				createOrganizationOwnerAddress(address.getId(), livingStatus, currentNamespaceId(), ownerId, OrganizationOwnerAddressAuthType.INACTIVE);

				if (StringUtils.hasLength(result.getG())) {
					long time = parseDate(RowResult.trimString(result.getG())).getTime();
					createOrganizationOwnerBehavior(ownerId, address.getId(), time, OrganizationOwnerBehaviorType.IMMIGRATION);
				}
				ownerList.add(owner);
			}
            if (ownerList.isEmpty()) {
                LOGGER.error("Import organization owner error.");
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT_NO_DATA,
                        "Import organization owner error.");
            }
            return ownerList;
        } else {
			LOGGER.error("excel data format is not correct.rowCount=" + resultList);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"excel data format is not correct");
		}
	}

	private List<OrganizationOwnerCar> processorOrganizationOwnerCar(Long communityId, ArrayList resultList) {
        if(resultList != null && resultList.size() > 0) {
            List<String> plateNumberList = new ArrayList<>();
            List<OrganizationOwnerCar> carList = new ArrayList<>();
            int row = resultList.size();
			for (int rowIndex = 2; rowIndex < row ; rowIndex++) {
				RowResult result = (RowResult)resultList.get(rowIndex);
                if (result.getA() == null || result.getA().trim().isEmpty()) {
                    continue;
                }
                OrganizationOwnerCar car = new OrganizationOwnerCar();
                User user = UserContext.current().getUser();

                // 检查车辆唯一性, 不唯一直接抛出异常
                if (plateNumberList.contains(result.getA().trim())) {
                    LOGGER.error("Import organization owner car plateNumber repeat, plateNumber = {}.", result.getA().trim());
                    throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_CAR_PLATE_NUMBER_REPEAT,
                            "Import organization owner car plateNumber repeat, plateNumber = %s.", result.getA().trim());
                }
                plateNumberList.add(result.getA().trim());
                checkOrganizationOwnerCarUnique(communityId, result.getA().trim(), user.getNamespaceId());

                car.setPlateNumber(result.getA().trim());
                car.setNamespaceId(user.getNamespaceId());
                car.setBrand(RowResult.trimString(result.getB()));
                car.setColor(RowResult.trimString(result.getC()));
                LocaleString parkingTypeLocale = localeStringProvider.findByText(OrganizationOwnerLocaleStringScope.PARKING_TYPE_SCOPE,
                        RowResult.trimString(result.getD()), user.getLocale());
                car.setParkingType(parkingTypeLocale != null ? Byte.valueOf(parkingTypeLocale.getCode()) : null);
                car.setParkingSpace(RowResult.trimString(result.getE()));
                car.setContacts(RowResult.trimString(result.getF()));
                car.setContactNumber(RowResult.trimString(result.getG()));

                car.setCommunityId(communityId);
                car.setStatus(OrganizationOwnerCarStatus.NORMAL.getCode());

                propertyMgrProvider.createOrganizationOwnerCar(car);

                carList.add(car);
			}
            if (carList.isEmpty()) {
                LOGGER.error("Import organization owner car error.");
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT_NO_DATA,
                        "Import organization owner car error.");
            }
            return carList;
        } else {
			LOGGER.error("excel data format is not correct.rowCount=" + resultList);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"excel data format is not correct");
		}
	}

    // 每个联系电话在一个小区内是唯一的
	private void checkContactTokenUnique(Long communityId, String contactToken) {
		List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwnersByToken(currentNamespaceId(),
                communityId, contactToken);
		if (pmOwners != null && pmOwners.size() > 0) {
            LOGGER.error("OrganizationOwner are already exist, contactToken = {}.", contactToken);
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_EXIST,
                    "OrganizationOwner are already exist, contactToken = %s.", contactToken);
        }
	}

	private java.sql.Date parseDate(String date) {
        if (date != null) {
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                TemporalAccessor accessor = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date);
                LocalDate ld = LocalDate.from(accessor);
                return java.sql.Date.valueOf(ld);
            } else if (date.matches("\\d{4}/\\d{2}/\\d{2}")) {
                TemporalAccessor accessor = DateTimeFormatter.ofPattern("yyyy/MM/dd").parse(date);
                LocalDate ld = LocalDate.from(accessor);
                return java.sql.Date.valueOf(ld);
            }
        }
        return null;
    }

	private Byte parseGender(String gender) {
		LocaleString localeString = localeStringProvider.findByText(UserLocalStringCode.SCOPE, gender, currentLocale());
		if (localeString != null) {
            return Byte.valueOf(localeString.getCode());
        }
        return UserGender.UNDISCLOSURED.getCode();
    }

	private Byte parseLivingStatus(String livingStatus) {
		LocaleString localeString = localeStringProvider.findByText(
				OrganizationOwnerLocaleStringScope.LIVING_STATUS_SCOPE, livingStatus, currentLocale());
		if (localeString == null) {
			LOGGER.error("The livingStatus {} is invalid.", livingStatus);
			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT,
					"The livingStatus %s is invalid.", livingStatus);
		}
		return Byte.valueOf(localeString.getCode());
	}

	private Address parseAddress(Integer namespaceId, Long communityId, String building, String apartment) {
        Address address = addressProvider.findApartmentAddress(namespaceId, communityId, StringUtils.trimAllWhitespace(building),
                StringUtils.trimAllWhitespace(apartment));
        if (address == null) {
            String addressText = StringUtils.trimAllWhitespace(building) + "-" + StringUtils.trimAllWhitespace(apartment);
            LOGGER.error("The address {} is not exist.", addressText);
			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT_ADDRESS_ERROR,
					"The address %s is not exist.", addressText);
		}
		return address;
	}

	private Long parseOrgOwnerTypeId(String orgOwnerTypeName) {
		OrganizationOwnerType type = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(orgOwnerTypeName);
		if (type == null) {
			LOGGER.error("The organization owner type {} is not exist.", orgOwnerTypeName);
			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT,
					"The organization owner type %s is not exist.", orgOwnerTypeName);
		}
		return type.getId();
	}

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o){
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ "+r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }
    }

	@Override
	public GetRequestInfoResponse getRequestInfo(GetRequestInfoCommand cmd) {
		if (StringUtils.isEmpty(cmd.getResourceType()) || cmd.getResourceId() == null || cmd.getRequestorUid() == null) {
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter");
		}
		EntityType resourceType = EntityType.fromCode(cmd.getResourceType());
		Long requestId = cmd.getRequestId();  //表示那条记录的id
		if (resourceType == EntityType.ORGANIZATIONS) {
		    // requestId 已经无法使用了，组织架构那边改了好多
			OrganizationMember organizationMember = organizationProvider.
                    findOrganizationMemberByOrgIdAndUId(cmd.getRequestorUid(), cmd.getResourceId());
			if (LOGGER.isDebugEnabled())
			    LOGGER.debug("getRequestInfo organizationMember {}", organizationMember);
			if (organizationMember != null) {
				return new GetRequestInfoResponse(organizationMember.getStatus());
			}
		}else if (resourceType == EntityType.GROUP || resourceType == EntityType.FAMILY) {
			// groupMember拒绝的时候是直接删除的，蛋疼
			GroupMember groupMember = groupProvider.findGroupMemberById(requestId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getRequestInfo groupMember {}", groupMember);
			if (groupMember != null) {
				return new GetRequestInfoResponse(groupMember.getMemberStatus());
			}
			// 新加了一个groupMemberLog表用来存储删除还是拒绝
			GroupMemberLog groupMemberLog = groupMemberLogProvider.findGroupMemberLogByGroupMemberId(requestId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getRequestInfo groupMemberLog {}", groupMemberLog);
			if (groupMemberLog != null) {
				return new GetRequestInfoResponse(groupMemberLog.getMemberStatus());
			}
		}
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getRequestInfo new GetRequestInfoResponse(GroupMemberStatus.INACTIVE.getCode())");
		return new GetRequestInfoResponse(GroupMemberStatus.INACTIVE.getCode());
	}
    
}
