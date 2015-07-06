package com.everhomes.recommend;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import net.greghaines.jesque.Job;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactoryImpl;
import com.everhomes.util.ConvertHelper;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private CommonWorkerPool workerPool;
    
    @Autowired
    JesqueClientFactoryImpl jesqueClientFactory;
    
    @Autowired
    RecommendationConfigProvider recommendationConfigProvider;
    
    @Autowired
    RecommendationProvider recommendationProvider;
    
    private final String queueName = "recommend";
    
    @PostConstruct
    public void setup() {
        workerPool.addQueue(queueName);
    }
    
    @Override
    public RecommendationConfig createConfig(@Valid CreateRecommendConfig cmd) {
       RecommendationConfig config = ConvertHelper.convert(cmd, RecommendationConfig.class);
       config.setCreateTime(DateTimeUtils.fromString(cmd.getExpireTime()));
       
       recommendationConfigProvider.createRecommendConfig(config);
       if(config.getId() > 0) {
           final Job job = new Job(RecommendAction.class.getName(),
                   new Object[]{ config });
           
           jesqueClientFactory.getClientPool().enqueue(queueName, job);
       }
       
       return config;
    }
    
    public List<Recommendation> getRecommendsByUserId(Long userId, int sourceType, int pageSize) {
        ListingLocator locator = new ListingLocator();
        locator.setEntityId(userId);
        return recommendationProvider.queryRecommendsByUserId(userId, locator, pageSize, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_RECOMMENDATIONS.USER_ID.eq(userId));
                query.addConditions(Tables.EH_RECOMMENDATIONS.SOURCE_TYPE.eq(sourceType));
                query.addConditions(Tables.EH_RECOMMENDATIONS.STATUS.eq(RecommendStatus.OK.getCode()));
                return query;
            }
            
        });
    }

    @Override
    public void ignoreRecommend(Long userId, Integer suggestType, Long sourceId, Integer sourceType) {
        recommendationProvider.ignoreRecommend(userId, suggestType, sourceId, sourceType);
    }
}
