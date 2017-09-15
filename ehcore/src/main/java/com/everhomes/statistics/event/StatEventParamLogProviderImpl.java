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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatEventParamLogProviderImpl implements StatEventParamLogProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatEventParamLogProviderImpl.class);

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
    public Map<String, StatEventCountDTO> countParamLogs(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime) {
        return context().select(
                Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE,
                DSL.count().as("totalCount"),
                DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID).as("completedSessions"),
                DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.UID).as("uniqueUsers")
        )
                .from(Tables.EH_STAT_EVENT_PARAM_LOGS)
                .where(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey))
                .and(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .groupBy(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE)
                .fetchMap(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE,
                        record -> {
                    Integer totalCount = record.getValue(DSL.count().as("totalCount"));
                    Integer completedSessions = record.getValue(DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID).as("completedSessions"));
                    Integer uniqueUsers = record.getValue(DSL.countDistinct(Tables.EH_STAT_EVENT_PARAM_LOGS.UID).as("uniqueUsers"));
                    StatEventCountDTO dto = new StatEventCountDTO();
                    dto.setTotalCount(totalCount);
                    dto.setCompletedSessions(completedSessions);
                    dto.setUniqueUsers(uniqueUsers);
                    return dto;
                });
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
    public Map<Map<String, String>, StatEventCountDTO> countParamLogs(Integer namespaceId, String eventName, String eventVersion, List<String> paramKeys, Timestamp minTime, Timestamp maxTime) {
        DSLContext context = context();

        SelectQuery<Record> baseQuery = context.selectQuery();

        Table<EhStatEventParamLogsRecord> tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        SelectQuery<EhStatEventParamLogsRecord> query = context.selectFrom(tableAlias).getQuery();
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID));

        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UID));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID));

        Table<EhStatEventParamLogsRecord> queryAlias;

        for (int i = 1; i < paramKeys.size(); i++) {
            String paramKey = paramKeys.get(i);
            SelectQuery<EhStatEventParamLogsRecord> subQuery = context.selectFrom(Tables.EH_STAT_EVENT_PARAM_LOGS).getQuery();

            Table<EhStatEventParamLogsRecord> subQueryI = subQuery.asTable("sub" + i);

            Field<String> pk = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey);
            query.addSelect(pk);

            Field<String> pv = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey + "_value");
            query.addSelect(pv);

            queryAlias = query.asTable("queryAlias");

            Field<String> field = queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey + "_value"));

            baseQuery.addSelect(field);
            baseQuery.addGroupBy(field);

            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE);

            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime));

            tableAlias = query.asTable("tableAlias");

            Field<Long> subQueryEventLogIdField = tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);

            query.addJoin(subQueryI, subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID).eq(subQueryEventLogIdField));
        }

        String paramKey = paramKeys.get(0);
        tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID).eq(namespaceId));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME).eq(eventName));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION).eq(eventVersion));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).eq(paramKey));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME).between(minTime, maxTime));

        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey+"_value"));

        queryAlias = query.asTable("queryAlias");

        baseQuery.addSelect(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));
        baseQuery.addGroupBy(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));

        Field<Integer> totalCountField = DSL.count().as("totalCount");
        Field<Integer> completedSessionsField = DSL.countDistinct(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID)).as("completedSessions");
        Field<Integer> uniqueUsersField = DSL.countDistinct(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UID)).as("uniqueUsers");

        baseQuery.addSelect(totalCountField);
        baseQuery.addSelect(completedSessionsField);
        baseQuery.addSelect(uniqueUsersField);

        baseQuery.addFrom(queryAlias);

        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("namespaceId = {}, eventName = {}, sql: {}", namespaceId, eventName, baseQuery.getSQL(true));
        // }

        Map<Map<String, String>, StatEventCountDTO> result = new HashMap<>();
        baseQuery.fetch().map(r -> {
            Map<String, String> subMap = new HashMap<>();
            for (String key : paramKeys) {
                subMap.put(key, r.getValue(key+"_value").toString());
            }

            StatEventCountDTO dto = new StatEventCountDTO();
            dto.setTotalCount(r.getValue(totalCountField));
            dto.setCompletedSessions(r.getValue(completedSessionsField));
            dto.setUniqueUsers(r.getValue(uniqueUsersField));

            result.put(subMap, dto);
            return null;
        });
        return result;
    }

    /*@Override
    public Map<Map<String, String>, Integer> countDistinctSession(Integer namespaceId, String eventName, String eventVersion, List<String> paramKeys, Timestamp minTime, Timestamp maxTime) {
        DSLContext context = context();

        SelectQuery<Record> baseQuery = context.selectQuery();

        Table<EhStatEventParamLogsRecord> tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        SelectQuery<EhStatEventParamLogsRecord> query = context.selectFrom(tableAlias).getQuery();
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID));
        Field<String> fieldSessionId = tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.SESSION_ID);
        query.addSelect(fieldSessionId);

        Table<EhStatEventParamLogsRecord> queryAlias;

        for (int i = 1; i < paramKeys.size(); i++) {
            String paramKey = paramKeys.get(i);
            SelectQuery<EhStatEventParamLogsRecord> subQuery = context.selectFrom(Tables.EH_STAT_EVENT_PARAM_LOGS).getQuery();

            Table<EhStatEventParamLogsRecord> subQueryI = subQuery.asTable("sub" + i);

            Field<String> pk = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey);
            query.addSelect(pk);

            Field<String> pv = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey + "_value");
            query.addSelect(pv);

            queryAlias = query.asTable("queryAlias");

            Field<String> field = queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey + "_value"));

            baseQuery.addSelect(field);
            baseQuery.addGroupBy(field);

            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE);

            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime));

            tableAlias = query.asTable("tableAlias");

            Field<Long> subQueryEventLogIdField = tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);

            query.addJoin(subQueryI, subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID).eq(subQueryEventLogIdField));
        }

        String paramKey = paramKeys.get(0);
        tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID).eq(namespaceId));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME).eq(eventName));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION).eq(eventVersion));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).eq(paramKey));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME).between(minTime, maxTime));

        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey+"_value"));

        queryAlias = query.asTable("queryAlias");

        baseQuery.addSelect(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));
        baseQuery.addGroupBy(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));

        baseQuery.addSelect(DSL.countDistinct(fieldSessionId).as("countDistinctSession"));
        baseQuery.addFrom(queryAlias);

        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("namespaceId = {}, eventName = {}, sql: {}", namespaceId, eventName, baseQuery.getSQL(true));
        // }

        Map<Map<String, String>, Integer> result = new HashMap<>();
        baseQuery.fetch().map(r -> {
            Map<String, String> subMap = new HashMap<>();
            for (String key : paramKeys) {
                subMap.put(key, r.getValue(key+"_value").toString());
            }
            result.put(subMap, r.getValue(DSL.countDistinct(fieldSessionId).as("countDistinctSession")));
            return null;
        });
        return result;
    }

    @Override
    public Map<Map<String, String>, Integer> countDistinctUid(Integer namespaceId, String eventName, String eventVersion, List<String> paramKeys, Timestamp minTime, Timestamp maxTime) {
        DSLContext context = context();

        SelectQuery<Record> baseQuery = context.selectQuery();

        Table<EhStatEventParamLogsRecord> tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        SelectQuery<EhStatEventParamLogsRecord> query = context.selectFrom(tableAlias).getQuery();
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID));
        Field<Long> fieldUid = tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UID);
        query.addSelect(fieldUid);

        Table<EhStatEventParamLogsRecord> queryAlias;

        for (int i = 1; i < paramKeys.size(); i++) {
            String paramKey = paramKeys.get(i);
            SelectQuery<EhStatEventParamLogsRecord> subQuery = context.selectFrom(Tables.EH_STAT_EVENT_PARAM_LOGS).getQuery();

            Table<EhStatEventParamLogsRecord> subQueryI = subQuery.asTable("sub" + i);

            Field<String> pk = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey);
            query.addSelect(pk);

            Field<String> pv = subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey + "_value");
            query.addSelect(pv);

            queryAlias = query.asTable("queryAlias");

            Field<String> field = queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey + "_value"));

            baseQuery.addSelect(field);
            baseQuery.addGroupBy(field);

            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY);
            subQuery.addSelect(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE);

            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID.eq(namespaceId));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME.eq(eventName));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION.eq(eventVersion));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY.eq(paramKey));
            subQuery.addConditions(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME.between(minTime, maxTime));

            tableAlias = query.asTable("tableAlias");

            Field<Long> subQueryEventLogIdField = tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID);

            query.addJoin(subQueryI, subQueryI.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_LOG_ID).eq(subQueryEventLogIdField));
        }

        String paramKey = paramKeys.get(0);
        tableAlias = Tables.EH_STAT_EVENT_PARAM_LOGS.asTable("tableAlias");

        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.NAMESPACE_ID).eq(namespaceId));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_NAME).eq(eventName));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.EVENT_VERSION).eq(eventVersion));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).eq(paramKey));
        query.addConditions(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.UPLOAD_TIME).between(minTime, maxTime));

        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.PARAM_KEY).as(paramKey));
        query.addSelect(tableAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE).as(paramKey+"_value"));

        queryAlias = query.asTable("queryAlias");

        baseQuery.addSelect(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));
        baseQuery.addGroupBy(queryAlias.field(Tables.EH_STAT_EVENT_PARAM_LOGS.STRING_VALUE.as(paramKey+"_value")));

        baseQuery.addSelect(DSL.countDistinct(fieldUid).as("countDistinctUid"));
        baseQuery.addFrom(queryAlias);

        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("namespaceId = {}, eventName = {}, sql: {}", namespaceId, eventName, baseQuery.getSQL(true));
        // }

        Map<Map<String, String>, Integer> result = new HashMap<>();
        baseQuery.fetch().map(r -> {
            Map<String, String> subMap = new HashMap<>();
            for (String key : paramKeys) {
                subMap.put(key, r.getValue(key+"_value").toString());
            }
            result.put(subMap, r.getValue(DSL.countDistinct(fieldUid).as("countDistinctUid")));
            return null;
        });
        return result;
    }*/
	
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
