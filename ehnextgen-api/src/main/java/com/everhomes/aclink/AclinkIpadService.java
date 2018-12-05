// @formatter:off
package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;

public interface AclinkIpadService {

	ListLocalIpadResponse listLocalIpads(CrossShardListingLocator locator, ListLocalIpadCommand cmd);

	CreateLocalIpadResponse createLocalIpad(CreateLocalIpadCommand cmd);

	void updateLocalIpad(UpdateLocalIpadCommand cmd);

	void updateIpadLogo(UpdateIpadLogoCommand cmd);

	void deleteLocalIpad(Long id);

}
