package com.everhomes.investment;


import com.everhomes.customer.CustomerService;
import com.everhomes.customer.CustomerTracking;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.rest.investment.CreateInvestmentCommand;
import com.everhomes.rest.investment.EnterpriseInvestmentDTO;
import com.everhomes.rest.investment.InvestmentEnterpriseType;
import com.everhomes.rest.investment.InvestmentStatisticsDTO;
import com.everhomes.rest.investment.SearchInvestmentResponse;
import com.everhomes.rest.investment.ViewInvestmentDetailCommand;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvestmentEnterpriseServiceImpl implements InvestmentEnterpriseService {

    @Autowired
    private EnterpriseCustomerSearcher customerSearcher;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private InvestmentEnterpriseProvider investmentEnterpriseProvider;

    private FieldService fieldService;

    @Override
    public void createInvestment(CreateInvestmentCommand cmd) {

    }

    @Override
    public void updateInvestment(CreateInvestmentCommand cmd) {
        // reflush contacts
        investmentEnterpriseProvider.deleteInvestmentContacts(cmd.getId());
        if (cmd.getContacts() != null && cmd.getContacts().size() > 0) {
            cmd.getContacts().forEach((c) -> {
                CustomerContact contact = ConvertHelper.convert(c, CustomerContact.class);
                contact.setCustomerId(cmd.getId());
                contact.setCommunityId(cmd.getCommunityId());
                contact.setNamespaceId(cmd.getNamespaceId());
                contact.setStatus(CommonStatus.ACTIVE.getCode());
                investmentEnterpriseProvider.createContact(contact);
            });
        }
        // reflush demend
        if (cmd.getDemand() != null) {
            CustomerRequirement requirement = ConvertHelper.convert(cmd.getDemand(), CustomerRequirement.class);
            requirement.setCommunityId(cmd.getCommunityId());
            requirement.setNamespaceId(cmd.getNamespaceId());
            requirement.setCustomerId(cmd.getId());
            requirement.setStatus(CommonStatus.ACTIVE.getCode());
            investmentEnterpriseProvider.createRequirement(requirement);
        }
        // reflush current basic info
        if(cmd.getNowInfo()!=null){
            CustomerCurrentRent currentRent = ConvertHelper.convert(cmd.getNowInfo(), CustomerCurrentRent.class);
            currentRent.setCommunityId(cmd.getCommunityId());
            currentRent.setNamespaceId(cmd.getNamespaceId());
            currentRent.setStatus(CommonStatus.ACTIVE.getCode());
            currentRent.setCustomerId(cmd.getId());
            investmentEnterpriseProvider.createCurrentRent(currentRent);
        }
        if (cmd.getTrackingInfos() != null) {
            cmd.getTrackingInfos().forEach((c) -> {
                CustomerTracking customerTracking = ConvertHelper.convert(c, CustomerTracking.class);
                customerTracking.setStatus(CommonStatus.ACTIVE.getCode());
                customerTracking.setCustomerType(CustomerType.ENTERPRISE.getCode());
                customerTracking.setCustomerSource(InvestmentEnterpriseType.INVESTMENT_ENTERPRISE.getCode());
            });
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

    private void syncInvestmentInfoToOrganization(CreateInvestmentCommand cmd) {

    }

    @Override
    public void deleteInvestment(CreateInvestmentCommand cmd) {
        customerSearcher.deleteById(cmd.getId());

        investmentEnterpriseProvider.deleteInvestment(cmd.getId());
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
            List<InvestmentStatisticsDTO> statistics = null;
            // this module should add investment statistics to response
            ListFieldItemCommand fieldItemCommand = new ListFieldItemCommand();
            fieldItemCommand.setNamespaceId(cmd.getNamespaceId());
            fieldItemCommand.setCommunityId(cmd.getCommunityId());
            // this field id menus investment enterprise levelItemId private key
            fieldItemCommand.setFieldId(5L);
            List<FieldItemDTO> items = fieldService.listFieldItems(fieldItemCommand);
            Map<Long, FieldItemDTO> itemsMap = transferCurrentCommunityItemsMap(items);
            if (itemsMap != null && itemsMap.size() > 0) {
                statistics = investmentEnterpriseProvider.getInvestmentStatistics(cmd.getNamespaceId(), cmd.getCommunityId(), itemsMap.keySet(),itemsMap);
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

    public EnterpriseInvestmentDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd){
        GetEnterpriseCustomerCommand cmd2 = ConvertHelper.convert(cmd, GetEnterpriseCustomerCommand.class);
        EnterpriseCustomerDTO customerDTO = customerService.getEnterpriseCustomer(cmd2);
        if(customerDTO != null) {
            EnterpriseInvestmentDTO investmentDTO = ConvertHelper.convert(customerDTO, EnterpriseInvestmentDTO.class);
            return investmentDTO;
        }

        return null;
    }
}


