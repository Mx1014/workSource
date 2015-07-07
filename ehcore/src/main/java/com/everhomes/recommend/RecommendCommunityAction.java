package com.everhomes.recommend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecommendCommunityAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RecommendCommunityAction.class);
    
    private final String userId;
    private final String communityId;
    
    @Override
    public void run() {
        
    }
    
    public RecommendCommunityAction(String userId, String communityId) {
        this.userId = userId;
        this.communityId = communityId;
    }
}
