package com.everhomes.dbsync;

import com.everhomes.rest.dbsync.CreateSyncMappingCommand;
import com.everhomes.rest.dbsync.SyncAppCreateCommand;
import com.everhomes.rest.dbsync.SyncAppDTO;
import com.everhomes.rest.dbsync.SyncMappingDTO;

public interface SyncDatabaseService {

    SyncAppDTO createApp(SyncAppCreateCommand cmd);

    SyncMappingDTO createMapping(CreateSyncMappingCommand cmd);

	String loadMapping(String appName, String mappingName);

}
