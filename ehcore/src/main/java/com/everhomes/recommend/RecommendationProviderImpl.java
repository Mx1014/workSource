package com.everhomes.recommend;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhRecommendationsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class RecommendationProviderImpl implements RecommendationProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    public void createRecommendation(Recommendation recommend) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhRecommendationsRecord> item = context.insertQuery(Tables.EH_RECOMMENDATIONS);
        item.setRecord(ConvertHelper.convert(recommend, EhRecommendationsRecord.class));
        
        item.setReturning(Tables.EH_RECOMMENDATIONS.ID);
        if(item.execute() > 0) {
            recommend.setId(item.getReturnedRecord().getId());
           }
    }
    
    
}
