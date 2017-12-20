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

    List<PointGood> listPointGood(Integer namespaceId, Byte status, int pageSize, ListingLocator locator);

    List<PointGood> listEnabledPointGoods(Integer namespaceId, int pageSize, ListingLocator locator);

    PointGood findBySystemAndGood(Long systemId, String number, String shopNumber);
}