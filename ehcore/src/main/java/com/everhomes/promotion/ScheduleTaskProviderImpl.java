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
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhScheduleTasksDao;
import com.everhomes.server.schema.tables.pojos.EhScheduleTasks;
import com.everhomes.server.schema.tables.records.EhScheduleTasksRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ScheduleTaskProviderImpl implements ScheduleTaskProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createScheduleTask(ScheduleTask obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhScheduleTasks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTasks.class));
        obj.setId(id);
        prepareObj(obj);
        EhScheduleTasksDao dao = new EhScheduleTasksDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateScheduleTask(ScheduleTask obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTasks.class));
        EhScheduleTasksDao dao = new EhScheduleTasksDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteScheduleTask(ScheduleTask obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTasks.class));
        EhScheduleTasksDao dao = new EhScheduleTasksDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ScheduleTask getScheduleTaskById(Long id) {
        try {
        ScheduleTask[] result = new ScheduleTask[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTasks.class));

        result[0] = context.select().from(Tables.EH_SCHEDULE_TASKS)
            .where(Tables.EH_SCHEDULE_TASKS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ScheduleTask.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ScheduleTask> queryScheduleTasks(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhScheduleTasks.class));

        SelectQuery<EhScheduleTasksRecord> query = context.selectQuery(Tables.EH_SCHEDULE_TASKS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SCHEDULE_TASKS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ScheduleTask> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ScheduleTask.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(ScheduleTask obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public ScheduleTask getSchduleTaskByPromotionId(Long promotionId) {
        ListingLocator locator = new ListingLocator();
        List<ScheduleTask> tasks = this.queryScheduleTasks(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SCHEDULE_TASKS.RESOURCE_ID.eq(promotionId));
                return query;
            }
            
        });
        
        if(tasks != null && tasks.size() > 0) {
            return tasks.get(0);
        }
        
        return null;
    }
}
