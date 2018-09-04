package com.everhomes.investment;

import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.CreateInvestmentCommand;
import com.everhomes.rest.investment.EnterpriseInvestmentDTO;
import com.everhomes.rest.investment.SearchInvestmentResponse;
import com.everhomes.rest.investment.ViewInvestmentDetailCommand;

public interface InvestmentEnterpriseService {


    void createInvestment(CreateInvestmentCommand cmd);

    void updateInvestment(CreateInvestmentCommand cmd);

    void deleteInvestment(CreateInvestmentCommand cmd);

    SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd);

    EnterpriseInvestmentDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd);
}
