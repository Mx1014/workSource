// @formatter:off
package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkListLocalServersCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.CreateLocalServersCommand;
import com.everhomes.rest.aclink.CreateMarchUUIDCommand;
import com.everhomes.rest.aclink.CreateMarchUUIDResponse;
import com.everhomes.rest.aclink.DeleteLocalServerCommand;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.GetLocalServerAddressResponse;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalServerByOrgCommand;
import com.everhomes.rest.aclink.PairLocalServerResponse;
import com.everhomes.rest.aclink.QueryServerRelationsCommand;
import com.everhomes.rest.aclink.QueryServerRelationsResponse;
import com.everhomes.rest.aclink.SyncLocalServerResponse;
import com.everhomes.rest.aclink.UpdateCameraIpadBatchCommand;
import com.everhomes.rest.aclink.ListLocalServerByOrgResponse;
import com.everhomes.rest.aclink.UpdateLocalServersCommand;

public interface AclinkServerService {

	String generateUUID(CreateMarchUUIDCommand cmd);

	ListAclinkServersResponse listLocalServers(AclinkListLocalServersCommand cmd);
	
	List<AclinkServerDTO> listLocalServers(CrossShardListingLocator locator, Long ownerId, DoorAccessOwnerType ownerType, String uuid, int count);

	void createLocalServer(CreateLocalServersCommand cmd);

	void updateLocalServer(UpdateLocalServersCommand cmd);

	void deleteLocalServer(DeleteLocalServerCommand cmd);

	AclinkServerDTO findLocalServerById(Long id);

	QueryServerRelationsResponse queryServerRelations(QueryServerRelationsCommand cmd);

	SyncLocalServerResponse syncLocalServer(Long id);

	PairLocalServerResponse pairLocalServer(String uuid, String ipAddress, String version);

	AclinkServerDTO onServerConnecting(AclinkConnectingCommand cmd);

	void updateServerSyncTime(Long serverId);

	ListLocalServerByOrgResponse listLocalServerByOrg(ListLocalServerByOrgCommand cmd);

	AclinkServerDTO onServerDisconnecting(AclinkDisconnectedCommand cmd);

	void updateCameraIpadBatch(UpdateCameraIpadBatchCommand cmd);

	GetLocalServerAddressResponse getLocalServerAddressByIpad(String uuid);

}
