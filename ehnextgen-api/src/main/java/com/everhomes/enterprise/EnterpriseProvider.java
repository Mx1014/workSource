package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationWorkPlaces;
import com.everhomes.techpark.expansion.EnterpriseDetail;

public interface EnterpriseProvider {
    public void createEnterprise(Enterprise enterprise);
    public Enterprise findEnterpriseById(long id) ;
    public void updateEnterprise(Enterprise enterprise);
    public Enterprise getEnterpriseById(Long id);
    public void deleteEnterpriseById(Long id);
    public void createEnterpriseCommunityMap(EnterpriseCommunityMap ec);
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap ec);
    public void deleteEnterpriseCommunityMapById(EnterpriseCommunityMap ec);
    public EnterpriseCommunityMap getEnterpriseCommunityMapById(Long id);
    public List<EnterpriseCommunityMap> queryEnterpriseMapByCommunityId(ListingLocator locator, Long comunityId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<EnterpriseCommunityMap> queryEnterpriseCommunityMap(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    
    public void createEnterpriseCommunity(Long creatorId, EnterpriseCommunity ec);
    public EnterpriseCommunity getEnterpriseCommunityById(Long id);
    public List<Enterprise> queryEnterprises(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback, Enterprise enterprise);
    List<Enterprise> queryEnterpriseByPhone(String phone);
    List<Enterprise> listEnterprises(CrossShardListingLocator locator, int count);
    EnterpriseCommunityMap findEnterpriseCommunityByEnterpriseId(Long communityId, Long enterpriseId);
    
    void createEnterpriseAttachment(EnterpriseAttachment attachment);
    void createEnterpriseAddress(EnterpriseAddress enterpriseAddr);
    List<EnterpriseAttachment> listEnterpriseAttachments(long enterpriseId);
    
    void populateEnterpriseAttachments(final Enterprise enterprise);
    void populateEnterpriseAttachments(final List<Enterprise> enterprises);
    void populateEnterpriseAddresses(final Enterprise enterprise);
    void populateEnterpriseAddresses(final List<Enterprise> enterprises);
    
    Enterprise findEnterpriseByAddressId(long addressId);
    
    Boolean isExistInEnterpriseAddresses(long enterpriseId, long addressId);
    
    void deleteEnterpriseAttachmentsByEnterpriseId(long enterpriseId);
    
    List<EnterpriseAddress> findEnterpriseAddressByEnterpriseId(Long enterpriseId);
    void deleteEnterpriseAddress(EnterpriseAddress enterpriseAddr);
    void updateEnterpriseAddress(EnterpriseAddress ea);
    List<Enterprise> listEnterprisesByName(CrossShardListingLocator locator, int count, Enterprise enterprise);
    
    EnterpriseAddress findEnterpriseAddressByAddressId(Long addressId);
    
    void createEnterpriseDetail(EnterpriseDetail enterpriseDetail);
    
    void updateEnterpriseDetail(EnterpriseDetail enterpriseDetail);
    
    void deleteEnterpriseAddressByEnterpriseId(long enterpriseId);
    
    void deleteEnterpriseAddressById(Long id);
    
    EnterpriseDetail findEnterpriseDetailByEnterpriseId(Long enterpriseId);
	public EnterpriseAddress findEnterpriseAddressByEnterpriseIdAndAddressId(Long enterpriseId, Long addressId);

    /**
     * 向eh_enterprise_community_map表中持久化数据
     * @param enterpriseCommunityMap
     */
    void insertIntoEnterpriseCommunityMap(EnterpriseCommunityMap enterpriseCommunityMap);

    /**
     * 向eh_organization_community_requests表中添加数据
     * @param organizationCommunityRequest
     */
    void insertIntoOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest);

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param organizationWorkPlaces
     */
    void deleteEnterpriseByOrgIdAndCommunityId(OrganizationWorkPlaces organizationWorkPlaces);

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param enterpriseCommunityMap
     */
    void deleteEnterpriseFromEnterpriseCommunityMapByOrgIdAndCommunityId(EnterpriseCommunityMap enterpriseCommunityMap);
}
