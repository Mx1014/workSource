package com.everhomes.customer;

import com.everhomes.acl.AuthorizationRelation;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.forum.AttachmentDescriptor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface EnterpriseCustomerProvider {
    void createEnterpriseCustomer(EnterpriseCustomer customer);
    void updateEnterpriseCustomer(EnterpriseCustomer customer);
    void deleteEnterpriseCustomer(EnterpriseCustomer customer);
    EnterpriseCustomer findById(Long id);
    EnterpriseCustomer findByOrganizationId(Long organizationId);
    EnterpriseCustomer findByNamespaceToken(String namespaceType, String namespaceCustomerToken);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, String name);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndNumber(Integer namespaceId, String number);
    List<EnterpriseCustomer> listEnterpriseCustomers(CrossShardListingLocator locator, Integer pageSize);
    Map<Long, EnterpriseCustomer> listEnterpriseCustomersByIds(List<Long> ids);
    Map<Long, Long> listEnterpriseCustomerSourceByCommunityId(Long communityId);
    Map<Long, Long> listEnterpriseCustomerIndustryByCommunityId(Long communityId);
    List<EnterpriseCustomer> listEnterpriseCustomers(Set<Long> customerIds);

    void createCustomerAccount(CustomerAccount account);
    void updateCustomerAccount(CustomerAccount account);
    void deleteCustomerAccount(CustomerAccount account);
    CustomerAccount findCustomerAccountById(Long id);
    List<CustomerAccount> listCustomerAccountsByCustomerId(Long customerId);

    void createCustomerTax(CustomerTax tax);
    void updateCustomerTax(CustomerTax tax);
    void deleteCustomerTax(CustomerTax tax);
    CustomerTax findCustomerTaxById(Long id);
    List<CustomerTax> listCustomerTaxesByCustomerId(Long customerId);

    void createCustomerTalent(CustomerTalent talent);
    void updateCustomerTalent(CustomerTalent talent);
    void deleteCustomerTalent(CustomerTalent talent);
    CustomerTalent findCustomerTalentById(Long id);
    CustomerTalent findCustomerTalentByPhone(String phone, Long customerId);
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
    Long countCertificatesByCustomerIds(List<Long> customerIds);

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

    void createCustomerEntryInfo(CustomerEntryInfo entryInfo);
    void updateCustomerEntryInfo(CustomerEntryInfo entryInfo);
    void deleteCustomerEntryInfo(CustomerEntryInfo entryInfo);
    CustomerEntryInfo findCustomerEntryInfoById(Long id);
    CustomerEntryInfo findCustomerEntryInfoByAddressId(Long customerId, Byte customerType, Long addressId);
    List<CustomerEntryInfo> listCustomerEntryInfos(Long customerId);
    List<CustomerEntryInfo> listAddressEntryInfos(Long addressId);

    void createCustomerDepartureInfo(CustomerDepartureInfo departureInfo);
    void updateCustomerDepartureInfo(CustomerDepartureInfo departureInfo);
    void deleteCustomerDepartureInfo(CustomerDepartureInfo departureInfo);
    CustomerDepartureInfo findCustomerDepartureInfoById(Long id);
    List<CustomerDepartureInfo> listCustomerDepartureInfos(Long customerId);

    void createCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    void updateCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    void deleteCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator);
    CustomerEconomicIndicator findCustomerEconomicIndicatorById(Long id);
    List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerId(Long customerId);
    List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerId(Long customerId, Timestamp startTime, Timestamp endTime);
    List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerIds(List<Long> customerIds);
    List<CustomerAnnualStatisticDTO> listCustomerAnnualStatistics(Long communityId, Timestamp now, CrossShardListingLocator locator, Integer pageSize,
        BigDecimal turnoverMinimum, BigDecimal turnoverMaximum, BigDecimal taxPaymentMinimum, BigDecimal taxPaymentMaximum);

    void createCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic);
    void updateCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic);
    void deleteCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic);
    CustomerEconomicIndicatorStatistic listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(Long customerId, Timestamp time);

    List<EnterpriseCustomer> listEnterpriseCustomerByCommunity(Long communityId);
    
	void createCustomerTracking(CustomerTracking tracking);
	CustomerTracking findCustomerTrackingById(Long id);
	void deleteCustomerTracking(CustomerTracking tracking);
	void updateCustomerTracking(CustomerTracking tracking);
	List<CustomerTracking> listCustomerTrackingsByCustomerId(Long customerId);
	
	void createCustomerTrackingPlan(CustomerTrackingPlan plan);
	CustomerTrackingPlan findCustomerTrackingPlanById(Long id);
	void deleteCustomerTrackingPlan(CustomerTrackingPlan plan);
	void updateCustomerTrackingPlan(CustomerTrackingPlan plan);
	List<CustomerTrackingPlan> listCustomerTrackingPlans(Long customerId);
	
	void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist,Byte deviceType);
	List<CustomerEvent> listCustomerEvents(Long customerId);
	
	void allotEnterpriseCustomer(EnterpriseCustomer customer);
	void giveUpEnterpriseCustomer(EnterpriseCustomer customer);
	
	List<EnterpriseCustomerDTO> findEnterpriseCustomersByDistance(ListNearbyEnterpriseCustomersCommand cmd , ListingLocator locator , int pageSize);
	boolean updateTrackingPlanNotify(Long recordId);
	List<CustomerTrackingPlan> listWaitNotifyTrackingPlans(Timestamp queryStartTime, Timestamp queryEndTime);
	void createTrackingNotifyLog(TrackingNotifyLog log);
	
	void updateTrackingPlanReadStatus(Long id);
	
	List<CustomerTrackingPlan> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd ,Timestamp timestamp);
	List<CustomerTrackingPlan> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd, Long todayFirst);
	
	void updateCustomerLastTrackingTime(EnterpriseCustomer customer);

    String findLastEnterpriseCustomerVersionByCommunity(Integer namespaceId, Long communityId);

    List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId, Long appId, Long communityId);

    void updateEnterpriseBannerUri(Long id, List<AttachmentDescriptor> banner);

    void createEnterpriseCustomerAdminRecord(Long customerId, String contactName,String contactType, String contactToken,Integer namespaceId);

    void deleteEnterpriseCustomerAdminRecord(Long customerId, String contactToken);

    void updateEnterpriseCustomerAdminRecord(String contacToken,Integer namespaceId);

    List<CustomerAdminRecord> listEnterpriseCustomerAdminRecords(Long customerId,String contactType);

    List<EnterpriseAttachment> listEnterpriseCustomerPostUri(Long id);

    List<Organization> listNoSyncOrganizations(Integer namespaceId);

    void deleteAllCustomerEntryInfo(Long customerId);

    void deleteAllEnterpriseCustomerAdminRecord(Long id);

    void deleteCustomerEntryInfoByCustomerIdAndAddressId(Long id, Long addressId);

    void deleteCustomerEntryInfoByCustomerIdAndAddressIds(Long id, List<Long> addressIds);

    void updateEnterpriseCustomerAdminRecordByCustomerId(Long customerId,Integer namespaceId);

    String getEnterpriseCustomerNameById(Long enterpriseCustomerId);

    List<EasySearchEnterpriseCustomersDTO> listEnterpriseCustomerNameAndId(List<Long> ids);

    List<EasySearchEnterpriseCustomersDTO> listCommunityEnterpriseCustomers(Long communityId, Integer namespaceId);

    void deleteCustomerEntryInfoByBuildingId(Long id);

    List<CustomerAdminRecord> listEnterpriseCustomerAdminRecordsByToken(Long id, String adminToken);
}