package com.everhomes.investment;

import java.util.List;

public interface InvestmentEnterpriseProvider {

    //联系人provider

    Long createContact(EnterpriseInvestmentContact contact);

    Long updateContact(EnterpriseInvestmentContact contact);

    EnterpriseInvestmentContact findContactById(Long id);

    List<EnterpriseInvestmentContact> findContactByCustomerId(Long customerId);

    List<EnterpriseInvestmentContact> findContactByCustomerIdAndType(Long customerId, Byte type);

    //客户需求provider
    Long createDemand(EnterpriseInvestmentDemand demand);

    Long updateDemand(EnterpriseInvestmentDemand demand);

    EnterpriseInvestmentDemand findDemandById(Long id);

    EnterpriseInvestmentDemand findNewestDemandByCustoemrId(Long customerId);

    //客户当前信息provider
    Long createNowInfo(EnterpriseInvestmentNowInfo nowInfo);

    Long updateNowInfo(EnterpriseInvestmentNowInfo nowInfo);

    EnterpriseInvestmentNowInfo findNowInfoById(Long id);

    EnterpriseInvestmentNowInfo findNewestNowInfoByCustoemrId(Long customerId);


}
