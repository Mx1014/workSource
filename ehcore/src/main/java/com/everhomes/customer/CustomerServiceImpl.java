package com.everhomes.customer;

import com.everhomes.acl.AuthorizationRelation;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.acl.RolePrivilegeServiceImpl;
import com.everhomes.activity.ActivityCategories;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.dynamicExcel.DynamicExcelService;
import com.everhomes.dynamicExcel.DynamicExcelStrings;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.entity.EntityType;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.investment.InvitedCustomerService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.openapi.ZJGKOpenServiceImpl;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationAttachment;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.quality.QualityConstant;
import com.everhomes.rentalv2.Rentalv2Service;
import com.everhomes.requisition.RequisitionService;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.activity.ListSignupInfoByOrganizationIdResponse;
import com.everhomes.rest.acl.admin.ListOrganizationContectDTOResponse;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CustomerAptitudeFlag;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.ActivationFlag;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.energy.ListCommnutyRelatedMembersCommand;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.equipment.AdminFlag;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.findScopeFieldItemCommand;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.module.ListServiceModuleAppsAdministratorResponse;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.rentalv2.admin.ListRentalBillsByOrdIdCommand;
import com.everhomes.rest.requisition.GetGeneralFormByCustomerIdCommand;
import com.everhomes.rest.requisition.GetGeneralFormByCustomerIdResponse;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.varField.ModuleName;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.tables.pojos.EhCustomerEntryInfos;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.Field;
import com.everhomes.varField.FieldGroup;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/15.
 */
@Component
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    final String downloadDir = "\\download\\";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Autowired
    private InvitedCustomerService invitedCustomerService;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ZJGKOpenServiceImpl zjgkOpenService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private ActivityProivider activityProivider;

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RequisitionService requisitionService;

    private static final String queueDelay = "trackingPlanTaskDelays";
    private static final String queueNoDelay = "trackingPlanTaskNoDelays";

    @Autowired
    private CommunityService communityService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private SyncDataTaskService syncDataTaskService;

    @Autowired
    private DynamicExcelService dynamicExcelService;

    @Autowired
    private Rentalv2Service rentalv2Service;

    @Autowired
    private YellowPageService yellowPageService;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${equipment.ip}")
    private String equipmentIp;


    @Override
    public void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
//        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
//        cmd.setNamespaceId(namespaceId);
//        cmd.setModuleId(ServiceModuleConstants.ENTERPRISE_CUSTOMER_MODULE);
//        cmd.setActionType(ActionType.OFFICIAL_URL.getCode());
//        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
//        Long appId = apps.getServiceModuleApps().get(0).getId();
//        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
//                orgId, privilegeId, appId, null, communityId)) {
//            LOGGER.error("Permission is prohibited, namespaceId={}, orgId={}, ownerType={}, ownerId={}, privilegeId={}",
//                    namespaceId, orgId, EntityType.COMMUNITY.getCode(), communityId, privilegeId);
//            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
//                    "check user privilege error");
//        }
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.ENTERPRISE_CUSTOMER_MODULE, ActionType.OFFICIAL_URL.getCode(), null, null, communityId);
    }

    private void checkPrivilege(Integer ns) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(ns);
        //产品功能 #20796
//        if(namespaceId == 999971 || namespaceId == 999983) {
//            LOGGER.error("Insufficient privilege");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }
    }

    private void checkEnterpriseCustomerNumberUnique(Long id, Integer namespaceId, Long communityId, String customerNumber, String customerName) {
        if (StringUtils.isNotBlank(customerNumber)) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndNumber(namespaceId, communityId, customerNumber);
            if (customers != null && customers.size() > 0) {
                if (id != null) {
                    for (EnterpriseCustomer customer : customers) {
                        if (id.equals(customer.getId())) {
                            return;
                        }
                    }
                }
                LOGGER.error("customerNumber {} in namespace {} already exist!", customerNumber, namespaceId);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NUMBER_IS_EXIST,
                        "contractNumber is already exist");
            }
        }
        if (StringUtils.isNotBlank(customerName)) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, communityId, customerName);
            if (customers != null && customers.size() > 0) {
                if (id != null) {
                    for (EnterpriseCustomer customer : customers) {
                        if (id.equals(customer.getId())) {
                            return;
                        }
                    }
                }
                LOGGER.error("customerName {} in namespace {} already exist!", customerName, namespaceId);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST,
                        "customerName is already exist");
            }
        }

    }

    @Override
    public SearchEnterpriseCustomerResponse queryEnterpriseCustomers(SearchEnterpriseCustomerCommand cmd) {
        cmd.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_LIST, cmd.getOrgId(), cmd.getCommunityId());
        Boolean isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        if(cmd.getCustomerIds() != null && cmd.getCustomerIds().size() > 0){
            return enterpriseCustomerSearcher.queryEnterpriseCustomersById(cmd);
        }else{
            return enterpriseCustomerSearcher.queryEnterpriseCustomers(cmd, isAdmin);

        }
    }
    @Override
    public Boolean checkCustomerAdmin(Long ownerId, String ownerType, Integer namespaceId) {
        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(namespaceId);
        listServiceModuleAppsCommand.setModuleId(QualityConstant.CUSTOMER_MODULE);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
        checkModuleManageCommand.setModuleId(QualityConstant.CUSTOMER_MODULE);
        checkModuleManageCommand.setOrganizationId(ownerId);
        checkModuleManageCommand.setOwnerType(ownerType);
        checkModuleManageCommand.setUserId(UserContext.currentUserId());
        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
            checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
        }
        return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
    }

    @Override
    public ListCommunitySyncResultResponse listCommunitySyncResult(ListCommunitySyncResultCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListCommunitySyncResultResponse response = syncDataTaskService.listCommunitySyncResult(cmd.getCommunityId(), cmd.getSyncType(), pageSize, cmd.getPageAnchor());
        return response;
    }

    @Override
    public String syncResultViewed(SyncResultViewedCommand cmd) {
        return syncDataTaskService.syncHasViewed(cmd.getCommunityId(), cmd.getSyncType());
    }

    @Override
    public void exportEnterpriseCustomer(ExportEnterpriseCustomerCommand cmd, HttpServletResponse response) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_EXPORT, cmd.getOrgId(), cmd.getCommunityId());
        ExportFieldsExcelCommand command = ConvertHelper.convert(cmd, ExportFieldsExcelCommand.class);
//        command.setIncludedGroupIds("10,11,12");
        List<FieldGroupDTO> results = fieldService.getAllGroups(command, false, true);
        if (results != null && results.size() > 0) {
            List<String> sheetNames = results.stream().map((r)->r.getGroupId().toString()).collect(Collectors.toList());
            dynamicExcelService.exportDynamicExcel(response, DynamicExcelStrings.CUSTOEMR, null, sheetNames, cmd, true, true, null);
        }
    }

    @Override
    public OutputStream exportEnterpriseCustomer(ExportEnterpriseCustomerCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_EXPORT, cmd.getOrgId(), cmd.getCommunityId());
        ExportFieldsExcelCommand command = ConvertHelper.convert(cmd, ExportFieldsExcelCommand.class);
//        command.setIncludedGroupIds("10,11,12");
        List<FieldGroupDTO> results = fieldService.getAllGroups(command, false, true);
        results.forEach(r -> {
            if(r.getGroupDisplayName().equals("客户账单") || r.getGroupDisplayName().equals("活动记录")){
                results.remove(r);
            }
        });
        if (results != null && results.size() > 0) {
            List<String> sheetNames = results.stream().map((r)->r.getGroupId().toString()).collect(Collectors.toList());
            return dynamicExcelService.exportDynamicExcel( DynamicExcelStrings.CUSTOEMR, null, sheetNames, cmd, true, true, null);
        }
        return null;
    }

    @Override
    public void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response) {
        List<String> sheetNames = new ArrayList<>();
        sheetNames.add("客户信息");
        String excelTemplateName = "客户模板" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(Calendar.getInstance().getTime()) + ".xls";
        Boolean isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        cmd.setIsAdmin(isAdmin);
        dynamicExcelService.exportDynamicExcel(response, DynamicExcelStrings.CUSTOEMR, null, sheetNames, cmd, true, false, excelTemplateName);
    }

    @Override
    public EnterpriseCustomerDTO createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd) {
        checkPrivilege(cmd.getNamespaceId());
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        checkEnterpriseCustomerNumberUnique(null, cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCustomerNumber(), cmd.getName());
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setOwnerId(cmd.getOrgId());
        customer.setNamespaceId((null != cmd.getNamespaceId() ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId()));
        if (cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        if (cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        customer.setCreatorUid(UserContext.currentUserId());
        if (null != customer.getLongitude() && null != customer.getLatitude()) {
            String geohash = GeoHashUtils.encode(customer.getLatitude(), customer.getLongitude());
            customer.setGeohash(geohash);
        }
        if (customer.getTrackingUid() != null) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(customer.getTrackingUid());
            if (null != detail && null != detail.getContactName()) {
                customer.setTrackingName(detail.getContactName());
            }else {
                User user = userProvider.findUserById(customer.getTrackingUid());
                if(user!=null)
                customer.setTrackingName(user.getNickName());
            }
        }
        customer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
        customer.setLevelItemId((long)CustomerLevelType.REGISTERED_CUSTOMER.getCode());
        if(customer.getAptitudeFlagItemId() == null){
            customer.setAptitudeFlagItemId((long)CustomerAptitudeFlag.NOAPTITUDE.getCode());
        }
//        customer.setSourceId(cmd.getSourceItemId());
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);
        //创建或更新customer的bannerUri
        enterpriseCustomerProvider.updateEnterpriseBannerUri(customer.getId(), cmd.getBanner());
        // 创建附件
        if (cmd.getAttachments() != null && cmd.getAttachments().size() > 0) {
            cmd.getAttachments().forEach((c) -> {
                CustomerAttachment attachment = ConvertHelper.convert(c, CustomerAttachment.class);
                attachment.setCustomerId(customer.getId());
                attachment.setNamespaceId(customer.getNamespaceId());
                enterpriseCustomerProvider.createCustomerAttachements(attachment);
            });
        }
        if(customer.getLevelItemId() != null){
            invitedCustomerService.createCustomerLevelByCustomerId(customer.getId(), customer.getLevelItemId());
        }

        //企业客户新增成功,保存客户事件
        saveCustomerEvent( 1  ,customer ,null,cmd.getDeviceType());
        //add potential data transfer to customer
        if (cmd.getSourceId() != null) {
            saveCustomerTransferEvent(UserContext.currentUserId(), customer, cmd.getSourceId(), cmd.getDeviceType());
        }
        syncPotentialTalentToCustomer(customer.getId(),cmd.getSourceId());
        enterpriseCustomerSearcher.feedDoc(customer);
        // add entry infos for asset adding customer
        if (cmd.getEntryInfos() != null) {
            CreateCustomerEntryInfoCommand entryInfoCommand = cmd.getEntryInfos();
            entryInfoCommand.setCheckAuth(false);
            entryInfoCommand.setCustomerId(customer.getId());
            createCustomerEntryInfo(cmd.getEntryInfos());
        }
        return ConvertHelper.convert(customer,EnterpriseCustomerDTO.class);
    }

    @Override
    public EnterpriseCustomerDTO createEnterpriseCustomerOutAuth(CreateEnterpriseCustomerCommand cmd) {
        checkEnterpriseCustomerNumberUnique(null, cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCustomerNumber(), cmd.getName());
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setNamespaceId((null != cmd.getNamespaceId() ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId()));
        if (cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        if(cmd.getExpectedSignDate() != null){
            customer.setExpectedSignDate(new Timestamp(cmd.getExpectedSignDate()));
        }
        if (cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        customer.setCreatorUid(UserContext.currentUserId());
        if (null != customer.getLongitude() && null != customer.getLatitude()) {
            String geohash = GeoHashUtils.encode(customer.getLatitude(), customer.getLongitude());
            customer.setGeohash(geohash);
        }
        if (customer.getTrackingUid() != null) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(customer.getTrackingUid());
            if (null != detail && null != detail.getContactName()) {
                customer.setTrackingName(detail.getContactName());
            }else {
                User user = userProvider.findUserById(customer.getTrackingUid());
                if(user!=null)
                    customer.setTrackingName(user.getNickName());
            }
        }
        if(customer.getAptitudeFlagItemId() == null){
            customer.setAptitudeFlagItemId((long)CustomerAptitudeFlag.NOAPTITUDE.getCode());
        }
//        customer.setSourceId(cmd.getSourceItemId());
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);
        //创建或更新customer的bannerUri
        enterpriseCustomerProvider.updateEnterpriseBannerUri(customer.getId(), cmd.getBanner());
        // 创建附件
        if (cmd.getAttachments() != null && cmd.getAttachments().size() > 0) {
            cmd.getAttachments().forEach((c) -> {
                CustomerAttachment attachment = ConvertHelper.convert(c, CustomerAttachment.class);
                attachment.setCustomerId(customer.getId());
                attachment.setNamespaceId(customer.getNamespaceId());
                enterpriseCustomerProvider.createCustomerAttachements(attachment);
            });
        }

        //企业客户新增成功,保存客户事件
        saveCustomerEvent( 1  ,customer ,null,cmd.getDeviceType());
        //add potential data transfer to customer
        if (cmd.getSourceId() != null) {
            saveCustomerTransferEvent(UserContext.currentUserId(), customer, cmd.getSourceId(), cmd.getDeviceType());
        }
        if(customer.getLevelItemId() != null){
            invitedCustomerService.createCustomerLevelByCustomerId(customer.getId(), customer.getLevelItemId());
        }
        syncPotentialTalentToCustomer(customer.getId(),cmd.getSourceId());
        enterpriseCustomerSearcher.feedDoc(customer);
        return convertToDTO(customer);
    }

    private void saveCustomerTransferEvent(Long uid, EnterpriseCustomer customer,Long sourceId,byte deeviceType) {
        CustomerTalent potentialData = enterpriseCustomerProvider.findPotentialCustomerById(sourceId);
        if (potentialData != null) {
            Map<String, String> eventMap = new HashMap<>();
            User user = userProvider.findUserById(uid);
            if (user != null) {
                eventMap.put("operatorName", user.getNickName());
                eventMap.put("time", dateTimeFormatter.format(LocalDateTime.now()));
                eventMap.put("customerName", potentialData.getName());
                String content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.TRANSFER, UserContext.current().getUser().getLocale(), eventMap, "");
                if (StringUtils.isNotEmpty(content)) {
                    CustomerEvent event = new CustomerEvent();
                    event.setCustomerType(CustomerType.ENTERPRISE.getCode());
                    event.setCustomerId(customer.getId());
                    event.setCustomerName(customer.getName());
                    event.setContactName(customer.getContactName());
                    event.setContent(content);
                    event.setCreatorUid(UserContext.currentUserId());
                    event.setNamespaceId(UserContext.getCurrentNamespaceId());
                    event.setDeviceType(deeviceType);
                    enterpriseCustomerProvider.createCustomerEvent(event);
                }
            }
        }
    }

    private void syncPotentialTalentToCustomer(Long customerId, Long sourceId) {
        if (sourceId != null) {
            // source id is potential customer data primary key
            enterpriseCustomerProvider.updatePotentialTalentsToCustomer(customerId, sourceId);
            // delete origin source potential data
            enterpriseCustomerProvider.deletePotentialCustomer(sourceId);
        }
    }

    private EnterpriseCustomerDTO convertToDTO(EnterpriseCustomer customer) {
        //数据库冗余又从动态表单查的 又要删除不显示  暂时这样
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
//        ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
        ScopeFieldItem categoryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), customer.getCategoryItemId());
        if (categoryItem != null) {
            dto.setCategoryItemName(categoryItem.getItemDisplayName());
        }else {
            dto.setCategoryItemName(null);
        }
//        ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
        ScopeFieldItem levelItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), customer.getLevelItemId());
        if (levelItem != null) {
            dto.setLevelItemName(levelItem.getItemDisplayName());
        }else {
            dto.setLevelItemName(null);
        }
        if (null != dto.getCorpIndustryItemId()) {
            ScopeFieldItem corpIndustryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getCorpIndustryItemId());
            if (null != corpIndustryItem) {
                dto.setCorpIndustryItemName(corpIndustryItem.getItemDisplayName());
            }else {
                dto.setCorpIndustryItemName(null);
            }
        }
        if (null != dto.getSourceItemId()) {
            ScopeFieldItem sourceItemId = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getSourceItemId());
            if (null != sourceItemId) {
                dto.setSourceItemName(sourceItemId.getItemDisplayName());
            }else {
                dto.setSourceItemName(null);
            }
        }
        if (null != dto.getContactGenderItemId()) {
            ScopeFieldItem contactGenderItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getContactGenderItemId());
            if (null != contactGenderItem) {
                dto.setContactGenderItemName(contactGenderItem.getItemDisplayName());
            }else {
                dto.setContactGenderItemName(null);
            }
        }
        if (dto.getTrackingUid() != null && dto.getTrackingUid() != -1) {
            dto.setTrackingName(dto.getTrackingName());
        }
        if (null != dto.getPropertyType()) {
            ScopeFieldItem propertyTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getPropertyType());
            if (null != propertyTypeItem) {
                dto.setPropertyTypeName(propertyTypeItem.getItemDisplayName());
            }else{
                dto.setPropertyTypeName(null);
            }
        }

        if (null != dto.getRegistrationTypeId()) {
            ScopeFieldItem registrationTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getRegistrationTypeId());
            if (null != registrationTypeItem) {
                dto.setRegistrationTypeName(registrationTypeItem.getItemDisplayName());
            }else {
                dto.setRegistrationTypeName(null);
            }
        }

        if (null != dto.getTechnicalFieldId()) {
            ScopeFieldItem technicalFieldItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getTechnicalFieldId());
            if (null != technicalFieldItem) {
                dto.setTechnicalFieldName(technicalFieldItem.getItemDisplayName());
            }else {
                dto.setTechnicalFieldName(null);
            }
        }

        if (null != dto.getTaxpayerTypeId()) {
            ScopeFieldItem taxpayerTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getTaxpayerTypeId());
            if (null != taxpayerTypeItem) {
                dto.setTaxpayerTypeName(taxpayerTypeItem.getItemDisplayName());
            }else {
                dto.setTaxpayerTypeName(null);
            }
        }

        if (null != dto.getRelationWillingId()) {
            ScopeFieldItem relationWillingItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getRelationWillingId());
            if (null != relationWillingItem) {
                dto.setRelationWillingName(relationWillingItem.getItemDisplayName());
            }else {
                dto.setRelationWillingName(null);
            }
        }

        if (null != dto.getHighAndNewTechId()) {
            ScopeFieldItem highAndNewTechItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getHighAndNewTechId());
            if (null != highAndNewTechItem) {
                dto.setHighAndNewTechName(highAndNewTechItem.getItemDisplayName());
            }else {
                dto.setHighAndNewTechName(null);
            }
        }

        if (null != dto.getEntrepreneurialCharacteristicsId()) {
            ScopeFieldItem entrepreneurialCharacteristicsItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getEntrepreneurialCharacteristicsId());
            if (null != entrepreneurialCharacteristicsItem) {
                dto.setEntrepreneurialCharacteristicsName(entrepreneurialCharacteristicsItem.getItemDisplayName());
            }else {
                dto.setEntrepreneurialCharacteristicsName(null);
            }
        }

        if (null != dto.getSerialEntrepreneurId()) {
            ScopeFieldItem serialEntrepreneurItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getSerialEntrepreneurId());
            if (null != serialEntrepreneurItem) {
                dto.setSerialEntrepreneurName(serialEntrepreneurItem.getItemDisplayName());
            }else {
                dto.setSerialEntrepreneurName(null);
            }
        }

        if (null != dto.getBuyOrLeaseItemId()) {
            ScopeFieldItem buyOrLeaseItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getBuyOrLeaseItemId());
            if (null != buyOrLeaseItem) {
                dto.setBuyOrLeaseItemName(buyOrLeaseItem.getItemDisplayName());
            } else {
                dto.setBuyOrLeaseItemName(null);
            }
        }

        if (null != dto.getFinancingDemandItemId()) {
            ScopeFieldItem financingDemandItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getFinancingDemandItemId());
            if (null != financingDemandItem) {
                dto.setFinancingDemandItemName(financingDemandItem.getItemDisplayName());
            } else {
                dto.setSerialEntrepreneurName(null);
            }
        }

        if (null != dto.getDropBox1ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox1ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox1ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox1ItemName(null);
            }
        }

        if (null != dto.getDropBox2ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox2ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox2ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox2ItemName(null);
            }
        }

        if (null != dto.getDropBox3ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox3ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox3ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox3ItemName(null);
            }
        }

        if (null != dto.getDropBox4ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox4ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox4ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox4ItemName(null);
            }
        }

        if (null != dto.getDropBox5ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox5ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox5ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox5ItemName(null);
            }
        }

        if (null != dto.getDropBox6ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox6ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox6ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox6ItemName(null);
            }
        }

        if (null != dto.getDropBox7ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox7ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox7ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox7ItemName(null);
            }
        }

        if (null != dto.getDropBox8ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox8ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox8ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox8ItemName(null);
            }
        }

        if (null != dto.getDropBox9ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox9ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox9ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox9ItemName(null);
            }
        }

        if (null != dto.getEntryStatusItemId()) {
            ScopeFieldItem itemId = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getEntryStatusItemId());
            if (null != itemId) {
                dto.setEntryStatusItemName(itemId.getItemDisplayName());
            }else {
                dto.setEntryStatusItemName(null);
            }
        }

        if(customer.getExpectedSignDate() != null){
            dto.setExpectedSignDate(customer.getExpectedSignDate().getTime());
        }

        if (null != dto.getAptitudeFlagItemId()) {
            findScopeFieldItemCommand cmd = new findScopeFieldItemCommand();
            cmd.setNamespaceId(customer.getNamespaceId());
            cmd.setBusinessValue(Byte.valueOf(dto.getAptitudeFlagItemId().toString()));
            cmd.setModuleName("enterprise_customer");
            cmd.setCommunityId(customer.getCommunityId());
            Field field = fieldProvider.findField(10L, "aptitudeFlagItemId");
            if (field != null)
                cmd.setFieldId(field.getId());
            FieldItemDTO aptitudeFlag = equipmentService.findScopeFieldItemByFieldItemId(cmd);

            if (null != aptitudeFlag) {
                dto.setAptitudeFlagItemName(aptitudeFlag.getItemDisplayName());
            } else {
                dto.setAptitudeFlagItemName(null);
            }
        }
        /*if (null != dto.getAptitudeFlagItemId()) {
            ScopeFieldItem aptitudeFlag = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), dto.getAptitudeFlagItemId());
            if (null != aptitudeFlag) {
                dto.setAptitudeFlagItemName(aptitudeFlag.getItemDisplayName());
            } else {
                dto.setAptitudeFlagItemName(null);
            }
        }*/

        //21002 企业管理1.4（来源于第三方数据，企业名称栏为灰色不可修改） add by xiongying20171219
        if (!StringUtils.isEmpty(customer.getNamespaceCustomerType())) {
            dto.setThirdPartFlag(true);
        }
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(customer.getTrackingUid());
        if (members != null && members.size()>0) {
            dto.setTrackingPhone(members.get(0).getContactToken());
        }
        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
        command.setOrganizationId(customer.getOrganizationId());
        command.setCustomerId(customer.getId());
        command.setNamespaceId(UserContext.getCurrentNamespaceId());
        command.setCommunityId(customer.getCommunityId());
        List<OrganizationContactDTO> admins = listOrganizationAdmin(command);
        dto.setEnterpriseAdmins(admins);
        //楼栋门牌
        ListCustomerEntryInfosCommand command1 = new ListCustomerEntryInfosCommand();
        command1.setCommunityId(customer.getCommunityId());
        command1.setCustomerId(customer.getId());
        List<CustomerEntryInfoDTO> entryInfos = listCustomerEntryInfosWithoutAuth(command1);
        dto.setEntryInfos(entryInfos);
        //增加注册人数he !
        if(customer.getOrganizationId()!=null && customer.getOrganizationId()!=0L){
            dto.setSignedUpCount(organizationProvider.getSignupCount(customer.getOrganizationId()));
        }
        return dto;
    }

    @Override
    public OrganizationDTO createOrganization(EnterpriseCustomer customer) {
        Organization org = organizationProvider.findOrganizationByName(customer.getName(), customer.getNamespaceId());
        if (org != null && OrganizationStatus.ACTIVE.equals(OrganizationStatus.fromCode(org.getStatus()))) {
            //已存在则更新 地址、官网地址、企业logo   postUri  hotline bannerUri
            org.setStringTag1(customer.getCorpEmail());
            org.setEmailDomain(customer.getCorpEmail());
            org.setWebsite(customer.getCorpWebsite());
            org.setUnifiedSocialCreditCode(customer.getUnifiedSocialCreditCode());
            organizationProvider.updateOrganization(org);
            OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(org.getId());
            if (detail != null) {
                detail.setMemberCount(customer.getCorpEmployeeAmount() == null ? null : customer.getCorpEmployeeAmount().longValue());
                detail.setStringTag1(customer.getCorpEmail());
                detail.setEmailDomain(customer.getCorpEmail());
                detail.setDescription(customer.getCorpDescription());
                detail.setCheckinDate(customer.getCorpEntryDate());
                detail.setPostUri(customer.getPostUri());
                detail.setDisplayName(customer.getNickName());
                detail.setAvatar(customer.getCorpLogoUri());
                detail.setAddress(customer.getContactAddress());
                detail.setLatitude(customer.getLatitude());
                detail.setLongitude(customer.getLongitude());
                detail.setPostUri(customer.getPostUri());
                detail.setContact(customer.getHotline());
                organizationProvider.updateOrganizationDetail(detail);
            } else {
                detail = new OrganizationDetail();
                detail.setOrganizationId(org.getId());
                detail.setMemberCount(customer.getCorpEmployeeAmount() == null ? null : customer.getCorpEmployeeAmount().longValue());
                detail.setStringTag1(customer.getCorpEmail());
                detail.setDescription(customer.getCorpDescription());
                detail.setCheckinDate(customer.getCorpEntryDate());
                detail.setPostUri(customer.getPostUri());
                detail.setDisplayName(customer.getNickName());
                detail.setAvatar(customer.getCorpLogoUri());
                detail.setAddress(customer.getContactAddress());
                detail.setLatitude(customer.getLatitude());
                detail.setLongitude(customer.getLongitude());
                detail.setPostUri(customer.getPostUri());
                detail.setContact(customer.getHotline());
                organizationProvider.createOrganizationDetail(detail);
            }
            addAttachments(org.getId(),customer.getBanner(),UserContext.currentUserId());
            return ConvertHelper.convert(org, OrganizationDTO.class);
        }else {
            // 企业管理中无该organization  则新建一个
            CreateEnterpriseCommand command = new CreateEnterpriseCommand();
            command.setName(customer.getName());
            command.setEmailDomain(customer.getCorpEmail());
            command.setNamespaceId(customer.getNamespaceId());
            command.setAvatar(customer.getCorpLogoUri());
            command.setCommunityId(customer.getCommunityId());
            command.setAddress(customer.getContactAddress());
            if (customer.getLatitude() != null) {
                command.setLatitude(customer.getLatitude().toString());
            }
            if (customer.getLongitude() != null) {
                command.setLongitude(customer.getLongitude().toString());
            }
            command.setWebsite(customer.getCorpWebsite());
            command.setContactsPhone(customer.getHotline());
            OrganizationDTO dto =  organizationService.createEnterprise(command);
            addAttachments(dto.getId(),customer.getBanner(),UserContext.currentUserId());
            return dto;
        }
    }

    /**
     * 添加banner图片
     *
     * @param id organizationId
     * @param attachments   附件
     */
    private void addAttachments(Long id, List<AttachmentDescriptor> attachments, Long userId) {
        dbProvider.execute((TransactionStatus status) -> {
            this.organizationProvider.deleteOrganizationAttachmentsByOrganizationId(id);
            if (attachments != null && attachments.size() > 0) {
                for (AttachmentDescriptor attachmentDescriptor : attachments) {
                    OrganizationAttachment attachment = ConvertHelper.convert(attachmentDescriptor, OrganizationAttachment.class);
                    attachment.setCreatorUid(userId);
                    attachment.setOrganizationId(id);
                    attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.organizationProvider.createOrganizationAttachment(attachment);
                }
            }
            return null;
        });
    }

    @Override
    public EnterpriseCustomerDTO updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        //checkPrivilege(customer.getNamespaceId());
        if (cmd.getCheckAuthFlag() != null) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        }
        //产品功能 #20796 同步过来的客户名称不可改
        if (NamespaceCustomerType.EBEI.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))
                || NamespaceCustomerType.SHENZHOU.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))) {
            if (!customer.getName().equals(cmd.getName())) {
                LOGGER.error("Insufficient privilege");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                        "Insufficient privilege");
            }
        }
        checkEnterpriseCustomerNumberUnique(customer.getId(), customer.getNamespaceId(), cmd.getCommunityId(), cmd.getCustomerNumber(), cmd.getName());
        EnterpriseCustomer updateCustomer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);

        updateCustomer.setNamespaceId(customer.getNamespaceId());
        updateCustomer.setOwnerId(cmd.getOrgId());
        updateCustomer.setCommunityId(customer.getCommunityId());
        if(cmd.getTransCommunityId() != null && cmd.getTransCommunityId() != 0){
            updateCustomer.setCommunityId(cmd.getTransCommunityId());
        }
        updateCustomer.setOrganizationId(customer.getOrganizationId());
        updateCustomer.setCreateTime(customer.getCreateTime());
        updateCustomer.setCreatorUid(customer.getCreatorUid());
        updateCustomer.setNamespaceCustomerToken(customer.getNamespaceCustomerToken());
        updateCustomer.setNamespaceCustomerType(customer.getNamespaceCustomerType());
        updateCustomer.setLastTrackingTime(customer.getLastTrackingTime());
        updateCustomer.setAdminFlag(customer.getAdminFlag());
        updateCustomer.setVersion(customer.getVersion());
        updateCustomer.setSourceId(customer.getSourceId());
        updateCustomer.setSourceType(customer.getSourceType());

        if(cmd.getCustomerSource() != null){
            updateCustomer.setCustomerSource(cmd.getCustomerSource());
        }else{
            updateCustomer.setCustomerSource(customer.getCustomerSource());
        }
        if (cmd.getCorpEntryDate() != null) {
            updateCustomer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }

        if (cmd.getFoundingTime() != null) {
            updateCustomer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        if (cmd.getExpectedSignDate() != null) {
            updateCustomer.setExpectedSignDate(new Timestamp(cmd.getExpectedSignDate()));
        }
        updateCustomer.setStatus(CommonStatus.ACTIVE.getCode());
        //保存经纬度
        if (null != updateCustomer.getLongitude() && null != updateCustomer.getLatitude()) {
            String geohash = GeoHashUtils.encode(updateCustomer.getLatitude(), updateCustomer.getLongitude());
            updateCustomer.setGeohash(geohash);
        }
        if (updateCustomer.getTrackingUid() != null && updateCustomer.getTrackingUid() != 0) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(updateCustomer.getTrackingUid());
            if (null != detail && null != detail.getContactName()) {
                updateCustomer.setTrackingName(detail.getContactName());
            }
        }
        if (updateCustomer.getTrackingUid() == null) {
            updateCustomer.setTrackingUid(null);
        }
        if (updateCustomer.getTrackingUid() == null) {
            updateCustomer.setTrackingName(null);
        }
        if(updateCustomer.getLevelItemId() != null){
            invitedCustomerService.changeCustomerLevelByCustomerId(updateCustomer.getId(), updateCustomer.getLevelItemId());
        }

        enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);
        enterpriseCustomerSearcher.feedDoc(updateCustomer);

        // 判定跟进人是否进行了改变，如果改变了，那么应当在update操作完成后，去通知新的跟进人 by wentian at 2018/6/5
        Long originalTrackingUid = customer.getTrackingUid();
        String originalTrackingName = customer.getTrackingName();
        Long currentTrackingUid = updateCustomer.getTrackingUid();
        String currentTrackingName = updateCustomer.getTrackingName();
        routeMsgForTrackingChanged(originalTrackingUid, currentTrackingUid, originalTrackingName, currentTrackingName, customer.getName(), null, false,cmd.getOrgId());

        //创建或更新customer的bannerUri
        enterpriseCustomerProvider.updateEnterpriseBannerUri(customer.getId(), cmd.getBanner());

        //保存之后重查一遍 因为数据类型导致 fix 22978
        updateCustomer = checkEnterpriseCustomer(cmd.getId());
        //保存客户事件
        saveCustomerEvent(3, updateCustomer, customer,cmd.getDeviceType(), cmd.getModuleName());

        if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            Organization org = organizationProvider.findOrganizationById(updateCustomer.getOrganizationId());
            if (org != null) {
                UpdateEnterpriseCommand command = new UpdateEnterpriseCommand();
                command.setId(updateCustomer.getOrganizationId());
                command.setName(updateCustomer.getName());
                command.setDisplayName(updateCustomer.getNickName());
                command.setNamespaceId(updateCustomer.getNamespaceId());
                command.setAvatar(updateCustomer.getCorpLogoUri());
                command.setDescription(updateCustomer.getCorpDescription());
                command.setCommunityId(updateCustomer.getCommunityId());
                command.setMemberCount(updateCustomer.getCorpEmployeeAmount() == null ? 0 : (long) updateCustomer.getCorpEmployeeAmount());
                command.setAddress(updateCustomer.getContactAddress());
                command.setLongitude(updateCustomer.getLongitude());
                command.setLatitude(updateCustomer.getLatitude());
                command.setWebsite(updateCustomer.getCorpWebsite());
                command.setEmailDomain(updateCustomer.getCorpEmail());
                command.setAttachments(cmd.getBanner());
                command.setPostUri(cmd.getPostUri());
                command.setUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode());
                command.setContactsPhone(cmd.getHotline());
                List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
                List<OrganizationAddressDTO> addressDTOs = new ArrayList<>();
                if (entryInfos != null && entryInfos.size() > 0) {
                    entryInfos.forEach((e) -> {
                        OrganizationAddressDTO addressDTO = new OrganizationAddressDTO();
                        addressDTO.setAddressId(e.getAddressId());
                        addressDTO.setBuildingId(e.getBuildingId());
                        addressDTO.setEnterpriseId(customer.getOrganizationId());
                        addressDTOs.add(addressDTO);
                    });
                    command.setAttachments(cmd.getBanner());
                    command.setAddressDTOs(addressDTOs);
                }
                organizationService.updateEnterprise(command, true);
            }
        }
//        else {//没有企业的要新增一个
//            OrganizationDTO dto = createOrganization(updateCustomer);
//            updateCustomer.setOrganizationId(dto.getId());
//            enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);
//        }

        //修改了客户名称则要同步修改合同里面的客户名称
        if (!customer.getName().equals(cmd.getName())) {
            List<Contract> contracts = contractProvider.listContractByCustomerId(updateCustomer.getCommunityId(), updateCustomer.getId(), CustomerType.ENTERPRISE.getCode());
            if (contracts != null && contracts.size() > 0) {
                for (Contract contract : contracts) {
                    contract.setCustomerName(updateCustomer.getName());
                    contractProvider.updateContract(contract);
                    contractSearcher.feedDoc(contract);
                }
            }
        }
        // 修改附件
        updateCustomerAttachments(cmd.getId(), cmd.getAttachments());
        return convertToDTO(updateCustomer);
    }

    private void updateCustomerAttachments(Long customerId, List<CustomerAttachmentDTO> attachments) {
        enterpriseCustomerProvider.deleteAllCustomerAttachements(customerId);
        if (attachments != null && attachments.size()>0) {
            attachments.forEach((a) -> {
                CustomerAttachment customerAttachment = ConvertHelper.convert(a, CustomerAttachment.class);
                customerAttachment.setCustomerId(customerId);
                enterpriseCustomerProvider.createCustomerAttachements(customerAttachment);
            });
        }
    }

    private void routeMsgForTrackingChanged(Long originalTrackingUid, Long currentTrackingUid, String originalTrackingName, String currentTrackingName, String customerName, Integer namespaceId, boolean deleteCustomer,Long organizationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerName", customerName);
        map.put("originalTrackingName", originalTrackingName);
        map.put("currentTrackingName", currentTrackingName);
        if (deleteCustomer && originalTrackingUid != null) {
            routeAppMsg(originalTrackingUid, CustomerNotification.scope, CustomerNotification.msg_to_tracking_on_customer_delete, map, namespaceId);
            return;
        }
        if (currentTrackingUid != null && !Objects.equals(currentTrackingUid, originalTrackingUid)) {
            // to new tracking people
            routeAppMsg(currentTrackingUid, CustomerNotification.scope, CustomerNotification.msg_to_new_tracking, map, namespaceId);
        }
        if (originalTrackingUid != null) {
            if (currentTrackingUid != null && !Objects.equals(currentTrackingUid, originalTrackingUid)) {
                // two messges should be sent to two seperate users
                // to old tracking people
                routeAppMsg(originalTrackingUid, CustomerNotification.scope, CustomerNotification.msg_to_old_tracking, map, namespaceId);
            } else if (currentTrackingUid == null) {
                // only one message sent to admin
                // change route message to module admins modify by jiarui 20180702
                ListServiceModuleAdministratorsCommand administratorsCommand = new ListServiceModuleAdministratorsCommand();
                administratorsCommand.setModuleId(21100L);
                administratorsCommand.setActivationFlag(ActivationFlag.YES.getCode());
                administratorsCommand.setOrganizationId(organizationId);
                administratorsCommand.setOwnerId(organizationId);
                administratorsCommand.setOwnerType("EhOrganizations");
                ListServiceModuleAppsAdministratorResponse moduleAppsAdministratorResponse = rolePrivilegeService.listServiceModuleAppsAdministrators(administratorsCommand);
                List<ServiceModuleAppsAuthorizationsDto> moduleAdmins = moduleAppsAdministratorResponse.getDtos();
                if (moduleAdmins != null && moduleAdmins.size() > 0) {
                    moduleAdmins.forEach((r) -> routeAppMsg(r.getTargetId(), CustomerNotification.scope, CustomerNotification.msg_to_admin_no_candidate, map, namespaceId));
                }
            }
        }
    }

    private void routeAppMsg(Long uid, String scope, Integer code, Map<String, Object> map, Integer namespaceId) {
        try {
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), String.valueOf(uid)));
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
           String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(namespaceId, scope, code, UserContext.current().getUser().getLocale(), map, "");
            messageDto.setBody(notifyTextForApplicant);
            messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
            if (!notifyTextForApplicant.trim().equals("")) {
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        String.valueOf(uid), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            LOGGER.error("TRACKING CHANGED SENDING APP MESSAGE FAILED");
        }
    }

    @Override
    public void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd, Boolean checkAuth) {
        EnterpriseCustomer customer = new EnterpriseCustomer();
        if (checkAuth) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_DELETE, cmd.getOrgId(), cmd.getCommunityId());
            customer = checkEnterpriseCustomer(cmd.getId());
        }else {
            customer = enterpriseCustomerProvider.findById(cmd.getId());
            if (customer == null) {
                return;
            }
        }
        //产品功能 #20796 同步过来的不能删
        if (NamespaceCustomerType.EBEI.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))
                || NamespaceCustomerType.SHENZHOU.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))) {
            LOGGER.error("Insufficient privilege");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }
        GetGeneralFormByCustomerIdCommand cmd2 = new GetGeneralFormByCustomerIdCommand();
        cmd2.setCommunityId(cmd.getCommunityId());
        cmd2.setCustomerId(customer.getId());
        cmd2.setModuleId(25000L);
        cmd2.setNamespaceId(cmd.getNamespaceId());
        GetGeneralFormByCustomerIdResponse response = requisitionService.getGeneralFormByCustomerId(cmd2);
        if(response != null && response.getSourceId() != null && response.getSourceId() != 0){
            LOGGER.error("this customer has a requisition , ", customer);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_HAS_REQUISITION,
                    "enterprise customer has requisition");
        }
        List<Contract> contracts = contractProvider.listContractByCustomerId(customer.getCommunityId(), customer.getId(), CustomerType.ENTERPRISE.getCode());
        for (Contract contract : contracts) {
            if (contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
                    || contract.getStatus() == ContractStatus.WAITING_FOR_APPROVAL.getCode() || contract.getStatus() == ContractStatus.APPROVE_QUALITIED.getCode()
                    || contract.getStatus() == ContractStatus.EXPIRING.getCode() || contract.getStatus() == ContractStatus.DRAFT.getCode()) {
                LOGGER.error("enterprise customer has contract. customer: {}", customer);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_HAS_CONTRACT,
                        "enterprise customer has contract");
            }
        }
        customer.setStatus(CommonStatus.INACTIVE.getCode());
        invitedCustomerService.changeCustomerLevelByCustomerId(customer.getId(), CustomerLevelType.DELETE_CUSTOMER.getCode());
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);

        //企业客户新增成功,保存客户事件
        saveCustomerEvent(2, customer, null,cmd.getDeviceType());

        if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0 && checkAuth) {
            Organization org = organizationProvider.findOrganizationById(customer.getOrganizationId());
            if (org != null && org.getId() != null) {
                DeleteOrganizationIdCommand command = new DeleteOrganizationIdCommand();
                command.setId(customer.getOrganizationId());
                ExecutorUtil.submit(() -> organizationService.deleteEnterpriseById(command, false));
            }
        }
        routeMsgForTrackingChanged(customer.getTrackingUid(), null, null, null
                , customer.getName(), null, true, cmd.getOrgId());

    }

    @Override
    public SearchEnterpriseCustomerResponse searchEnterpriseCustomer(SearchEnterpriseCustomerCommand cmd) {
        return null;
    }

    @Override
    public ImportFileTaskDTO importEnterpriseCustomer(ImportEnterpriseCustomerDataCommand cmd, MultipartFile mfile, Long userId) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
        checkPrivilege(cmd.getNamespaceId());
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(), "File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.ENTERPRISE_CUSTOMER.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(() -> {
                ImportFileResponse response = new ImportFileResponse();
                List<ImportEnterpriseCustomerDataDTO> datas = handleImportEnterpriseCustomerData(resultList);
                if (datas.size() > 0) {
                    //设置导出报错的结果excel的标题
                    response.setTitle(datas.get(0));
                    datas.remove(0);
                }
                List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> results = importEnterpriseCustomerData(cmd, datas, userId);
                response.setTotalCount((long) datas.size());
                response.setFailCount((long) results.size());
                response.setLogs(results);
                return response;
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        LOGGER.info("task: {}", task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, List<ImportEnterpriseCustomerDataDTO> list, Long userId) {
        List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> errorDataLogs = new ArrayList<>();
        Boolean isAdmin = checkCustomerAdmin(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId());
        for (ImportEnterpriseCustomerDataDTO str : list) {
            ImportFileResultLog<ImportEnterpriseCustomerDataDTO> log = new ImportFileResultLog<>(CustomerErrorCode.SCOPE);
            EnterpriseCustomer customer = new EnterpriseCustomer();

            if (StringUtils.isBlank(str.getName())) {
                LOGGER.error("enterpirse customer name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer name is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            List<EnterpriseCustomer> enterpriseCustomers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(cmd.getNamespaceId(), cmd.getCommunityId(), str.getName());
            if (enterpriseCustomers != null && enterpriseCustomers.size() > 0) {
                LOGGER.error("enterpirse customer name is already exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer name is already exist");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST);
                errorDataLogs.add(log);
                continue;
            }
            customer.setName(str.getName());

            if (StringUtils.isBlank(str.getContactName())) {
                LOGGER.error("enterpirse customer contact name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer contact name is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CONTACT_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setContactName(str.getContactName());

            if (StringUtils.isBlank(str.getContactMobile())) {
                LOGGER.error("enterpirse customer contact mobile is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer contact mobile is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CONTACT_MOBILE_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setContactMobile(str.getContactMobile());
            customer.setContactPhone(str.getContactPhone());
            customer.setContactAddress(str.getContactAddress());
            //产品期望改为不存在的导入失败 by xiongying20170904
//            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            ScopeFieldItem scopeCategoryFieldItem = fieldService.findScopeFieldItemByDisplayName(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            if (scopeCategoryFieldItem == null) {
                LOGGER.error("enterpirse customer category is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer category is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CATEGORY_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
//            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
            ScopeFieldItem scopeLevelFieldItem = fieldService.findScopeFieldItemByDisplayName(cmd.getNamespaceId(),cmd.getOwnerId(), cmd.getCommunityId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
            if (scopeLevelFieldItem == null) {
                LOGGER.error("enterpirse customer level is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer level is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_LEVEL_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setLevelItemId(scopeLevelFieldItem.getItemId());
//            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
//            if(scopeCategoryFieldItem != null) {
//                customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
//            }
//            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
//            if(scopeLevelFieldItem != null) {
//                customer.setLevelItemId(scopeLevelFieldItem.getItemId());
//            }

            customer.setCommunityId(cmd.getCommunityId());
            customer.setNamespaceId(cmd.getNamespaceId());
            customer.setCreatorUid(userId);
            if (!isAdmin) {
                customer.setTrackingUid(UserContext.currentUserId());
                List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembersByUId(userId);
                if (organizationMembers != null && organizationMembers.size() > 0) {
                    customer.setTrackingName(organizationMembers.get(0).getContactName());
                }
            } else {
                customer.setTrackingUid(null);
            }
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);

            OrganizationDTO organizationDTO = createOrganization(customer);
            customer.setOrganizationId(organizationDTO.getId());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
            //给企业账号添加管理员 默认添加联系人作为管理员 by xiongying20170909
//            Map<Long, List<String>> orgAdminAccounts = new HashMap<>();
//            if (orgAdminAccounts.get(organizationDTO.getId()) == null ||
//                    !orgAdminAccounts.get(organizationDTO.getId()).contains(str.getContactMobile())) {
//                if (!org.springframework.util.StringUtils.isEmpty(str.getContactMobile())) {
//                    CreateOrganizationAdminCommand createOrganizationAdminCommand = new CreateOrganizationAdminCommand();
//                    createOrganizationAdminCommand.setOrganizationId(organizationDTO.getId());
//                    createOrganizationAdminCommand.setContactToken(str.getContactMobile());
//                    createOrganizationAdminCommand.setContactName(str.getContactName());
//                    rolePrivilegeService.createOrganizationAdmin(createOrganizationAdminCommand, cmd.getNamespaceId());
//                }
//                if(orgAdminAccounts.get(organizationDTO.getId()) == null) {
//                    List<String> mobiles = new ArrayList<>();
//                    mobiles.add(str.getContactMobile());
//                    orgAdminAccounts.put(organizationDTO.getId(), mobiles);
//                }
//                orgAdminAccounts.get(organizationDTO.getId()).add(str.getContactMobile());
//            }
        }
        return errorDataLogs;
    }

    private List<ImportEnterpriseCustomerDataDTO> handleImportEnterpriseCustomerData(List list) {
        List<ImportEnterpriseCustomerDataDTO> result = new ArrayList<>();
        int row = 1;
//        int i = 1;
        for (Object o : list) {
            if (row < 2) {
                row++;
                continue;
            }

//            if(i > 9 && result.size() < 2) {
//                break;
//            }
//            i++;

            RowResult r = (RowResult) o;
            ImportEnterpriseCustomerDataDTO data = null;
            if (StringUtils.isNotBlank(r.getA())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setName(r.getA().trim());
            }

            if (StringUtils.isNotBlank(r.getB())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setCategoryItemName(r.getB().trim());
            }

            if (StringUtils.isNotBlank(r.getC())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setLevelItemName(r.getC().trim());
            }

            if (StringUtils.isNotBlank(r.getD())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactName(r.getD().trim());
            }

            if (StringUtils.isNotBlank(r.getE())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactMobile(r.getE().trim());
            }

            if (StringUtils.isNotBlank(r.getF())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactPhone(r.getF().trim());
            }

            if (StringUtils.isNotBlank(r.getG())) {
                if (data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactAddress(r.getG().trim());
            }

            if (data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;

    }

    @Override
    public EnterpriseCustomerDTO getEnterpriseCustomer(GetEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        //zuolin base
        customer.setOwnerId(cmd.getOrgId());
        EnterpriseCustomerDTO dto = convertToDTO(customer);
        popularCustomerUrl(dto);
        return dto;
    }

    private void popularCustomerUrl(EnterpriseCustomerDTO dto) {
        if (dto.getContactAvatarUri() != null) {
            String contentUrl = contentServerService.parserUri(dto.getContactAvatarUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId());
            dto.setContactAvatarUrl(contentUrl);
        }
        if (dto.getCorpLogoUri() != null) {
            String contentUrl = contentServerService.parserUri(dto.getCorpLogoUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId());
            dto.setCorpLogoUrl(contentUrl);
        }
        List<EnterpriseAttachment> attachments = enterpriseCustomerProvider.listEnterpriseCustomerPostUri(dto.getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentDescriptor> bannerUrls = new ArrayList<>();
            attachments.forEach((a) -> {
                AttachmentDescriptor bannerUrl = new AttachmentDescriptor();
                bannerUrl.setContentType(a.getContentUri());
                bannerUrl.setContentUri(a.getContentUri());
                bannerUrl.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId()));
                bannerUrls.add(bannerUrl);
            });
            dto.setBanner(bannerUrls);
        }
        dto.setPostUrl(contentServerService.parserUri(dto.getPostUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId()));
        //此处为客户本身的附件  没有和企业的用一个表了
        List<CustomerAttachment> customerAttachments = enterpriseCustomerProvider.listCustomerAttachments(dto.getId());
        if(customerAttachments!=null && customerAttachments.size()>0){
            List<CustomerAttachmentDTO> attachmentDTOS = new ArrayList<>();
            customerAttachments.forEach((c)->{
                CustomerAttachmentDTO dto1 = ConvertHelper.convert(c, CustomerAttachmentDTO.class);
                dto1.setContentUrl(contentServerService.parserUri(c.getContentUri(),EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId()));
                attachmentDTOS.add(dto1);
            });
            dto.setAttachments(attachmentDTOS);
        }
    }

    private EnterpriseCustomer checkEnterpriseCustomer(Long id) {
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(id);
        if (customer == null || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(customer.getStatus()))) {
            LOGGER.error("enterprise customer is not exist or active. id: {}, customer: {}", id, customer);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                    "customer is not exist or active");
        }
        return customer;
    }

    @Override
    public void createCustomerAccount(CreateCustomerAccountCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerAccount account = ConvertHelper.convert(cmd, CustomerAccount.class);
        enterpriseCustomerProvider.createCustomerAccount(account);
    }

    @Override
    public void createCustomerTax(CreateCustomerTaxCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTax tax = ConvertHelper.convert(cmd, CustomerTax.class);
        enterpriseCustomerProvider.createCustomerTax(tax);
    }

    @Override
    public void deleteCustomerAccount(DeleteCustomerAccountCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerAccount account = checkCustomerAccount(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerAccount(account);
    }

    @Override
    public void deleteCustomerTax(DeleteCustomerTaxCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTax tax = checkCustomerTax(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTax(tax);
    }

    @Override
    public CustomerAccountDTO getCustomerAccount(GetCustomerAccountCommand cmd) {
        CustomerAccount account = checkCustomerAccount(cmd.getId(), cmd.getCustomerId());
        return convertCustomerAccountDTO(account, cmd.getCommunityId());
    }

    @Override
    public CustomerTaxDTO getCustomerTax(GetCustomerTaxCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTax tax = checkCustomerTax(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTaxDTO(tax, cmd.getCommunityId());
    }

    private CustomerAccount checkCustomerAccount(Long id, Long customerId) {
        CustomerAccount account = enterpriseCustomerProvider.findCustomerAccountById(id);
        if (account == null || !account.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(account.getStatus()))) {
            LOGGER.error("enterprise customer account is not exist or active. id: {}, account: {}", id, account);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ACCOUNT_NOT_EXIST,
                    "customer account is not exist or active");
        }
        return account;
    }

    private CustomerTax checkCustomerTax(Long id, Long customerId) {
        CustomerTax tax = enterpriseCustomerProvider.findCustomerTaxById(id);
        if (tax == null || !tax.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(tax.getStatus()))) {
            LOGGER.error("enterprise customer tax is not exist or active. id: {}, tax: {}", id, tax);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TAX_NOT_EXIST,
                    "customer tax is not exist or active");
        }
        return tax;
    }

    private CustomerAccountDTO convertCustomerAccountDTO(CustomerAccount account, Long communityId) {
        CustomerAccountDTO dto = ConvertHelper.convert(account, CustomerAccountDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(account.getCustomerId());

        if (dto.getAccountTypeId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(account.getNamespaceId(),customer.getOwnerId(), communityId, dto.getAccountTypeId());
            if (scopeFieldItem != null) {
                dto.setAccountTypeName(scopeFieldItem.getItemDisplayName());
            }
        }

        if (dto.getAccountNumberTypeId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(account.getNamespaceId(), customer.getOwnerId(),communityId, dto.getAccountNumberTypeId());
            if (scopeFieldItem != null) {
                dto.setAccountNumberTypeName(scopeFieldItem.getItemDisplayName());
            }
        }

        if (dto.getContractId() != null) {
            Contract contract = contractProvider.findContractById(dto.getContractId());
            if (contract != null) {
                dto.setContractName(contract.getName());
            }
        }

        return dto;
    }

    private CustomerTaxDTO convertCustomerTaxDTO(CustomerTax tax, Long communityId) {
        CustomerTaxDTO dto = ConvertHelper.convert(tax, CustomerTaxDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(tax.getCustomerId());
        if (dto.getTaxPayerTypeId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(tax.getNamespaceId(),customer.getOwnerId(), communityId, dto.getTaxPayerTypeId());
            if (scopeFieldItem != null) {
                dto.setTaxPayerTypeName(scopeFieldItem.getItemDisplayName());
            }
        }

        return dto;
    }

    @Override
    public List<CustomerAccountDTO> listCustomerAccounts(ListCustomerAccountsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerAccount> accounts = enterpriseCustomerProvider.listCustomerAccountsByCustomerId(cmd.getCustomerId());
        if (accounts != null && accounts.size() > 0) {
            return accounts.stream().map(account -> {
                return convertCustomerAccountDTO(account, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerTaxDTO> listCustomerTaxes(ListCustomerTaxesCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerTax> taxes = enterpriseCustomerProvider.listCustomerTaxesByCustomerId(cmd.getCustomerId());
        if (taxes != null && taxes.size() > 0) {
            return taxes.stream().map(tax -> {
                return convertCustomerTaxDTO(tax, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerAccount(UpdateCustomerAccountCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerAccount exist = checkCustomerAccount(cmd.getId(), cmd.getCustomerId());
        CustomerAccount account = ConvertHelper.convert(cmd, CustomerAccount.class);
        account.setCreateTime(exist.getCreateTime());
        account.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerAccount(account);
    }

    @Override
    public void updateCustomerTax(UpdateCustomerTaxCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTax exist = checkCustomerTax(cmd.getId(), cmd.getCustomerId());
        CustomerTax tax = ConvertHelper.convert(cmd, CustomerTax.class);
        tax.setCreateTime(exist.getCreateTime());
        tax.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerTax(tax);
    }

    @Override
    public void createCustomerTalent(CreateCustomerTalentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTalent talent = ConvertHelper.convert(cmd, CustomerTalent.class);
//        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getPhone());
//        if (userIdentifier != null) {
//            talent.setRegisterStatus(CommonStatus.ACTIVE.getCode());
//        }
        enterpriseCustomerProvider.createCustomerTalent(talent);
    }

    @Override
    public void deleteCustomerTalent(DeleteCustomerTalentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTalent talent = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTalent(talent);

    }

    @Override
    public CustomerTalentDTO getCustomerTalent(GetCustomerTalentCommand cmd) {
        CustomerTalent talent = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTalentDTO(talent, cmd.getCommunityId());
    }

    private CustomerTalentDTO convertCustomerTalentDTO(CustomerTalent talent, Long communityId) {
        CustomerTalentDTO dto = ConvertHelper.convert(talent, CustomerTalentDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(talent.getCustomerId());
        if (dto.getGender() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getGender());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), customer.getOwnerId(),communityId, dto.getGender());
            if (scopeFieldItem != null) {
                dto.setGenderName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getReturneeFlag() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getReturneeFlag());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), customer.getOwnerId(),communityId, dto.getReturneeFlag());
            if (scopeFieldItem != null) {
                dto.setReturneeFlagName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getAbroadItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getAbroadItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(),customer.getOwnerId(), communityId, dto.getAbroadItemId());
            if (scopeFieldItem != null) {
                dto.setAbroadItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getDegreeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getDegreeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(),customer.getOwnerId(), communityId, dto.getDegreeItemId());
            if (scopeFieldItem != null) {
                dto.setDegreeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getNationalityItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getNationalityItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(),customer.getOwnerId(), communityId, dto.getNationalityItemId());
            if (scopeFieldItem != null) {
                dto.setNationalityItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getIndividualEvaluationItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getIndividualEvaluationItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(),customer.getOwnerId(), communityId, dto.getIndividualEvaluationItemId());
            if (scopeFieldItem != null) {
                dto.setIndividualEvaluationItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getTechnicalTitleItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getTechnicalTitleItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(),customer.getOwnerId(), communityId, dto.getTechnicalTitleItemId());
            if (scopeFieldItem != null) {
                dto.setTechnicalTitleItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (talent.getTalentSourceItemId() != null && StringUtils.isNotBlank(dto.getOriginSourceType())) {
            if (PotentialCustomerType.SERVICE_ALLIANCE.getValue().equals(talent.getOriginSourceType())) {
                ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(talent.getTalentSourceItemId());
                if (serviceAllianceCategories != null) {
                    dto.setTalentSourceItemName(serviceAllianceCategories.getName());
                }
            } else if (PotentialCustomerType.ACTIVITY.getValue().equals(talent.getOriginSourceType())) {
                ActivityCategories entryCategory = activityProivider.findActivityCategoriesByEntryId(talent.getTalentSourceItemId(), talent.getNamespaceId());
                if (entryCategory != null) {
                    dto.setTalentSourceItemName(entryCategory.getName());
                }
            }
        }else {
            dto.setTalentSourceItemName("手动添加");
        }
        dto.setRegisterStatus(talent.getRegisterStatus().toString());
        return dto;
    }

    @Override
    public List<CustomerTalentDTO> listCustomerTalents(ListCustomerTalentsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerTalent> talents = enterpriseCustomerProvider.listCustomerTalentsByCustomerId(cmd.getCustomerId());
        if (talents != null && talents.size() > 0) {
            return talents.stream().map(talent -> convertCustomerTalentDTO(talent, cmd.getCommunityId())).collect(Collectors.toList());
        }
        return null;
    }

    private CustomerTalent checkCustomerTalent(Long id, Long customerId) {
        CustomerTalent talent = enterpriseCustomerProvider.findCustomerTalentById(id);
        if (talent == null || !talent.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(talent.getStatus()))) {
            LOGGER.error("enterprise customer talent is not exist or active. id: {}, talent: {}", id, talent);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TALENT_NOT_EXIST,
                    "customer talent is not exist or active");
        }
        return talent;
    }

    @Override
    public void updateCustomerTalent(UpdateCustomerTalentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTalent exist = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        CustomerTalent talent = ConvertHelper.convert(cmd, CustomerTalent.class);
        talent.setCreateTime(exist.getCreateTime());
        talent.setCreatorUid(exist.getCreatorUid());
        talent.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerTalent(talent);
    }

    @Override
    public void createCustomerApplyProject(CreateCustomerApplyProjectCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerApplyProject project = ConvertHelper.convert(cmd, CustomerApplyProject.class);
        if (cmd.getProjectCompleteDate() != null) {
            project.setProjectCompleteDate(new Timestamp(cmd.getProjectCompleteDate()));
        }
        if (cmd.getProjectEstablishDate() != null) {
            project.setProjectEstablishDate(new Timestamp(cmd.getProjectEstablishDate()));
        }
        enterpriseCustomerProvider.createCustomerApplyProject(project);
    }

    @Override
    public void createCustomerCommercial(CreateCustomerCommercialCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCommercial commercial = ConvertHelper.convert(cmd, CustomerCommercial.class);
        if (cmd.getCancelDate() != null) {
            commercial.setCancelDate(new Timestamp(cmd.getCancelDate()));
        }
        if (cmd.getChangeDate() != null) {
            commercial.setChangeDate(new Timestamp(cmd.getChangeDate()));
        }
        if (cmd.getFoundationDate() != null) {
            commercial.setFoundationDate(new Timestamp(cmd.getFoundationDate()));
        }
        if (cmd.getBusinessLicenceDate() != null) {
            commercial.setBusinessLicenceDate(new Timestamp(cmd.getBusinessLicenceDate()));
        }
        if (cmd.getTaxRegistrationDate() != null) {
            commercial.setTaxRegistrationDate(new Timestamp(cmd.getTaxRegistrationDate()));
        }
        if (cmd.getValidityBeginDate() != null) {
            commercial.setValidityBeginDate(new Timestamp(cmd.getValidityBeginDate()));
        }
        if (cmd.getValidityEndDate() != null) {
            commercial.setValidityEndDate(new Timestamp(cmd.getValidityEndDate()));
        }
        if (cmd.getLiquidationCommitteeRecoredDate() != null) {
            commercial.setLiquidationCommitteeRecoredDate(new Timestamp(cmd.getLiquidationCommitteeRecoredDate()));
        }
        if (cmd.getBranchRegisteredDate() != null) {
            commercial.setBranchRegisteredDate(new Timestamp(cmd.getBranchRegisteredDate()));
        }

        enterpriseCustomerProvider.createCustomerCommercial(commercial);
    }

    @Override
    public void createCustomerPatent(CreateCustomerPatentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        if (cmd.getRegisteDate() != null) {
            patent.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerPatent(patent);
    }

    @Override
    public void createCustomerTrademark(CreateCustomerTrademarkCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
        if (cmd.getRegisteDate() != null) {
            trademark.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerTrademark(trademark);
    }

    @Override
    public void deleteCustomerApplyProject(DeleteCustomerApplyProjectCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerApplyProject project = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerApplyProject(project);
    }

    private CustomerApplyProject checkCustomerApplyProject(Long id, Long customerId) {
        CustomerApplyProject project = enterpriseCustomerProvider.findCustomerApplyProjectById(id);
        if (project == null || !project.getCustomerId().equals(customerId)
                || CommonStatus.INACTIVE.equals(CommonStatus.fromCode(project.getStatus()))) {
            LOGGER.error("enterprise customer project is not exist or active. id: {}, project: {}", id, project);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_PROJECT_NOT_EXIST,
                    "customer project is not exist or active");
        }
        return project;
    }

    @Override
    public void deleteCustomerCommercial(DeleteCustomerCommercialCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCommercial commercial = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerCommercial(commercial);
    }

    private CustomerCommercial checkCustomerCommercial(Long id, Long customerId) {
        CustomerCommercial commercial = enterpriseCustomerProvider.findCustomerCommercialById(id);
        if (commercial == null || !commercial.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(commercial.getStatus()))) {
            LOGGER.error("enterprise customer commercial is not exist or active. id: {}, commercial: {}", id, commercial);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_COMMERCIAL_NOT_EXIST,
                    "customer commercial is not exist or active");
        }
        return commercial;
    }

    @Override
    public void deleteCustomerPatent(DeleteCustomerPatentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerPatent patent = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerPatent(patent);
    }

    private CustomerPatent checkCustomerPatent(Long id, Long customerId) {
        CustomerPatent patent = enterpriseCustomerProvider.findCustomerPatentById(id);
        if (patent == null || !patent.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(patent.getStatus()))) {
            LOGGER.error("enterprise customer patent is not exist or active. id: {}, patent: {}", id, patent);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_PATENT_NOT_EXIST,
                    "customer patent is not exist or active");
        }
        return patent;
    }

    @Override
    public void deleteCustomerTrademark(DeleteCustomerTrademarkCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrademark talent = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTrademark(talent);
    }

    private CustomerTrademark checkCustomerTrademark(Long id, Long customerId) {
        CustomerTrademark trademark = enterpriseCustomerProvider.findCustomerTrademarkById(id);
        if (trademark == null || !trademark.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(trademark.getStatus()))) {
            LOGGER.error("enterprise customer patent is not exist or active. id: {}, trademark: {}", id, trademark);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRADEMARK_NOT_EXIST,
                    "customer trademark is not exist or active");
        }
        return trademark;
    }

    @Override
    public CustomerApplyProjectDTO getCustomerApplyProject(GetCustomerApplyProjectCommand cmd) {
        CustomerApplyProject project = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        return convertCustomerApplyProjectDTO(project, cmd.getCommunityId());
    }

    private CustomerApplyProjectDTO convertCustomerApplyProjectDTO(CustomerApplyProject project, Long communityId) {
        CustomerApplyProjectDTO dto = ConvertHelper.convert(project, CustomerApplyProjectDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(project.getCustomerId());
        if (dto.getStatus() != null) {
            CustomerApplyProjectStatus status = CustomerApplyProjectStatus.fromStatus(dto.getStatus());
            if (status != null) {
                dto.setStatusName(status.getName());
            }
        }
        //PROJECTGSOURCE不是必填项目，这里没有判断 为空字符串
        if (dto.getProjectSource() != null) {
            String[] ids = dto.getProjectSource().split(",");
            LOGGER.info("project source: {}", ids);
            StringBuilder sb = new StringBuilder();
            for (String id : ids) {
//                ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(project.getNamespaceId(), Long.valueOf(id));
                ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(project.getNamespaceId(),customer.getOwnerId(), communityId, Long.valueOf(id));
                LOGGER.info("project source scopeFieldItem: {}", scopeFieldItem);
                if (scopeFieldItem != null) {
                    if (sb.length() == 0) {
                        sb.append(scopeFieldItem.getItemDisplayName());
                    } else {
                        sb.append(",");
                        sb.append(scopeFieldItem.getItemDisplayName());
                    }
                }
            }
            dto.setProjectSourceNames(sb.toString());

        }
        return dto;
    }

    @Override
    public CustomerCommercialDTO getCustomerCommercial(GetCustomerCommercialCommand cmd) {
        CustomerCommercial commercial = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        return convertCustomerCommercialDTO(commercial, cmd.getCommunityId());
    }

    private CustomerCommercialDTO convertCustomerCommercialDTO(CustomerCommercial commercial, Long communityId) {
        CustomerCommercialDTO dto = ConvertHelper.convert(commercial, CustomerCommercialDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(commercial.getCustomerId());
        if (dto.getEnterpriseTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getEnterpriseTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(),customer.getOwnerId(), communityId, dto.getEnterpriseTypeItemId());
            if (scopeFieldItem != null) {
                dto.setEnterpriseTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getShareTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getShareTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(),customer.getOwnerId(), communityId, dto.getShareTypeItemId());
            if (scopeFieldItem != null) {
                dto.setShareTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getPropertyType() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getPropertyType());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(),customer.getOwnerId(), communityId, dto.getPropertyType());
            if (scopeFieldItem != null) {
                dto.setPropertyTypeName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerPatentDTO getCustomerPatent(GetCustomerPatentCommand cmd) {
        CustomerPatent patent = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        return convertCustomerPatentDTO(patent, cmd.getCommunityId());
    }

    private CustomerPatentDTO convertCustomerPatentDTO(CustomerPatent patent, Long communityId) {
        CustomerPatentDTO dto = ConvertHelper.convert(patent, CustomerPatentDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(patent.getCustomerId());
        if (dto.getPatentStatusItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentStatusItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), customer.getOwnerId(),communityId, dto.getPatentStatusItemId());
            if (scopeFieldItem != null) {
                dto.setPatentStatusItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getPatentTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), customer.getOwnerId(),communityId, dto.getPatentTypeItemId());
            if (scopeFieldItem != null) {
                dto.setPatentTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerTrademarkDTO getCustomerTrademark(GetCustomerTrademarkCommand cmd) {
        CustomerTrademark trademark = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTrademarkDTO(trademark, cmd.getCommunityId());
    }

    private CustomerTrademarkDTO convertCustomerTrademarkDTO(CustomerTrademark trademark, Long communityId) {
        CustomerTrademarkDTO dto = ConvertHelper.convert(trademark, CustomerTrademarkDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(trademark.getCustomerId());
        if (dto.getTrademarkTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(trademark.getNamespaceId(), dto.getTrademarkTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(trademark.getNamespaceId(),customer.getOwnerId(), communityId, dto.getTrademarkTypeItemId());
            if (scopeFieldItem != null) {
                dto.setTrademarkTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public List<CustomerApplyProjectDTO> listCustomerApplyProjects(ListCustomerApplyProjectsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerApplyProject> projects = enterpriseCustomerProvider.listCustomerApplyProjectsByCustomerId(cmd.getCustomerId());
        if (projects != null && projects.size() > 0) {
            return projects.stream().map(project -> {
                return convertCustomerApplyProjectDTO(project, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerCommercialDTO> listCustomerCommercials(ListCustomerCommercialsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerCommercial> commercials = enterpriseCustomerProvider.listCustomerCommercialsByCustomerId(cmd.getCustomerId());
        if (commercials != null && commercials.size() > 0) {
            return commercials.stream().map(commercial -> {
                return convertCustomerCommercialDTO(commercial, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerPatentDTO> listCustomerPatents(ListCustomerPatentsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerPatent> patents = enterpriseCustomerProvider.listCustomerPatentsByCustomerId(cmd.getCustomerId());
        if (patents != null && patents.size() > 0) {
            return patents.stream().map(patent -> {
                return convertCustomerPatentDTO(patent, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerTrademarkDTO> listCustomerTrademarks(ListCustomerTrademarksCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerTrademark> trademarks = enterpriseCustomerProvider.listCustomerTrademarksByCustomerId(cmd.getCustomerId());
        if (trademarks != null && trademarks.size() > 0) {
            return trademarks.stream().map(trademark -> {
                return convertCustomerTrademarkDTO(trademark, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerApplyProject(UpdateCustomerApplyProjectCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerApplyProject exist = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        CustomerApplyProject project = ConvertHelper.convert(cmd, CustomerApplyProject.class);
        if (cmd.getProjectCompleteDate() != null) {
            project.setProjectCompleteDate(new Timestamp(cmd.getProjectCompleteDate()));
        }
        if (cmd.getProjectEstablishDate() != null) {
            project.setProjectEstablishDate(new Timestamp(cmd.getProjectEstablishDate()));
        }
        project.setCreateTime(exist.getCreateTime());
        project.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerApplyProject(project);
    }

    @Override
    public void updateCustomerCommercial(UpdateCustomerCommercialCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCommercial exist = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        CustomerCommercial commercial = ConvertHelper.convert(cmd, CustomerCommercial.class);

        if (cmd.getCancelDate() != null) {
            commercial.setCancelDate(new Timestamp(cmd.getCancelDate()));
        }
        if (cmd.getChangeDate() != null) {
            commercial.setChangeDate(new Timestamp(cmd.getChangeDate()));
        }
        if (cmd.getFoundationDate() != null) {
            commercial.setFoundationDate(new Timestamp(cmd.getFoundationDate()));
        }
        if (cmd.getBusinessLicenceDate() != null) {
            commercial.setBusinessLicenceDate(new Timestamp(cmd.getBusinessLicenceDate()));
        }
        if (cmd.getTaxRegistrationDate() != null) {
            commercial.setTaxRegistrationDate(new Timestamp(cmd.getTaxRegistrationDate()));
        }
        if (cmd.getValidityBeginDate() != null) {
            commercial.setValidityBeginDate(new Timestamp(cmd.getValidityBeginDate()));
        }
        if (cmd.getValidityEndDate() != null) {
            commercial.setValidityEndDate(new Timestamp(cmd.getValidityEndDate()));
        }
        if (cmd.getLiquidationCommitteeRecoredDate() != null) {
            commercial.setLiquidationCommitteeRecoredDate(new Timestamp(cmd.getLiquidationCommitteeRecoredDate()));
        }
        if (cmd.getBranchRegisteredDate() != null) {
            commercial.setBranchRegisteredDate(new Timestamp(cmd.getBranchRegisteredDate()));
        }
        commercial.setCreateTime(exist.getCreateTime());
        commercial.setCreateUid(exist.getCreateUid());
        commercial.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerCommercial(commercial);
    }

    @Override
    public void updateCustomerPatent(UpdateCustomerPatentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerPatent exist = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        if (cmd.getRegisteDate() != null) {
            patent.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        patent.setCreateTime(exist.getCreateTime());
        patent.setCreateUid(exist.getCreateUid());
        patent.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerPatent(patent);
    }

    @Override
    public void updateCustomerTrademark(UpdateCustomerTrademarkCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrademark exist = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
        if (cmd.getRegisteDate() != null) {
            trademark.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        trademark.setCreateTime(exist.getCreateTime());
        trademark.setCreateUid(exist.getCreateUid());
        trademark.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerTrademark(trademark);
    }

    @Override
    public void createCustomerEconomicIndicator(CreateCustomerEconomicIndicatorCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerEconomicIndicator indicator = ConvertHelper.convert(cmd, CustomerEconomicIndicator.class);
        if (cmd.getMonth() != null) {
            indicator.setMonth(new Timestamp(cmd.getMonth()));
        }
        enterpriseCustomerProvider.createCustomerEconomicIndicator(indicator);

        //年月不为null 则填到年月所属对应的年度统计表里
        if (cmd.getMonth() != null) {
            CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), new Timestamp(cmd.getMonth()));
            if (statistic == null) {
                statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Timestamp(cmd.getMonth()));
                statistic.setStartTime(getDayBegin(cal));
                statistic.setEndTime(getDayEnd(cal));
                enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
            } else {
                BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
            }
        }

    }

    @Override
    public void createCustomerInvestment(CreateCustomerInvestmentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
        if (cmd.getInvestmentTime() != null) {
            investment.setInvestmentTime(new Timestamp(cmd.getInvestmentTime()));
        }
        enterpriseCustomerProvider.createCustomerInvestment(investment);
    }

    @Override
    public void deleteCustomerEconomicIndicator(DeleteCustomerEconomicIndicatorCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerEconomicIndicator indicator = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerEconomicIndicator(indicator);

        if (indicator.getMonth() != null) {
            CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
            if (statistic != null) {
                if (statistic.getTaxPayment() != null && indicator.getTaxPayment() != null) {
                    if (statistic.getTaxPayment().compareTo(indicator.getTaxPayment()) > 0) {
                        statistic.setTaxPayment(statistic.getTaxPayment().subtract(indicator.getTaxPayment()));
                    } else {
                        statistic.setTaxPayment(BigDecimal.ZERO);
                    }
                }
                if (statistic.getTurnover() != null && indicator.getTurnover() != null) {
                    if (statistic.getTurnover().compareTo(indicator.getTurnover()) > 0) {
                        statistic.setTurnover(statistic.getTurnover().subtract(indicator.getTurnover()));
                    } else {
                        statistic.setTurnover(BigDecimal.ZERO);
                    }
                }

                if (statistic.getTaxPayment().compareTo(BigDecimal.ZERO) == 0
                        && statistic.getTurnover().compareTo(BigDecimal.ZERO) == 0) {
                    enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                }

            }
        }
    }

    private CustomerEconomicIndicator checkCustomerEconomicIndicator(Long id, Long customerId) {
        CustomerEconomicIndicator indicator = enterpriseCustomerProvider.findCustomerEconomicIndicatorById(id);
        if (indicator == null || !indicator.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(indicator.getStatus()))) {
            LOGGER.error("enterprise customer economic indicator is not exist or active. id: {}, indicator: {}", id, indicator);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ECONOMIC_INDICATOR_NOT_EXIST,
                    "customer economic indicator is not exist or active");
        }
        return indicator;
    }

    @Override
    public void deleteCustomerInvestment(DeleteCustomerInvestmentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerInvestment investment = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerInvestment(investment);
    }

    private CustomerInvestment checkCustomerInvestment(Long id, Long customerId) {
        CustomerInvestment investment = enterpriseCustomerProvider.findCustomerInvestmentById(id);
        if (investment == null || !investment.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(investment.getStatus()))) {
            LOGGER.error("enterprise customer investment is not exist or active. id: {}, investment: {}", id, investment);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_INVESTMENT_NOT_EXIST,
                    "customer investment is not exist or active");
        }
        return investment;
    }

    @Override
    public CustomerEconomicIndicatorDTO getCustomerEconomicIndicator(GetCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator indicator = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(indicator, CustomerEconomicIndicatorDTO.class);
    }

    @Override
    public CustomerInvestmentDTO getCustomerInvestment(GetCustomerInvestmentCommand cmd) {
        CustomerInvestment investment = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(investment, CustomerInvestmentDTO.class);
    }

    @Override
    public List<CustomerEconomicIndicatorDTO> listCustomerEconomicIndicators(ListCustomerEconomicIndicatorsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerEconomicIndicator> indicators = enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerId(cmd.getCustomerId());
        if (indicators != null && indicators.size() > 0) {
            return indicators.stream().map(indicator -> {
                return ConvertHelper.convert(indicator, CustomerEconomicIndicatorDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerInvestmentDTO> listCustomerInvestments(ListCustomerInvestmentsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerInvestment> investments = enterpriseCustomerProvider.listCustomerInvestmentsByCustomerId(cmd.getCustomerId());
        if (investments != null && investments.size() > 0) {
            return investments.stream().map(investment -> {
                return ConvertHelper.convert(investment, CustomerInvestmentDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerEconomicIndicator(UpdateCustomerEconomicIndicatorCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerEconomicIndicator exist = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        CustomerEconomicIndicator indicator = ConvertHelper.convert(cmd, CustomerEconomicIndicator.class);
        if (cmd.getMonth() != null) {
            indicator.setMonth(new Timestamp(cmd.getMonth()));
        }
        indicator.setCreateTime(exist.getCreateTime());
        indicator.setCreateUid(exist.getCreateUid());
        indicator.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerEconomicIndicator(indicator);

        if (isSameYear(indicator.getMonth(), exist.getMonth())) {
            if (indicator.getMonth() != null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), new Timestamp(cmd.getMonth()));
                if (statistic == null) {
                    statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    statistic.setStartTime(getDayBegin(cal));
                    statistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()).subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()).subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if (statistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && statistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                    }
                }
            }
        } else {
            if (indicator.getMonth() == null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), exist.getMonth());
                if (statistic != null) {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if (statistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && statistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                    }
                }
            } else if (exist.getMonth() == null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
                if (statistic == null) {
                    statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    statistic.setStartTime(getDayBegin(cal));
                    statistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                }
            } else {
                CustomerEconomicIndicatorStatistic existStatistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), exist.getMonth());
                if (existStatistic != null) {
                    BigDecimal tax = existStatistic.getTaxPayment() == null ? BigDecimal.ZERO : existStatistic.getTaxPayment();
                    existStatistic.setTaxPayment(tax.subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = existStatistic.getTurnover() == null ? BigDecimal.ZERO : existStatistic.getTurnover();
                    existStatistic.setTurnover(turnover.subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if (existStatistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && existStatistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(existStatistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(existStatistic);
                    }
                }
                CustomerEconomicIndicatorStatistic newStatistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
                if (newStatistic == null) {
                    newStatistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    newStatistic.setStartTime(getDayBegin(cal));
                    newStatistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(newStatistic);
                } else {
                    BigDecimal tax = newStatistic.getTaxPayment() == null ? BigDecimal.ZERO : newStatistic.getTaxPayment();
                    newStatistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                    BigDecimal turnover = newStatistic.getTurnover() == null ? BigDecimal.ZERO : newStatistic.getTurnover();
                    newStatistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(newStatistic);
                }
            }

        }
    }

    @Override
    public void updateCustomerInvestment(UpdateCustomerInvestmentCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerInvestment exist = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
        if (cmd.getInvestmentTime() != null) {
            investment.setInvestmentTime(new Timestamp(cmd.getInvestmentTime()));
        }
        investment.setCreateTime(exist.getCreateTime());
        investment.setCreateUid(exist.getCreateUid());
        investment.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerInvestment(investment);
    }

    @Override
    public void createCustomerCertificate(CreateCustomerCertificateCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCertificate certificate = ConvertHelper.convert(cmd, CustomerCertificate.class);
        if (cmd.getRegisteDate() != null) {
            certificate.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerCertificate(certificate);
    }

    @Override
    public void deleteCustomerCertificate(DeleteCustomerCertificateCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCertificate certificate = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerCertificate(certificate);
    }

    private CustomerCertificate checkCustomerCertificate(Long id, Long customerId) {
        CustomerCertificate certificate = enterpriseCustomerProvider.findCustomerCertificateById(id);
        if (certificate == null || !certificate.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(certificate.getStatus()))) {
            LOGGER.error("enterprise customer certificate is not exist or active. id: {}, certificate: {}", id, certificate);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer certificate is not exist or active");
        }
        return certificate;
    }

    @Override
    public CustomerCertificateDTO getCustomerCertificate(GetCustomerCertificateCommand cmd) {
        CustomerCertificate certificate = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(certificate, CustomerCertificateDTO.class);
    }

    @Override
    public List<CustomerCertificateDTO> listCustomerCertificates(ListCustomerCertificatesCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerCertificate> certificates = enterpriseCustomerProvider.listCustomerCertificatesByCustomerId(cmd.getCustomerId());
        if (certificates != null && certificates.size() > 0) {
            return certificates.stream().map(certificate -> {
                return ConvertHelper.convert(certificate, CustomerCertificateDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerCertificate(UpdateCustomerCertificateCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerCertificate exist = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        CustomerCertificate certificate = ConvertHelper.convert(cmd, CustomerCertificate.class);
        if (cmd.getRegisteDate() != null) {
            certificate.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        certificate.setCreateTime(exist.getCreateTime());
        certificate.setCreateUid(exist.getCreateUid());
        certificate.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerCertificate(certificate);
    }

    @Override
    public void createCustomerDepartureInfo(CreateCustomerDepartureInfoCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerDepartureInfo departureInfo = ConvertHelper.convert(cmd, CustomerDepartureInfo.class);
        if (cmd.getReviewTime() != null) {
            departureInfo.setReviewTime(new Timestamp(cmd.getReviewTime()));
        }
        enterpriseCustomerProvider.createCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void createCustomerEntryInfo(CreateCustomerEntryInfoCommand cmd) {
        if (cmd.getCheckAuth() == null || cmd.getCheckAuth()) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        }
        CustomerEntryInfo entryInfo = ConvertHelper.convert(cmd, CustomerEntryInfo.class);
        if (cmd.getContractEndDate() != null) {
            entryInfo.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
        }
        if (cmd.getContractStartDate() != null) {
            entryInfo.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
        }
        enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if (customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            Organization organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
            if (organization != null) {
                updateOrganizationAddress(organization.getId(), entryInfo.getBuildingId(), entryInfo.getAddressId());
                organizationSearcher.feedDoc(organization);
            } else {
                syncCustomerInfoIntoOrganization(entryInfo, customer);
            }
        } else if (customer != null) {
            syncCustomerInfoIntoOrganization(entryInfo, customer);
        }
        enterpriseCustomerSearcher.feedDoc(customer);
    }

    private void syncCustomerInfoIntoOrganization(CustomerEntryInfo entryInfo, EnterpriseCustomer customer) {
        //这里增加入驻信息后自动同步到企业管理
        OrganizationDTO organizationDTO = createOrganization(customer);
        customer.setOrganizationId(organizationDTO.getId());
        //管理员同步过去
//        dbProvider.execute((TransactionStatus status) -> {
        List<CustomerAdminRecord> untrackAdmins = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecords(customer.getId(), null);
        if (untrackAdmins != null && untrackAdmins.size() > 0) {
            untrackAdmins.forEach((r) -> {
                CreateOrganizationAdminCommand cmd = new CreateOrganizationAdminCommand();
                cmd.setContactName(r.getContactName());
                cmd.setContactToken(r.getContactToken());
                cmd.setOrganizationId(customer.getOrganizationId());
                cmd.setNamespaceId(customer.getNamespaceId());
                try {
                    rolePrivilegeService.createOrganizationAdmin(cmd);
                } catch (Exception e) {
                    LOGGER.debug("set organization admin errro organization id = {},exception ={}", cmd.getOrganizationId(), e);
                }
            });
//                enterpriseCustomerProvider.updateEnterpriseCustomerAdminRecordByCustomerId(customer.getId(), customer.getNamespaceId());
            customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        }
        refreshCustomerAdminStatus(untrackAdmins);
//            return null;
//        });
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        updateOrganizationAddress(organizationDTO.getId(), entryInfo.getBuildingId(), entryInfo.getAddressId());
        List<EnterpriseAttachment> attachments = enterpriseCustomerProvider.listEnterpriseCustomerPostUri(customer.getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentDescriptor> bannerUrls = new ArrayList<>();
            attachments.forEach((a) -> {
                AttachmentDescriptor bannerUrl = new AttachmentDescriptor();
                bannerUrl.setContentType(a.getContentType());
                bannerUrl.setContentUri(a.getContentUri());
                bannerUrl.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), customer.getId()));
                bannerUrls.add(bannerUrl);
            });
            addAttachments(customer.getOrganizationId(), bannerUrls, UserContext.currentUserId());
            Organization createOrganization = organizationProvider.findOrganizationById(customer.getOrganizationId());
            organizationSearcher.feedDoc(createOrganization);
        }
    }

    private void refreshCustomerAdminStatus(List<CustomerAdminRecord> untrackAdmins) {
        if (untrackAdmins != null && untrackAdmins.size() > 0) {
            untrackAdmins.forEach((a) -> {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(a.getNamespaceId(), a.getContactToken());
                if (userIdentifier != null) {
                    enterpriseCustomerProvider.updateEnterpriseCustomerAdminRecord(a.getContactToken(), a.getNamespaceId());
                }
            });
        }
    }

    // 企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
    @Override
    public void updateOrganizationAddress(Long orgId, Long buildingId, Long addressId) {
        Long userId = UserContext.currentUserId();
        OrganizationAddress address = organizationProvider.findOrganizationAddressByOrganizationIdAndAddressId(orgId, addressId);
        Address trueAddress = addressProvider.findAddressById(addressId);
        if (address != null) {
            return;
        }
        address = new OrganizationAddress();
        Address addr = this.addressProvider.findAddressById(addressId);
        if (addr != null) {
            address.setBuildingName(addr.getBuildingName());
        }
        address.setOrganizationId(orgId);
        address.setAddressId(addressId);
        address.setBuildingId(buildingId);
        address.setCreatorUid(userId);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setStatus(OrganizationAddressStatus.ACTIVE.getCode());

        this.organizationProvider.createOrganizationAddress(address);

        UpdateWorkPlaceCommand cmd = new UpdateWorkPlaceCommand();
        cmd.setOrganizationId(orgId);
        CreateOfficeSiteCommand cmd2 = new CreateOfficeSiteCommand();
        cmd2.setCommunityId(trueAddress.getCommunityId());
        cmd2.setSiteName(trueAddress.getAddress());
        cmd2.setWholeAddressName(trueAddress.getAddress());
        OrganizationSiteApartmentDTO siteDto = new OrganizationSiteApartmentDTO();
        siteDto.setBuildingId(buildingId);
        siteDto.setApartmentId(addressId);
        List<OrganizationSiteApartmentDTO> siteDtos = new ArrayList<>();
        siteDtos.add(siteDto);
        cmd2.setSiteDtos(siteDtos);
        List<CreateOfficeSiteCommand> cmd2s = new ArrayList<>();
        cmd2s.add(cmd2);
        cmd.setOfficeSites(cmd2s);

        try{
            organizationService.insertWorkPlacesAndBuildings(cmd);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    // 企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
    private void deleteOrganizationAddress(Long orgId, Long buildingId, Long addressId) {
        OrganizationAddress address = organizationProvider.findOrganizationAddressByOrganizationIdAndAddressId(orgId, addressId);
        if (address != null) {
            organizationProvider.deleteOrganizationAddress(address);
        }
    }


    @Override
    public void deleteCustomerDepartureInfo(DeleteCustomerDepartureInfoCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerDepartureInfo departureInfo = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void deleteCustomerEntryInfo(DeleteCustomerEntryInfoCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerEntryInfo entryInfo = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerEntryInfo(entryInfo);

        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        Organization organization = new Organization();
        if (customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
            if (organization != null) {
                deleteOrganizationAddress(organization.getId(), entryInfo.getBuildingId(), entryInfo.getAddressId());
            }
        }
        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(cmd.getCustomerId());
        if (entryInfos == null || entryInfos.size() == 0) {
            if (organization != null && organization.getId() != 0 && organization.getId()!=null) {
//                organizationSearcher.deleteById(organization.getId());
//                organizationProvider.deleteOrganization(organization);
                DeleteOrganizationIdCommand command = new DeleteOrganizationIdCommand();
                command.setCommunityId(cmd.getCommunityId());
                command.setId(organization.getId());
                command.setManageOrganizationId(cmd.getOrgId());
                command.setEnterpriseId(cmd.getOrgId());
                command.setCheckAuth(TrueOrFalseFlag.FALSE.getCode());
                organizationService.deleteOrganization(command);
                if (customer != null) {
                    customer.setOrganizationId(0L);
                }
            }
        }
        //sync to es
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        organizationSearcher.feedDoc(organization);
    }

    private CustomerEntryInfo checkCustomerEntryInfo(Long id, Long customerId) {
        CustomerEntryInfo entryInfo = enterpriseCustomerProvider.findCustomerEntryInfoById(id);
        if (entryInfo == null || !entryInfo.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(entryInfo.getStatus()))) {
            LOGGER.error("enterprise customer entryInfo is not exist or active. id: {}, certificate: {}", id, entryInfo);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer entryInfo is not exist or active");
        }
        return entryInfo;
    }

    private CustomerDepartureInfo checkCustomerDepartureInfo(Long id, Long customerId) {
        CustomerDepartureInfo departureInfo = enterpriseCustomerProvider.findCustomerDepartureInfoById(id);
        if (departureInfo == null || !departureInfo.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(departureInfo.getStatus()))) {
            LOGGER.error("enterprise customer departureInfo is not exist or active. id: {}, departureInfo: {}", id, departureInfo);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer departureInfo is not exist or active");
        }
        return departureInfo;
    }

    @Override
    public CustomerDepartureInfoDTO getCustomerDepartureInfo(GetCustomerDepartureInfoCommand cmd) {
        CustomerDepartureInfo departureInfo = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        return convertCustomerDepartureInfoDTO(departureInfo, cmd.getCommunityId());
    }

    @Override
    public CustomerEntryInfoDTO getCustomerEntryInfo(GetCustomerEntryInfoCommand cmd) {
        CustomerEntryInfo entryInfo = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        if (entryInfo.getAddressId() != null) {

        }
        return convertCustomerEntryInfoDTO(entryInfo);
    }

    @Override
    public List<CustomerDepartureInfoDTO> listCustomerDepartureInfos(ListCustomerDepartureInfosCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerDepartureInfo> departureInfos = enterpriseCustomerProvider.listCustomerDepartureInfos(cmd.getCustomerId());
        if (departureInfos != null && departureInfos.size() > 0) {
            return departureInfos.stream().map(departureInfo -> {
                return convertCustomerDepartureInfoDTO(departureInfo, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    private CustomerDepartureInfoDTO convertCustomerDepartureInfoDTO(CustomerDepartureInfo departureInfo, Long communityId) {
        CustomerDepartureInfoDTO dto = ConvertHelper.convert(departureInfo, CustomerDepartureInfoDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(departureInfo.getCustomerId());
        if (dto.getDepartureDirectionId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(departureInfo.getNamespaceId(), customer.getOwnerId(),communityId, dto.getDepartureDirectionId());
            if (scopeFieldItem != null) {
                dto.setDepartureDirectionName(scopeFieldItem.getItemDisplayName());
            }
        }
        if (dto.getDepartureNatureId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(departureInfo.getNamespaceId(),customer.getOwnerId(), communityId, dto.getDepartureNatureId());
            if (scopeFieldItem != null) {
                dto.setDepartureNatureName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    private CustomerEntryInfoDTO convertCustomerEntryInfoDTO(CustomerEntryInfo entryInfo) {
        CustomerEntryInfoDTO dto = ConvertHelper.convert(entryInfo, CustomerEntryInfoDTO.class);

        if (dto.getAddressId() != null) {
            Address address = addressProvider.findAddressById(dto.getAddressId());
            // (old) only hook active address info
            // (old) if (address != null && AddressAdminStatus.ACTIVE.equals(AddressAdminStatus.fromCode(address.getStatus()))) {
            //hook every address info
            if (address != null) {
                dto.setAddressName(address.getAddress());
                dto.setBuilding(address.getBuildingName());
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
                
                //issue-34394,添加buildingId信息，避免前端无法获取buildingId，导致没办法和楼栋门牌匹配上
                dto.setBuildingId(address.getBuildingId());
//                Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
//                if(building != null) {
//                    dto.setBuildingId(building.getId());
//                }
            }
        }
        return dto;
    }


    @Override
    public List<CustomerEntryInfoDTO> listCustomerEntryInfos(ListCustomerEntryInfosCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(cmd.getCustomerId());
        entryInfos = removeDuplicatedEntryInfo(entryInfos);
        if (entryInfos.size() > 0) {
            return entryInfos.stream().map(this::convertCustomerEntryInfoDTO).collect(Collectors.toList());
        }
        return null;
    }

    private List<CustomerEntryInfo> removeDuplicatedEntryInfo(List<CustomerEntryInfo> entryInfos) {
        Map<Long, CustomerEntryInfo> map = new HashMap<>();
        if(entryInfos!=null && entryInfos.size()>0){
            entryInfos.forEach((e)-> map.putIfAbsent(e.getAddressId(), e));
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public List<CustomerEntryInfoDTO> listCustomerEntryInfosWithoutAuth(ListCustomerEntryInfosCommand cmd) {
        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(cmd.getCustomerId());
        //issue-39640 新签合同，签约“前海金融管理学院”资产重复出现多条
        entryInfos = removeDuplicatedEntryInfo(entryInfos);
        if (entryInfos != null && entryInfos.size() > 0) {
            return entryInfos.stream().map(this::convertCustomerEntryInfoDTO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerDepartureInfo(UpdateCustomerDepartureInfoCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerDepartureInfo exist = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        CustomerDepartureInfo departureInfo = ConvertHelper.convert(cmd, CustomerDepartureInfo.class);
        if (cmd.getReviewTime() != null) {
            departureInfo.setReviewTime(new Timestamp(cmd.getReviewTime()));
        }
        departureInfo.setCreateTime(exist.getCreateTime());
        departureInfo.setCreateUid(exist.getCreateUid());
        departureInfo.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void updateCustomerEntryInfo(UpdateCustomerEntryInfoCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerEntryInfo exist = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        CustomerEntryInfo entryInfo = ConvertHelper.convert(cmd, CustomerEntryInfo.class);
        if (cmd.getContractEndDate() != null) {
            entryInfo.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
        }
        if (cmd.getContractStartDate() != null) {
            entryInfo.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
        }
        entryInfo.setCreateTime(exist.getCreateTime());
        entryInfo.setCreateUid(exist.getCreateUid());
        entryInfo.setStatus(exist.getStatus());
        enterpriseCustomerProvider.updateCustomerEntryInfo(entryInfo);

        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if (customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            Organization organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
            if (organization != null) {
                updateOrganizationAddress(organization.getId(), entryInfo.getBuildingId(), entryInfo.getAddressId());
                organizationSearcher.feedDoc(organization);
            }
        }
        enterpriseCustomerSearcher.feedDoc(customer);
    }

    @Override
    public CustomerIndustryStatisticsResponse listCustomerIndustryStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        CustomerIndustryStatisticsResponse response = new CustomerIndustryStatisticsResponse();
        List<CustomerIndustryStatisticsDTO> dtos = new ArrayList<>();
        Map<Long, Long> industries = enterpriseCustomerProvider.listEnterpriseCustomerIndustryByCommunityId(cmd.getCommunityId());
        response.setCustomerTotalCount(0L);
        industries.forEach((categoryId, count) -> {
            CustomerIndustryStatisticsDTO dto = new CustomerIndustryStatisticsDTO();
            dto.setCorpIndustryItemId(categoryId);
            dto.setCustomerCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(),cmd.getOrgId(), cmd.getCommunityId(), categoryId);
            if (item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setCustomerTotalCount(response.getCustomerTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public ListRentalBillsCommandResponse listCustomerRentalBills(ListCustomerRentalBillsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if(customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            ListRentalBillsByOrdIdCommand command = new ListRentalBillsByOrdIdCommand();
            command.setOrganizationId(customer.getOrganizationId());
            command.setPageSize(cmd.getPageSize());
            command.setPageAnchor(cmd.getPageAnchor());
            return rentalv2Service.listRentalBillsByOrdId(command);
        }
        return new ListRentalBillsCommandResponse();
    }

    @Override
    public SearchRequestInfoResponse listCustomerSeviceAllianceAppRecords(ListCustomerSeviceAllianceAppRecordsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if(customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            return yellowPageService.listSeviceAllianceAppRecordsByEnterpriseId(customer.getOrganizationId(), cmd.getPageAnchor(), cmd.getPageSize());
        }
        return new SearchRequestInfoResponse();
    }

    @Override
    public CustomerIntellectualPropertyStatisticsResponse listCustomerIntellectualPropertyStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });
        CustomerIntellectualPropertyStatisticsResponse response = new CustomerIntellectualPropertyStatisticsResponse();
        response.setPropertyTotalCount(0L);
        List<CustomerIntellectualPropertyStatisticsDTO> dtos = new ArrayList<>();
        Long trademarks = enterpriseCustomerProvider.countTrademarksByCustomerIds(customerIds);
        if (trademarks != null && trademarks != 0) {
            response.setPropertyTotalCount(response.getPropertyTotalCount() + trademarks);

            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("商标");
            dto.setPropertyCount(trademarks);
            dtos.add(dto);
        }

        Long certificates = enterpriseCustomerProvider.countCertificatesByCustomerIds(customerIds);
        if (certificates != null && certificates != 0) {
            response.setPropertyTotalCount(response.getPropertyTotalCount() + certificates);

            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("证书");
            dto.setPropertyCount(certificates);
            dtos.add(dto);
        }

        Map<Long, Long> properties = enterpriseCustomerProvider.listCustomerPatentsByCustomerIds(customerIds);
        properties.forEach((categoryId, count) -> {
            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("专利");
            dto.setPropertyCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
//            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), categoryId);
//            if(item != null) {
//                dto.setPropertyType(item.getItemDisplayName());
//            }
            dtos.add(dto);
            response.setPropertyTotalCount(response.getPropertyTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerProjectStatisticsResponse listCustomerProjectStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });

        CustomerProjectStatisticsResponse response = new CustomerProjectStatisticsResponse();
        List<CustomerProjectStatisticsDTO> dtos = new ArrayList<>();
        response.setProjectTotalAmount(BigDecimal.ZERO);
        response.setProjectTotalCount(0L);

        Map<Long, CustomerProjectStatisticsDTO> statistics = enterpriseCustomerProvider.listCustomerApplyProjectsByCustomerIds(customerIds);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listCustomerProjectStatistics customer ids : {}, statistics: {}", customerIds, StringHelper.toJsonString(statistics));
        }

        statistics.forEach((itemId, statistic) -> {
            CustomerProjectStatisticsDTO dto = statistic;
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), itemId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(),cmd.getOrgId(), cmd.getCommunityId(), itemId);
            if (item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setProjectTotalAmount(response.getProjectTotalAmount().add(dto.getProjectAmount()));
            response.setProjectTotalCount(response.getProjectTotalCount() + dto.getProjectCount());
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerSourceStatisticsResponse listCustomerSourceStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        CustomerSourceStatisticsResponse response = new CustomerSourceStatisticsResponse();
        List<CustomerSourceStatisticsDTO> dtos = new ArrayList<>();
        Map<Long, Long> sources = enterpriseCustomerProvider.listEnterpriseCustomerSourceByCommunityId(cmd.getCommunityId());
        response.setCustomerTotalCount(0L);
        sources.forEach((categoryId, count) -> {
            CustomerSourceStatisticsDTO dto = new CustomerSourceStatisticsDTO();
            dto.setSourceItemId(categoryId);
            dto.setCustomerCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(),cmd.getOrgId(), cmd.getCommunityId(), categoryId);
            if (item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setCustomerTotalCount(response.getCustomerTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerTalentStatisticsResponse listCustomerTalentStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });

        CustomerTalentStatisticsResponse response = new CustomerTalentStatisticsResponse();
        Map<Long, Long> talents = enterpriseCustomerProvider.listCustomerTalentCountByCustomerIds(customerIds);
        List<CustomerTalentStatisticsDTO> dtos = new ArrayList<>();
        response.setMemberTotalCount(0L);
        talents.forEach((categoryId, count) -> {
            CustomerTalentStatisticsDTO dto = new CustomerTalentStatisticsDTO();
            dto.setTalentCategoryId(categoryId);
            dto.setCustomerMemberCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(),cmd.getOrgId(), cmd.getCommunityId(), categoryId);
            if (item != null) {
                dto.setCategoryName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setMemberTotalCount(response.getMemberTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public EnterpriseCustomerStatisticsDTO listEnterpriseCustomerStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        EnterpriseCustomerStatisticsDTO dto = new EnterpriseCustomerStatisticsDTO();
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        dto.setCustomerCount(customers.size() & 0xFFFFFFFFL);
        dto.setCustomerMemberCount(0L);

        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
            int members = customer.getCorpEmployeeAmount() == null ? 0 : customer.getCorpEmployeeAmount();
            dto.setCustomerMemberCount(dto.getCustomerMemberCount() + members);
        });

        List<CustomerEconomicIndicator> indicators = enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerIds(customerIds);
        dto.setTotalTurnover(BigDecimal.ZERO);
        dto.setTotalTaxAmount(BigDecimal.ZERO);
        indicators.forEach(indicator -> {
            BigDecimal turnover = indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover();
            BigDecimal taxPayment = indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment();
            dto.setTotalTurnover(dto.getTotalTurnover().add(turnover));
            dto.setTotalTaxAmount(dto.getTotalTaxAmount().add(taxPayment));
        });

        return dto;
    }

    @Override
    public ListCustomerAnnualStatisticsResponse listCustomerAnnualStatistics(ListCustomerAnnualStatisticsCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_STAT, cmd.getOrgId(), cmd.getCommunityId());
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        ListCustomerAnnualStatisticsResponse response = new ListCustomerAnnualStatisticsResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getPageAnchor() != null) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<CustomerAnnualStatisticDTO> dtos = enterpriseCustomerProvider.listCustomerAnnualStatistics(cmd.getCommunityId(), now, locator, pageSize,
                cmd.getTurnoverMinimum(), cmd.getTurnoverMaximum(), cmd.getTaxPaymentMinimum(), cmd.getTaxPaymentMaximum());
        response.setStatisticDTOs(dtos);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private Boolean isSameYear(Timestamp newMonth, Timestamp oldMonth) {
        if (newMonth == null && oldMonth == null) {
            return true;
        } else {
            if (newMonth != null && oldMonth != null) {
                Calendar newCal = Calendar.getInstance();
                newCal.setTime(newMonth);

                Calendar oldCal = Calendar.getInstance();
                oldCal.setTime(oldMonth);
                if (newCal.get(Calendar.YEAR) == oldCal.get(Calendar.YEAR)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Timestamp getDayBegin(Calendar cal) {
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getDayEnd(Calendar cal) {
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DATE, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getMonth(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public ListCustomerAnnualDetailsResponse listCustomerAnnualDetails(ListCustomerAnnualDetailsCommand cmd) {
        ListCustomerAnnualDetailsResponse response = new ListCustomerAnnualDetailsResponse();
        List<CustomerEconomicIndicator> economicIndicators = enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerId(cmd.getCustomerId(), new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime()));
        if (economicIndicators != null && economicIndicators.size() > 0) {
            Map<Timestamp, MonthStatistics> monthStatisticsMap = new HashMap<>();
            economicIndicators.forEach(economicIndicator -> {
                Timestamp month = getMonth(economicIndicator.getMonth());
                MonthStatistics statistic = monthStatisticsMap.get(month);
                if (statistic == null) {
                    statistic = new MonthStatistics();
                    statistic.setMonth(month.getTime());
                    statistic.setTaxPayment(economicIndicator.getTaxPayment());
                    statistic.setTurnover(economicIndicator.getTurnover());
                } else {
                    BigDecimal taxPayment = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(taxPayment.add(economicIndicator.getTaxPayment() == null ? BigDecimal.ZERO : economicIndicator.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(economicIndicator.getTurnover() == null ? BigDecimal.ZERO : economicIndicator.getTurnover()));
                }
                monthStatisticsMap.put(month, statistic);
            });
            List<MonthStatistics> statistics = new ArrayList<>();
            monthStatisticsMap.forEach((month, statistic) -> {
                statistics.add(statistic);
            });
            response.setStatistics(statistics);

            //季度
            Map<Integer, QuarterStatistics> quarterStatisticsMap = new HashMap<>();
            monthStatisticsMap.forEach((timestamp, statistic) -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(timestamp);
                int month = cal.get(Calendar.MONTH);
                int quarter = 0;
                if (month <= 3) {
                    quarter = YearQuarter.THE_FIRST_QUARTER.getCode();
                } else if (month > 3 && month <= 6) {
                    quarter = YearQuarter.THE_SECOND_QUARTER.getCode();
                } else if (month > 6 && month <= 9) {
                    quarter = YearQuarter.THE_THIRD_QUARTER.getCode();
                } else if (month > 9 && month <= 12) {
                    quarter = YearQuarter.THE_FOURTH_QUARTER.getCode();
                }

                QuarterStatistics qs = quarterStatisticsMap.get(quarter);
                if (qs == null) {
                    qs = new QuarterStatistics();
                    qs.setQuarter(quarter);
                    qs.setTaxPayment(statistic.getTaxPayment());
                    qs.setTurnover(statistic.getTurnover());

                } else {
                    BigDecimal taxPayment = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    qs.setTaxPayment(taxPayment.add(qs.getTaxPayment() == null ? BigDecimal.ZERO : qs.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    qs.setTurnover(turnover.add(qs.getTurnover() == null ? BigDecimal.ZERO : qs.getTurnover()));
                }
                quarterStatisticsMap.put(quarter, qs);
            });

            List<QuarterStatistics> quarterStatisticses = new ArrayList<>();
            quarterStatisticsMap.forEach((quarter, statistic) -> {
                quarterStatisticses.add(statistic);
            });
            response.setQuarterStatisticses(quarterStatisticses);
        }
        return response;
    }

    /**
     * 每天早上2点20,自动同步客户信息
     */
    @Scheduled(cron = "1 20 2 * * ?")
    public void customerAutoSync() {
//        Accessor accessor = bigCollectionProvider.getMapAccessor(CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode() + System.currentTimeMillis(), "");
//        RedisTemplate redisTemplate = accessor.getTemplate(stringRedisSerializer);
//        Map<String,String> runningMap  =new HashMap<>();
//        this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode()).tryEnter(() -> {
//            String runningFlag = getSyncTaskToken(redisTemplate, CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode());
//            runningMap.put(CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode(), runningFlag);
//            if(StringUtils.isBlank(runningFlag))
//            redisTemplate.opsForValue().set(CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode(), "executing", 5, TimeUnit.HOURS);
//        });
        String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
        if (taskServer.equals(equipmentIp)) {
//        if(StringUtils.isEmpty(runningMap.get(CoordinationLocks.SYNC_THIRD_CUSTOMER.getCode()))) {
            List<Community> communities = communityProvider.listAllCommunitiesWithNamespaceToken();
            if (communities != null) {
                for (Community community : communities) {
                    SyncCustomersCommand command = new SyncCustomersCommand();
                    command.setNamespaceId(community.getNamespaceId());
                    command.setCommunityId(community.getId());
                    syncEnterpriseCustomers(command, false);
                    syncIndividualCustomers(command);
                }
            }
        }
    }

    @Override
    public String syncEnterpriseCustomers(SyncCustomersCommand cmd, Boolean authFlag) {
        if (authFlag) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_SYNC, cmd.getOrgId(), cmd.getCommunityId());
        }

        if (cmd.getNamespaceId() == 999971) {

            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
            if (community != null) {
                int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.ENTERPRISE.getCode());
                if (syncCount > 0) {
                    return "1";
                }

                SyncDataTask task = new SyncDataTask();
                task.setOwnerType(EntityType.COMMUNITY.getCode());
                task.setOwnerId(community.getId());
                task.setType(SyncDataTaskType.CUSTOMER.getCode());
                task.setCreatorUid(UserContext.currentUserId());
                task.setLockKey(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId());
                syncDataTaskService.executeTask(() -> {
                    SyncDataResponse response = new SyncDataResponse();
                    zjgkOpenService.syncEnterprises("0", community.getNamespaceCommunityToken(), task.getId());
                    return response;
                }, task);
            }
        } else {
            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
            if (community == null) {
                return "0";
            }
            int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.ENTERPRISE.getCode());
            if (syncCount > 0) {
                return "1";
            }
            String version;
            if(cmd.getAllSyncFlag() != null && cmd.getAllSyncFlag() == 1) {
                version = "0";
            }else{
                version = enterpriseCustomerProvider.findLastEnterpriseCustomerVersionByCommunity(cmd.getNamespaceId(), community.getId());
            }
            CustomerHandle customerHandle = PlatformContext.getComponent(CustomerHandle.CUSTOMER_PREFIX + cmd.getNamespaceId());
            if (customerHandle != null) {
                SyncDataTask task = new SyncDataTask();
                task.setOwnerType(EntityType.COMMUNITY.getCode());
                task.setOwnerId(community.getId());
                task.setType(SyncDataTaskType.CUSTOMER.getCode());
                task.setCreatorUid(UserContext.currentUserId());
                task.setLockKey(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId());
                syncDataTaskService.executeTask(() -> {
                    SyncDataResponse response = new SyncDataResponse();
                    customerHandle.syncEnterprises("1", version, community.getNamespaceCommunityToken(), task.getId());
                    return response;
                }, task);
            }
        }
        return "0";
//        if(cmd.getNamespaceId() == 999971) {
//            this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId()).tryEnter(()-> {
//                ExecutorUtil.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            if(cmd.getCommunityId() == null) {
//                                int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(cmd.getNamespaceId(), null, DataType.ENTERPRISE.getCode());
//                                if(syncCount > 0) {
//                                    return ;
//                                }
//                                SyncDataTask task = new SyncDataTask();
//                                task.setType(SyncDataTaskType.CUSTOMER.getCode());
//                                task.setCreatorUid(UserContext.currentUserId());
//                                task = syncDataTaskService.executeTask(() -> {
//                                    SyncDataResponse response = new SyncDataResponse();
//                                    zjgkOpenService.syncEnterprises("0", null);
//                                    return response;
//                                }, task);
//
//                            } else {
//                                Community community = communityProvider.findCommunityById(cmd.getCommunityId());
//                                if(community != null) {
//                                    int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.ENTERPRISE.getCode());
//                                    if(syncCount > 0) {
//                                        return ;
//                                    }
//                                    task.setOwnerType(EntityType.COMMUNITY.getCode());
//                                    task.setOwnerId(community.getId());
//                                    zjgkOpenService.syncEnterprises("0", community.getNamespaceCommunityToken());
//
//                                }
//
//                            }
//                        }catch (Exception e){
//                            LOGGER.error("syncEnterpriseCustomers error.", e);
//                        }
//                    }
//                });
//            });
//        } else {
//            this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId()).tryEnter(()-> {
//                ExecutorUtil.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
//                            if(community == null) {
//                                return;
//                            }
//                            int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.ENTERPRISE.getCode());
//                            if(syncCount > 0) {
//                                return ;
//                            }
//                            String version = enterpriseCustomerProvider.findLastEnterpriseCustomerVersionByCommunity(cmd.getNamespaceId(), community.getId());
//                            CustomerHandle customerHandle = PlatformContext.getComponent(CustomerHandle.CUSTOMER_PREFIX + cmd.getNamespaceId());
//                            if(customerHandle != null) {
//                                task.setOwnerType(EntityType.COMMUNITY.getCode());
//                                task.setOwnerId(community.getId());
//                                customerHandle.syncEnterprises("1", version, community.getNamespaceCommunityToken());
//                            }
//
//                        }catch (Exception e){
//                            LOGGER.error("syncEnterpriseCustomers error.", e);
//                        }
//                    }
//                });
//            });
//        }

    }

    @Override
    public String syncIndividualCustomers(SyncCustomersCommand cmd) {
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community != null) {
            int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.INDIVIDUAL.getCode());
            if (syncCount > 0) {
                return "1";
            }

            SyncDataTask task = new SyncDataTask();
            task.setOwnerType(EntityType.COMMUNITY.getCode());
            task.setOwnerId(community.getId());
            task.setType(SyncDataTaskType.INDIVIDUAL.getCode());
            task.setCreatorUid(UserContext.currentUserId());
            syncDataTaskService.executeTask(() -> {
                SyncDataResponse response = new SyncDataResponse();
                zjgkOpenService.syncIndividuals("0", community.getNamespaceCommunityToken(), task.getId());
                return response;
            }, task);

        }

        return "0";

//        if(cmd.getNamespaceId() == 999971) {
//            ExecutorUtil.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        if(cmd.getCommunityId() == null) {
//                            int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(cmd.getNamespaceId(), null, DataType.INDIVIDUAL.getCode());
//                            if(syncCount > 0) {
//                                return ;
//                            }
//                            zjgkOpenService.syncIndividuals("0", null);
//                        } else {
//                            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
//                            if(community != null) {
//                                int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.INDIVIDUAL.getCode());
//                                if(syncCount > 0) {
//                                    return ;
//                                }
//                                zjgkOpenService.syncIndividuals("0", community.getNamespaceCommunityToken());
//                            }
//                        }
//                    }catch (Exception e){
//                        LOGGER.error("syncIndividualCustomers error.", e);
//                    }
//                }
//            });
//
//        }
    }


    @Override
    public List<CustomerTrackingDTO> listCustomerTrackings(ListCustomerTrackingsCommand cmd) {
        if (InvitedCustomerType.INVITED_CUSTOMER.equals(InvitedCustomerType.fromCode(cmd.getCustomerSource()))) {
            //todo: check invited customer privilege
        } else {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        }
//        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerTracking> trackings = enterpriseCustomerProvider.listCustomerTrackingsByCustomerId(cmd.getCustomerId(),cmd.getCustomerSource());
        if (trackings != null && trackings.size() > 0) {
            return trackings.stream().map(tracking -> convertCustomerTrackingDTO(tracking, cmd.getCommunityId())).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CustomerTrackingDTO getCustomerTracking(GetCustomerTrackingCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTracking tracking = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTrackingDTO(tracking, cmd.getCommunityId());
    }

    private CustomerTrackingDTO convertCustomerTrackingDTO(CustomerTracking tracking, Long communityId) {
        CustomerTrackingDTO dto = ConvertHelper.convert(tracking, CustomerTrackingDTO.class);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(tracking.getCustomerId());
        if (dto.getTrackingType() != null) {
//        	String trackingTypeName = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, Integer.parseInt(dto.getTrackingType().toString()) , UserContext.current().getUser().getLocale(), new HashMap<>(), "");

            FieldGroup group = fieldProvider.findGroupByGroupLogicName("com.everhomes.customer.CustomerTracking");
            if (group != null) {
                Field field = fieldProvider.findField(ModuleName.ENTERPRISE_CUSTOMER.getName(), "trackingType", group.getPath());
                ScopeFieldItem item = fieldProvider.findScopeFieldItemByBusinessValue(tracking.getNamespaceId(),customer.getOwnerId(),customer.getOwnerType(), communityId, ModuleName.ENTERPRISE_CUSTOMER.getName(), field.getId(), dto.getTrackingType().byteValue());
                if (item != null) {
                    dto.setTrackingTypeName(item.getItemDisplayName());
                }
            }

        }
//        if (dto.getTrackingUid() != null) {
//            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(dto.getTrackingUid());
//            if (null != detail && null != detail.getContactName()) {
//                dto.setTrackingUidName(detail.getContactName()+"("+detail.getContactToken()+")");
//            } else {
//                User user = userProvider.findUserById(dto.getTrackingUid());
//                if (user != null) {
//                    dto.setTrackingUidName(user.getNickName()+"("+user.getIdentifierToken()+")");
//                }
//            }
//        }
        if (dto.getIntentionGrade() != null) {
            Field field = fieldProvider.findField(19L, "intentionGrade");
            if (field != null) {
                ScopeFieldItem item = fieldProvider.findScopeFieldItemByBusinessValue(tracking.getNamespaceId(),customer.getOwnerId(),customer.getOwnerType(), communityId, ModuleName.ENTERPRISE_CUSTOMER.getName(), field.getId(), dto.getIntentionGrade().byteValue());
                if (item != null) {
                    dto.setIntentionGradeName(item.getItemDisplayName());
                }
            }
        }
        List<String> urlList = new ArrayList<>();
        List<String> uriList = new ArrayList<>();
        if (StringUtils.isNotEmpty(tracking.getContentImgUri())) {
            String[] uriArray = tracking.getContentImgUri().split(",");
            for (String uri : uriArray) {
                uriList.add(uri);
                String contentUrl = contentServerService.parserUri(uri, EntityType.CUSTOMER_TRACKING.getCode(), dto.getId());
                urlList.add(contentUrl);
            }
        }
        dto.setContentImgUriList(uriList);
        dto.setContentImgUrlList(urlList);
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(tracking.getTrackingUid());
        if(members!=null && members.size()>0){
            dto.setTrackingUidName(members.get(0).getContactName() + "(" + members.get(0).getContactToken() + ")");
        }else {
            User user = userProvider.findUserById(tracking.getTrackingUid());
            if(user!=null){
                dto.setTrackingUidName(user.getNickName() + "(" + user.getIdentifierToken() + ")");
            }
        }
        return dto;
    }

    @Override
    public CustomerTrackingDTO updateCustomerTracking(UpdateCustomerTrackingCommand cmd) {
//        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        if (InvitedCustomerType.INVITED_CUSTOMER.equals(InvitedCustomerType.fromCode(cmd.getCustomerSource()))) {
            //todo: check invited customer privilege
        } else {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        }
        CustomerTracking exist = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
        CustomerTracking tracking = ConvertHelper.convert(cmd, CustomerTracking.class);
        if (cmd.getTrackingTime() != null) {
            tracking.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
        }
        tracking.setCreateTime(exist.getCreateTime());
        tracking.setCreatorUid(exist.getCreatorUid());
        tracking.setCustomerType(exist.getCustomerSource());
        if (null != cmd.getContentImgUri()) {
            tracking.setContentImgUri(cmd.getContentImgUri());
        }
        enterpriseCustomerProvider.updateCustomerTracking(tracking);
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
//        customer.setLastTrackingTime(tracking.getTrackingTime());
        //更细客户表的最后跟进时间
        Timestamp maxTrackingTime = enterpriseCustomerProvider.getCustomerMaxTrackingTime(cmd.getCustomerId(), cmd.getCustomerSource());
        customer.setLastTrackingTime(maxTrackingTime);
        enterpriseCustomerProvider.updateCustomerLastTrackingTime(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        return ConvertHelper.convert(tracking, CustomerTrackingDTO.class);
    }

    @Override
    public void deleteCustomerTracking(DeleteCustomerTrackingCommand cmd) {
//        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        if (InvitedCustomerType.INVITED_CUSTOMER.equals(InvitedCustomerType.fromCode(cmd.getCustomerSource()))) {
            //todo: check invited customer privilege
        } else {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        }
        CustomerTracking tracking = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTracking(tracking);
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if (customer != null) {
            List<CustomerTracking> customerTrackings = enterpriseCustomerProvider.listCustomerTrackingsByCustomerId(customer.getId(),cmd.getCustomerSource());
            if (customerTrackings != null && customerTrackings.size() > 0) {
                customer.setLastTrackingTime(customerTrackings.get(0).getTrackingTime());
                enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
//                enterpriseCustomerSearcher.feedDoc(customer);
            }else {
                customer.setLastTrackingTime(null);
                enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            }
        }
    }

    @Override
    public void createCustomerTracking(CreateCustomerTrackingCommand cmd) {
        if (InvitedCustomerType.INVITED_CUSTOMER.equals(InvitedCustomerType.fromCode(cmd.getCustomerSource()))) {
            //todo: check invited customer privilege
        } else {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        }
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
        CustomerTracking tracking = ConvertHelper.convert(cmd, CustomerTracking.class);
        if (cmd.getTrackingTime() != null) {
            tracking.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
        }
        enterpriseCustomerProvider.createCustomerTracking(tracking);
//        customer.setLastTrackingTime(tracking.getTrackingTime());
        //更细客户表的最后跟进时间
        Timestamp maxTrackingTime = enterpriseCustomerProvider.getCustomerMaxTrackingTime(cmd.getCustomerId(), cmd.getCustomerSource());
        customer.setLastTrackingTime(maxTrackingTime);
        enterpriseCustomerProvider.updateCustomerLastTrackingTime(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
    }

    private CustomerTracking checkCustomerTracking(Long id, Long customerId) {
        CustomerTracking tracking = enterpriseCustomerProvider.findCustomerTrackingById(id);
        if (tracking == null || !tracking.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(tracking.getStatus()))) {
            LOGGER.error("enterprise customer tracking is not exist or inactive. id: {}, tracking: {}", id, tracking);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRACKING_NOT_EXIST,
                    "customer tracking is not exist or inactive");
        }
        return tracking;
    }

    @Override
    public List<CustomerTrackingPlanDTO> listCustomerTrackingPlans(ListCustomerTrackingPlansCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        List<CustomerTrackingPlan> plans = enterpriseCustomerProvider.listCustomerTrackingPlans(cmd.getCustomerId());
        if (plans != null && plans.size() > 0) {
            return plans.stream().map(plan -> {
                return convertCustomerTrackingPlanDTO(plan);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CustomerTrackingPlanDTO getCustomerTrackingPlan(GetCustomerTrackingPlanCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrackingPlan plan = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
        CustomerTrackingPlanDTO customerTrackingPlanDTO = convertCustomerTrackingPlanDTO(plan);
        //更新状态为已读
        enterpriseCustomerProvider.updateTrackingPlanReadStatus(cmd.getId());
        return customerTrackingPlanDTO;
    }

    private CustomerTrackingPlanDTO convertCustomerTrackingPlanDTO(CustomerTrackingPlan plan) {
        CustomerTrackingPlanDTO dto = ConvertHelper.convert(plan, CustomerTrackingPlanDTO.class);
        if (dto.getTrackingType() != null) {
            String trackingTypeName = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, Integer.parseInt(dto.getTrackingType().toString()), UserContext.current().getUser().getLocale(), new HashMap<>(), "");
//        	String trackingTypeName = fieldProvider.findScopeFieldItemByBusinessValue(plan.getNamespaceId(), communityId, ModuleName.ENTERPRISE_CUSTOMER.getName(), , plan.getTrackingType());
            dto.setTrackingTypeName(trackingTypeName);
        }
        return dto;
    }

    @Override
    public CustomerTrackingPlanDTO updateCustomerTrackingPlan(UpdateCustomerTrackingPlanCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrackingPlan exist = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
        CustomerTrackingPlan plan = ConvertHelper.convert(cmd, CustomerTrackingPlan.class);
        if (cmd.getTrackingTime() != null) {
            plan.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
        }
        plan.setNotifyStatus(TrackingPlanNotifyStatus.INVAILD.getCode());
        if (cmd.getNotifyTime() != null) {
            plan.setNotifyTime(new Timestamp(cmd.getNotifyTime()));
            plan.setNotifyStatus(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode());
        }
        plan.setCreateTime(exist.getCreateTime());
        plan.setCreatorUid(exist.getCreatorUid());
        enterpriseCustomerProvider.updateCustomerTrackingPlan(plan);
        return ConvertHelper.convert(plan, CustomerTrackingPlanDTO.class);
    }

    @Override
    public void deleteCustomerTrackingPlan(DeleteCustomerTrackingPlanCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_DELETE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrackingPlan plan = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTrackingPlan(plan);
    }

    private CustomerTrackingPlan checkCustomerTrackingPlan(Long id, Long customerId) {
        CustomerTrackingPlan plan = enterpriseCustomerProvider.findCustomerTrackingPlanById(id);
        if (plan == null || !plan.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(plan.getStatus()))) {
            LOGGER.error("enterprise customer tracking plan is not exist or inactive. id: {}, plan: {}", id, plan);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRACKING_NOT_EXIST,
                    "customer tracking plan is not exist or inactive");
        }
        return plan;
    }

    @Override
    public void createCustomerTrackingPlan(CreateCustomerTrackingPlanCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_CREATE, cmd.getOrgId(), cmd.getCommunityId());
        CustomerTrackingPlan plan = ConvertHelper.convert(cmd, CustomerTrackingPlan.class);
        if (cmd.getTrackingTime() != null) {
            plan.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
        }
        plan.setNotifyStatus(TrackingPlanNotifyStatus.INVAILD.getCode());
        if (cmd.getNotifyTime() != null) {
            plan.setNotifyTime(new Timestamp(cmd.getNotifyTime()));
            plan.setNotifyStatus(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode());
        }
        plan.setReadStatus(TrackingPlanReadStatus.UNREAD.getCode());
        enterpriseCustomerProvider.createCustomerTrackingPlan(plan);
    }

    @Override
    public void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist, Byte deviceType) {
        enterpriseCustomerProvider.saveCustomerEvent(i, customer, exist, deviceType);
    }


    public void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist, Byte deviceType, String moduleName) {
        enterpriseCustomerProvider.saveCustomerEvent(i, customer, exist, deviceType, moduleName);
    }

    @Override
    public List<CustomerEventDTO> listCustomerEvents(ListCustomerEventsCommand cmd) {
        List<CustomerEvent> events = enterpriseCustomerProvider.listCustomerEvents(cmd.getCustomerId());
        if (events != null && events.size() > 0) {
            return events.stream().map(this::convertCustomerEventDTO).collect(Collectors.toList());
        }
        return null;
    }

    private CustomerEventDTO convertCustomerEventDTO(CustomerEvent event) {
        CustomerEventDTO dto = ConvertHelper.convert(event, CustomerEventDTO.class);
        if (dto.getCreatorUid() != null) {
            User user = userProvider.findUserById(dto.getCreatorUid());
            if (user != null) {
                dto.setCreatorName(user.getNickName());
            }
        }
        if (dto.getCreateTime() != null) {
            dto.setOperateTime(dto.getCreateTime().toLocalDateTime().format(dateSF));
        }
        if (dto.getDeviceType() != null) {
            CustomerEventType type = CustomerEventType.fromCode(dto.getDeviceType());
            if (type != null) {
                dto.setSourceType(type.getValue());
            }
        }
        return dto;
    }

    @Override
    public void createCustomerTalentFromOrgMember(Long orgId, OrganizationMember member) {
        EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(orgId);
        if(customer != null) {
            CustomerTalent talent = enterpriseCustomerProvider.findCustomerTalentByPhone(member.getContactToken(), customer.getId());
            if(talent == null) {
                talent = new CustomerTalent();
                talent.setNamespaceId(customer.getNamespaceId());
                talent.setCustomerId(customer.getId());
                talent.setCustomerType(CustomerType.ENTERPRISE.getCode());
                talent.setName(member.getContactName());
                talent.setPhone(member.getContactToken());
                talent.setMemberId(member.getId());
                enterpriseCustomerProvider.createCustomerTalent(talent);
            } else {
                talent.setMemberId(member.getId());
                enterpriseCustomerProvider.updateCustomerTalent(talent);
            }
            // change customer talent memebers register status  20180709
            CustomerTalent potentialTalent = enterpriseCustomerProvider.findCustomerTalentByPhone(member.getContactToken(), customer.getId());
            potentialTalent.setRegisterStatus(CommonStatus.ACTIVE.getCode());
            enterpriseCustomerProvider.updateCustomerTalent(talent);
        }

    }
    @Override
    public void allotEnterpriseCustomer(AllotEnterpriseCustomerCommand cmd) {
        //checkPrivilege();
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        //zuolin base
        customer.setOwnerId(cmd.getOrgId());
        EnterpriseCustomer exist = enterpriseCustomerProvider.findById(cmd.getId());
        Long originalTrackingUid = exist.getTrackingUid();
        String orignalTrackingName = exist.getTrackingName();
        Long currentTrackingUid = cmd.getTrackingUid();
        String currentTrackingName = null;
        customer.setTrackingUid(cmd.getTrackingUid());
        customer.setTrackingName(null);
        if (cmd.getTrackingUid() != null) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(cmd.getTrackingUid());
            currentTrackingName = detail.getContactName();
            if (null != detail && null != detail.getContactName()) {
                customer.setTrackingName(detail.getContactName());
            }
        }
        //保存事件
        saveCustomerEvent(3, customer, exist,cmd.getDeviceType());
        enterpriseCustomerProvider.allotEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        routeMsgForTrackingChanged(originalTrackingUid, currentTrackingUid, orignalTrackingName, currentTrackingName,
                customer.getName(), null, false, cmd.getOrgId());
    }

    @Override
    public void giveUpEnterpriseCustomer(GiveUpEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        Long originalTrackingUid = customer.getTrackingUid();
        String orignalTrackingName = customer.getTrackingName();
        Long currentTrackingUid = null;
        String currentTrackingName = null;
        //查看当前用户是否和跟进人一致
        if (null == customer.getTrackingUid() || !(customer.getTrackingUid().toString()).equals(UserContext.currentUserId() == null ? "" : UserContext.currentUserId().toString())) {
            LOGGER.error("enterprise customer do not contains trackingUid or not the same uid. id: {}, customer: {} ,current:{}", cmd.getId(), customer, UserContext.currentUserId());
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                    "enterprise customer do not contains trackingUid or not the same uid");
        }
        customer.setTrackingUid(null);
        customer.setTrackingName(null);
        enterpriseCustomerProvider.giveUpEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        routeMsgForTrackingChanged(originalTrackingUid, currentTrackingUid, orignalTrackingName, currentTrackingName,
                customer.getName(), null, false, cmd.getOrgId());
    }

    @Override
    public ListNearbyEnterpriseCustomersCommandResponse listNearbyEnterpriseCustomers(ListNearbyEnterpriseCustomersCommand cmd) {
        ListNearbyEnterpriseCustomersCommandResponse resp = new ListNearbyEnterpriseCustomersCommandResponse();
        List<EnterpriseCustomerDTO> results = new ArrayList<EnterpriseCustomerDTO>();

        if (cmd.getLatitude() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");


        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<EnterpriseCustomerDTO> customers = this.enterpriseCustomerProvider.findEnterpriseCustomersByDistance(cmd, locator, pageSize + 1);
        if (customers != null) {
            for (EnterpriseCustomerDTO customer : customers) {
                results.add(customer);
            }
        }
        if (results != null && results.size() > pageSize) {
            resp.setNextPageAnchor(results.get(results.size() - 1).getId());
            results.remove(results.size() - 1);
        }
        if (results != null) {
            resp.setDtos(results);
        }
        return resp;
    }


    //每15分钟执行一次，找出待n~n+15分钟内提醒时间的跟进计划
    @Scheduled(cron = "0 0/15 * * * ?")
    @Override
    public void trackingPlanWarningSchedule() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            LOGGER.info("trackingPlanWarningSchedule is running...");
            //使用tryEnter方法可以防止分布式部署时重复执行
            coordinationProvider.getNamedLock(CoordinationLocks.TRACKING_PLAN_WARNING_SCHEDULE.getCode()).tryEnter(() -> {
                Date now = DateHelper.currentGMTTime();
                Timestamp queryStartTime = new Timestamp(now.getTime());
                Timestamp queryEndTime = new Timestamp(now.getTime() + 900 * 1000);
                List<CustomerTrackingPlan> plans = enterpriseCustomerProvider.listWaitNotifyTrackingPlans(queryStartTime, queryEndTime);
                if (null != plans && plans.size() > 0) {
                    plans.forEach(plan -> {
                        pushPlanIntoEnqueue(plan);
                    });
                }
            });
        }
    }

    private void pushPlanIntoEnqueue(CustomerTrackingPlan plan) {
        Map<String, Object> map = new HashMap<>();
        map.put("trackingPlanId", plan.getId());
        if (plan.getNotifyTime().getTime() > (DateHelper.currentGMTTime().getTime() + 10L)) {
            scheduleProvider.scheduleSimpleJob(
                    queueDelay + plan.getId(),
                    queueDelay + plan.getId(),
                    new Date(plan.getNotifyTime().getTime()),
                    TrackingPlanNotifytJob.class,
                    map
            );

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("pushTrackingPlanNotify delayedEnqueue trackingPlan = {}", plan);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("pushTrackingPlanNotify noDelayedenqueue trackingPlan = {}", plan);
            }
            scheduleProvider.scheduleSimpleJob(
                    queueNoDelay + plan.getId(),
                    queueNoDelay + plan.getId(),
                    new Date(System.currentTimeMillis() + 1000),
                    TrackingPlanNotifytJob.class,
                    map
            );
        }
    }

    @Override
    public void processTrackingPlanNotify(CustomerTrackingPlan plan) {
        Long userId = plan.getCreatorUid();
        LOGGER.info("processTrackingPlanNotify userId:{}", userId);
        if (userId != null && null != plan.getTrackingTime()) {
            String taskName = plan.getTitle() == null ? "" : plan.getTitle();
            Timestamp time = plan.getTrackingTime();
            String customerName = plan.getCustomerName();
            int code = TrackingNotifyTemplateCode.TRACKING_NEARLY_REACH_NOTIFY;
            String scope = TrackingNotifyTemplateCode.SCOPE;
            String locale = "zh_CN";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("processPmNotifyRecord, userId={}, recordId={}", userId, plan.getId());
            }
            TrackingNotifyLog log = new TrackingNotifyLog();
            log.setCustomerType(plan.getCustomerType());
            log.setCustomerId(plan.getCustomerId());
            log.setReceiverId(userId);
            String notifyTextForApplicant = getMessage(customerName, taskName, time, scope, locale, code);
            sendMessageToUser(userId, notifyTextForApplicant);
            log.setNotifyText(notifyTextForApplicant);
            enterpriseCustomerProvider.createTrackingNotifyLog(log);
        }
    }

    private String getMessage(String customerName, String taskName, Timestamp time, String scope, String locale, int code) {
        Map<String, Object> notifyMap = new HashMap<String, Object>();
        notifyMap.put("taskName", taskName);
        notifyMap.put("time", timeToStr(time));
        notifyMap.put("customerName", customerName);
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
        return notifyTextForApplicant;
    }

    private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    private void sendMessageToUser(Long userId, String content) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    @Override
    public List<List<CustomerTrackingPlanDTO>> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd) {
        List<List<CustomerTrackingPlanDTO>> planList = new ArrayList<>();
        List<CustomerTrackingPlanDTO> todayPlan = new ArrayList<>();
        List<CustomerTrackingPlanDTO> tomorrowPlan = new ArrayList<>();
        List<CustomerTrackingPlanDTO> passOrFuturePlan = new ArrayList<>();
        List<CustomerTrackingPlan> plans = null;
        Long todayFirst = getTodayFirstTimestamp();
        Long todayLast = getTodayLastTimestamp();
        Long tomorrowLast = getTomorrowLastTimestamp();
        //历史记录
        if (StringUtils.isNotEmpty(cmd.getGetHistory()) && "1".equals(cmd.getGetHistory())) {
            plans = enterpriseCustomerProvider.listCustomerTrackingPlansByDate(cmd, todayFirst);
            plans.forEach(plan -> {
                passOrFuturePlan.add(convertCustomerTrackingPlanDTO(plan));
            });
            Collections.sort(passOrFuturePlan);
            planList.add(passOrFuturePlan);
            return planList;
        }
        //今天 & 明天  & 以后
        plans = enterpriseCustomerProvider.listCustomerTrackingPlansByDate(cmd, new Timestamp(todayFirst));
        plans.forEach(plan -> {
            if (null != plan.getTrackingTime()) {
                Long trackingTime = plan.getTrackingTime().getTime();
                if (trackingTime <= todayLast && trackingTime >= todayFirst) {
                    todayPlan.add(convertCustomerTrackingPlanDTO(plan));
                } else if (trackingTime > tomorrowLast) {
                    passOrFuturePlan.add(convertCustomerTrackingPlanDTO(plan));
                } else {
                    tomorrowPlan.add(convertCustomerTrackingPlanDTO(plan));
                }
            }
        });
        Collections.sort(todayPlan);
        Collections.sort(tomorrowPlan);
        Collections.sort(passOrFuturePlan);
        planList.add(todayPlan);
        planList.add(tomorrowPlan);
        planList.add(passOrFuturePlan);
        return planList;
    }

    private Long getTodayFirstTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    private Long getTodayLastTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    private Long getTomorrowLastTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    @Override
    public List<OrganizationMemberDTO> listCommunityUserRelatedTrackUsers(ListCommunitySyncResultCommand cmd) {
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<OrganizationMemberDTO> members = new ArrayList<>();
        if (customers != null && customers.size() > 0) {
            customers.forEach((customer) -> {
                List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembersByUId(customer.getTrackingUid());
                if (organizationMembers != null && organizationMembers.size() > 0) {
                    members.add(ConvertHelper.convert(organizationMembers.get(0), OrganizationMemberDTO.class));
                }
            });
        }
        Map<Long, OrganizationMemberDTO> memberDTOMap = new HashMap<>();
        if (members != null && members.size() > 0) {
            members.forEach((r) -> {
                memberDTOMap.putIfAbsent(r.getTargetId(), r);
            });
        }
        List<OrganizationMemberDTO> result = new ArrayList<>();
        result = new ArrayList<>(memberDTOMap.values());
        return result;
    }

    @Override
    public List<OrganizationMemberDTO> listCommunityRelatedMembers(ListCommnutyRelatedMembersCommand cmd) {

        List<OrganizationMemberDTO> relatedMembers = new ArrayList<>();
        List<AuthorizationRelation> authorizationRelations = enterpriseCustomerProvider
                .listAuthorizationRelations(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getModuleId(), cmd.getAppId(), cmd.getCommunityId());

        authorizationRelations.forEach((r) -> {
            String targetJson = r.getTargetJson();
            AssignmentTarget[] targetArr = (AssignmentTarget[]) StringHelper.fromJsonString(targetJson, AssignmentTarget[].class);
            List<AssignmentTarget> targets = Arrays.asList(targetArr);
            targets.forEach((p) -> {
                //按用户分配的模块权限
                if (EntityType.USER == EntityType.fromCode(p.getTargetType())) {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(p.getTargetId());
                    if (members != null && members.size() > 0)
                        relatedMembers.add(ConvertHelper.convert(members.get(0), OrganizationMemberDTO.class));

                } else if (EntityType.ORGANIZATIONS == EntityType.fromCode(p.getTargetType())) {
//                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(p.getTargetId());
                    // organization manager module has their children apartment concept
                    ListOrganizationContactCommand memberCommand = new ListOrganizationContactCommand();
                    memberCommand.setOrganizationId(p.getTargetId());
                    memberCommand.setNamespaceId(cmd.getNamespaceId());
                    memberCommand.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CHILD_DEPARTMENT.getCode()));
                    memberCommand.setTargetTypes(Arrays.asList(OrganizationGroupType.DEPARTMENT.getCode(), OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode()));
                    memberCommand.setPageSize(Integer.MAX_VALUE-1);
                    ListOrganizationMemberCommandResponse membersResponse = organizationService.listOrganizationPersonnelsWithDownStream(memberCommand);
                    if (membersResponse.getMembers() != null && membersResponse.getMembers().size() > 0) {
//                        relatedMembers.addAll(members.stream().map((member) -> ConvertHelper.convert(member, OrganizationMemberDTO.class)).collect(Collectors.toList()));
                        relatedMembers.addAll(membersResponse.getMembers());
                    }

                }
            });
        });
        //获取超级管理员和应用管理员
        ListServiceModuleAdministratorsCommand administratorsCommand = new ListServiceModuleAdministratorsCommand();
        administratorsCommand.setModuleId(cmd.getModuleId());
        administratorsCommand.setActivationFlag(ActivationFlag.YES.getCode());
        administratorsCommand.setOrganizationId(cmd.getOwnerId());
        administratorsCommand.setOwnerId(cmd.getOwnerId());
        administratorsCommand.setOwnerType(cmd.getOwnerType());
        ListServiceModuleAppsAdministratorResponse moduleAppsAdministratorResponse = rolePrivilegeService.listServiceModuleAppsAdministrators(administratorsCommand);
        List<OrganizationContactDTO> superAdmins = rolePrivilegeService.listOrganizationSuperAdministrators(administratorsCommand);
        List<OrganizationMemberDTO> admins = getAdminUsers(moduleAppsAdministratorResponse,superAdmins,cmd.getCommunityId());

        if(admins!=null && admins.size()>0){
            relatedMembers.addAll(admins);
        }
        if (relatedMembers.size() > 0) {
            relatedMembers.forEach((r) ->{
                Organization organization =  organizationProvider.findOrganizationById(r.getOrganizationId());
                if(organization!=null){
                    r.setDepartmentName(organization.getName());
                } else {
                    r.setDepartmentName("");
                }
            });
        }
        // remove duplicated members
        return removeDuplicatedMembers(relatedMembers);
    }

    private List<OrganizationMemberDTO> removeDuplicatedMembers(List<OrganizationMemberDTO> relatedMembers) {
        Map<Long, OrganizationMemberDTO> map = new HashMap<>();
        if (relatedMembers != null && relatedMembers.size() > 0) {
            relatedMembers.forEach(r ->{
                if(r.getTargetId() != 0) {
                    map.putIfAbsent(r.getTargetId(), r);
                }
            });
            return new ArrayList<>(map.values());
        }
        return new ArrayList<>();
    }

    private List<OrganizationMemberDTO> getAdminUsers(ListServiceModuleAppsAdministratorResponse moduleAppsAdministratorResponse, List<OrganizationContactDTO> superAdmins, Long communityId) {
        List<OrganizationMemberDTO> users = new ArrayList<>();
        if (superAdmins != null && superAdmins.size() > 0) {
            superAdmins.forEach(s -> {
                OrganizationMemberDTO dto = new OrganizationMemberDTO();
                dto.setTargetId(s.getTargetId());
                dto.setTargetType(s.getTargetType());
                dto.setContactName(s.getContactName());
                dto.setGender(s.getGender());
                dto.setContactToken(s.getContactToken());
                dto.setOrganizationId(s.getOrganizationId());
                users.add(dto);
            });
        }
        List<ServiceModuleAppsAuthorizationsDto> moduleAdmins = moduleAppsAdministratorResponse.getDtos();
        if (moduleAdmins != null && moduleAdmins.size() > 0) {
            moduleAdmins.forEach((r) -> {
                //这里权限那边并不会返回null  getCommunityControlIds
                List<Long> communityControlIds = new ArrayList<>();
                if (r.getCommunityControlApps() != null) {
                    communityControlIds = r.getCommunityControlApps().getCommunityControlIds();
                }
//                if (AllFlag.ALL.equals(AllFlag.fromCode(r.getAllFlag())) || (communityControlIds != null && communityControlIds.contains(communityId))) {
                if ((communityControlIds == null || communityControlIds.size() == 0) || communityControlIds.contains(communityId)) {
                    OrganizationMemberDTO dto = new OrganizationMemberDTO();
                    dto.setTargetId(r.getTargetId());
                    dto.setTargetType(r.getTargetType());
                    dto.setContactName(r.getContactName());
                    dto.setGender(r.getGender());
                    dto.setContactToken(r.getIdentifierToken());
                    dto.setOrganizationId(r.getOwnerId());
                    users.add(dto);
                }
            });
        }
        return users;
    }

    @Override
    public Byte checkCustomerCurrentUserAdmin(ListCommnutyRelatedMembersCommand cmd) {
        Boolean result = checkCustomerAdmin(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId());
        if (result) {
            return (byte) 1;
        } else {
            return (byte) 0;
        }
    }

    @Override
    public void createOrganizationAdmin(CreateOrganizationAdminCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANNAGER_SET, cmd.getOwnerId(), cmd.getCommunityId());
        //用customerId 找到企业管理中的organizationId
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getContactToken());
        String contactType = null;
        if (null != userIdentifier) {
            contactType = OrganizationMemberTargetType.USER.getCode();
        } else {
            contactType = OrganizationMemberTargetType.UNTRACK.getCode();
        }
        if (customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0) {
            cmd.setOrganizationId(customer.getOrganizationId());
            rolePrivilegeService.createOrganizationAdmin(cmd);
            List<CustomerAdminRecord> records = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecordsByToken(customer.getId(), cmd.getContactToken());
            if (records == null || records.size() == 0)
            enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getCustomerId(), cmd.getContactName(), contactType, cmd.getContactToken(),customer.getNamespaceId());
        } else if (customer != null) {
            //如果属于未认证的 只记录下管理员信息  在添加楼栋门牌和签约的时候激活管理员即可
            // 旧版的模式为新建客户则关联企业客户中organizationId，现在为激活才增加organizationId
            List<CustomerAdminRecord> records = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecordsByToken(customer.getId(), cmd.getContactToken());
            if (records == null || records.size() == 0)
            enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getCustomerId(), cmd.getContactName(), contactType, cmd.getContactToken(),customer.getNamespaceId());
        }
        if (customer != null) {
            customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
        }
    }

    @Override
    public void deleteOrganizationAdmin(DeleteOrganizationAdminCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANNAGER_SET, cmd.getOwnerId(), cmd.getCommunityId());
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
        //删除客户管理中的管理员记录
        enterpriseCustomerProvider.deleteEnterpriseCustomerAdminRecord(cmd.getCustomerId(), cmd.getContactToken());
        if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0) {
            //删除企业管理中的管理员权限
//            rolePrivilegeService.deleteOrganizationAdministrators(cmd);
            //标准版 平台组更换接口
            rolePrivilegeService.deleteOrganizationSuperAdministrators(cmd);
        }
        List<CustomerAdminRecord> customerAdminRecords = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecords(cmd.getCustomerId(), null);
        if (customerAdminRecords != null && customerAdminRecords.size() > 0) {
            customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        }else {
            customer.setAdminFlag(TrueOrFalseFlag.FALSE.getCode());
        }
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
    }

    @Override
    public List<OrganizationContactDTO> listOrganizationAdmin(ListServiceModuleAdministratorsCommand cmd) {
//        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANNAGER_LIST, cmd.getOwnerId(), cmd.getCommunityId());
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
        List<OrganizationContactDTO> result = new ArrayList<>();
        // 旧版模式导致存在customer 和organization record不一致的问题
        String contactType = null;
        if (ActivationFlag.YES == ActivationFlag.fromCode(cmd.getActivationFlag())) {
            contactType = OrganizationMemberTargetType.USER.getCode();
        } else if (ActivationFlag.NO == ActivationFlag.fromCode(cmd.getActivationFlag())) {
            contactType = OrganizationMemberTargetType.UNTRACK.getCode();
        }
        List<CustomerAdminRecord> customerAdminRecords = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecords(cmd.getCustomerId(), contactType);
        //reflect map
        List<OrganizationContactDTO> customerAdminContacts = new ArrayList<>();
        List<CustomerAdminRecord> adminRecords = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecords(cmd.getCustomerId(), null);
        if (customerAdminRecords != null && customerAdminRecords.size() > 0) {
            customerAdminContacts = processOrganizationMembers(customerAdminRecords);
        }
        if (null == adminRecords || adminRecords.size() == 0) {
            cmd.setOrganizationId(customer.getOrganizationId() == null ? 0 : customer.getOrganizationId());
//            result = rolePrivilegeService.listOrganizationAdministrators(cmd);
            // 标准版修改成此种概念
            if(cmd.getOrganizationId() != null && cmd.getOrganizationId() != 0) {
                OrganizationContactDTO organizationAdmin = rolePrivilegeService.listOrganizationTopAdministrator(cmd);
                if(organizationAdmin != null){
                    result = new ArrayList<>();
                    result.add(organizationAdmin);
                    result.forEach((admin) -> enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getCustomerId(), admin.getContactName(), admin.getTargetType(), admin.getContactToken(),customer.getNamespaceId()));
                    customer.setAdminFlag(AdminFlag.YES.getCode());
                    enterpriseCustomerSearcher.feedDoc(customer);
                }
            } else if(result == null || result.size() < 1){
                ListOrganizationContectDTOResponse organizationAdmins = rolePrivilegeService.listOrganizationSystemAdministrators(cmd);
                result = organizationAdmins == null ? null : organizationAdmins.getDtos();
                //复制organization管理员到企业客户管理中来
                if (result != null && result.size() > 0) {
                    result.forEach((admin) -> enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getCustomerId(), admin.getContactName(), admin.getTargetType(), admin.getContactToken(), customer.getNamespaceId()));
                    customer.setAdminFlag(AdminFlag.YES.getCode());
                    enterpriseCustomerSearcher.feedDoc(customer);
                }
            }
        } else {
            result = customerAdminContacts;
        }
        if(result != null && result.size() > 0){
            List<OrganizationContactDTO> oneAdmin = new ArrayList<>();
            oneAdmin.add(result.get(0));
            return oneAdmin;
        }
        return result;
    }

    private List<OrganizationContactDTO> processOrganizationMembers(List<CustomerAdminRecord> customerAdminRecords) {
        List<OrganizationContactDTO> dtos = new ArrayList<>();
        if (customerAdminRecords != null && customerAdminRecords.size() > 0) {
            customerAdminRecords.forEach((r) -> {
                OrganizationContactDTO dto = new OrganizationContactDTO();
                dto.setContactName(r.getContactName());
                dto.setContactToken(r.getContactToken());
                dtos.add(dto);
            });
        }
        return dtos;
    }

    @Override
    public void syncOrganizationToCustomer(SyncCustomerDataCommand cmd) {
        //防止执行两次  之前出现过
        Accessor accessor = bigCollectionProvider.getMapAccessor(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + System.currentTimeMillis(), "");
        RedisTemplate redisTemplate = accessor.getTemplate(stringRedisSerializer);
        String runningFlag = getSyncTaskToken(redisTemplate,CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode());
        if(StringUtils.isEmpty(runningFlag)) {
            redisTemplate.opsForValue().set(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode(), "executing", 2, TimeUnit.HOURS);
            List<Organization> organizations = enterpriseCustomerProvider.listNoSyncOrganizations(cmd.getNamespaceId());
            if (organizations != null && organizations.size() > 0) {
                organizations.forEach((organization -> {
                    try {
                        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
                        List<OrganizationAddress> addresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());
                        //                OrganizationCommunityRequest request = organizationProvider.getOrganizationRequest(organization.getId());
                        OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());
                        //                OrganizationDetail org = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
                        List<OrganizationAttachment> banners = organizationProvider.listOrganizationAttachments(organization.getId());
                        List<OrganizationAddressDTO> addressDTOs = new ArrayList<>();
                        if (addresses != null && addresses.size() > 0) {
                            addressDTOs = addresses.stream().map((address) -> ConvertHelper.convert(address, OrganizationAddressDTO.class)).collect(Collectors.toList());
                        }
                        String avatar = "";
                        String postUri = "";
                        if (organizationDetail == null) {
                            organizationDetail = new OrganizationDetail();
                        } else {
                            avatar = organizationDetail.getAvatar();
                            postUri = organizationDetail.getPostUri();
                        }
                        Long communityId = 0L;
                        if (request != null) {
                            communityId = request.getCommunityId();
                        }
                        createEnterpriseCustomer(organization, avatar, banners, postUri, organizationDetail, communityId, addressDTOs);
                    } catch (Exception e) {
                        LOGGER.error("error organizationId ={}", organization.getId());
                        LOGGER.error("sync organziation to customer error :{}", e);
                    }
                }));
            }
        }
    }

    private String getSyncTaskToken(RedisTemplate redisTemplate,String code) {
        Map<String, String> map = makeSyncTaskToken(redisTemplate,code);
        if(map == null) {
            return null;
        }
        return  map.get(code);
    }

    private Map<String, String> makeSyncTaskToken(RedisTemplate redisTemplate,String code) {
        Object o = redisTemplate.opsForValue().get(code);
        if(o != null) {
            Map<String, String> keys = new HashMap<>();
            keys.put(code, (String)o);
            return keys;
        } else {
            return null;
        }
    }

    private void createEnterpriseCustomer(Organization organization, String logo, List<OrganizationAttachment> banner , String postUri, OrganizationDetail enterprise, Long communityId, List<OrganizationAddressDTO> addressDTOs) {
//        if(organization.getNamespaceId() == 999971 || organization.getNamespaceId() == 999983) {
//            LOGGER.error("Insufficient privilege, createEnterpriseCustomer");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(organization.getNamespaceId(), communityId, organization.getName());
        if (customers != null && customers.size() > 0) {
            EnterpriseCustomer customer = customers.get(0);
            customer.setOrganizationId(organization.getId());
            customer.setCorpWebsite(organization.getWebsite());
            customer.setCorpLogoUri(logo);
            customer.setContactAddress(enterprise.getAddress());
            customer.setLatitude(enterprise.getLatitude());
            customer.setLongitude(enterprise.getLongitude());
            customer.setCorpEntryDate(enterprise.getCheckinDate());
            customer.setCorpOpAddress(enterprise.getAddress());
            customer.setCorpDescription(enterprise.getDescription());
            customer.setCorpEmployeeAmount(enterprise.getMemberCount() == null ? null : enterprise.getMemberCount().intValue());
            customer.setPostUri(postUri);
            customer.setNickName(enterprise.getDisplayName());
            customer.setHotline(enterprise.getContact());
            customer.setCorpEmail(enterprise.getEmailDomain());
            customer.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            //企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
            this.updateCustomerEntryInfo(customer, addressDTOs,banner);

        } else {
            EnterpriseCustomer customer = new EnterpriseCustomer();
            customer.setCommunityId(communityId);
            customer.setNamespaceId(organization.getNamespaceId());
            customer.setOrganizationId(organization.getId());
            customer.setName(organization.getName());
            customer.setCorpEntryDate(enterprise.getCheckinDate());
            customer.setCorpWebsite(organization.getWebsite());
            customer.setCorpLogoUri(logo);
            customer.setContactAddress(enterprise.getAddress());
            customer.setLatitude(enterprise.getLatitude());
            customer.setLongitude(enterprise.getLongitude());
            customer.setCorpDescription(enterprise.getDescription());
            customer.setCorpEmployeeAmount(enterprise.getMemberCount() == null ? null : enterprise.getMemberCount().intValue());
            customer.setPostUri(postUri);
            customer.setNickName(enterprise.getDisplayName());
            customer.setHotline(enterprise.getContact());
            customer.setCorpEmail(enterprise.getEmailDomain());
            customer.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            //企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
            this.updateCustomerEntryInfo(customer, addressDTOs,banner);
        }
    }

    private void updateCustomerEntryInfo(EnterpriseCustomer customer, List<OrganizationAddressDTO> addressDTOs, List<OrganizationAttachment> banner) {
        LOGGER.debug("updateCustomerEntryInfo customer id: {}, address: {}", customer.getId(), addressDTOs);
        if (addressDTOs != null && addressDTOs.size() > 0) {
            List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
            List<Long> addressIds = new ArrayList<>();
            if (entryInfos != null && entryInfos.size() > 0) {
                addressIds = entryInfos.stream().map(EhCustomerEntryInfos::getAddressId).collect(Collectors.toList());
            }

            for (OrganizationAddressDTO organizationAddressDTO : addressDTOs) {
                if (addressIds.contains(organizationAddressDTO.getAddressId())) {
                    continue;
                }

                CustomerEntryInfo info = new CustomerEntryInfo();
                info.setNamespaceId(customer.getNamespaceId());
                info.setCustomerType(CustomerType.ENTERPRISE.getCode());
                info.setCustomerId(customer.getId());
                info.setCustomerName(customer.getName());
                info.setAddressId(organizationAddressDTO.getAddressId());
                info.setBuildingId(organizationAddressDTO.getBuildingId());
                enterpriseCustomerProvider.createCustomerEntryInfo(info);
            }
        }
        if (banner != null && banner.size() > 0) {
            List<AttachmentDescriptor> bannerList = new ArrayList<>();
            banner.forEach((b) -> {
                AttachmentDescriptor attachment = new AttachmentDescriptor();
                attachment.setContentUri(b.getContentUri());
                attachment.setContentUrl(b.getContentUrl());
                attachment.setContentType(b.getContentType());
                bannerList.add(attachment);
            });
            enterpriseCustomerProvider.updateEnterpriseBannerUri(customer.getId(), bannerList);
        }
    }

    @Override
    public HttpServletResponse exportCustomerDetails(ListEnterpriseCustomerStatisticsCommand cmd,HttpServletResponse httpResponse) {

//        ListCustomerAnnualStatisticsCommand annualStatisticsCommand = ConvertHelper.convert(cmd, ListCustomerAnnualStatisticsCommand.class);
        //各园区企业企业分布
        EnterpriseCustomerStatisticsDTO customerStatisticsDTO =  listEnterpriseCustomerStatistics(cmd);
//        ListCustomerAnnualStatisticsResponse annualStatisticsResponse =  listCustomerAnnualStatistics(annualStatisticsCommand);
        //企业行业分布
        CustomerIndustryStatisticsResponse industryStatisticsResponse = listCustomerIndustryStatistics(cmd);
        //企业人才分布
        CustomerTalentStatisticsResponse talentStatisticsResponse = listCustomerTalentStatistics(cmd);
        //知识产权分布
        CustomerIntellectualPropertyStatisticsResponse statisticsResponse = listCustomerIntellectualPropertyStatistics(cmd);
        //获批项目分布
        CustomerProjectStatisticsResponse projectStatisticsResponse = listCustomerProjectStatistics(cmd);
        //认知途径分布
        CustomerSourceStatisticsResponse sourceStatisticsResponse = listCustomerSourceStatistics(cmd);

        URL rootPath = CustomerServiceImpl.class.getResource("/");
        String filePath = rootPath.getPath() + this.downloadDir;
        File file = new File(filePath);
        if (!file.exists()){
            boolean mdkirResult =   file.mkdirs();
            LOGGER.info("mkdir  excel file result :{}",mdkirResult);
        }
        filePath = filePath + "enterprise_customer_details" + System.currentTimeMillis() + ".xlsx";
        //  new file
        this.createEquipmentStandardsBook(filePath, customerStatisticsDTO, null,
                industryStatisticsResponse.getDtos(), talentStatisticsResponse.getDtos(), statisticsResponse.getDtos(), projectStatisticsResponse.getDtos(), sourceStatisticsResponse.getDtos());

        return download(filePath, httpResponse);
    }

    private void createEquipmentStandardsBook(String filePath, EnterpriseCustomerStatisticsDTO customerStatisticsDTO, List<CustomerAnnualStatisticDTO> statisticDTOs, List<CustomerIndustryStatisticsDTO> dtos, List<CustomerTalentStatisticsDTO> dtos1, List<CustomerIntellectualPropertyStatisticsDTO> dtos2, List<CustomerProjectStatisticsDTO> dtos3, List<CustomerSourceStatisticsDTO> dtos4) {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet customerStatisticSheet = wb.createSheet("各园区企业分布");
        Sheet industrySheet = wb.createSheet("企业行业分布");
        Sheet talentSheet = wb.createSheet("企业人才分布");
        Sheet intellectualPropertySheet = wb.createSheet("知识产权分布");
        Sheet projectSheet = wb.createSheet("获批项目分布");
        Sheet sourceSheet = wb.createSheet("认知途径分布");
        customerStatisticSheet.setDefaultColumnWidth(20 * 256);
        industrySheet.setDefaultColumnWidth(20 * 256);
        talentSheet.setDefaultColumnWidth(20 * 256);
        intellectualPropertySheet.setDefaultColumnWidth(20 * 256);
        projectSheet.setDefaultColumnWidth(20 * 256);
        sourceSheet.setDefaultColumnWidth(20 * 256);
        // 设置sheet页面表头和cell数据
        setNewCustomerStatisticSheetHeadAndBookRow(customerStatisticSheet, customerStatisticsDTO);
        setNewIndustrySheetHeadAndBookRow(industrySheet, dtos);
        setNewTalentSheetHeadAndBookRow(talentSheet, dtos1);
        setNewIntellectualPropertySheetHeadAndBookRow(intellectualPropertySheet, dtos2);
        setNewProjectSheetHeadAndBookRow(projectSheet, dtos3);
        setNewSourceSheetHeadAndBookRow(sourceSheet, dtos4);

        try {
            FileOutputStream out = new FileOutputStream(filePath);
            wb.write(out);
            wb.close();
            out.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
                    EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
                    e.getLocalizedMessage());
        }
    }

    private void setNewSourceSheetHeadAndBookRow(Sheet sourceSheet, List<CustomerSourceStatisticsDTO> dtos4) {
        Row row = sourceSheet.createRow(sourceSheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("认知途径");
        row.createCell(++i).setCellValue("数量");
        if (dtos4 != null && dtos4.size() > 0) {
            dtos4.forEach((r) -> setNewSourceSheetBookRow(sourceSheet, r));
        }
    }

    private void setNewSourceSheetBookRow(Sheet sourceSheet, CustomerSourceStatisticsDTO dto) {
        Row row = sourceSheet.createRow(sourceSheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getItemName() == null ? "未知" :dto.getItemName());
        row.createCell(++i).setCellValue(dto.getCustomerCount());
    }

    private void setNewProjectSheetHeadAndBookRow(Sheet projectSheet, List<CustomerProjectStatisticsDTO> dtos3) {
        Row row = projectSheet.createRow(projectSheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("获批项目");
        row.createCell(++i).setCellValue("项目数量");
        if(dtos3!=null && dtos3.size()>0){
            dtos3.forEach((r) -> setNewProjectSheetBookRow(projectSheet, r));
        }
    }

    private void setNewProjectSheetBookRow(Sheet projectSheet, CustomerProjectStatisticsDTO dto) {
        Row row = projectSheet.createRow(projectSheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getItemName() == null ? "未知" : dto.getItemName());
        row.createCell(++i).setCellValue(dto.getProjectCount());
    }

    private void setNewIntellectualPropertySheetHeadAndBookRow(Sheet intellectualPropertySheet, List<CustomerIntellectualPropertyStatisticsDTO> dtos2) {
        Row row = intellectualPropertySheet.createRow(intellectualPropertySheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("知识产权");
        row.createCell(++i).setCellValue("数量");
        if (dtos2 != null && dtos2.size() > 0) {
            dtos2.forEach((r) -> setNewIntellectualPropertySheetBookRow(intellectualPropertySheet, r));
        }
    }

    private void setNewIntellectualPropertySheetBookRow(Sheet intellectualPropertySheet, CustomerIntellectualPropertyStatisticsDTO dto) {
        Row row = intellectualPropertySheet.createRow(intellectualPropertySheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getPropertyType() == null ? "未知" : dto.getPropertyType());
        row.createCell(++i).setCellValue(dto.getPropertyCount());
    }

    private void setNewTalentSheetHeadAndBookRow(Sheet talentSheet, List<CustomerTalentStatisticsDTO> dtos1) {
        Row row = talentSheet.createRow(talentSheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("人才类型");
        row.createCell(++i).setCellValue("人才数");
        if (dtos1 != null && dtos1.size() > 0) {
            dtos1.forEach((r) -> setTalentSheetBookRow(talentSheet, r));
        }
    }

    private void setTalentSheetBookRow(Sheet talentSheet, CustomerTalentStatisticsDTO dto) {
        Row row = talentSheet.createRow(talentSheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getCategoryName() == null ? "未知" : dto.getCategoryName());
        row.createCell(++i).setCellValue(dto.getCustomerMemberCount() == null ? 0L : dto.getCustomerMemberCount());
    }

    private void setNewIndustrySheetHeadAndBookRow(Sheet industrySheet, List<CustomerIndustryStatisticsDTO> dtos) {
        Row row = industrySheet.createRow(industrySheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("行业");
        row.createCell(++i).setCellValue("企业数");
        if (dtos != null && dtos.size() > 0) {
            dtos.forEach((r) -> setIndustrySheetBookRow(industrySheet, r));
        }
    }

    private void setIndustrySheetBookRow(Sheet industrySheet, CustomerIndustryStatisticsDTO dto) {
        Row row = industrySheet.createRow(industrySheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getItemName() == null ? "未知" : dto.getItemName());
        row.createCell(++i).setCellValue(dto.getCustomerCount() == null ? 0L : dto.getCustomerCount());
    }

    private void setNewCustomerStatisticSheetHeadAndBookRow(Sheet customerStatisticSheet, EnterpriseCustomerStatisticsDTO statisticDTO) {
        Row row = customerStatisticSheet.createRow(customerStatisticSheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("企业客户总数 单位：个");
        row.createCell(++i).setCellValue("项目总企业人数 单位：人");
        row.createCell(++i).setCellValue("截止到当前，项目总营业额  单位：万元");
        row.createCell(++i).setCellValue("截止到当前，项目总纳税额  单位：万元");
        setCustomerStatisticSheetBookRow(customerStatisticSheet, statisticDTO);
    }

    private void setCustomerStatisticSheetBookRow(Sheet customerStatisticSheet, EnterpriseCustomerStatisticsDTO dto) {
        Row row = customerStatisticSheet.createRow(customerStatisticSheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getCustomerCount() == null ? 0 : dto.getCustomerCount());
        row.createCell(++i).setCellValue(dto.getCustomerMemberCount() == null ? 0 : dto.getCustomerMemberCount());
        row.createCell(++i).setCellValue(dto.getTotalTurnover().toString());
        row.createCell(++i).setCellValue(dto.getTotalTaxAmount().toString());
    }

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            if (!file.isFile()) {
                LOGGER.info("filename:{} is not a file", path);
            }
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            // 读取完成删除文件
            if (file.isFile() && file.exists()) {
                boolean deleteResult = file.delete();
                LOGGER.info("delete  excel file result :{}",deleteResult);
            }

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE, QualityServiceErrorCode.ERROR_DOWNLOAD_EXCEL, ex.getLocalizedMessage());
        }
        return response;
    }

    @Override
    public void deletePotentialCustomer(DeleteEnterpriseCommand cmd) {
        enterpriseCustomerProvider.deletePotentialCustomer(cmd.getSourceId());
    }

    @Override
    public CustomerPotentialResponse listPotentialCustomers(DeleteEnterpriseCommand cmd) {

        List<PotentialCustomerDTO> dtos = new ArrayList<>();
        CustomerPotentialResponse response = new CustomerPotentialResponse();
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
        List<CustomerPotentialData> data = enterpriseCustomerProvider.listPotentialCustomers(cmd.getNamespaceId(), cmd.getSourceId(), cmd.getSourceType(), cmd.getPotentialName(), cmd.getPageAnchor(), cmd.getPageSize() + 1);
        if (data != null && data.size() > 0) {
            data.forEach((r) -> {
                PotentialCustomerDTO potentialCustomerDTO = ConvertHelper.convert(r, PotentialCustomerDTO.class);
                CustomerTalent talent = enterpriseCustomerProvider.findPotentialTalentBySourceId(r.getId());
                if (talent != null) {
                    potentialCustomerDTO.setContactPhone(talent.getPhone());
                    potentialCustomerDTO.setContactName(talent.getName());
                }

                if (r.getSourceId() != null && StringUtils.isNotBlank(r.getSourceType())) {
                    if (PotentialCustomerType.SERVICE_ALLIANCE.getValue().equals(r.getSourceType())) {
                        ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(r.getSourceId());
                        if (serviceAllianceCategories != null) {
                            potentialCustomerDTO.setOriginSourceName(serviceAllianceCategories.getName());
                        }
                    } else if (PotentialCustomerType.ACTIVITY.getValue().equals(r.getSourceType())) {
                        ActivityCategories activityCategories = activityProivider.findActivityCategoriesByEntryId(r.getSourceId(),cmd.getNamespaceId());
                        if (activityCategories != null) {
                            potentialCustomerDTO.setOriginSourceName(activityCategories.getName());
                        }
                    }
                }
                dtos.add(potentialCustomerDTO);
            });
        }
        if (dtos.size() > cmd.getPageSize()) {
            dtos.remove(dtos.size() - 1);
            response.setNextPageAnchor(dtos.get(dtos.size() - 1).getId());
        }
        response.setDtos(dtos);
        return response;
    }

    @Override
    public void setSyncPotentialCustomer(CustomerConfigurationCommand cmd) {
        CustomerConfiguration customerConfiguration = ConvertHelper.convert(cmd, CustomerConfiguration.class);
        enterpriseCustomerProvider.deleteCustomerConfiguration(cmd.getNamespaceId(),cmd.getScopeType());
        enterpriseCustomerProvider.createCustomerConfiguration(customerConfiguration);
    }

    @Override
    public List<CustomerConfigurationDTO> listSyncPotentialCustomer(CustomerConfigurationCommand cmd) {
        List<CustomerConfiguration> configurations = enterpriseCustomerProvider.listSyncPotentialCustomer(cmd.getNamespaceId());
        List<CustomerConfigurationDTO> dtos = new ArrayList<>();
        if (configurations != null && configurations.size() > 0) {
            configurations.forEach((c) -> dtos.add(ConvertHelper.convert(c, CustomerConfigurationDTO.class)));
        }
        return dtos;
    }

    @Override
    public List<CustomerExpandItemDTO> listExpandItems(CustomerConfigurationCommand cmd) {

        List<CustomerExpandItemDTO> items = new ArrayList<>();
        List<ActivityCategories> activityCategories = activityProivider.listActivityCategory(UserContext.getCurrentNamespaceId(), null);
        if (activityCategories != null && activityCategories.size() > 0) {
            activityCategories.forEach((a) -> {
                CustomerExpandItemDTO activityItem = new CustomerExpandItemDTO();
                activityItem.setSourceType(PotentialCustomerType.ACTIVITY.getValue());
                activityItem.setDisplayName(a.getName());
                activityItem.setSourceId(a.getEntryId());
                items.add(activityItem);
            });
        }
        //add service alliance categories
        ListServiceAllianceCategoriesCommand command = new ListServiceAllianceCategoriesCommand();
        command.setNamespaceId(UserContext.getCurrentNamespaceId());

        List<ServiceAllianceCategoryDTO> serviceAllianceCategories =  yellowPageService.listServiceAllianceCategories(command);
        if (serviceAllianceCategories != null && serviceAllianceCategories.size() > 0) {
            serviceAllianceCategories.forEach((r) -> {
                CustomerExpandItemDTO allianceItem = new CustomerExpandItemDTO();
                allianceItem.setSourceId((long) r.getId());
                allianceItem.setDisplayName(r.getName());
                allianceItem.setSourceType(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                items.add(allianceItem);
            });
        }
        return items;
    }

    @Override
    public List<CustomerTalentDTO> listPotentialTalent(DeleteEnterpriseCommand cmd) {
        List<CustomerTalent> dtos = enterpriseCustomerProvider.listPotentialTalentBySourceId(cmd.getSourceId());
        List<CustomerTalentDTO> result = new ArrayList<>();
        if(dtos!=null && dtos.size()>0){
            dtos.forEach((r)->{
                CustomerTalentDTO dto = ConvertHelper.convert(r, CustomerTalentDTO.class);
                dto.setRegisterStatus(r.getRegisterStatus().toString());
                dto.setCreateTime(dateTimeFormatter.format(r.getCreateTime().toLocalDateTime()));
                if (r.getTalentSourceItemId() != null && StringUtils.isNotBlank(r.getOriginSourceType())) {
                    if (PotentialCustomerType.SERVICE_ALLIANCE.getValue().equals(r.getOriginSourceType())) {
                        ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(r.getTalentSourceItemId());
                        if (serviceAllianceCategories != null) {
                            dto.setTalentSourceItemName(serviceAllianceCategories.getName());
                        }
                    } else if (PotentialCustomerType.ACTIVITY.getValue().equals(r.getOriginSourceType())) {
                        ActivityCategories entryCategory = activityProivider.findActivityCategoriesByEntryId(r.getTalentSourceItemId(), cmd.getNamespaceId());
                        if (entryCategory != null) {
                            dto.setTalentSourceItemName(entryCategory.getName());
                        }
                    }
                }
                result.add(dto);
            });
        }
        return result;
    }

    @Override
    public void updatePotentialCustomer(DeleteEnterpriseCommand cmd) {

        CustomerPotentialData data = enterpriseCustomerProvider.findPotentialCustomerByName(cmd.getName());
        if (data != null && !Objects.equals(data.getId(), cmd.getSourceId())) {
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST,
                    "potential customer name  is already exist");
        }
        enterpriseCustomerProvider.updatePotentialCustomer(cmd.getSourceId(), cmd.getName(), UserContext.currentUserId(), UserContext.getCurrentNamespaceId());
    }

    @Override
    public EnterpriseCustomerDTO getCustomerBasicInfoByOrgId(GetEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(cmd.getOrgId());
        if (customer == null) {
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST, "customer is not exist");
        }
//        return ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
        EnterpriseCustomerDTO dto = new EnterpriseCustomerDTO();
        dto.setCommunityId(customer.getCommunityId());
        dto.setId(customer.getId());
        dto.setOrganizationId(customer.getOrganizationId());
        return dto;
    }

    @Override
    public SearchEnterpriseCustomerResponse listSyncErrorCustomer(SearchEnterpriseCustomerCommand cmd){
        List<EnterpriseCustomerDTO> customers = new ArrayList<>();
        List<SyncDataError> syncDataErrors = syncDataTaskProvider.listSyncErrorMsgByTaskId(cmd.getTaskId(), "customer", null, 999999);

        for(SyncDataError syncDataError : syncDataErrors){
            EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(syncDataError.getOwnerId());
            EnterpriseCustomerDTO customerDTO = ConvertHelper.convert(enterpriseCustomer,EnterpriseCustomerDTO.class);
            customerDTO.setSyncErrorMsg(syncDataError.getErrorMessage());
            customers.add(customerDTO);
        }
        SearchEnterpriseCustomerResponse response = new SearchEnterpriseCustomerResponse();
        response.setDtos(customers);
        return response;
    }

    @Override
    public void exportContractListByContractList(ExportEnterpriseCustomerCommand cmd) {
        //  export with the file download center
        Map<String, Object> params = new HashMap<>();

        //  the value could be null if it is not exist
        params.put("namespaceId", cmd.getNamespaceId());
        params.put("communityId", cmd.getCommunityId());
        params.put("moduleName", cmd.getModuleName());
        params.put("trackingUids", cmd.getTrackingUids());
        params.put("trackingUid", cmd.getTrackingUid());
        params.put("orgId", cmd.getOrgId());
        params.put("abnormalFlag", cmd.getAbnormalFlag());
        params.put("addressId", cmd.getAddressId());
        params.put("adminFlag", cmd.getAdminFlag());
        params.put("buildingId", cmd.getBuildingId());
        params.put("type", cmd.getType());
        params.put("trackingName", cmd.getTrackingName());
        params.put("aptitudeFlagItemId" ,cmd.getAptitudeFlagItemId());
        params.put("corpIndustryItemId", cmd.getCorpIndustryItemId());
        params.put("customerCategoryId", cmd.getCustomerCategoryId());
        params.put("includedGroupIds", cmd.getIncludedGroupIds());
        params.put("infoFLag", cmd.getInfoFLag());
        params.put("keyword", cmd.getKeyword());
        params.put("lastTrackingTime", cmd.getLastTrackingTime());
        params.put("levelId", cmd.getLevelId());
        params.put("ownerId", cmd.getOwnerId());
        params.put("ownerType", cmd.getOwnerType());
        params.put("propertyArea", cmd.getPropertyArea());
        params.put("propertyUnitPrice", cmd.getPropertyUnitPrice());
        params.put("propertyType", cmd.getPropertyType());
        params.put("sortField", cmd.getSortField());
        params.put("sortType", cmd.getSortType());
        params.put("task_Id", cmd.getTaskId());
        params.put("customerSource", cmd.getCustomerSource());
        params.put("requirementMinArea", cmd.getRequirementMinArea());
        params.put("requirementMaxArea", cmd.getRequirementMaxArea());
        params.put("entryStatusItemId", cmd.getEntryStatusItemId());
        params.put("trackerUids", cmd.getTrackerUids());
        params.put("customerName", cmd.getCustomerName());


        String fileName;
        if(cmd.getModuleName().equals("enterprise_customer")){
            fileName = String.format(localeStringService.getLocalizedString("enterpriseCustomer.export","1",UserContext.current().getUser().getLocale(),"") + com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.DATE_TIME_NO_SLASH)) + ".xlsx";
        }else{
            fileName = String.format(localeStringService.getLocalizedString("enterpriseCustomer.export","2",UserContext.current().getUser().getLocale(),"") + com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.DATE_TIME_NO_SLASH)) + ".xlsx";
        }

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CustomerExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }


    @Override
    public OutputStream exportEnterpriseCustomerWihtOutPrivilege(ExportEnterpriseCustomerCommand cmd) {
        ExportFieldsExcelCommand command = ConvertHelper.convert(cmd, ExportFieldsExcelCommand.class);
//        command.setIncludedGroupIds("10,11,12");
        List<FieldGroupDTO> results = fieldService.getAllGroups(command, false, true);
        if (results != null && results.size() > 0) {
            List<String> sheetNames = results.stream().map((r)->r.getGroupId().toString()).collect(Collectors.toList());
            String code = "";
            String baseInfo = "";

            if(cmd.getModuleName().equals("enterprise_customer")){
                code = DynamicExcelStrings.CUSTOEMR;
                baseInfo = DynamicExcelStrings.baseIntro;
            }else if(cmd.getModuleName().equals("investment_promotion")){
                code = DynamicExcelStrings.INVITED_CUSTOMER;
                baseInfo = DynamicExcelStrings.invitedBaseIntro;

            }
            return dynamicExcelService.exportDynamicExcel( code, baseInfo, sheetNames, cmd, true, true, null);
        }
        return null;
    }

    @Override
    public void changeCustomerAptitude(SearchEnterpriseCustomerCommand cmd){
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_CHANGE_APTITUDE, cmd.getOrgId(), cmd.getCommunityId());
        Boolean isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        SearchEnterpriseCustomerResponse res = null;
        if(cmd.getCustomerIds()!= null && cmd.getCustomerIds().size() > 0){
             res = enterpriseCustomerSearcher.queryEnterpriseCustomersById(cmd);
        }else{
             res = enterpriseCustomerSearcher.queryEnterpriseCustomers(cmd, isAdmin);

        }
        for(EnterpriseCustomerDTO dto : res.getDtos()){
            enterpriseCustomerProvider.updateCustomerAptitudeFlag(dto.getId(), 1l);
        }
    }

    @Override
    public ListSignupInfoByOrganizationIdResponse listCustomerApartmentActivity(ListCustomerApartmentActivityCommand cmd){
        return activityService.listSignupInfoByOrganizationId(cmd.getOrgId(), cmd.getNamespaceId(), cmd.getPageAnchor(), cmd.getPageSize());
    }

    @Override
    public void updateSuperAdmin(createSuperAdminCommand cmd){
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.ENTERPRISE_CUSTOMER_MANAGE_LIST, cmd.getOrgId(), cmd.getCommunityId());
        UpdateSuperAdminCommand cmd2 = ConvertHelper.convert(cmd, UpdateSuperAdminCommand.class);
        organizationService.updateSuperAdmin(cmd2);

        //adminflag表示该用户有管理员,设置管理员后更新该用户有管理员
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmd.getCustomerId());
        if(customer != null){
            customer.setAdminFlag((byte)1);
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
        }else{
            LOGGER.error("params targetId is null");
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "params targetId is null.");
        }
    }

    @Override
    public void transNewAdmin(TransNewAdminCommand cmd2){
        Long nextPageAnchor = 0l;
        boolean breakFlag = true;
        int totalCount = 0;
        long roundStartTime = System.currentTimeMillis();
        while(breakFlag){

            List<CreateOrganizationAdminCommand> list = new ArrayList<>();

            if(cmd2.getNamespaceId() != null || cmd2.getNamespaceId() != 0){
                list = enterpriseCustomerProvider.getOrganizationAdmin(nextPageAnchor, cmd2.getNamespaceId());

            }else{
                list = enterpriseCustomerProvider.getOrganizationAdmin(nextPageAnchor);
            }
            nextPageAnchor = list.get(list.size()-1).getOwnerId();
            if(list.size() < 101){
                breakFlag = false;
            }else{
                list.remove(list.size()-1);
            }
            long roundQueryEndTime = System.currentTimeMillis();




            my:for(CreateOrganizationAdminCommand cmd : list){
                cmd.setOwnerId(null);

                /*ListServiceModuleAdministratorsCommand cmd3 = new ListServiceModuleAdministratorsCommand();
                cmd3.setNamespaceId(cmd.getNamespaceId());
                cmd3.setOrganizationId(cmd.getOrganizationId());
                cmd3.setOwnerId(cmd.getOrganizationId());
                cmd3.setOwnerType("EhOrganizations");
                cmd3.setActivationFlag((byte)1);
                cmd3.setPageSize(999999);
                ListOrganizationContectDTOResponse response = rolePrivilegeService.listOrganizationSystemAdministrators(cmd3);
                if(response != null){
                    List<OrganizationContactDTO> orgList = response.getDtos();
                    LOGGER.debug("query exist admin by namespaceId = {}, communityId = {}, organization = {}, result = {}", cmd3.getNamespaceId(), cmd3.getCommunityId(), cmd3.getOrganizationId(), orgList);
                    for(OrganizationContactDTO org : orgList){
                        if(cmd.getContactToken().equals(org.getContactToken())){
                            LOGGER.debug("the admin is exist, name = {}, token = {}", org.getContactName(), org.getContactToken());
                            continue my;
                        }
                    }
                }*/





                dbProvider.execute((TransactionStatus status) -> {
                    //根据组织id、用户姓名、手机号、超级管理员权限代号、机构管理，超级管理员，拥有 所有权限、来创建超级管理员
                    /*try {
                        rolePrivilegeService.createOrganizationAdmin(cmd.getOrganizationId(), cmd.getContactName(), cmd.getContactToken(),
                                PrivilegeConstants.ORGANIZATION_SUPER_ADMIN, RoleConstants.PM_SUPER_ADMIN, false, false);
                    }catch(Exception e){
                        LOGGER.warn(e.getMessage());
                    }*/
                    cmd.setOwnerType("EhOrganizations");
                    cmd.setOwnerId(cmd.getOrganizationId());
                    rolePrivilegeService.deleteOrganizationAdministratorsForOnes(ConvertHelper.convert(cmd, DeleteOrganizationAdminCommand.class));
                    rolePrivilegeService.createOrganizationAdmin(cmd.getOrganizationId(), cmd.getContactName(), cmd.getContactToken(),
                            PrivilegeConstants.ORGANIZATION_SUPER_ADMIN,  RoleConstants.PM_SUPER_ADMIN, true, false);
                    return null;
                });
            }

            totalCount += list.size();
            if(LOGGER.isInfoEnabled()) {
                long roundEndTime = System.currentTimeMillis();
                LOGGER.info("Replace organization admin, namespaceId={}, totalCount={}, pageAnchor={}, queryElapse={}, elapse={}",
                        cmd2.getNamespaceId(), totalCount, nextPageAnchor, (roundEndTime - roundQueryEndTime), (roundEndTime - roundStartTime));
                roundStartTime  = roundEndTime;
            }
        }
    }


}
