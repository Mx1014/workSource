package com.everhomes.investment;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.investment.InvitedCustomerStatisticsDTO;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.server.schema.tables.pojos.EhCustomerStatisticsTotal;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InvitedCustomerProvider {

    //联系人provider

    Long createContact(CustomerContact contact);

    Long updateContact(CustomerContact contact);

    CustomerContact findContactById(Long id);

    List<CustomerContact> findContactByCustomerId(Long customerId);

    List<CustomerContact> findContactByCustomerIdAndType(Long customerId, Byte type);

    //跟进人provider

    Long createTracker(CustomerTracker contact);

    Long updateTracker(CustomerTracker contact);

    CustomerTracker findTrackerById(Long id);

    List<CustomerTracker> findTrackerByCustomerId(Long customerId);

    //客户需求provider
    Long createRequirement(CustomerRequirement demand);

    Long updateRequirement(CustomerRequirement demand);

    CustomerRequirement findRequirementById(Long id);

    CustomerRequirement findNewestRequirementByCustoemrId(Long customerId);

    //客户当前信息provider
    Long createCurrentRent(CustomerCurrentRent nowInfo);

    Long updateCurrentRent(CustomerCurrentRent nowInfo);

    CustomerCurrentRent findCurrentRentById(Long id);

    CustomerCurrentRent findNewestCurrentRentByCustomerId(Long customerId);

    List<InvitedCustomerStatisticsDTO> getInvitedCustomerStatistics(Boolean isAdmin,String keyWord,BigDecimal startAreaSize, BigDecimal endAreaSize, Set<Long> itemIds, Map<Long, FieldItemDTO> itemsMap, ListingQueryBuilderCallback callback);


    void deleteInvitedCustomer(Long id);

    void deleteCustomerContacts(Long id);
    void deleteCustomerContactsWithType(Long id, Byte type);

    Long createRequirementAddress(CustomerRequirementAddress address);

    Long updateCurrentRent(CustomerRequirementAddress address);

    CustomerRequirementAddress findRequirementAddressById(Long id);

    List<CustomerRequirementAddress> findRequirementAddressByRequirementId(Long requirementId);

    List<EnterpriseCustomer> listCustomersByType(byte code, ListingLocator locator, int pageSize);

    void deleteCustomerTrackersByCustomerId(Long customerId);

    void updateToEnterpriseCustomerByCustomerId(Long customerId, Long phoneNumber, String contactName);

    List<EnterpriseCustomer> getInitCustomerStatus(Integer pageSize, Long nextAnchor);

    void createCustomerLevelChangeRecord(CustomerLevelChangeRecord record);

    List<CustomerLevelChangeRecord> listCustomerLevelChangeRecord(Integer namespaceId, Long communityId, Timestamp queryStartDate, Timestamp queryEndDate);

    Integer countCustomerLevelLossChangeRecord(Integer namespaceId, Long communityId, Timestamp queryStartDate, Timestamp queryEndDate, Long levelItemId);

    Integer countCustomerNumByCreateDate(Long communityId, Timestamp queryStartDate, Timestamp queryEndDate);

    Integer countTrackingNumByTrackingDate(Long communityId, Timestamp queryStartDate, Timestamp queryEndDate);

    void createCustomerStatisticsDaily(CustomerStatisticDaily daily);

    void createCustomerStatisticsDailyTotal(CustomerStatisticDailyTotal dailyTotal);

    CustomerStatisticDaily getCustomerStatisticsDaily(Integer namespaceId, Long communityId, Date date);

    List<CustomerStatisticDaily> listCustomerStatisticDaily(Integer namespaceId, List<Long> communityIds, Date startDate, Date endDate, Integer pageSize, Integer offset);

    void createCustomerStatisticsMonthly(CustomerStatisticMonthly monthly);

    void createCustomerStatisticsMonthlyTotal(CustomerStatisticMonthlyTotal monthlyTotal);

    CustomerStatisticMonthly getCustomerStatisticsMonthly(Integer namespaceId, Long communityId, Date date);

    List<CustomerStatisticMonthly> listCustomerStatisticMonthly(Integer namespaceId, List<Long> communityIds, Date startDate, Date endDate, Integer pageSize, Integer offset);

    List<CustomerStatisticDailyTotal> listCustomerStatisticDailyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate, Integer pageSize, Integer offset);

    List<CustomerStatisticMonthlyTotal> listCustomerStatisticMonthlyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate, Integer pageSize, Integer offset);

    void createCustomerStatisticTotal(CustomerStatisticTotal total);

    CustomerStatisticTotal getCustomerStatisticsTotal(Integer namespaceId, Long organizationId, Date date);

    void deleteCustomerStatisticDaily(Integer namespaceId, Long communityId, Date startDate);

    void deleteCustomerStatisticDailyTotal(Integer namespaceId, Long organizationId, Date startDate);

    void deleteCustomerStatisticDaily(Integer namespaceId, Long communityId, Date startDate, Date endDate);

    void deleteCustomerStatisticDailyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate);

    void deleteCustomerStatisticMonthly(Integer namespaceId, Long communityId, Date startDate);

    void deleteCustomerStatisticMonthlyTotal(Integer namespaceId, Long organizationId, Date startDate);

    void deleteCustomerStatisticMonthly(Integer namespaceId, Long communityId, Date startDate, Date endDate);

    void deleteCustomerStatisticMonthlyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate);

    void deleteCustomerStatisticTotal(Integer namespaceId, Long organizationId, Date startDate);


    CustomerStatisticTotal getCustomerStatisticTotal(Integer namespaceId, Long communityId, Date date);


}
