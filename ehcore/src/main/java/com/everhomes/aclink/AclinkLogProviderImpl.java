package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.aclink.*;
import com.everhomes.server.schema.tables.EhDoorAccess;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import org.apache.poi.ddf.NullEscherSerializationListener;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinkLogsDao;
import com.everhomes.server.schema.tables.pojos.EhAclinkLogs;
import com.everhomes.server.schema.tables.records.EhAclinkLogsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
            query.addConditions(Tables.EH_ACLINK_LOGS.ID.ge(locator.getAnchor()));
            }
        
        //暂过滤掉物理按键开门的日志, by liuyilin 20180625
        query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.ne(AclinkLogEventType.BUTTON_OPEN.getCode()));
        
        if (count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkLog.class);
        });

        if(count > 0 && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
    
    @Override
    public List<AclinkLog> queryAclinkLogsByTime(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        SelectQuery<EhAclinkLogsRecord> query = context.selectQuery(Tables.EH_ACLINK_LOGS);
        query.addFrom(Tables.EH_ACLINK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addOrderBy(Tables.EH_ACLINK_LOGS.CREATE_TIME.desc());
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_LOGS.CREATE_TIME.le(new Timestamp(locator.getAnchor())));
            }

        //暂过滤掉物理按键开门的日志, by liuyilin 20180625
        query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.ne(AclinkLogEventType.BUTTON_OPEN.getCode()));

        if (count > 0){
        	query.addLimit(count + 1);
        }
        List<AclinkLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkLog.class);
        });

        if(count > 0 && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getCreateTime().getTime());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public List<AclinkLogDTO> queryAclinkLogDTOsByTime(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addOrderBy(Tables.EH_ACLINK_LOGS.CREATE_TIME.desc());
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_LOGS.CREATE_TIME.le(new Timestamp(locator.getAnchor())));
        }

        //暂过滤掉物理按键开门的日志, by liuyilin 20180625
        query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.ne(AclinkLogEventType.BUTTON_OPEN.getCode()));
        //auth_type
        query.addJoin(Tables.EH_DOOR_AUTH,Tables.EH_ACLINK_LOGS.AUTH_ID.eq(Tables.EH_DOOR_AUTH.ID));
        query.addConditions(Tables.EH_ACLINK_LOGS.AUTH_ID.ne(0L));
        if (count > 0){
            query.addLimit(count + 1);
        }
        List<AclinkLogDTO> objs = query.fetch().map((r) -> {
            AclinkLogDTO dto = ConvertHelper.convert(r, AclinkLogDTO.class);
            dto.setId(r.getValue(Tables.EH_ACLINK_LOGS.ID));
            dto.setCreateTime(r.getValue(Tables.EH_ACLINK_LOGS.CREATE_TIME));
            dto.setAuthType(r.getValue(Tables.EH_DOOR_AUTH.AUTH_TYPE));
            dto.setUserName(r.getValue(Tables.EH_ACLINK_LOGS.USER_NAME));
            dto.setUserIdentifier(r.getValue(Tables.EH_ACLINK_LOGS.USER_IDENTIFIER));
            dto.setDoorName(r.getValue(Tables.EH_ACLINK_LOGS.DOOR_NAME));
            dto.setEventType(r.getValue(Tables.EH_ACLINK_LOGS.EVENT_TYPE));
            dto.setLogTime(r.getValue(Tables.EH_ACLINK_LOGS.LOG_TIME));
            return dto;
        });

        if(count > 0 && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getCreateTime().getTime());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
//add by liqingyan 需要修改
    @Override
    public List<DoorStatisticDTO> queryDoorStatisticDTO (ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkLogs.class));
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addOrderBy(Tables.EH_ACLINK_LOGS.CREATE_TIME.desc());
        query.addJoin(Tables.EH_DOOR_AUTH,Tables.EH_ACLINK_LOGS.AUTH_ID.eq(Tables.EH_DOOR_AUTH.ID));
        query.addConditions(Tables.EH_ACLINK_LOGS.AUTH_ID.ne(0L));
        query.addSelect(Tables.EH_DOOR_AUTH.AUTH_TYPE);
        List<DoorStatisticDTO> objs = query.fetch().map((r) -> {
            DoorStatisticDTO dto = ConvertHelper.convert(r, DoorStatisticDTO.class);
            return dto;
        });
        return objs;
    }
    //add by liqingyan
    @Override
    public DoorStatisticDTO queryDoorStatistic(DoorStatisticCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        DoorStatisticDTO dto = new DoorStatisticDTO();
        com.everhomes.server.schema.tables.EhAclinkLogs t = Tables.EH_ACLINK_LOGS.as("t");
        EhDoorAccess t1 = Tables.EH_DOOR_ACCESS.as("t1");
        com.everhomes.server.schema.tables.EhDoorAuth t2 = Tables.EH_DOOR_AUTH.as("t2");

        Result<Record1<Integer>> rlt1 = context.select(t1.ID.count().as("door"))
                .from(t1).fetch();
        Result<Record1<Integer>> rlt2 = context.select(t.ID.count().as("open"))
                .from(t)
                .where(t.AUTH_ID.ne(0L))
                .fetch();
        Result<Record1<Integer>> rlt3 = context.select(t.AUTH_ID.count().as("temp"))
                .from(t,t2)
                .where(t.AUTH_ID.ne(0L))
                .and(t.AUTH_ID.eq(t2.ID))
                .and(t2.AUTH_TYPE.eq((byte)1)).fetch();
        Result<Record1<Integer>> rlt4 = context.select(t.AUTH_ID.count().as("perm"))
                .from(t).leftOuterJoin(t2).on(t.AUTH_ID.eq(t2.ID))
                .where(t.AUTH_ID.ne(0L))
                .and(t2.AUTH_TYPE.eq((byte)0)).fetch();
        Result<Record1<Integer>> rlt5 = context.select(t.AUTH_ID.count().as("bluetooth"))
                .from(t).leftOuterJoin(t2).on(t.AUTH_ID.eq(t2.ID))
                .where(t.AUTH_ID.ne(0L))
                .and(t.EVENT_TYPE.eq(0L)).fetch();
        Result<Record1<Integer>> rlt6 = context.select(t.AUTH_ID.count().as("qr"))
                .from(t).leftOuterJoin(t2).on(t.AUTH_ID.eq(t2.ID))
                .where(t.AUTH_ID.ne(0L))
                .and(t.EVENT_TYPE.eq(1L)).fetch();
        Result<Record1<Integer>> rlt7 = context.select(t.AUTH_ID.count().as("remote"))
                .from(t).leftOuterJoin(t2).on(t.AUTH_ID.eq(t2.ID))
                .where(t.AUTH_ID.ne(0L))
                .and(t.EVENT_TYPE.eq(2L)).fetch();
        Result<Record1<Integer>> rlt8 = context.select(t.AUTH_ID.count().as("face"))
                .from(t).leftOuterJoin(t2).on(t.AUTH_ID.eq(t2.ID))
                .where(t.AUTH_ID.ne(0L))
                .and(t.EVENT_TYPE.eq(3L)).fetch();
        dto.setActiveDoor(new Long((Integer)rlt1.get(0).getValue("door")));
        dto.setOpenTotal(new Long ((Integer)rlt2.get(0).getValue("open")));
        dto.setTempAuthTotal(new Long ((Integer)rlt3.get(0).getValue("temp")));
        dto.setPermAuthTotal(new Long ((Integer)rlt4.get(0).getValue("perm")));
        dto.setBluetoothTotal(new Long ((Integer)rlt5.get(0).getValue("bluetooth")));
        dto.setQrTotal(new Long ((Integer)rlt6.get(0).getValue("qr")));
        dto.setRemoteTotal(new Long ((Integer)rlt7.get(0).getValue("remote")));
        dto.setFaceTotal(new Long ((Integer)rlt8.get(0).getValue("face")));
        return dto;
    }
    //add by liqingyan
    @Override
    public List<DoorStatisticByTimeDTO> queryDoorStatisticByTime (DoorStatisticByTimeCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAclinkLogs t = Tables.EH_ACLINK_LOGS.as("t");
        com.everhomes.server.schema.tables.EhDoorAuth t2 = Tables.EH_DOOR_AUTH.as("t2");
        Condition condition = t.AUTH_ID.ne(0L);
        if(cmd.getStartTime() != null){
            condition = condition.and(t.CREATE_TIME.between(new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime())));
        }
        List<DoorStatisticByTimeDTO> dtos = new ArrayList<DoorStatisticByTimeDTO>();
        SelectHavingStep<Record2<Integer, Date>> groupBy = context.select(t.ID.count().as("num"),
                DSL.date(t.CREATE_TIME).as("d"))
                .from(t)
                .where(condition).groupBy(DSL.date(t.CREATE_TIME).as("d"));
        groupBy.fetch().map((r) -> {
            DoorStatisticByTimeDTO dto = new DoorStatisticByTimeDTO();
            dto.setCreateTime(r.getValue("d").toString());
            dto.setOpenNumber(Long.parseLong(r.getValue("num").toString()));
            dtos.add(dto);
            return null;
        });
        return dtos;
    }

    private void prepareObj(AclinkLog obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }

}
