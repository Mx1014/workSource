package com.everhomes.recommend;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import net.greghaines.jesque.Job;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.recommend.CreateRecommendConfig;
import com.everhomes.rest.recommend.ListRecommendConfigCommand;
import com.everhomes.rest.recommend.ListRecommendConfigResponse;
import com.everhomes.rest.recommend.RecommendConfigDTO;
import com.everhomes.rest.recommend.RecommendStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactoryImpl;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
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
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
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
    
   @Override
   public void createRecommendation(Recommendation recommend) {
       recommendationProvider.createRecommendation(recommend);
       
       UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(recommend.getUserId(), UserProfileContstant.RecommendName);
       if(null != profile) {
           profile.setItemValue(Long.toString(System.currentTimeMillis()));
           userActivityProvider.updateUserProfile(profile);
       } else {
           UserProfile p2 = new UserProfile();
           p2.setItemName(UserProfileContstant.RecommendName);
           p2.setItemKind((byte)0);
           p2.setItemValue(Long.toString(System.currentTimeMillis()));
           p2.setOwnerId(recommend.getUserId());
           userActivityProvider.addUserProfile(p2);
           }
    }
   
   @Override
   public void communityNotify(Long userId, Long addressId ,Long communityId) {
       final Job job = new Job(RecommendCommunityAction.class.getName(),
               new Object[]{ userId, addressId, communityId });
       
       jesqueClientFactory.getClientPool().enqueue(queueName, job);
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
                query.addConditions(Tables.EH_RECOMMENDATIONS.EMBEDDED_JSON.isNotNull());
                return query;
            }
            
        });
    }

    @Override
    public void ignoreRecommend(Long userId, Integer suggestType, Long sourceId, Integer sourceType) {
        recommendationProvider.ignoreRecommend(userId, suggestType, sourceId, sourceType);
    }
    
    @Override
    public ListRecommendConfigResponse listRecommendConfigsBySource(ListRecommendConfigCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<RecommendationConfig> recs = recommendationConfigProvider.listRecommendConfigs(locator, pageSize, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(cmd.getSourceId() != null) {
                    query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.SOURCE_ID.eq(cmd.getSourceId()));
                    }
                if(cmd.getSourceType() != null) {
                    query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.SOURCE_TYPE.eq(cmd.getSourceType()));
                    }
                if(cmd.getTargetId() != null) {
                    query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.TARGET_ID.eq(cmd.getTargetId()));
                    }
                if(cmd.getTargetType() != null) {
                    query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.TARGET_TYPE.eq(cmd.getTargetType()));
                    }
                if(cmd.getSuggestType() != null) {
                    query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.SUGGEST_TYPE.eq(cmd.getSuggestType()));
                    }
                return query;
            }
            
        });
        
        ListRecommendConfigResponse resp = new ListRecommendConfigResponse();
        resp.setNextPageAnchor(locator.getAnchor());
        List<RecommendConfigDTO> dtos = recs.stream().map((r) -> {
            return ConvertHelper.convert(r, RecommendConfigDTO.class);
        }).collect(Collectors.toList());
        resp.setRecommendConfigs(dtos);
        return resp;
    }
}
