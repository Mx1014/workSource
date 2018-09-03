package com.everhomes.investment;

import java.util.List;

public interface InvestmentEnterpriseProvider {

    Long createContact(EnterpriseInvestmentContact contact);

    Long updateContact(EnterpriseInvestmentContact contact);

    EnterpriseInvestmentContact findContactById(Long id);

    List<EnterpriseInvestmentContact> findContactByCustomerId(Long customerId);

    List<EnterpriseInvestmentContact> findContactByCustomerIdAndType(Long customerId, Byte type);



}
