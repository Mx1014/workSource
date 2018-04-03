package com.everhomes.aclink;

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

import com.everhomes.rest.aclink.AclinkLogDTO;
import com.everhomes.rest.aclink.AclinkQueryLogResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinkLogsDao;
import com.everhomes.server.schema.tables.pojos.EhAclinkLogs;
import com.everhomes.server.schema.tables.records.EhAclinkLogsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AclinkLogProviderImpl implements AclinkLogProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAclinkLog(AclinkLog obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkLogs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        obj.setId(id);
        prepareObj(obj);
        EhAclinkLogsDao dao = new EhAclinkLogsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAclinkLog(AclinkLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        EhAclinkLogsDao dao = new EhAclinkLogsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAclinkLog(AclinkLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        EhAclinkLogsDao dao = new EhAclinkLogsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AclinkLog getAclinkLogById(Long id) {
        try {
        AclinkLog[] result = new AclinkLog[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));

        result[0] = context.select().from(Tables.EH_ACLINK_LOGS)
            .where(Tables.EH_ACLINK_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, AclinkLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<AclinkLog> queryAclinkLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));

        SelectQuery<EhAclinkLogsRecord> query = context.selectQuery(Tables.EH_ACLINK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_LOGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AclinkLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkLog.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
    
    @Override
    public List<AclinkLog> queryAclinkLogsByTime(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));

        SelectQuery<EhAclinkLogsRecord> query = context.selectQuery(Tables.EH_ACLINK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addOrderBy(Tables.EH_ACLINK_LOGS.ID.desc());
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_LOGS.ID.lt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AclinkLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkLog.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(AclinkLog obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
