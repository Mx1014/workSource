// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhStatEventTaskLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventTaskLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

import java.sql.Date;

@Repository
public class StatEventTaskLogProviderImpl implements StatEventTaskLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOrUpdateStatEventTaskLog(StatEventTaskLog statEventTaskLog) {
        if (statEventTaskLog.getId() == null) {
            Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventTaskLogs.class));
            statEventTaskLog.setId(id);
            statEventTaskLog.setCreateTime(DateUtils.currentTimestamp());
            rwDao().insert(statEventTaskLog);
            DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventTaskLogs.class, id);
        } else {
            rwDao().update(statEventTaskLog);
            statEventTaskLog.setUpdateTime(DateUtils.currentTimestamp());
            DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventTaskLogs.class, statEventTaskLog.getId());
        }
	}

	@Override
	public void updateStatEventTaskLog(StatEventTaskLog statEventTaskLog) {
		// statEventTaskLog.setUpdateTime(DateUtils.currentTimestamp());
		// statEventTaskLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventTaskLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventTaskLogs.class, statEventTaskLog.getId());
	}

	@Override
	public StatEventTaskLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventTaskLog.class);
	}

    @Override
    public StatEventTaskLog findByTaskDate(Date taskDate) {
        return context().selectFrom(Tables.EH_STAT_EVENT_TASK_LOGS)
                .where(Tables.EH_STAT_EVENT_TASK_LOGS.TASK_DATE.eq(taskDate))
                .fetchAnyInto(StatEventTaskLog.class);
    }

    private EhStatEventTaskLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventTaskLogsDao(context.configuration());
	}

	private EhStatEventTaskLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventTaskLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
