// @formatter:off
package com.everhomes.promotion;


import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOpPromotions;
import com.everhomes.server.schema.tables.records.EhOpPromotionsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class OpPromotionProviderImpl implements OpPromotionProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpPromotionProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Override
    public List<OpPromotion> listOpPromotions(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        long startTime = System.currentTimeMillis();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOpPromotions.class));
        
        SelectQuery<EhOpPromotionsRecord> query = context.selectQuery(Tables.EH_OP_PROMOTIONS);
    
        if(queryBuilderCallback != null) {
            queryBuilderCallback.buildCondition(locator, query);
        }
            
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_OP_PROMOTIONS.ID.lt(locator.getAnchor()));
        }
        
        query.addOrderBy(Tables.EH_OP_PROMOTIONS.CREATE_TIME.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query operation promotion by count, sql=" + query.getSQL());
            LOGGER.debug("Query operation promotion by count, bindValues=" + query.getBindValues());
        }
        
        List<OpPromotion> promotions = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OpPromotion.class);
        });
        
        if(promotions.size() > 0) {
            locator.setAnchor(promotions.get(promotions.size() -1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Query operation promotion by count, resultSize=" + promotions.size() 
                + ", maxCount=" + count + ", elapse=" + (endTime - startTime));
        }
        
        return promotions;
    }
}
