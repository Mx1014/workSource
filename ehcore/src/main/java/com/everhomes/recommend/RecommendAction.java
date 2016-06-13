package com.everhomes.recommend;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.family.FamilyService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.family.FamilyMemberDTO;
import com.everhomes.rest.recommend.RecommendTargetType;
import com.everhomes.search.SearchSyncAction;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecommendAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SearchSyncAction.class);
    
    @Autowired
    private  FamilyService familyService;
    
    @Autowired
    RecommendationConfigProvider recommendationConfigProvider;
    
    @Autowired
    RecommendationProvider recommendationProvider;
    
    private RecommendationConfig recommendConfig;

    private void runCity() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCityId(this.recommendConfig.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
                }
            
            pageOffset++;            
            if(pageOffset > 10000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    private void runCommunity() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCommunityId(this.recommendConfig.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
                }
            
            pageOffset++;            
            if(pageOffset > 1000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    private void runFamily() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = this.familyService.listFamilyMembersByFamilyId(this.recommendConfig.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
                }
            
            pageOffset++;            
            if(pageOffset > 1000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    private void runUser() {
        this.runByUserId(this.recommendConfig.getTargetId());
    }
    
    private void runByUserId(Long userId) {
        ListingLocator locator = new ListingLocator();
        locator.setEntityId(userId);
        List<Recommendation> recommends = this.recommendationProvider.queryRecommendsByUserId(userId, locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_RECOMMENDATIONS.SOURCE_TYPE.eq(recommendConfig.getSourceType()));
                query.addConditions(Tables.EH_RECOMMENDATIONS.SOURCE_ID.eq(recommendConfig.getSourceId()));
                return query;
                }
        });
        
        // TODO if recommends exists before?
//        if(recommends.size() > 0) {
//            Recommendation commend = recommends.get(0);
//            commend.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            this.recommendationProvider.updateRecommendation(commend);
//        }
        
        Recommendation recommend = ConvertHelper.convert(this.recommendConfig, Recommendation.class);
        recommend.setUserId(userId);
        recommend.setCreateTime(new Timestamp(System.currentTimeMillis()));
        this.recommendationProvider.createRecommendation(recommend);
        
        //TODO send an alert
    }
    
    @Override
    public void run() {
        RecommendTargetType targetType = RecommendTargetType.fromCode(this.recommendConfig.getTargetType());
        log.info("Running config id= " + recommendConfig.getId());
        
        switch(targetType) {
        case CITY:
            this.runCity();
            break;
        case COMMUNITY:
            this.runCommunity();
            break;
        case FAMILY:
            this.runFamily();
            break;
        case USER:
            this.runUser();
            break;
        }
    }
    
    public RecommendAction(RecommendationConfig recommend) {
        this.recommendConfig = recommend;
    }
}
