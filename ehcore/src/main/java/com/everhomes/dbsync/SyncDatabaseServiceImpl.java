package com.everhomes.dbsync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.dbsync.CreateSyncMappingCommand;
import com.everhomes.rest.dbsync.SyncAppCreateCommand;
import com.everhomes.rest.dbsync.SyncAppDTO;
import com.everhomes.rest.dbsync.SyncMappingDTO;
import com.everhomes.rest.dbsync.SyncPolicyType;
import com.everhomes.rest.dbsync.SyncStateType;
import com.everhomes.util.ConvertHelper;

@Component
public class SyncDatabaseServiceImpl implements SyncDatabaseService {
    @Autowired
    private SyncAppProvider syncAppProvider;
   
    @Autowired
    private NashornObjectService nashornObjectService;
    
    @Autowired
    private SyncMappingProvider syncMappingProvider;
    
    @Autowired
    private SyncTranProvider syncTranProvider;
    
    @Override
    public SyncAppDTO createApp(SyncAppCreateCommand cmd) {
        SyncApp app = ConvertHelper.convert(cmd, SyncApp.class);
        app.setSecretKey(""); //TODO remove it?
        app.setPolicyType(SyncPolicyType.INCREMENT.getCode());
        app.setState(SyncStateType.SLEEPING.getCode());
        syncAppProvider.createSyncApp(app);
        
        return ConvertHelper.convert(app, SyncAppDTO.class);
    }
    
    @Override
    public SyncMappingDTO createMapping(CreateSyncMappingCommand cmd) {
        SyncMapping map = ConvertHelper.convert(cmd, SyncMapping.class);
        syncMappingProvider.createSyncMapping(map);
        return ConvertHelper.convert(map, SyncMappingDTO.class);
    }

}
