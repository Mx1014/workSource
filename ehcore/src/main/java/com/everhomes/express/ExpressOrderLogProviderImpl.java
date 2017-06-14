// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressOrderLogsDao;
import com.everhomes.server.schema.tables.pojos.EhExpressOrderLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressOrderLogProviderImpl implements ExpressOrderLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressOrderLog(ExpressOrderLog expressOrderLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressOrderLogs.class));
		expressOrderLog.setId(id);
		expressOrderLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressOrderLog.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(expressOrderLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressOrderLogs.class, null);
	}

	@Override
	public void updateExpressOrderLog(ExpressOrderLog expressOrderLog) {
		assert (expressOrderLog.getId() != null);
		getReadWriteDao().update(expressOrderLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressOrderLogs.class, expressOrderLog.getId());
	}

	@Override
	public ExpressOrderLog findExpressOrderLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressOrderLog.class);
	}
	
	@Override
	public List<ExpressOrderLog> listExpressOrderLog() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_ORDER_LOGS)
				.orderBy(Tables.EH_EXPRESS_ORDER_LOGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressOrderLog.class));
	}
	
	private EhExpressOrderLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressOrderLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressOrderLogsDao getDao(DSLContext context) {
		return new EhExpressOrderLogsDao(context.configuration());
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
