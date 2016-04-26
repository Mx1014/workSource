package com.everhomes.promotion;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOpPromotionActivitiesDao;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;
import com.everhomes.server.schema.tables.records.EhOpPromotionActivitiesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class OpPromotionActivitieProviderImpl implements OpPromotionActivityProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOpPromotionActivitie(OpPromotionActivity obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOpPromotionActivities.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        obj.setId(id);
        prepareObj(obj);
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOpPromotionActivitie(OpPromotionActivity obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOpPromotionActivitie(OpPromotionActivity obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OpPromotionActivity getOpPromotionActivitieById(Long id) {
        try {
            OpPromotionActivity[] result = new OpPromotionActivity[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));

        result[0] = context.select().from(Tables.EH_OP_PROMOTION_ACTIVITIES)
            .where(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OpPromotionActivity.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<OpPromotionActivity> queryOpPromotionActivities(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));

        SelectQuery<EhOpPromotionActivitiesRecord> query = context.selectQuery(Tables.EH_OP_PROMOTION_ACTIVITIES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<OpPromotionActivity> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OpPromotionActivity.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(OpPromotionActivity obj) {
    }
}
