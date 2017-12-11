// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointActionProvider {

	void createPointAction(PointAction pointAction);

	void updatePointAction(PointAction pointAction);

    List<PointAction> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointAction findById(Long id);

    List<PointAction> listPointActionsBySystemId(Long systemId);

    void createPointActions(List<PointAction> pointActions);

    List<PointAction> listByOwner(Integer namespaceId, Long systemId, String ownerType, Long ownerId);
}