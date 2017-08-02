// @formatter:off
package com.everhomes.portal;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PortalItemProvider {

	void createPortalItem(PortalItem portalItem);

	void updatePortalItem(PortalItem portalItem);

	PortalItem findPortalItemById(Long id);

	List<PortalItem> listPortalItemByCategoryId(Long itemCategoryId);

	List<PortalItem> listPortalItemByGroupId(Long itemGroupId);

	List<PortalItem> listPortalItems(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

	List<PortalItem> listPortalItems(Long itemCategoryId, Integer namespaceId, String actionType, Long itemGroupId);

	List<PortalItem> listPortalItems(Long itemCategoryId, Long itemGroupId);
}