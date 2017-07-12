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

    List<LeaseIssuer> listLeaseIssers(Integer namespaceId, Long organizationId, String keyword, Long pageAnchor, Integer pageSize);

    LeasePromotionConfig getLeasePromotionConfigByNamespaceId(Integer namespaceId);

    void createLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress);

    void deleteLeaseIssuerAddressByLeaseIssuerId(Long leaseIssuerId);

    List<LeaseIssuerAddress> listLeaseIsserAddresses(Long leaseIssuerId, Long buildingId);

    List<LeaseIssuerAddress> listLeaseIsserBuildings(Long leaseIssuerId);

    LeaseIssuer fingLeaseIssersByOrganizationId(Integer namespaceId, Long organizationId);

    LeaseIssuer findLeaseIssersByContact(Integer namespaceId, String contact);

    List<LeasePromotionConfig2> listLeasePromotionConfigByNamespaceId(Integer namespaceId);
}
