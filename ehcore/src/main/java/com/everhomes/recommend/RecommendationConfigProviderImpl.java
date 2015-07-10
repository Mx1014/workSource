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
import com.everhomes.server.schema.tables.records.EhRecommendationConfigsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class RecommendationConfigProviderImpl implements RecommendationConfigProvider {
    @Autowired
    private DbProvider dbProvider;
    
    
    @Override
    public void createRecommendConfig(RecommendationConfig config) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhRecommendationConfigsRecord> item = context.insertQuery(Tables.EH_RECOMMENDATION_CONFIGS);
        item.setRecord(ConvertHelper.convert(config, EhRecommendationConfigsRecord.class));
        
        item.setReturning(Tables.EH_RECOMMENDATION_CONFIGS.ID);
        if(item.execute() > 0) {
            config.setId(item.getReturnedRecord().getId());
           }
    }
    
    @Override
    public List<RecommendationConfig> listRecommendConfigs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhRecommendationConfigsRecord> query = context.selectQuery(Tables.EH_RECOMMENDATION_CONFIGS);
        if(queryBuilderCallback != null) {
            queryBuilderCallback.buildCondition(locator, query);
            }
        
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_RECOMMENDATION_CONFIGS.ID.gt(locator.getAnchor()));
            }
            
        query.addOrderBy(Tables.EH_RECOMMENDATION_CONFIGS.CREATE_TIME.desc());
        query.addLimit(count);
        List<RecommendationConfig> recommends = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, RecommendationConfig.class);
        });
        
        if(recommends.size() > 0) {
            locator.setAnchor(recommends.get(recommends.size() -1).getId());
            }
        
        return recommends;
    }
}
