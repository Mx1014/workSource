package com.everhomes.customer;

/**
 * Created by ying.xiong on 2017/11/21.
 */
public interface CustomerHandle {
    String CUSTOMER_PREFIX = "customer-";
    void syncEnterprises(String pageOffset, String version, String communityIdentifier);
    void syncIndividuals(String pageOffset, String version, String communityIdentifier);
}
