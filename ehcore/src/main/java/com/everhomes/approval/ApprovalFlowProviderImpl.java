// @formatter:off
package com.everhomes.approval;

import java.util.ArrayList;
import java.util.List;

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
import com.everhomes.rest.approval.CommonStatus;
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
	
	@Override
	public List<ApprovalFlow> listApprovalFlow(Integer namespaceId, String ownerType, Long ownerId, Long pageAnchor,
			int pageSize) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOWS)
												.where(Tables.EH_APPROVAL_FLOWS.NAMESPACE_ID.eq(namespaceId))
												.and(Tables.EH_APPROVAL_FLOWS.OWNER_TYPE.eq(ownerType))
												.and(Tables.EH_APPROVAL_FLOWS.OWNER_ID.eq(ownerId))
												.and(Tables.EH_APPROVAL_FLOWS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		
		if (pageAnchor != null) {
			step = step.and(Tables.EH_APPROVAL_FLOWS.ID.lt(pageAnchor));
		}
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_FLOWS.ID.desc())
									.limit(pageSize)
									.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalFlow.class));
		}
		
		return new ArrayList<ApprovalFlow>();
	}

	@Override
	public List<ApprovalFlow> listApprovalFlow(Integer namespaceId, String ownerType, Long ownerId) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOWS)
									.where(Tables.EH_APPROVAL_FLOWS.NAMESPACE_ID.eq(namespaceId))
									.and(Tables.EH_APPROVAL_FLOWS.OWNER_TYPE.eq(ownerType))
									.and(Tables.EH_APPROVAL_FLOWS.OWNER_ID.eq(ownerId))
									.and(Tables.EH_APPROVAL_FLOWS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
									.orderBy(Tables.EH_APPROVAL_FLOWS.ID.desc())
									.fetch();
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalFlow.class));
		}

		return new ArrayList<ApprovalFlow>();
	}

	@Override
	public ApprovalFlow findApprovalFlowByName(Integer namespaceId, String ownerType, Long ownerId, String name) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOWS)
							.where(Tables.EH_APPROVAL_FLOWS.NAMESPACE_ID.eq(namespaceId))
							.and(Tables.EH_APPROVAL_FLOWS.OWNER_TYPE.eq(ownerType))
							.and(Tables.EH_APPROVAL_FLOWS.OWNER_ID.eq(ownerId))
							.and(Tables.EH_APPROVAL_FLOWS.NAME.eq(name))
							.and(Tables.EH_APPROVAL_FLOWS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
							.limit(1)
							.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalFlow.class);
		}
		
		return null;
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

	@Override
	public void deleteApprovalFlows(List<Long> flowIds) {
		getReadWriteContext().delete(Tables.EH_APPROVAL_FLOWS)
		.where(Tables.EH_APPROVAL_FLOWS.ID.in(flowIds)) 
		.execute();
	}
}
