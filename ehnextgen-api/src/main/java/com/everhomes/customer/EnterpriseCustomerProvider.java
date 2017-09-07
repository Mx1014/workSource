package com.everhomes.customer;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.customer.CustomerProjectStatisticsDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface EnterpriseCustomerProvider {
    void createEnterpriseCustomer(EnterpriseCustomer customer);
    void updateEnterpriseCustomer(EnterpriseCustomer customer);
    void deleteEnterpriseCustomer(EnterpriseCustomer customer);
    EnterpriseCustomer findById(Long id);
    EnterpriseCustomer findByOrganizationId(Long organizationId);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, String name);
    List<EnterpriseCustomer> listEnterpriseCustomers(CrossShardListingLocator locator, Integer pageSize);
    Map<Long, EnterpriseCustomer> listEnterpriseCustomersByIds(List<Long> ids);
    Map<Long, Long> listEnterpriseCustomerSourceByCommunityId(Long communityId);
    Map<Long, Long> listEnterpriseCustomerIndustryByCommunityId(Long communityId);

    void createCustomerTalent(CustomerTalent talent);
    void updateCustomerTalent(CustomerTalent talent);
    void deleteCustomerTalent(CustomerTalent talent);
    CustomerTalent findCustomerTalentById(Long id);
    List<CustomerTalent> listCustomerTalentsByCustomerId(Long customerId);
    Map<Long, Long> listCustomerTalentCountByCustomerIds(List<Long> customerIds);

    void createCustomerTrademark(CustomerTrademark trademark);
    void updateCustomerTrademark(CustomerTrademark trademark);
    void deleteCustomerTrademark(CustomerTrademark trademark);
    CustomerTrademark findCustomerTrademarkById(Long id);
    List<CustomerTrademark> listCustomerTrademarksByCustomerId(Long customerId);
    Long countTrademarksByCustomerIds(List<Long> customerIds);

    void createCustomerPatent(CustomerPatent patent);
    void updateCustomerPatent(CustomerPatent patent);
    void deleteCustomerPatent(CustomerPatent patent);
    CustomerPatent findCustomerPatentById(Long id);
    List<CustomerPatent> listCustomerPatentsByCustomerId(Long customerId);
    Map<Long, Long> listCustomerPatentsByCustomerIds(List<Long> customerIds);

    void createCustomerCertificate(CustomerCertificate certificate);
    void updateCustomerCertificate(CustomerCertificate certificate);
    void deleteCustomerCertificate(CustomerCertificate certificate);
    CustomerCertificate findCustomerCertificateById(Long id);
    List<CustomerCertificate> listCustomerCertificatesByCustomerId(Long customerId);

    void createCustomerApplyProject(CustomerApplyProject project);
    void updateCustomerApplyProject(CustomerApplyProject project);
    void deleteCustomerApplyProject(CustomerApplyProject project);
    CustomerApplyProject findCustomerApplyProjectById(Long id);
    List<CustomerApplyProject> listCustomerApplyProjectsByCustomerId(Long customerId);
    Map<Long, CustomerProjectStatisticsDTO> listCustomerApplyProjectsByCustomerIds(List<Long> customerIds);

    void createCustomerCommercial(CustomerCommercial commercial);
    void updateCustomerCommercial(CustomerCommercial commercial);
    void deleteCustomerCommercial(CustomerCommercial commercial);
    CustomerCommercial findCustomerCommercialById(Long id);
    List<CustomerCommercial> listCustomerCommercialsByCustomerId(Long customerId);

    void createCustomerInvestment(CustomerInvestment investment);
    void updateCustomerInvestment(CustomerInvestment investment);
    void deleteCustomerInvestment(CustomerInvestment investment);
    CustomerInvestment findCustomerInvestmentById(Long id);
    List<CustomerInvestment> listCustomerInvestmentsByCustomerId(Long customerId);

    void createCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    void updateCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    void deleteCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    CustomerEconomicIndicator findCustomerEconomicIndicatorById(Long id);
    List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerId(Long customerId);
    List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerIds(List<Long> customerIds);

    List<EnterpriseCustomer> listEnterpriseCustomerByCommunity(Long communityId);
}
