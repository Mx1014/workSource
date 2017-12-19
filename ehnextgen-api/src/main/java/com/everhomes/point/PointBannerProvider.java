// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;
import java.util.Set;

public interface PointBannerProvider {

	void createPointBanner(PointBanner pointBanner);

	void updatePointBanner(PointBanner pointBanner);

    void deletePointBanner(PointBanner pointBanner);

    List<PointBanner> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointBanner findById(Long id);

    List<PointBanner> listByIds(Set<Long> ids);

    List<PointBanner> listPointBannersBySystemId(Long systemId, int pageSize, ListingLocator locator);
}