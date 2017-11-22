// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointSystemProvider {

	void createPointSystem(PointSystem pointSystem);

	void updatePointSystem(PointSystem pointSystem);

    List<PointSystem> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointSystem findById(Long id);

    List<PointSystem> getEnabledPointSystems(Integer namespaceId);
}