// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchVacationBalancesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchVacationBalances;
import com.everhomes.server.schema.tables.records.EhPunchVacationBalancesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Component
public class PunchVacationBalanceProviderImpl implements PunchVacationBalanceProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchVacationBalance(PunchVacationBalance punchVacationBalance) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchVacationBalances.class));
		punchVacationBalance.setId(id);
		if (null == punchVacationBalance.getCreatorUid()) {
			punchVacationBalance.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchVacationBalance.setCreatorUid(UserContext.currentUserId());
			punchVacationBalance.setUpdateTime(punchVacationBalance.getCreateTime());
			punchVacationBalance.setOperatorUid(punchVacationBalance.getCreatorUid());
		}
		getReadWriteDao().insert(punchVacationBalance);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchVacationBalances.class, null);
	}

	@Override
	public void updatePunchVacationBalance(PunchVacationBalance punchVacationBalance) {
		assert (punchVacationBalance.getId() != null);
		Long userId = UserContext.currentUserId();
		punchVacationBalance.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if (null != userId) {
			punchVacationBalance.setOperatorUid(UserContext.currentUserId());
		}
		getReadWriteDao().update(punchVacationBalance);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchVacationBalances.class, punchVacationBalance.getId());
	}

	@Override
	public PunchVacationBalance findPunchVacationBalanceById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchVacationBalance.class);
	}
	
	@Override
	public List<PunchVacationBalance> listPunchVacationBalance() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_VACATION_BALANCES)
				.orderBy(Tables.EH_PUNCH_VACATION_BALANCES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchVacationBalance.class));
	}

	@Override
	public PunchVacationBalance findPunchVacationBalanceByDetailId(Long id) {
		Record record = getReadOnlyContext().select().from(Tables.EH_PUNCH_VACATION_BALANCES)
				.where(Tables.EH_PUNCH_VACATION_BALANCES.DETAIL_ID.eq(id))
				.orderBy(Tables.EH_PUNCH_VACATION_BALANCES.ID.asc())
				.fetchAny();
		if (null == record) {
			return null;
		}
		return ConvertHelper.convert(record, PunchVacationBalance.class);
	}

	@Override
	public List<PunchVacationBalance> findPunchVacationBalanceByDetailIds(List<Long> detailIds) {
		if (CollectionUtils.isEmpty(detailIds)) {
			return Collections.emptyList();
		}
		DSLContext context = getReadOnlyContext();
		SelectQuery<EhPunchVacationBalancesRecord> query = context.selectQuery(Tables.EH_PUNCH_VACATION_BALANCES);
		query.addConditions(Tables.EH_PUNCH_VACATION_BALANCES.DETAIL_ID.in(detailIds));

		Result<EhPunchVacationBalancesRecord> result = query.fetch();
		if (result == null || result.isEmpty()) {
			return Collections.emptyList();
		}
		return result.map(r -> {
			return ConvertHelper.convert(r, PunchVacationBalance.class);
		});
	}

	private EhPunchVacationBalancesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchVacationBalancesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchVacationBalancesDao getDao(DSLContext context) {
		return new EhPunchVacationBalancesDao(context.configuration());
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
