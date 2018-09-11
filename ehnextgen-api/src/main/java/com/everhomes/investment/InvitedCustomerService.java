package com.everhomes.investment;

import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.*;

public interface InvitedCustomerService {


    InvitedCustomerDTO createInvitedCustomer(CreateInvitedCustomerCommand cmd);

    void updateInvestment(CreateInvitedCustomerCommand cmd);

    void deleteInvestment(CreateInvitedCustomerCommand cmd);

    SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd);

    InvitedCustomerDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd);

    CustomerRequirementDTO getCustomerRequirementDTOByCustomerId(Long customerId);

    void syncTrackerData();
}
