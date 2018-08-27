// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.AclinkListLocalServersCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.DoorAccessOwnerType;

public interface AclinkServerProvider {

	List<AclinkServer> listLocalServers(CrossShardListingLocator locator, Long ownerId, DoorAccessOwnerType ownerType, String uuid,
			int count);
	
	List<AclinkServer> listLocalServersByUserAuth(CrossShardListingLocator locator, Long userId, Integer namespaceId,
			int count);

	List<AclinkServer> queryAclinkServer(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void createLocalServer(AclinkServer server);

	AclinkServer findServerById(Long id);

	void updateLocalServer(AclinkServer server);

	void deleteLocalServer(Long id);

	AclinkServer findLocalServersByUuid(String uuid);

	List<AclinkServer> queryLocalServers(CrossShardListingLocator locator, AclinkListLocalServersCommand cmd);

}
