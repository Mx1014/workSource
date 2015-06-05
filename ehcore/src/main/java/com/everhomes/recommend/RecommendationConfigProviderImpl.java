package com.everhomes.recommend;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
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
}
