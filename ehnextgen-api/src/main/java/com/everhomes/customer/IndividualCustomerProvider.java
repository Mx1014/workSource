package com.everhomes.customer;

import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.pm.OrganizationOwnerAddress;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/28.
 */
public interface IndividualCustomerProvider {
    //个人客户因为历史原因在eh_organization_owners表中
    // 对该表的一些操作在organizationProvider，考虑这个provider太多方法了决定单拿出来
    List<OrganizationOwner> listOrganizationOwnerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId);
    List<OrganizationOwner> listOrganizationOwnerByNamespaceIdAndName(Integer namespaceId, String name);

    List<OrganizationOwnerAddress> listOrganizationOwnerAddressByOwnerId(Long ownerId);
    void deleteOrganizationOwnerAddress(OrganizationOwnerAddress address);
    void createOrganizationOwnerAddress(OrganizationOwnerAddress address);
    OrganizationOwner findOrganizationOwnerById(Long id);
}
