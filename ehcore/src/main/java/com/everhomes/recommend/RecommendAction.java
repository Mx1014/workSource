package com.everhomes.recommend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.search.SearchSyncAction;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecommendAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SearchSyncAction.class);
    private RecommendationConfig recommendConfig;

    @Override
    public void run() {
        //this.recommendConfig.getTargetType()
        log.info("Running config id= " + recommendConfig.getId());
    }
    
    public RecommendAction(RecommendationConfig recommend) {
        this.recommendConfig = recommend;
    }
}
