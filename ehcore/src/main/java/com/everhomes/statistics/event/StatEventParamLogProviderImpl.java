// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventParamLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventParamLogs;
import com.everhomes.server.schema.tables.records.EhStatEventParamLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatEventParamLogProviderImpl implements StatEventParamLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventParamLog(StatEventParamLog statEventParamLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventParamLogs.class));
		statEventParamLog.setId(id);
		statEventParamLog.setCreateTime(DateUtils.currentTimestamp());
		rwDao().insert(statEventParamLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventParamLogs.class, id);
	}

	@Override
	public void updateStatEventParamLog(StatEventParamLog statEventParamLog) {
		// statEventParamLog.setUpdateTime(DateUtils.currentTimestamp());
		// statEventParamLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventParamLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventParamLogs.class, statEventParamLog.getId());
	}

	@Override
	public StatEventParamLog findStatEventParamLogById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventParamLog.class);
	}

    @Override
    public List<StatEventParamLog> findStatEventParamByEventLogId(Long eventLogId) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID.eq(eventLogId))
                .fetchInto(StatEventParamLog.class);
    }

    @Override
    public List<StatEventParamLog> listEventParamLog(Integer namespaceId, String eventName, String eventVersion, Timestamp minTime, Timestamp maxTime) {
        return context().select()
                .from(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .fetchInto(StatEventParamLog.class);
    }

    @Override
    public Map<String, Integer> countParamTotalCount(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime) {
        return context().select(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.count())
                .from(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .groupBy(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE)
                .fetchMap(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.count());
    }

    /*
     SELECT
	    subT.v1, subT.v2, COUNT(*)
        FROM
            (SELECT
                 aa.id, aa.event_name, aa.param_key AS p1, aa.string_value AS v1, tt.param_key AS p2, tt.string_value AS v2
             FROM eh_stat_event_param_logs aa
                 JOIN (SELECT
                           id, param_key, string_value, event_log_id
                       FROM eh_stat_event_param_logs
                       WHERE event_name = 'launchpad_on_news_flash_item_click' AND param_key = 'layoutId') AS tt
                     ON tt.event_log_id = aa.event_log_id
             WHERE aa.event_name = 'launchpad_on_news_flash_item_click' AND aa.param_key = 'newsToken') AS subT
        GROUP BY subT.v1, subT.v2;
    */
    @Override
    public Map<Map<String, String>, Integer> countParamTotalCount(Integer namespaceId, String eventName, String eventVersion, List<String> paramKeys, Timestamp minTime, Timestamp maxTime) {

        DSLContext context = context();

        SelectQuery<Record> baseQuery = context.selectQuery();

        Table<EhStatEventParamLogsRecord> aa = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("aa");
        SelectQuery<EhStatEventParamLogsRecord> query = context.selectFrom(aa).getQuery();
        query.addSelect(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME));
        query.addSelect(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID));

        Table<EhStatEventParamLogsRecord> ttt = query.asTable("ttt");

        for (int i = 1; i < paramKeys.size(); i++) {
            String paramKey = paramKeys.get(i);
            SelectQuery<EhStatEventParamLogsRecord> subQuery = context.selectFrom(Tables.EH_STAT_EVENT_PARAM_LOGS).getQuery();

            // Table<EhStatEventParamLogsRecord> tt = subQuery.asTable("tt");
            Table<EhStatEventParamLogsRecord> subI = subQuery.asTable("sub" + i);

            Field<String> pk = subI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey);
            query.addSelect(pk);

            Field<String> pv = subI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey + "_value");
            query.addSelect(pv);

            ttt = query.asTable("ttt");

            Field<String> field = ttt.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey + "_value"));

            baseQuery.addSelect(field);
            baseQuery.addGroupBy(field);

            // baseQuery.addSelect(query.field(subQuery.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey+"_value")));

            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE);

            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime));

            aa = query.asTable("aa");

            Field<Long> subQueryEventLogIdField = aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);

            query.addJoin(subI, subI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID).eq(subQueryEventLogIdField));
        }

        String paramKey = paramKeys.get(0);
        aa = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("aa");
        query.addConditions(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID).eq(namespaceId));
        query.addConditions(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME).eq(eventName));
        query.addConditions(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION).eq(eventVersion));
        query.addConditions(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).eq(paramKey));
        query.addConditions(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME).between(minTime, maxTime));

        query.addSelect(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey));
        query.addSelect(aa.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey+"_value"));

        ttt = query.asTable("ttt");

        baseQuery.addSelect(ttt.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));
        baseQuery.addGroupBy(ttt.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));

        baseQuery.addSelect(DSL.count().as("totalCount"));
        baseQuery.addFrom(ttt);

        Map<Map<String, String>, Integer> map = new HashMap<>();
        baseQuery.fetch().map(r -> {
            Map<String, String> subMap = new HashMap<>();
            for (String key : paramKeys) {
                subMap.put(key, r.getValue(key+"_value").toString());
            }
            map.put(subMap, r.getValue(DSL.count().as("totalCount")));
            return null;
        });
        return map;
    }

    @Override
    public Map<String, Integer> countDistinctSession(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime) {
        return context().select(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID))
                .from(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .groupBy(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE)
                .fetchMap(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID));
    }

    @Override
    public Map<String, Integer> countDistinctUid(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime) {
        return context().select(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.UID))
                .from(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .groupBy(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE)
                .fetchMap(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE, DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.UID));
    }

    // @Override
	// public List<StatEventParamLog> listStatEventParamLog() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_PARAM_LOGS)
	//			.orderBy(Tables.EH_STAT_EVENT_PARAM_LOGS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventParamLog.class));
	// }
	
	private EhStatEventParamLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventParamLogsDao(context.configuration());
	}

	private EhStatEventParamLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventParamLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
