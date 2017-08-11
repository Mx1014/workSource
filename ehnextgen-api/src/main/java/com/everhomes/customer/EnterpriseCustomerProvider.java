package com.everhomes.customer;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface EnterpriseCustomerProvider {
    List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType);
}
