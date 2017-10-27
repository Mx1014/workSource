// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemCategoryProvider {

	void createPortalItemCategory(PortalItemCategory portalItemCategory);

	void updatePortalItemCategory(PortalItemCategory portalItemCategory);

	PortalItemCategory findPortalItemCategoryById(Long id);

	List<PortalItemCategory> listPortalItemCategory(Integer namespaceId, Long itemGroupId);

	PortalItemCategory getPortalItemCategoryByName(Integer namespaceId, Long itemGroupId, String name);
}