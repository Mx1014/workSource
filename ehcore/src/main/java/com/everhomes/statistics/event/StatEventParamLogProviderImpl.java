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
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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
