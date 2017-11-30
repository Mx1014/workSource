// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointCategoryProvider {

	void createPointCategory(PointCategory pointCategory);

	void updatePointCategory(PointCategory pointCategory);

    List<PointCategory> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointCategory findById(Long id);

}