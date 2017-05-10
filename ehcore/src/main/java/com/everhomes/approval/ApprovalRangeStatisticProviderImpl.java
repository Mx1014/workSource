package com.everhomes.approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
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
import com.everhomes.server.schema.tables.daos.EhApprovalRangeStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalRangeStatistics;
import com.everhomes.server.schema.tables.records.EhApprovalRangeStatisticsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ApprovalRangeStatisticProviderImpl implements ApprovalRangeStatisticProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createApprovalRangeStatistic(ApprovalRangeStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhApprovalRangeStatistics.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhApprovalRangeStatisticsDao dao = new EhApprovalRangeStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateApprovalRangeStatistic(ApprovalRangeStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhApprovalRangeStatisticsDao dao = new EhApprovalRangeStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteApprovalRangeStatistic(ApprovalRangeStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhApprovalRangeStatisticsDao dao = new EhApprovalRangeStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ApprovalRangeStatistic getApprovalRangeStatisticById(Long id) {
        try {
        ApprovalRangeStatistic[] result = new ApprovalRangeStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_APPROVAL_RANGE_STATISTICS)
            .where(Tables.EH_APPROVAL_RANGE_STATISTICS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ApprovalRangeStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }
    
    @Override
    public ApprovalRangeStatistic getApprovalRangeStatisticByParams(String punchMonth,String ownerType,Long ownerId,Long userId,Byte approvalType,Long categoryId) {
        try {
        ApprovalRangeStatistic[] result = new ApprovalRangeStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_APPROVAL_RANGE_STATISTICS)
            .where(Tables.EH_APPROVAL_RANGE_STATISTICS.PUNCH_MONTH.eq(punchMonth))
            .and(Tables.EH_APPROVAL_RANGE_STATISTICS.OWNER_TYPE.eq(ownerType))
            .and(Tables.EH_APPROVAL_RANGE_STATISTICS.OWNER_ID.eq(ownerId))
            .and(Tables.EH_APPROVAL_RANGE_STATISTICS.USER_ID.eq(userId))
            .and(Tables.EH_APPROVAL_RANGE_STATISTICS.APPROVAL_TYPE.eq(approvalType))
            .and(Tables.EH_APPROVAL_RANGE_STATISTICS.CATEGORY_ID.eq(categoryId)) 
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ApprovalRangeStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ApprovalRangeStatistic> queryApprovalRangeStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhApprovalRangeStatisticsRecord> query = context.selectQuery(Tables.EH_APPROVAL_RANGE_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_APPROVAL_RANGE_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ApprovalRangeStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ApprovalRangeStatistic.class);
        });

        return objs;
    }

    private void prepareObj(ApprovalRangeStatistic obj) {
    }
}