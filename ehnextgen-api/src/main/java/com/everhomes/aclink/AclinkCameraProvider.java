// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.AclinkCameraDTO;

public interface AclinkCameraProvider {

	List<AclinkCamera> queryLocalCameras(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
	
	List<AclinkCamera> listLocalCameras(CrossShardListingLocator locator, Long serverId, List<Long> serverIds, Long doorAccessId, Byte enterStatus, Byte linkStatus, int count);

	void createLocalCamera(AclinkCamera camera);

	AclinkCamera findCameraById(Long id);

	void updateLocalCamera(AclinkCamera camera);

	void deleteLocalCamera(Long id);

}
