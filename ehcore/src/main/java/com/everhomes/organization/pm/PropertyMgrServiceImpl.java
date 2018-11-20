// @formatter:off
package com.everhomes.organization.pm;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Null;

import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.everhomes.acl.Role;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.activity.ActivityService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressArrangement;
import com.everhomes.address.AddressProperties;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.address.AuthorizePriceExportHandler;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.asset.chargingitem.AssetChargingItemService;
import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contract.ContractEvents;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.customer.CustomerEntryInfo;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.enterprise.EnterpriseCommunityMap;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyBillingAccount;
import com.everhomes.family.FamilyBillingTransactions;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberLog;
import com.everhomes.group.GroupMemberLogProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.openapi.ContractTemplate;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.BillingAccountHelper;
import com.everhomes.organization.BillingAccountType;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationBillingAccount;
import com.everhomes.organization.OrganizationBillingTransactions;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationOrder;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationTask;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.pushmessage.PushMessage;
import com.everhomes.pushmessage.PushMessageProvider;
import com.everhomes.pushmessage.PushMessageResult;
import com.everhomes.pushmessage.PushMessageResultProvider;
import com.everhomes.pushmessage.PushMessageStatus;
import com.everhomes.pushmessage.PushMessageTargetType;
import com.everhomes.pushmessage.PushMessageType;
import com.everhomes.pushmessagelog.PushMessageLogService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.activity.ListSignupInfoByOrganizationIdResponse;
import com.everhomes.rest.activity.ListSignupInfoResponse;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressArrangementType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.AddressLivingStatus;
import com.everhomes.rest.address.AddressServiceErrorCode;
import com.everhomes.rest.address.ApartmentAbstractDTO;
import com.everhomes.rest.address.ApartmentBriefInfoDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.ApartmentEventDTO;
import com.everhomes.rest.address.AuthorizePriceCommand;
import com.everhomes.rest.address.AuthorizePriceDTO;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.CreateApartmentCommand;
import com.everhomes.rest.address.DeleteApartmentCommand;
import com.everhomes.rest.address.ExportApartmentsAuthorizePriceDTO;
import com.everhomes.rest.address.GetApartmentDetailCommand;
import com.everhomes.rest.address.GetApartmentDetailResponse;
import com.everhomes.rest.address.ImportAuthorizePriceDataDTO;
import com.everhomes.rest.address.ListAddressByKeywordCommand;
import com.everhomes.rest.address.ListApartmentEventsCommand;
import com.everhomes.rest.address.ListApartmentEventsResponse;
import com.everhomes.rest.address.ListApartmentsCommand;
import com.everhomes.rest.address.ListApartmentsInBuildingCommand;
import com.everhomes.rest.address.ListApartmentsInBuildingResponse;
import com.everhomes.rest.address.ListApartmentsResponse;
import com.everhomes.rest.address.ListAuthorizePricesResponse;
import com.everhomes.rest.address.ListBuildingByKeywordCommand;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.address.ListPropApartmentsResponse;
import com.everhomes.rest.address.UpdateApartmentCommand;
import com.everhomes.rest.address.admin.ImportAddressCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.FindReservationsCommand;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.contract.ContractEventDTO;
import com.everhomes.rest.community.ListApartmentEnterpriseCustomersCommand;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.ContractTrackingTemplateCode;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapType;
import com.everhomes.rest.family.ApproveMemberCommand;
import com.everhomes.rest.family.FamilyBillingTransactionDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.LeaveFamilyCommand;
import com.everhomes.rest.family.RejectMemberCommand;
import com.everhomes.rest.family.RevokeMemberCommand;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.forum.PostPrivacy;
import com.everhomes.rest.forum.PropertyPostDTO;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.ImageBody;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.AccountType;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.BillTransactionResult;
import com.everhomes.rest.organization.GetOrgDetailCommand;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.OrganizationBillingTransactionDTO;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationMemberGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationOrderStatus;
import com.everhomes.rest.organization.OrganizationOrderType;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationTaskStatus;
import com.everhomes.rest.organization.OrganizationTaskType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.PaidType;
import com.everhomes.rest.organization.TxType;
import com.everhomes.rest.organization.UpdateReservationCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.pushmessagelog.PushMessageTypeCode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.SetCurrentCommunityCommand;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserLocalStringCode;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.OrganizationOwnerCarSearcher;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhContractEvents;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.EhPmResoucreReservations;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerCars;
import com.everhomes.server.schema.tables.pojos.EhParkingCardCategories;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.CronDateUtils;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateStatisticHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.ProcessBillModel1;
import com.everhomes.util.excel.handler.PropMgrBillHandler;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.FieldItem;
import com.everhomes.varField.FieldParams;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;

import net.greghaines.jesque.Job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;


//import org.apache.commons.lang.StringUtils;

import net.greghaines.jesque.Job;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 物业和组织共用同一张表。所有的逻辑都由以前的communityId 转移到 organizationId。
 */
@Component
public class PropertyMgrServiceImpl implements PropertyMgrService, ApplicationListener<ContextRefreshedEvent> {
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
    private ForumService forumService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationService organizationService;

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
    private FieldService fieldService;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private ImportFileService importFileService;

	@Autowired
	private DefaultChargingItemProvider defaultChargingItemProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	private EnterpriseCustomerProvider enterpriseCustomerProvider;

   	@Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
   	private ContractBuildingMappingProvider contractBuildingMappingProvider;
   	@Autowired
   	private PushMessageLogService pushMessageLogService;

   	@Autowired
	protected LocaleStringService localeStringService;

   	@Autowired
	protected TaskService taskService;

   	@Autowired
	private AssetService assetService;

   	@Autowired
   	private ActivityService activityService;
   	
   	@Autowired
   	private AssetChargingItemService assetChargingItemService;
   	
   	@Autowired
   	private AssetGroupProvider assetGroupProvider;
   	
   	@Autowired
   	private UserPrivilegeMgr userPrivilegeMgr;
   	
	@Autowired
   	private LocaleTemplateService localeTemplateService;

    private String queueName = "property-mgr-push";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    
    boolean isRunning = false;

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
    	synchronized (this) {
			if(!isRunning) {
				workerPoolFactory.getWorkerPool().addQueue(queueName);
				isRunning = true;
				LOGGER.info("WorkerPool addQueue queueName ={}",queueName);
			}
		}
        
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    @Override
    public void applyPropertyMember(applyPropertyMemberCommand cmd) {
        User user = UserContext.current().getUser();
        Long communityId = user.getCommunityId();
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        this.checkCommunityIdIsEqual(cmd.getCommunityId().longValue(), communityId.longValue());
        Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
        long organizationId = org.getId();
        this.checkUserInOrg(user.getId(), organizationId);
        CommunityPmMember communityPmMember = this.createCommunityPmMember(organizationId, cmd.getContactDescription(), user);
        propertyMgrProvider.createPropMember(communityPmMember);
    }

    private void checkUserInOrg(Long userId, Long orgId) {
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
        if (member != null) {
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
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
		if(member == null){
			LOGGER.error("User is not in the organization.");
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"User is not in the organization.");
		}*/
    }

    public CommunityPmMember createCommunityPmMember(Long orgId, String description, User user) {

        CommunityPmMember communityPmMember = new CommunityPmMember();
        communityPmMember.setOrganizationId(orgId);
        communityPmMember.setContactName(user.getNickName());
        communityPmMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
        communityPmMember.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
        communityPmMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
        communityPmMember.setTargetId(user.getId());
        communityPmMember.setContactDescription(description);
        UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
        if (identifier != null) {
            communityPmMember.setContactToken(identifier.getIdentifierToken());
            communityPmMember.setContactType(identifier.getIdentifierType());
        }
        return communityPmMember;
    }

    private UserIdentifier getUserMobileIdentifier(Long userId) {
        List<UserIdentifier> userIndIdentifiers = userProvider.listUserIdentifiersOfUser(userId);
        if (userIndIdentifiers != null && userIndIdentifiers.size() > 0) {
            for (UserIdentifier userIdentifier : userIndIdentifiers) {
                if (userIdentifier.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode()) {
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
        Long addUserId = cmd.getTargetId();
        //添加已注册用户为管理员。
        if (addUserId != null && addUserId != 0) {
            List<CommunityPmMember> list = propertyMgrProvider.findPmMemberByCommunityAndTarget(cmd.getCommunityId(), cmd.getTargetType(), cmd.getTargetId());
            if (list == null || list.size() == 0) {
                if (cmd.getContactName() == null || cmd.getContactName().trim().equals("")) {
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
        User user = UserContext.current().getUser();

        List<CommunityPmMember> entityResultList = propertyMgrProvider.listUserCommunityPmMembers(user.getId());
        List<PropertyMemberDTO> members = new ArrayList<PropertyMemberDTO>();
        if (entityResultList != null && entityResultList.size() > 0) {
            for (CommunityPmMember communityPmMember : entityResultList) {
                Organization organization = organizationProvider.findOrganizationById(communityPmMember.getOrganizationId());
                if (OrganizationType.PM == OrganizationType.fromCode(organization.getOrganizationType())) {
                    PropertyMemberDTO dto = ConvertHelper.convert(communityPmMember, PropertyMemberDTO.class);
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
        if (totalCount == 0) return commandResponse;

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(organizationId, null, cmd.getPageOffset(), pageSize);
        commandResponse.setMembers(entityResultList.stream()
				.map(r->{
                    PropertyMemberDTO dto = ConvertHelper.convert(r, PropertyMemberDTO.class);
                    Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
                    if (organization != null) {
                        dto.setOrganizationName(organization.getName());
                        if (OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM) {
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
        if (entityResultList == null || entityResultList.size() == 0) {
            ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
            comand.setCommunityId(community.getId());
            comand.setKeyword("");
            comand.setPageSize(Integer.MAX_VALUE);
            List<AddressDTO> addresses = addressService.listAddressByKeyword(comand).getRequests();
            if (addresses != null && addresses.size() > 0) {
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
        if (totalCount == 0) return commandResponse;
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int pageCount = getPageCount(totalCount, pageSize);

        List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(organizationId, cmd.getPageOffset(), pageSize);
        commandResponse.setMembers(entityResultList.stream()
				.map(r->{
                    PropAddressMappingDTO dto = ConvertHelper.convert(r, PropAddressMappingDTO.class);
                    Address address = addressProvider.findAddressById(dto.getAddressId());
                    if (address != null)
                        dto.setAddressName(address.getAddress());
                    return dto;
                }).collect(Collectors.toList()));
        commandResponse.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return commandResponse;
    }

    @Override
    public ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd) {
        ListPropBillCommandResponse commandResponse = new ListPropBillCommandResponse();
        User user = UserContext.current().getUser();
        if (cmd.getCommunityId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        //权限控制
        long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
        cmd.setCommunityId(organizationId);
        int totalCount = propertyMgrProvider.countCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress());
        if (totalCount == 0) return commandResponse;
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int pageCount = getPageCount(totalCount, pageSize);
        List<CommunityPmBill> entityResultList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress(), cmd.getPageOffset(), pageSize);
        commandResponse.setMembers(entityResultList.stream()
				.map(r->{
                    PropBillDTO dto = ConvertHelper.convert(r, PropBillDTO.class);
                    List<CommunityPmBillItem> itemList = propertyMgrProvider.listCommunityPmBillItems(dto.getId());
                    List<PropBillItemDTO> itemDtoList = new ArrayList<PropBillItemDTO>();
                    for (CommunityPmBillItem item : itemList) {
                        PropBillItemDTO itemDto = ConvertHelper.convert(item, PropBillItemDTO.class);
                        itemDtoList.add(itemDto);
                    }
                    dto.setItemList(itemDtoList);
                    return dto;
                })
                .collect(Collectors.toList()));
        commandResponse.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return commandResponse;
    }


    @Override
    public ListPropOwnerCommandResponse listPMPropertyOwnerInfo(ListPropOwnerCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        Community community = this.checkCommunity(cmd.getCommunityId());

        ListPropOwnerCommandResponse commandResponse = new ListPropOwnerCommandResponse();
        Organization org = this.checkOrganizationByCommIdAndOrgType(community.getId(), OrganizationType.PM.getCode());
        long organizationId = org.getId();

        int totalCount = propertyMgrProvider.countCommunityPmOwners(organizationId, cmd.getAddress(), cmd.getContactToken());
        if (totalCount == 0) return commandResponse;
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int pageCount = getPageCount(totalCount, pageSize);

        List<CommunityPmOwner> entityResultList = propertyMgrProvider.listCommunityPmOwners(organizationId, cmd.getAddress(), cmd.getContactToken(), cmd.getPageOffset(), pageSize);
        commandResponse.setMembers(entityResultList.stream()
                .map(r -> {
                    return ConvertHelper.convert(r, PropOwnerDTO.class);
                })
                .collect(Collectors.toList()));
        commandResponse.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return commandResponse;
    }

    private Community checkCommunity(Long communityId) {
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
            LOGGER.error("Unable to find the community");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Unable to find the community.");
        }
        return community;
    }

    private int getPageCount(int totalCount, int pageSize) {
        int pageCount = totalCount / pageSize;

        if (totalCount % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }


    @Caching(evict = {@CacheEvict(value = "ForumPostById", key = "#topicId")})
    private void sendComment(long topicId, long forumId, long userId, long category) {
        Post comment = new Post();
        comment.setParentPostId(topicId);
        comment.setForumId(forumId);
        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        comment.setCreatorUid(userId);
        comment.setContentType(PostContentType.TEXT.getCode());
        String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_COMMENT, "");
        if (StringUtils.isEmpty(template)) {
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
     * @param userId   维修人员id
     * @param owerId   业主id
     * @param category 分类
     */
    private void sendMSMToUser(long topicId, long userId, long owerId, long category) {
        //给维修人员发送短信是否显示业主地址
        //		String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
        List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
        userList.stream().filter((u) -> {
            if (u.getIdentifierType() != IdentifierType.MOBILE.getCode())
                return false;
            return true;
        });
        if (userList == null || userList.isEmpty()) return;
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
        smsProvider.sendSms(userList.get(0).getNamespaceId(), cellPhone, templateScope, templateId, templateLocale, null);
    }

    @Override
    public ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());

        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20L);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize().intValue());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

        ListPropInvitedUserCommandResponse response = new ListPropInvitedUserCommandResponse();
        List<PropInvitedUserDTO> users = new ArrayList<PropInvitedUserDTO>();

        List<OrganizationMember> result = this.organizationProvider.listOrganizationMembers(org.getId(), null, offset, pageSize + 1);
        if (result != null && !result.isEmpty()) {
            if (result.size() == pageSize + 1) {
                result.remove(result.size() - 1);
                response.setNextPageAnchor(cmd.getPageOffset() + 1);
            }
            for (OrganizationMember r : result) {
                PropInvitedUserDTO user = new PropInvitedUserDTO();
                user.setContactType(r.getContactType());
                user.setContactToken(r.getContactToken());
                user.setInvitorName(r.getContactName());

                if (r.getTargetId() != null && r.getTargetId().longValue() != 0L) {
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
        User user = UserContext.current().getUser();
        if (cmd.getCommunityId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        //权限控制--admin角色
        CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
        if (communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode()) {
            LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() + ",memberId=" + cmd.getMemberId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the property member or the property member status is not confirming.");
        }
        communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
        propertyMgrProvider.updatePropMember(communityPmMember);

        //发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
        long userId = communityPmMember.getTargetId();
        if (userId != 0) {

        }
    }

    @Override
    public void rejectPropMember(CommunityPropMemberCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getCommunityId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        //权限控制--admin角色
        CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
        if (communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode()) {
            LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() + ",memberId=" + cmd.getMemberId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the property member or the property member status is not confirming.");
        }
        long userId = communityPmMember.getTargetId();
        propertyMgrProvider.deletePropMember(communityPmMember);

        //发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
        if (userId != 0) {

        }
    }

    @Override
    public void revokePMGroupMember(DeletePropMemberCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getCommunityId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        //权限控制--admin角色
        CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
        if (communityPmMember == null) {
            LOGGER.error("Unable to find the property member.communityId=" + cmd.getCommunityId() + ",memberId=" + cmd.getMemberId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the property member.");
        }
        long userId = communityPmMember.getTargetId();
        propertyMgrProvider.deletePropMember(communityPmMember);

        //发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
        if (userId != 0) {

        }
    }

    @Override
    public void approvePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Group family = this.checkFamily(cmd.getFamilyId());
        this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(), cmd.getCommunityId().longValue());

        ApproveMemberCommand comand = new ApproveMemberCommand();
        comand.setId(cmd.getFamilyId());
        comand.setMemberUid(cmd.getUserId());
        comand.setOperatorRole(Role.SystemAdmin);
        familyService.approveMember(comand);
    }

    private void checkCommunityIdIsEqual(long longValue, long longValue2) {
        if (longValue != longValue2) {
            LOGGER.error("communityId not equal.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                    "communityId not equal.");
        }
    }

    @Override
    public void rejectPropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Group family = this.checkFamily(cmd.getFamilyId());
        this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(), cmd.getCommunityId().longValue());

        String reason = "";

        RejectMemberCommand command = new RejectMemberCommand();
        command.setId(cmd.getFamilyId());
        command.setMemberUid(cmd.getUserId());
        command.setOperatorRole(Role.SystemAdmin);
        command.setReason(reason);
        familyService.rejectMember(command);
    }

    private void checkCommunityIdIsNull(Long communityId) {
        if (communityId == null) {
            LOGGER.error("communityId paramter is empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "communityId paramter is empty");
        }

    }

    @Override
    public void revokePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        this.checkFamilyIdIsNull(cmd.getFamilyId());
        Group family = this.checkFamily(cmd.getFamilyId());
        this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(), cmd.getCommunityId().longValue());

        String reason = "";
        RevokeMemberCommand comand = new RevokeMemberCommand();
        comand.setId(cmd.getFamilyId());
        comand.setMemberUid(cmd.getUserId());
        comand.setOperatorRole(Role.ResourceAdmin);
        comand.setReason(reason);
        familyService.revokeMember(comand);
    }

    @Override
    public Tuple<Integer, List<BuildingDTO>> listPropBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Tuple<Integer, List<BuildingDTO>> tuple = addressService.listBuildingsByKeyword(cmd);

        return tuple;
    }

    @Override
    public UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd) {
        User operator = UserContext.current().getUser();
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if (operator != null) {
            namespaceId = operator.getNamespaceId();
        }

        UserTokenCommandResponse commandResponse = new UserTokenCommandResponse();
        User user = userService.findUserByIndentifier(namespaceId, cmd.getUserIdentifier());
        if (user != null) {
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
        if (totalCount == 0) return commandResponse;
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        long offset = PaginationHelper.offsetFromPageOffset(Long.valueOf(cmd.getPageOffset()), pageSize);
        int pageCount = getPageCount(totalCount, pageSize);

        List<FamilyDTO> entityResultList = familyProvider.listWaitApproveFamily(cmd.getCommunityId(), offset, new Long(pageSize));
        commandResponse.setMembers(entityResultList.stream()
                .map(r -> {
                    return r;
                })
                .collect(Collectors.toList()));
        commandResponse.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return commandResponse;
    }

    @Override
    public void setApartmentStatus(SetPropAddressStatusCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getAddressId() == null || cmd.getStatus() == null) {
            LOGGER.error("propterty organizationId or addressId or status paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "propterty organizationId or addressId or status paramter can not be null or empty");
        }

        this.checkOrganization(cmd.getOrganizationId());
        CommunityAddressMapping mapping = this.checkAddressMappingByAddressId(cmd.getAddressId());

        mapping.setLivingStatus(cmd.getStatus());
        propertyMgrProvider.updateOrganizationAddressMapping(mapping);
    }

    private CommunityAddressMapping checkAddressMappingByAddressId(Long addressId) {
        CommunityAddressMapping mapping = this.propertyMgrProvider.findAddressMappingByAddressId(addressId);
        if (mapping == null) {
            LOGGER.error("address mapping is not find.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ADDRESS_MAPPING_NOT_EXIST,
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
        if (family == null) {
            LOGGER.error("family is not existed.communityId=" + cmd.getCommunityId() + ",addressId=" + cmd.getAddressId());
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_FAMILY_NOT_EXIST,
                    "Unable to find the family.");
        }
        this.checkCommunityIdIsEqual(family.getIntegralTag2().longValue(), cmd.getCommunityId().longValue());

        PropFamilyDTO dto = new PropFamilyDTO();
        dto.setName(family.getDisplayName());
        dto.setId(family.getId());
        dto.setMemberCount(family.getMemberCount());
        dto.setAddressId(family.getAddressId());
        dto.setAddress(family.getName());
        Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), OrganizationType.PM.getCode());
        CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(org.getId(), cmd.getAddressId());
        if (mapping != null) {
            dto.setLivingStatus(mapping.getLivingStatus());
        } else {
            dto.setLivingStatus(AddressMappingStatus.LIVING.getCode());
        }
        return dto;
    }

    private Organization checkOrganizationByCommIdAndOrgType(Long communityId, String orgType) {
        Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, orgType);
        if (org == null) {
            LOGGER.error("organization can not find by communityId and orgType.communityId=" + communityId + ",orgType=" + orgType);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_NOT_EXIST,
                    "organization can not find by communityId and orgType.");
        }
        return org;
    }

    @Override
    public void importPropertyBills(@Valid PropCommunityIdCommand cmd, MultipartFile[] files) {
        User user = UserContext.current().getUser();
        Long communityId = cmd.getCommunityId();
        if (communityId == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
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
        if (billList != null && billList.size() > 0) {
            for (CommunityPmBill bill : billList) {
                if (mappingList != null && mappingList.size() > 0) {
                    for (CommunityAddressMapping mapping : mappingList) {
                        if (bill != null && bill.getAddress().equals(mapping.getOrganizationAddress())) ;
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
        this.dbProvider.execute((status) -> {
            propertyMgrProvider.createPropBill(bill);
            List<CommunityPmBillItem> itemList = bill.getItemList();
            if (itemList != null && itemList.size() > 0) {
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
        User user = UserContext.current().getUser();
        Long communityId = cmd.getCommunityId();
        if (communityId == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + communityId);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        CommunityPmBill bill = propertyMgrProvider.findPropBillById(cmd.getBillId());
        sendPropertyBillById(bill);

    }

    @Override
    public void sendPropertyBillByMonth(PropCommunityBillDateCommand cmd) {
        User user = UserContext.current().getUser();
        Long communityId = cmd.getCommunityId();
        if (communityId == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
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
        if (billList != null && billList.size() > 0) {
            for (CommunityPmBill communityPmBill : billList) {
                Address address = addressProvider.findAddressById(communityPmBill.getEntityId());
                if (address != null) {
                    Family family = familyProvider.findFamilyByAddressId(address.getId());
                    if (family != null) {
                        String message = buildBillMessage(communityPmBill);
                        sendNoticeToFamilyById(family.getId(), message, MessageBodyType.TEXT.getCode());
                    }
                }
            }
        }

    }

    public String buildBillMessage(CommunityPmBill bill) {
        String template = configurationProvider.getValue(PROP_MESSAGE_BILL, "");
        String content = "\n" + template + "账单时间：" + bill.getDateStr() + "\n";
        List<CommunityPmBillItem> itemList = propertyMgrProvider.listCommunityPmBillItems(bill.getId());
        if (itemList != null && itemList.size() > 0) {
            for (CommunityPmBillItem item : itemList) {
                content += item.getItemName() + ": " + item.getTotalAmount() + "元\n";
            }
        }
        //content += "总费用：" + bill.getTotalAmount();
        return content;
    }


    public void sendPropertyBillById(CommunityPmBill bill) {
        if (bill == null) {
            LOGGER.error("Unable to find the bill.");
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_BILL,
                    "Unable to find the bill.");
        }
        Address address = addressProvider.findAddressById(bill.getEntityId());
        if (address == null) {
            LOGGER.error("Unable to find the address.addressId=" + bill.getEntityId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_ADDRESS,
                    "Unable to find the address.");
        }
        Family family = familyProvider.findFamilyByAddressId(address.getId());
        if (family == null) {
            LOGGER.error("Unable to find the family.familyId=" + address.getId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_FAMILY,
                    "Unable to find the family.");
        }
        String message = buildBillMessage(bill);
        sendNoticeToFamilyById(family.getId(), message, MessageBodyType.TEXT.getCode());
    }

    @Override
    public void sendNotice(SendNoticeCommand cmd) {

       // this.checkCommunityIdIsNull(cmd.getCommunityId());

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
        //创建消息记录
        Long logId = pushMessageLogService.createfromSendNotice(cmd, PushMessageTypeCode.APP.getCode());
        cmd.setLogId(logId);
        LOGGER.info("create pushMessageLog and set status waitting  .");
        // 调度执行一键推送
        /*Job job = new Job(
                SendNoticeAction.class.getName(),
                StringHelper.toJsonString(cmd),
                String.valueOf(UserContext.currentUserId()),
                UserContext.current().getScheme(),
                cmd.getNamespaceId()
        );


        jesqueClientFactory.getClientPool().enqueue(queueName, job);*/

        //更换推送方式 add by huangliangming 20180723
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("cmd", StringHelper.toJsonString(cmd));
        paramMap.put("namespaceId", cmd.getNamespaceId());
        paramMap.put("operatorUid", String.valueOf(UserContext.currentUserId()));
        paramMap.put("schema", UserContext.current().getScheme());
        String triggerName = queueName + System.currentTimeMillis();
        String jobName = queueName + System.currentTimeMillis();
        Long time = System.currentTimeMillis()+2*1000;
        CronDateUtils.getCron(new Timestamp(time));
        String cronExpression = CronDateUtils.getCron(new Timestamp(time));
        LOGGER.info("triggerName:[" + triggerName+"];jobName:["+jobName+"];cronExpression:["+cronExpression+"];params:["+paramMap+"].");
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, SendNoticeNewAction.class, paramMap);

    }

    @Override
    public void pushMessage(SendNoticeCommand cmd, User operator) {
        MessageBodyType bodyType = MessageBodyType.fromCode(cmd.getMessage());
        if (bodyType == MessageBodyType.TEXT && StringUtils.isEmpty(cmd.getMessage())) {
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_MESSAGE,
                    "Message body should not empty.");
        }

        SendNoticeMode sendNoticeMode = SendNoticeMode.fromCode(cmd.getSendMode());

        if (sendNoticeMode == SendNoticeMode.NAMESPACE) {
            sendNoticeToNamespaceUser(cmd);
        }
         //添加按项目推送的维度 20180712
        else if(sendNoticeMode == SendNoticeMode.COMMUNITY){
        	sendNoticeToCommunitsUser(cmd);

        }
        //按号码推送
        else if(sendNoticeMode == SendNoticeMode.MOBILE){
        	sendNoticeByPhone(cmd, operator);
        }
        //此代码目前暂时无用，但后续应该有借鉴之用，暂时不删，前面有很多坑要靠这段代码来填
        else {
            // 八百年前的代码，看都不想看
            Community community = this.checkCommunity(cmd.getCommunityId());

            if (community.getCommunityType() == CommunityType.RESIDENTIAL.getCode()) {
                sendNoticeToCommunityPmOwner(cmd, operator);
            } else if (community.getCommunityType() == CommunityType.COMMERCIAL.getCode()) {
                // sendNoticeToEnterpriseContactor(cmd);
                this.sendNoticeToOrganizationMember(cmd, operator);
            }
        }
    }

    private void sendNoticeToNamespaceUser(SendNoticeCommand cmd) {
        String messageBody = processMessage(cmd.getMessage(), cmd.getMessageBodyType(),
                cmd.getImgUri(), EntityType.NAMESPACE.getCode(), Long.valueOf(cmd.getNamespaceId()));

        CrossShardListingLocator locator = new CrossShardListingLocator();
        do {
            List<User> users = userProvider.listUserByNamespace(null, cmd.getNamespaceId(), locator, 200);
            for (User u : users) {
                sendNoticeToUserById(u.getId(), messageBody, cmd.getMessageBodyType());
            }
        } while (locator.getAnchor() != null);
    }

    /**
     * 按项目推送
     * @param cmd
     */
    private void sendNoticeToCommunitsUser(SendNoticeCommand cmd) {
        String messageBody = processMessage(cmd.getMessage(), cmd.getMessageBodyType(),
                cmd.getImgUri(), EntityType.NAMESPACE.getCode(), Long.valueOf(cmd.getNamespaceId()));

        List<Integer> status = new ArrayList<Integer>();
        status.add(AuthFlag.PENDING_AUTHENTICATION.getCode());
        status.add(AuthFlag.AUTHENTICATED.getCode());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        do {
            List<UserOrganizations> users = organizationProvider.findUserByCommunityIDAndAuthStatus(cmd.getNamespaceId(),
            							cmd.getCommunityIds(), status, locator, 200);
            for (UserOrganizations u : users) {
                sendNoticeToUserById(u.getUserId(), messageBody, cmd.getMessageBodyType());
            }
        } while (locator.getAnchor() != null);
    }

    public void sendNoticeToEnterpriseContactor(SendNoticeCommand cmd) {

        Long communityId = cmd.getCommunityId();
        List<String> buildingNames = cmd.getBuildingNames();
        List<Long> addressIds = cmd.getAddressIds();

        List<Long> enterpriseIds = new ArrayList<Long>();
        List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();

        //未设定消息提类型则默认文本 sfyan by 20160729
        if (null == MessageBodyType.fromCode(cmd.getMessageBodyType())) {
            cmd.setMessageBodyType(MessageBodyType.TEXT.getCode());
        }

        //按园区发送: buildingNames 和 addressIds 为空。
        if ((buildingNames == null || buildingNames.size() == 0) && (addressIds == null || addressIds.size() == 0)) {
            List<EnterpriseCommunityMap> enterpriseMapList = enterpriseProvider.queryEnterpriseMapByCommunityId(new ListingLocator(),
                    communityId, Integer.MAX_VALUE - 1, (loc, query) -> {
                        query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
                        query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
                        return null;
                    });
            if (enterpriseMapList != null && enterpriseMapList.size() > 0) {
                for (EnterpriseCommunityMap enterpriseMap : enterpriseMapList) {
                    enterpriseIds.add(enterpriseMap.getMemberId());
                    List<EnterpriseContact> contactList = enterpriseContactProvider.queryContactByEnterpriseId(new ListingLocator(),
                            enterpriseMap.getMemberId(), Integer.MAX_VALUE - 1, null);
                    contacts.addAll(contactList);
                }
            }
        }

        //按地址发送：
        else if (addressIds != null && addressIds.size() > 0) {
            for (Long addressId : addressIds) {
                EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByAddressId(addressId);
                if (enterpriseAddress != null) {
                    enterpriseIds.add(enterpriseAddress.getEnterpriseId());
                    List<EnterpriseContact> contactList = enterpriseContactProvider.queryContactByEnterpriseId(new ListingLocator(),
                            enterpriseAddress.getEnterpriseId(), Integer.MAX_VALUE - 1, null);
                    contacts.addAll(contactList);
                }
            }
        }

        //按楼栋发送：
        else if ((addressIds == null || addressIds.size() == 0) && (buildingNames != null && buildingNames.size() > 0)) {
            for (String buildingName : buildingNames) {
                List<ApartmentDTO> addresses = addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, Integer.MAX_VALUE);
                if (addresses != null && addresses.size() > 0) {
                    for (ApartmentDTO address : addresses) {
                        EnterpriseAddress enterpriseAddress = enterpriseProvider.findEnterpriseAddressByAddressId(address.getAddressId());
                        if (enterpriseAddress != null) {
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

        processCommunityEnterpriseContactor(communityId, contacts, cmd.getMessage(), cmd.getMessageBodyType());
    }

    @Override
    public void sendNoticeToOrganizationMember(SendNoticeCommand cmd, User user) {
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
        if (null != addressIds && 0 != addressIds.size()) {
            for (Long addressId : addressIds) {
                OrganizationAddress orgAddress = organizationProvider.findOrganizationAddressByAddressId(addressId);
                if (null != orgAddress) {
                    orgs.add(organizationProvider.findOrganizationById(orgAddress.getOrganizationId()));
                }
            }
        }
        // 根据楼栋Id获取要推送的企业
        else if (null != buildingIds && 0 != buildingIds.size()) {
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
        else if (null != buildingNames && 0 != buildingNames.size()) {
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
        else if (null != phones && 0 != phones.size()) {
            members = new ArrayList<>();
            for (String phone : phones) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
                OrganizationMember member = new OrganizationMember();
                member.setContactToken(phone);
                member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                if (null != userIdentifier) {
                    member.setTargetId(userIdentifier.getOwnerUid());
                    member.setTargetType(OrganizationMemberTargetType.USER.getCode());
                }
                members.add(member);
            }
        }
        // 根据小区获取要推送的企业
        else if (null != communityId) {
            List<OrganizationCommunityRequest> requests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(new CrossShardListingLocator(), communityId, 100000, null);
            for (OrganizationCommunityRequest req : requests) {
                orgs.add(organizationProvider.findOrganizationById(req.getMemberId()));
            }
        }

        if (0 != orgs.size()) {
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
    private List<OrganizationMember> getOrganizationMembersByAddress(List<Organization> orgs) {
        List<OrganizationMember> members = new ArrayList<>();
        for (Organization organization : orgs) {
            if (null != organization) {
                members.addAll(organizationProvider.listOrganizationMembersByOrgId(organization.getId()));
            }
        }
        return members;
    }

    private void processCommunityEnterpriseContactor(Long communityId, List<EnterpriseContact> contacts, String message, String messageBodyType) {

        List<String> phones = new ArrayList<String>();
        List<Long> userIds = new ArrayList<Long>();
        if (contacts != null && contacts.size() > 0) {
            for (EnterpriseContact contact : contacts) {

                if (contact.getUserId() == 0) {// 不是user，发短信
                    List<EnterpriseContactEntry> entries = enterpriseContactProvider.queryContactEntryByContactId(contact, ContactType.MOBILE.getCode());
                    if (entries != null && entries.size() > 0) {
                        phones.add(entries.get(0).getEntryValue());
                    }

                } else {//是user，发个人消息
                    userIds.add(contact.getUserId());

                }
            }
        }

        //是user，发个人信息.
        if (userIds != null && userIds.size() > 0) {
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
        smsProvider.sendSms(UserContext.current().getUser().getNamespaceId(), phoneArray, templateScope, templateId, templateLocale, variables);
        //		//不是user，发短信。
        //		if(phones != null && phones.size() > 0 ){
        //			for (String phone : phones) {
        //				smsProvider.sendSms(phone, message);
        //			}
        //		}

    }

    private void processSmsByMembers(List<OrganizationMember> members, String message, String messageBodyType, String imgUri, User user) {

        List<String> phones = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        // 区分是否是平台用户
        for (OrganizationMember member : members) {
            if (member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
                userIds.add(member.getTargetId());
            } else {
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
            smsProvider.sendSms(user.getNamespaceId(), phoneArray, templateScope, templateId, user.getLocale(), variables);
        }
    }
    /**
     * 按号码发送
     * @param cmd
     * @param user
     */
    private void sendNoticeByPhone(SendNoticeCommand cmd, User user) {
    	List<String> phones = cmd.getMobilePhones();
    	Integer namespaceId = user.getNamespaceId();
    	 //按电话号码发送
        if (phones != null && phones.size() > 0) {
            List<OrganizationMember> members = new ArrayList<>();
            for (String phone : phones) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
                OrganizationMember member = new OrganizationMember();
                member.setContactToken(phone);
                member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                if (null != userIdentifier) {
                    member.setTargetId(userIdentifier.getOwnerUid());
                    member.setTargetType(OrganizationMemberTargetType.USER.getCode());
                }
                members.add(member);
            }
            if (members.size() > 0) {
                this.processSmsByMembers(members, cmd.getMessage(), cmd.getMessageBodyType(), cmd.getImgUri(), user);
            }
        }
    }


    private void sendNoticeToCommunityPmOwner(SendNoticeCommand cmd, User user) {

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

        List<CommunityPmOwner> owners = new ArrayList<>();
        List<Long> familyIds = new ArrayList<>();

        //按电话号码发送
        if (phones != null && phones.size() > 0) {
            List<OrganizationMember> members = new ArrayList<>();
            for (String phone : phones) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
                OrganizationMember member = new OrganizationMember();
                member.setContactToken(phone);
                member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                if (null != userIdentifier) {
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
        else if (addressIds != null && addressIds.size() > 0) {
            for (Long addressId : addressIds) {
                Family family = familyProvider.findFamilyByAddressId(addressId);
                if (family != null) {
                    familyIds.add(family.getId());
                }
                // List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(orgId, addressId);
                // owners.addAll(ownerList);
            }
        }
        //按楼栋发送：
        else if (buildingNames != null && buildingNames.size() > 0) {
            for (String buildingName : buildingNames) {
                List<ApartmentDTO> addresses = addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, Integer.MAX_VALUE);
                if (addresses != null && addresses.size() > 0) {
                    for (ApartmentDTO address : addresses) {
                        Family family = familyProvider.findFamilyByAddressId(address.getAddressId());
                        if (family != null) {
                            familyIds.add(family.getId());
                        }
                        // List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(orgId, address.getApartmentId());
                        // owners.addAll(ownerList);
                    }
                }
            }
        }
        //按小区发送
        else if (null != communityId) {
            ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
            comand.setCommunityId(cmd.getCommunityId());
            comand.setKeyword("");
            comand.setPageSize(Integer.MAX_VALUE);
            List<AddressDTO> addresses = addressService.listAddressByKeyword(comand).getRequests();
            if (addresses != null && addresses.size() > 0) {
                for (AddressDTO address : addresses) {
                    Family family = familyProvider.findFamilyByAddressId(address.getId());
                    if (family != null) {
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

        if (familyIds.size() > 0) {
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
        if (MessageBodyType.fromCode(messageBodyType) == MessageBodyType.IMAGE) {
            if (StringUtils.isEmpty(imgUri)) {
                LOGGER.error("uri is null.");
                return null;
            }
            ImageBody imageBody = contentServerService.parserImageBody(imgUri, ownerType, ownerId);
            if (null == imageBody) {
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
                System.currentTimeMillis() + "",
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

    public void sendNoticeToFamilyById(Long familyId, String message, String messageBodyType) {
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

    public void sendNoticeToUserById(Long userId, String message, String messageBodyType) {
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
        if (memberList != null && memberList.size() > 0) {
            for (CommunityPmMember communityPmMember : memberList) {
                sendNoticeToUserById(communityPmMember.getTargetId(), cmd.getMessage(), MessageBodyType.TEXT.getCode());
            }
        }
    }

    @Override
    public PostDTO createTopic(NewTopicCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getVisibleRegionId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(cmd.getVisibleRegionId());
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + cmd.getVisibleRegionId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }
        //权限控制
        if (cmd.getCreatorTag() == null || "".equals(cmd.getCreatorTag())) {
            cmd.setCreatorTag(PostEntityTag.PM.getCode());
        }
        if (cmd.getPrivateFlag() == null || "".equals(cmd.getCreatorTag())) {
            cmd.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
        }
        if (cmd.getVisibleRegionType() == null || cmd.getVisibleRegionType() == 0) {
            cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
        }
        if (cmd.getTargetTag() == null || "".equals(cmd.getCreatorTag())) {
            cmd.setTargetTag(PostEntityTag.USER.getCode());
        }
        PostDTO post = forumService.createTopic(cmd);
        return post;
    }

    @Override
    public ListPropPostCommandResponse queryTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
        ListPropPostCommandResponse response = new ListPropPostCommandResponse();
        User user = UserContext.current().getUser();

        Long communityId = cmd.getCommunityId();
        if (communityId == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
            LOGGER.error("Unable to find the community.communityId=" + communityId);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the community.");
        }

        //权限控制
        long organizationId = findPropertyOrganizationId(cmd.getCommunityId());
        cmd.setCommunityId(organizationId);
        int totalCount = 0; //propertyMgrProvider.countCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus());
        if (totalCount == 0) return response;
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        List<PropertyPostDTO> results = new ArrayList<PropertyPostDTO>();
        List<CommunityPmTasks> tasks = null; //propertyMgrProvider.listCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, OrganizationTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus(), cmd.getPageOffset(), pageSize);
        if (tasks != null && tasks.size() > 0) {
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
        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return response;
    }

    @Override
    public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        Long communityId = cmd.getCommunityId();
        if (communityId == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
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
        String taskType = taskTypeObj == null ? null : taskTypeObj.getCode();
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
        List<Integer> todayList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType, currentStartDate.getTime(), date.getTime());

        /** 上周数量列表*/
        List<Integer> weekList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType, weekStartDate.getTime(), date.getTime());

        /** z昨天数量列表*/
        List<Integer> yesterdayList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType, yesterdayStartDate.getTime(), currentStartDate.getTime());

        /** 上月数量列表*/
        List<Integer> monthList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType, monthStartDate.getTime(), date.getTime());

        /** 时间点数量列表*/
        List<Integer> dateList = null;

        if (!StringUtils.isEmpty(startStrTime) && !StringUtils.isEmpty(endStrTime)) {
            Date startTime;
            Date endTime;
            try {
                startTime = DateStatisticHelper.parseDateStrToMin(startStrTime);
                endTime = DateStatisticHelper.parseDateStrToMax(endStrTime);
                dateList = this.getTaskCounts(organizationId, cmd.getCommunityId(), taskType, startTime.getTime(), endTime.getTime());
            } catch (ParseException e) {
                LOGGER.error("failed to parse date.startStrTime=" + startStrTime + ",endStrTime=" + endStrTime);
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
        if (contentCategoryId != null) {
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_NOTICE) {
                return OrganizationTaskType.NOTICE;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_REPAIRS) {
                return OrganizationTaskType.REPAIRS;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
                return OrganizationTaskType.CONSULT_APPEAL;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
                return OrganizationTaskType.COMPLAINT_ADVICE;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_EMERGENCY_HELP) {
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

        int sum = addressProvider.countApartment(communityId);
        int defaultCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.DEFAULT.getCode());
        int liveCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.LIVING.getCode()) + sum - propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, null);
        int rentCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.RENT.getCode());
        int freeCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.FREE.getCode());
        int saledCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.SALED.getCode());
        int unsaleCount = propertyMgrProvider.countCommunityAddressMappings(organizationId, communityId, AddressMappingStatus.UNSALE.getCode());

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

        // 科技园的从address表里统计，其它域空间还是按以前的方式统计
        if (community.getNamespaceId() != 1000000) {
            return getApartmentStatistics(cmd);
        }
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
        List<OrganizationCommunity> organizationCommunityList = organizationProvider.listOrganizationByCommunityId(cmd.getCommunityId());
        if (organizationCommunityList != null && organizationCommunityList.size() > 0) {
            for (OrganizationCommunity organizationCommunity : organizationCommunityList) {
                Organization organization = organizationProvider.findOrganizationById(organizationCommunity.getOrganizationId());
                if (organization != null && OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM) {
                    dto = ConvertHelper.convert(organization, OrganizationDTO.class);
                    break;
                }
            }
        }
        if (dto == null) {
            Organization organization = new Organization();
            organization.setLevel(0);
            organization.setAddressId(0l);
            organization.setName(community.getName() + "物业");
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
        if (dto != null) {
            return dto.getId();
        } else {
            return 0l;
        }
    }

    @Override
    public Community findPropertyOrganizationcommunity(Long organizationId) {
        Community community = new Community();
        OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationPropertyCommunity(organizationId);
        if (organizationCommunity != null) {
            community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
        }
        return community;
    }

    public List<PropCommunityContactDTO> listPropertyCommunityContacts(ListPropCommunityContactCommand cmd) {

        if (cmd.getCommunityId() == null) {
            LOGGER.error("propterty communityId paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                    "propterty communityId paramter can not be null or empty");
        }
        GetOrgDetailCommand c = new GetOrgDetailCommand();
        c.setCommunityId(cmd.getCommunityId());
        c.setOrganizationType(cmd.getOrganizationType());
        OrganizationDTO organizationDTO = this.organizationService.getOrganizationByComunityidAndOrgType(c);
        if (organizationDTO == null) {
            LOGGER.error("Property organization is not exists.communityId=" + cmd.getCommunityId());
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_NOT_EXIST,
                    "Property organization is not exists.");
        }
        long organizationId = organizationDTO.getId();
        List<CommunityPmContact> communityPmContacts = propertyMgrProvider.listCommunityPmContacts(organizationId);
        if (communityPmContacts == null || communityPmContacts.isEmpty()) {
            LOGGER.error("Property community contacts is not exists.communityId=" + cmd.getCommunityId());
            return null;
        }
        List<PropCommunityContactDTO> propCommunityContactdtos = communityPmContacts.stream().map(r -> {
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
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_CREATE_APARTMENT, cmd.getOrganizationId(), cmd.getCommunityId());

    	if (cmd.getCommunityId() == null || cmd.getStatus() == null || StringUtils.isEmpty(cmd.getBuildingName())) {
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER, "Invalid parameters");
        }
        if (StringUtils.isEmpty(cmd.getApartmentName())) {
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_APARTMENT_NAME_EMPTY, "apartment name cannot be empty");
        }
        Community community = checkCommunity(cmd.getCommunityId());
        Long organizationId = findOrganizationByCommunity(community);

        Building building = communityProvider.findBuildingByCommunityIdAndName(community.getId(), cmd.getBuildingName());

        if (null == building) {
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BUILDING_NOT_EXIST, "not exists building name");
        }

        Address address = addressProvider.findAddressByCommunityAndAddress(community.getCityId(), community.getAreaId(), community.getId(), building.getName() + "-" + cmd.getApartmentName());
        if (address == null) {
            address = new Address();
            address.setCommunityId(community.getId());
            address.setCommunityName(community.getName());
            address.setCityId(community.getCityId());
            address.setCityName(community.getCityName());
            address.setAreaId(community.getAreaId());
            address.setAreaName(community.getAreaName());
            address.setBuildingName(building.getName());
            //TODO 这个BuildingId应该由前端传过来
            address.setBuildingId(building.getId());
            address.setApartmentName(cmd.getApartmentName());
            address.setAreaSize(cmd.getAreaSize());
            address.setAddress(building.getName() + "-" + cmd.getApartmentName());

            address.setBuildArea(cmd.getBuildArea());
            address.setRentArea(cmd.getRentArea());
            address.setChargeArea(cmd.getChargeArea());
            address.setSharedArea(cmd.getSharedArea());
            address.setFreeArea(cmd.getFreeArea());
            //房源所在楼层
            address.setApartmentFloor(cmd.getApartmentFloor());
            address.setIsFutureApartment((byte)0);

            if (cmd.getCategoryItemId() != null) {
                address.setCategoryItemId(cmd.getCategoryItemId());
//				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getCategoryItemId());
                ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getCategoryItemId());
                if (item != null) {
                    address.setCategoryItemName(item.getItemDisplayName());
                }
            }

            if (cmd.getSourceItemId() != null) {
                address.setSourceItemId(cmd.getSourceItemId());
//				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getSourceItemId());
                ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getSourceItemId());
                if (item != null) {
                    address.setSourceItemName(item.getItemDisplayName());
                }
            }
            address.setDecorateStatus(cmd.getDecorateStatus());
            address.setOrientation(cmd.getOrientation());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setNamespaceId(community.getNamespaceId());
            addressProvider.createAddress(address);
            //添加日志
            saveAddressEvent(AddressTrackingTemplateCode.ADDRESS_ADD,address,null,null,null);
        } else if (AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
        	address.setCommunityId(community.getId());
            address.setCommunityName(community.getName());
            address.setBuildingName(building.getName());
            address.setBuildingId(building.getId());

        	address.setAreaSize(cmd.getAreaSize());
            address.setBuildArea(cmd.getBuildArea());
            address.setRentArea(cmd.getRentArea());
            address.setChargeArea(cmd.getChargeArea());
            address.setSharedArea(cmd.getSharedArea());
            address.setFreeArea(cmd.getFreeArea());
            //房源所在楼层
            address.setApartmentFloor(cmd.getApartmentFloor());
            if (cmd.getCategoryItemId() != null) {
                address.setCategoryItemId(cmd.getCategoryItemId());
//				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getCategoryItemId());
                ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getOwnerId(),cmd.getCommunityId(), cmd.getCategoryItemId());
                if (item != null) {
                    address.setCategoryItemName(item.getItemDisplayName());
                }
            }

            if (cmd.getSourceItemId() != null) {
                address.setSourceItemId(cmd.getSourceItemId());
//				ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getSourceItemId());
                ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), cmd.getSourceItemId());
                if (item != null) {
                    address.setSourceItemName(item.getItemDisplayName());
                }
            }
            address.setDecorateStatus(cmd.getDecorateStatus());
            address.setOrientation(cmd.getOrientation());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setOperateTime(getCurrentTimestamp());
            addressProvider.updateAddress(address);
            //添加日志
            saveAddressEvent(AddressTrackingTemplateCode.ADDRESS_ADD,address,null,null,null);
        } else {
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_EXISTS_APARTMENT_NAME, "exists apartment name");
        }

        //添加房源、小区、机构的对应关系，房源的具体状态存在eh_organization_address_mappings表中
        insertOrganizationAddressMapping(organizationId, community, address, cmd.getStatus());

        //门牌对应的楼栋和园区的sharedArea chargeArea buildArea rentArea都要增加相应的值 by xiongying 20170815
        if (address.getAreaSize() != null) {
            Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
            building.setAreaSize(buildingAreaSize + address.getAreaSize());

            Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
            community.setAreaSize(communityAreaSize + address.getAreaSize());
        }
        if (address.getRentArea() != null) {
            Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
            building.setRentArea(buildingRentArea + address.getRentArea());

            Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
            community.setRentArea(communityRentArea + address.getRentArea());
        }
        if (address.getSharedArea() != null) {
            Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
            building.setSharedArea(buildingSharedArea + address.getSharedArea());

            Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
            community.setSharedArea(communitySharedArea + address.getSharedArea());
        }
        if (address.getBuildArea() != null) {
            Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
            building.setBuildArea(buildingBuildArea + address.getBuildArea());

            Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
            community.setBuildArea(communityBuildArea + address.getBuildArea());
        }
        if (address.getChargeArea() != null) {
            Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
            building.setChargeArea(buildingChargeArea + address.getChargeArea());

            Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
            community.setChargeArea(communityChargeArea + address.getChargeArea());
        }
        if (address.getFreeArea() != null) {
            Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
            building.setFreeArea(buildingFreeArea + address.getFreeArea());

            Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
            community.setFreeArea(communityFreeArea + address.getFreeArea());
        }
        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
    }

    @Override
    public void updateApartment(UpdateApartmentCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_APARTMENT, cmd.getOrganizationId(), cmd.getCommunityId());
    	
    	dbProvider.execute((TransactionStatus status) -> {
	    	Address address = addressProvider.findAddressById(cmd.getId());
	        if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
	            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER, "Invalid parameters");
	        }
	        //用于日志记录时进行比较
	        Address oldAddress = addressProvider.findAddressById(cmd.getId());
	        Community community = checkCommunity(address.getCommunityId());
	        Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
	        //房源名称
	        if (!StringUtils.isEmpty(cmd.getApartmentName())) {
	            //Address other = addressProvider.findAddressByBuildingApartmentName(address.getNamespaceId(), address.getCommunityId(), address.getBuildingName(), cmd.getApartmentName());
	            Address other = addressProvider.findNotInactiveAddressByBuildingApartmentName(address.getNamespaceId(), address.getCommunityId(), address.getBuildingName(), cmd.getApartmentName());
	            if (other != null && !other.getId().equals(cmd.getId())) {
	                throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_EXISTS_APARTMENT_NAME, "exists apartment name");
	            }
	            address.setApartmentName(cmd.getApartmentName());
	            address.setAddress(address.getBuildingName() + "-" + cmd.getApartmentName());
	            //update eh_contract_building_mapping
	            List<ContractBuildingMapping> contractBuildingMappingList = addressProvider.findContractBuildingMappingByAddressId(address.getId());
	            if (contractBuildingMappingList != null && contractBuildingMappingList.size() > 0) {
	                for (ContractBuildingMapping contractBuildingMapping : contractBuildingMappingList) {
	                    contractBuildingMapping.setApartmentName(cmd.getApartmentName());
	                    addressProvider.updateContractBuildingMapping(contractBuildingMapping);
	                }
	            }
	        }
	        //房源状态；
	        //用于日志记录
	        Byte oldLivingStatus = null;
	        //在update的时候，前端一定会传一个status过来，所以这里的判断其实是多余的(当前版本5.11.0)
	        if (cmd.getStatus() != null) {
	            Long organizationId = findOrganizationByCommunity(community);
	            //针对标准版的兼容，因为在标准版中一个community可能被不同的organization管理，但同一时刻只能是一个organization管理。
	            //这里再以organizationId为条件来查可能就查不出来了。而且目前业务中，addressId和livingStatus应该是一一对应的。
	            //CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
	            CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMappingByAddressId(address.getId());
	            if (communityAddressMapping != null) {
	            	oldLivingStatus = communityAddressMapping.getLivingStatus(); 
	            	if (!cmd.getStatus().equals(communityAddressMapping.getLivingStatus())) {
	            		communityAddressMapping.setLivingStatus(cmd.getStatus());
		                organizationProvider.updateOrganizationAddressMapping(communityAddressMapping);
					}
	            } else {
	            	/*
	            	 * 在目前的业务逻辑中，创建房源时，一定要选择一个房源状态，因此communityAddressMapping这个对象肯定不会为空。
	            	 * 基于上述原因，这个else分支的代码肯定是不会运行的。
	            	 * (当前版本5.11.0)
	            	*/
	                communityAddressMapping = new CommunityAddressMapping();
	                //communityAddressMapping.setOrganizationId(organizationId);
	                communityAddressMapping.setCommunityId(community.getId());
	                communityAddressMapping.setAddressId(address.getId());
	                communityAddressMapping.setOrganizationAddress(address.getAddress());
	                communityAddressMapping.setLivingStatus(cmd.getStatus());
	                communityAddressMapping.setCreateTime(getCurrentTimestamp());
	                communityAddressMapping.setUpdateTime(communityAddressMapping.getCreateTime());
	                organizationProvider.createOrganizationAddressMapping(communityAddressMapping);
	            }
	        }
	        //建筑面积
	        Double areaSize = cmd.getAreaSize() == null ? 0.0 : cmd.getAreaSize();
			Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
			Double oldAddressAreaSize = address.getAreaSize() == null ? 0.0 : address.getAreaSize();
			building.setAreaSize(buildingAreaSize - oldAddressAreaSize + areaSize);
			Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
			community.setAreaSize(communityAreaSize - oldAddressAreaSize + areaSize);
	
	        address.setAreaSize(cmd.getAreaSize());
	        //公摊面积
			Double sharedArea = cmd.getSharedArea() == null ? 0.0 : cmd.getSharedArea();
	        Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
	        Double oldAddressSharedArea = address.getSharedArea() == null ? 0.0 : address.getSharedArea();
	        building.setSharedArea(buildingSharedArea - oldAddressSharedArea + sharedArea);
	        Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
	        community.setSharedArea(communitySharedArea - oldAddressSharedArea + sharedArea);
	        address.setSharedArea(cmd.getSharedArea());
	        //不知道业务上怎么定义这个字段，目前该字段没有在楼宇资产管理的业务中使用，目前版本5.9.0，2018年9月30日16:22:10
	        Double buildArea = cmd.getBuildArea() == null ? 0.0 : cmd.getBuildArea();
	        Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
	        Double oldAddressBuildArea = address.getBuildArea() == null ? 0.0 : address.getBuildArea();
	        building.setBuildArea(buildingBuildArea - oldAddressBuildArea + buildArea);
	        Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
	        community.setBuildArea(communityBuildArea - oldAddressBuildArea + buildArea);
	        address.setBuildArea(cmd.getBuildArea());
	        //在租面积
	        Double rentArea = cmd.getRentArea() == null ? 0.0 : cmd.getRentArea();
	        Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
	        Double oldAddressRentArea = address.getRentArea() == null ? 0.0 : address.getRentArea();
	        building.setRentArea(buildingRentArea - oldAddressRentArea + rentArea);
	        Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
	        community.setRentArea(communityRentArea - oldAddressRentArea + rentArea);
	        address.setRentArea(cmd.getRentArea());
	        //收费面积
	        Double chargeArea = cmd.getChargeArea() == null ? 0.0 : cmd.getChargeArea();
	        Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
	        Double oldAddressChargeArea = address.getChargeArea() == null ? 0.0 : address.getChargeArea();
	        building.setChargeArea(buildingChargeArea - oldAddressChargeArea + chargeArea);
	        Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
	        community.setChargeArea(communityChargeArea - oldAddressChargeArea + chargeArea);
	        address.setChargeArea(cmd.getChargeArea());
	        //可招租面积
	        Double freeArea = cmd.getFreeArea() == null ? 0.0 : cmd.getFreeArea();
	        Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
	        Double oldAddressFreeArea = address.getFreeArea() == null ? 0.0 : address.getFreeArea();
	        building.setFreeArea(buildingFreeArea - oldAddressFreeArea + freeArea);
	        Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
	        community.setFreeArea(communityFreeArea - oldAddressFreeArea + freeArea);
	        address.setFreeArea(cmd.getFreeArea());
	
	        if (cmd.getCategoryItemId() != null) {
	            address.setCategoryItemId(cmd.getCategoryItemId());
	            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(), cmd.getOwnerId(),address.getCommunityId(), cmd.getCategoryItemId());
	            if (item != null) {
	                address.setCategoryItemName(item.getItemDisplayName());
	            }
	        }
	
	        if (cmd.getSourceItemId() != null) {
	            address.setSourceItemId(cmd.getSourceItemId());
	            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(address.getNamespaceId(),cmd.getOwnerId(), address.getCommunityId(), cmd.getSourceItemId());
	            if (item != null) {
	                address.setSourceItemName(item.getItemDisplayName());
	            }
	        }
	
	        address.setDecorateStatus(cmd.getDecorateStatus());
	        address.setOrientation(cmd.getOrientation());
	        address.setApartmentFloor(cmd.getApartmentFloor());
	        address.setOperateTime(getCurrentTimestamp());
	        
	        addressProvider.updateAddress(address);
	        //添加日志
	        saveAddressEvent(AddressTrackingTemplateCode.ADDRESS_UPDATE,address,oldAddress,cmd.getStatus(),oldLivingStatus);
	        communityProvider.updateBuilding(building);
	        communityProvider.updateCommunity(community);
	        
	        return null;
    	});
    }


    @Override
    public GetApartmentDetailResponse getApartmentDetail(GetApartmentDetailCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_GET_APARTMENT_DETAIL, cmd.getOrganizationId(), cmd.getCommunityId());
    	
    	GetApartmentDetailResponse response = new GetApartmentDetailResponse();
        Address address = addressProvider.findAddressById(cmd.getId());
        if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ADDRESS_NOT_EXIST, "address not exist");
        }
        Community community = checkCommunity(address.getCommunityId());
        Long organizationId = findOrganizationByCommunity(community);
        //针对标准版的兼容，因为在标准版中一个community可能被不同的organization管理，但同一时刻只能是一个organization管理，
        //这里再以organizationId为条件来查可能就查不出来了。而且目前业务中，addressId和livingStatus应该是一一对应的
        //CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
        CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMappingByAddressId(address.getId());
        response = ConvertHelper.convert(address, GetApartmentDetailResponse.class);
        if (communityAddressMapping != null) {
            response.setStatus(communityAddressMapping.getLivingStatus());
        } else {
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
        } else {
            List<OrganizationOwnerAddress> organizationOwnerAddresses = propertyMgrProvider.listOrganizationOwnerAddressByAddressId(address.getNamespaceId(), address.getId());
            if (organizationOwnerAddresses != null) {
                List<OrganizationOwnerDTO> owerList = organizationOwnerAddresses.stream().map(this::convert).collect(Collectors.toList());
                response.setOwerList(owerList);
            }
        }
        response.setCommunityType(community.getCommunityType());

        AddressArrangement arrangement = null;
		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(cmd.getId());
		for (AddressArrangement addressArrangement : arrangements) {
			List<String> originalIds = (List<String>)StringHelper.fromJsonString(addressArrangement.getOriginalId(), ArrayList.class);
			if (originalIds.contains(cmd.getId().toString())) {
				arrangement = addressArrangement;
				break;
			}
		}
		if (arrangement != null) {
			response.setArrangementInvolved((byte)1);
			response.setArrangementOperationType(arrangement.getOperationType());
			if (AddressArrangementType.MERGE.getCode() == arrangement.getOperationType()) {
				if (arrangement.getAddressId().equals(cmd.getId())) {
					response.setIsPassiveApartment((byte)0);
				}else {
					response.setIsPassiveApartment((byte)1);
				}
			}
		}

        Contract latestEndDateContract = findLatestEndDateContract(cmd.getId());
        if (latestEndDateContract!=null) {
        	response.setRelatedContractEndDate(latestEndDateContract.getContractEndDate().getTime());
		}
        //关联的预定计划
        if (propertyMgrProvider.isInvolvedWithReservation(cmd.getId())) {
        	response.setReservationInvolved((byte)1);
		}else {
			response.setReservationInvolved((byte)0);
		}
        //关联的企业客户
        List<EnterpriseCustomer> customers = getApartmentRelatedEnterpriseCustomers(cmd.getId());
        if(customers != null && customers.size() > 0) {
        	response.setEnterpriseCustomerInvolved((byte)1);
        } else {
			response.setEnterpriseCustomerInvolved((byte)0);
		}
        //关联的个人客户
        List<OrganizationOwnerDTO> individualCustomerList = getApartmentRelatedIndividualCustomers(address.getNamespaceId(), address.getId());
        if (individualCustomerList!=null && individualCustomerList.size()>0) {
        	response.setIndividualCustomerInvolved((byte)1);
		}else {
			response.setIndividualCustomerInvolved((byte)0);
		}
        //设置所在楼宇的楼层数
        Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
        response.setBuildingFloorNumber(building.getFloorNumber());
        response.setBuildingId(building.getId());//房源的授权价
        AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesByApartmentId(community, building.getId(), address.getId());
        if (addressProperties != null && addressProperties.getApartmentAuthorizeType() != null && addressProperties.getAuthorizePrice() != null) {
        	String chargingItemName = PaymentChargingItemType.fromCode(addressProperties.getChargingItemsId()).getDesc();
    		String apartmentAuthorizeType = AuthorizePriceType.fromCode(addressProperties.getApartmentAuthorizeType()).getDesc(); //周期
    		response.setApartAuthorizePrice(apartmentAuthorizeType+chargingItemName+addressProperties.getAuthorizePrice());
		}
        return response;
    }

    private List<OrganizationOwnerDTO> getApartmentRelatedIndividualCustomers(Integer namespaceId, Long addressId) {
    	List<OrganizationOwnerDTO> individualCustomerList = new ArrayList<>();
    	List<OrganizationOwnerAddress> organizationOwnerAddressesMappings = propertyMgrProvider.listOrganizationOwnerAddressByAddressId(namespaceId, addressId);
	    if (organizationOwnerAddressesMappings != null && organizationOwnerAddressesMappings.size()>0) {
	    	for (OrganizationOwnerAddress organizationOwnerAddress : organizationOwnerAddressesMappings) {
	    		OrganizationOwner organizationOwner = propertyMgrProvider.findOrganizationOwnerById(organizationOwnerAddress.getOrganizationOwnerId());
	            if (organizationOwner != null) {
	            	OrganizationOwnerDTO organizationOwnerDTO = new OrganizationOwnerDTO();
	                organizationOwnerDTO.setContactName(organizationOwner.getContactName());
	                organizationOwnerDTO.setContactToken(organizationOwner.getContactToken());
	                individualCustomerList.add(organizationOwnerDTO);
	            }
			}
	    }
	    return individualCustomerList;
	}

	//  在门牌管理页面中新增企业客户信息tab页，该门牌关联的企业信息来源于3处，详见!新增企业客户信息tab.png!：
	//  a) 企业客户基本信息中关联了楼栋门牌的企业
	//  b) 入驻信息tab页中关联了楼栋门牌的企业
	//  c) 合同中关联了楼栋门牌的企业；
    private List<EnterpriseCustomer> getApartmentRelatedEnterpriseCustomers(Long addressId) {
    	Set<Long> customerIds = new HashSet<>();
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

        List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByAddress(addressId);
        if(mappings != null && mappings.size() > 0) {
            mappings.forEach(mapping -> {
                Contract contract = contractProvider.findContractById(mapping.getContractId());
                if (contract != null) {
                	if(ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))
                            && now.after(contract.getContractStartDate()) && now.before(contract.getContractEndDate())
                            && CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                        customerIds.add(contract.getCustomerId());
                    }
				}
            });
        }
        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.findActiveCustomerEntryInfoByAddressId(addressId);
        if(entryInfos != null && entryInfos.size() > 0) {
            entryInfos.forEach(entryInfo -> {
                if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(entryInfo.getCustomerType()))) {
                    customerIds.add(entryInfo.getCustomerId());
                }
            });
        }
        if(customerIds != null && customerIds.size() > 0) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomers(customerIds);
            return customers;
        }
		return null;
	}

	//寻找与房源关联的有效合同中，结束日期最晚的合同
    private Contract findLatestEndDateContract(Long addressId){
    	Contract latestEndDateContract = null;
        List<Contract> contracts = contractProvider.listContractsByAddressId(addressId);
        if (contracts!=null && contracts.size()>0) {
        	Iterator<Contract> iterator = contracts.iterator();
            while (iterator.hasNext()) {
    			Contract contract = (Contract) iterator.next();
    			if (ContractStatus.INACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))
            			|| ContractStatus.APPROVE_NOT_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))
            			|| ContractStatus.EXPIRED.equals(ContractStatus.fromStatus(contract.getStatus()))
            			|| ContractStatus.HISTORY.equals(ContractStatus.fromStatus(contract.getStatus()))
            			|| ContractStatus.INVALID.equals(ContractStatus.fromStatus(contract.getStatus()))
            			|| ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))) {
            		iterator.remove();
    			}
            }
		}
        if (contracts!=null && contracts.size()>0){
        	//合同按结束日期降序排列
            contracts.sort(new Comparator<Contract>() {
    			@Override
    			public int compare(Contract arg0, Contract arg1) {
    				if (arg0.getContractEndDate().after(arg1.getContractEndDate())) {
    					return -1;
    				}else if (arg0.getContractEndDate().before(arg1.getContractEndDate())) {
    					return 1;
    				}else {
    					return 0;
    				}
    			}
    		});
            latestEndDateContract = contracts.get(0);
        }
        return latestEndDateContract;
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
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_APARTMENT, cmd.getOrganizationId(), cmd.getCommunityId());
    	
        Address address = addressProvider.findAddressById(cmd.getId());
        if (address == null || AddressAdminStatus.fromCode(address.getStatus()) != AddressAdminStatus.ACTIVE) {
        	throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ADDRESS_NOT_EXIST, "address not exist");
        }
        List<Contract> contracts = contractProvider.listContractByAddressId(address.getId());
        if (contracts != null && contracts.size() > 0) {
            contracts.forEach(contract -> {
                if (contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
                        || contract.getStatus() == ContractStatus.WAITING_FOR_APPROVAL.getCode() || contract.getStatus() == ContractStatus.APPROVE_QUALITIED.getCode()
                        || contract.getStatus() == ContractStatus.EXPIRING.getCode() || contract.getStatus() == ContractStatus.DRAFT.getCode()) {
                    LOGGER.error("the address has attach to contract. address id: {}", cmd.getId());
                    throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_HAS_CONTRACT,
                            "the address has attach to contract");
                }
            });
        }
        //删除房源时，需要判断该房源关联的未来房源是否有合同，如果有，则不能删除该房源
        AddressArrangement arrangement = null;
		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(cmd.getId());
		for (AddressArrangement addressArrangement : arrangements) {
			List<String> originalIds = (List<String>)StringHelper.fromJsonString(addressArrangement.getOriginalId(), ArrayList.class);
			if (originalIds.contains(cmd.getId().toString())) {
				arrangement = addressArrangement;
				break;
			}
		}
		if (arrangement != null) {
			List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
			List<Contract> contractsRelatedWithFutureApartment = new ArrayList<>();
			for (String id : targetIds) {
				contractsRelatedWithFutureApartment.addAll(contractProvider.listContractByAddressId(Long.parseLong(id)));
			}
			if (contractsRelatedWithFutureApartment != null && contractsRelatedWithFutureApartment.size() > 0) {
				contractsRelatedWithFutureApartment.forEach(contract -> {
	                if (contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
	                        || contract.getStatus() == ContractStatus.WAITING_FOR_APPROVAL.getCode() || contract.getStatus() == ContractStatus.APPROVE_QUALITIED.getCode()
	                        || contract.getStatus() == ContractStatus.EXPIRING.getCode() || contract.getStatus() == ContractStatus.DRAFT.getCode()) {
	                    LOGGER.error("the address has attach to contract. address id: {}", cmd.getId());
	                    throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_FUTURE_ADDRESS_HAS_CONTRACT,
	                            "the related future address has attach to contract");
	                }
	            });
	        }
			//删除房源时，要删除关联的拆分合并计划产生的未来房源
			for (String id : targetIds) {
				Address futureAddress = addressProvider.findAddressById(Long.parseLong(id));
				futureAddress.setStatus(AddressAdminStatus.INACTIVE.getCode());
				addressProvider.updateAddress(futureAddress);
			}
			//删除房源时，要删除关联的拆分合并计划
			addressProvider.deleteAddressArrangement(arrangement.getId());
		}

        address.setStatus(AddressAdminStatus.INACTIVE.getCode());
        address.setDeleteTime(getCurrentTimestamp());
        address.setOperateTime(address.getDeleteTime());
        addressProvider.updateAddress(address);
        //添加日志，同时将之前与该房源的日志都记为失效状态
        saveAddressEvent(AddressTrackingTemplateCode.ADDRESS_DELETE,address,null,null,null);
        addressProvider.deleteAddressEventByAddressId(address.getId());
        //删除房源的状态记录
        addressProvider.updateOrganizationAddressMapping(address.getId());
        //EH_ORGANIZATION_ADDRESSES这个表不知道是干啥的
        addressProvider.updateOrganizationAddress(address.getId());
        //删除房源与个人客户的关联关系
        //addressProvider.updateOrganizationOwnerAddress(address.getId());
        //删除房源与企业客户的关联关系
        //enterpriseCustomerProvider.deleteCustomerEntryInfoByAddessId(cmd.getId());

        //门牌对应的楼栋和园区的sharedArea chargeArea buildArea rentArea都要减去相应的值 by xiongying 20170815
        Community community = checkCommunity(address.getCommunityId());
        Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
        if (address.getAreaSize() != null) {
            Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
            building.setAreaSize(buildingAreaSize - address.getAreaSize());

            Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
            community.setAreaSize(communityAreaSize - address.getAreaSize());
        }
        if (address.getRentArea() != null) {
            Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
            building.setRentArea(buildingRentArea - address.getRentArea());

            Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
            community.setRentArea(communityRentArea - address.getRentArea());
        }
        if (address.getSharedArea() != null) {
            Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
            building.setSharedArea(buildingSharedArea - address.getSharedArea());

            Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
            community.setSharedArea(communitySharedArea - address.getSharedArea());
        }
//        if (address.getBuildArea() != null) {
//            Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
//            building.setBuildArea(buildingBuildArea - address.getBuildArea());
//
//            Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
//            community.setBuildArea(communityBuildArea - address.getBuildArea());
//        }
        if (address.getChargeArea() != null) {
            Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
            building.setChargeArea(buildingChargeArea - address.getChargeArea());

            Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
            community.setChargeArea(communityChargeArea - address.getChargeArea());
        }
        if (address.getFreeArea() != null) {
            Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
            building.setFreeArea(buildingFreeArea - address.getFreeArea());

            Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
            community.setFreeArea(communityFreeArea - address.getFreeArea());
        }
        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
		//删除门牌
		//// TODO: 2018/5/11 
        //删除门牌应该以置状态的方式，而不能直接去删除数据库里的记录，不然会对房源招商，租户管理，个人客户管理等模块的业务产生影响，by 唐岑
		//addressProvider.deleteAddressById(cmd.getId());
    }

    private void insertOrganizationAddressMapping(Long organizationId, Community community, Address address, Byte livingStatus) {
        if (organizationId != null && community != null && address != null) {
        	//针对标准版的兼容，因为在标准版中一个community可能被不同的organization管理，但同一时刻只能是一个organization管理。
            //这里再以organizationId为条件来查可能就查不出来了。而且目前业务中，addressId和livingStatus应该是一一对应的。
            //CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
            CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMappingByAddressId(address.getId());
        	if (communityAddressMapping == null) {
                communityAddressMapping = new CommunityAddressMapping();
                //communityAddressMapping.setOrganizationId(organizationId);
                communityAddressMapping.setCommunityId(community.getId());
                communityAddressMapping.setAddressId(address.getId());
                communityAddressMapping.setOrganizationAddress(address.getAddress());
                communityAddressMapping.setLivingStatus(livingStatus);
                communityAddressMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                communityAddressMapping.setUpdateTime(communityAddressMapping.getCreateTime());
                organizationProvider.createOrganizationAddressMapping(communityAddressMapping);
            } else {
                communityAddressMapping.setLivingStatus(livingStatus);
                communityAddressMapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                organizationProvider.updateOrganizationAddressMapping(communityAddressMapping);
            }
        }
    }

    @Override
    public ListApartmentsResponse listApartments(ListApartmentsCommand cmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getPageAnchor() != null) {
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
        if (aptList != null && aptList.size() > 0) {
            if (aptList.size() > pageSize) {
                aptList.remove(aptList.size() - 1);
                response.setNextPageAnchor(aptList.get(aptList.size() - 1).getId());
            }
            //门牌转化成门牌id列表
            List<Long> aptIdList = aptList.stream().map(a -> a.getId()).collect(Collectors.toList());
            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
            //处理小区地址关联表
            Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList);
            LOGGER.info("listApartments communityAddressMappingMap: {}", communityAddressMappingMap);
            aptList.forEach(apt -> {
                if (apt.getLivingStatus() == null) {
                    CommunityAddressMapping mapping = communityAddressMappingMap.get(apt.getId());
                    if (mapping != null) {
                        apt.setLivingStatus(mapping.getLivingStatus());
                    } else {
                        apt.setLivingStatus(AddressMappingStatus.LIVING.getCode());
                    }
                }
                if(community != null) {
                    apt.setCommunityName(community.getName());
                    apt.setCommunityId(community.getId());
                }
                apartments.add(apt);
            });
            //设置每个房源关联的拆分合并计划的生效时间
            for (ApartmentAbstractDTO apartment: apartments) {
            	if (apartment.getIsFutureApartment()==0) {
            		//不是未来房源
            		AddressArrangement addressArrangement = null;
            		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(apartment.getId());
            		for (AddressArrangement arrangement : arrangements) {
            			List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
            			if (originalIds.contains(apartment.getId().toString())) {
            				addressArrangement = arrangement;
            				break;
						}
            		}
            		if (addressArrangement != null) {
            			apartment.setRelatedArrangementBeginDate(addressArrangement.getDateBegin().getTime());
					}
				}else if (apartment.getIsFutureApartment()==1) {
					//是未来房源
            		AddressArrangement addressArrangement = null;
            		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByTargetIdV2(apartment.getId());
            		for (AddressArrangement arrangement : arrangements) {
            			List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
            			if (targetIds.contains(apartment.getId().toString())) {
            				addressArrangement = arrangement;
            				break;
						}
            		}
					if (addressArrangement != null) {
            			apartment.setRelatedArrangementBeginDate(addressArrangement.getDateBegin().getTime());
					}
				}
			}
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
		Map<Long, Integer> ownerCountMap = propertyMgrProvider.mapOrganizationOwnerCountByAddressIds(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), aptIdList);

		//处理小区地址关联表
		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList);
//		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList, cmd.getLivingStatus());

		//判断是否欠费
		Map<Long, Byte> billOwedMap = mapBillOwedFlag(aptIdList);

		//门牌转化成要返回的列表
		List<PropFamilyDTO> resultList = aptList.stream().map(apartmentDTO->convertToPropFamilyDTO(cmd, apartmentDTO, familyMap, ownerCountMap,
				communityAddressMappingMap, billOwedMap)).collect(Collectors.toList());

		PropAptStatisticDTO statistics = getStatistics(resultList);

		ListPropApartmentsResponse response = ConvertHelper.convert(statistics, ListPropApartmentsResponse.class);
		List<PropFamilyDTO>  filterData = new ArrayList<>();
		for(PropFamilyDTO dto : resultList){
			filterData.add(dto);
			//按状态筛选
			if(cmd.getLivingStatus() != null && dto.getLivingStatus() != cmd.getLivingStatus()){
				filterData.remove(dto);
                continue;
			}
			//按楼层筛选
			if (!StringUtils.isEmpty(cmd.getApartmentFloor())){
				if (dto.getApartmentFloor()==null || !dto.getApartmentFloor().equals(cmd.getApartmentFloor())) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按建筑面积筛选
			if (cmd.getAreaSizeFrom() != null) {
				if (dto.getAreaSize()==null || dto.getAreaSize().doubleValue() < cmd.getAreaSizeFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getAreaSizeTo() != null) {
				if (dto.getAreaSize()==null || dto.getAreaSize().doubleValue() > cmd.getAreaSizeTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按在租面积筛选
			if (cmd.getRentAreaFrom() != null) {
				if (dto.getRentArea()==null || dto.getRentArea().doubleValue() < cmd.getRentAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getRentAreaTo() != null) {
				if (dto.getRentArea()==null || dto.getRentArea().doubleValue() > cmd.getRentAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按收费面积筛选
			if (cmd.getChargeAreaFrom() != null) {
				if (dto.getChargeArea()==null || dto.getChargeArea().doubleValue() < cmd.getChargeAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getChargeAreaTo() != null ) {
				if (dto.getChargeArea()==null || dto.getChargeArea().doubleValue() > cmd.getChargeAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按可招租面积筛选
			if (cmd.getFreeAreaFrom() != null) {
				if (dto.getFreeArea()==null || dto.getFreeArea().doubleValue() < cmd.getFreeAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getFreeAreaTo() != null) {
				if (dto.getFreeArea()==null || dto.getFreeArea().doubleValue() > cmd.getFreeAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}

			//房源预定相关
			dto.setReservationInvolved((byte)0);
			if(AddressLivingStatus.OCCUPIED.getCode() == dto.getLivingStatus()){
				List<PmResourceReservation> reservations = propertyMgrProvider.findReservationByAddress(dto.getAddressId(), ReservationStatus.ACTIVE);
				if (reservations != null && reservations.size() > 0) {
					dto.setReservationInvolved((byte)1);
					dto.setReservationId(reservations.get(0).getId());
				}
			}

			//房源拆分合并相关
			dto.setArrangementInvolved((byte)0);
			AddressArrangement arrangement = null;
			List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(dto.getAddressId());
			for (AddressArrangement addressArrangement : arrangements) {
				List<String> originalIds = (List<String>)StringHelper.fromJsonString(addressArrangement.getOriginalId(), ArrayList.class);
				if (originalIds.contains(dto.getAddressId().toString())) {
					arrangement = addressArrangement;
					break;
				}
			}
			if (arrangement != null) {
				dto.setArrangementInvolved((byte)1);
			}
		}
		response.setResultList(filterData);

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
		statistics.setAptCount(statistics.getDefaultCount() + statistics.getLiveCount() + statistics.getRentCount() + statistics.getFreeCount() + statistics.getSaledCount() + statistics.getUnsaleCount() + statistics.getOccupiedCount());
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
		dto.setRentArea(apartmentDTO.getRentArea());
		dto.setFreeArea(apartmentDTO.getFreeArea());
		dto.setChargeArea(apartmentDTO.getChargeArea());
		dto.setEnterpriseName(apartmentDTO.getEnterpriseName());
		dto.setApartmentFloor(apartmentDTO.getApartmentFloor());

		//设置家庭信息
		setFamilyInfo(dto, familyMap);
		//设置门牌入住的人数
		setOwnerCount(dto, ownerCountMap);
		//设置门牌的入住状态
		setLivingStatus(dto, communityAddressMappingMap, apartmentDTO.getLivingStatus());
		//设置公寓是否欠费
		setOwedFlag(dto, billOwedMap);
//设置与该房源关联的有效合同中，结束日期最晚的合同的结束日期
		Contract latestEndDateContract = findLatestEndDateContract(addressId);
		if (latestEndDateContract!=null) {
			dto.setRelatedContractEndDate(latestEndDateContract.getContractEndDate().getTime());
		}
		/*
		* 因为通过addressService.listApartmentsByKeyword(cmd).second()方法获得的address，其IS_FUTURE_APARTMENT都为0，
		* 所以如下代码可以注释掉，只留下else if分支的代码
		*/
		//设置该房源是否为未来房源，及与其关联的房源拆分合并计划的开始时间
//		Address address = addressProvider.findAddressById(addressId);
//		dto.setIsFutureApartment(address.getIsFutureApartment());
//		if (address.getIsFutureApartment() == 1) {
//			AddressArrangement addressArrangement = null;
//			List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByTargetIdV2(addressId);
//			for (AddressArrangement arrangement : arrangements) {
//    			List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
//    			if (targetIds.contains(addressId.toString())) {
//    				addressArrangement = arrangement;
//    				break;
//				}
//    		}
//			if(addressArrangement!=null){
//				dto.setRelatedAddressArrangementBeginDate(addressArrangement.getDateBegin().getTime());
//			}
//		}else if (address.getIsFutureApartment() == 0) {
//			AddressArrangement addressArrangement = null;
//			List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(addressId);
//			for (AddressArrangement arrangement : arrangements) {
//    			List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
//    			if (originalIds.contains(addressId.toString())) {
//    				addressArrangement = arrangement;
//    				break;
//				}
//    		}
//			if(addressArrangement!=null){
//				dto.setRelatedAddressArrangementBeginDate(addressArrangement.getDateBegin().getTime());
//			}
//		}
		dto.setIsFutureApartment((byte)0);
		AddressArrangement addressArrangement = null;
		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(addressId);
		for (AddressArrangement arrangement : arrangements) {
			List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
			if (originalIds.contains(addressId.toString())) {
				addressArrangement = arrangement;
				break;
			}
		}
		if(addressArrangement!=null){
			dto.setRelatedAddressArrangementBeginDate(addressArrangement.getDateBegin().getTime());
		}
		return dto;
	}

	private void checkListPropApartmentByKeywordParamters(ListPropApartmentsByKeywordCommand cmd) {
		if(null == cmd.getCommunityId()){
			checkOrganization(cmd.getOrganizationId());

			OrganizationCommunity  orgCom = this.propertyMgrProvider.findPmCommunityByOrgId(cmd.getOrganizationId());
			if(orgCom == null){
				LOGGER.error("Unable to find the community by organizationId="+cmd.getOrganizationId());
				throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_NOT_EXIST,
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
        if (files == null) {
            LOGGER.error("files is null");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_FILE,
                    "files is null");
        }
        this.checkOrganizationIdIsNull(orgId);
        this.checkOrganization(orgId);

        String rootPath = System.getProperty("user.dir");
        String filePath1 = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        String filePath2 = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        String jarPath = this.getJarPath();
        String command = this.setExecCommand(jarPath, orgId, filePath1, filePath2);

        try {
            //将原文件暂存在服务器中
            this.storeFile(files[0], filePath1);
            //启动进程解析原文件
            Process process = Runtime.getRuntime().exec(command);
            int i = 1;
            while (process.isAlive()) {
                if (LOGGER.isDebugEnabled()) {
                    System.out.println("isAliveTime=" + i * 1000);
                    LOGGER.info("isAliveTime=" + i * 1000);
                }
                Thread.sleep(i * 1000);
                i++;
                if (i > 10) break;
            }
            File file2 = new File(filePath2);
            if (file2 == null || !file2.exists()) {
                LOGGER.error("parse file failure.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_PARSE_FILE,
                        "parse file failure.");
            }
            List<CommunityPmBill> bills = this.convertExcelFileToPmBills(file2);
            createPmBills(bills, orgId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_PARSE_FILE,
                    e.getMessage());
        } finally {
            File file = new File(filePath1);
            File file2 = new File(filePath2);
            if (file.exists()) file.delete();
            if (file2.exists()) file2.delete();
        }
    }

    /**
     * 将数据插入数据库
     */
    public void createPmBills(List<CommunityPmBill> bills, Long orgId) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        User user = UserContext.current().getUser();
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

        List<CommunityAddressMapping> mappingList = propertyMgrProvider.listAddressMappingsByOrgId(orgId);

        if (bills != null && bills.size() > 0) {
            this.dbProvider.execute(s -> {
                for (CommunityPmBill bill : bills) {
                    if (mappingList != null && mappingList.size() > 0) {
                        CommunityAddressMapping mapping = null;
                        for (CommunityAddressMapping m : mappingList) {
                            if (bill != null && bill.getAddress().equals(m.getOrganizationAddress())) {
                                mapping = m;
                                break;
                            }
                        }
                        if (mapping == null) {
                            LOGGER.error(bill.getAddress() + " not find in address mapping.");
                            continue;
							/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									bill.getAddress() + " not find in address mapping.");*/
                        }

                        CommunityPmBill existedBill = this.propertyMgrProvider.findPmBillByAddressIdAndTime(mapping.getAddressId(), bill.getStartDate(), bill.getEndDate());
                        if (existedBill != null) {
                            LOGGER.error("the bill is exist.please don't import repeat data.address=" + bill.getAddress() + ",startDate=" + format.format(bill.getStartDate()) + ",endDate=" + format.format(bill.getEndDate()));
                            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_REPEATED_BILL,
                                    "the bill is exist.please don't import repeat data.address=" + bill.getAddress() + ",startDate=" + format.format(bill.getStartDate()) + ",endDate=" + format.format(bill.getEndDate()));
                        }

                        bill.setOrganizationId(orgId);
                        bill.setEntityId(mapping.getAddressId());
                        bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
                        bill.setNotifyCount(0);
                        bill.setNotifyTime(null);

                        cal.setTimeInMillis(bill.getStartDate().getTime());
                        StringBuilder builder = new StringBuilder();
                        builder.append(cal.get(Calendar.YEAR) + "-");
                        if (cal.get(Calendar.MONTH) < 9)
                            builder.append("0" + (cal.get(Calendar.MONTH) + 1));
                        else
                            builder.append(cal.get(Calendar.MONTH) + 1);
                        bill.setDateStr(builder.toString());
                        bill.setName(bill.getDateStr() + "月账单");
                        bill.setCreatorUid(user.getId());
                        bill.setCreateTime(timeStamp);
                        //往期欠款处理
                        if (bill.getOweAmount() == null) {
                            CommunityPmBill beforeBill = this.propertyMgrProvider.findNewestBillByAddressId(mapping.getAddressId());
                            if (beforeBill != null) {
                                //payAmount为负
                                BigDecimal payedAmount = this.countPmBillPaidAmount(beforeBill.getId());
                                BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(payedAmount);
                                bill.setOweAmount(oweAmount);
                            } else
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

        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1L);
        ListPmBillsByConditionsCommandResponse result = new ListPmBillsByConditionsCommandResponse();
        List<PmBillsDTO> billList = new ArrayList<PmBillsDTO>();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        if (cmd.getBillDate() != null && !cmd.getBillDate().equals("")) {
            startDate = this.getFirstDayOfMonthByStr(cmd.getBillDate());
            endDate = this.getLastDayOfMonthByStr(cmd.getBillDate());
        } else {//没有查询条件返回最新月份的账单
            if (cmd.getAddress() == null || cmd.getAddress().isEmpty()) {
                List<CommunityPmBill> tempBills = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(), null);
                if (tempBills != null && !tempBills.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                    String billDate = format.format(tempBills.get(0).getStartDate());
                    startDate = this.getFirstDayOfMonthByStr(billDate);
                    endDate = this.getLastDayOfMonthByStr(billDate);
                }
            }
        }

        List<CommunityPmBill> comBillList = this.propertyMgrProvider.listPmBillsByOrgId(organization.getId(), cmd.getAddress(), startDate, endDate, offset, pageSize + 1);
        if (comBillList != null && !comBillList.isEmpty()) {
            if (comBillList.size() == pageSize + 1) {
                comBillList.remove(comBillList.size() - 1);
                result.setNextPageOffset(cmd.getPageOffset() + 1);
            }

            for (CommunityPmBill comBill : comBillList) {
                PmBillsDTO billDto = ConvertHelper.convert(comBill, PmBillsDTO.class);
                this.convertBillDateToDto(comBill, billDto);
                BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
                this.setPmBillAmounts(billDto, payedAmount);
                billList.add(billDto);
            }
        }
        result.setRequests(billList);
        return result;
    }

    private BigDecimal countPmBillPaidAmount(Long billId) {
        List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billId, OrganizationOrderStatus.PAID.getCode());
        BigDecimal paidAmount = BigDecimal.ZERO;
        if (orders != null && !orders.isEmpty()) {
            for (OrganizationOrder order : orders) {
                paidAmount = paidAmount.add(order.getAmount());
            }
        }
        return paidAmount;
    }

    private String setExecCommand(String jarPath, Long orgId, String filePath1, String filePath2) {
        StringBuilder builder = new StringBuilder();
        //builder.append("java -jar D:/dev_documents/workspace2/ehparser/target/ehparser-0.0.1-SNAPSHOT.jar");
        builder.append("java -jar" + " " + jarPath);
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
        String jarPath = rootPath + File.separator + "ehparser-0.0.1-SNAPSHOT.jar";

        File dir = new File(rootPath);
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    String name = file.getName();
                    if (LOGGER.isDebugEnabled())
                        LOGGER.error("jarFileName=" + name);

                    if (name.startsWith("ehparser") && name.endsWith(".jar")) {
                        jarPath = rootPath + File.separator + file.getName();
                        break;
                    }
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.error("jarPath=" + jarPath);
        return jarPath;
    }

    private void storeFile(MultipartFile file, String filePath1) throws Exception {
        OutputStream out = new FileOutputStream(new File(filePath1));
        InputStream in = file.getInputStream();
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
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
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
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
                if (oweAmountCell != null) {
                    BigDecimal oweAmount = new BigDecimal(oweAmountCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    bill.setOweAmount(oweAmount);
                }
                //description column
                Cell descriptionCell = row.getCell(6);
                if (descriptionCell != null)
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

    private void processCommunityPmOwner(Long communityId, List<CommunityPmOwner> owners, String message, String messageBodyType, User user) {
        User operator = user;
        Integer namespaceId = operator.getNamespaceId();

        List<String> phones = new ArrayList<String>();
        List<Long> userIds = new ArrayList<Long>();
        if (owners != null && owners.size() > 0) {
            for (CommunityPmOwner communityPmOwner : owners) {
                User userPhone = userService.findUserByIndentifier(namespaceId, communityPmOwner.getContactToken());

                if (userPhone == null) {// 3-不是user，发短信
                    phones.add(communityPmOwner.getContactToken());
                } else {//是用户，未加入或创建家庭,给用户发个人消息
                    Family family = familyProvider.findFamilyByAddressId(communityPmOwner.getAddressId());
                    if (family != null) {
                        GroupMember member = groupProvider.findGroupMemberByMemberInfo(family.getId(), GroupDiscriminator.FAMILY.getCode(), userPhone.getId());
                        if (member == null) {// 2- 是user，还未加入家庭，发个人信息 + 提醒配置项【可以加入家庭】。
                            userIds.add(userPhone.getId());
                        }
                        //1- 是user，已加入家庭，发家庭消息已包含该user。 已经发过family。
                    } else {//4：是user，家庭不存在，发个人信息 + 提醒配置项【可以创建家庭】。
                        userIds.add(userPhone.getId());
                    }
                }
            }
        }

        //是user，还未加入家庭，发个人信息.
        if (userIds != null && userIds.size() > 0) {
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
        smsProvider.sendSms(namespaceId, phoneArray, templateScope, templateId, templateLocale, variables);
        //		if(phones != null && phones.size() > 0 ){
        //			for (String phone : phones) {
        //				smsProvider.sendSms(phone, message);
        //			}
        //		}

    }

    private void createList(Long organizationId, String taskType, List<Integer> todayList, long startTime, long endTime) {
        int todayCount = propertyMgrProvider.countCommunityPmTasks(organizationId, taskType, null, String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
        todayList.add(todayCount);
        int num = OrganizationTaskStatus.OTHER.getCode();
        for (int i = 1; i <= num; i++) {
            int count = propertyMgrProvider.countCommunityPmTasks(organizationId, taskType, (byte) i, String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
            todayList.add(count);
        }
    }

    private List<Integer> getTaskCounts(Long organizationId, Long communityId, String taskType, long startTime, long endTime) {
        List<Integer> counts = new ArrayList<Integer>();
        int num = OrganizationTaskStatus.OTHER.getCode();
        List<OrganizationTask> tasks = propertyMgrProvider.communityPmTaskLists(organizationId, communityId, taskType, null, String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
        if (null != communityId) {
            counts.add(this.getPostByCommunityId(communityId, tasks).size());
        } else {
            counts.add(tasks.size());
        }

        for (int i = 1; i <= num; i++) {
            tasks = propertyMgrProvider.communityPmTaskLists(organizationId, communityId, taskType, (byte) i, String.format("%tF %<tT", startTime), String.format("%tF %<tT", endTime));
            if (null != communityId) {
                counts.add(this.getPostByCommunityId(communityId, tasks).size());
            } else {
                counts.add(tasks.size());
            }
        }

        return counts;
    }

    private List<Post> getPostByCommunityId(Long communityId, List<OrganizationTask> tasks) {
        Community community = communityProvider.findCommunityById(communityId);
        List<Post> posts = new ArrayList<Post>();
        for (OrganizationTask task : tasks) {
            Post post = forumProvider.findPostById(task.getApplyEntityId());
            if (null != post && post.getForumId().equals(community.getDefaultForumId())) {
                posts.add(post);
            }
        }
        return posts;
    }

    private void checkOrganizationIdIsNull(Long organizationId) {
        if (organizationId == null) {
            LOGGER.error("propterty organizationId paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "propterty organizationId paramter can not be null or empty");
        }
    }

    private Organization checkOrganization(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId=" + orgId);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_NOT_EXIST,
                    "Unable to find the organization.");
        }
        return org;
    }

    private void convertBillDateToDto(CommunityPmBill comBill, PmBillsDTO billDto) {
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
        try {
            List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByStatus(OrganizationOrderStatus.WAITING_FOR_PAY.getCode());
            if (orders != null && !orders.isEmpty()) {
                for (OrganizationOrder order : orders) {
                    String orderNo = this.convertOrderIdToOrderNo(order.getId());
                    LOGGER.error("remoteUpdate:orderNo=" + orderNo);
                    //this.remoteUpdateOrgOrderByOrderNo(orderNo);
                }
            }
        } catch (Exception e) {
            LOGGER.error("remote refresh organization order fail.message=" + e.getMessage());
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
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_PARSE_DATE_FORMAT,
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
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_PARSE_DATE_FORMAT,
                    "date format is wrong.must be yyyy-MM");
        }
    }

    @Override
    public void deletePmBills(DeletePmBillsCommand cmd) {
        if (cmd.getIds() == null || cmd.getIds().isEmpty()) {
            LOGGER.error("ids paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "ids paramter can not be null or empty");
        }
        this.dbProvider.execute(s -> {
            if (cmd.getIds() != null && !cmd.getIds().isEmpty()) {
                for (Long billId : cmd.getIds()) {
                    DeletePmBillCommand command = new DeletePmBillCommand();
                    command.setId(billId);
                    this.deletePmBill(command);
                }
            }
            return true;
        });

    }

    private void deletePmBillTrans(CommunityPmBill bill, String content) {
        this.dbProvider.execute(s -> {
            this.organizationProvider.deleteOrganizationBillById(bill.getId());
            this.createPmOperLog(bill.getId(), PmBillOperLogType.DELETE.getCode(), content);
            return true;
        });
    }

    private void createPmOperLog(Long billId, String type, String content) {
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
        if (cmd.getInsertList() == null || cmd.getInsertList().isEmpty()) {
            LOGGER.error("insertList paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "insertList paramter can not be null or empty");
        }

        if (cmd.getInsertList() != null && !cmd.getInsertList().isEmpty()) {
            for (UpdatePmBillsDto insertBill : cmd.getInsertList()) {
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

    private void insertPmBillTrans(CommunityPmBill bill, String content) {
        this.dbProvider.execute(s -> {
            this.createPropBill(bill);
            this.createPmOperLog(bill.getId(), PmBillOperLogType.INSERT.getCode(), content);
            return true;
        });

    }

    @Override
    public int updatePmBills(UpdatePmBillsCommand cmd) {
        if (cmd.getUpdateList() == null || cmd.getUpdateList().isEmpty()) {
            LOGGER.error("updateList paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "updateList paramter can not be null or empty");
        }

        if (cmd.getUpdateList() != null && !cmd.getUpdateList().isEmpty()) {
            this.dbProvider.execute(s -> {
                for (UpdatePmBillsDto bill : cmd.getUpdateList()) {
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
            this.createPmOperLog(bill.getId(), PmBillOperLogType.UPDATE.getCode(), content);
            return true;
        });
    }

    @Override
    public void deletePmBill(DeletePmBillCommand cmd) {
        CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(cmd.getId());
        if (communBill != null) {
            User user = UserContext.current().getUser();
            String content = "用户" + user.getNickName() + "删除物业账单:" + communBill.getName();
            this.deletePmBillTrans(communBill, content);
        }
    }

    @Override
    public void updatePmBill(UpdatePmBillCommand bill) {
        if (bill.getId() == null || bill.getOrganizationId() == null) {
            LOGGER.error("id or organizationId paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "id or organizationId paramter can not be null or empty");
        }

        CommunityPmBill communBill = this.organizationProvider.findOranizationBillById(bill.getId());
        if (communBill != null) {
            User user = UserContext.current().getUser();
            StringBuilder builder = new StringBuilder();
            builder.append("用户");
            builder.append(user.getNickName());
            builder.append("更新物业账单:");
            builder.append(communBill.getName());
            builder.append(".");
            if (bill.getEndDate() != null) {
                java.sql.Date endDate = new java.sql.Date(bill.getEndDate());
                builder.append("截止日期:");
                builder.append(communBill.getEndDate());
                builder.append("更新为");
                builder.append(endDate);
                builder.append(".");
                communBill.setEndDate(endDate);
            }
            if (bill.getPayDate() != null) {
                java.sql.Date payDate = new java.sql.Date(bill.getPayDate());
                builder.append("还款日期:");
                builder.append(communBill.getPayDate());
                builder.append("更新为");
                builder.append(payDate);
                builder.append(".");
                communBill.setPayDate(payDate);
            }
            if (bill.getStartDate() != null) {
                java.sql.Date startDate = new java.sql.Date(bill.getStartDate());
                builder.append("开始日期:");
                builder.append(communBill.getStartDate());
                builder.append("更新为");
                builder.append(startDate);
                builder.append(".");
                communBill.setStartDate(startDate);
            }
            if (!(bill.getDescription() == null || bill.getDescription().equals(""))) {
                builder.append("描述:");
                builder.append(communBill.getDescription());
                builder.append("更新为");
                builder.append(bill.getDescription());
                builder.append(".");
                communBill.setDescription(bill.getDescription());
            }
            if (bill.getDueAmount() != null) {
                builder.append("本月金额:");
                builder.append(communBill.getDueAmount());
                builder.append("更新为");
                builder.append(bill.getDueAmount());
                builder.append(".");
                communBill.setDueAmount(bill.getDueAmount());
            }
            if (bill.getOweAmount() != null) {
                builder.append("往期欠款:");
                builder.append(communBill.getOweAmount());
                builder.append("更新为");
                builder.append(bill.getOweAmount());
                builder.append(".");
                communBill.setOweAmount(bill.getOweAmount());
            }
            if (bill.getAddress() != null && !bill.getAddress().equals("")) {
                CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(communBill.getOrganizationId(), bill.getAddress());
                if (addressMapping != null) {
                    builder.append("楼栋-门牌号:");
                    builder.append(communBill.getAddress());
                    builder.append("更新为");
                    builder.append(bill.getAddress());
                    builder.append(".");
                    communBill.setAddress(bill.getAddress());
                    communBill.setEntityId(addressMapping.getAddressId());
                } else {
                    LOGGER.error("update bill failure.because address not found..address=" + bill.getAddress());
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "update bill failure.because address not found.address=" + bill.getAddress());
                }
            }
            this.updatePmBillTrans(communBill, builder.toString());
        }

    }

    @Override
    public void insertPmBill(InsertPmBillCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getAddress() == null) {
            LOGGER.error("propterty organizationId or address paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "propterty organizationId or address paramter can not be null or empty");
        }
        Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (organization == null) {
            LOGGER.error("Insert failure.Unable to find the organization.organizationId=" + cmd.getOrganizationId());
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                    "Insert failure.Unable to find the organization.");
        }
        if (cmd.getAddress() == null || cmd.getAddress().equals("") || cmd.getDueAmount() == null || cmd.getEndDate() == null ||
                cmd.getPayDate() == null || cmd.getStartDate() == null) {
            LOGGER.error("address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "address or dueAmount or endDate or payDate or startDate paramter can not be null or empty");
        }

        Calendar cal = Calendar.getInstance();
        User user = UserContext.current().getUser();
        Timestamp timeStamp = new Timestamp(new Date().getTime());

        CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByOrgIdAndAddress(cmd.getOrganizationId(), cmd.getAddress());
        if (addressMapping == null) {
            LOGGER.error("Insert failure.the address not find");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ADDRESS_MAPPING_NOT_EXIST,
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
        builder.append(cal.get(Calendar.YEAR) + "-");
        if (cal.get(Calendar.MONTH) < 9)
            builder.append("0" + (cal.get(Calendar.MONTH) + 1));
        else
            builder.append(cal.get(Calendar.MONTH) + 1);
        bill.setDateStr(builder.toString());
        bill.setName(bill.getDateStr() + "月账单");
        //往期欠款处理
        if (bill.getOweAmount() == null) {
            CommunityPmBill beforeBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
            if (beforeBill != null) {
                BigDecimal paidAmount = this.countPmBillPaidAmount(beforeBill.getId());
                BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).subtract(paidAmount);
                bill.setOweAmount(oweAmount);
            } else
                bill.setOweAmount(BigDecimal.ZERO);
        }
        String content = "用户" + user.getNickName() + "新增物业账单:" + bill.getName();
        this.insertPmBillTrans(bill, content);
    }

    @Override
    public ListOrgBillingTransactionsByConditionsCommandResponse listOrgBillingTransactionsByConditions(ListOrgBillingTransactionsByConditionsCommand cmd) {
        this.checkOrganizationIdIsNull(cmd.getOrganizationId());
        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1L);

        ListOrgBillingTransactionsByConditionsCommandResponse result = new ListOrgBillingTransactionsByConditionsCommandResponse();
        List<OrganizationBillingTransactionDTO> orgbillTxList = new ArrayList<OrganizationBillingTransactionDTO>();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        Timestamp startTime = null;
        Timestamp endTime = null;
        if (cmd.getPayDate() != null && !cmd.getPayDate().equals("")) {
            startTime = new Timestamp(this.getFirstDayOfMonthByStr(cmd.getPayDate()).getTime());
            endTime = new Timestamp(this.getLastDayOfMonthByStr(cmd.getPayDate()).getTime());
        }

        orgbillTxList = this.organizationProvider.listOrgBillTxByOrgId(organization.getId(), BillTransactionResult.SUCCESS.getCode(), startTime, endTime, cmd.getAddress(), offset, pageSize + 1);
        if (orgbillTxList != null && orgbillTxList.size() == pageSize + 1) {
            orgbillTxList.remove(orgbillTxList.size() - 1);
            result.setNextPageOffset(cmd.getPageOffset() + 1);
        }

        if (orgbillTxList != null && !orgbillTxList.isEmpty()) {
            //设置业主电话
            for (OrganizationBillingTransactionDTO orgbillTx : orgbillTxList) {
                List<CommunityPmOwner> orgOwnerList = this.organizationProvider.listOrgOwnerByOrgIdAndAddressId(orgbillTx.getOrganizationId(), orgbillTx.getAddressId());
                if (orgOwnerList != null && !orgOwnerList.isEmpty()) {
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

        List<CommunityPmBill> billList = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(), cmd.getAddress());
        if (billList != null && !billList.isEmpty()) {
            for (CommunityPmBill bill : billList) {
                BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
                BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
                if (waitPayAmount.compareTo(BigDecimal.ZERO) > 0) {//该家庭欠费
                    OweFamilyDTO family = new OweFamilyDTO();
                    family.setAddress(bill.getAddress());
                    family.setAddressId(bill.getEntityId());
                    family.setBillDescription(bill.getDescription());
                    family.setBillId(bill.getId());
                    this.setOweFamilyDTOLastTransactionInfo(family, bill.getId());
                    family.setOweAmount(waitPayAmount);
                    this.setOweFamilyOwnerInfo(family, bill.getEntityId(), bill.getOrganizationId());
                    //判断欠费家庭是否在最后缴费时间查询条件范围内
                    if (cmd.getLastPayDate() != null && !cmd.getLastPayDate().isEmpty()) {
                        if (family.getLastPayTime() != null) {
                            Date startDate = this.getFirstDayOfMonthByStr(cmd.getLastPayDate());
                            Date endDate = this.getLastDayOfMonthByStr(cmd.getLastPayDate());
                            if (family.getLastPayTime().compareTo(startDate.getTime()) >= 0 && family.getLastPayTime().compareTo(endDate.getTime()) <= 0) {
                                familyList.add(family);
                            }
                        }
                    } else
                        familyList.add(family);
                }
            }
        }
        this.sortOweFamilyDTOByPayTimeDesc(familyList);

        response.setRequests(familyList);
        return response;
    }

    private void sortOweFamilyDTOByPayTimeDesc(List<OweFamilyDTO> familyList) {
        if (familyList != null && !familyList.isEmpty()) {
            familyList.sort(new Comparator<OweFamilyDTO>() {
                public int compare(OweFamilyDTO o1, OweFamilyDTO o2) {
                    if (o1.getLastPayTime() == null || o2.getLastPayTime() == null) {
                        if (o2.getLastPayTime() != null)
                            return 1;
                        else if (o1.getLastPayTime() != null)
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
        List<CommunityPmOwner> orgMemberList = this.organizationProvider.listOrganizationOwnerByAddressIdAndOrgId(addressId, organizationId);
        if (orgMemberList != null && !orgMemberList.isEmpty()) {
            family.setOwnerTelephone(orgMemberList.get(0).getContactToken());
        }
    }

    private void setOweFamilyDTOLastTransactionInfo(OweFamilyDTO family, Long billId) {
        List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billId, OrganizationOrderStatus.PAID.getCode());
        if (orders != null && !orders.isEmpty()) {
            OrganizationOrder order = orders.get(0);
            for (int i = 1; i < orders.size(); i++) {
                if (order.getPaidTime().compareTo(orders.get(i).getPaidTime()) < 0)
                    order = orders.get(i);
            }
            family.setLastPayTime(order.getPaidTime().getTime());
        }
    }

    @Override
    public ListBillTxByAddressIdCommandResponse listBillTxByAddressId(ListBillTxByAddressIdCommand cmd) {
        this.checkAddressIdIsNull(cmd.getAddressId());
        this.checkAddress(cmd.getAddressId());
        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1L);

        ListBillTxByAddressIdCommandResponse response = new ListBillTxByAddressIdCommandResponse();
        List<FamilyBillingTransactionDTO> transactionList = new ArrayList<FamilyBillingTransactionDTO>();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

        Long addressId = cmd.getAddressId();

        List<FamilyBillingTransactions> familyTransactionList = this.familyProvider.listFBillTx(BillTransactionResult.SUCCESS.getCode(), addressId, pageSize + 1, offset);
        if (familyTransactionList != null && familyTransactionList.size() == pageSize + 1) {
            response.setNextPageOffset(cmd.getPageOffset() + 1);
            familyTransactionList.remove(familyTransactionList.size() - 1);
        }
        if (familyTransactionList != null && !familyTransactionList.isEmpty()) {
            familyTransactionList.stream().map(r -> {
                FamilyBillingTransactionDTO fBillTxdto = new FamilyBillingTransactionDTO();
                this.convertFBillTxToDto(r, fBillTxdto);
                transactionList.add(fBillTxdto);
                return null;
            }).toArray();
        }

        response.setRequests(transactionList);
        return response;
    }

    private void convertFBillTxToDto(FamilyBillingTransactions r, FamilyBillingTransactionDTO fBillTxdto) {
        fBillTxdto.setBillType(r.getTxType());
        fBillTxdto.setChargeAmount(r.getChargeAmount().negate());
        fBillTxdto.setCreateTime(r.getCreateTime().getTime());
        fBillTxdto.setDescription(r.getDescription());
        fBillTxdto.setId(r.getId());
    }

    private Group checkFamily(Long familyId) {
        Group family = this.groupProvider.findGroupById(familyId);
        if (family == null) {
            LOGGER.error("the family is not exist.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_FAMILY_NOT_EXIST,
                    "the family is not exist");
        }
        return family;
    }

    private void checkFamilyIdIsNull(Long familyId) {
        if (familyId == null) {
            LOGGER.error("familyId paramter is null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "familyId paramter is null or empty");
        }
    }

    private void checkAddressIdIsNull(Long addressId) {
        if (addressId == null) {
            LOGGER.error("addressId paramter is null or empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
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
        if (cmd.getBillDate() != null && !cmd.getBillDate().equals("")) {
            startDate = new java.sql.Date(this.getFirstDayOfMonthByStr(cmd.getBillDate()).getTime());
            endDate = new java.sql.Date(this.getLastDayOfMonthByStr(cmd.getBillDate()).getTime());
        }
        CommunityPmBill communityBill = this.propertyMgrProvider.findPmBillByAddressAndDate(addressId, startDate, endDate);
        if (communityBill != null) {
            billDto = this.convertBillToDto(communityBill);
            BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
            this.setPmBillAmounts(billDto, payedAmount);
        }
        return billDto;
    }

    private void checkBillDate(String billDate) {
        if (billDate == null || billDate.equals("")) {
            LOGGER.error("billDate paramter is empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
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
        if (billDto != null) {
            //账单缴费记录
            this.setBillTx(billDto, cmd.getFamilyId());
        }
        return billDto;
    }

    private PmBillsDTO convertBillToDto(CommunityPmBill communityPmBill) {
        PmBillsDTO billDto = ConvertHelper.convert(communityPmBill, PmBillsDTO.class);
        this.convertBillDateToDto(communityPmBill, billDto);
        return billDto;
    }

    private void setBillTx(PmBillsDTO billDto, Long familyId) {
        List<FamilyBillingTransactionDTO> payDtoList = new ArrayList<FamilyBillingTransactionDTO>();
        BigDecimal payedAmount = BigDecimal.ZERO;
        List<OrganizationOrder> orders = this.organizationProvider.listOrgOrdersByBillIdAndStatus(billDto.getId(), OrganizationOrderStatus.PAID.getCode());
        if (orders != null && !orders.isEmpty()) {
            for (OrganizationOrder r : orders) {
                FamilyBillingTransactions fBillTx = this.familyProvider.findFamilyBillTxByOrderId(r.getId(), familyId);
                if (fBillTx != null) {
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
        if (payDtoList != null && !payDtoList.isEmpty()) {
            payDtoList.sort(new Comparator<FamilyBillingTransactionDTO>() {
                public int compare(FamilyBillingTransactionDTO o1, FamilyBillingTransactionDTO o2) {
                    if (o1.getCreateTime() == null)
                        return -1;
                    if (o2.getCreateTime() == null)
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
        this.setPmInfoToResponse(response, cmd.getCommunityId());

        if (cmd.getFamilyId() == null) {
            LOGGER.error("familyId paramter is null or empty");
        } else {
            //Group family = this.checkFamily(cmd.getFamilyId());
            Group family = this.groupProvider.findGroupById(cmd.getFamilyId());
            if (family == null) {
                LOGGER.error("the family is not exist.");
            } else {
                Long addressId = family.getIntegralTag1();
                //向统一支付发请求,查询订单支付状态
                LOGGER.error("listFamilyBillsAndPaysByFamilyId-remoteUpdate");
                //remoteRefreshOrgOrderStatus();

                if (cmd.getPageOffset() == null)
                    cmd.setPageOffset(1L);
                int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
                long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

                List<CommunityPmBill> commBillList = this.organizationProvider.listOrganizationBillsByAddressId(addressId, offset, pageSize + 1);
                if (commBillList != null && !commBillList.isEmpty()) {
                    if (commBillList.size() == pageSize + 1) {
                        commBillList.remove(commBillList.size() - 1);
                        response.setNextPageOffset(cmd.getPageOffset() + 1);
                    }
                    for (CommunityPmBill commBill : commBillList) {
                        PmBillsDTO billDto = this.convertBillToDto(commBill);
                        //账单缴费记录
                        this.setBillTx(billDto, family.getId());
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
        if (org == null) {
            LOGGER.error("could not found organization by addressId.addressId=" + addressId);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_NOT_EXIST,
                    "could not found organization by addressId.");
        }
        return org;
    }

    private void setZlInfoToResponse(ListFamilyBillsAndPaysByFamilyIdCommandResponse response) {
        response.setZlName("深圳市永佳天成科技发展有限公司");
        response.setZlTelephone("4008384688");
    }

    private void setPmInfoToResponse(ListFamilyBillsAndPaysByFamilyIdCommandResponse response, Long communityId) {
        if (communityId == null)
            return;
        Organization organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, OrganizationType.PM.getCode());
        if (organization != null) {
            response.setOrgIsExist(Byte.valueOf((byte) 1));
            response.setOrgName(organization.getName());
            List<CommunityPmContact> contacts = this.propertyMgrProvider.listCommunityPmContacts(organization.getId());
            if (contacts != null && !contacts.isEmpty()) {
                for (CommunityPmContact contact : contacts)
                    if (contact.getContactType().compareTo(IdentifierType.MOBILE.getCode()) == 0)
                        response.setOrgTelephone(contact.getContactToken());
            }
        } else
            response.setOrgIsExist(Byte.valueOf((byte) 0));
    }

    private BigDecimal countFamilyPmBillDueAndOweAmountInYear(Long orgId, Long addressId) {
        BigDecimal totalDueAmount = this.propertyMgrProvider.countFamilyPmBillDueAmountInYear(orgId, addressId);
        CommunityPmBill bill = this.propertyMgrProvider.findFamilyFirstPmBillInYear(orgId, addressId);
        if (totalDueAmount == null)
            totalDueAmount = BigDecimal.ZERO;
        if (bill != null && bill.getOweAmount() != null)
            totalDueAmount = totalDueAmount.add(bill.getOweAmount());
        return totalDueAmount;

    }

    @Override
    public int payPmBillByAddressId(PayPmBillByAddressIdCommand cmd) {
        if (cmd.getPaidType() == null || cmd.getPayAmount() == null || cmd.getPayTime() == null || cmd.getTxType() == null) {
            LOGGER.error("paidType or payAmount or payTime or txType paramteris empty");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "paidType or payAmount or payTime or txType paramter is empty");
        }
        this.checkAddressIdIsNull(cmd.getAddressId());
        this.checkAddress(cmd.getAddressId());
        Long addressId = cmd.getAddressId();

        this.checkOrganizationByAddressId(addressId);

        CommunityPmBill bill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
        if (bill == null) {
            LOGGER.error("the bill is not exist by addressId=" + addressId);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                    "the bill is not exist");
        }

        User user = UserContext.current().getUser();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());//当前时间
        Timestamp paidTimeStamp = new Timestamp(cmd.getPayTime());//支付时间
        String uuidStr = UUID.randomUUID().toString();//uuid
        StringBuilder builder = new StringBuilder();
        if (cmd.getDescription() != null && !cmd.getDescription().isEmpty())
            builder.append(cmd.getDescription() + " ");
        if (cmd.getTelephone() != null && !cmd.getTelephone().isEmpty())
            builder.append(cmd.getTelephone() + " ");
        if (cmd.getOwnerName() != null && !cmd.getOwnerName().isEmpty())
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

            FamilyBillingAccount fAccount = this.findOrNewFBillAcount(bill.getEntityId(), currentTimestamp);
            OrganizationBillingAccount oAccount = this.findOrNewOBillAccount(bill.getOrganizationId(), currentTimestamp);

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
        if (billList != null && !billList.isEmpty()) {
            for (CommunityPmBill bill : billList) {
                BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
                BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
                if (waitPayAmount.compareTo(BigDecimal.ZERO) > 0) {//该家庭欠费
                    waitPayAmounts = waitPayAmounts.add(waitPayAmount);
                    oweFamilyCount++;
                }
            }
        }
        yearIncomeAmount = this.propertyMgrProvider.countPmYearIncomeByOrganizationId(cmd.getOrganizationId(), BillTransactionResult.SUCCESS.getCode());
        if (yearIncomeAmount == null)
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
        if (bill == null) {
            LOGGER.error("the bill is not exist by addressId=" + addressId);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                    "the bill is not exist");
        }

        BigDecimal payAmount = this.countPmBillPaidAmount(bill.getId());
        BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(payAmount);
        if (waitPayAmount.compareTo(BigDecimal.ZERO) <= 0) {//不欠费
            LOGGER.error("The family don't owed pm fee.Should not send pm pay message.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_SEND_MESSAGE_ALLOWED,
                    "The family don't owed pm fee.Should not send pm pay message.");
        }
        //短信发送物业缴费通知
        this.dbProvider.execute(s -> {
            List<Tuple<String, Object>> variables = this.getPmPayVariables(bill, waitPayAmount, payAmount);
            this.sendPmPayMessageToUnRegisterUserInFamily(org.getId(), addressId, variables);
            bill.setNotifyCount((bill.getNotifyCount() == null ? 0 : bill.getNotifyCount()) + 1);
            bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
            this.organizationProvider.updateOrganizationBill(bill);

            return s;
        });
    }

    private Organization findOrganizationByAddressId(Long addressId) {
        CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByAddressId(addressId);
        if (addressMapping != null) {
            return this.organizationProvider.findOrganizationById(addressMapping.getOrganizationId());
        }
        return null;
    }

    private void sendPmPayMessageToUnRegisterUserInFamily(Long organizationId, Long addressId, List<Tuple<String, Object>> variables) {
        List<OrganizationOwner> orgOwnerList = this.organizationProvider.listOrganizationOwnersByOrgIdAndAddressId(organizationId, addressId);
        if (orgOwnerList != null && !orgOwnerList.isEmpty()) {
            for (OrganizationOwner orgOwner : orgOwnerList) {
                if (orgOwner.getContactToken() != null) {
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

        List<CommunityPmBill> billList = this.propertyMgrProvider.listNewestPmBillsByOrgId(organization.getId(), null);
        if (billList != null && !billList.isEmpty()) {
            for (CommunityPmBill bill : billList) {
                BigDecimal paidAmount = this.countPmBillPaidAmount(bill.getId());
                BigDecimal waitPayAmount = bill.getDueAmount().add(bill.getOweAmount()).subtract(paidAmount);
                if (waitPayAmount.compareTo(BigDecimal.ZERO) > 0) {//欠费
                    Long addressId = bill.getEntityId();
                    //短信发送物业缴费通知
                    this.dbProvider.execute(s -> {
                        List<Tuple<String, Object>> variables = this.getPmPayVariables(bill, waitPayAmount, paidAmount);
                        this.sendPmPayMessageToUnRegisterUserInFamily(organization.getId(), addressId, variables);
                        bill.setNotifyCount((bill.getNotifyCount() == null ? 0 : bill.getNotifyCount()) + 1);
                        bill.setNotifyTime(new Timestamp(System.currentTimeMillis()));
                        this.organizationProvider.updateOrganizationBill(bill);
                        return s;
                    });
                }
            }

        }
    }

    private List<Tuple<String, Object>> getPmPayVariables(CommunityPmBill bill, BigDecimal balance, BigDecimal payAmount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bill.getStartDate());
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_YEAR, String.valueOf(cal.get(Calendar.YEAR)));
        int month = cal.get(Calendar.MONTH) + 1;
        if (month < 10)
            smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_MONTH, "0" + month);
        else
            smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_MONTH, month);
        smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DUEAMOUNT, bill.getDueAmount());
        smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_OWEAMOUNT, bill.getOweAmount());
        smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_PAYAMOUNT, payAmount);
        smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_BALANCE, balance);
        if (bill.getDescription() != null && !bill.getDescription().isEmpty()) {
            if (bill.getDescription().length() > 15)
                smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, bill.getDescription().substring(0, 14) + "...");
            else
                smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, bill.getDescription() + "。");
        } else {
            smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_DESCRIPTION, "");
        }
        return variables;

    }

    private String getPmPayMessage(CommunityPmBill bill, BigDecimal balance, BigDecimal payAmount) {
        //String str = "您2015年07月物业账单为，本月金额：300.00,往期欠款：200.00，本月实付金额：100.00，应付金额：400.00 ，+ 账单说明。 请尽快使用左邻缴纳物业费。";
        StringBuilder builder = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTime(bill.getStartDate());
        builder.append("您" + cal.get(Calendar.YEAR) + "年");
        int month = cal.get(Calendar.MONTH) + 1;
        if (month < 10)
            builder.append("0" + month + "月");
        else
            builder.append(month + "月");
        builder.append("物业账单为，本月金额：");
        builder.append(bill.getDueAmount());
        builder.append("，往期欠款：");
        builder.append(bill.getOweAmount());
        builder.append("，本月实付金额：");
        builder.append(payAmount);
        builder.append("，应付金额：");
        builder.append(balance);
        if (bill.getDescription() != null && !bill.getDescription().isEmpty()) {
            if (bill.getDescription().length() > 15)
                builder.append("，" + bill.getDescription().substring(0, 14) + "...");
            else builder.append("，" + bill.getDescription());

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
        BigDecimal totalDueOweAmount = this.countFamilyPmBillDueAndOweAmountInYear(org.getId(), addressId);
        BigDecimal totalPaidAmount = this.familyProvider.countFamilyBillTxChargeAmountInYear(addressId);
        BigDecimal nowWaitPayAmount = BigDecimal.ZERO;

        CommunityPmBill bill = this.propertyMgrProvider.findNewestBillByAddressId(addressId);
        if (bill != null) {
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
        if (communityBill != null) {
            billDto = this.convertBillToDto(communityBill);
            BigDecimal payedAmount = this.countPmBillPaidAmount(billDto.getId());
            this.setPmBillAmounts(billDto, payedAmount);
        }

        return billDto;
    }

    private Address checkAddress(Long addressId) {
        Address address = this.addressProvider.findAddressById(addressId);
        if (address == null) {
            LOGGER.error("Unable to find the address.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Unable to find the address.");
        }
        return address;

    }

    @Override
    public void onlinePayPmBill(OnlinePayPmBillCommand cmd) {
        //fail
        if (cmd.getPayStatus().toLowerCase().equals("fail"))
            this.onlinePayPmBillFail(cmd);
        //success
        if (cmd.getPayStatus().toLowerCase().equals("success"))
            this.onlinePayPmBillSuccess(cmd);
    }

    private void onlinePayPmBillFail(OnlinePayPmBillCommand cmd) {
        if (LOGGER.isDebugEnabled())
            LOGGER.error("onlinePayPmBillFail");

        this.checkOrderNoIsNull(cmd.getOrderNo());
        Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
        OrganizationOrder order = this.checkOrder(orderId);

        Date cunnentTime = new Date();
        Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
        this.updateOrderStatus(order, 0L, currentTimestamp, OrganizationOrderStatus.INACTIVE.getCode());
    }

    private void onlinePayPmBillSuccess(OnlinePayPmBillCommand cmd) {
        if (LOGGER.isDebugEnabled())
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("before lock.order=" + order.toString());
        }
        if (order.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()) {
            String uuidStr = UUID.randomUUID().toString();
            //pre-set-parameter-familyTx
            FamilyBillingTransactions familyTx = new FamilyBillingTransactions();
            familyTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
            familyTx.setChargeAmount(payAmount.negate());
            familyTx.setCreateTime(payTimeStamp);
            if (cmd.getDescription() != null && !cmd.getDescription().isEmpty())
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
            if (cmd.getDescription() != null && !cmd.getDescription().isEmpty())
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
                LOGGER.error("find pm order in the lock.elapse=" + (lqEndTime - lqStartTime));

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.error("in lock.order=" + order2.toString());
                }

                if (order2.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()) {
                    long luStartTime = System.currentTimeMillis();
                    this.dbProvider.execute(s -> {
                        order2.setAmount(payAmount);
                        this.updateOrderStatus(order2, 0L, payTimeStamp, OrganizationOrderStatus.PAID.getCode());

                        FamilyBillingAccount fAccount = this.findOrNewFBillAcountNoLock(bill.getEntityId(), currentTimestamp);
                        OrganizationBillingAccount oAccount = this.findOrNewOBillAccountNoLock(bill.getOrganizationId(), currentTimestamp);

                        familyTx.setOrderId(order2.getId());
                        familyTx.setOwnerAccountId(fAccount.getId());
                        familyTx.setTargetAccountId(oAccount.getId());
                        this.familyProvider.createFamilyBillingTransaction(familyTx);

                        orgTx.setOrderId(order2.getId());
                        orgTx.setOwnerAccountId(oAccount.getId());
                        orgTx.setTargetAccountId(fAccount.getId());
                        this.organizationProvider.createOrganizationBillingTransaction(orgTx);

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.error("check online pm pay amount.oAccountAmount=" + oAccount.getBalance() + ".payAmount=" + payAmount + ".order=" + order2.toString());
                        }

                        //线上支付,将金额存到物业账号中
                        oAccount.setBalance(oAccount.getBalance().add(payAmount));
                        oAccount.setUpdateTime(currentTimestamp);
                        this.organizationProvider.updateOrganizationBillingAccount(oAccount);

                        return true;
                    });
                    long luEndTime = System.currentTimeMillis();
                    LOGGER.error("update pm order in the lock.elapse=" + (luEndTime - luStartTime));
                }
                return order2;
            });

            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("pm order lock finish.status=" + result.second());
            }
        }
    }

    private OrganizationBillingAccount findOrNewOBillAccountNoLock(Long orgId, Timestamp currentTimestamp) {
        OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(orgId);
        if (oAccount == null) {
            oAccount = new OrganizationBillingAccount();
            oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
            oAccount.setBalance(BigDecimal.ZERO);
            oAccount.setCreateTime(currentTimestamp);
            oAccount.setOwnerId(orgId);
            this.organizationProvider.createOrganizationBillingAccount(oAccount);
        }
        return oAccount;
    }

    private FamilyBillingAccount findOrNewFBillAcountNoLock(Long entityId, Timestamp currentTimestamp) {
        FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
        if (fAccount == null) {
            fAccount = new FamilyBillingAccount();
            fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
            fAccount.setBalance(BigDecimal.ZERO);
            fAccount.setCreateTime(currentTimestamp);
            fAccount.setOwnerId(entityId);
            this.familyProvider.createFamilyBillingAccount(fAccount);
        }
        return fAccount;
    }

    private OrganizationBillingAccount findOrNewOBillAccount(Long orgId, Timestamp currentTimestamp) {
        OrganizationBillingAccount oAccount1 = this.organizationProvider.findOrganizationBillingAccount(orgId);
        if (oAccount1 == null) {
            Tuple<OrganizationBillingAccount, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_O_BILL_ACCOUNT.getCode()).enter(() -> {
                OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(orgId);
                if (oAccount == null) {
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

    private FamilyBillingAccount findOrNewFBillAcount(Long entityId, Timestamp currentTimestamp) {
        FamilyBillingAccount fAccount1 = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
        if (fAccount1 == null) {
            Tuple<FamilyBillingAccount, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_F_BILL_ACCOUNT.getCode()).enter(() -> {
                FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
                if (fAccount == null) {
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

    private void updateOrderStatus(OrganizationOrder order, long payerId, Timestamp payTimeStamp, byte code) {
        order.setPayerId(0L);
        order.setPaidTime(payTimeStamp);
        order.setStatus(code);
        this.organizationProvider.updateOrganizationOrder(order);
    }

    private void checkVendorTypeFormat(String vendorType) {
        if (VendorType.fromCode(vendorType) == null) {
            LOGGER.error("vendor type is wrong.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_VENDOR_TYPE,
                    "vendor type is wrong.");
        }
    }

    private CommunityPmBill checkPmBill(Long billId) {
        CommunityPmBill bill = this.organizationProvider.findOranizationBillById(billId);
        if (bill == null) {
            LOGGER.error("the bill not found.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                    "the bill not found.");
        }
        return bill;
    }

    private void checkAmountIsEqual(BigDecimal amount, BigDecimal waitPayAmount) {
        if (amount.compareTo(waitPayAmount) != 0) {
            LOGGER.error("order amount not equal payAmount.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "order amount not equal payAmount.");
        }

    }

    private OrganizationOrder checkOrder(Long orderId) {
        OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
        if (order == null) {
            LOGGER.error("the order not found.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "the order not found.");
        }
        return order;
    }

    private void checkPayAccountIsNull(String payAccount) {
        if (payAccount == null || payAccount.trim().equals("")) {
            LOGGER.error("payAccount is null or empty.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "payAccount is null or empty.");
        }

    }

    private void checkPayTimeIsNull(String payTime) {
        if (payTime == null || payTime.trim().equals("")) {
            LOGGER.error("payTime is null or empty.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "payTime is null or empty.");
        }

    }

    private void checkPayAmountIsNull(String payAmount) {
        if (payAmount == null || payAmount.trim().equals("")) {
            LOGGER.error("payAmount is null or empty.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "payAmount or is null or empty.");
        }

    }

    private void checkVendorTypeIsNull(String vendorType) {
        if (vendorType == null || vendorType.trim().equals("")) {
            LOGGER.error("vendorType is null or empty.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "vendorType is null or empty.");
        }

    }

    private void checkOrderNoIsNull(String orderNo) {
        if (orderNo == null || orderNo.trim().equals("")) {
            LOGGER.error("orderNo is null or empty.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
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
        if (cmd.getOrderNo() == null || cmd.getOrderNo().isEmpty()) {
            LOGGER.error("orderNo is null or empty.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "orderNo is null or empty.");
        }
        Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
        OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
        if (order == null) {
            LOGGER.error("the order not found.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORDER_NOT_EXIST,
                    "the order not found.");
        }
        CommunityPmBill bill = this.organizationProvider.findOranizationBillById(order.getBillId());
        if (bill == null) {
            LOGGER.error("the bill not found.");
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
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
    public OrganizationOrderDTO createPmBillOrder(CreatePmBillOrderCommand cmd) {
        try {
            if (cmd.getBillId() == null || cmd.getAmount() == null) {
                LOGGER.error("billId or amount is null.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                        "billId or amount is null.");
            }
            CommunityPmBill bill = this.organizationProvider.findOranizationBillById(cmd.getBillId());
            if (bill == null) {
                LOGGER.error("the bill not found.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                        "the bill not found.");
            }
            CommunityPmBill nowBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
            if (nowBill == null || nowBill.getId().compareTo(bill.getId()) != 0) {
                LOGGER.error("the bill is invalid.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                        "the bill is invalid.");
            }

            User user = UserContext.current().getUser();
            OrganizationOrder order = new OrganizationOrder();
            order.setAmount(cmd.getAmount());
            order.setBillId(bill.getId());
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (cmd.getDescription() != null && !cmd.getDescription().equals(""))
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
        } catch (Exception e) {
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                    e.getMessage());
        }
    }

    private void setSignatureParam(OrganizationOrderDTO dto) throws Exception {
        String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
        Long timestamp = System.currentTimeMillis();
        Integer randomNum = (int) (Math.random() * 1000);
        App app = appProvider.findAppByKey(appKey);

        Map<String, String> map = new HashMap<String, String>();
        map.put("appKey", appKey);
        map.put("timestamp", timestamp + "");
        map.put("randomNum", randomNum + "");
        map.put("amount", dto.getAmount().doubleValue() + "");
        String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
        dto.setAppKey(appKey);
        dto.setRandomNum(randomNum);
        dto.setSignature(URLEncoder.encode(signature, "UTF-8"));
        dto.setTimestamp(timestamp);
    }

    private String convertToVerdorType(String verdorTypeStr) {
        if (verdorTypeStr.equals("alipay"))
            return "10001";
        return "10002";
    }

    private void checkPayStatusIsNull(String payStatus, String orderNo) {
        if (payStatus == null) {
            LOGGER.error("payStatus is null.orderNo=" + orderNo);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "payStatus is null.");
        }
    }

    private void checkResultHolderIsNull(ResultHolder resultHolder, String orderNo) {
        if (resultHolder == null) {
            LOGGER.error("remote search pay order return null.orderNo=" + orderNo);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "remote search pay order return null.");
        }
    }

    @Override
    public CommonOrderDTO createPmBillOrderDemo(CreatePmBillOrderCommand cmd) {
        try {
            //自定义接口处理
            if (cmd.getBillId() == null || cmd.getAmount() == null) {
                LOGGER.error("billId or amount is null.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                        "billId or amount is null.");
            }
            CommunityPmBill bill = this.organizationProvider.findOranizationBillById(cmd.getBillId());
            if (bill == null) {
                LOGGER.error("the bill not found.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_EXIST,
                        "the bill not found.");
            }
            CommunityPmBill nowBill = this.propertyMgrProvider.findNewestBillByAddressId(bill.getEntityId());
            if (nowBill == null || nowBill.getId().compareTo(bill.getId()) != 0) {
                LOGGER.error("the bill is invalid.");
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BILL_NOT_VALID,
                        "the bill is invalid.");
            }

            User user = UserContext.current().getUser();
            OrganizationOrder order = new OrganizationOrder();
            order.setAmount(cmd.getAmount());
            order.setBillId(bill.getId());
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (cmd.getDescription() != null && !cmd.getDescription().equals(""))
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
        } catch (Exception e) {
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                    e.getMessage());
        }
    }

    @Override
    public OrganizationOwnerDTO updateOrganizationOwner(UpdateOrganizationOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        User currentUser = UserContext.current().getUser();
        if (currentUser.getNamespaceId() == 999971) {
            LOGGER.error("Insufficient privilege, updateOrganizationOwner");
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INSUFFICIENT_PRIVILEGE,
                    "Insufficient privilege");
        }
        Tuple<CommunityPmOwner, Boolean> tuple =
                coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ORGANIZATION_OWNER.getCode() + cmd.getId()).enter(() -> {
            CommunityPmOwner owner = propertyMgrProvider.findPropOwnerById(cmd.getId());
            if (owner == null) {
                LOGGER.error("Organization owner are not exist, id = {}", cmd.getId());
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                        "Organization owner are not exist, id = %s", cmd.getId());
            }
            boolean needUpdateDoc = false;
			Boolean flag = false;
			if(cmd.getContactName() != null && !owner.getContactName().equals(cmd.getContactName())) {
				flag = true;
			}
            if (cmd.getContactName() != null) {
                owner.setContactName(cmd.getContactName());
                needUpdateDoc = true;
            }
            if (cmd.getAvatar() != null)
                owner.setAvatar(cmd.getAvatar());
            if (cmd.getBirthday() != null) {
				owner.setBirthday(new java.sql.Date(cmd.getBirthday()));
			}else {
            	owner.setBirthday(null);
			}
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
            //added mutiple tels for customer by wentian 2016/6/11
//            if (cmd.getCustomerExtraTels() != null){
//				JsonArray array = new JsonParser().parse(StringHelper.toJsonString(cmd.getCustomerExtraTels())).getAsJsonArray();
//				owner.setContactExtraTels(array.toString());
//			}
            //modify mutiple tels for customer by tangcen 2016/6/29
            if(cmd.getCustomerExtraTels() != null && cmd.getCustomerExtraTels().size() > 0){
	            try{
	                owner.setContactExtraTels(StringHelper.toJsonString(cmd.getCustomerExtraTels()));
	                //set owner's contact token for possible unknown uses
					owner.setContactToken(cmd.getCustomerExtraTels().get(0));
	            }catch (Exception e){
	                LOGGER.error("failed to convert customerExtraTels into jsonarray, custoemrExtratel is={}", cmd.getCustomerExtraTels());
	            }
			}
            UserGender gender = UserGender.fromCode(cmd.getGender());
            if (gender != null) {
                owner.setGender(gender.getCode());
            }
            propertyMgrProvider.updatePropOwner(owner);
            if (needUpdateDoc) {
                pmOwnerSearcher.feedDoc(owner);
            }
			//修改了客户名称则要同步修改合同里面的客户名称
			if(flag) {
				List<Contract> contracts = contractProvider.listContractByCustomerId(null, owner.getId(), CustomerType.INDIVIDUAL.getCode());
				if(contracts != null && contracts.size() > 0) {
					contracts.forEach(contract -> {
						contract.setCustomerName(owner.getContactName());
						contractProvider.updateContract(contract);
						contractSearcher.feedDoc(contract);
					});
				}
			}
            return owner;
        });


        List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(cmd.getNamespaceId(), cmd.getId());
        for (OrganizationOwnerAddressCommand addressCmd : cmd.getAddresses()) {
            OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(cmd.getNamespaceId(), cmd.getId(), addressCmd.getAddressId());
            if (ownerAddress == null) {
                Address address = addressProvider.findAddressById(addressCmd.getAddressId());
                if (address != null) {
                    ownerAddress = new OrganizationOwnerAddress();
                    ownerAddress.setAddressId(address.getId());
                    ownerAddress.setNamespaceId(addressCmd.getNamespaceId());
                    ownerAddress.setOrganizationOwnerId(cmd.getId());
                    ownerAddress.setLivingStatus(addressCmd.getLivingStatus());
                    ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
                    if (addressCmd.getCheckInDate() != null) {
                        createOrganizationOwnerBehavior(cmd.getId(), address.getId(), addressCmd.getCheckInDate(), OrganizationOwnerBehaviorType.IMMIGRATION);
                    }
                    // 如果小区里有该手机号的用户, 则自动审核当前客户
                    //只有在申请了才会自动通过，现要改为只要是注册用户不管申没申请都自动同步给客户 19558 by xiongying20171219
//                    autoApprovalOrganizationOwnerAddress(cmd.getCommunityId(), cmd.getContactToken(), ownerAddress);
                    propertyMgrProvider.createOrganizationOwnerAddress(ownerAddress);
                    getIntoFamily(address, cmd.getCustomerExtraTels(), cmd.getNamespaceId());
                } else {
                    LOGGER.error("CreateOrganizationOwner: address id is wrong! addressId = {}", addressCmd.getAddressId());
                }
            } else {
                if (ownerAddressList != null && ownerAddressList.size() > 0) {
                    for (OrganizationOwnerAddress address : ownerAddressList) {
                        if (address.getId().equals(ownerAddress.getId())) {
                            ownerAddressList.remove(address);
                            break;
                        }
                    }

                }

                ownerAddress.setLivingStatus(addressCmd.getLivingStatus());
                ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
                propertyMgrProvider.updateOrganizationOwnerAddress(ownerAddress);
            }
        }

        if (ownerAddressList != null && ownerAddressList.size() > 0) {
            for (OrganizationOwnerAddress ownerAddress : ownerAddressList) {
                propertyMgrProvider.deleteOrganizationOwnerAddress(ownerAddress);

                // 创建删除行为记录
                createOrganizationOwnerBehavior(ownerAddress.getOrganizationOwnerId(), ownerAddress.getAddressId(),
                        System.currentTimeMillis(), OrganizationOwnerBehaviorType.DELETE);

                // 如果当前用户在该地址下认证过,则移除认证状态
                Family family = this.familyProvider.findFamilyByAddressId(ownerAddress.getAddressId());
                if (family != null) {
                    leaveFamilyByOwnerId(ownerAddress.getOrganizationOwnerId(), family.getId());
                }

                createAuditLog(ownerAddress.getId(), ownerAddress.getClass());
            }
        }
        CommunityPmOwner owner = propertyMgrProvider.findPropOwnerById(cmd.getId());
        pmOwnerSearcher.feedDoc(owner);

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
        newPmOwner.setOrgOwnerTypeId(7L);// 先写死, 归类为"其他"
        newPmOwner.setGender(memberUser.getGender());
        newPmOwner.setCommunityId(memberUser.getCommunityId() == null ? null : memberUser.getCommunityId().toString());
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
//		User currentUser = UserContext.current().getUser();
        if (cmd.getNamespaceId() == 999971) {
            LOGGER.error("Insufficient privilege, createOrganizationOwner");
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INSUFFICIENT_PRIVILEGE,
                    "Insufficient privilege");
        }
        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(cmd.getOrgOwnerTypeId());
        if (ownerType == null) {
            invalidParameterException("orgOwnerTypeId", cmd.getOrgOwnerTypeId());
        }
        //checkContactTokenUnique(cmd.getCommunityId(), cmd.getContactToken());

        CommunityPmOwner owner = ConvertHelper.convert(cmd, CommunityPmOwner.class);
        if (cmd.getBirthday() != null) {
            owner.setBirthday(new java.sql.Date(cmd.getBirthday()));
        }
        owner.setCommunityId(cmd.getCommunityId().toString());
        owner.setOrgOwnerTypeId(ownerType.getId());
        owner.setNamespaceId(cmd.getNamespaceId());
        owner.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
        if(cmd.getCustomerExtraTels() != null && cmd.getCustomerExtraTels().size() > 0){
        	checkContactExtraTels(cmd.getCommunityId(), cmd.getCustomerExtraTels());
        	try{
                owner.setContactExtraTels(StringHelper.toJsonString(cmd.getCustomerExtraTels()));
                //set owner's contact token for possible unknown uses
				owner.setContactToken(cmd.getCustomerExtraTels().get(0));
            }catch (Exception e){
                LOGGER.error("failed to convert customerExtraTels into jsonarray, custoemrExtratel is={}", cmd.getCustomerExtraTels());
            }
		}
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
            for (OrganizationOwnerAddressCommand addressCmd : cmd.getAddresses()) {
                Address address = addressProvider.findAddressById(addressCmd.getAddressId());
                if (address != null) {
                    OrganizationOwnerAddress ownerAddress = new OrganizationOwnerAddress();
                    ownerAddress.setAddressId(address.getId());
                    ownerAddress.setNamespaceId(addressCmd.getNamespaceId());
                    ownerAddress.setOrganizationOwnerId(ownerId);
                    ownerAddress.setLivingStatus(addressCmd.getLivingStatus());
                    ownerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
                    if (addressCmd.getCheckInDate() != null) {
                        createOrganizationOwnerBehavior(ownerId, address.getId(), addressCmd.getCheckInDate(), OrganizationOwnerBehaviorType.IMMIGRATION);
                    }
                    // 如果小区里有该手机号的用户, 则自动审核当前客户
                    //只有在申请了才会自动通过，现要改为只要是注册用户不管申没申请都自动同步给客户 19558 by xiongying20171219
//                    autoApprovalOrganizationOwnerAddress(cmd.getCommunityId(), cmd.getContactToken(), ownerAddress);
                    propertyMgrProvider.createOrganizationOwnerAddress(ownerAddress);
                    getIntoFamily(address, cmd.getCustomerExtraTels(), cmd.getNamespaceId());
                } else {
                    LOGGER.error("CreateOrganizationOwner: address id is wrong! addressId = {}", addressCmd.getAddressId());
                }
            }
            pmOwnerSearcher.feedDoc(owner);
            return null;
        });
        return convertOwnerToDTO(owner);
    }
	
    private void checkContactExtraTels(Long communityId, List<String> customerExtraTels) {
    	for(String tel : customerExtraTels){
    		List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwnersByTel(currentNamespaceId(),communityId, tel);
    		if (pmOwners != null && pmOwners.size() > 0) {
                LOGGER.error("OrganizationOwner are already exist, tel = {}.", tel);
                throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_EXIST,
                        "OrganizationOwner are already exist, tel = %s.", tel);
            }
    	}
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
        if (userIdentifier != null) {
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
        if (userIdentifier != null) {
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
        String ownerCommunity = owner.getCommunityId();
        if (ownerCommunity != null) {
            String[] communityIds = ownerCommunity.split(",");
            List<String> ownerCommunities = new ArrayList<>();
            for (String communityId : communityIds) {
                if (!communityId.equals(cmd.getCommunityId())) {
                    ownerCommunities.add(communityId);
                }
            }

            if (ownerCommunities.size() == 0) {
                this.dbProvider.execute((TransactionStatus status) -> {
                    propertyMgrProvider.deletePropOwner(owner);
                    pmOwnerSearcher.deleteById(owner.getId());
                    //tuichujiating
                    leaveFamily(owner.getAddressId(), owner.getContactToken(), owner.getNamespaceId());

                    return null;
                });
            } else {
                StringBuilder sb = new StringBuilder();
                ownerCommunities.forEach(community -> {
                    if (sb.length() == 0) {
                        sb.append(community);
                    } else {
                        sb.append(",");
                        sb.append(community);
                    }
                });
                this.dbProvider.execute((TransactionStatus status) -> {
                    owner.setCommunityId(sb.toString());
                    propertyMgrProvider.updatePropOwner(owner);
                    pmOwnerSearcher.feedDoc(owner);
                    //tuichujiating
                    leaveFamily(owner.getAddressId(), owner.getContactToken(), owner.getNamespaceId());

                    return null;
                });
            }
//		}
//		if(owner.getCommunityId() == cmd.getCommunityId()) {
//			this.dbProvider.execute((TransactionStatus status) -> {
//				propertyMgrProvider.deletePropOwner(owner);
//				pmOwnerSearcher.deleteById(owner.getId());
//				//tuichujiating
//				leaveFamily(owner.getAddressId(), owner.getContactToken(), owner.getNamespaceId());
//
//				return null;
//			});
        } else {
            LOGGER.error("deletePMPropertyOwnerAddress: id is not in the community! id = " + cmd.getId() + ", communityId = " + cmd.getCommunityId());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_COMMUNITY,
                    "id is not in the community!");
        }

    }

    @Override
    public void createPMPropertyOwnerAddress(CreatePropOwnerAddressCommand cmd) {
        Integer namespaceId = currentNamespaceId();

        CommunityPmOwner owner = new CommunityPmOwner();
        owner.setCommunityId(cmd.getCommunityId() == null ? null : cmd.getCommunityId().toString());
        owner.setContactName(cmd.getContactName());
        owner.setContactToken(cmd.getContactToken());
        owner.setContactType(cmd.getContactType());
        owner.setOrganizationId(cmd.getOrganizationId());
        owner.setNamespaceId(namespaceId);

        Address address = addressProvider.findAddressById(cmd.getAddressId());
        if (null != address) {
            owner.setAddressId(address.getId());
            owner.setAddress(address.getAddress());

            this.dbProvider.execute((TransactionStatus status) -> {
                propertyMgrProvider.createPropOwner(owner);
                pmOwnerSearcher.feedDoc(owner);
                //jiarujiating
                getIntoFamily(address, Collections.singletonList(cmd.getContactToken()), namespaceId);

                return null;
            });

        } else {
            LOGGER.error("createOrUpdateOrganizationOwner: address id is wrong! addressId = " + cmd.getAddressId());
        }

    }

    private void getIntoFamily(Address address, List<String> contactToken, Integer namespaceId) {
        if(contactToken!=null && contactToken.size()>0){
            contactToken.forEach((c)->{
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, c);
                if (null != userIdentifier) {
                    User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                    if (null != user) {
                        address.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                        familyService.getOrCreatefamily(address, user);
                    }
                }
            });
        }
    }

    private void leaveFamily(Long addressId, String contactToken, Integer namespaceId) {
        if (null == addressId) {
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
                    "Invalid addressId parameter");
        }
        Address address = this.addressProvider.findAddressById(addressId);
        if (null == address) {
            throw errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Invalid addressId parameter,address is not found");
        }

        Family family = this.familyProvider.findFamilyByAddressId(addressId);
        if (null != family) {
            LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
            leaveCmd.setId(family.getId());
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, contactToken);
            if (null != userIdentifier) {
                User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                if (null != user) {
                    familyService.leave(leaveCmd, user);
                }
            }
        }
    }

    @Override
    public void processUserForOwner(UserIdentifier identifier) {
//        List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwnersByToken(
//                identifier.getNamespaceId(), identifier.getIdentifierToken());
        List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwnersByTel(identifier.getNamespaceId(), null, identifier.getIdentifierToken());
        LOGGER.debug("processUserForOwner: user identifier: {}, owners: {}", identifier.getIdentifierToken(), StringHelper.toJsonString(owners));
        if (null != owners && owners.size() > 0) {
            for (CommunityPmOwner owner : owners) {
                List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(identifier.getNamespaceId(), owner.getId());
//				Address address = addressProvider.findAddressById(owner.getAddressId());
                if (ownerAddressList != null && ownerAddressList.size() > 0) {
                    for (OrganizationOwnerAddress ownerAddress : ownerAddressList) {
                        Address address = addressProvider.findAddressById(ownerAddress.getAddressId());
                        if (address != null) {
                            //jiarujiating
                            getIntoFamily(address, Collections.singletonList(identifier.getIdentifierToken()), identifier.getNamespaceId());
                        }
                    }
                }

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
    public List<OrganizationOwnerBehaviorDTO> listApartmentOrganizationOwnerBehaviors(ListApartmentOrganizationOwnerBehaviorsCommand cmd) {
        User user = UserContext.current().getUser();
        List<OrganizationOwnerBehavior> behaviorList = propertyMgrProvider.listApartmentOrganizationOwnerBehaviors(cmd.getAddressId());

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
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ORGANIZATION_OWNER_BEHAVIOR_NOT_EXIST,
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
                cmd.getNamespaceId(), cmd.getId());
        if (attachment == null) {
            LOGGER.error("Organization owner attachment is not exist. id = {}", cmd.getId());
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ATTACHMENT_NOT_EXIST,
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
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_ATTACHMENT_NOT_EXIST,
                    "Organization owner car attachment is not exist. id = %s", cmd.getId());
        }
        propertyMgrProvider.deleteOrganizationOwnerCarAttachment(attachment);
        createAuditLog(attachment.getId(), attachment.getClass());
    }

    @Override
    public void deleteOrganizationOwner(DeleteOrganizationOwnerCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        User currentUser = UserContext.current().getUser();
        if (currentUser.getNamespaceId() == 999971) {
            LOGGER.error("Insufficient privilege, deleteOrganizationOwner");
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INSUFFICIENT_PRIVILEGE,
                    "Insufficient privilege");
        }
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

//        Integer namespaceId = currentNamespaceId();
        List<OrganizationOwnerAddress> ownerAddressList = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(cmd.getNamespaceId(), cmd.getOrgOwnerId());

        return ownerAddressList.stream().map(r -> {
            Address address = addressProvider.findAddressById(r.getAddressId());
            if (address != null) {
            	OrganizationOwnerAddressDTO dto = new OrganizationOwnerAddressDTO();
                String locale = currentLocale();
                dto.setBuilding(address.getBuildingName());
                dto.setAddress(address.getAddress());
                dto.setAddressId(address.getId());
                dto.setApartment(address.getApartmentName());
                dto.setChargeArea(address.getChargeArea());
                dto.setOrientation(address.getOrientation());

                if (address.getStatus().byteValue() == AddressAdminStatus.ACTIVE.getCode()) {
                	dto.setAddressExists((byte)1);
				}else {
					dto.setAddressExists((byte)0);
				}

                if (address.getLivingStatus() == null) {
                    CommunityAddressMapping mapping = propertyMgrProvider.findAddressMappingByAddressId(address.getId());
                    if (mapping != null) {
                        address.setLivingStatus(mapping.getLivingStatus());
                    } else {
                        address.setLivingStatus(AddressMappingStatus.LIVING.getCode());
                    }
                }
                dto.setApartmentLivingStatus(address.getLivingStatus());
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
            }
            return null;
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

//        Integer namespaceId = currentNamespaceId();
        Integer namespaceId = cmd.getNamespaceId();
        OrganizationOwnerOwnerCar ownerOwnerCar = propertyMgrProvider.findOrganizationOwnerOwnerCarByOwnerIdAndCarId(
                namespaceId, cmd.getOrgOwnerId(), cmd.getCarId());

        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getOrgOwnerId());
        OrganizationOwnerCar ownerCar = propertyMgrProvider.findOrganizationOwnerCar(namespaceId, cmd.getCarId());
        OrganizationOwnerOwnerCar primaryUser = propertyMgrProvider.findOrganizationOwnerCarPrimaryUser(namespaceId, cmd.getCarId());
        dbProvider.execute(status -> {
            // 如果现已经有首要联系人,则设置当前首要联系人为非首要联系人
            if (primaryUser != null) {
                primaryUser.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.NORMAL.getCode());
                propertyMgrProvider.updateOrganizationOwnerOwnerCar(primaryUser);
            } else {
                //没有的话要new一个
                OrganizationOwnerOwnerCar newPrimaryUser = new OrganizationOwnerOwnerCar();
                newPrimaryUser.setNamespaceId(namespaceId);
                newPrimaryUser.setCarId(cmd.getCarId());
                newPrimaryUser.setOrganizationOwnerId(pmOwner.getId());
                newPrimaryUser.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode());
                propertyMgrProvider.createOrganizationOwnerOwnerCar(newPrimaryUser);
            }
            if (ownerOwnerCar != null) {
                ownerOwnerCar.setPrimaryFlag(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode());
                propertyMgrProvider.updateOrganizationOwnerOwnerCar(ownerOwnerCar);
            }

            ownerCar.setContactNumber(pmOwner.getContactToken());
            propertyMgrProvider.updateOrganizationOwnerCar(ownerCar);
            return null;
        });

    }

    @Override
    public void deleteRelationOfOrganizationOwnerAndCar(DeleteRelationOfOrganizationOwnerAndCarCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

//        Integer namespaceId = currentNamespaceId();
        Integer namespaceId = cmd.getNamespaceId();
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

        if (dto.getContentUri() != null) {
            dto.setContentUrl(parserUri(dto.getContentUri(), EhOrganizationOwners.class.getSimpleName(), dto.getId()));
        }
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
            List<OrganizationOwnerDTO> owners = new ArrayList<>();
            ownerDTOs.forEach((r) -> {
                if (r.getAddresses() != null && r.getAddresses().size() > 0) {
                    r.getAddresses().forEach((address) -> {
                        OrganizationOwnerDTO dto = ConvertHelper.convert(r, OrganizationOwnerDTO.class);
                        dto.setBuilding(address.getBuilding());
                        dto.setAddress(address.getApartment());
                        dto.setLivingDate(address.getLivingDate());
                        dto.setLivingStatus(address.getLivingStatus());
                        owners.add(dto);
                    });
//                    owners.remove(r);
                }else{
                    owners.add(ConvertHelper.convert(r, OrganizationOwnerDTO.class));
                }
            });

            String fileName = String.format("客户信息_%s_%s", community.getName(), DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
            ExcelUtils excelUtils = new ExcelUtils(response, fileName, "客户信息");

            excelUtils.setNeedTitleRemark(true).setTitleRemark("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                    "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照提示输入。\n" +
                    "2、请在表格里面逐行录入数据，建议一次导入不超过500条信息。\n" +
                    "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                    "4、日期输入格式：yyyy-MM-dd（2017-01-01）或 yyyy/MM/dd（2017/01/01）\n" +
                    "5、红色字段为必填项,不填将导致导入失败。\n" +
                    "6、请注意：手机号码是唯一的，不能重复\n" +
                    "7、楼栋门牌必须在后台管理真实存在\n" +
                    "8、身份证号码为18位\n" +
                    "9、客户类型为：业主、租户、亲属、朋友、保姆、地产中介、其他、无；\n" +
                    "10、是否在户：是、否\n" +
                    "\n", (short) 13, (short) 2500).setNeedSequenceColumn(false).setIsCellStylePureString(true);
            String[] propertyNames = {"contactName", "orgOwnerType", "customerExtraTelsForExport", "gender","building","address", "livingStatus","livingDate", "birthdayDate", "maritalStatus", "job", "company",
                    "idCardNumber", "registeredResidence"};
            
            
            String[] titleNames = {"姓名", "客户类型", "手机号码", "性别","楼栋","门牌", "是否在户", "迁入日期","生日", "婚姻状况", "职业", "单位", "证件号码", "户口所在地"};
            int[] titleSizes = {20, 10, 40, 30, 20, 10, 20, 30, 40,40, 30, 30, 40, 40};
            excelUtils.writeExcel(propertyNames, titleNames, titleSizes, owners);
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
        //如果用户支持多手机号，则导出customerExtraTelsForExport的值
        if(owner.getContactExtraTels() != null){
            String contactExtraTels = owner.getContactExtraTels();
            List<String> contactExtraTelsList = (List<String>)StringHelper.fromJsonString(contactExtraTels, ArrayList.class);
            dto.setCustomerExtraTels(contactExtraTelsList);
            //用在导出Excel时
            String customerExtraTelsForExport = contactExtraTelsList.toString();
            dto.setCustomerExtraTelsForExport(customerExtraTelsForExport.substring(1, customerExtraTelsForExport.length()-1));
        }else {
        	//如果不支持多手机号，则导出ContactToken的值
        	dto.setCustomerExtraTelsForExport(dto.getContactToken());
        	List<String> contactExtraTelsList = new ArrayList<>();
        	contactExtraTelsList.add(dto.getContactToken());
        	dto.setCustomerExtraTels(contactExtraTelsList);
		}
        OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(owner.getOrgOwnerTypeId());
        if (ownerType != null) {
            dto.setOrgOwnerType(ownerType.getDisplayName());
        }
        if (owner.getBirthday() != null) {
            dto.setBirthday(owner.getBirthday().getTime());
            dto.setBirthdayDate(DateUtil.dateToStr(new Date(dto.getBirthday()), DateUtil.YMR_SLASH));
        }

        //添加门牌
        List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(owner.getNamespaceId(), owner.getId());
        dto.setAddresses(addresses.stream().map(r2 -> {
            OrganizationOwnerAddressDTO d = ConvertHelper.convert(r2, OrganizationOwnerAddressDTO.class);
            Address address = addressProvider.findAddressById(r2.getAddressId());
            if (address != null) {
                OrganizationOwnerBehavior behavior = propertyMgrProvider.findOrganizationOwnerBehaviorByOwnerAndAddressId(owner.getId(), address.getId());
                if (behavior != null) {
                    d.setLivingDate(DateUtil.dateToStr(new Date(behavior.getBehaviorTime().getTime()), DateUtil.YMR_SLASH));
                }
                d.setAddressId(address.getId());
                d.setAddress(address.getAddress());
                d.setApartment(address.getApartmentName());
                d.setLivingStatus(localeStringProvider.find(OrganizationOwnerLocaleStringScope.LIVING_STATUS_SCOPE, r2.getLivingStatus().toString(), currentLocale()).getText());
                d.setBuilding(address.getBuildingName());
            }
            return d;
        }).collect(Collectors.toList()));
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
        statisticDTOList.forEach(r -> r.setThird((int) ((Double.valueOf(r.getSecond()) / totalNum[0] * 100)) + "%"));
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

        maleDtoList.forEach(r -> r.setThird((int) ((Double.valueOf(r.getSecond()) / maleTotalNum[0] * 100)) + ""));
        femaleDtoList.forEach(r -> r.setThird((int) ((Double.valueOf(r.getSecond()) / femaleTotalNum[0] * 100)) + ""));
        totalDtoMap.values().forEach(r -> r.setThird((int) ((Double.valueOf(r.getSecond()) / totalNum[0] * 100)) + ""));

        // 为了把101+的放在最后面
        ListOrganizationOwnerStatisticDTO otherAgeDto = totalDtoMap.remove("未知");

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
        ownerAddress = createOrganizationOwnerAddress(address.getId(), cmd.getLivingStatus(), cmd.getNamespaceId(),
                cmd.getOrgOwnerId(), OrganizationOwnerAddressAuthType.ACTIVE);
        // 自动审核用户与客户
        //只有在申请了才会自动通过，现要改为只要是注册用户不管申没申请都自动同步给客户 19558 by xiongying20171219
        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(cmd.getOrgOwnerId());
        if (pmOwner != null) {
            getIntoFamily(address, (List<String>)StringHelper.fromJsonString(pmOwner.getContactExtraTels(),ArrayList.class), pmOwner.getNamespaceId());
//            autoApprovalOrganizationOwnerAddress(address.getCommunityId(), pmOwner.getContactToken(), ownerAddress);
        }
        return buildOrganizationOwnerAddressDTO(cmd, address, ownerAddress);
    }

    @Override
    public void addAddressToOrganizationOwner(Integer namespaceId, Long addressId, Long orgOwnerId) {
        Address address = addressProvider.findAddressById(addressId);
        if (address == null) {
            LOGGER.error("The address is not exist, addressId = {}.", addressId);
            return;
        }
        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                namespaceId, orgOwnerId, addressId);
        if (ownerAddress != null) {
            LOGGER.error("The organization owner {} already in address {}.", orgOwnerId, addressId);
            return;
        }
        createOrganizationOwnerAddress(address.getId(), AddressLivingStatus.ACTIVE.getCode(), namespaceId,
                orgOwnerId, OrganizationOwnerAddressAuthType.ACTIVE);

        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(orgOwnerId);
        if (pmOwner != null) {
            getIntoFamily(address, (List<String>)StringHelper.fromJsonString(pmOwner.getContactExtraTels(), ArrayList.class), pmOwner.getNamespaceId());
        }
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

    @Override
    public void deleteAddressToOrgOwner(Integer namespaceId, Long addressId, Long orgOwnerId) {
        Address address = addressProvider.findAddressById(addressId);
        if (address == null) {
            LOGGER.error("The address is not exist, addressId = {}.", addressId);
            return;
        }
        OrganizationOwnerAddress ownerAddress = propertyMgrProvider.findOrganizationOwnerAddressByOwnerAndAddress(
                namespaceId, orgOwnerId, address.getId());
        if (ownerAddress == null) {
            LOGGER.error("The ownerAddress is not exist.");
            return;
        }
        propertyMgrProvider.deleteOrganizationOwnerAddress(ownerAddress);
        // 创建删除行为记录
        createOrganizationOwnerBehavior(ownerAddress.getOrganizationOwnerId(), ownerAddress.getAddressId(),
                System.currentTimeMillis(), OrganizationOwnerBehaviorType.DELETE);

        // 如果当前用户在该地址下认证过,则移除认证状态
        Family family = this.familyProvider.findFamilyByAddressId(addressId);
        if (family != null) {
            leaveFamilyByOwnerId(orgOwnerId, family.getId());
        }

        createAuditLog(ownerAddress.getId(), ownerAddress.getClass());
    }

    private void createAuditLog(Long resourceId, Class<?> resourceType) {
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
        Integer namespaceId = cmd.getNamespaceId();
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
                if (userIdentifier != null) {
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
        if (family == null) {
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
            if (userIdentifier != null) {
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
        return propertyMgrProvider.listOrganizationOwnersByAddressId(address.getNamespaceId(), address.getId(), mapper);
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

    private String parserUri(String uri, String ownerType, long ownerId) {
        try {
            if (!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri, ownerType, ownerId);
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
    public ListOrganizationOwnersResponse searchOrganizationOwners(SearchOrganizationOwnersCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return pmOwnerSearcher.query(cmd);
    }

    @Override
    public List<OrganizationOwnerDTO> searchOrganizationOwnersBycondition(SearchOrganizationOwnersByconditionCommand cmd) {
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
            if (apartments.size() == 0)
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
        if (!StringUtils.isEmpty(cmd.getContact())) {
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
    public ImportFileTaskDTO importOrganizationOwners(@Valid ImportOrganizationsOwnersCommand cmd, MultipartFile[] file) {
        User user = UserContext.current().getUser();
        if (cmd.getNamespaceId() == 999971) {
            LOGGER.error("Insufficient privilege, importOrganizationOwners");
            throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INSUFFICIENT_PRIVILEGE,
                    "Insufficient privilege");
        }
        Long communityId = cmd.getCommunityId();
        this.checkCommunityIdIsNull(communityId);
        this.checkCommunity(communityId);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        ArrayList resultList = processorExcel(file[0]);

        resultList.removeIf((r) -> r.toString().equals("RowResult: [{}]"));
        ImportFileTask task = processorOrganizationOwner(user.getId(), cmd.getOrganizationId(), cmd.getCommunityId(), cmd.getNamespaceId(), resultList);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
        //用 bulkUpdate不会更新 by xiongying20171009
//        pmOwnerSearcher.bulkUpdate(ownerList);
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

    private ImportFileTask processorOrganizationOwner(long userId, long organizationId, Long communityId, Integer namespaceId, ArrayList resultList) {
        if (resultList != null && resultList.size() > 0) {
            //增加导入错误提示信息
            ImportFileTask task = new ImportFileTask();
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(organizationId);
            task.setType(ImportFileTaskType.INDIVIDUAL_CUSTOMER.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(() -> {
                ImportFileResponse response = new ImportFileResponse();
                List<ImportOrganizationOwnerDTO> datas = handleImportOrganizationOwnersData(resultList);
                if (datas.size() > 0) {
                    //设置导出报错的结果excel的标题
                    response.setTitle(datas.get(0));
                    datas.remove(0);
                }
                List<ImportFileResultLog<ImportOrganizationOwnerDTO>> results = importOrganizationOwnerData(organizationId, communityId, namespaceId, datas);
                response.setTotalCount((long) datas.size());
                if (results != null) {
                    response.setFailCount((long) results.size());
                }
                response.setLogs(results);
                return response;
            }, task);
            return task;
        } else {
            LOGGER.error("excel data format is not correct.rowCount=" + resultList);
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_EXCEL_DATA_FORMAT,
                    "excel data format is not correct");
        }
    }

    private List<ImportFileResultLog<ImportOrganizationOwnerDTO>> importOrganizationOwnerData(long organizationId, Long communityId, Integer namespaceId, List<ImportOrganizationOwnerDTO> datas) {
        List<ImportFileResultLog<ImportOrganizationOwnerDTO>> resultLogs = new ArrayList<>();
        List<String> contactTokenList = new ArrayList<>();
        if (datas == null || datas.size() == 0) {
            return resultLogs;
        }

        for (ImportOrganizationOwnerDTO dto : datas) {
            ImportFileResultLog<ImportOrganizationOwnerDTO> log = new ImportFileResultLog<>(PropertyServiceErrorCode.SCOPE);
            CommunityPmOwner owner = ConvertHelper.convert(dto, CommunityPmOwner.class);
            // 校验必填项目
            Boolean mandatoryFlag = Stream.of(dto.getContactName(), dto.getContactType(), dto.getContactToken(), dto.getGender(), dto.getBuilding(), dto.getAddress(), dto.getLivingStatus()).anyMatch(StringUtils::isEmpty);
            if (mandatoryFlag) {
                LOGGER.error("organization owner mandatory column is empty, data = {}", dto);
                log.setData(dto);
                // 这里不是动态表单 暂时用这个
                String fieldName = getDisplayName(dto);
                log.setFieldName(fieldName);
                log.setErrorLog("eorganization owner mandatory column is empty");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_MANDATORY_COLUMN);
                resultLogs.add(log);
                continue;
            }
            Long orgOwnerTypeId = null;
            if((orgOwnerTypeId = parseOrgOwnerTypeId(dto.getContactType()))==null){
                LOGGER.error("organization owner type is not exist, data = {}", dto);
                log.setData(dto);
                log.setErrorLog("eorganization owner type is not exist,");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_CUSTOMER_TYPE_ERROR);
                resultLogs.add(log);
                continue;
            } else {
                owner.setOrgOwnerTypeId(orgOwnerTypeId);
            }
            //校验性别
            if (!UserGender.FEMALE.equals(UserGender.fromText(dto.getGender())) && !UserGender.MALE.equals(UserGender.fromText(dto.getGender()))) {
                LOGGER.error("gender is not correct , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("gender is not correct");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_GENDER_FORMAT_ERROR);
                resultLogs.add(log);
                continue;
            } else {
                owner.setGender(UserGender.fromText(dto.getGender()).getCode());
            }
            // 校验楼栋门牌是否存在
            Address address = parseAddress(namespaceId, communityId, dto.getBuilding(), dto.getAddress());
            if (address == null) {
                LOGGER.error("organization owner building and address is not exist , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("organization owner building and address is not exist ");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_ADDRESS_NOT_EXISTS);
                resultLogs.add(log);
                continue;
            }
            //校验入驻状态
            if (null == parseLivingStatus(dto.getLivingStatus())) {
                LOGGER.error("living status is not illegal  , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("living status is not illegal ");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_LIVING_STATUS_FORMAT_ERROR);
                resultLogs.add(log);
                continue;
            }
            //校验时间格式
            if(!validDate(dto.getLivingTime())){
                LOGGER.error("organization owner dateTime format error  , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("organization owner dateTime format error ");
                log.setFieldName("迁入日期");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_DATE_INVALIED_ERROR);
                resultLogs.add(log);
                continue;
            }
            java.sql.Date date = null;
            if ((date = parseDate(dto.getLivingTime())) == null && org.apache.commons.lang.StringUtils.isNotBlank(dto.getLivingTime())) {
                LOGGER.error("organization owner dateTime format error  , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("organization owner dateTime format error ");
                log.setFieldName("迁入日期");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_DATE_FORMAT_ERROR);
                resultLogs.add(log);
                continue;
            }
            //校验时间格式
            if(!validDate(dto.getBirthday())){
                LOGGER.error("organization owner dateTime is not  exist  , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("organization owner dateTime format error ");
                log.setFieldName("生日");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_DATE_INVALIED_ERROR);
                resultLogs.add(log);
                continue;
            }
            if ((date = parseDate(dto.getBirthday())) == null && org.apache.commons.lang.StringUtils.isNotBlank(dto.getBirthday())) {
                LOGGER.error("organization owner dateTime format error  , data = {}", dto);
                log.setData(dto);
                log.setErrorLog("organization owner dateTime format error ");
                log.setFieldName("生日");
                log.setCode(PropertyServiceErrorCode.ERROR_IMPORT_DATE_FORMAT_ERROR);
                resultLogs.add(log);
                continue;
            } else {
                owner.setBirthday(date);
            }

            // 检查手机号的唯一性
            //CommunityPmOwner exist = propertyMgrProvider.findOrganizationOwnerByCommunityIdAndContactToken(namespaceId, communityId, dto.getContactToken());
            CommunityPmOwner exist = null;
            
            List<String> extraTels = (List<String>)StringHelper.fromJsonString(dto.getContactExtraTels(), ArrayList.class);
            for(String tel : extraTels){
        		List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwnersByTel(namespaceId,communityId, tel);
        		if (pmOwners != null && pmOwners.size() > 0) {
        			exist = pmOwners.get(0);
        			break;
                }
        	}
            if (exist != null) {
                // 支持同一个人导入多行，但只能生成一个业主信息，add by tt, 170427
                // remove new data which is blank
                compareOwnerInfo(exist, owner);
                owner.setId(exist.getId());
            }
            for(String tel : extraTels){
            	if (!contactTokenList.contains(tel) && exist!=null) {
                    propertyMgrProvider.deleteOrganizationOwnerAddressByOwnerId(namespaceId, exist.getId());
                    break;
            	}
        	}
            
            contactTokenList.addAll(extraTels);
            owner.setNamespaceId(namespaceId);
            owner.setCreatorUid(UserContext.currentUserId());
            owner.setOrganizationId(organizationId);
            owner.setCommunityId(communityId == null ? null : communityId.toString());
            owner.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
            owner.setAddressId(address.getId());
            Long ownerId = owner.getId();
            if (ownerId == null) {
                ownerId = propertyMgrProvider.createPropOwner(owner);
                pmOwnerSearcher.feedDoc(owner);
            }else {
                propertyMgrProvider.updatePropOwner(owner);
                pmOwnerSearcher.feedDoc(owner);
            }
            List<OrganizationOwnerAddress> ownerAddresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(namespaceId, ownerId);
            List<Long> addressIds = new ArrayList<>();
            if (ownerAddresses != null && ownerAddresses.size() > 0) {
                addressIds = ownerAddresses.stream().map(OrganizationOwnerAddress::getAddressId).collect(Collectors.toList());
            }
            if (!addressIds.contains(address.getId())) {
                Byte livingStatus = parseLivingStatus(dto.getLivingStatus());
                createOrganizationOwnerAddress(address.getId(), livingStatus, namespaceId, ownerId, OrganizationOwnerAddressAuthType.INACTIVE);
                if (StringUtils.hasLength(dto.getLivingTime())) {
                    long time = parseDate(dto.getLivingTime()).getTime();
                    createOrganizationOwnerBehavior(ownerId, address.getId(), time, OrganizationOwnerBehaviorType.IMMIGRATION);
                }
            }
            pmOwnerSearcher.feedDoc(owner);
        }
        return resultLogs;
    }

    private boolean validDate(String livingTime) {
        try {
            parseDate(livingTime);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private String getDisplayName(ImportOrganizationOwnerDTO dto) {
        String displayName = null;
        if (StringUtils.isEmpty(dto.getContactName())) {
            displayName = "客户名称";
        }
        if(StringUtils.isEmpty(dto.getContactType())){
            displayName = "客户类型";
        }
        if (StringUtils.isEmpty(dto.getContactToken())) {
            displayName = "手机号码";
        }
        if (StringUtils.isEmpty(dto.getGender())) {
            displayName = "性别";
        }
        if (StringUtils.isEmpty(dto.getBuilding())) {
            displayName = "楼栋";
        }
        if (StringUtils.isEmpty(dto.getAddress())) {
            displayName = "门牌";
        }
        if (StringUtils.isEmpty(dto.getLivingStatus())) {
            displayName = "是否在户";
        }
        return displayName;
    }

    private void compareOwnerInfo(CommunityPmOwner exist, CommunityPmOwner owner) {
        // compare customer from excel and db exist and filter all blank fields
        Class<?> clz = CommunityPmOwner.class;
        Field[] fields = clz.getSuperclass().getDeclaredFields();
        if (fields.length > 0) {
            Object existData = null;
            Object excelData = null;
            for (Field field : fields) {
                PropertyDescriptor descriptor = null;
                try {
                    if(field.getName().equals("serialVersionUID")){
                        continue;
                    }
                    descriptor = new PropertyDescriptor(field.getName(), clz);
                    Method method = descriptor.getReadMethod();
                    if (method != null) {
                        existData = method.invoke(exist);
                        excelData = method.invoke(owner);
                        if (excelData == null || excelData.equals("")) {
                            if (existData != null && excelData != "") {
                                Method writeMethod = descriptor.getWriteMethod();
                                writeMethod.invoke(owner, existData);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("invoke exception:{}", e);
                }
            }
        }
    }

    private List<ImportOrganizationOwnerDTO> handleImportOrganizationOwnersData(ArrayList resultList) {
        List<ImportOrganizationOwnerDTO> organizationOwnerDTOS = new ArrayList<>();
        if (resultList == null || resultList.size() == 0) {
            return organizationOwnerDTOS;
        }
        for (int rowIndex = 1; rowIndex < resultList.size(); rowIndex++) {
            RowResult result = (RowResult) resultList.get(rowIndex);
            ImportOrganizationOwnerDTO owner = new ImportOrganizationOwnerDTO();
            owner.setContactName(RowResult.trimString(result.getA()) == null ? "" : RowResult.trimString(result.getA()));
            owner.setContactType(RowResult.trimString(result.getB()) == null ? "" : RowResult.trimString(result.getB()));
            //fix conflict by tangcen
            //owner.setContactToken(RowResult.trimString(result.getC()) == null ? "" : RowResult.trimString(result.getC()));
            String contactExtraTelsString = RowResult.trimString(result.getC());
            List<String> contactExtraTels = Arrays.asList(contactExtraTelsString.split(","));
            owner.setContactToken(contactExtraTels.get(0));
            owner.setContactExtraTels(StringHelper.toJsonString(contactExtraTels));
            
            owner.setGender(RowResult.trimString(result.getD()) == null ? "" : RowResult.trimString(result.getD()));
            owner.setBuilding(RowResult.trimString(result.getE()) == null ? "" : RowResult.trimString(result.getE()));
            owner.setAddress(RowResult.trimString(result.getF()) == null ? "" : RowResult.trimString(result.getF()));
            owner.setLivingStatus(RowResult.trimString(result.getG()) == null ? "" : RowResult.trimString(result.getG()));
            owner.setLivingTime(RowResult.trimString(result.getH()) == null ? "" : RowResult.trimString(result.getH()));
            owner.setBirthday(RowResult.trimString(result.getI()) == null ? "" : RowResult.trimString(result.getI()));
            owner.setMaritalStatus(RowResult.trimString(result.getJ()) == null ? "" : RowResult.trimString(result.getJ()));
            owner.setJob(RowResult.trimString(result.getK()) == null ? "" : RowResult.trimString(result.getK()));
            owner.setCompany(RowResult.trimString(result.getL()) == null ? "" : RowResult.trimString(result.getL()));
            owner.setIdCardNumber(RowResult.trimString(result.getM()) == null ? "" : RowResult.trimString(result.getM()));
            owner.setRegisteredResidence(RowResult.trimString(result.getN()) == null ? "" : RowResult.trimString(result.getN()));

            organizationOwnerDTOS.add(owner);
        }
        return organizationOwnerDTOS;
    }

    private List<OrganizationOwnerCar> processorOrganizationOwnerCar(Long communityId, ArrayList resultList) {
        if (resultList != null && resultList.size() > 0) {
            List<String> plateNumberList = new ArrayList<>();
            List<OrganizationOwnerCar> carList = new ArrayList<>();
            int row = resultList.size();
            for (int rowIndex = 2; rowIndex < row; rowIndex++) {
                RowResult result = (RowResult) resultList.get(rowIndex);
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
//			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT,
//					"The livingStatus %s is invalid.", livingStatus);
            return null;
        }
        return Byte.valueOf(localeString.getCode());
    }

    private Address parseAddress(Integer namespaceId, Long communityId, String building, String apartment) {
    	//issue-32652,在校验门牌是否存在时，应该去查正常的门牌（status为2）
//    	Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, communityId, StringUtils.trimAllWhitespace(building),
//                StringUtils.trimAllWhitespace(apartment));
    	Address address = addressProvider.findActiveAddressByBuildingApartmentName(namespaceId, communityId, StringUtils.trimAllWhitespace(building),
                StringUtils.trimAllWhitespace(apartment));
        if (address == null) {
            String addressText = StringUtils.trimAllWhitespace(building) + "-" + StringUtils.trimAllWhitespace(apartment);
            LOGGER.error("The address {} is not exist.", addressText);
//			throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT_ADDRESS_ERROR,
//					"The address %s is not exist.", addressText);
        }
        return address;
    }

    private Long parseOrgOwnerTypeId(String orgOwnerTypeName) {
        OrganizationOwnerType type = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(orgOwnerTypeName);
        if (type == null) {
//            LOGGER.error("The organization owner type {} is not exist.", orgOwnerTypeName);
//            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_IMPORT,
//                    "The organization owner type %s is not exist.", orgOwnerTypeName);
            return null;
        }
        return type.getId();
    }

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ " + r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }
    }

    @Override
    public GetRequestInfoResponse getRequestInfo(GetRequestInfoCommand cmd) {
        if (StringUtils.isEmpty(cmd.getResourceType()) || cmd.getResourceId() == null || cmd.getRequestorUid() == null) {
            throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER, "Invalid parameter");
        }
        EntityType resourceType = EntityType.fromCode(cmd.getResourceType());
        Long requestId = cmd.getRequestId();  //表示那条记录的id
        if (resourceType == EntityType.ORGANIZATIONS) {
            // requestId 已经无法使用了，组织架构那边改了好多
			/*OrganizationMember organizationMember = organizationProvider.
                    findOrganizationMemberByUIdAndOrgId(cmd.getRequestorUid(), cmd.getResourceId());*/
            OrganizationMember organizationMember = null;
            List<OrganizationMember> organizationMemberList = organizationProvider.findOrganizationMemberByOrgIdAndUIdWithoutAllStatus(cmd.getResourceId(), cmd.getRequestorUid());
            if (organizationMemberList != null) {
                organizationMember = organizationMemberList.get(0);
            }

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getRequestInfo organizationMember {}", organizationMember);
            if (organizationMember != null) {
                return new GetRequestInfoResponse(organizationMember.getStatus());
            }
        } else if (resourceType == EntityType.GROUP || resourceType == EntityType.FAMILY) {
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
                return new GetRequestInfoResponse(groupMemberLog.getMemberStatus(), groupMemberLog.getRejectText());
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getRequestInfo new GetRequestInfoResponse(GroupMemberStatus.INACTIVE.getCode())");
		return new GetRequestInfoResponse(GroupMemberStatus.INACTIVE.getCode());
	}

	@Override
	public void deleteDefaultChargingItem(DeleteDefaultChargingItemCommand cmd) {
		DefaultChargingItem item = findDefaultChargingItem(cmd.getId());
		item.setStatus(CommonStatus.INACTIVE.getCode());
		item.setDeleteUid(UserContext.currentUserId());
		item.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		defaultChargingItemProvider.updateDefaultChargingItem(item);
	}

	private DefaultChargingItem findDefaultChargingItem(Long id) {
		DefaultChargingItem item = defaultChargingItemProvider.findById(id);
		if(item == null || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(item.getStatus()))) {
			LOGGER.error("DefaultChargingItem id: {} is not exist or active!", id);
			throw errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_CHARGING_ITEM_NOT_EXIST,
					"DefaultChargingItem id is not exist or active.");
		}
		return item;
	}

	@Override
	public DefaultChargingItemDTO updateDefaultChargingItem(UpdateDefaultChargingItemCommand cmd) {
		DefaultChargingItem defaultChargingItem = ConvertHelper.convert(cmd, DefaultChargingItem.class);
		if(cmd.getChargingStartTime() != null) {
			defaultChargingItem.setChargingStartTime(new Timestamp(cmd.getChargingStartTime()));
		}
		if(cmd.getChargingExpiredTime() != null) {
			defaultChargingItem.setChargingExpiredTime(new Timestamp(cmd.getChargingExpiredTime()));
		}
		defaultChargingItem.setStatus(CommonStatus.ACTIVE.getCode());
		if(cmd.getId() == null) {
			defaultChargingItemProvider.createDefaultChargingItem(defaultChargingItem);
			dealDefaultChargingItemProperty(defaultChargingItem, cmd.getApartments());
		} else {
			DefaultChargingItem exist = findDefaultChargingItem(cmd.getId());
			defaultChargingItem.setCreateUid(exist.getCreateUid());
			defaultChargingItem.setCreateTime(exist.getCreateTime());
			defaultChargingItemProvider.updateDefaultChargingItem(defaultChargingItem);
			dealDefaultChargingItemProperty(defaultChargingItem, cmd.getApartments());
		}
		return toDefaultChargingItemDTO(defaultChargingItem);
	}

	@Override
	public List<DefaultChargingItemDTO> listDefaultChargingItems(ListDefaultChargingItemsCommand cmd) {
		List<DefaultChargingItem> items = defaultChargingItemProvider.listDefaultChargingItems(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(items != null && items.size() > 0) {
			List<DefaultChargingItemDTO> list = items.stream().map(item -> toDefaultChargingItemDTO(item)).collect(Collectors.toList());
			for(DefaultChargingItemDTO itemDto : list) {
				if(itemDto.getBillGroupId() != null) {
					PaymentBillGroup group = assetGroupProvider.getBillGroupById(itemDto.getBillGroupId());
					if(group != null) {
						itemDto.setBillGroupName(group.getName());
					}
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public void createReservation(CreateReservationCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_CREATE_RESERVATION, cmd.getOrganizationId(), cmd.getCommunityId());
		
		PmResourceReservation resourceReservation = new PmResourceReservation();
		List<PmResourceReservation> existReservations = propertyMgrProvider.findReservationByAddress(cmd.getAddressId(), ReservationStatus.ACTIVE);
		Timestamp start = new Timestamp(Long.valueOf(cmd.getStartTime()));
		Timestamp end = new Timestamp(Long.valueOf(cmd.getEndTime()));
		for(PmResourceReservation r : existReservations){
			if(DateUtil.hasIntersection(r.getStartTime(), r.getEndTime(), start, end)){
				throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_CREATE_RESERVATION_FAILURE, "in this period of time, there had been" +
						"valid reservations exist");
			}
		}
		long nextId = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPmResoucreReservations.class));
		resourceReservation.setId(nextId);
		resourceReservation.setCommunityId(cmd.getCommunityId());
		resourceReservation.setNamespaceId(cmd.getNamespaceId());
		resourceReservation.setCreatorUid(UserContext.currentUserId());
		resourceReservation.setEndTime(end);
		resourceReservation.setStartTime(start);
		resourceReservation.setEnterpriseCustomerId(cmd.getEnterpriseCustomerId());
		resourceReservation.setStatus((byte)2);
		resourceReservation.setAddressId(cmd.getAddressId());
		resourceReservation.setCreateTime(getCurrentTimestamp());
		resourceReservation.setUpdateTime(resourceReservation.getCreateTime());
		//add by tangcen 解决EH_ORGANIZATION_ADDRESS_MAPPINGS表中，一个address_id会对应两条数据的问题，实际应该是脏数据导致的。
		Address address = addressProvider.findAddressById(cmd.getAddressId());
		Byte livingStatus = addressProvider.getAddressLivingStatus(address.getId(),address.getAddress());
        //Byte livingStatus = addressProvider.getAddressLivingStatus(cmd.getAddressId());
        resourceReservation.setPreviousLivingStatus(livingStatus);
//		this.dbProvider.execute((status) -> {

			// living status的完整枚举记录在前端，后端和数据库没有
        	// add by tangcen 
			//int effectedRow = addressProvider.changeAddressLivingStatus(cmd.getAddressId(), AddressLivingStatus.OCCUPIED.getCode());
        	int effectedRow = addressProvider.changeAddressLivingStatus(address.getId(),address.getAddress(), AddressLivingStatus.OCCUPIED.getCode());
			if(effectedRow != 1){
				throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_CREATE_RESERVATION_FAILURE, "address living status change" +
						"result in "+effectedRow+" rows affected, addressid is "+cmd.getAddressId());
			}
			propertyMgrProvider.insertResourceReservation(resourceReservation);
			//添加房源日志
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_ADD,cmd.getAddressId(),resourceReservation,null);
            // 未来1小时内结束的开启一个任务
            if(end.toLocalDateTime().minusHours(1l).isBefore(LocalDateTime.now()) || end.toLocalDateTime().minusHours(1l).equals(LocalDateTime.now())){
                HashMap<String, Object> param = new HashMap<>();
                param.put("addressId", cmd.getAddressId());
                param.put("reservationId", nextId);
                scheduleProvider.scheduleSimpleJob("Address Reservation"+System.currentTimeMillis(),
                        "Address Reservation", Date.from(end.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant())
                        , ReservationJob.class, param);
            }
//			return null;
//		});
        System.out.println("done");
    }

	@Override
	public List<ListReservationsDTO> listReservations(ListReservationsCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_LIST_RESERVATIONS, cmd.getOrganizationId(), cmd.getCommunityId());
		
		List<ListReservationsDTO> ret = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        if(cmd.getAddressId() != null){
        	//find a specific reservation
			ids.add(cmd.getAddressId());
		}else{
			ListPropApartmentsByKeywordCommand temp = new ListPropApartmentsByKeywordCommand();
			temp.setNamespaceId(cmd.getNamespaceId());
			temp.setCommunityId(cmd.getCommunityId());
			temp.setBuildingName(cmd.getBuildingName());
			temp.setOrganizationId(cmd.getOrganizationId());
			//取得门牌列表
			List<ApartmentDTO> aptList = addressService.listApartmentsByKeyword(temp).second();

			//门牌转化成门牌id列表
			List<Long> aptIdList = aptList.stream().map(a->a.getAddressId()).collect(Collectors.toList());

			ids.addAll(aptIdList);
		}
		List<PmResourceReservation> list = propertyMgrProvider.listReservationsByAddresses(ids);
        for(PmResourceReservation r : list){
        	ListReservationsDTO dto = new ListReservationsDTO();
        	dto.setAddressName(addressProvider.getAddressNameById(r.getAddressId()));
        	dto.setAddressId(r.getAddressId());
        	dto.setCreateUname(userProvider.getNickNameByUid(r.getCreatorUid()));
        	dto.setEndTime(r.getEndTime());
        	dto.setStartTime(r.getStartTime());
        	dto.setEnterpriseCustomerName(enterpriseCustomerProvider.getEnterpriseCustomerNameById(r.getEnterpriseCustomerId()));
        	dto.setEnterpriseCustomerId(r.getEnterpriseCustomerId());
        	dto.setReservationId(r.getId());
        	dto.setReservationStatus(r.getStatus());
        	ret.add(dto);
        }
        return ret;
	}

	@Override
	public void updateReservation(UpdateReservationCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_RESERVATION, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Timestamp start = new Timestamp(Long.valueOf(cmd.getStartTime()));
		Timestamp end = new Timestamp(Long.valueOf(cmd.getEndTime()));
		PmResourceReservation oldReservation = propertyMgrProvider.findReservationById(cmd.getReservationId());
		//用于日志记录
		PmResourceReservation oldReservationBackUps = propertyMgrProvider.findReservationById(cmd.getReservationId());
		if (cmd.getAddressId()!=null && !cmd.getAddressId().equals(oldReservation.getAddressId())) {
			oldReservation.setAddressId(cmd.getAddressId());
			//如果修改了预定的地址，则需要将以前的资源释放
			addressProvider.changeAddressLivingStatus(oldReservation.getAddressId(), oldReservation.getPreviousLivingStatus());
    		//修改新的资源的状态
    		Address address = addressProvider.findAddressById(cmd.getAddressId());
    		Byte livingStatus = addressProvider.getAddressLivingStatus(address.getId(),address.getAddress());
    		oldReservation.setPreviousLivingStatus(livingStatus);
    		int effectedRow = addressProvider.changeAddressLivingStatus(address.getId(),address.getAddress(), AddressLivingStatus.OCCUPIED.getCode());
			if(effectedRow != 1){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "address living status change" +
						"result in "+effectedRow+" rows affected, addressid is "+cmd.getAddressId());
			}
		}
//		PmResourceReservation resourceReservation = new PmResourceReservation();
		oldReservation.setStatus((byte)2);
		oldReservation.setEndTime(end);
		oldReservation.setStartTime(start);
		oldReservation.setEnterpriseCustomerId(cmd.getEnterpriseCustomerId());
		oldReservation.setUpdateTime(getCurrentTimestamp());
		propertyMgrProvider.updateReservation(oldReservation);
    	//propertyMgrProvider.updateReservation(cmd.getReservationId(), start, end, cmd.getEnterpriseCustomerId(),cmd.getAddressId());
		//记录房源日志
		if (cmd.getAddressId()!=null && !cmd.getAddressId().equals(oldReservation.getAddressId())) {
			//之前的房源相当于删除了房源预定计划
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_DELETE,oldReservation.getAddressId(),oldReservation,null);
			//新的房源相当于添加了房源预定计划
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_ADD,cmd.getAddressId(),oldReservation,null);
		}else {
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_UPDATE,oldReservation.getAddressId(),oldReservation,oldReservationBackUps);
		}
	}

	/**
	 * delete Reservations and release address
	 * @param cmd contains id of reservation
	 */
	@Override
	public void deleteReservation(DeleteReservationCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_DELETE_RESERVATION, cmd.getOrganizationId(), cmd.getCommunityId());

		this.dbProvider.execute((status) -> {
			Long addressId = propertyMgrProvider.changeReservationStatus(cmd.getReservationId(), ReservationStatus.DELTED.getCode());
			//添加房源日志
			PmResourceReservation resourceReservation = propertyMgrProvider.findReservationById(cmd.getReservationId());
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_DELETE,addressId,resourceReservation,null);
			return null;
		});

	}

	@Override
	public void cancelReservation(CancelReservationCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_CANCEL_RESERVATION, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Byte previousStatus = propertyMgrProvider.getReservationPreviousLivingStatusById(cmd.getReservationId());
		this.dbProvider.execute((status) -> {
			Long addressId = propertyMgrProvider.changeReservationStatus(cmd.getReservationId(), ReservationStatus.INACTIVE.getCode());
			addressProvider.changeAddressLivingStatus(addressId, previousStatus);
			//添加房源日志
			PmResourceReservation resourceReservation = propertyMgrProvider.findReservationById(cmd.getReservationId());
			saveAddressReservationEvent(AddressTrackingTemplateCode.ADDRESS_RESERVATION_CANCEL,addressId,resourceReservation,null);
			return null;
		});
	}

	@Scheduled(cron = "0 0 */1 * * ?")
	private void ReservationExpireCheck(){
        if (scheduleProvider.getRunningFlag() == RunningFlag.FALSE.getCode()) {
            return;
        }
		List<ReservationInfo> list = propertyMgrProvider.listRunningReservations();
		for(ReservationInfo info : list){
			//if it will expire in the future 1 hour
			try{
				LocalDateTime expireTime = info.getEndTime().toLocalDateTime();
				LocalDateTime timing = LocalDateTime.now(ZoneId.systemDefault()).plusHours(1l);
				if(expireTime.isBefore(timing)){
					Map<String,Object> param = new HashMap<String,Object>(){{
						put("addressId", info.getAddressId());
						put("reservationId", info.getReservationId());
					}};
				    scheduleProvider.scheduleSimpleJob("Address Reservation"+System.currentTimeMillis(),
							"Address Reservation", Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant())
							, ReservationJob.class, param);
				}
			}catch (Exception e){
				LOGGER.error("failed to check reservation one time",e);
			}
		}

	}


	private void dealDefaultChargingItemProperty(DefaultChargingItem item, List<DefaultChargingItemPropertyDTO> apartments) {
		List<DefaultChargingItemProperty> existItemProperties = defaultChargingItemProvider.findByItemId(item.getId());
		Map<Long, DefaultChargingItemProperty> map = new HashMap<>();
		if(existItemProperties != null && existItemProperties.size() > 0) {
			existItemProperties.forEach(address -> {
				map.put(address.getId(), address);
			});
		}

        if (apartments != null && apartments.size() > 0) {
            apartments.forEach(itemProperty -> {
                if (itemProperty.getId() == null) {
                    DefaultChargingItemProperty property = ConvertHelper.convert(itemProperty, DefaultChargingItemProperty.class);
                    property.setDefaultChargingItemId(item.getId());
                    property.setNamespaceId(item.getNamespaceId());
                    property.setStatus(CommonStatus.ACTIVE.getCode());
                    defaultChargingItemProvider.createDefaultChargingItemProperty(property);
                } else {
                    map.remove(itemProperty.getId());
                }
            });
        }
        if (map.size() > 0) {
            map.forEach((id, property) -> {
                property.setStatus(CommonStatus.INACTIVE.getCode());
                defaultChargingItemProvider.updateDefaultChargingItemProperty(property);
            });
        }
    }

    private DefaultChargingItemDTO toDefaultChargingItemDTO(DefaultChargingItem item) {
        DefaultChargingItemDTO dto = ConvertHelper.convert(item, DefaultChargingItemDTO.class);
        String itemName = assetProvider.findChargingItemNameById(dto.getChargingItemId());
        dto.setChargingItemName(itemName);
        String standardName = assetProvider.getStandardNameById(dto.getChargingStandardId());
        dto.setChargingStandardName(standardName);
        String lateFeeStandardName = assetProvider.getStandardNameById(dto.getLateFeeStandardId());
        dto.setLateFeeStandardName(lateFeeStandardName);
        String lateFeeformula = assetProvider.findFormulaByChargingStandardId(dto.getLateFeeStandardId());
        dto.setLateFeeformula(lateFeeformula);
        processDefaultChargingItemAddresses(dto);
        return dto;
    }

    private void processDefaultChargingItemAddresses(DefaultChargingItemDTO dto) {
        List<DefaultChargingItemProperty> itemAddresses = defaultChargingItemProvider.findByItemId(dto.getId());
        if (itemAddresses != null && itemAddresses.size() > 0) {
            List<DefaultChargingItemPropertyDTO> addressDtos = new ArrayList<>();
            itemAddresses.forEach(address -> {
                DefaultChargingItemPropertyDTO propertyDTO = ConvertHelper.convert(address, DefaultChargingItemPropertyDTO.class);

                if (propertyDTO.getPropertyType() == DefaultChargingItemPropertyType.COMMUNITY.getCode()) {
                    Community community = communityProvider.findCommunityById(propertyDTO.getPropertyId());
                    if (community != null) {
                        propertyDTO.setPropertyName(community.getName());
                    }
                } else if (propertyDTO.getPropertyType() == DefaultChargingItemPropertyType.BUILDING.getCode()) {
                    Building building = communityProvider.findBuildingById(propertyDTO.getPropertyId());
                    if (building != null) {
                        propertyDTO.setPropertyName(building.getName());
                    }
                } else if (propertyDTO.getPropertyType() == DefaultChargingItemPropertyType.APARTMENT.getCode()) {
                    Address addr = addressProvider.findAddressById(propertyDTO.getPropertyId());
                    if (addr != null) {
                        propertyDTO.setPropertyName(addr.getApartmentName());
                    }
                }
                addressDtos.add(propertyDTO);
            });
            dto.setApartments(addressDtos);
        }
    }

	
	@Override
	public List<ListReservationsDTO> findReservations(FindReservationsCommand cmd) {
		List<ListReservationsDTO> result = new ArrayList<>();

		List<PmResourceReservation> reservations = propertyMgrProvider.findReservationByAddress(cmd.getAddressId(), ReservationStatus.fromCode(cmd.getStatus()));
		for(PmResourceReservation r : reservations){
        	ListReservationsDTO dto = new ListReservationsDTO();
        	dto.setAddressName(addressProvider.getAddressNameById(r.getAddressId()));
        	dto.setAddressId(r.getAddressId());
        	dto.setCreateUname(userProvider.getNickNameByUid(r.getCreatorUid()));
        	dto.setEndTime(r.getEndTime());
        	dto.setStartTime(r.getStartTime());
        	dto.setEnterpriseCustomerName(enterpriseCustomerProvider.getEnterpriseCustomerNameById(r.getEnterpriseCustomerId()));
        	dto.setEnterpriseCustomerId(r.getEnterpriseCustomerId());
        	dto.setReservationId(r.getId());
        	dto.setReservationStatus(r.getStatus());
        	result.add(dto);
        }
		return result;
	}

	//一房一价
	@Override
	public void setAuthorizePrice(AuthorizePriceCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_SET_AUTHORIZE_PRICE, cmd.getOrganizationId(), cmd.getCommunityId());

		if (cmd.getNamespaceId() == null || cmd.getCommunityId() == null) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not exist.");
		}
		AddressProperties addressProperties = ConvertHelper.convert(cmd, AddressProperties.class);
		AddressProperties existProperties = propertyMgrProvider.findAddressPropertiesByApartmentId(community, addressProperties.getBuildingId(), addressProperties.getAddressId());

		if (existProperties != null) {
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE,
					ContractErrorCode.ERROR_ADDRESS_PROPERTIES_IS_EXIST, "AddressProperties is already exist!");
		}

		propertyMgrProvider.createAuthorizePrice(addressProperties);
		//添加房源日志
		saveAddressAuthorizePriceEvent(AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_ADD,cmd.getAddressId(),addressProperties,null);
	}

	@Override
	public ListAuthorizePricesResponse listAuthorizePrices(AuthorizePriceCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_LIST_AUTHORIZE_PRICES, cmd.getOrganizationId(), cmd.getCommunityId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<AddressProperties> list = propertyMgrProvider.listAuthorizePrices(cmd.getNamespaceId(), cmd.getBuildingId(), cmd.getCommunityId(), cmd.getPageAnchor(), pageSize);
		ListAuthorizePricesResponse response = new ListAuthorizePricesResponse();

		if (list.size() > 0) {
			List<AuthorizePriceDTO> resultList = list.stream().map((c) -> {
				AuthorizePriceDTO dto = ConvertHelper.convert(c, AuthorizePriceDTO.class);
				if (dto.getCreatorUid() != null) {
					User user = userProvider.findUserById(dto.getCreatorUid());
					if (user != null) {
						dto.setCreatorName(user.getNickName());
					}
				}
				// 房源名称
				if (dto.getBuildingId() != null && dto.getAddressId() != null) {
					Address address = addressProvider.findAddressById(dto.getAddressId());
					dto.setAddressName(address.getAddress());
				}
				if (dto.getChargingItemsId() != null) {
					String chargingItemName = assetProvider.findChargingItemNameById(dto.getChargingItemsId());
					dto.setChargingItemsName(chargingItemName);
				}

				return dto;
			}).collect(Collectors.toList());
			response.setAuthorizePricesList(resultList);
			if (list.size() != pageSize) {
				response.setNextPageAnchor(null);
			} else {
				response.setNextPageAnchor(list.get(list.size() - 1).getId());
			}
		}
		return response;
	}

	@Override
	public AuthorizePriceDTO authorizePriceDetail(AuthorizePriceCommand cmd) {
		if (cmd.getId() == null || cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}

		AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesById(cmd.getId());
		return ConvertHelper.convert(addressProperties, AuthorizePriceDTO.class);
	}

	@Override
	public void updateAuthorizePrice(AuthorizePriceCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_AUTHORIZE_PRICES, cmd.getOrganizationId(), cmd.getCommunityId());
		
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}

		AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesById(cmd.getId());
		//用于日志记录
		AddressProperties oldAddressProperties = propertyMgrProvider.findAddressPropertiesById(cmd.getId());
		if (cmd.getAddressId() != null && !cmd.getAddressId().equals(addressProperties.getAddressId())) {
			addressProperties.setAddressId(cmd.getAddressId());
		}
		if (cmd.getApartmentAuthorizeType() != null) {
			addressProperties.setApartmentAuthorizeType(cmd.getApartmentAuthorizeType());
		}
		if (cmd.getAuthorizePrice() != null) {
			addressProperties.setAuthorizePrice(cmd.getAuthorizePrice());
		}
		if (cmd.getChargingItemsId() != null) {
			addressProperties.setChargingItemsId(cmd.getChargingItemsId());
		}
		propertyMgrProvider.updateAuthorizePrice(addressProperties);
		//添加房源日志
		if (cmd.getAddressId() != null && !cmd.getAddressId().equals(oldAddressProperties.getAddressId())) {
			//之前的房源相当于删除了房源授权价
			saveAddressAuthorizePriceEvent(AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_DELETE,oldAddressProperties.getAddressId(),addressProperties,null);
			//新的房源相当于添加了房源授权价
			saveAddressAuthorizePriceEvent(AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_ADD,cmd.getAddressId(),addressProperties,null);
		}else {
			//如果addressId没有改变，继续在原address上添加日志
			saveAddressAuthorizePriceEvent(AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_UPDATE,addressProperties.getAddressId(),addressProperties,oldAddressProperties);
		}
	}

	@Override
	public void deleteAuthorizePrice(AuthorizePriceCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_DELETE_AUTHORIZE_PRICES, cmd.getOrganizationId(), cmd.getCommunityId());
		
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}

		AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesById(cmd.getId());
		propertyMgrProvider.deleteAuthorizePrice(addressProperties);
		//添加房源日志
		saveAddressAuthorizePriceEvent(AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_DELETE,addressProperties.getAddressId(),addressProperties,null);
	}

	@Override
	public void exportApartmentsInBuilding(ListPropApartmentsByKeywordCommand cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("namespaceId", cmd.getNamespaceId());
		params.put("communityId", cmd.getCommunityId());
		params.put("UserContext", UserContext.current().getUser());
		params.put("CommandCMD", cmd);

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not exist.");
		}
		String fileName = String.format("一房一价信息_%s", community.getName(),
				com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH)) + ".xlsx";

		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), AuthorizePriceExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	@Override
	public OutputStream exportOutputStreamAuthorizePriceList(ListPropApartmentsByKeywordCommand cmd, Long taskId) {

		List<ApartmentDTO> aptList = addressService.listApartmentsByKeyword(cmd).second();
		taskService.updateTaskProcess(taskId, 40);
		String fileName = null;
		if (cmd.getCommunityId() != null) {
			Community community = communityProvider.findCommunityById(cmd.getCommunityId());
			fileName = String.format("一房一价信息_%s_%s", community.getName(), cmd.getBuildingName());
		} else {
			fileName = String.format("一房一价信息_%s", cmd.getBuildingName());
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		if (aptList != null && aptList.size() > 0) {
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, "一房一价信息");
			taskService.updateTaskProcess(taskId, 60);
			excelUtils = excelUtils.setNeedSequenceColumn(false);
			List<ExportApartmentsAuthorizePriceDTO> data = aptList.stream().map(r -> {
				ExportApartmentsAuthorizePriceDTO dto = ConvertHelper.convert(r, ExportApartmentsAuthorizePriceDTO.class);
				Byte livingStatus = addressProvider.getAddressLivingStatusByAddressId(r.getAddressId());
				dto.setLivingStatus(AddressMappingStatus.fromCode(livingStatus).getDesc());
				AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesByApartmentId(community, cmd.getBuildingId(), r.getAddressId());
				if (addressProperties != null && addressProperties.getApartmentAuthorizeType() != null && addressProperties.getAuthorizePrice() != null && addressProperties.getChargingItemsId() != null) {
					dto.setChargingItemsName(PaymentChargingItemType.fromCode(addressProperties.getChargingItemsId()).getDesc());
					dto.setApartmentAuthorizeType(AuthorizePriceType.fromCode(addressProperties.getApartmentAuthorizeType()).getDesc());
					dto.setAuthorizePrice(addressProperties.getAuthorizePrice());
				}
				return dto;
			}).collect(Collectors.toList());

			List<ExportApartmentsAuthorizePriceDTO> filterData = filterExportApartmentsInBuildingDTO(data, cmd);
			taskService.updateTaskProcess(taskId, 80);
			//动态获取费项名称
			StringBuffer chargingItemName = new StringBuffer();
			String chargingItemNames = "";
			AuthorizePriceCommand chargingItemcmd = new AuthorizePriceCommand();
			chargingItemcmd.setNamespaceId(cmd.getNamespaceId());
			chargingItemcmd.setCommunityId(cmd.getCommunityId());

			List<ListChargingItemsDTO> lists = chargingItemNameList(chargingItemcmd);
			for (int i = 0; i < lists.size(); i++) {
				chargingItemName.append(lists.get(i).getChargingItemName() + ",");
			}
			if (chargingItemName != null && !"".equals(chargingItemName)) {
				chargingItemNames = (chargingItemName.toString()).substring(0, (chargingItemName.toString()).length() - 1);
			} else {
				chargingItemNames = "暂无可选费项，请设置后再试";
			}

			excelUtils.setNeedTitleRemark(true).setTitleRemark(
							"填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n"
							+ "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n"
							+ "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n"
							+ "3、请不要随意复制单元格，这样会破坏字段规则校验。\n"
							+ "4、带有星号（*）的红色字段为必填项。\n"
							+ "5、楼栋名称相同的情况下，门牌不允许重复，重复只算一条。\n"
							+ "6、状态可填项：待租、待售、出租、已售、自用、其他。\n"
							+ "7、费项名称可选项为： "+chargingItemNames+  "。\n"
							+ "8、周期可选项为：按月、按季、按年、按天。\n"
							+ "9、面积只允许填写数字，非数字将导致对应行导入失败\n"
							+ "10、导入系统已存在的门牌，将按照导入的门牌更新系统的门牌信息。\n"
							+ "\n",
					(short) 13, (short) 2500).setNeedSequenceColumn(false).setIsCellStylePureString(true);

			String[] propertyNames = { "apartmentName", "livingStatus", "chargeArea", "chargingItemsName",
					"authorizePrice", "apartmentAuthorizeType", "namespaceAddressType", "namespaceAddressToken" };

			String[] titleNames = { "*房源", "*状态", "收费面积", "费项名称", "授权价", "周期", "第三方来源", "第三方标识" };
			int[] titleSizes = { 20, 20, 20, 20, 20, 20, 20, 20 };

			return excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, filterData);
		} else {
			throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA, "no data");
		}
	}

	private List<ExportApartmentsAuthorizePriceDTO> filterExportApartmentsInBuildingDTO(List<ExportApartmentsAuthorizePriceDTO> data, ListPropApartmentsByKeywordCommand cmd) {
		List<ExportApartmentsAuthorizePriceDTO> filterData = new ArrayList<>();
		for (ExportApartmentsAuthorizePriceDTO dto : data) {
			filterData.add(dto);
			// 按状态筛选
			if (cmd.getLivingStatus() != null
					&& AddressMappingStatus.fromDesc(dto.getLivingStatus()).getCode() != cmd.getLivingStatus()) {
				filterData.remove(dto);
				continue;
			}
			// 按收费面积筛选
			if (cmd.getChargeAreaFrom() != null) {
				if (dto.getChargeArea() == null
						|| dto.getChargeArea().doubleValue() < cmd.getChargeAreaFrom().doubleValue()) {
					filterData.remove(dto);
					continue;
				}
			}
			if (cmd.getChargeAreaTo() != null) {
				if (dto.getChargeArea() == null
						|| dto.getChargeArea().doubleValue() > cmd.getChargeAreaTo().doubleValue()) {
					filterData.remove(dto);
					continue;
				}
			}
		}
		return filterData;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object importAddressAuthorizePriceData(ImportAddressCommand cmd, MultipartFile file) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_IMPORT_AUTHORIZE_PRICES, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			// 解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
			if (null == resultList || resultList.isEmpty()) {
				LOGGER.error("File content is empty.userId=" + userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE,
						OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY, "File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(cmd.getCommunityId());
			task.setType(ImportFileTaskType.APARTMENT.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
				ImportFileResponse response = new ImportFileResponse();
				List<ImportAuthorizePriceDataDTO> datas = handleImportApartmentData(resultList);
				if (datas.size() > 0) {
					// 设置导出报错的结果excel的标题
					response.setTitle(datas.get(0));
					datas.remove(0);
				}
				List<ImportFileResultLog<ImportAuthorizePriceDataDTO>> results = importApartment(datas, userId, cmd);
				response.setTotalCount((long) datas.size());
				response.setFailCount((long) results.size());
				response.setLogs(results);
				return response;
			}, task);

		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return ConvertHelper.convert(task, ImportFileTaskDTO.class);
	}

	private List<ImportFileResultLog<ImportAuthorizePriceDataDTO>> importApartment(List<ImportAuthorizePriceDataDTO> datas, Long userId, ImportAddressCommand cmd) {
		if (null == cmd.getCommunityId() || cmd.getBuildingId() == null) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		Building building = communityProvider.findBuildingById(cmd.getBuildingId());

		List<ImportFileResultLog<ImportAuthorizePriceDataDTO>> errorLogs = new ArrayList<>();
		// 参数校验
		for (ImportAuthorizePriceDataDTO data : datas) {
			ImportFileResultLog<ImportAuthorizePriceDataDTO> log = new ImportFileResultLog<>(AddressServiceErrorCode.SCOPE);

			if (StringUtils.isEmpty(data.getApartmentName())) {
				log.setData(data);
				log.setErrorLog("apartment name is null");
				log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_NAME_EMPTY);
				errorLogs.add(log);
				continue;
			}
			if (StringUtils.isEmpty(data.getStatus())) {
				log.setData(data);
				log.setErrorLog("apartmentStatus is null");
				log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_STATUS_EMPTY);
				errorLogs.add(log);
				continue;
			}

			if (!StringUtils.isEmpty(data.getStatus())) {
				Byte livingStatus = AddressMappingStatus.fromDesc(data.getStatus()).getCode();
				if (livingStatus == -1) {
					log.setData(data);
					log.setErrorLog("apartmentStatus is error");
					log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_STATUS_TYPE);
					errorLogs.add(log);
					continue;
				}
			}
			if (!StringUtils.isEmpty(data.getApartmentAuthorizeType())) {
				Byte apartmentAuthorizeType = AuthorizePriceType.fromDesc(data.getApartmentAuthorizeType()).getCode();
				if (apartmentAuthorizeType == -1) {
					log.setData(data);
					log.setErrorLog("authorizeType is error");
					log.setCode(AddressServiceErrorCode.ERROR_AUTHORIZE_PRICE_TYPE);
					errorLogs.add(log);
					continue;
				}
			}

			BigDecimal chargeArea = BigDecimal.ZERO;
			if (!StringUtils.isEmpty(data.getChargeArea())) {
				try {
					chargeArea = new BigDecimal(data.getChargeArea());
					if (chargeArea.compareTo(BigDecimal.ZERO) == -1) {
						log.setData(data);
						log.setErrorLog("charge area should be greater than zero");
						log.setCode(AddressServiceErrorCode.ERROR_CHARGE_AREA_LESS_THAN_ZERO);
						errorLogs.add(log);
						continue;
					}
				} catch (Exception e) {
					log.setData(data);
					log.setErrorLog("charge area is not number");
					log.setCode(AddressServiceErrorCode.ERROR_CHARGE_AREA_NOT_NUMBER);
					errorLogs.add(log);
					continue;
				}
			}
			// 正则校验数字
			if (!StringUtils.isEmpty(data.getAuthorizePrice())) {
				String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
				if (!Pattern.compile(reg).matcher(data.getAuthorizePrice()).find()) {
					log.setData(data);
					log.setErrorLog("AuthorizePrice format is error");
					log.setCode(AddressServiceErrorCode.ERROR_AUTHORIZE_PRICE_FORMAT);
					errorLogs.add(log);
					continue;
				}
			}
			// 导入房源授权价
			importApartmentAuthorizePrice(community, building, data);
		}
		return errorLogs;
	}

	private void importApartmentAuthorizePrice(Community community, Building building, ImportAuthorizePriceDataDTO data) {
		Long organizationId = findOrganizationByCommunity(community);
		Address address = addressProvider.findActiveAddressByBuildingApartmentName(community.getNamespaceId(), community.getId(), building.getName(), data.getApartmentName());

		Byte chargingItemId = null;
		Byte apartmentAuthorizeType = null;
		// 查询该房源是否已经设置过授权价，并且费项保持一致，一个房源只支持一种费项绑定
		if (!StringUtils.isEmpty(data.getChargingItemsName())) {
			chargingItemId = PaymentChargingItemType.fromDesc(data.getChargingItemsName()).getCode();
		}
		if (!StringUtils.isEmpty(data.getChargingItemsName())) {
			apartmentAuthorizeType = AuthorizePriceType.fromDesc(data.getApartmentAuthorizeType()).getCode();
		}

		AddressProperties addressProperties = propertyMgrProvider.findAddressPropertiesByApartmentId(community, building.getId(), address.getId());

		if (addressProperties == null) {
			addressProperties = new AddressProperties();
			addressProperties.setNamespaceId(community.getNamespaceId());
			addressProperties.setCommunityId(community.getId());
			addressProperties.setBuildingId(building.getId());
			addressProperties.setAddressId(address.getId());
			if (chargingItemId != null) {
				addressProperties.setChargingItemsId((long) chargingItemId);
			}
			if (!StringUtils.isEmpty(data.getAuthorizePrice())) {
				addressProperties.setAuthorizePrice(new BigDecimal(data.getAuthorizePrice()));
			}
			if (apartmentAuthorizeType != null) {
				addressProperties.setApartmentAuthorizeType(apartmentAuthorizeType);
			}

			propertyMgrProvider.createAuthorizePrice(addressProperties);
		} else {
			if (chargingItemId != null) {
				addressProperties.setChargingItemsId((long) chargingItemId);
			}
			if (!StringUtils.isEmpty(data.getAuthorizePrice())) {
				addressProperties.setAuthorizePrice(new BigDecimal(data.getAuthorizePrice()));
			}
			if (apartmentAuthorizeType != null) {
				addressProperties.setApartmentAuthorizeType(apartmentAuthorizeType);
			}
			propertyMgrProvider.updateAuthorizePrice(addressProperties);
		}
	}

	private List<ImportAuthorizePriceDataDTO> handleImportApartmentData(List resultList) {
		List<ImportAuthorizePriceDataDTO> list = new ArrayList<>();
		for (int i = 1; i < resultList.size(); i++) {
			RowResult r = (RowResult) resultList.get(i);
			if (!StringUtils.isEmpty(r.getA()) || !StringUtils.isEmpty(r.getB()) || !StringUtils.isEmpty(r.getC())) {
				ImportAuthorizePriceDataDTO data = new ImportAuthorizePriceDataDTO();
				data.setApartmentName(trim(r.getA()));
				data.setStatus(trim(r.getB()));
				data.setChargeArea(trim(r.getC()));
				data.setChargingItemsName(trim(r.getD()));
				data.setAuthorizePrice(trim(r.getE()));
				data.setApartmentAuthorizeType(trim(r.getF()));
				data.setNamespaceAddressType(trim(r.getG()));
				data.setNamespaceAddressToken(trim(r.getH()));
				list.add(data);
			}
		}
		return list;
	}

	private String trim(String string) {
		if (string != null) {
			return string.trim();
		}
		return "";
	}
	@Override
	public ListSignupInfoByOrganizationIdResponse listApartmentActivity(ListApartmentActivityCommand cmd) {
		ListSignupInfoByOrganizationIdResponse response = new ListSignupInfoByOrganizationIdResponse();

		ListApartmentEnterpriseCustomersCommand cmd2 = new ListApartmentEnterpriseCustomersCommand();
		cmd2.setAddressId(cmd.getAddressId());
		List<EnterpriseCustomerDTO> enterpriseCustomers = addressService.listApartmentEnterpriseCustomers(cmd2);
		if (enterpriseCustomers!=null && enterpriseCustomers.size()>0) {
			EnterpriseCustomerDTO enterpriseCustomer = enterpriseCustomers.get(0);
			response = activityService.listSignupInfoByOrganizationId(enterpriseCustomer.getOrganizationId(), cmd.getNamespaceId(), cmd.getPageAnchor(), cmd.getPageSize());
		}
		return response;
	}

	@Override
	public List<ListChargingItemsDTO> chargingItemNameList(AuthorizePriceCommand cmd) {
		List<ListChargingItemsDTO> result = new ArrayList<>();
		// 1、获取categoryId 列表
		List<AssetServiceModuleAppDTO> dtos = assetService.listAssetModuleApps(cmd.getNamespaceId());
		Map<Long, String> chargingItemNameList = new HashMap<>();

		for (int i = 0; i < dtos.size(); i++) {
			OwnerIdentityCommand ownerIdentityCommand = new OwnerIdentityCommand();
			ownerIdentityCommand.setOwnerId(cmd.getCommunityId());
			ownerIdentityCommand.setOwnerType("community");
			ownerIdentityCommand.setCategoryId(dtos.get(i).getCategoryId());
			List<ListChargingItemsDTO> listChargingItem = assetChargingItemService.listAllChargingItems(ownerIdentityCommand);
			for (int j = 0; j < listChargingItem.size(); j++) {
				if (listChargingItem.get(j).getIsSelected() == 1) {
					chargingItemNameList.put(listChargingItem.get(j).getChargingItemId(),
							listChargingItem.get(j).getChargingItemName());
				}
			}
		}
		if (chargingItemNameList != null) {
			for (Map.Entry<Long, String> map : chargingItemNameList.entrySet()) {
				ListChargingItemsDTO chargingItemsDTO = new ListChargingItemsDTO();
				chargingItemsDTO.setChargingItemId(map.getKey());
				chargingItemsDTO.setChargingItemName(map.getValue());
				result.add(chargingItemsDTO);
			}
		}
		return result;
	}

	public List<ApartmentBriefInfoDTO> listApartmentsInBuilding(ListApartmentsInBuildingCommand cmd) {
		List<Address> addresses = addressProvider.findActiveApartmentsByBuildingId(cmd.getBuildingId());
		List<Long> addressIdList = addresses.stream().map(a->a.getId()).collect(Collectors.toList());
		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(addressIdList);

		List<Address> filterData = new ArrayList<>();
		for(Address address : addresses){
			filterData.add(address);
			if (cmd.getLivingStatus() != null ) {
				CommunityAddressMapping communityAddressMapping = communityAddressMappingMap.get(address.getId());
				if (communityAddressMapping != null && communityAddressMapping.getLivingStatus() != cmd.getLivingStatus()) {
					filterData.remove(address);
				}else if (communityAddressMapping == null) {
					filterData.remove(address);
				}
			}
		}

		List<ApartmentBriefInfoDTO> apartments = filterData.stream().map(a->{
			ApartmentBriefInfoDTO dto = new ApartmentBriefInfoDTO();
			dto.setId(a.getId());
			dto.setApartmentName(a.getApartmentName());
			return dto;
		}).collect(Collectors.toList());

		return apartments;
	}

	@Override
    public OrganizationOwnerDTO getOrgOwnerByContactToken(GetOrgOwnerByContactTokenCommand cmd){
	    CommunityPmOwner owner;
        owner = propertyMgrProvider.findOrganizationOwnerByContactToken(cmd.getContactToken(), cmd.getNamespaceId());
	    if(owner == null || owner.getId() == null){
	        owner = propertyMgrProvider.findOrganizationOwnerByContactExtraTels("\"" + cmd.getContactToken() + "\"", cmd.getNamespaceId());
        }

        if (owner != null) {
            return convertOwnerToDTO(owner);
        } else {
            LOGGER.error("The organization owner contactToken {} is not exist.", cmd.getContactToken());
            throw errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_OWNER_NOT_EXIST,
                    "The organization owner %s is not exist.", cmd.getContactToken());
        }

    }

	private void assetManagementPrivilegeCheck(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.ASSET_MANAGEMENT, null, null, null, communityId);
	}
	
	@Override
	public List<ApartmentEventDTO> listApartmentEvents(ListApartmentEventsCommand cmd) {
		List<AddressEvent> addressEventsList = addressProvider.listAddressEvents(cmd.getAddressId(),null,null);
		//把操作时间（OpearteTime）相同的日志记录内容（Content）都合起来，传给前端展示
		LinkedHashMap<Timestamp, ApartmentEventDTO> map = new LinkedHashMap<>();
		for (AddressEvent addressEvent : addressEventsList) {
			if (map.containsKey(addressEvent.getOperateTime())) {
				ApartmentEventDTO apartmentEventDTO = map.get(addressEvent.getOperateTime());
				String content = apartmentEventDTO.getContent();
				content = content + ";" +addressEvent.getContent();
				apartmentEventDTO.setContent(content);
			}else {
				ApartmentEventDTO dto = ConvertHelper.convert(addressEvent, ApartmentEventDTO.class);
		        if(dto.getOperatorUid() != null) {
		            //用户可能不在组织架构中 所以用nickname
		            User user = userProvider.findUserById(dto.getOperatorUid());
		            if(user != null) {
		                dto.setOperatorName(user.getNickName());
		            }
		        }
				map.put(dto.getOperateTime(), dto);
			}
		}
		List<ApartmentEventDTO> result = new ArrayList<>();
		Set<Entry<Timestamp, ApartmentEventDTO>> entrySet = map.entrySet();
		for (Entry<Timestamp, ApartmentEventDTO> entry : entrySet) {
			result.add(entry.getValue());
		}
		return result;
	}
	
	@Override
	public ListApartmentEventsResponse listApartmentEventsV2(ListApartmentEventsCommand cmd) {
		ListApartmentEventsResponse response = new ListApartmentEventsResponse();
		
		Integer pageSize = cmd.getPageSize();
		if (pageSize == null) {
			pageSize = 10;
		}
		Long pageAnchor = cmd.getPageAnchor();
		if (pageAnchor == null) {
			pageAnchor = 0L;
		}
		
		List<AddressEvent> AddressEventList = addressProvider.listAddressEvents(cmd.getAddressId(),pageSize+1,pageAnchor);
		
		if (AddressEventList.size() > pageSize) {
			AddressEventList.remove(AddressEventList.size()-1);
			response.setNextPageAnchor(pageAnchor + pageSize.longValue());
		}
		
		List<ApartmentEventDTO> results = new ArrayList<>();
		AddressEventList.stream().forEach(r->{
			ApartmentEventDTO dto = ConvertHelper.convert(r, ApartmentEventDTO.class);
	        if(dto.getOperatorUid() != null) {
	            //用户可能不在组织架构中 所以用nickname
	            User user = userProvider.findUserById(dto.getOperatorUid());
	            if(user != null) {
	                dto.setOperatorName(user.getNickName());
	            }
	        }
	        results.add(dto);
		});
		response.setResults(results);
		
		return response;
	}

	//记录房源修改日志
	@Override
	public void saveAddressEvent(int opearteType, Address newAddress, Address oldAddress,Byte newLivingStatus,Byte oldLivingStatus) {	
		String content = null;
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE,3:UPDATE
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_ADD : 
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_ADD ,UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_DELETE :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_DELETE ,UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_UPDATE:
				content = compareAddress(newAddress,oldAddress,newLivingStatus,oldLivingStatus);
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			AddressEvent event = new AddressEvent(); 
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setAddressId(newAddress.getId());	
			event.setStatus(AddressEventStatus.ACTIVE.getCode());
			event.setOperatorUid(UserContext.currentUserId());
			event.setOperateTime(newAddress.getOperateTime());
			event.setOperateType((byte)opearteType);
	        addressProvider.createAddressEvent(event);
		}
	}
	
	//记录房源拆分计划修改日志
	@Override
	public void saveAddressArrangementEvent(int opearteType, Address address,AddressArrangement newArrangement,AddressArrangement oldArrangement) {	
		String content = null;
		Map<String,Object> dataMap = new HashMap<String,Object>();
		//根据不同操作类型获取具体描述
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_ADD : 
				dataMap.put("dateBegin", getDateStr(newArrangement.getDateBegin()));
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_ADD ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_ADD :
				dataMap.put("dateBegin", getDateStr(newArrangement.getDateBegin()));
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_ADD ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_DELETE :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_DELETE ,UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_DELETE :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_DELETE ,UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_UPDATE:
				content = compareAddressArrangement(newArrangement,oldArrangement);
				break;
			case AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_UPDATE:
				content = compareAddressArrangement(newArrangement,oldArrangement);
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			AddressEvent event = new AddressEvent(); 
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setAddressId(address.getId());	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOperateTime(newArrangement.getUpdateTime());
			event.setOperateType((byte)opearteType);
			event.setStatus(AddressEventStatus.ACTIVE.getCode());
	        addressProvider.createAddressEvent(event);
		}
	}
	
	//记录房源拆分计划修改日志
	@Override
	public void saveAddressArrangementEventAboutAddress(int opearteType,AddressArrangement arrangement, Address newAddress, Address oldAddress) {
		String content = null;
		Map<String,Object> dataMap = new HashMap<String,Object>();
		//根据不同操作类型获取具体描述
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_UPDATE : 
				content = compareAddress(newAddress, oldAddress, null, null);
				break;
			case AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_UPDATE :
				content = compareAddress(newAddress, oldAddress, null, null);
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			AddressEvent event = new AddressEvent(); 
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setAddressId(newAddress.getId());	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOperateTime(arrangement.getUpdateTime());
			event.setOperateType((byte)opearteType);
			event.setStatus(AddressEventStatus.ACTIVE.getCode());
	        addressProvider.createAddressEvent(event);
		}
	}
	
	//获取房源拆分计划修改日志内容
	@Override
	public String getAddressArrangementEventContent(int opearteType,AddressArrangement arrangement, Address newAddress, Address oldAddress) {
		String content = null;
		Map<String,Object> dataMap = new HashMap<String,Object>();
		//根据不同操作类型获取具体描述
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_MERGE_ARRANGEMENT_UPDATE : 
				content = compareAddress(newAddress, oldAddress, null, null);
				break;
			case AddressTrackingTemplateCode.ADDRESS_SPLIT_ARRANGEMENT_UPDATE :
				content = compareAddress(newAddress, oldAddress, null, null);
				break;
			default :
				break;
		}
		return content;
	}
	
	//记录一房一价日志
	@Override
	public void saveAddressAuthorizePriceEvent(int opearteType,Long addressId, AddressProperties newAddressProperties, AddressProperties oldAddressProperties) {
		String content = null;
		Map<String,Object> dataMap = new HashMap<String,Object>();
		//根据不同操作类型获取具体描述
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_ADD : 
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_ADD ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_DELETE :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_DELETE ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_AUTHORIZE_PRICE_UPDATE:
				content = compareAddressProperties(newAddressProperties,oldAddressProperties);
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			AddressEvent event = new AddressEvent(); 
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setAddressId(addressId);	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOperateTime(newAddressProperties.getOperatorTime());
			event.setOperateType((byte)opearteType);
			event.setStatus(AddressEventStatus.ACTIVE.getCode());
	        addressProvider.createAddressEvent(event);
		}
	}
	
	//记录房源预定计划日志
	@Override
	public void saveAddressReservationEvent(int opearteType, Long addressId,PmResourceReservation newResourceReservation, PmResourceReservation oldResourceReservation) {
		String content = null;
		Map<String,Object> dataMap = new HashMap<String,Object>();
		//根据不同操作类型获取具体描述
		switch(opearteType){
			case AddressTrackingTemplateCode.ADDRESS_RESERVATION_ADD : 
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_RESERVATION_ADD ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_RESERVATION_DELETE :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_RESERVATION_DELETE ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_RESERVATION_CANCEL :
				content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, AddressTrackingTemplateCode.ADDRESS_RESERVATION_CANCEL ,UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case AddressTrackingTemplateCode.ADDRESS_RESERVATION_UPDATE:
				content = compareReservationEvent(newResourceReservation,oldResourceReservation);
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			AddressEvent event = new AddressEvent(); 
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setAddressId(addressId);	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOperateTime(newResourceReservation.getUpdateTime());
			event.setOperateType((byte)opearteType);
			event.setStatus(AddressEventStatus.ACTIVE.getCode());
	        addressProvider.createAddressEvent(event);
		}
	}
	
	/*
	 * 在各displayName前加前缀（例如：Address_房源状态）是为了区别各个不同业务里的字段
	 */
	
	//对比PmResourceReservation类
	private String compareReservationEvent(PmResourceReservation newResourceReservation,PmResourceReservation oldResourceReservation) {
		StringBuffer buffer = new StringBuffer();
		
		String enterpriseCustomerContent = getAddressEventContent("Reservation_预定客户",newResourceReservation.getEnterpriseCustomerId(),oldResourceReservation.getEnterpriseCustomerId());
		if (enterpriseCustomerContent != null) {
			buffer.append(enterpriseCustomerContent);
			buffer.append(";");
		}
		String startTimeContent = getAddressEventContent("Reservation_预定开始时间",newResourceReservation.getStartTime(),oldResourceReservation.getStartTime());
		if (startTimeContent != null) {
			buffer.append(startTimeContent);
			buffer.append(";");
		}
		String endTimeContent = getAddressEventContent("Reservation_预定结束时间",newResourceReservation.getEndTime(),oldResourceReservation.getEndTime());
		if (endTimeContent != null) {
			buffer.append(endTimeContent);
			buffer.append(";");
		}
		//去除最后一个分号
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
	}

	//对比AddressProperties类
	private String compareAddressProperties(AddressProperties newAddressProperties,AddressProperties oldAddressProperties) {
		StringBuffer buffer = new StringBuffer();
		
		String chargingItemsContent = getAddressEventContent("AuthorizePrice_费项名称",newAddressProperties.getChargingItemsId(),oldAddressProperties.getChargingItemsId());
		if (chargingItemsContent != null) {
			buffer.append(chargingItemsContent);
			buffer.append(";");
		}
		String authorizePriceContent = getAddressEventContent("AuthorizePrice_金额",newAddressProperties.getAuthorizePrice(),oldAddressProperties.getAuthorizePrice());
		if (authorizePriceContent != null) {
			buffer.append(authorizePriceContent.trim());
			buffer.append(";");
		}
		String authorizePriceTypeContent = getAddressEventContent("AuthorizePrice_周期",newAddressProperties.getApartmentAuthorizeType(),oldAddressProperties.getApartmentAuthorizeType());
		if (authorizePriceTypeContent != null) {
			buffer.append(authorizePriceTypeContent);
			buffer.append(";");
		}
		//去除最后一个分号
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
	}

	//对比AddressArrangement类
	private String compareAddressArrangement(AddressArrangement newArrangement,AddressArrangement oldArrangement) {
		StringBuffer buffer = new StringBuffer();
		
		String dateBeginContent = getAddressEventContent("Arrangement_计划生效时间",newArrangement.getDateBegin(),oldArrangement.getDateBegin());
		if (dateBeginContent != null) {
			buffer.append(dateBeginContent);
			buffer.append(";");
		}
		//去除最后一个分号
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
		
	}

	//对比address类，获取改变的信息，只对比“楼宇资产管理/楼宇详情/房源详情”页面上可以修改的参数
	private String compareAddress(Address newAddress, Address oldAddress,Byte newLivingStatus,Byte oldLivingStatus) {
		StringBuffer buffer = new StringBuffer();
		
		String apartmentNameContent = getAddressEventContent("Address_房源名称",newAddress.getApartmentName(),oldAddress.getApartmentName());
		if (apartmentNameContent != null) {
			buffer.append(apartmentNameContent);
			buffer.append(";");
		}
		String livingStatusContent = getAddressEventContent("Address_房源状态",newLivingStatus,oldLivingStatus);
		if (livingStatusContent != null) {
			buffer.append(livingStatusContent);
			buffer.append(";");
		}
		String apartmentFloorContent = getAddressEventContent("Address_楼层",newAddress.getApartmentFloor(),oldAddress.getApartmentFloor());
		if (apartmentFloorContent != null) {
			buffer.append(apartmentFloorContent);
			buffer.append(";");
		}
		String orientationContent = getAddressEventContent("Address_朝向",newAddress.getOrientation(),oldAddress.getOrientation());
		if (orientationContent != null) {
			buffer.append(orientationContent);
			buffer.append(";");
		}
		String areaSizeContent = getAddressEventContent("Address_建筑面积",newAddress.getAreaSize(),oldAddress.getAreaSize());
		if (areaSizeContent != null) {
			buffer.append(areaSizeContent);
			buffer.append(";");
		}
		String rentAreaContent = getAddressEventContent("Address_在租面积",newAddress.getRentArea(),oldAddress.getRentArea());
		if (rentAreaContent != null) {
			buffer.append(rentAreaContent);
			buffer.append(";");
		}
		String freeAreaContent = getAddressEventContent("Address_可招租面积",newAddress.getFreeArea(),oldAddress.getFreeArea());
		if (freeAreaContent != null) {
			buffer.append(freeAreaContent);
			buffer.append(";");
		}
		String chargeAreaContent = getAddressEventContent("Address_收费面积",newAddress.getChargeArea(),oldAddress.getChargeArea());
		if (chargeAreaContent != null) {
			buffer.append(chargeAreaContent);
			buffer.append(";");
		}
		String sharedAreaContent = getAddressEventContent("Address_公摊面积",newAddress.getSharedArea(),oldAddress.getSharedArea());
		if (sharedAreaContent != null) {
			buffer.append(sharedAreaContent);
			buffer.append(";");
		}
		//去除最后一个分号
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
	}
	
	private Timestamp getCurrentTimestamp(){
		return new Timestamp(DateHelper.currentGMTTime().getTime());
	}
	
	private String getDateStr(Timestamp timestamp){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = simpleDateFormat.format(timestamp);
		return format;
	}
	
	private String getAddressEventContent(String displayName,Object newData,Object oldData){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		Object convertedNewData = null;
		Object convertedOldData = null;
		
		if ("Address_房源状态".equals(displayName)) {
			if (newData != null) {
				convertedNewData = AddressMappingStatus.fromCode((Byte)newData).getDesc();
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedOldData = AddressMappingStatus.fromCode((Byte)oldData).getDesc();
			}else {
				convertedOldData = "空";
			}
		}else if ("AuthorizePrice_周期".equals(displayName)) {
			if (newData != null) {
				convertedNewData = ApartmentAuthorizeType.fromCode((Byte)newData).getDesc();
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedOldData = ApartmentAuthorizeType.fromCode((Byte)oldData).getDesc();
			}else {
				convertedOldData = "空";
			}
		}else if ("Arrangement_计划生效时间".equals(displayName)||"Reservation_预定开始时间".equals(displayName)||"Reservation_预定结束时间".equals(displayName)) {
			//处理Timestamp类，转化为“yyyy-MM-dd”字符串
			if (newData != null) {
				convertedNewData = getDateStr((Timestamp) newData);
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedOldData = getDateStr((Timestamp) oldData);
			}else {
				convertedOldData = "空";
			}
		}else if ("AuthorizePrice_费项名称".equals(displayName)) {
			if (newData != null) {
				convertedNewData =  assetProvider.findChargingItemNameById((Long) newData);
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedOldData = assetProvider.findChargingItemNameById((Long) oldData);
			}else {
				convertedOldData = "空";
			}
		}else if ("Reservation_预定客户".equals(displayName)) {
			if (newData != null) {
				convertedNewData = enterpriseCustomerProvider.getEnterpriseCustomerNameById((Long) newData);
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedNewData = enterpriseCustomerProvider.getEnterpriseCustomerNameById((Long) oldData);
			}else {
				convertedOldData = "空";
			}
		}else {
			if (newData != null) {
				convertedNewData = newData;
			}else {
				convertedNewData = "空";
			}
			if (oldData != null) {
				convertedOldData = oldData;
			}else {
				convertedOldData = "空";
			}
		}
		
		if (!convertedNewData.equals(convertedOldData)) {
			dataMap.put("newData", convertedNewData);
			dataMap.put("oldData", convertedOldData);
			//在各displayName中加前缀（例如：Address_房源状态）是为了区别各个不同业务里的字段，显示时还是显示不带前缀的字段
			dataMap.put("display", displayName.split("_")[1]);
			String content = localeTemplateService.getLocaleTemplateString(AddressTrackingTemplateCode.SCOPE, 
					AddressTrackingTemplateCode.ADDRESS_UPDATE ,UserContext.current().getUser().getLocale(), dataMap, "");
			return content;
		}else {
			return null;
		}
	}


}
