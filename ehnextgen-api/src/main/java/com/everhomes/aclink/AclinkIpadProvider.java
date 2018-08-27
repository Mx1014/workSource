// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.ListLocalIpadCommand;

public interface AclinkIpadProvider {

	List<AclinkIpad> queryLocalIpads(CrossShardListingLocator locator, Integer count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<AclinkIpad> listLocalIpads(CrossShardListingLocator locator, ListLocalIpadCommand cmd);

	void createLocalIpad(AclinkIpad ipad);

	AclinkIpad findIpadById(Long id);

	void updateLocalIpad(AclinkIpad ipad);

	void deleteLocalIpad(Long id);

	List<AclinkIpad> listLocalIpadByIds(List<Long> reqIpadIds);

	void updateIpadBatch(List<AclinkIpad> updateIpads);

}
