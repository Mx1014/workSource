package com.everhomes.investment;

import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.CreateInvestmentCommand;
import com.everhomes.rest.investment.SearchInvestmentResponse;

public interface InvestmentEnterpriseService {


    void createInvestment(CreateInvestmentCommand cmd);

    void updateInvestment(CreateInvestmentCommand cmd);

    void deleteInvestment(CreateInvestmentCommand cmd);

    SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd);
}
