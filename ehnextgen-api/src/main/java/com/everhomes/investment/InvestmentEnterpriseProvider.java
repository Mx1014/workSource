package com.everhomes.investment;

import com.everhomes.rest.investment.InvestmentStasticsDTO;

import java.util.List;

public interface InvestmentEnterpriseProvider {

    Long createContact(EnterpriseInvestmentContact contact);

    Long updateContact(EnterpriseInvestmentContact contact);

    EnterpriseInvestmentContact findContactById(Long id);

    List<EnterpriseInvestmentContact> findContactByCustomerId(Long customerId);

    List<EnterpriseInvestmentContact> findContactByCustomerIdAndType(Long customerId, Byte type);


    List<InvestmentStasticsDTO> getInvestmentStastics(Integer namespaceId, Long communityId);
}
