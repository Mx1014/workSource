package com.everhomes.customer;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface EnterpriseCustomerProvider {
    void createEnterpriseCustomer(EnterpriseCustomer customer);
    void updateEnterpriseCustomer(EnterpriseCustomer customer);
    void deleteEnterpriseCustomer(EnterpriseCustomer customer);
    EnterpriseCustomer findById(Long id);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, String name);
    List<EnterpriseCustomer> listEnterpriseCustomers(CrossShardListingLocator locator, Integer pageSize);
    List<EnterpriseCustomer> listEnterpriseCustomersByIds(List<Long> ids);
}
