package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.aclink.DoorAuthLevelDTO;
import com.everhomes.rest.aclink.DoorAuthStatus;
import com.everhomes.rest.aclink.ListDoorAuthLevelCommand;
import com.everhomes.rest.aclink.ListDoorAuthLevelResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorAuthLevelDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAuthLevel;
import com.everhomes.server.schema.tables.records.EhDoorAuthLevelRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class DoorAuthLevelProviderImpl implements DoorAuthLevelProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDoorAuthLevel(DoorAuthLevel obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAuthLevel.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLevel.class));
        obj.setId(id);
        prepareObj(obj);
        EhDoorAuthLevelDao dao = new EhDoorAuthLevelDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorAuthLevel(DoorAuthLevel obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLevel.class));
        EhDoorAuthLevelDao dao = new EhDoorAuthLevelDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteDoorAuthLevel(DoorAuthLevel obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLevel.class));
        EhDoorAuthLevelDao dao = new EhDoorAuthLevelDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public DoorAuthLevel getDoorAuthLevelById(Long id) {
        try {
        DoorAuthLevel[] result = new DoorAuthLevel[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLevel.class));

        result[0] = context.select().from(Tables.EH_DOOR_AUTH_LEVEL)
            .where(Tables.EH_DOOR_AUTH_LEVEL.ID.eq(id).and(Tables.EH_DOOR_AUTH_LEVEL.STATUS.eq(DoorAuthStatus.VALID.getCode())))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, DoorAuthLevel.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<DoorAuthLevel> queryDoorAuthLevels(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLevel.class));

        SelectQuery<EhDoorAuthLevelRecord> query = context.selectQuery(Tables.EH_DOOR_AUTH_LEVEL);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<DoorAuthLevel> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorAuthLevel.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
    
    public DoorAuthLevel findAuthLevel(Long levelId, Byte levelType, Long doorId) {     
        List<DoorAuthLevel> lvls = queryDoorAuthLevels(new ListingLocator(), 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_ID.eq(levelId));
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_TYPE.eq(levelType));
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                return query;
            }
            
        });
        
        if(lvls != null && lvls.size() > 0) {
            return lvls.get(0);
        }
        
      return null;
    }
    
    @Override
    public ListDoorAuthLevelResponse findAuthLevels(ListDoorAuthLevelCommand cmd) {
        ListDoorAuthLevelResponse resp = new ListDoorAuthLevelResponse();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<DoorAuthLevel> lvls = queryDoorAuthLevels(locator, cmd.getPageSize(), new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.DOOR_ID.eq(cmd.getDoorId()));
                if(cmd.getLevelId() != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_ID.eq(cmd.getLevelId()));
                    query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_TYPE.eq(cmd.getLevelType()));    
                }
                
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                return query;
            }
            
        });
        
        if(lvls != null && lvls.size() > 0) {
            List<DoorAuthLevelDTO> dtos = lvls.stream().map((r)-> {
                return ConvertHelper.convert(r, DoorAuthLevelDTO.class);
            }).collect(Collectors.toList());
            
            resp.setDtos(dtos);
        }
        
        resp.setNextPageAnchor(locator.getAnchor());
        
        return resp;
        
    }
    
    @Override
    public List<DoorAuthLevel> findAuthLevels(Long levelId, Byte levelType) {
        List<DoorAuthLevel> lvls = queryDoorAuthLevels(new ListingLocator(), 1000, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_ID.eq(levelId));
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.LEVEL_TYPE.eq(levelType));
                query.addConditions(Tables.EH_DOOR_AUTH_LEVEL.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                return query;
            }
            
        });
        
      return lvls;
    }

    private void prepareObj(DoorAuthLevel obj) {
        obj.setStatus(DoorAuthStatus.VALID.getCode());
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
