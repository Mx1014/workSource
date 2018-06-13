// @formatter:off
package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.CreateLocalIpadCommand;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalIpadResponse;
import com.everhomes.rest.aclink.UpdateLocalIpadCommand;

public interface AclinkIpadService {

	ListLocalIpadResponse listLocalIpads(CrossShardListingLocator locator, Long ownerId, Byte ownerType, Long serverId,
			Long doorAccessId, Byte enterStatus, Byte linkStatus, Byte activeStatus, String uuid, Integer pageSize);

	void createLocalIpad(CreateLocalIpadCommand cmd);

	void updateLocalIpad(UpdateLocalIpadCommand cmd);

	void deleteLocalIpad(Long id);

}
