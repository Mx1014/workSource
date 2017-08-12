package com.everhomes.customer;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface EnterpriseCustomerProvider {
    void createEnterpriseCustomer(EnterpriseCustomer customer);
    void updateEnterpriseCustomer(EnterpriseCustomer customer);
    void deleteEnterpriseCustomer(EnterpriseCustomer customer);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, String name);
}
