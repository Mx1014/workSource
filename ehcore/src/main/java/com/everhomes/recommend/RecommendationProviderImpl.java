package com.everhomes.recommend;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
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
    public List<Recommendation> queryRecommendsByUserId(long userId, ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        
        SelectQuery<EhRecommendationsRecord> query = context.selectQuery(Tables.EH_RECOMMENDATIONS);
        query.addSelect(Tables.EH_RECOMMENDATIONS.fields());
        query.setDistinct(true);
    
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
            
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_RECOMMENDATIONS.ID.gt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_RECOMMENDATIONS.CREATE_TIME.desc());
        query.addLimit(count);
        
        List<EhRecommendationsRecord> records = query.fetch();
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
    
    
}
