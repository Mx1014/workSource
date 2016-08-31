// @formatter:off
package com.everhomes.approval;

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
import com.everhomes.server.schema.tables.daos.EhApprovalTimeRangesDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalTimeRanges;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalTimeRangeProviderImpl implements ApprovalTimeRangeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalTimeRange(ApprovalTimeRange approvalTimeRange) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalTimeRanges.class));
		approvalTimeRange.setId(id);
		getReadWriteDao().insert(approvalTimeRange);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalTimeRanges.class, null);
	}

	@Override
	public void updateApprovalTimeRange(ApprovalTimeRange approvalTimeRange) {
		assert (approvalTimeRange.getId() != null);
		getReadWriteDao().update(approvalTimeRange);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalTimeRanges.class, approvalTimeRange.getId());
	}

	@Override
	public ApprovalTimeRange findApprovalTimeRangeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalTimeRange.class);
	}
	
	@Override
	public List<ApprovalTimeRange> listApprovalTimeRange() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_TIME_RANGES)
				.orderBy(Tables.EH_APPROVAL_TIME_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalTimeRange.class));
	}
	
	private EhApprovalTimeRangesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalTimeRangesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalTimeRangesDao getDao(DSLContext context) {
		return new EhApprovalTimeRangesDao(context.configuration());
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
