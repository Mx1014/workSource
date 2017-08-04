// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventContentLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventContentLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class StatEventContentLogProviderImpl implements StatEventContentLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventLogContent(StatEventLogContent statEventLogContent) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventContentLogs.class));
		statEventLogContent.setId(id);
		statEventLogContent.setCreateTime(DateUtils.currentTimestamp());
		// statEventLogContent.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventLogContent);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventContentLogs.class, id);
	}

	@Override
	public void updateStatEventLogContent(StatEventLogContent statEventLogContent) {
		// statEventLogContent.setUpdateTime(DateUtils.currentTimestamp());
		// statEventLogContent.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventLogContent);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventContentLogs.class, statEventLogContent.getId());
	}

	@Override
	public StatEventLogContent findStatEventLogContentById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventLogContent.class);
	}

    @Override
    public List<StatEventLogContent> listEventLogContent(byte status, Timestamp minTime, Timestamp maxTime) {
        return context().selectFrom(Tables.EH_STAT_EVENT_CONTENT_LOGS)
                .where(Tables.EH_STAT_EVENT_CONTENT_LOGS.STATUS.eq(status))
                .and(Tables.EH_STAT_EVENT_CONTENT_LOGS.CREATE_TIME.between(minTime, maxTime))
                .fetchInto(StatEventLogContent.class);
    }

    // @Override
	// public List<StatEventLogContent> listStatEventLogContent() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_LOG_CONTENTS)
	//			.orderBy(Tables.EH_STAT_EVENT_LOG_CONTENTS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventLogContent.class));
	// }
	
	private EhStatEventContentLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventContentLogsDao(context.configuration());
	}

	private EhStatEventContentLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventContentLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
