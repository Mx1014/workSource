// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkIpadProvider {

	List<AclinkIpad> queryLocalIpads(CrossShardListingLocator locator, Integer count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<AclinkIpad> listLocalIpads(CrossShardListingLocator locator, Long serverId, List<Long> serverIds,
			Long doorAccessId, Byte enterStatus, Byte linkStatus, Byte activeStatus, String uuid, Integer count);

	void createLocalIpad(AclinkIpad ipad);

	AclinkIpad findIpadById(Long id);

	void updateLocalIpad(AclinkIpad ipad);

	void deleteLocalIpad(Long id);

}
