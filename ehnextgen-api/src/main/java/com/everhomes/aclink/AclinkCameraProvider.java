// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.AclinkCameraDTO;
import com.everhomes.rest.aclink.ListLocalCamerasCommand;
import com.everhomes.rest.aclink.UpdateCameraIpadBatchCommand;

public interface AclinkCameraProvider {

	List<AclinkCamera> queryLocalCameras(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
	
	List<AclinkCamera> listLocalCameras(CrossShardListingLocator locator,ListLocalCamerasCommand cmd);
	
	List<AclinkCamera> listLocalCamerasByIds(List<Long> ids);

	void createLocalCamera(AclinkCamera camera);

	AclinkCamera findCameraById(Long id);

	List<AclinkCameraDTO> findCameraByDoorId(Long doorId);

	void updateLocalCamera(AclinkCamera camera);

	void deleteLocalCamera(Long id);

	void updateCameraBatch(List<AclinkCamera> updateCameras);

}
