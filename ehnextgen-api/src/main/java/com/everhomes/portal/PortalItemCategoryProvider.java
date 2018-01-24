// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemCategoryProvider {

	void createPortalItemCategory(PortalItemCategory portalItemCategory);

    void createPortalItemCategories(List<PortalItemCategory> portalItemCategories);

    void updatePortalItemCategory(PortalItemCategory portalItemCategory);

	PortalItemCategory findPortalItemCategoryById(Long id);

	List<PortalItemCategory> listPortalItemCategory(Integer namespaceId, Long itemGroupId);

	PortalItemCategory getPortalItemCategoryByName(Integer namespaceId, Long itemGroupId, String name);

    List<PortalItemCategory> listPortalItemCategoryByVersion(Integer namespaceId, Long versionId);
}