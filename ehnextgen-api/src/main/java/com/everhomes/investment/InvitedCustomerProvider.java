package com.everhomes.investment;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.investment.InvitedCustomerStatisticsDTO;
import com.everhomes.rest.varField.FieldItemDTO;

import java.math.BigDecimal;
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

    void getInitCustomerStatus();
}
