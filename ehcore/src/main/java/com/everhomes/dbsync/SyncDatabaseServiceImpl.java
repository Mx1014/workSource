package com.everhomes.dbsync;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.listing.ListingLocator;
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
    
    @Autowired
    private NashornProcessService nashornProcessService;
    
    @PostConstruct
    public void setup() {
    	nashornProcessService.start();
    	
    	ListingLocator locator = new ListingLocator();
    	int count = 200;
    	do {
    		List<SyncMapping> mappings = syncMappingProvider.getAllMappings(locator, count);
    		for(SyncMapping map : mappings) {
    			SyncApp app = syncAppProvider.getSyncAppById(map.getSyncAppId());
    	        NashornMappingObject mapObj = new NashornMappingObject();
    	        mapObj.setName(map.getName());
    	        mapObj.setAppName(app.getName());
    	        nashornProcessService.putProcessJob(mapObj);
    		}
    	} while(locator.getAnchor() != null);
    }
    
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
        
        NashornMappingObject mapObj = new NashornMappingObject();
        mapObj.setName(map.getName());
        
        SyncApp app = syncAppProvider.getSyncAppById(map.getSyncAppId());
        mapObj.setAppName(app.getName());
        
        map.setStatus((byte)1);//mark as valid
        syncMappingProvider.createSyncMapping(map);
        
        nashornProcessService.putProcessJob(mapObj);
        
        return ConvertHelper.convert(map, SyncMappingDTO.class);
    }
    
    @Override
    public String loadMapping(String appName, String mappingName) {
    	SyncApp app = syncAppProvider.findSyncAppByName(appName);
    	if(app == null) {
    		return null;
    	}
    	
    	if(mappingName.endsWith(".js")) {
    		mappingName = mappingName.substring(0, mappingName.length() - 3);
    	}
    	
    	SyncMapping mapping = this.syncMappingProvider.findSyncMappingByName(mappingName);
    	if(mapping != null) {
    		return mapping.getContent();
    	}
    	
    	return null;
    }

}
