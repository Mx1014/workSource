package com.everhomes.investment;


import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.dynamicExcel.DynamicExcelService;
import com.everhomes.dynamicExcel.DynamicExcelStrings;
import com.everhomes.filedownload.TaskService;
import com.everhomes.http.HttpUtils;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.*;
import com.everhomes.portal.PortalService;
import com.everhomes.quality.QualityConstant;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.ListCommunitiesCommand;
import com.everhomes.rest.community.ListCommunitiesResponse;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class InvitedCustomerServiceImpl implements InvitedCustomerService , ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitedCustomerServiceImpl.class);
    private static final String CreateCustomer = "/CreateCustomer";
    private static final Integer SUCCESS_CODE = 0;
    private static final String CreateContract = "/CreateContract";

    @Autowired
    private CommunityService communityService;

    @Autowired
    private EnterpriseCustomerSearcher customerSearcher;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private InvitedCustomerProvider invitedCustomerProvider;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private DynamicExcelService dynamicExcelService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ScheduleProvider scheduleProvider;




    public void setup(){
        String triggerName = CustomerStatisticsScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
        String jobName = triggerName;
        String cronExpression = CustomerStatisticsScheduleJob.CRON_EXPRESSION;
        //启动定时任务
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, CustomerStatisticsScheduleJob.class, null);
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }





    private void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.INVITED_CUSTOMER, null, null, null, communityId);
    }

    @Override
    public InvitedCustomerDTO createInvitedCustomer(CreateInvitedCustomerCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_CREATE, cmd.getOrgId(), cmd.getCommunityId());

        InvitedCustomerDTO result;
        if(cmd.getCustomerSource() == null){
            if(cmd.getLevelItemId()== CustomerLevelType.REGISTERED_CUSTOMER.getCode()){
                cmd.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            }else{
                cmd.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
            }
        }

        CreateEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, CreateEnterpriseCustomerCommand.class);

        try{
            EnterpriseCustomerDTO customer = customerService.createEnterpriseCustomerOutAuth(cmd2);

            if(customer != null) {


                cmd.setId(customer.getId());


                if (cmd.getContacts() != null && cmd.getContacts().size() > 0) {
                    cmd.getContacts().forEach((c) -> {
                        CustomerContact contact = ConvertHelper.convert(c, CustomerContact.class);
                        contact.setCustomerId(cmd.getId());
                        contact.setCommunityId(cmd.getCommunityId());
                        contact.setNamespaceId(cmd.getNamespaceId());
                        contact.setStatus(CommonStatus.ACTIVE.getCode());
                        contact.setCustomerSource(cmd.getCustomerSource());
                        if(StringUtils.isNotBlank(contact.getName()) && StringUtils.isNotBlank(contact.getPhoneNumber())){
                            invitedCustomerProvider.createContact(contact);
                        }
                    });
                }
                // reflush requirement
                if (cmd.getRequirement() != null) {
                    CustomerRequirement requirement = ConvertHelper.convert(cmd.getRequirement(), CustomerRequirement.class);
                    requirement.setCommunityId(cmd.getCommunityId());
                    requirement.setNamespaceId(cmd.getNamespaceId());
                    requirement.setCustomerId(cmd.getId());
                    requirement.setStatus(CommonStatus.ACTIVE.getCode());
                    Long requirementId = invitedCustomerProvider.createRequirement(requirement);
                    if (cmd.getRequirement().getAddresses() != null && cmd.getRequirement().getAddresses().size() > 0) {
                        cmd.getRequirement().getAddresses().forEach((c) -> {
                            CustomerRequirementAddress address = ConvertHelper.convert(c, CustomerRequirementAddress.class);
                            address.setCustomerId(cmd.getId());
                            address.setCommunityId(cmd.getCommunityId());
                            address.setNamespaceId(cmd.getNamespaceId());
                            address.setStatus(CommonStatus.ACTIVE.getCode());
                            address.setRequirementId(requirementId);
                            invitedCustomerProvider.createRequirementAddress(address);
                        });
                    }

                }
                // reflush current basic info
                if (cmd.getCurrentRent() != null) {
                    CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getCurrentRent(), CustomerCurrentRent.class);
                    if (cmd.getCurrentRent().getContractIntentionDate() != null) {
                        currentRent.setContractIntentionDate(new Timestamp(cmd.getCurrentRent().getContractIntentionDate()));

                    }
                    currentRent.setCommunityId(cmd.getCommunityId());
                    currentRent.setNamespaceId(cmd.getNamespaceId());
                    currentRent.setStatus(CommonStatus.ACTIVE.getCode());
                    currentRent.setCustomerId(cmd.getId());
                    invitedCustomerProvider.createCurrentRent(currentRent);
                }
                if (cmd.getTrackers() != null) {
                    cmd.getTrackers().forEach((c) -> {
                        CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                        tracker.setCommunityId(cmd.getCommunityId());
                        tracker.setNamespaceId(cmd.getNamespaceId());
                        tracker.setStatus(CommonStatus.ACTIVE.getCode());
                        tracker.setCustomerId(cmd.getId());
                        if(tracker.getTrackerUid() != null && tracker.getTrackerUid() != 0){
                            invitedCustomerProvider.createTracker(tracker);
                        }
                    });
                }

                EnterpriseCustomer dto = ConvertHelper.convert(customer, EnterpriseCustomer.class);

                dto.setStatus(CommonStatus.ACTIVE.getCode());
                dto.setNamespaceId(cmd.getNamespaceId());
                customerSearcher.feedDoc(dto);

                return ConvertHelper.convert(cmd, InvitedCustomerDTO.class);
            }
        }catch(RuntimeErrorException e){
            LOGGER.error(e.getMessage());
            throw e;
        }

        return null;
    }

    @Override
    public void updateInvestment(CreateInvitedCustomerCommand cmd) {
        if(cmd.getAdmin() == null || !cmd.getAdmin()) {
            checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
        }


        if(cmd.getLevelItemId() != null){
            if(cmd.getLevelItemId()== CustomerLevelType.REGISTERED_CUSTOMER.getCode()){
                cmd.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            }else{
                cmd.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
            }
        }



        UpdateEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, UpdateEnterpriseCustomerCommand.class);
        EnterpriseCustomerDTO customerDTO = null;
        try {
            cmd2.setCheckAuthFlag(null);
            cmd2.setModuleName("investment_promotion");
            if (cmd.getTransCommunityId() != null && cmd.getTransCommunityId() != 0) {
                List<EnterpriseCustomer> customers = customerProvider.listEnterpriseCustomerByNamespaceIdAndName(cmd2.getNamespaceId(), cmd.getTransCommunityId(), cmd2.getName());
                if(customers.size() > 0){
                    LOGGER.error("the community you want to change has already existed ");
                    throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST,
                            "the community you want to change has already existed");
                }
                cmd2.setCommunityId(cmd.getTransCommunityId());
                cmd2.setTransCommunityId(cmd.getTransCommunityId());
            }
            customerDTO = customerService.updateEnterpriseCustomer(cmd2);
        }catch (RuntimeErrorException e){
            LOGGER.error(e.getMessage());
            throw e;
        }


        // reflush contacts
        invitedCustomerProvider.deleteCustomerContacts(cmd.getId());
        if (cmd.getContacts() != null && cmd.getContacts().size() > 0) {
            cmd.getContacts().forEach((c) -> {
                CustomerContact contact = ConvertHelper.convert(c, CustomerContact.class);
                contact.setCustomerId(cmd.getId());
                contact.setCommunityId(cmd.getCommunityId());
                contact.setNamespaceId(cmd.getNamespaceId());
                contact.setStatus(CommonStatus.ACTIVE.getCode());
                contact.setCustomerSource(cmd.getCustomerSource());
                if(StringUtils.isNotBlank(contact.getName()) && StringUtils.isNotBlank(contact.getPhoneNumber())){
                    invitedCustomerProvider.createContact(contact);
                }
            });
        }
        // reflush requirement
        if (cmd.getRequirement() != null) {
            CustomerRequirement requirement = ConvertHelper.convert(cmd.getRequirement(), CustomerRequirement.class);
            requirement.setCommunityId(cmd.getCommunityId());
            requirement.setNamespaceId(cmd.getNamespaceId());
            requirement.setCustomerId(cmd.getId());
            requirement.setStatus(CommonStatus.ACTIVE.getCode());
            Long requirementId = invitedCustomerProvider.createRequirement(requirement);
            if(cmd.getRequirement().getAddresses() != null && cmd.getRequirement().getAddresses().size() > 0){
                cmd.getRequirement().getAddresses().forEach((c) -> {
                    CustomerRequirementAddress address = ConvertHelper.convert(c, CustomerRequirementAddress.class);
                    address.setCustomerId(cmd.getId());
                    address.setCommunityId(cmd.getCommunityId());
                    address.setNamespaceId(cmd.getNamespaceId());
                    address.setStatus(CommonStatus.ACTIVE.getCode());
                    address.setRequirementId(requirementId);
                    invitedCustomerProvider.createRequirementAddress(address);
                });
            }
        }
        // reflush current basic info
        if(cmd.getCurrentRent()!=null){
            CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getCurrentRent(), CustomerCurrentRent.class);
            if(cmd.getCurrentRent().getContractIntentionDate() != null){
                currentRent.setContractIntentionDate(new Timestamp(cmd.getCurrentRent().getContractIntentionDate()));

            }
            currentRent.setCommunityId(cmd.getCommunityId());
            currentRent.setNamespaceId(cmd.getNamespaceId());
            currentRent.setStatus(CommonStatus.ACTIVE.getCode());
            currentRent.setCustomerId(cmd.getId());
            invitedCustomerProvider.createCurrentRent(currentRent);
        }
        // update tracking user infos
        invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId());
        if(cmd.getTrackers() != null){
            cmd.getTrackers().forEach((c) -> {
                CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                tracker.setCommunityId(cmd.getCommunityId());
                tracker.setNamespaceId(cmd.getNamespaceId());
                tracker.setStatus(CommonStatus.ACTIVE.getCode());
                tracker.setCustomerId(cmd.getId());
                if(tracker.getTrackerUid() != null && tracker.getTrackerUid() != 0){
                    invitedCustomerProvider.createTracker(tracker);
                }
            });
        }
        EnterpriseCustomer customer = ConvertHelper.convert(customerDTO, EnterpriseCustomer.class);

        // todo: update main record data
        customer.setNamespaceId(cmd.getNamespaceId());
        customer.setStatus(CommonStatus.ACTIVE.getCode());
        customer.setLevelItemId(cmd.getLevelItemId());
        customer.setCustomerSource(cmd.getCustomerSource());
        customer.setName(cmd.getName());
        customer.setSourceId(cmd.getSourceId());
        customer.setCorpIndustryItemId(cmd.getCorpIndustryItemId());
        customer.setSourceItemId(cmd.getSourceItemId());
        if(cmd.getExpectedSignDate() != null){
            customer.setExpectedSignDate(new Timestamp(cmd.getExpectedSignDate()));
        }
        customer.setTransactionRatio(cmd.getTransactionRatio());
        //sync tenant info into organization
        if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0) {
            ExecutorUtil.submit(() -> syncInvestmentInfoToOrganization(cmd, customer.getOrganizationId()));
        }

        customerSearcher.feedDoc(customer);
    }

    private EnterpriseCustomer checkEnterpriseCustomer(Long id) {
        EnterpriseCustomer customer = customerProvider.findById(id);
        if (customer == null || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(customer.getStatus()))) {
            LOGGER.error("enterprise customer is not exist or active. id: {}, customer: {}", id, customer);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                    "customer is not exist or active");
        }
        return customer;
    }

    private void syncInvestmentInfoToOrganization(CreateInvitedCustomerCommand cmd, Long organizationId) {
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        organization.setName(cmd.getName());
        // todo : add some filed for sync
        organizationProvider.updateOrganization(organization);
        organizationSearcher.feedDoc(organization);
    }

    @Override
    public void deleteInvestment(DeleteInvitedCustomerCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_DELETE, cmd.getOrgId(), cmd.getCommunityId());

        customerSearcher.deleteById(cmd.getId());
        DeleteEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, DeleteEnterpriseCustomerCommand.class);
        customerService.deleteEnterpriseCustomer(cmd2, false);
        invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId());
        invitedCustomerProvider.deleteCustomerContacts(cmd.getId());

    }


    private Boolean checkCustomerAdmin(Long ownerId, String ownerType, Integer namespaceId) {
        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(namespaceId);
        listServiceModuleAppsCommand.setModuleId(QualityConstant.INVITED_CUSTOMER);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
        checkModuleManageCommand.setModuleId(QualityConstant.INVITED_CUSTOMER);
        checkModuleManageCommand.setOrganizationId(ownerId);
        checkModuleManageCommand.setOwnerType(ownerType);
        checkModuleManageCommand.setUserId(UserContext.currentUserId());
        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
            checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
        }
        return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
    }


    @Override
    public SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_VIEW, cmd.getOrgId(), cmd.getCommunityId());
        Boolean isAdmin = false;
        SearchEnterpriseCustomerResponse searchResponse;
        if(cmd.getCustomerIds() != null && cmd.getCustomerIds().size() > 0){
            searchResponse = customerSearcher.queryEnterpriseCustomersById(cmd);
            SearchInvestmentResponse response = new SearchInvestmentResponse();
            response.setDtos(searchResponse.getDtos());
            response.setNextPageAnchor(searchResponse.getNextPageAnchor());
            return response;
        }else {
            isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
            searchResponse = customerSearcher.queryEnterpriseCustomers(cmd, isAdmin);
        }
        SearchInvestmentResponse response = new SearchInvestmentResponse();
        response.setDtos(searchResponse.getDtos());
        response.setNextPageAnchor(searchResponse.getNextPageAnchor());
        // only the first time we requested, populate stastics data
        if ( cmd.getPageAnchor() == null || cmd.getPageAnchor() == 0) {
            List<InvitedCustomerStatisticsDTO> statistics = null;
            // this module should add investment statistics to response
            ListFieldItemCommand fieldItemCommand = new ListFieldItemCommand();
            fieldItemCommand.setNamespaceId(cmd.getNamespaceId());
            fieldItemCommand.setCommunityId(cmd.getCommunityId());
            // this field id menus investment enterprise levelItemId private key
            fieldItemCommand.setFieldId(5L);
            fieldItemCommand.setModuleName("investment_promotion");
            List<FieldItemDTO> items = fieldService.listFieldItems(fieldItemCommand);
            Map<Long, FieldItemDTO> itemsMap = transferCurrentCommunityItemsMap(items);
            if (itemsMap != null && itemsMap.size() > 0) {

                statistics = invitedCustomerProvider.getInvitedCustomerStatistics(isAdmin, cmd.getKeyword(), cmd.getRequirementMinArea(), cmd.getRequirementMaxArea(), itemsMap.keySet(), itemsMap, (locator, query) -> {
                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(cmd.getCommunityId()));
                    if (cmd.getCorpIndustryItemId() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CORP_INDUSTRY_ITEM_ID.eq(cmd.getCorpIndustryItemId()));
                    }
                    if (cmd.getSourceItemId() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.SOURCE_ITEM_ID.eq(cmd.getSourceItemId()));
                    }
                    if (cmd.getCustomerSource() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CUSTOMER_SOURCE.eq(cmd.getCustomerSource()));
                    }
                    if (cmd.getMinTrackingPeriod() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.LAST_TRACKING_TIME.ge(new Timestamp(cmd.getMinTrackingPeriod())));
                    }
                    if (cmd.getMaxTrackingPeriod() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.LAST_TRACKING_TIME.le(new Timestamp(cmd.getMaxTrackingPeriod())));
                    }

                    return query;
                });
            }
            // add transfer Rate  infos
            addExtendInfo(statistics);
            response.setStatistics(statistics);
        }
        return response;
    }

    private void addExtendInfo(List<InvitedCustomerStatisticsDTO> statistics) {
        InvitedCustomerStatisticsDTO dto = new InvitedCustomerStatisticsDTO();
        dto.setKey("rate");
        dto.setValue("0");
        if (statistics != null && statistics.size() > 0) {
            Long dealCustomerCount = 0L;
            Long totalCustomerCount = 0L;
            for (InvitedCustomerStatisticsDTO tmp : statistics) {
                if (tmp.getItemId()!=null &&  6 == tmp.getItemId()) {
                    dealCustomerCount = Long.parseLong(tmp.getValue());
                }
                if ("totalCount".equals(tmp.getKey())) {
                    totalCustomerCount = Long.parseLong(tmp.getValue());
                }
            }
            if (totalCustomerCount != 0L) {
                dto.setValue(new BigDecimal(dealCustomerCount).divide(new BigDecimal(totalCustomerCount), 2, RoundingMode.HALF_UP).toString());
            }
            statistics.add(dto);
        }
    }

    private Map<Long, FieldItemDTO> transferCurrentCommunityItemsMap(List<FieldItemDTO> items) {
        Map<Long, FieldItemDTO> result = null;
        if (items != null && items.size() > 0) {
            result = new HashMap<>();
            for (FieldItemDTO item : items) {
                result.put(item.getItemId(), item);
            }
        }
        return result;
    }

    public InvitedCustomerDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd){
        if(cmd.getIsAdmin() == null || !cmd.getIsAdmin()) {
            checkCustomerAuth(UserContext.getCurrentNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_VIEW, cmd.getOrgId(), cmd.getCommunityId());
        }

        GetEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, GetEnterpriseCustomerCommand.class);
        EnterpriseCustomerDTO customerDTO = customerService.getEnterpriseCustomer(cmd2);
        if(customerDTO != null) {
            InvitedCustomerDTO invitedCustomerDTO = ConvertHelper.convert(customerDTO, InvitedCustomerDTO.class);

            invitedCustomerDTO.setExpectedSignDate(customerDTO.getExpectedSignDate());

            List<CustomerContact> contacts = invitedCustomerProvider.findContactByCustomerId(invitedCustomerDTO.getId());
            if(contacts != null && contacts.size() != 0){
                List<CustomerContactDTO> dtos = contacts.stream().map(r -> ConvertHelper.convert(r, CustomerContactDTO.class)).collect(Collectors.toList());
                invitedCustomerDTO.setContacts(dtos);
            }
            List<CustomerTracker> trackers = invitedCustomerProvider.findTrackerByCustomerId(invitedCustomerDTO.getId());
            if(trackers != null && trackers.size() != 0){
                List<CustomerTrackerDTO> dtos = trackers.stream().map(r -> ConvertHelper.convert(r, CustomerTrackerDTO.class)).collect(Collectors.toList());
                dtos.forEach((r) -> {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(r.getTrackerUid());
                    if (members != null && members.size()>0) {
                        r.setTrackerPhone(members.get(0).getContactToken());
                        r.setTrackerName(members.get(0).getContactName());
                    }
                });

                invitedCustomerDTO.setTrackers(dtos);

            }

            CustomerRequirement requirement = invitedCustomerProvider.findNewestRequirementByCustoemrId(invitedCustomerDTO.getId());
            if(requirement != null){
                CustomerRequirementDTO requirementDTO = ConvertHelper.convert(requirement, CustomerRequirementDTO.class);
                List<CustomerRequirementAddress> addresses = invitedCustomerProvider.findRequirementAddressByRequirementId(requirement.getId());
                if(addresses != null){
                    List<CustomerRequirementAddressDTO> dtos = addresses.stream().map(r -> ConvertHelper.convert(r, CustomerRequirementAddressDTO.class)).collect(Collectors.toList());
                    if(dtos != null && dtos.size() > 0){
                        dtos.forEach(r ->{
                            Address address = addressProvider.findAddressById(r.getAddressId());
                            if(address != null){
                                if(address.getStatus().equals(AddressAdminStatus.INACTIVE.getCode())){
                                    r.setAddressName(address.getCommunityName() + "/" + address.getBuildingName() + "/" + address.getApartmentName() + "(房源已删除)");
                                    r.setCommunityId(address.getCommunityId());
                                    r.setCommunityName(address.getCommunityName());
                                    r.setAddressArea(address.getBuildArea());
                                }else{
                                    r.setAddressName(address.getCommunityName() + "/" + address.getBuildingName() + "/" + address.getApartmentName());
                                    r.setAddressArea(address.getBuildArea());
                                    r.setCommunityId(address.getCommunityId());
                                    r.setCommunityName(address.getCommunityName());
                                }
                            }else{
                                dtos.remove(r);
                            }
                        });
                        if(dtos.size() > 0) {
                            requirementDTO.setAddresses(dtos);
                        }
                    }
                }


                invitedCustomerDTO.setRequirement(requirementDTO);

            }

            CustomerCurrentRent currentRent = invitedCustomerProvider.findNewestCurrentRentByCustomerId(invitedCustomerDTO.getId());
            if(currentRent != null){
                CustomerCurrentRentDTO currentRentDTO = ConvertHelper.convert(currentRent, CustomerCurrentRentDTO.class);
                if(currentRent.getContractIntentionDate() != null)
                    currentRentDTO.setContractIntentionDate(currentRent.getContractIntentionDate().getTime());
                invitedCustomerDTO.setCurrentRent(currentRentDTO);
            }


            return invitedCustomerDTO;
        }

        return null;
    }

    @Override
    public CustomerRequirementDTO getCustomerRequirementDTOByCustomerId(Long customerId){
        CustomerRequirement requirement = invitedCustomerProvider.findNewestRequirementByCustoemrId(customerId);
        if(requirement != null){
            List<CustomerRequirementAddress> addresses = invitedCustomerProvider.findRequirementAddressByRequirementId(requirement.getId());
            CustomerRequirementDTO requirementDTO = ConvertHelper.convert(requirement, CustomerRequirementDTO.class);
            if(addresses != null && addresses.size() > 0){
                List<CustomerRequirementAddressDTO> addressesDTO = new ArrayList<>();
                addresses.forEach(a -> {
                    CustomerRequirementAddressDTO addressDTO = ConvertHelper.convert(a, CustomerRequirementAddressDTO.class);
                    Address address = addressProvider.findAddressById(addressDTO.getAddressId());
                    if(address != null){
                        if(address.getStatus().equals(AddressAdminStatus.INACTIVE.getCode())){
                            addressDTO.setAddressName(address.getCommunityName() + "/" + address.getBuildingName() + "/" + address.getApartmentName() + "(房源已删除)");

                        }else{
                            addressDTO.setAddressName(address.getCommunityName() + "/" + address.getBuildingName() + "/" + address.getApartmentName());
                            addressDTO.setAddressArea(address.getRentArea());
                        }
                    }
                    addressesDTO.add(addressDTO);

                });
                requirementDTO.setAddresses(addressesDTO);
            }
            return requirementDTO;
        }
        return null;
    }


    @Override
    public List<Long> changeInvestmentToCustomer(ChangeInvestmentToCustomerCommand cmd){

        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_CHANGE_ENTERPRISE_CUSTOMER, cmd.getOrgId(), cmd.getCommunityId());


        List<Long> customerIds = cmd.getCustomerIds();
        List<EnterpriseCustomer> customers = new ArrayList<>();

        customerIds.forEach(r ->{
            ViewInvestmentDetailCommand cmd2 = new ViewInvestmentDetailCommand();
            cmd2.setCommunityId(cmd.getCommunityId());
            cmd2.setOrgId(cmd.getOrgId());
            cmd2.setId(r);
            cmd2.setIsAdmin(true);
            InvitedCustomerDTO customerDTO = viewInvestmentDetail(cmd2);

            //EnterpriseCustomer customer =
            if(customerDTO.getContacts() != null && customerDTO.getContacts().size() > 0) {

                customerDTO.setContactName(customerDTO.getContacts().get(0).getName());
                customerDTO.setContactPhone(customerDTO.getContacts().get(0).getPhoneNumber().toString());
            }
            customerDTO.setLevelItemId((long)CustomerLevelType.REGISTERED_CUSTOMER.getCode());
            customerDTO.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            CreateInvitedCustomerCommand cmd3 = ConvertHelper.convert(customerDTO, CreateInvitedCustomerCommand.class);
            cmd3.setAdmin(true);
            cmd3.setNamespaceId(cmd.getNamespaceId());
            updateInvestment(cmd3);
        });
        return cmd.getCustomerIds();
    }


    @Override
    public void giveUpInvitedCustomer(ViewInvestmentDetailCommand cmd) {
        InvitedCustomerDTO invitedCustomerDTO = viewInvestmentDetail(cmd);


        if(invitedCustomerDTO != null) {
            List<CustomerTrackerDTO> trackers = invitedCustomerDTO.getTrackers();

            if(trackers != null && trackers.size() > 0){
                for(CustomerTrackerDTO tracker : trackers){
                    if(tracker.getTrackerUid().equals(UserContext.current().getUser().getId())) {
                        trackers.remove(tracker);
                        break;
                    }
                }
            }

            invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId());
            if(trackers != null && trackers.size() > 0){
                trackers.forEach((c) -> {
                    CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                    tracker.setCommunityId(invitedCustomerDTO.getCommunityId());
                    tracker.setNamespaceId(invitedCustomerDTO.getNamespaceId());
                    tracker.setStatus(CommonStatus.ACTIVE.getCode());
                    tracker.setCustomerId(invitedCustomerDTO.getId());
                    tracker.setCustomerSource(invitedCustomerDTO.getCustomerSource());
                    if(tracker.getTrackerUid() != null && tracker.getTrackerUid() != 0){
                        invitedCustomerProvider.createTracker(tracker);
                    }
                });
            }
            invitedCustomerDTO.setTrackers(trackers);
            EnterpriseCustomer customer = ConvertHelper.convert(invitedCustomerDTO, EnterpriseCustomer.class);
            customerSearcher.feedDoc(customer);

        }

    }


    @Override
    public void syncTrackerData() {

    }

    @Override
    public void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response){
        List<String> sheetNames = new ArrayList<>();
        sheetNames.add("客户信息");
        String excelTemplateName = "招商客户模板" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(Calendar.getInstance().getTime()) + ".xls";
        Boolean isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        cmd.setIsAdmin(isAdmin);
        dynamicExcelService.exportDynamicExcel(response, DynamicExcelStrings.INVITED_CUSTOMER, DynamicExcelStrings.invitedBaseIntro, sheetNames, cmd, true, false, excelTemplateName);
    }


    @Override
    public DynamicImportResponse importEnterpriseCustomer(ImportFieldExcelCommand cmd, MultipartFile mfile){
        checkCustomerAuth(UserContext.getCurrentNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_IMPORT, cmd.getOrgId(), cmd.getCommunityId());
        return fieldService.importDynamicExcel(cmd,mfile);
    }

    @Override
    public void exportContractListByContractList(ExportEnterpriseCustomerCommand cmd){
        checkCustomerAuth(UserContext.getCurrentNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_EXPORT, cmd.getOrgId(), cmd.getCommunityId());
        customerService.exportContractListByContractList(cmd);
    }


    @Override
    public InvitedCustomerDTO createInvitedCustomerWithoutAuth(CreateInvitedCustomerCommand cmd) {
        InvitedCustomerDTO result;
        if(cmd.getCustomerSource() == null){
            if(cmd.getLevelItemId()== CustomerLevelType.REGISTERED_CUSTOMER.getCode()){
                cmd.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            }else{
                cmd.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
            }
        }

        CreateEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, CreateEnterpriseCustomerCommand.class);

        try{
            EnterpriseCustomerDTO customer = customerService.createEnterpriseCustomerOutAuth(cmd2);

            if(customer != null) {


                cmd.setId(customer.getId());


                if (cmd.getContacts() != null && cmd.getContacts().size() > 0) {
                    cmd.getContacts().forEach((c) -> {
                        CustomerContact contact = ConvertHelper.convert(c, CustomerContact.class);
                        contact.setCustomerId(cmd.getId());
                        contact.setCommunityId(cmd.getCommunityId());
                        contact.setNamespaceId(cmd.getNamespaceId());
                        contact.setStatus(CommonStatus.ACTIVE.getCode());
                        contact.setCustomerSource(cmd.getCustomerSource());
                        if(StringUtils.isNotBlank(contact.getName()) && StringUtils.isNotBlank(contact.getPhoneNumber())){
                            invitedCustomerProvider.createContact(contact);
                        }
                    });
                }
                // reflush requirement
                if (cmd.getRequirement() != null) {
                    CustomerRequirement requirement = ConvertHelper.convert(cmd.getRequirement(), CustomerRequirement.class);
                    requirement.setCommunityId(cmd.getCommunityId());
                    requirement.setNamespaceId(cmd.getNamespaceId());
                    requirement.setCustomerId(cmd.getId());
                    requirement.setStatus(CommonStatus.ACTIVE.getCode());
                    Long requirementId = invitedCustomerProvider.createRequirement(requirement);
                    if (cmd.getRequirement().getAddresses() != null && cmd.getRequirement().getAddresses().size() > 0) {
                        cmd.getRequirement().getAddresses().forEach((c) -> {
                            CustomerRequirementAddress address = ConvertHelper.convert(c, CustomerRequirementAddress.class);
                            address.setCustomerId(cmd.getId());
                            address.setCommunityId(cmd.getCommunityId());
                            address.setNamespaceId(cmd.getNamespaceId());
                            address.setStatus(CommonStatus.ACTIVE.getCode());
                            address.setRequirementId(requirementId);
                            invitedCustomerProvider.createRequirementAddress(address);
                        });
                    }

                }
                // reflush current basic info
                if (cmd.getCurrentRent() != null) {
                    CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getCurrentRent(), CustomerCurrentRent.class);
                    if (cmd.getCurrentRent().getContractIntentionDate() != null) {
                        currentRent.setContractIntentionDate(new Timestamp(cmd.getCurrentRent().getContractIntentionDate()));

                    }
                    currentRent.setCommunityId(cmd.getCommunityId());
                    currentRent.setNamespaceId(cmd.getNamespaceId());
                    currentRent.setStatus(CommonStatus.ACTIVE.getCode());
                    currentRent.setCustomerId(cmd.getId());
                    invitedCustomerProvider.createCurrentRent(currentRent);
                }
                if (cmd.getTrackers() != null) {
                    cmd.getTrackers().forEach((c) -> {
                        CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                        tracker.setCommunityId(cmd.getCommunityId());
                        tracker.setNamespaceId(cmd.getNamespaceId());
                        tracker.setStatus(CommonStatus.ACTIVE.getCode());
                        tracker.setCustomerId(cmd.getId());
                        if(tracker.getTrackerUid() != null && tracker.getTrackerUid() != 0){
                            invitedCustomerProvider.createTracker(tracker);
                        }
                    });
                }

                EnterpriseCustomer dto = ConvertHelper.convert(customer, EnterpriseCustomer.class);

                dto.setStatus(CommonStatus.ACTIVE.getCode());
                dto.setNamespaceId(cmd.getNamespaceId());
                customerSearcher.feedDoc(dto);

                return ConvertHelper.convert(cmd, InvitedCustomerDTO.class);
            }
        }catch(RuntimeErrorException e){
            LOGGER.error(e.getMessage());
            throw e;
        }

        return null;
    }


    @Override
    public void changeCustomerAptitude(SearchEnterpriseCustomerCommand cmd){
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_CHANGE_APTITUDE, cmd.getOrgId(), cmd.getCommunityId());
        Boolean isAdmin = checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        SearchEnterpriseCustomerResponse res = null;
        if(cmd.getCustomerIds()!= null && cmd.getCustomerIds().size() > 0){
            res = customerSearcher.queryEnterpriseCustomersById(cmd);
        }else{
            res = customerSearcher.queryEnterpriseCustomers(cmd, isAdmin);

        }
        for(EnterpriseCustomerDTO dto : res.getDtos()){
            customerProvider.updateCustomerAptitudeFlag(dto.getId(), 1l);
        }
    }

    @Override
    public String signCustomerDataToThird(SignCustomerDataToThirdCommand cmd){
        ViewInvestmentDetailCommand cmd2 = new ViewInvestmentDetailCommand();
        cmd2.setIsAdmin(true);
        cmd2.setId(cmd.getId());
        cmd2.setOrgId(cmd.getOrgId());
        cmd2.setCommunityId(cmd.getCommunityId());
        InvitedCustomerDTO customer = viewInvestmentDetail(cmd2);

        Map<String, String> params= new HashMap<>();
        Map<String, String> codeString= new HashMap<>();

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());



        if(customer.getContacts() != null && customer.getContacts().size() > 0){
            for (CustomerContactDTO contactDTO : customer.getContacts()) {
                if(contactDTO.getContactType().equals(CustomerContactType.CUSTOMER_CONTACT.getCode())){
                    params.put("TakerName", contactDTO.getName());
                    params.put("TakerPhone", contactDTO.getPhoneNumber());
                    params.put("TakerEmail", contactDTO.getEmail());
                    break;
                }
            }
        }

        params.put("CustomerName",customer.getName());
        params.put("propertyid", community.getNamespaceCommunityToken());
        params.put("AccountNo", customer.getId().toString());
        params.put("OA-AccountID", customer.getId().toString());
        params.put("IsCompany", RACustomerType.CUSTOMER_COMPANY);

        String Sales = organizationService.getAccountByTargetIdAndOrgId(UserContext.current().getUser().getId(), cmd.getOrgId());
        if(StringUtils.isNotBlank(Sales)){
            params.put("Sales", Sales);
        }else{
            params.put("Sales", "office.app");
        }



        String account = null;
        String url = configurationProvider.getValue("RuiAnCM.sync.url", "");

        try {
            String codeStr = JSONObject.toJSONString(params);
            codeString.put("CodeStr", codeStr);
            LOGGER.info("create Customer for RuiAnCM param: {}", codeString);
            account = HttpUtils.get(url+CreateCustomer, codeString, 60, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("create Customer for RuiAnCM error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ContractErrorCode.ERROR_CONTRACT_SYNC_UNKNOW_ERROR, "sync customer from RuiAnCM error");
        }
        account = account.substring(account.indexOf(">{")+1, account.indexOf("</string>"));

        LOGGER.info("create Customer from RuiAnCM is complete. ");

        RAAccountObject raAccountObject =
                (RAAccountObject) StringHelper.fromJsonString(account, RAAccountObject.class);

        LOGGER.info("receive syncCustomer for RuiAnCM param: {}", account);

        if(raAccountObject.getErrorCode().equals(SUCCESS_CODE) || raAccountObject.getErrorCode().equals(1)){
            try {
                String codeStr = JSONObject.toJSONString(params);
                codeString.put("CodeStr", codeStr);
                LOGGER.info("get create contract url for RuiAnCM param: {}", codeString);
                //jumpUrl = HttpUtils.get(url+CreateContract, codeString, 60, "UTF-8");

                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : codeString.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                String paramstr = URLEncodedUtils.format(qparams, "UTF-8");
                if (StringUtils.isNotEmpty(paramstr)) {
                    url = url + CreateContract + "?" + paramstr;
                }

                return url;
            } catch (Exception e) {
                LOGGER.error("get create contract url  RuiAnCM error: {}", e);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ContractErrorCode.ERROR_CONTRACT_SYNC_UNKNOW_ERROR, "sync customer from RuiAnCM error");
            }
        }else{
            LOGGER.error("sync from RuiAnCM error code : {}, error description : {}, error detail : {}", raAccountObject.getErrorCode(), raAccountObject.getErrorDescription(), raAccountObject.getErrorDetails());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, raAccountObject.getErrorCode(), raAccountObject.getErrorDescription());
        }

    }

    @Override
    public List<CustomerStatisticsDTO> getAllCommunityCustomerStatisticsDaily(GetAllCommunityCustomerStatisticsDailyCommand cmd) {
        return null;
    }

    @Override
    public List<CustomerStatisticsDTO> getAllCommunityCustomerStatisticsMonthly(GetAllCommunityCustomerStatisticsMonthlyCommand cmd) {
        return null;
    }

    @Override
    public GetCustomerStatisticResponse getCustomerStatisticsDaily(GetCustomerStatisticsCommand cmd) {
        List<CommunityCustomerStatisticDTO> result = new ArrayList<>();

        java.sql.Date startQueryTime = null;
        java.sql.Date endQueryTime = null;
        if(cmd.getStartQueryTime() != null){
            startQueryTime = getDateByTimestamp(new Timestamp(cmd.getStartQueryTime()));
        }
        if(cmd.getEndQueryTime() != null){
            endQueryTime = getDateByTimestamp(new Timestamp(cmd.getEndQueryTime()));
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0);
        }
        //如果传了园区ID，则根据园区ID筛选数据，此时为分园区的查询
        List<CustomerStatisticDaily> dailies = invitedCustomerProvider.listCustomerStatisticDaily(cmd.getNamespaceId(), cmd.getCommunities(),
                startQueryTime, endQueryTime, cmd.getPageSize(), cmd.getPageAnchor());

        Integer nextPageAnchor = null;
        if(dailies.size() == cmd.getPageSize() + 1){
            nextPageAnchor = cmd.getPageAnchor() + 1;
            dailies.remove(dailies.size() - 1);
        }
        GetCustomerStatisticResponse response = new GetCustomerStatisticResponse();
        List<CustomerStatisticsDTO> dtos = dailies.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList());
        for(CustomerStatisticsDTO dto: dtos){
            dto.setCommunityName(communityProvider.findCommunityById(dto.getCommunityId()).getName());
        }
        response.setDtos(dtos);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }


    @Override
    public GetCustomerStatisticResponse getCustomerStatisticsDailyTotal(GetCustomerStatisticsCommand cmd) {
        java.sql.Date startQueryTime = null;
        java.sql.Date endQueryTime = null;
        if(cmd.getStartQueryTime() != null){
            startQueryTime = getDateByTimestamp(new Timestamp(cmd.getStartQueryTime()));
        }
        if(cmd.getEndQueryTime() != null){
            endQueryTime = getDateByTimestamp(new Timestamp(cmd.getEndQueryTime()));
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0);
        }
        List<CustomerStatisticDailyTotal> dailies = invitedCustomerProvider.listCustomerStatisticDailyTotal(cmd.getNamespaceId(), cmd.getOrgId(),
                startQueryTime, endQueryTime, cmd.getPageSize(), cmd.getPageAnchor());

        GetCustomerStatisticResponse response = new GetCustomerStatisticResponse();
        Integer nextPageAnchor = null;
        if(dailies.size() == cmd.getPageSize() + 1){
            nextPageAnchor = cmd.getPageAnchor() + 1;
            dailies.remove(dailies.size() - 1);
        }
        response.setDtos(dailies.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList()));
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    @Override
    public GetCustomerStatisticResponse getCustomerStatisticsMonthly(GetCustomerStatisticsCommand cmd) {
        java.sql.Date startQueryTime = null;
        java.sql.Date endQueryTime = null;
        if(cmd.getStartQueryTime() != null){
            startQueryTime = getDateByTimestamp(new Timestamp(cmd.getStartQueryTime()));
        }
        if(cmd.getEndQueryTime() != null){
            endQueryTime = getDateByTimestamp(new Timestamp(cmd.getEndQueryTime()));
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0);
        }
        List<CommunityCustomerStatisticDTO> result = new ArrayList<>();
        List<CustomerStatisticMonthly> monthlies = invitedCustomerProvider.listCustomerStatisticMonthly(cmd.getNamespaceId(), cmd.getCommunities(),
                startQueryTime, endQueryTime, cmd.getPageSize(), cmd.getPageAnchor());

        Integer nextPageAnchor = null;
        if(monthlies.size() == cmd.getPageSize() + 1){
            nextPageAnchor =  cmd.getPageAnchor() + 1;
            monthlies.remove(monthlies.size() - 1);
        }
        GetCustomerStatisticResponse response = new GetCustomerStatisticResponse();
        List<CustomerStatisticsDTO> dtos = monthlies.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList());
        for(CustomerStatisticsDTO dto: dtos){
            dto.setCommunityName(communityProvider.findCommunityById(dto.getCommunityId()).getName());
        }
        response.setDtos(dtos);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    @Override
    public GetCustomerStatisticResponse getCustomerStatisticsMonthlyTotal(GetCustomerStatisticsCommand cmd) {

        java.sql.Date startQueryTime = null;
        java.sql.Date endQueryTime = null;
        if(cmd.getStartQueryTime() != null){
            startQueryTime = getDateByTimestamp(new Timestamp(cmd.getStartQueryTime()));
        }
        if(cmd.getEndQueryTime() != null){
            endQueryTime = getDateByTimestamp(new Timestamp(cmd.getEndQueryTime()));
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0);
        }
        List<CustomerStatisticMonthlyTotal> monthlyTotals = invitedCustomerProvider.listCustomerStatisticMonthlyTotal(cmd.getNamespaceId(), cmd.getOrgId(),
                startQueryTime, endQueryTime, cmd.getPageSize(), cmd.getPageAnchor());

        GetCustomerStatisticResponse response = new GetCustomerStatisticResponse();
        Integer nextPageAnchor = null;
        if(monthlyTotals.size() == cmd.getPageSize() + 1){
            nextPageAnchor = cmd.getPageAnchor() + 1;
            monthlyTotals.remove(monthlyTotals.size() - 1);
        }
        response.setDtos(monthlyTotals.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList()));
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    @Override
    public StatisticDataDTO getCustomerStatisticsTotal(GetCustomerStatisticsCommand cmd) {
        StatisticTime time = getBeforeForStatistic(new Date(), Calendar.DAY_OF_MONTH);
        java.sql.Date startQueryTime = new java.sql.Date(getDateByTimestamp(time.getStatisticStartTime()).getTime());
        CustomerStatisticTotal total = invitedCustomerProvider.getCustomerStatisticsTotal(cmd.getNamespaceId(), cmd.getOrgId(), startQueryTime);
        return ConvertHelper.convert(total, StatisticDataDTO.class);
    }

    @Override
    public GetCustomerStatisticResponse getCustomerStatisticsNow(GetCustomerStatisticsNowCommand cmd) {
        return null;
    }

    @Override
    public void initCustomerStatusToDB(){
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize" , 500);
        taskService.createTask("initCustomerStatusToDB", TaskType.FILEDOWNLOAD.getCode(), InitCustomerStatisticsHandle.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());

    }

    @Override
    public void recordCustomerLevelChange(Long oldLevelItemId, Long newLevelItemId, Integer namespaceId, Long communityId, Long customerId, Timestamp changeDate) {
        if(newLevelItemId != null) {
            if (!newLevelItemId.equals(oldLevelItemId)) {
                CustomerLevelChangeRecord record = new CustomerLevelChangeRecord();
                record.setCustomerId(customerId);
                record.setCommunityId(communityId);
                record.setOldStatus(oldLevelItemId);
                record.setNewStatus(newLevelItemId);
                record.setChangeDate(changeDate);
                record.setNamespaceId(namespaceId);
                invitedCustomerProvider.createCustomerLevelChangeRecord(record);
            }
        }
    }
    @Override
    public void changeCustomerLevel(EnterpriseCustomer customer, Long newLevelItemId){
        recordCustomerLevelChange(customer.getLevelItemId(), newLevelItemId, customer.getNamespaceId(), customer.getCommunityId(), customer.getId(), null);
        //customer.setLevelItemId(levelItemId);
        //还未确定是否改变客户状态就会改变客户类型

        /*if(levelItemId.equals(CustomerLevelType.REGISTERED_CUSTOMER.getCode())){
            customer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
        }else{
            customer.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
        }*/
    }

    @Override
    public void changeCustomerLevelByCustomerId(Long customerId, Long newLevelItemId){
        EnterpriseCustomer customer = customerProvider.findById(customerId);
        changeCustomerLevel(customer, newLevelItemId);
    }

    @Override
    public void createCustomerLevelByCustomerId(Long customerId, Long newLevelItemId){
        EnterpriseCustomer customer = customerProvider.findById(customerId);
        customer.setLevelItemId(null);
        changeCustomerLevel(customer, newLevelItemId);
    }


    //开始进行统计，将所有的管理公司取出，并根据其下的园区进行统计
    @Override
    public List<StatisticDataDTO> startCustomerStatisticTotal(StatisticTime time){
        List<Long> orgIds = organizationProvider.getOrganizationIdsHaveCommunity();
        List<Organization> orgs = organizationProvider.listOrganizationsByIds(orgIds);
        Timestamp statisticStartTime = time.getStatisticStartTime();
        Timestamp statisticEndTime = time.getStatisticEndTime();


        List<StatisticDataDTO> result = new ArrayList<>();

        for(Organization org : orgs){
            ListCommunitiesCommand cmd2 = new ListCommunitiesCommand();
            cmd2.setNamespaceId(org.getNamespaceId());
            cmd2.setOrgId(org.getId());
            ListCommunitiesResponse response = communityService.listCommunities(cmd2);
            List<CommunityDTO> communities = response.getList();

            List<StatisticDataDTO> tempResult = new ArrayList<>();
            for(CommunityDTO community : communities) {

                tempResult.add(statisticCustomerNum(org.getNamespaceId(), community.getId(), statisticStartTime, statisticEndTime));
            }

            StatisticDataDTO data = new StatisticDataDTO();
            data.setNamespaceId(org.getNamespaceId());
            data.setOrganizationId(org.getId());
            data.setCommunityNum(communities.size());
            data.setNewCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getNewCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            data.setTrackingNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getTrackingNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            data.setLossCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getLossCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            data.setHistoryCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getHistoryCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            data.setRegisteredCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getRegisteredCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            data.setDeleteCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getDeleteCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
            result.add(data);

        }
        return result;
    }


    @Override
    public List<StatisticDataDTO> startCustomerStatistic(StatisticTime time){
        List<Community>  allCommunities = communityProvider.listAllBizCommunities();
        Timestamp statisticStartTime = time.getStatisticStartTime();
        Timestamp statisticEndTime = time.getStatisticEndTime();


        List<StatisticDataDTO> result = new ArrayList<>();

        for(Community community : allCommunities){

            result.add(statisticCustomerNum(community.getNamespaceId(), community.getId(), statisticStartTime, statisticEndTime));

        }
        return result;
    }

    @Override
    public StatisticTime getBeforeForStatistic(Date date, int type) {

        /*
         * 支持两种计算
         * Calendar. DAY_OF_MONTH：获取当前传入时间的前一天的一整天的时间
         * Calendar. MONTH ： 获取当前传入时间的前一月的一整月的时间
         *
         */
        if(type == Calendar. DAY_OF_MONTH) {
            //Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();

            Timestamp statisticStartTime = new Timestamp(date.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            date = calendar.getTime();
            Timestamp statisticEndTime = new Timestamp(date.getTime());

            StatisticTime result = new StatisticTime();
            result.setStatisticEndTime(statisticEndTime);
            result.setStatisticStartTime(statisticStartTime);
            return result;
        }
        if(type == Calendar. MONTH){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar. HOUR_OF_DAY, 0);
            calendar.set(Calendar. MINUTE, 0);
            calendar.set(Calendar. SECOND, 0);
            calendar.add(Calendar. MONTH, -1);
            calendar.set(Calendar. MILLISECOND, 0);
            calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            date = calendar.getTime();

            Timestamp statisticStartTime = new Timestamp(date.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            date = calendar.getTime();
            Timestamp statisticEndTime = new Timestamp(date.getTime());

            StatisticTime result = new StatisticTime();
            result.setStatisticEndTime(statisticEndTime);
            result.setStatisticStartTime(statisticStartTime);
            return result;
        }
        if(type == Calendar. YEAR){
            //Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();
            Timestamp statisticEndTime = new Timestamp(date.getTime());

            StatisticTime result = new StatisticTime();
            result.setStatisticEndTime(statisticEndTime);
            result.setStatisticStartTime(null);
            return result;
        }
        return null;

    }

    @Override
    public StatisticTime getNowForStatistic(Date date, int type) {

        /*
         * 支持两种计算
         * Calendar. DAY_OF_MONTH：获取当前传入时间的当天的一整天的时间
         * Calendar. MONTH ： 获取当前传入时间的当月的一整月的时间
         *
         */
        if(type == Calendar. DAY_OF_MONTH) {
            //Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            date = calendar.getTime();

            Timestamp statisticStartTime = new Timestamp(date.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            date = calendar.getTime();
            Timestamp statisticEndTime = new Timestamp(date.getTime());

            StatisticTime result = new StatisticTime();
            result.setStatisticEndTime(statisticEndTime);
            result.setStatisticStartTime(statisticStartTime);
            return result;
        }
        if(type == Calendar. MONTH){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar. HOUR_OF_DAY, 0);
            calendar.set(Calendar. MINUTE, 0);
            calendar.set(Calendar. SECOND, 0);
            calendar.set(Calendar. MILLISECOND, 0);
            calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            date = calendar.getTime();

            Timestamp statisticStartTime = new Timestamp(date.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            date = calendar.getTime();
            Timestamp statisticEndTime = new Timestamp(date.getTime());

            StatisticTime result = new StatisticTime();
            result.setStatisticEndTime(statisticEndTime);
            result.setStatisticStartTime(statisticStartTime);
            return result;
        }
        return null;

    }


    @Override
    public void statisticCustomerDailyTotal(Date date){
        LOGGER.info("the scheduleJob of customer daily total by organization statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. DAY_OF_MONTH);
        List<StatisticDataDTO> datas = startCustomerStatisticTotal(statisticTime);

        invitedCustomerProvider.deleteCustomerStatisticDailyTotal(null, null, getDateByTimestamp(statisticTime.getStatisticStartTime()));
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticStartTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticDailyTotal dailyTotal = ConvertHelper.convert(data, CustomerStatisticDailyTotal.class);
                    invitedCustomerProvider.createCustomerStatisticsDailyTotal(dailyTotal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        LOGGER.info("the scheduleJob of customer daily statistics is end!, result = {}" , datas);

    }

    @Override
    public void statisticCustomerDaily(Date date){
        LOGGER.info("the scheduleJob of customer daily statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. DAY_OF_MONTH);
        List<StatisticDataDTO> datas = startCustomerStatistic(statisticTime);

        invitedCustomerProvider.deleteCustomerStatisticDaily(null, null, getDateByTimestamp(statisticTime.getStatisticStartTime()));
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticStartTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticDaily daily = ConvertHelper.convert(data, CustomerStatisticDaily.class);
                    invitedCustomerProvider.createCustomerStatisticsDaily(daily);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        LOGGER.info("the scheduleJob of customer daily statistics is end!, result = {}" , datas);

    }

    @Override
    public void statisticCustomerMonthlyTotal(Date date){
        LOGGER.info("the scheduleJob of customer monthly total by organization statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. MONTH);
        List<StatisticDataDTO> datas = startCustomerStatisticTotal(statisticTime);

        invitedCustomerProvider.deleteCustomerStatisticMonthlyTotal(null, null, getDateByTimestamp(statisticTime.getStatisticStartTime()));
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticStartTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticMonthlyTotal monthlyTotal = ConvertHelper.convert(data, CustomerStatisticMonthlyTotal.class);
                    invitedCustomerProvider.createCustomerStatisticsMonthlyTotal(monthlyTotal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        LOGGER.info("the scheduleJob of customer daily statistics is end!, result = {}" , datas);

    }

    @Override
    public void statisticCustomerMonthly(Date date){
        LOGGER.info("the scheduleJob of customer monthly statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. MONTH);
        List<StatisticDataDTO> datas = startCustomerStatistic(statisticTime);

        invitedCustomerProvider.deleteCustomerStatisticMonthly(null, null, getDateByTimestamp(statisticTime.getStatisticStartTime()));
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticStartTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticMonthly monthly = ConvertHelper.convert(data, CustomerStatisticMonthly.class);
                    invitedCustomerProvider.createCustomerStatisticsMonthly(monthly);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("the scheduleJob of customer monthly statistics is end!, result = {}" , datas);

    }

    @Override
    public void statisticCustomerTotal(Date date){
        LOGGER.info("the scheduleJob of customer total statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. YEAR);
        List<StatisticDataDTO> datas = startCustomerStatisticTotal(statisticTime);

        Timestamp endTime = statisticTime.getStatisticEndTime();
        invitedCustomerProvider.deleteCustomerStatisticTotal(null, null, getDateByTimestamp(endTime));
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticEndTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticTotal total = ConvertHelper.convert(data, CustomerStatisticTotal.class);
                    invitedCustomerProvider.createCustomerStatisticTotal(total);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("the scheduleJob of customer total statistics is end!, result = {}" , datas);
    }

    @Override
    public void statisticCustomerAll(Date date){
        LOGGER.info("the scheduleJob of customer monthly statistics is start!");
        //Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        StatisticTime statisticTime = getBeforeForStatistic(date , Calendar. DAY_OF_MONTH);
        statisticTime.setStatisticEndTime(null);
        startCustomerStatistic(statisticTime);

    }

    @Override
    public void testCustomerStatistic(TestCreateCustomerStatisticCommand cmd){
        CustomerStatisticType type = CustomerStatisticType.fromCode(cmd.getType());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        switch (type) {
            case STATISTIC_DAILY:
                try {
                    Date date = format.parse(cmd.getDate());
                    statisticCustomerDaily(date);
                    statisticCustomerDailyTotal(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case STATISTIC_MONTHLY:
                try {
                    Date date = format.parse(cmd.getDate());
                    statisticCustomerMonthly(date);
                    statisticCustomerMonthlyTotal(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case STATISTIC_ALL:
                try {
                    Date date = format.parse(cmd.getDate());
                    statisticCustomerTotal(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            default:
        }
    }

    @Override
    public java.sql.Date getDateByTimestamp(Timestamp time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time.toString());
            java.sql.Date result = new java.sql.Date(date.getTime());
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GetCustomerStatisticNowResponse queryCustomerStatisticMonthlyNow(GetCustomerStatisticNowCommand cmd) {
        StatisticTime time = getNowForStatistic(new Date(), Calendar.MONTH);
        GetCustomerStatisticNowResponse response = communityCustomerStatistic(time, cmd.getOrgId(), cmd.getNamespaceId(), cmd.getPageSize(), cmd.getPageAnchor());

        return response;
    }

    @Override
    public CustomerStatisticsDTO queryCustomerStatisticMonthlyTotalNow(GetCustomerStatisticsCommand cmd) {
        StatisticTime time = getNowForStatistic(new Date(), Calendar.MONTH);
        StatisticDataDTO datas = organizationCustomerStatisticTotal(time, cmd.getOrgId(), cmd.getNamespaceId());

        return ConvertHelper.convert(datas, CustomerStatisticsDTO.class);
    }

    @Override
    public GetCustomerStatisticNowResponse queryCustomerStatisticDailyNow(GetCustomerStatisticNowCommand cmd) {
        StatisticTime time = getNowForStatistic(new Date(), Calendar.DAY_OF_MONTH);
        GetCustomerStatisticNowResponse response = communityCustomerStatistic(time, cmd.getOrgId(), cmd.getNamespaceId(), cmd.getPageSize(), cmd.getPageAnchor());

        return response;
    }

    @Override
    public CustomerStatisticsDTO queryCustomerStatisticDailyTotalNow(GetCustomerStatisticsCommand cmd) {
        StatisticTime time = getNowForStatistic(new Date(), Calendar.DAY_OF_MONTH);
        StatisticDataDTO datas = organizationCustomerStatisticTotal(time, cmd.getOrgId(), cmd.getNamespaceId());

        return ConvertHelper.convert(datas, CustomerStatisticsDTO.class);
    }

    @Override
    public CustomerStatisticsDTO queryCustomerStatisticTotal(GetCustomerStatisticsCommand cmd){
        StatisticTime time = getNowForStatistic(new Date(), Calendar.DAY_OF_MONTH);
        StatisticDataDTO datas = organizationCustomerStatisticTotal(time, cmd.getOrgId(), cmd.getNamespaceId());
        StatisticDataDTO datas2 = getCustomerStatisticsTotal(cmd);

        if(datas != null && datas2 != null) {
            datas.setNewCustomerNum(datas.getNewCustomerNum() + datas2.getNewCustomerNum());
            datas.setLossCustomerNum(datas.getLossCustomerNum() + datas2.getLossCustomerNum());
            datas.setHistoryCustomerNum(datas.getHistoryCustomerNum() + datas2.getHistoryCustomerNum());
            datas.setDeleteCustomerNum(datas.getDeleteCustomerNum() + datas2.getDeleteCustomerNum());
            datas.setTrackingNum(datas.getTrackingNum() + datas2.getTrackingNum());
        }

        return ConvertHelper.convert(datas, CustomerStatisticsDTO.class);

    }

    private GetCustomerStatisticNowResponse communityCustomerStatistic(StatisticTime time, Long orgId, Integer namespaceId, Integer pageSize, Long pageAnchor){
        Timestamp statisticStartTime = time.getStatisticStartTime();
        Timestamp statisticEndTime = time.getStatisticEndTime();


        List<StatisticDataDTO> dtos = new ArrayList<>();

        ListCommunitiesCommand cmd2 = new ListCommunitiesCommand();
        cmd2.setNamespaceId(namespaceId);
        cmd2.setOrgId(orgId);
        cmd2.setPageAnchor(pageAnchor);
        cmd2.setPageSize(pageSize);
        ListCommunitiesResponse response = communityService.listCommunities(cmd2);
        List<CommunityDTO> communities = response.getList();


        for(CommunityDTO community: communities) {
            StatisticDataDTO dto = statisticCustomerNum(namespaceId, community.getId(), statisticStartTime, statisticEndTime);
            dto.setCommunityName(community.getName());
            dtos.add(dto);
        }

        GetCustomerStatisticNowResponse result = new GetCustomerStatisticNowResponse();
        result.setDtos(dtos.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList()));
        result.setNextPageAnchor(response.getNextPageAnchor());

        return result;
    }


    private StatisticDataDTO organizationCustomerStatisticTotal(StatisticTime time, Long orgId, Integer namespaceId){
        Timestamp statisticStartTime = time.getStatisticStartTime();
        Timestamp statisticEndTime = time.getStatisticEndTime();


        List<StatisticDataDTO> result = new ArrayList<>();

        ListCommunitiesCommand cmd2 = new ListCommunitiesCommand();
        cmd2.setNamespaceId(namespaceId);
        cmd2.setOrgId(orgId);
        ListCommunitiesResponse response = communityService.listCommunities(cmd2);
        List<CommunityDTO> communities = response.getList();

        List<StatisticDataDTO> tempResult = new ArrayList<>();
        for(CommunityDTO community : communities) {
            tempResult.add(statisticCustomerNum(namespaceId, community.getId(), statisticStartTime, statisticEndTime));
        }

        StatisticDataDTO data = new StatisticDataDTO();
        data.setNamespaceId(namespaceId);
        data.setOrganizationId(orgId);
        data.setCommunityNum(communities.size());
        data.setNewCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getNewCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
        data.setTrackingNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getTrackingNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
        data.setLossCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getLossCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
        data.setHistoryCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getHistoryCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
        data.setRegisteredCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getRegisteredCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));
        data.setDeleteCustomerNum(Integer.valueOf(String.valueOf(tempResult.stream().map(StatisticDataDTO::getDeleteCustomerNum).collect(Collectors.toList()).stream().mapToInt(x->x).summaryStatistics().getSum())));

        return data;
    }

    private StatisticDataDTO statisticCustomerNum(Integer namespaceId, Long communityId, Timestamp statisticStartTime, Timestamp statisticEndTime){
        LOGGER.debug("the scheduleJob of customer statistics at community by org : {}, query start date : {}, end date : {}", communityId, statisticStartTime, statisticEndTime);

        /*List<CustomerLevelChangeRecord> listRecord = invitedCustomerProvider.listCustomerLevelChangeRecord(namespaceId, communityId, statisticStartTime, statisticEndTime);
        Integer addCustomerNum = invitedCustomerProvider.countCustomerNumByCreateDate(communityId, statisticStartTime, statisticEndTime);
        LOGGER.debug("the scheduleJob of customer statistics at community by org : {}, query start date : {}, end date : {}, add customer num : {}", communityId, statisticStartTime, statisticEndTime, addCustomerNum);
*/

        Integer lossNum = invitedCustomerProvider.countCustomerLevelLossChangeRecord(namespaceId, communityId, statisticStartTime, statisticEndTime, CustomerLevelType.LOSS_CUSTOMER.getCode());

        Integer historyNum = invitedCustomerProvider.countCustomerLevelLossChangeRecord(namespaceId, communityId, statisticStartTime, statisticEndTime, CustomerLevelType.HISTORY_CUSTOMER.getCode());

        Integer registeredNum = invitedCustomerProvider.countCustomerLevelLossChangeRecord(namespaceId, communityId, statisticStartTime, statisticEndTime, CustomerLevelType.REGISTERED_CUSTOMER.getCode());

        Integer deleteNum = invitedCustomerProvider.countCustomerLevelLossChangeRecord(namespaceId, communityId, statisticStartTime, statisticEndTime, CustomerLevelType.DELETE_CUSTOMER.getCode());

        StatisticDataDTO data = new StatisticDataDTO();
        data.setNamespaceId(namespaceId);
        data.setCommunityId(communityId);
        data.setNewCustomerNum(invitedCustomerProvider.countCustomerNumByCreateDate(communityId, statisticStartTime, statisticEndTime));
        data.setTrackingNum(invitedCustomerProvider.countTrackingNumByTrackingDate(communityId, statisticStartTime, statisticEndTime));
        data.setLossCustomerNum(lossNum);
        data.setHistoryCustomerNum(historyNum);
        data.setRegisteredCustomerNum(registeredNum);
        data.setDeleteCustomerNum(deleteNum);
        return data;
    }

    @Override
    public void exportCustomerStatistic(ExportCustomerStatisticsCommand cmd) {
        Map<String, Object> params = new HashMap<>();
        params.put("ExportCustomerStatisticsCommand", cmd);
        String exportFileNamePrefix = cmd.getPrefixFileName();
        String fileName = String.format(exportFileNamePrefix,"") + com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH) + ".xlsx";
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CustomerStatisticExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @SuppressWarnings("deprecation")
    public OutputStream exportOutputStreamCustomerStatistic(ExportCustomerStatisticsCommand cmd, Long taskId, Byte exportType){
        OutputStream outputStream = new ByteArrayOutputStream();
        //每一条数据
        Integer pageOffSet = 0;
        Integer pageSize = 100000;

        List<CustomerStatisticsDTO> dtos = new ArrayList<>();
        Workbook wb = null;
        InputStream in = null;
        switch(exportType){
            case 1:
                GetCustomerStatisticsCommand cmd2 = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                cmd2.setPageSize(pageSize);
                cmd2.setPageAnchor(pageOffSet);
                GetCustomerStatisticResponse response = getCustomerStatisticsDaily(cmd2);
                in = this.getClass().getResourceAsStream("/excels/customer/daily.xlsx");
                dtos = response.getDtos();
                break;
            case 2:
                GetCustomerStatisticsCommand cmd3 = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                cmd3.setPageSize(pageSize);
                cmd3.setPageAnchor(pageOffSet);
                GetCustomerStatisticResponse response2 = getCustomerStatisticsDailyTotal(cmd3);
                in = this.getClass().getResourceAsStream("/excels/customer/dailyTotal.xlsx");
                dtos = response2.getDtos();
                break;
            case 3:
                GetCustomerStatisticNowCommand cmd4 = ConvertHelper.convert(cmd, GetCustomerStatisticNowCommand.class);
                cmd4.setPageSize(pageSize);
                GetCustomerStatisticNowResponse response3 = queryCustomerStatisticDailyNow(cmd4);
                in = this.getClass().getResourceAsStream("/excels/customer/dailyNow.xlsx");
                dtos = response3.getDtos();
                break;
            case 4:
                GetCustomerStatisticsCommand cmd5 = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                cmd5.setPageSize(pageSize);
                cmd5.setPageAnchor(pageOffSet);
                GetCustomerStatisticResponse response4 = getCustomerStatisticsMonthly(cmd5);
                in = this.getClass().getResourceAsStream("/excels/customer/monthly.xlsx");
                dtos = response4.getDtos();
                break;
            case 5:
                GetCustomerStatisticsCommand cmd6 = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                cmd6.setPageSize(pageSize);
                cmd6.setPageAnchor(pageOffSet);
                GetCustomerStatisticResponse response5 = getCustomerStatisticsMonthlyTotal(cmd6);
                in = this.getClass().getResourceAsStream("/excels/customer/monthlyTotal.xlsx");
                dtos = response5.getDtos();
                break;
            case 6:
                GetCustomerStatisticNowCommand cmd7 = ConvertHelper.convert(cmd, GetCustomerStatisticNowCommand.class);
                cmd7.setPageSize(pageSize);
                GetCustomerStatisticNowResponse response6 = queryCustomerStatisticDailyNow(cmd7);
                in = this.getClass().getResourceAsStream("/excels/customer/monthlyNow.xlsx");
                dtos = response6.getDtos();
                break;
            case 7:
                GetCustomerStatisticsCommand cmd8 = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                cmd8.setPageSize(pageSize);
                CustomerStatisticsDTO dto = queryCustomerStatisticTotal(cmd8);
                in = this.getClass().getResourceAsStream("/excels/customer/total.xlsx");
                dtos.add(dto);
                break;
            default:
                //in = this.getClass().getResourceAsStream("/excels/customer/daily.xlsx");
                break;
        }


        taskService.updateTaskProcess(taskId, 20);
        try {
            wb = new XSSFWorkbook(copyInputStream(in));
        } catch (IOException e) {
            LOGGER.error("exportOutputStreamCustomerStatistic copy inputStream error.");
        }
        Sheet sheet = wb.getSheetAt(0);
        if (null != sheet) {

            if(exportType == 1 || exportType == 2 || exportType == 4 || exportType == 5 ) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Row firstRow = sheet.getRow(0);
                Cell title = firstRow.getCell(0);
                if(cmd.getStartQueryTime() != null && cmd.getEndQueryTime() != null) {
                    Date startDate = new Date(cmd.getStartQueryTime());
                    Date endDate = new Date(cmd.getEndQueryTime());
                    title.setCellValue(title.getStringCellValue() + "（" + format.format(startDate) + "~" + format.format(endDate) + "）");
                }else if(cmd.getStartQueryTime() != null && cmd.getEndQueryTime() == null){
                    Date startDate = new Date(cmd.getStartQueryTime());
                    Date endDate = new Date();
                    title.setCellValue(title.getStringCellValue() + "（" + format.format(startDate) + "~" + format.format(endDate) + "）");
                }else if(cmd.getStartQueryTime() == null && cmd.getEndQueryTime() != null){
                    Date endDate = new Date(cmd.getEndQueryTime());
                    title.setCellValue(title.getStringCellValue() + "（" + " " + "~" + format.format(endDate) + "）");
                }

            }else{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date nowDate = new Date();
                Row firstRow = sheet.getRow(0);
                Cell title = firstRow.getCell(0);
                title.setCellValue(title.getStringCellValue() + "（" + format.format(nowDate) + "）");
            }


            Row defaultRow = sheet.getRow(1);
            Cell cell = defaultRow.getCell(1);
            CellStyle style = cell.getCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中
            int size = 0;
            if(null != dtos){
                size = dtos.size();
                taskService.updateTaskProcess(taskId, 30);
                for(int i = 0;i < size;i++){
                    Row tempRow = sheet.createRow(i + 2);
                    CustomerStatisticsDTO dto = dtos.get(i);
                    int orderNum = i + 1;//序号
                    boolean isLastRow = false;
                    if(exportType == 1 || exportType == 4){
                        fillRowCellCustomerStatistic(tempRow, style, isLastRow, orderNum, dto);
                    }else if(exportType == 2 || exportType == 5){
                        fillRowCellCustomerStatisticTotal(tempRow, style, isLastRow, orderNum, dto);
                    }else if(exportType == 3 || exportType == 6){
                        fillRowCellCustomerStatisticNowWithTotal(tempRow, style, isLastRow, orderNum, dto);
                    }else if(exportType == 7){
                        fillRowCellCustomerStatisticNowTotal(tempRow, style, isLastRow, orderNum, dto);
                    }
                }
                taskService.updateTaskProcess(taskId, 70);
                //最后的一行总计
                if(exportType == 3 || exportType == 6){
                    GetCustomerStatisticsCommand cmdLast = ConvertHelper.convert(cmd, GetCustomerStatisticsCommand.class);
                    CellStyle totalStyle = getTotalStyle(wb, style);
                    Row tempRow = sheet.createRow(dtos.size() + 2);
                    int orderNum = dtos.size() + 1;//序号
                    boolean isLastRow = true;
                    CustomerStatisticsDTO totalDTO = queryCustomerStatisticDailyTotalNow(cmdLast);
                    fillRowCellCustomerStatisticNowWithTotal(tempRow, totalStyle, isLastRow, orderNum, totalDTO );
                }

                taskService.updateTaskProcess(taskId, 80);
                //设置自适应列宽
				for(int i = 0; i < 10;i++) {
                    sheet.setColumnWidth(i, "汉字".getBytes().length*2*256);
				}
                try {
                    wb.write(outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return outputStream;
            }else {
                throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
            }
        }
        return outputStream;
    }

    private InputStream copyInputStream(InputStream source) {
        if(null == source)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = source.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            LOGGER.error("ExportTasks is fail, cmd={}");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_FLIE_EXPORT_FAIL,
                    "ExportTasks is fail.");
        }
        // 打开一个新的输入流
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * @param tempRow ： Excel的行
     * @param style
     * @param isLastRow ： 是否是最后一行
     * @param orderNum ： 序号
     * @param dto
     */
    private void fillRowCellCustomerStatistic(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, CustomerStatisticsDTO dto) {
        //序号
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        Cell cell1 = tempRow.createCell(0);
        cell1.setCellStyle(style);
        if(isLastRow) {
            cell1.setCellValue(dto.getDateStr() != null ? format.format(dto.getDateStr()) : "");
        }else {
            cell1.setCellValue(dto.getDateStr() != null ? format.format(dto.getDateStr()) : "");
        }
        Cell cell2 = tempRow.createCell(1);
        cell2.setCellStyle(style);
        cell2.setCellValue(dto.getCommunityName()!= null ? dto.getCommunityName() : "");
        //项目名称
        Cell cell3 = tempRow.createCell(2);
        cell3.setCellStyle(style);
        cell3.setCellValue(dto.getNewCustomerNum()!= null ? dto.getNewCustomerNum().toString() : "0");
        //项目分类
        Cell cell4 = tempRow.createCell(3);
        cell4.setCellStyle(style);
        cell4.setCellValue(dto.getLossCustomerNum() != null ? dto.getLossCustomerNum().toString() : "0");
        //楼宇总数
        Cell cell5 = tempRow.createCell(4);
        cell5.setCellStyle(style);
        cell5.setCellValue(dto.getRegisteredCustomerNum() != null ? dto.getRegisteredCustomerNum().toString() : "0");
        //建筑面积
        Cell cell6 = tempRow.createCell(5);
        cell6.setCellStyle(style);
        cell6.setCellValue(dto.getHistoryCustomerNum() != null ? dto.getHistoryCustomerNum().toString() : "0");
        //应收含税金额(元)
        Cell cell7 = tempRow.createCell(6);
        cell7.setCellStyle(style);
        cell7.setCellValue(dto.getDeleteCustomerNum() != null ? dto.getDeleteCustomerNum().toString() : "0");
        //已收金额(元)
        Cell cell8 = tempRow.createCell(7);
        cell8.setCellStyle(style);
        cell8.setCellValue(dto.getTrackingNum() != null ? dto.getTrackingNum().toString() : "0");

    }


    private void fillRowCellCustomerStatisticTotal(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, CustomerStatisticsDTO dto) {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        //序号
        Cell cell1 = tempRow.createCell(0);
        cell1.setCellStyle(style);
        if(isLastRow) {
            cell1.setCellValue(dto.getDateStr() != null ? format.format(dto.getDateStr()) : "");
        }else {
            cell1.setCellValue(dto.getDateStr() != null ? format.format(dto.getDateStr()) : "");
        }
        Cell cell2 = tempRow.createCell(1);
        cell2.setCellStyle(style);
        cell2.setCellValue(dto.getCommunityNum()!= null ? dto.getCommunityNum() : 0);
        //项目名称
        Cell cell3 = tempRow.createCell(2);
        cell3.setCellStyle(style);
        cell3.setCellValue(dto.getNewCustomerNum()!= null ? dto.getNewCustomerNum() : 0);
        //项目分类
        Cell cell4 = tempRow.createCell(3);
        cell4.setCellStyle(style);
        cell4.setCellValue(dto.getLossCustomerNum() != null ? dto.getLossCustomerNum() : 0);
        //楼宇总数
        Cell cell5 = tempRow.createCell(4);
        cell5.setCellStyle(style);
        cell5.setCellValue(dto.getRegisteredCustomerNum() != null ? dto.getRegisteredCustomerNum() : 0);
        //建筑面积
        Cell cell6 = tempRow.createCell(5);
        cell6.setCellStyle(style);
        cell6.setCellValue(dto.getHistoryCustomerNum() != null ? dto.getHistoryCustomerNum() : 0);
        //应收含税金额(元)
        Cell cell7 = tempRow.createCell(6);
        cell7.setCellStyle(style);
        cell7.setCellValue(dto.getDeleteCustomerNum() != null ? dto.getDeleteCustomerNum() : 0);
        //已收金额(元)
        Cell cell8 = tempRow.createCell(7);
        cell8.setCellStyle(style);
        cell8.setCellValue(dto.getTrackingNum() != null ? dto.getTrackingNum() : 0);

    }

    private void fillRowCellCustomerStatisticNowWithTotal(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, CustomerStatisticsDTO dto) {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");

        //序号
        Cell cell1 = tempRow.createCell(0);
        cell1.setCellStyle(style);
        if(isLastRow) {
            cell1.setCellValue("合计");
        }else {
            cell1.setCellValue(orderNum);
        }
        Cell cell2 = tempRow.createCell(1);
        cell2.setCellStyle(style);
        if(isLastRow) {
            cell2.setCellValue(dto.getCommunityNum()!= null ? dto.getCommunityNum() : 0);
        }else {
            cell2.setCellValue(dto.getCommunityName()!= null ? dto.getCommunityName() : "");
        }
        //项目名称
        Cell cell3 = tempRow.createCell(2);
        cell3.setCellStyle(style);
        cell3.setCellValue(dto.getNewCustomerNum()!= null ? dto.getNewCustomerNum() : 0);
        //项目分类
        Cell cell4 = tempRow.createCell(3);
        cell4.setCellStyle(style);
        cell4.setCellValue(dto.getLossCustomerNum() != null ? dto.getLossCustomerNum() : 0);
        //楼宇总数
        Cell cell5 = tempRow.createCell(4);
        cell5.setCellStyle(style);
        cell5.setCellValue(dto.getRegisteredCustomerNum() != null ? dto.getRegisteredCustomerNum() : 0);
        //建筑面积
        Cell cell6 = tempRow.createCell(5);
        cell6.setCellStyle(style);
        cell6.setCellValue(dto.getHistoryCustomerNum() != null ? dto.getHistoryCustomerNum() : 0);
        //应收含税金额(元)
        Cell cell7 = tempRow.createCell(6);
        cell7.setCellStyle(style);
        cell7.setCellValue(dto.getDeleteCustomerNum() != null ? dto.getDeleteCustomerNum() : 0);
        //已收金额(元)
        Cell cell8 = tempRow.createCell(7);
        cell8.setCellStyle(style);
        cell8.setCellValue(dto.getTrackingNum() != null ? dto.getTrackingNum() : 0);

    }

    private void fillRowCellCustomerStatisticNowTotal(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, CustomerStatisticsDTO dto) {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");

        Cell cell2 = tempRow.createCell(0);
        cell2.setCellStyle(style);
        cell2.setCellValue(dto.getCommunityNum()!= null ? dto.getCommunityNum() : 0);

        //项目名称
        Cell cell3 = tempRow.createCell(1);
        cell3.setCellStyle(style);
        cell3.setCellValue(dto.getNewCustomerNum()!= null ? dto.getNewCustomerNum() : 0);
        //项目分类
        Cell cell4 = tempRow.createCell(2);
        cell4.setCellStyle(style);
        cell4.setCellValue(dto.getLossCustomerNum() != null ? dto.getLossCustomerNum() : 0);
        //楼宇总数
        Cell cell5 = tempRow.createCell(3);
        cell5.setCellStyle(style);
        cell5.setCellValue(dto.getRegisteredCustomerNum() != null ? dto.getRegisteredCustomerNum() : 0);
        //建筑面积
        Cell cell6 = tempRow.createCell(4);
        cell6.setCellStyle(style);
        cell6.setCellValue(dto.getHistoryCustomerNum() != null ? dto.getHistoryCustomerNum() : 0);
        //应收含税金额(元)
        Cell cell7 = tempRow.createCell(5);
        cell7.setCellStyle(style);
        cell7.setCellValue(dto.getDeleteCustomerNum() != null ? dto.getDeleteCustomerNum() : 0);
        //已收金额(元)
        Cell cell8 = tempRow.createCell(6);
        cell8.setCellStyle(style);
        cell8.setCellValue(dto.getTrackingNum() != null ? dto.getTrackingNum() : 0);

    }

    @SuppressWarnings("deprecation")
    private CellStyle getTotalStyle(Workbook wb, CellStyle style) {
        //生成一个字体
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLUE.index);//字体颜色
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体增粗
        //把字体应用到当前的样式
        CellStyle totalStyle = wb.createCellStyle();
        totalStyle.cloneStyleFrom(style);
        totalStyle.setFont(font);
        totalStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        totalStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        return totalStyle;
    }
}


