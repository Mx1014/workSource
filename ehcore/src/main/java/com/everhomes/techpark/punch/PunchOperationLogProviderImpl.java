// @formatter:off
package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchOperationLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPunchOperationLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PunchOperationLogProviderImpl implements PunchOperationLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchOperationLog(PunchOperationLog punchOperationLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchOperationLogs.class));
		punchOperationLog.setId(id);
		punchOperationLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchOperationLog.setCreatorUid(UserContext.current().getUser().getId());
		punchOperationLog.setUpdateTime(punchOperationLog.getCreateTime());
		punchOperationLog.setOperatorUid(punchOperationLog.getCreatorUid());
		getReadWriteDao().insert(punchOperationLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchOperationLogs.class, null);
	}

	@Override
	public void updatePunchOperationLog(PunchOperationLog punchOperationLog) {
		assert (punchOperationLog.getId() != null);
		punchOperationLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchOperationLog.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(punchOperationLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchOperationLogs.class, punchOperationLog.getId());
	}

	@Override
	public PunchOperationLog findPunchOperationLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchOperationLog.class);
	}
	
	@Override
	public List<PunchOperationLog> listPunchOperationLog() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_OPERATION_LOGS)
				.orderBy(Tables.EH_PUNCH_OPERATION_LOGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchOperationLog.class));
	}
	
	private EhPunchOperationLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchOperationLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchOperationLogsDao getDao(DSLContext context) {
		return new EhPunchOperationLogsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
