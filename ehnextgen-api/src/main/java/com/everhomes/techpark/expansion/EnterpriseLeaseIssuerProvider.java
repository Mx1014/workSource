package com.everhomes.techpark.expansion;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public interface EnterpriseLeaseIssuerProvider {

    LeaseIssuer getLeaseIssuerById(Long id);

    void createLeaseIssuer(LeaseIssuer leaseIssuer);

    void updateLeaseIssuer(LeaseIssuer leaseIssuer);

    void deleteLeaseIssuer(LeaseIssuer leaseIssuer);

    List<LeaseIssuer> listLeaseIssers(Integer namespaceId, String keyword, Long pageAnchor, Integer pageSize);

    LeasePromotionConfig getLeasePromotionConfigByNamespaceId(Integer namespaceId);

    void createLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress);

    void deleteLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress);

    List<LeaseIssuerAddress> listLeaseIsserAddresses(Long leaseIssuerId, Long buildingId);
}
