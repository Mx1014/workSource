package com.everhomes.investment;


import com.everhomes.address.AddressProvider;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.varField.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.BUSINESS_INVITATION, null, null, null, communityId);
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
                    invitedCustomerProvider.createContact(contact);
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
            if (cmd.getCurrentRent() != null) {
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
            if(cmd.getTrackers() != null){
                cmd.getTrackers().forEach((c) -> {
                    CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                    tracker.setCommunityId(cmd.getCommunityId());
                    tracker.setNamespaceId(cmd.getNamespaceId());
                    tracker.setStatus(CommonStatus.ACTIVE.getCode());
                    tracker.setCustomerId(cmd.getId());
                    tracker.setCustomerSource(cmd.getCustomerSource());
                    invitedCustomerProvider.createTracker(tracker);
                });
            }

            EnterpriseCustomer dto = ConvertHelper.convert(customer, EnterpriseCustomer.class);

            dto.setStatus(CommonStatus.ACTIVE.getCode());
            dto.setNamespaceId(cmd.getNamespaceId());
            customerSearcher.feedDoc(dto);
            return ConvertHelper.convert(cmd, InvitedCustomerDTO.class);
        }

        return null;
    }

    @Override
    public void updateInvestment(CreateInvitedCustomerCommand cmd) {
        checkCustomerAuth(cmd.getNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_UPDATE, cmd.getOrgId(), cmd.getCommunityId());

        if(cmd.getCustomerSource() == null){
            if(cmd.getLevelItemId()== CustomerLevelType.REGISTERED_CUSTOMER.getCode()){
                cmd.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            }else{
                cmd.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
            }
        }

        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
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
                invitedCustomerProvider.createContact(contact);
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
        invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId(), InvitedCustomerType.INVITED_CUSTOMER.getCode());
        if(cmd.getTrackers() != null){
            cmd.getTrackers().forEach((c) -> {
                CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                tracker.setCommunityId(cmd.getCommunityId());
                tracker.setNamespaceId(cmd.getNamespaceId());
                tracker.setStatus(CommonStatus.ACTIVE.getCode());
                tracker.setCustomerId(cmd.getId());
                tracker.setCustomerSource(cmd.getCustomerSource());
                invitedCustomerProvider.createTracker(tracker);
            });
        }
        // todo: update main record data
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
        customerProvider.updateEnterpriseCustomer(customer);
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
        customerService.deleteEnterpriseCustomer(cmd2, true);
        invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId(), InvitedCustomerType.INVITED_CUSTOMER.getCode());
        invitedCustomerProvider.deleteCustomerContacts(cmd.getId());

    }

    @Override
    public SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        Boolean isAdmin = customerService.checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        SearchEnterpriseCustomerResponse searchResponse = customerSearcher.queryEnterpriseCustomers(cmd, isAdmin);
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
            List<FieldItemDTO> items = fieldService.listFieldItems(fieldItemCommand);
            Map<Long, FieldItemDTO> itemsMap = transferCurrentCommunityItemsMap(items);
            if (itemsMap != null && itemsMap.size() > 0) {
                statistics = invitedCustomerProvider.getInvitedCustomerStatistics(cmd.getStartAreaSize(), cmd.getEndAreaSize(), itemsMap.keySet(), itemsMap, (locator, query) -> {
                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(cmd.getCommunityId()));
                    if (cmd.getCorpIndustryItemId() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CORP_INDUSTRY_ITEM_ID.eq(cmd.getCorpIndustryItemId()));
                    }
                    if (cmd.getSourceItemId() != null) {
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.SOURCE_ITEM_ID.eq(cmd.getSourceItemId()));
                    }
                    if(cmd.getCustomerSource()!=null){
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CUSTOMER_SOURCE.eq(cmd.getCustomerSource()));
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
        checkCustomerAuth(UserContext.getCurrentNamespaceId(), PrivilegeConstants.INVITED_CUSTOMER_VIEW, cmd.getOrgId(), cmd.getCommunityId());

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
                    dtos.forEach(r ->{
                        r.setAddressName(addressProvider.findAddressById(r.getAddressId()).getBuildingName() + "/" + addressProvider.findAddressById(r.getAddressId()).getApartmentName());
                    });
                    requirementDTO.setAddresses(dtos);
                }


                invitedCustomerDTO.setRequirement(requirementDTO);

            }

            CustomerCurrentRent currentRent = invitedCustomerProvider.findNewestCurrentRentByCustomerId(invitedCustomerDTO.getId());
            CustomerCurrentRentDTO currentRentDTO = ConvertHelper.convert(currentRent, CustomerCurrentRentDTO.class);
            if(currentRent.getContractIntentionDate() != null)
                currentRentDTO.setContractIntentionDate(currentRent.getContractIntentionDate().getTime());
            invitedCustomerDTO.setCurrentRent(currentRentDTO);






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
                    addressDTO.setAddressName(addressProvider.findAddressById(addressDTO.getAddressId()).getBuildingName() + "/" + addressProvider.findAddressById(addressDTO.getAddressId()).getApartmentName());
                    addressesDTO.add(addressDTO);

                });
                requirementDTO.setAddresses(addressesDTO);
            }
            return requirementDTO;
        }
        return null;
    }



    @Override
    public void giveUpInvitedCustomer(ViewInvestmentDetailCommand cmd) {
        InvitedCustomerDTO invitedCustomerDTO = viewInvestmentDetail(cmd);


        if(invitedCustomerDTO != null) {
            List<CustomerTrackerDTO> trackers = invitedCustomerDTO.getTrackers();

            if(trackers != null && trackers.size() > 0){
                trackers.forEach(r -> {
                    if(r.getTrackerUid().equals(UserContext.current().getUser().getId())) {
                        trackers.remove(r);
                    }
                });
            }

            invitedCustomerProvider.deleteCustomerTrackersByCustomerId(cmd.getId(), InvitedCustomerType.INVITED_CUSTOMER.getCode());
            if(trackers != null && trackers.size() > 0){
                trackers.forEach((c) -> {
                    CustomerTracker tracker = ConvertHelper.convert(c, CustomerTracker.class);
                    tracker.setCommunityId(invitedCustomerDTO.getCommunityId());
                    tracker.setNamespaceId(invitedCustomerDTO.getNamespaceId());
                    tracker.setStatus(CommonStatus.ACTIVE.getCode());
                    tracker.setCustomerId(invitedCustomerDTO.getId());
                    tracker.setCustomerSource(invitedCustomerDTO.getCustomerSource());
                    invitedCustomerProvider.createTracker(tracker);
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
}


