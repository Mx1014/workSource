// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointGoodProvider {

	void createPointGood(PointGood pointGood);

	void updatePointGood(PointGood pointGood);

    List<PointGood> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

	PointGood findById(Long id);

    List<PointGood> listPointGood(Integer namespaceId, Long systemId, int pageSize, ListingLocator locator);
}