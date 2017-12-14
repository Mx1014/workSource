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

    List<LeaseIssuer> listLeaseIssuers(Integer namespaceId, Long organizationId, String keyword, Long categoryId,
                                       Long pageAnchor, Integer pageSize);

    LeasePromotionConfig getLeasePromotionConfigByNamespaceId(Integer namespaceId);

    void createLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress);

    void deleteLeaseIssuerAddressByLeaseIssuerId(Long leaseIssuerId);

    List<LeaseIssuerAddress> listLeaseIssuerAddresses(Long leaseIssuerId, Long buildingId);

    List<LeaseIssuerAddress> listLeaseIssuerBuildings(Long leaseIssuerId);

    LeaseIssuer fingLeaseIssuersByOrganizationId(Integer namespaceId, Long organizationId, Long categoryId);

    LeaseIssuer findLeaseIssuersByContact(Integer namespaceId, String contact, Long categoryId);

    List<LeasePromotionConfig> listLeasePromotionConfigs(Integer namespaceId, Long categoryId);

    void createLeasePromotionConfig(LeasePromotionConfig config);

    LeasePromotionConfig findLeasePromotionConfig(Integer namespaceId, String configName, Long categoryId);

    void deleteLeasePromotionConfig(Integer namespaceId, String configName, Long categoryId);

    void updateLeasePromotionConfig(LeasePromotionConfig config);
}
