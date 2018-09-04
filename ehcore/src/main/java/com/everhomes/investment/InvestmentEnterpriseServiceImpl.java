package com.everhomes.investment;


import com.everhomes.customer.CustomerService;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.rest.investment.CreateInvestmentCommand;
import com.everhomes.rest.investment.InvestmentStatisticsDTO;
import com.everhomes.rest.investment.SearchInvestmentResponse;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldItemCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestmentEnterpriseServiceImpl implements InvestmentEnterpriseService {

    @Autowired
    private EnterpriseCustomerSearcher customerSearcher;

    @Autowired
    private CustomerService customerService;

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
                EnterpriseInvestmentContact contact = ConvertHelper.convert(c, EnterpriseInvestmentContact.class);
                contact.setCustomerId(cmd.getId());
                contact.setCommunityId(cmd.getCommunityId());
                contact.setNamespaceId(cmd.getNamespaceId());
                contact.setStatus(CommonStatus.ACTIVE.getCode());
                investmentEnterpriseProvider.createContact(contact);
            });
        }
        // reflush demend
        if (cmd.getDemand() != null) {
            EnterpriseInvestmentDemand investmentDemand = ConvertHelper.convert(cmd.getDemand(), EnterpriseInvestmentDemand.class);
            investmentDemand.setCommunityId(cmd.getCommunityId());
            investmentDemand.setNamespaceId(cmd.getNamespaceId());
            investmentDemand.setCustomerId(cmd.getId());
            investmentDemand.setStatus(CommonStatus.ACTIVE.getCode());
            investmentEnterpriseProvider.createDemand(investmentDemand);
        }
        // reflush current basic info
        if(cmd.getNowInfos()!=null && cmd.getNowInfos().size()>0){
            cmd.getNowInfos().forEach((c)->{
                EnterpriseInvestmentNowInfo nowInfo = ConvertHelper.convert(c, EnterpriseInvestmentNowInfo.class);
                nowInfo.setCommunityId(cmd.getCommunityId());
                nowInfo.setNamespaceId(cmd.getNamespaceId());
                nowInfo.setStatus(CommonStatus.ACTIVE.getCode());
                nowInfo.setCustomerId(cmd.getId());
                investmentEnterpriseProvider.createNowInfo(nowInfo);
            });
        }
        // todo: update main record data

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
}


