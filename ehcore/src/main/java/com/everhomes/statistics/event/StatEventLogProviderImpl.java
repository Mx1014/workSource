// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class StatEventLogProviderImpl implements StatEventLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventLog(StatEventLog statEventLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventLogs.class));
		statEventLog.setId(id);
		statEventLog.setCreateTime(DateUtils.currentTimestamp());
		// statEventLog.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventLogs.class, id);
	}

	@Override
	public void updateStatEventLog(StatEventLog statEventLog) {
		// statEventLog.setUpdateTime(DateUtils.currentTimestamp());
		// statEventLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventLogs.class, statEventLog.getId());
	}

	@Override
	public StatEventLog findStatEventLogById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventLog.class);
	}

    @Override
    public List<Long> listDeviceGenIdBySessionId(String sessionId) {
        return context().select(Tables.EH_STAT_EVENT_LOGS.DEVICE_GEN_ID)
                .from(Tables.EH_STAT_EVENT_LOGS)
                .where(Tables.EH_STAT_EVENT_LOGS.SESSION_ID.eq(sessionId))
                .fetchInto(Long.class);
    }

    @Override
    public List<StatEventLog> listEventLog(Timestamp minTime, Timestamp maxTime) {
        return context().select().from(Tables.EH_STAT_EVENT_LOGS)
                .where(Tables.EH_STAT_EVENT_LOGS.UPLOAD_TIME.between(minTime, maxTime))
                .fetchInto(StatEventLog.class);
    }

    // @Override
	// public List<StatEventLog> listStatEventLog() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_LOGS)
	//			.orderBy(Tables.EH_STAT_EVENT_LOGS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventLog.class));
	// }
	
	private EhStatEventLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventLogsDao(context.configuration());
	}

	private EhStatEventLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
