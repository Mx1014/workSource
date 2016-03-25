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

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorCommandDao;
import com.everhomes.server.schema.tables.pojos.EhDoorCommand;
import com.everhomes.server.schema.tables.records.EhDoorCommandRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class DoorCommandProviderImpl implements DoorCommandProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDoorCommand(DoorCommand obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorCommand.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        obj.setId(id);
        prepareObj(obj);
        EhDoorCommandDao dao = new EhDoorCommandDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorCommand(DoorCommand obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhDoorCommandDao dao = new EhDoorCommandDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteDoorCommand(DoorCommand obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhDoorCommandDao dao = new EhDoorCommandDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public DoorCommand getDoorCommandById(Long id) {
        try {
            DoorCommand[] result = new DoorCommand[1];

            dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
                (DSLContext context, Object reducingContext) -> {
                    result[0] = context.select().from(Tables.EH_DOOR_COMMAND)
                        .where(Tables.EH_DOOR_COMMAND.ID.eq(id))
                        .fetchAny().map((r) -> {
                            return ConvertHelper.convert(r, DoorCommand.class);
                        });

                    if (result[0] != null) {
                        return false;
                    } else {
                        return true;
                    }
                });

            return result[0];            
        } catch (Exception ex) {
            //TODO fetchAny() maybe return null
            return null;
        }

    }

    @Override
    public List<DoorCommand> queryDoorCommandByDoorId(ListingLocator locator, Long refId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDoorAccess.class, refId));

        SelectQuery<EhDoorCommandRecord> query = context.selectQuery(Tables.EH_DOOR_COMMAND);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_DOOR_COMMAND.DOOR_ID.eq(refId));

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_DOOR_COMMAND.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<DoorCommand> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorCommand.class);
        });
         
         if(objs.size() >= count) {
             locator.setAnchor(objs.get(objs.size() - 1).getId());
         } else {
             locator.setAnchor(null);
         }
         
         return objs;
    }

    @Override
    public List<DoorCommand> queryDoorCommands(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<DoorCommand> objs = new ArrayList<DoorCommand>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhDoorCommandRecord> query = context.selectQuery(Tables.EH_DOOR_COMMAND);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_DOOR_COMMAND.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_DOOR_COMMAND.ID.asc());
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, DoorCommand.class));
                return null;
            });

            if(objs.size() >= count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;

        });
        return objs;
    }

    private void prepareObj(DoorCommand obj) {
    }
    
    @Override
    public List<DoorCommand> queryValidDoorCommands(ListingLocator locator, Long doorId, int count) {
        return queryDoorCommandByDoorId(locator, doorId, count, new ListingQueryBuilderCallback() {
            
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_COMMAND.STATUS.eq(DoorCommandStatus.CREATING.getCode())
                        .or(Tables.EH_DOOR_COMMAND.STATUS.eq(DoorCommandStatus.SENDING.getCode())));
                return query;
            }
            
        });
    }
    
    @Override
    public DoorCommand queryActiveDoorCommand(Long doorId) {
        ListingLocator locator = new ListingLocator();
        List<DoorCommand> cmds = queryDoorCommandByDoorId(locator, doorId, 1, new ListingQueryBuilderCallback() {
            
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_COMMAND.STATUS.eq(DoorCommandStatus.CREATING.getCode())
                        .or(Tables.EH_DOOR_COMMAND.STATUS.eq(DoorCommandStatus.SENDING.getCode())));
                query.addConditions(Tables.EH_DOOR_COMMAND.CMD_ID.eq(AclinkCommandType.INIT_SERVER_KEY.getCode()));
                return query;
            }
            
        });
        
        if(cmds == null || cmds.size() == 0) {
            return null;
        }
        
        return cmds.get(0);
    }
    
}
