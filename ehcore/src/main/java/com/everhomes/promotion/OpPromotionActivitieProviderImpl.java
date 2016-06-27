package com.everhomes.promotion;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.promotion.OpPromotionConditionType;
import com.everhomes.rest.promotion.OpPromotionStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOpPromotionActivitiesDao;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhOpPromotionActivitiesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OpPromotionActivitieProviderImpl implements OpPromotionActivityProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOpPromotionActivity(OpPromotionActivity obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOpPromotionActivities.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        obj.setId(id);
        prepareObj(obj);
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.insert(obj);
        
//        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOpPromotionActivities.class, id);
        return id;
    }

    @Override
    public void updateOpPromotionActivity(OpPromotionActivity obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.update(obj);
//        Change to service level
//        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOpPromotionActivities.class, obj.getId());
    }

    @Override
    public void deleteOpPromotionActivity(OpPromotionActivity obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionActivities.class));
        EhOpPromotionActivitiesDao dao = new EhOpPromotionActivitiesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OpPromotionActivity getOpPromotionActivityById(Long id) {
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
            query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.lt(locator.getAnchor()));
            }

        query.addLimit(count);
        query.addOrderBy(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.desc());
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
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public List<OpPromotionActivity> listOpPromotion(ListingLocator locator, int count) {
        return queryOpPromotionActivities(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.STATUS.ne(OpPromotionStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
    }
    
    @Override
    public List<OpPromotionActivity> listOpPromotionByPriceRange(ListingLocator locator, int count, Long value) {
        List<OpPromotionActivity> promotions = queryOpPromotionActivities(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.POLICY_TYPE.eq(OpPromotionConditionType.ORDER_RANGE_VALUE.getCode()));
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.INTEGRAL_TAG1.le(value));
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.INTEGRAL_TAG2.gt(value));
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.STATUS.ne(OpPromotionStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        return promotions;
    }
    
    @Override
    public List<OpPromotionActivity> searchOpPromotionByKeyword(ListingLocator locator, int count, String keyword) {
        
        Long id = 0l;
        try {
            id = Long.parseLong(keyword);
        } catch(Exception ex) {
            
        }
        
        final Long searchId = id;
        
        List<OpPromotionActivity> promotions = queryOpPromotionActivities(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.TITLE.like("%" + keyword + "%")
                        .or(Tables.EH_OP_PROMOTION_ACTIVITIES.DESCRIPTION.like("%" + keyword + "%"))
                        .or(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.eq(searchId)));
                
                query.addConditions(Tables.EH_OP_PROMOTION_ACTIVITIES.STATUS.ne(OpPromotionStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        return promotions;        
    }
}
