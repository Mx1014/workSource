// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventParamLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventParamLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;

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
		// statEventParamLog.setCreatorUid(UserContext.currentUserId());
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
}
