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
import com.everhomes.server.schema.tables.daos.EhApprovalFlowsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalFlows;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalFlowProviderImpl implements ApprovalFlowProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalFlow(ApprovalFlow approvalFlow) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalFlows.class));
		approvalFlow.setId(id);
		getReadWriteDao().insert(approvalFlow);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalFlows.class, null);
	}

	@Override
	public void updateApprovalFlow(ApprovalFlow approvalFlow) {
		assert (approvalFlow.getId() != null);
		getReadWriteDao().update(approvalFlow);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalFlows.class, approvalFlow.getId());
	}

	@Override
	public ApprovalFlow findApprovalFlowById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalFlow.class);
	}
	
	@Override
	public List<ApprovalFlow> listApprovalFlow() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOWS)
				.orderBy(Tables.EH_APPROVAL_FLOWS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalFlow.class));
	}
	
	private EhApprovalFlowsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalFlowsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalFlowsDao getDao(DSLContext context) {
		return new EhApprovalFlowsDao(context.configuration());
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
