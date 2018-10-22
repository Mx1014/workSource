package com.everhomes.investment;


import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.dynamicExcel.DynamicExcelService;
import com.everhomes.dynamicExcel.DynamicExcelStrings;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.*;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.varField.FieldService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InvitedCustomerServiceImpl implements InvitedCustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitedCustomerServiceImpl.class);

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
            isAdmin = customerService.checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
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
        Boolean isAdmin = customerService.checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
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
        Boolean isAdmin = customerService.checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
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

}


