// @formatter:off
package com.everhomes.portal;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PortalItemProvider {

	void createPortalItem(PortalItem portalItem);

	void updatePortalItem(PortalItem portalItem);

	PortalItem findPortalItemById(Long id);

	List<PortalItem> listPortalItem(Long itemCategoryId);

	List<PortalItem> listPortalItem(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

	List<PortalItem> listPortalItem(Long itemCategoryId, Integer namespaceId, String actionType);
}