package com.everhomes.promotion;

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
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.promotion.OpPromotionOwnerType;
import com.everhomes.rest.promotion.ScheduleTaskResourceType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhScheduleTaskLogsDao;
import com.everhomes.server.schema.tables.pojos.EhScheduleTaskLogs;
import com.everhomes.server.schema.tables.records.EhScheduleTaskLogsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ScheduleTaskLogProviderImpl implements ScheduleTaskLogProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createScheduleTaskLog(ScheduleTaskLog obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhScheduleTaskLogs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTaskLogs.class));
        obj.setId(id);
        prepareObj(obj);
        EhScheduleTaskLogsDao dao = new EhScheduleTaskLogsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateScheduleTaskLog(ScheduleTaskLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTaskLogs.class));
        EhScheduleTaskLogsDao dao = new EhScheduleTaskLogsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteScheduleTaskLog(ScheduleTaskLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTaskLogs.class));
        EhScheduleTaskLogsDao dao = new EhScheduleTaskLogsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ScheduleTaskLog getScheduleTaskLogById(Long id) {
        try {
        ScheduleTaskLog[] result = new ScheduleTaskLog[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTaskLogs.class));

        result[0] = context.select().from(Tables.EH_SCHEDULE_TASK_LOGS)
            .where(Tables.EH_SCHEDULE_TASK_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ScheduleTaskLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ScheduleTaskLog> queryScheduleTaskLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTaskLogs.class));

        SelectQuery<EhScheduleTaskLogsRecord> query = context.selectQuery(Tables.EH_SCHEDULE_TASK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SCHEDULE_TASK_LOGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ScheduleTaskLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ScheduleTaskLog.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(ScheduleTaskLog obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override 
    public ScheduleTaskLog findScheduleTaskLogByUser(Long promotionId, Long userId) {
        ListingLocator locator = new ListingLocator();
        int count = 1;
        
        List<ScheduleTaskLog> taskLogs = this.queryScheduleTaskLogs(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SCHEDULE_TASK_LOGS.RESOURCE_ID.eq(promotionId));
                query.addConditions(Tables.EH_SCHEDULE_TASK_LOGS.RESOURCE_TYPE.eq(ScheduleTaskResourceType.PROMOTION_ACTIVITY.getCode()));
                query.addConditions(Tables.EH_SCHEDULE_TASK_LOGS.OWNER_ID.eq(userId));
                query.addConditions(Tables.EH_SCHEDULE_TASK_LOGS.OWNER_TYPE.eq(OpPromotionOwnerType.USER.getCode()));
                return query;
            }
            
        });
        
        if(taskLogs != null && taskLogs.size() > 0) {
            return taskLogs.get(0);
        }
        
        return null;
    }
}

