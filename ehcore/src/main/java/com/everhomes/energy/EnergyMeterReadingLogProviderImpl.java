package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterReadingLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.server.schema.tables.records.EhEnergyMeterReadingLogsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class EnergyMeterReadingLogProviderImpl implements EnergyMeterReadingLogProvider {

    @Autowired
    private DbProvider dbProvider;

    // @Autowired
    // private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyMeterReadingLog(EnergyMeterReadingLog log) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterReadingLogs.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        log.setId(id);
        log.setCreatorUid(UserContext.current().getUser().getId());
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        prepareObj(log);
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.insert(log);
        return id;
    }

    /*@Override
    public void updateEnergyMeterReadingLog(EnergyMeterReadingLog obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.update(obj);
    }*/

    @Override
    public void deleteEnergyMeterReadingLog(EnergyMeterReadingLog log) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.delete(log);
    }

    @Override
    public EnergyMeterReadingLog getEnergyMeterReadingLogById(Long id) {
        try {
        EnergyMeterReadingLog[] result = new EnergyMeterReadingLog[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_METER_READING_LOGS)
            .where(Tables.EH_ENERGY_METER_READING_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyMeterReadingLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyMeterReadingLog> queryEnergyMeterReadingLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyMeterReadingLogsRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_READING_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.ID.gt(locator.getAnchor()));
            }

        query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));
        query.addLimit(count);
        List<EnergyMeterReadingLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyMeterReadingLog.class);
        });

        return objs;
    }

    @Override
    public List<EnergyMeterReadingLog> listMeterReadingLogByDate(Long meterId, Timestamp startBegin, Timestamp endBegin) {
    	 DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());

         SelectQuery<EhEnergyMeterReadingLogsRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_READING_LOGS);
 
         query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(meterId)); 
         query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.between(startBegin , endBegin)); 
         query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));
        return query.fetch().map((r) -> ConvertHelper.convert(r, EnergyMeterReadingLog.class));
    }

    @Override
    public EnergyMeterReadingLog getLastMeterReadingLogByDate(Long id, Timestamp startBegin, Timestamp endBegin) {
    	try {
            EnergyMeterReadingLog[] result = new EnergyMeterReadingLog[1];
            DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
            SelectConditionStep<Record> step =  context.select().from(Tables.EH_ENERGY_METER_READING_LOGS)
            .where(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(id))
            .and(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));
            if(null != startBegin)
            	step = step.and(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.greaterOrEqual(startBegin));
            if(null != endBegin)
            	step = step.and(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.lessOrEqual(endBegin));
            result[0] = step.orderBy(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.desc()).fetchAny().map((r) -> {
                    return ConvertHelper.convert(r, EnergyMeterReadingLog.class);
                });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyMeterReadingLog> listMeterReadingLogs(long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.ID.ge(pageAnchor))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.asc())
                .limit(pageSize).fetchInto(EnergyMeterReadingLog.class);
    }

    @Override
    public EnergyMeterReadingLog findLastReadingLogByMeterId(Integer namespaceId, Long meterId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.desc())
                .fetchAnyInto(EnergyMeterReadingLog.class);
    }

    @Override
    public List<EnergyMeterReadingLog> listMeterReadingLogsByMeterId(Integer namespaceId, Long meterId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetchInto(EnergyMeterReadingLog.class);
    }

    @Override
    public void deleteMeterReadingLogsByMeterId(Integer namespaceId, Long meterId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(meterId))
                .execute();
    }

    /*@Override
    public List<EnergyMeterReadingLog> listMeterReadingLogs(Integer namespaceId, Long meterId, Long pageAnchor, int pageSize) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyMeterReadingLogsRecord> query = context.selectFrom(Tables.EH_ENERGY_METER_READING_LOGS)
                .where(Tables.EH_ENERGY_METER_READING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .and(Tables.EH_ENERGY_METER_READING_LOGS.METER_ID.eq(meterId)).getQuery();
        if (pageAnchor != null) {
            query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.le(new Timestamp(pageAnchor)));
        }
        query.addOrderBy(Tables.EH_ENERGY_METER_READING_LOGS.CREATE_TIME.desc());
        query.addLimit(pageSize);
        return query.fetchInto(EnergyMeterReadingLog.class);
    }*/

    private void prepareObj(EnergyMeterReadingLog log) {

    }
}
