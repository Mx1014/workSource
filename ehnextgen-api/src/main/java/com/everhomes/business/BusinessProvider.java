package com.everhomes.business;

import com.everhomes.rest.asset.CheckPaymentUserResponse;

import java.util.List;

public interface BusinessProvider {
    void createBusiness(Business business);
    void updateBusiness(Business business);
    void deleteBusiness(Business business);
    void deleteBusiness(Long id);
    Business findBusinessById(Long id);
    
    List<BusinessCategory> findBusinessCategoriesByCategory(Long category, Integer offset, Integer pageSize);
    List<Business> findBusinessByIds(List<Long> ids);
    List<BusinessVisibleScope> findBusinessVisibleScopesByScope(Long scopeId, Byte scopeCode);
    
    void createBusinessVisibleScope(BusinessVisibleScope businessVisibleScope);
    void updateBusinessVisibleScope(BusinessVisibleScope businessVisibleScope);
    void deleteBusinessVisibleScope(BusinessVisibleScope businessVisibleScope);
    void deleteBusinessVisibleScope(Long id);
    BusinessVisibleScope findBusinessVisibleScopeById(Long id);
    void deleteBusinessVisibleScopeByBusiness(Business business);
    
    void createBusinessCategory(BusinessCategory businessCategory);
    void updateBusinessCategory(BusinessCategory businessCategory);
    void deleteBusinessCategory(BusinessCategory businessCategory);
    void deleteBusinessCategory(Long id);
    void deleteBusinessCategoryByBusiness(Business business);
    BusinessCategory findBusinessCategoryById(Long id);
    Business findBusinessByTargetId(String targetId);
    List<Business> listBusinessByCategroys(List<Long> categoryIds, List<String> geoHashList,List<Long> businessNamespaceOwnerIds);
    List<Business> findBusinessByCreatorId(Long userId);
    List<Business> listBusinessesByKeyword(String keyword, Integer offset, Integer pageSize);
    void createBusinessAssignedScope(BusinessAssignedScope businessAssignedScope);
    void deleteBusinessAssignedScope(Long id);
    void deleteBusinessAssignedScope(BusinessAssignedScope businessAssignedScope);
    void updateBusinessAssignedScope(BusinessAssignedScope businessAssignedScope);
    BusinessAssignedScope findBusinessAssignedScopeById(Long id);
    void deleteBusinessAssignedScopeByBusinessId(Long businessId);
    List<BusinessAssignedScope> listBusinessAssignedScopeByScope(Long cityId,Long communityId,List<Long> businessNamespaceOwnerIds);
    List<BusinessCategory> findBusinessCategoriesByIdAndOwnerIds(Long id,List<Long> recommendBizIds);
    List<BusinessAssignedScope> findBusinessAssignedScopesByBusinessId(Long id);
	List<BusinessCategory> listBusinessCategoriesByCatPIdAndOwnerIds(Long id,List<Long> recommendBizIds);
	List<Business> listBusinessByIds(List<Long> ids);
	BusinessAssignedNamespace findBusinessAssignedNamespaceByNamespace(Long ownerId,Integer namespaceId,Byte visibleFlag);
	void updateBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace);
	void createBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace);
	void deleteBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace);
	BusinessAssignedScope findBusinessAssignedScopeByScope(Long ownerId,Byte scopeCode, Long scopeId);
	List<BusinessAssignedScope> listBusinessAssignedScopeByOwnerId(Long ownerId);
	List<BusinessAssignedNamespace> listBusinessAssignedNamespaceByOwnerId(Long ownerId,Byte visibleFlag);
	List<BusinessAssignedNamespace> listBusinessAssignedNamespaceByNamespaceId(Integer namespaceId,Byte visibleFlag);
	List<Business> listBusinessByCategorys(List<Long> categoryIds,List<Long> bizIds);

    CheckPaymentUserResponse checkoutPaymentUser(Long userId);
}
