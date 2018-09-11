package com.everhomes.investment;

import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.CreateInvitedCustomerCommand;
import com.everhomes.rest.investment.InvitedCustomerDTO;
import com.everhomes.rest.investment.SearchInvestmentResponse;
import com.everhomes.rest.investment.ViewInvestmentDetailCommand;

public interface InvitedCustomerService {


    InvitedCustomerDTO createInvitedCustomer(CreateInvitedCustomerCommand cmd);

    void updateInvestment(CreateInvitedCustomerCommand cmd);

    void deleteInvestment(CreateInvitedCustomerCommand cmd);

    SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd);

    InvitedCustomerDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd);

    void syncTrackerData();
}
