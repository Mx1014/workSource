package com.everhomes.investment;


import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.forum.ForumCategoryDTO;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InvitedCustomerServiceImpl implements InvitedCustomerService {

    @Autowired
    private EnterpriseCustomerSearcher customerSearcher;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private InvitedCustomerProvider invitedCustomerProvider;

    private FieldService fieldService;

    @Override
    public InvitedCustomerDTO createInvitedCustomer(CreateInvitedCustomerCommand cmd) {
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
            // reflush demend
            if (cmd.getRequirement() != null) {
                CustomerRequirement requirement = ConvertHelper.convert(cmd.getRequirement(), CustomerRequirement.class);
                requirement.setCommunityId(cmd.getCommunityId());
                requirement.setNamespaceId(cmd.getNamespaceId());
                requirement.setCustomerId(cmd.getId());
                requirement.setStatus(CommonStatus.ACTIVE.getCode());
                invitedCustomerProvider.createRequirement(requirement);
            }
            // reflush current basic info
            if (cmd.getCurrentRent() != null) {
                CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getCurrentRent(), CustomerCurrentRent.class);
                currentRent.setCommunityId(cmd.getCommunityId());
                currentRent.setNamespaceId(cmd.getNamespaceId());
                currentRent.setStatus(CommonStatus.ACTIVE.getCode());
                currentRent.setCustomerId(cmd.getId());
                invitedCustomerProvider.createCurrentRent(currentRent);
            }
            if(cmd.getTrackers() != null){
                CustomerTracker tracker = ConvertHelper.convert(cmd.getTrackers(), CustomerTracker.class);
                tracker.setCommunityId(cmd.getCommunityId());
                tracker.setNamespaceId(cmd.getNamespaceId());
                tracker.setStatus(CommonStatus.ACTIVE.getCode());
                tracker.setCustomerId(cmd.getId());
                invitedCustomerProvider.createTracker(tracker);
            }

            return ConvertHelper.convert(cmd, InvitedCustomerDTO.class);
        }

        return null;
    }

    @Override
    public void updateInvestment(CreateInvitedCustomerCommand cmd) {
        // reflush contacts
        invitedCustomerProvider.deleteInvitedCustomer(cmd.getId());
        if (cmd.getContacts() != null && cmd.getContacts().size() > 0) {
            cmd.getContacts().forEach((c) -> {
                CustomerContact contact = ConvertHelper.convert(c, CustomerContact.class);
                contact.setCustomerId(cmd.getId());
                contact.setCommunityId(cmd.getCommunityId());
                contact.setNamespaceId(cmd.getNamespaceId());
                contact.setStatus(CommonStatus.ACTIVE.getCode());
                invitedCustomerProvider.createContact(contact);
            });
        }
        // reflush demend
        if (cmd.getRequirement() != null) {
            CustomerRequirement requirement = ConvertHelper.convert(cmd.getRequirement(), CustomerRequirement.class);
            requirement.setCommunityId(cmd.getCommunityId());
            requirement.setNamespaceId(cmd.getNamespaceId());
            requirement.setCustomerId(cmd.getId());
            requirement.setStatus(CommonStatus.ACTIVE.getCode());
            invitedCustomerProvider.createRequirement(requirement);
        }
        // reflush current basic info
        if(cmd.getCurrentRent()!=null){
            CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getCurrentRent(), CustomerCurrentRent.class);
            currentRent.setCommunityId(cmd.getCommunityId());
            currentRent.setNamespaceId(cmd.getNamespaceId());
            currentRent.setStatus(CommonStatus.ACTIVE.getCode());
            currentRent.setCustomerId(cmd.getId());
            invitedCustomerProvider.createCurrentRent(currentRent);
        }
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        // todo: update main record data
        customer.setStatus(CommonStatus.ACTIVE.getCode());
        customerProvider.updateEnterpriseCustomer(customer);
//        //sync tenant info into organization
//        ExecutorUtil.submit(() -> {
//            syncInvestmentInfoToOrganization(cmd);
//        });
        customerSearcher.feedDoc(customer);
    }

    private void syncInvestmentInfoToOrganization(CreateInvitedCustomerCommand cmd) {

    }

    @Override
    public void deleteInvestment(CreateInvitedCustomerCommand cmd) {
        customerSearcher.deleteById(cmd.getId());

        invitedCustomerProvider.deleteInvitedCustomer(cmd.getId());
    }

    @Override
    public SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        Boolean isAdmin = customerService.checkCustomerAdmin(cmd.getOrgId(), cmd.getOwnerType(), cmd.getNamespaceId());
        SearchEnterpriseCustomerResponse searchResponse = customerSearcher.queryEnterpriseCustomers(cmd, isAdmin);
        SearchInvestmentResponse response = new SearchInvestmentResponse();
        response.setDtos(searchResponse.getDtos());
        response.setNextPageAnchor(searchResponse.getNextPageAnchor());
        // only the first time we requested, populate stastics data
        if (cmd.getPageAnchor() == 0 || cmd.getPageAnchor() == null) {
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
                statistics = invitedCustomerProvider.getInvitedCustomerStatistics(cmd.getNamespaceId(), cmd.getCommunityId(), itemsMap.keySet(),itemsMap);
            }
            response.setStastics(statistics);
        }
        return response;
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
        GetEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, GetEnterpriseCustomerCommand.class);
        EnterpriseCustomerDTO customerDTO = customerService.getEnterpriseCustomer(cmd2);
        if(customerDTO != null) {
            InvitedCustomerDTO invitedCustomerDTO = ConvertHelper.convert(customerDTO, InvitedCustomerDTO.class);

            List<CustomerContact> contacts = invitedCustomerProvider.findContactByCustomerId(invitedCustomerDTO.getId());
            if(contacts != null && contacts.size() != 0){
                List<CustomerContactDTO> dtos = contacts.stream().map(r -> ConvertHelper.convert(r, CustomerContactDTO.class)).collect(Collectors.toList());
                invitedCustomerDTO.setContacts(dtos);
            }
            List<CustomerTracker> trackers = invitedCustomerProvider.findTrackerByCustomerId(invitedCustomerDTO.getId());
            if(trackers != null && trackers.size() != 0){
                List<CustomerTrackerDTO> dtos = trackers.stream().map(r -> ConvertHelper.convert(r, CustomerTrackerDTO.class)).collect(Collectors.toList());
                invitedCustomerDTO.setTrackers(dtos);
            }
            CustomerRequirement requirement = invitedCustomerProvider.findNewestRequirementByCustoemrId(invitedCustomerDTO.getId());




            return invitedCustomerDTO;
        }

        return null;
    }
}


