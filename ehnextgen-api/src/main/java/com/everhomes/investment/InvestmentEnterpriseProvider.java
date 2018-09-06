package com.everhomes.investment;

import com.everhomes.rest.investment.InvestmentStatisticsDTO;
import com.everhomes.rest.varField.FieldItemDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InvestmentEnterpriseProvider {

    //联系人provider

    Long createContact(EnterpriseInvestmentContact contact);

    Long updateContact(EnterpriseInvestmentContact contact);

    EnterpriseInvestmentContact findContactById(Long id);

    List<EnterpriseInvestmentContact> findContactByCustomerId(Long customerId);

    List<EnterpriseInvestmentContact> findContactByCustomerIdAndType(Long customerId, Byte type);

    //跟进人provider

    Long createTracker(EnterpriseInvestmentTracker contact);

    Long updateTracker(EnterpriseInvestmentTracker contact);

    EnterpriseInvestmentTracker findTrackerById(Long id);

    List<EnterpriseInvestmentTracker> findTrackerByCustomerId(Long customerId);

    List<EnterpriseInvestmentTracker> findTrackerByCustomerIdAndType(Long customerId, Byte type);

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

    List<InvestmentStatisticsDTO> getInvestmentStatistics(Integer namespaceId, Long communityId, Set<Long> itemIds,Map<Long, FieldItemDTO> itemsMap);

    void deleteInvestment(Long id);

    void deleteInvestmentContacts(Long id);
}
