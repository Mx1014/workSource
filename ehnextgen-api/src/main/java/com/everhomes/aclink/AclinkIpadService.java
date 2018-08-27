// @formatter:off
package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.CreateLocalIpadCommand;
import com.everhomes.rest.aclink.CreateLocalIpadResponse;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalIpadCommand;
import com.everhomes.rest.aclink.ListLocalIpadResponse;
import com.everhomes.rest.aclink.UpdateLocalIpadCommand;

public interface AclinkIpadService {

	ListLocalIpadResponse listLocalIpads(CrossShardListingLocator locator, ListLocalIpadCommand cmd);

	CreateLocalIpadResponse createLocalIpad(CreateLocalIpadCommand cmd);

	void updateLocalIpad(UpdateLocalIpadCommand cmd);

	void deleteLocalIpad(Long id);

}
