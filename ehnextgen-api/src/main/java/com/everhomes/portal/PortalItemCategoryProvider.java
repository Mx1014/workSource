// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemCategoryProvider {

	void createPortalItemCategory(PortalItemCategory portalItemCategory);

    void createPortalItemCategories(List<PortalItemCategory> portalItemCategories);

    void updatePortalItemCategory(PortalItemCategory portalItemCategory);

    void deleteByVersionId(Long versionId);

    PortalItemCategory findPortalItemCategoryById(Long id);

    Integer findDefaultOrder(Long itemGroupId);

	List<PortalItemCategory> listPortalItemCategory(Integer namespaceId, Long itemGroupId);

	PortalItemCategory getPortalItemCategoryByName(Integer namespaceId, Long itemGroupId, String name);

    List<PortalItemCategory> listPortalItemCategoryByVersion(Integer namespaceId, Long versionId);
}