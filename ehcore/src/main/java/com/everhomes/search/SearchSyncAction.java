package com.everhomes.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchSyncAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SearchSyncAction.class);
    
    //OK can do better for build, using bean resolver. But now, just it.
    private String actionType;
    
    @Autowired
    GroupSearcher groupSearcher;
    
    @Autowired
    PostSearcher postSearcher;
    
    @Autowired
    CommunitySearcher communitySearcher;
    
    @Override
    public void run() {
        if(actionType.equals("group")) {
            groupSearcher.syncFromDb();
        } else if(actionType.equals("post")) {
            postSearcher.syncFromDb();
        } else if(actionType.equals("community")) {
            communitySearcher.syncDb();
        } else if(actionType.equals("all")) {
            postSearcher.syncFromDb();
            communitySearcher.syncDb();
            groupSearcher.syncFromDb();
        } else {
            log.debug("do nothing for syncdb");
        }
    }
    
    public SearchSyncAction(final String actionType) {
        this.actionType = actionType; 
    }
}
