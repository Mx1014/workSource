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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    public GetCustomerStatisticDailyResponse getCustomerStatisticsDaily(GetCustomerStatisticsDailyCommand cmd) {
        List<CommunityCustomerStatisticDTO> result = new ArrayList<>();
        //如果传了园区ID，则根据园区ID筛选数据，此时为分园区的查询
        List<CustomerStatisticDaily> dailies = invitedCustomerProvider.listCustomerStatisticDaily(cmd.getNamespaceId(), cmd.getCommunities(),
                getDateByTimestamp(new Timestamp(cmd.getStartQueryTime())), getDateByTimestamp(new Timestamp(cmd.getEndQueryTime())), cmd.getPageSize(), cmd.getPageAnchor());

        Long nextPageAnchor = null;
        if(dailies.size() == cmd.getPageSize() + 1){
            nextPageAnchor = dailies.get(dailies.size() - 1).getId();
            dailies.remove(dailies.size() - 1);
        }
        GetCustomerStatisticDailyResponse response = new GetCustomerStatisticDailyResponse();
        response.setDtos(dailies.stream().map(r->ConvertHelper.convert(r, CustomerStatisticsDTO.class)).collect(Collectors.toList()));
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    public List<CommunityCustomerStatisticDTO> getCustomerStatisticsDailyTotal(GetCustomerStatisticsDailyCommand cmd) {
        ListCommunitiesCommand cmd2 = new ListCommunitiesCommand();
        cmd2.setNamespaceId(cmd.getNamespaceId());
        cmd2.setOrgId(cmd.getOrgId());
        ListCommunitiesResponse response = communityService.listCommunities(cmd2);
        List<CommunityDTO> communities = response.getList();
        List<Long> communityIds = communities.stream().map(r->r.getId()).collect(Collectors.toList());

        for(CommunityDTO community : communities){
            CommunityCustomerStatisticDTO dto = new CommunityCustomerStatisticDTO();
            List<CustomerStatisticsDTO> dtos = new ArrayList<>();
            List<CustomerStatisticDaily> dailies = invitedCustomerProvider.listCustomerStatisticDaily(cmd.getNamespaceId(), community.getId(), getDateByTimestamp(new Timestamp(cmd.getStartQueryTime())), getDateByTimestamp(new Timestamp(cmd.getEndQueryTime())));
            dto.setCommunityId(community.getId());
            dailies.forEach(r->dtos.add(ConvertHelper.convert(r, CustomerStatisticsDTO.class)));
            dto.setDtos(dtos);
            result.add(dto);
        }
        return result;
    }

        @Override
    public List<CommunityCustomerStatisticDTO> getCustomerStatisticsMonthly(GetCustomerStatisticsMonthlyCommand cmd) {
        List<CommunityCustomerStatisticDTO> result = new ArrayList<>();
        if(cmd.getCommunities() != null && cmd.getCommunities().size() > 0) {
            for(Long communityId : cmd.getCommunities()){
                CommunityCustomerStatisticDTO dto = new CommunityCustomerStatisticDTO();
                List<CustomerStatisticsDTO> dtos = new ArrayList<>();
                List<CustomerStatisticMonthly> monthlies = invitedCustomerProvider.listCustomerStatisticMonthly(cmd.getNamespaceId(), communityId, getDateByTimestamp(new Timestamp(cmd.getStartQueryTime())), getDateByTimestamp(new Timestamp(cmd.getEndQueryTime())));
                dto.setCommunityId(communityId);
                monthlies.forEach(r->dtos.add(ConvertHelper.convert(r, CustomerStatisticsDTO.class)));
                dto.setDtos(dtos);
                result.add(dto);
            }
        }else if(cmd.getOrgId() != null){
            ListCommunitiesCommand cmd2 = new ListCommunitiesCommand();
            cmd2.setNamespaceId(cmd.getNamespaceId());
            cmd2.setOrgId(cmd.getOrgId());
            ListCommunitiesResponse response = communityService.listCommunities(cmd2);
            List<CommunityDTO> communities = response.getList();
            for(CommunityDTO community : communities){
                CommunityCustomerStatisticDTO dto = new CommunityCustomerStatisticDTO();
                List<CustomerStatisticsDTO> dtos = new ArrayList<>();
                List<CustomerStatisticMonthly> monthlies = invitedCustomerProvider.listCustomerStatisticMonthly(cmd.getNamespaceId(), community.getId(), getDateByTimestamp(new Timestamp(cmd.getStartQueryTime())), getDateByTimestamp(new Timestamp(cmd.getEndQueryTime())));
                dto.setCommunityId(community.getId());
                monthlies.forEach(r->dtos.add(ConvertHelper.convert(r, CustomerStatisticsDTO.class)));
                dto.setDtos(dtos);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<CustomerStatisticsDTO> getCustomerStatisticsNow(GetCustomerStatisticsNowCommand cmd) {
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
    public List<StatisticDataDTO> startCustomerStatistic(StatisticTime time){
        List<Community>  allCommunities = communityProvider.listAllBizCommunities();
        Timestamp statisticStartTime = time.getStatisticStartTime();
        Timestamp statisticEndTime = time.getStatisticEndTime();


        List<StatisticDataDTO> result = new ArrayList<>();

        for(Community community : allCommunities){
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}", community.getId(), statisticStartTime, statisticEndTime);

            List<CustomerLevelChangeRecord> listRecord = invitedCustomerProvider.listCustomerLevelChangeRecord(null, community.getId(), statisticStartTime, statisticEndTime);
            Integer addCustomerNum = invitedCustomerProvider.countCustomerNumByCreateDate(community.getId(), statisticStartTime, statisticEndTime);
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, add customer num : {}", community.getId(), statisticStartTime, statisticEndTime, addCustomerNum);

            List<CustomerLevelChangeRecord> listRegisteredCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.REGISTERED_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to registered num : {}", community.getId(), statisticStartTime, statisticEndTime, listRegisteredCustomer.size());


            List<CustomerLevelChangeRecord> listLossCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.LOSS_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to loss num : {}", community.getId(), statisticStartTime, statisticEndTime, listLossCustomer.size());

            List<CustomerLevelChangeRecord> listHistoryCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.HISTORY_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to history num : {}", community.getId(), statisticStartTime, statisticEndTime, listHistoryCustomer.size());


            StatisticDataDTO data = new StatisticDataDTO();
            data.setCommunityId(community.getId());
            data.setNamespaceId(community.getNamespaceId());
            data.setNewCustomerNum(invitedCustomerProvider.countCustomerNumByCreateDate(community.getId(), statisticStartTime, statisticEndTime));
            data.setTrackingNum(invitedCustomerProvider.countTrackingNumByCreateDate(community.getId(), statisticStartTime, statisticEndTime));
            data.setLossCustomerNum(listLossCustomer.size());
            data.setHistoryCustomerNum(listHistoryCustomer.size());
            data.setRegisteredCustomerNum(listRegisteredCustomer.size());
            result.add(data);


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
        return null;

    }


    @Override
    public void statisticCustomerDaily(Date date){
        LOGGER.info("the scheduleJob of customer daily statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(date, Calendar. DAY_OF_MONTH);
        List<StatisticDataDTO> datas = startCustomerStatistic(statisticTime);

        invitedCustomerProvider.deleteCustomerstatisticDaily(null, null, getDateByTimestamp(statisticTime.getStatisticStartTime()));
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
    public void statisticCustomerMonthly(Date date){
        LOGGER.info("the scheduleJob of customer monthly statistics is start!");
        StatisticTime statisticTime = getBeforeForStatistic(new Date(), Calendar. MONTH);
        List<StatisticDataDTO> datas = startCustomerStatistic(statisticTime);
        if(datas != null && datas.size() > 0) {
            for (StatisticDataDTO data : datas) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(statisticTime.getStatisticStartTime());
                try {
                    data.setDateStr(new java.sql.Date(format.parse(str).getTime()));
                    CustomerStatisticMonthly monthly = ConvertHelper.convert(data, CustomerStatisticMonthly.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("the scheduleJob of customer monthly statistics is end!, result = {}" , datas);

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

        switch (type) {
            case STATISTIC_DAILY:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date date = format.parse(cmd.getDate());
                    statisticCustomerDaily(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case STATISTIC_MONTHLY:
                break;

            case STATISTIC_ALL:
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
}


