// @formatter:off
package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchVacationBalanceLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPunchVacationBalanceLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PunchVacationBalanceLogProviderImpl implements PunchVacationBalanceLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchVacationBalanceLog(PunchVacationBalanceLog punchVacationBalanceLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchVacationBalanceLogs.class));
		punchVacationBalanceLog.setId(id);
		if (null != UserContext.currentUserId()) {
			punchVacationBalanceLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchVacationBalanceLog.setCreatorUid(UserContext.current().getUser().getId());
			punchVacationBalanceLog.setUpdateTime(punchVacationBalanceLog.getCreateTime());
			punchVacationBalanceLog.setOperatorUid(punchVacationBalanceLog.getCreatorUid());
		}
		getReadWriteDao().insert(punchVacationBalanceLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchVacationBalanceLogs.class, null);
	}

	@Override
	public void updatePunchVacationBalanceLog(PunchVacationBalanceLog punchVacationBalanceLog) {
		assert (punchVacationBalanceLog.getId() != null);
		punchVacationBalanceLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchVacationBalanceLog.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(punchVacationBalanceLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchVacationBalanceLogs.class, punchVacationBalanceLog.getId());
	}

	@Override
	public PunchVacationBalanceLog findPunchVacationBalanceLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchVacationBalanceLog.class);
	}
	
	@Override
	public List<PunchVacationBalanceLog> listPunchVacationBalanceLog() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_VACATION_BALANCE_LOGS)
				.orderBy(Tables.EH_PUNCH_VACATION_BALANCE_LOGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchVacationBalanceLog.class));
	}

	@Override
	public List<PunchVacationBalanceLog> listPunchVacationBalanceLog(Long detailId, CrossShardListingLocator locator, int pageSize) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_PUNCH_VACATION_BALANCE_LOGS)
				.where(Tables.EH_PUNCH_VACATION_BALANCE_LOGS.DETAIL_ID.eq(detailId));
		if(locator != null && locator.getAnchor() != null)  {
			step = step.and(Tables.EH_PUNCH_VACATION_BALANCE_LOGS.ID.lt(locator.getAnchor()));
		}
		Result<Record> records = step.orderBy(Tables.EH_PUNCH_VACATION_BALANCE_LOGS.ID.desc()).limit(pageSize).fetch();
		if (records == null || records.size() == 0) {
			return null;
		}
		return records.map(r -> ConvertHelper.convert(r, PunchVacationBalanceLog.class));
	}

	private EhPunchVacationBalanceLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchVacationBalanceLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchVacationBalanceLogsDao getDao(DSLContext context) {
		return new EhPunchVacationBalanceLogsDao(context.configuration());
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
