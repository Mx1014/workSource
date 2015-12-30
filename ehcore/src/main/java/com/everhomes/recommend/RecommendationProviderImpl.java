package com.everhomes.recommend;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.recommend.RecommendStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.records.EhRecommendationsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class RecommendationProviderImpl implements RecommendationProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createRecommendation(Recommendation recommend) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, recommend.getUserId()));
        InsertQuery<EhRecommendationsRecord> item = context.insertQuery(Tables.EH_RECOMMENDATIONS);
        item.setRecord(ConvertHelper.convert(recommend, EhRecommendationsRecord.class));
        
        item.setReturning(Tables.EH_RECOMMENDATIONS.ID);
        if(item.execute() > 0) {
            recommend.setId(item.getReturnedRecord().getId());
           }
    }
    
    @Override
    public void updateRecommendation(Recommendation recommend) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, recommend.getUserId()));
        UpdateQuery<EhRecommendationsRecord> item = context.updateQuery(Tables.EH_RECOMMENDATIONS);
        item.setRecord(ConvertHelper.convert(recommend, EhRecommendationsRecord.class));
        
        item.setReturning(Tables.EH_RECOMMENDATIONS.ID);
        item.execute();
    }
    
    @Override
    public List<Recommendation> queryRecommendsByUserId(long userId, ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        
        SelectQuery<EhRecommendationsRecord> query = context.selectQuery(Tables.EH_RECOMMENDATIONS);
        query.addSelect(Tables.EH_RECOMMENDATIONS.USER_ID
                , Tables.EH_RECOMMENDATIONS.SOURCE_ID
                , Tables.EH_RECOMMENDATIONS.SOURCE_TYPE
                , Tables.EH_RECOMMENDATIONS.APPID
                , Tables.EH_RECOMMENDATIONS.SUGGEST_TYPE
                , Tables.EH_RECOMMENDATIONS.EMBEDDED_JSON
                , Tables.EH_RECOMMENDATIONS.STATUS);
        query.setDistinct(true);
    
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
            
        //query.addConditions(Tables.EH_RECOMMENDATIONS.USER_ID.eq(userId));
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_RECOMMENDATIONS.ID.gt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_RECOMMENDATIONS.CREATE_TIME.desc());
        query.addLimit(count);
        
        List<EhRecommendationsRecord> records = query.fetch().map(new RecommendationRecordMapper());
        List<Recommendation> recommends = records.stream().map((r) -> {
            return ConvertHelper.convert(r, Recommendation.class);
        }).collect(Collectors.toList());
        
        if(recommends.size() > 0) {
            locator.setAnchor(recommends.get(recommends.size() -1).getId());
        }
        
        return recommends;
    }
    
//    public List<Recommendation> findRecommendByUserId(long userId) {
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
//        ListingLocator locator = new ListingLocator();
//        
//        
//        return null;
//    }
    
    @Override
    public void ignoreRecommend(Long userId, Integer suggestType, Long sourceId, Integer sourceType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userId));
        context.update(Tables.EH_RECOMMENDATIONS).set(Tables.EH_RECOMMENDATIONS.STATUS, RecommendStatus.IGNORE.getCode())
        .where(Tables.EH_RECOMMENDATIONS.SOURCE_ID.eq(sourceId))
        .and(Tables.EH_RECOMMENDATIONS.SOURCE_TYPE.eq(sourceType))
        .and(Tables.EH_RECOMMENDATIONS.SUGGEST_TYPE.eq(suggestType))
        .and(Tables.EH_RECOMMENDATIONS.USER_ID.eq(userId))
        .and(Tables.EH_RECOMMENDATIONS.STATUS.eq(0)).execute();
        
    }
    
}
