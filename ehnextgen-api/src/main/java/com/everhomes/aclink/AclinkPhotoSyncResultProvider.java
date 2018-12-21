// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkPhotoSyncResultProvider {

	List<AclinkPhotoSyncResult> querySyncResList(CrossShardListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);
	
	List<AclinkPhotoSyncResult> queryByPhotoId(Long id);

	void createSyncResult(AclinkPhotoSyncResult syncRes);

	List<AclinkPhotoSyncResult> queryByPhotoIdAndRes(Long id, byte res);

}
