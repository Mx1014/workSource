// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.AclinkCameraDTO;
import com.everhomes.rest.aclink.AclinkListLocalServersCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.CreateLocalCamerasCommand;
import com.everhomes.rest.aclink.CreateLocalServersCommand;
import com.everhomes.rest.aclink.DeleteLocalCamerasCommand;
import com.everhomes.rest.aclink.DeleteLocalServerCommand;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalCamerasCommand;
import com.everhomes.rest.aclink.ListLocalCamerasResponse;
import com.everhomes.rest.aclink.QueryAclinkCamerasCommand;
import com.everhomes.rest.aclink.UpdateLocalCamerasCommand;
import com.everhomes.rest.aclink.UpdateLocalServersCommand;

public interface AclinkCameraService {
	//CrossShardListingLocator locator, Long ownerId, Byte ownerType, Long serverId, Long doorAccessId, Byte enterStatus, Byte linkStatus, Integer pageSize
	ListLocalCamerasResponse listLocalCameras(ListLocalCamerasCommand cmd);

	void createLocalCamera(CreateLocalCamerasCommand cmd);

	void updateLocalCamera(UpdateLocalCamerasCommand cmd);

	void deleteLocalCameras(Long id);
	
	
}
