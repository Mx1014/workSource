// @formatter:off
package com.everhomes.approval;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalOpRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalOpRequests;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalOpRequestProviderImpl implements ApprovalOpRequestProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalOpRequest(ApprovalOpRequest approvalOpRequest) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalOpRequests.class));
		approvalOpRequest.setId(id);
		getReadWriteDao().insert(approvalOpRequest);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalOpRequests.class, null);
	}

	@Override
	public void updateApprovalOpRequest(ApprovalOpRequest approvalOpRequest) {
		assert (approvalOpRequest.getId() != null);
		getReadWriteDao().update(approvalOpRequest);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalOpRequests.class, approvalOpRequest.getId());
	}

	@Override
	public ApprovalOpRequest findApprovalOpRequestById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalOpRequest.class);
	}
	
	@Override
	public List<ApprovalOpRequest> listApprovalOpRequest() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_OP_REQUESTS)
				.orderBy(Tables.EH_APPROVAL_OP_REQUESTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalOpRequest.class));
	}
	
	@Override
	public List<ApprovalOpRequest> listApprovalOpRequestByRequestId(Long requestId) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_OP_REQUESTS)
				.where(Tables.EH_APPROVAL_OP_REQUESTS.REQUEST_ID.eq(requestId))
				.orderBy(Tables.EH_APPROVAL_OP_REQUESTS.ID.asc())
				.fetch();

		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalOpRequest.class));
		}
		
		return new ArrayList<ApprovalOpRequest>();
	}

	@Override
	public void deleteApprovalOpRequestByRequestId(Long requestId) {
		getReadWriteContext().delete(Tables.EH_APPROVAL_OP_REQUESTS).where(Tables.EH_APPROVAL_OP_REQUESTS.REQUEST_ID.eq(requestId)).execute();
	}

	private EhApprovalOpRequestsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalOpRequestsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalOpRequestsDao getDao(DSLContext context) {
		return new EhApprovalOpRequestsDao(context.configuration());
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
