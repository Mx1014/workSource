// @formatter:off
package com.everhomes.approval;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.ApprovalRequestCondition;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalRequests;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalRequestProviderImpl implements ApprovalRequestProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalRequest(ApprovalRequest approvalRequest) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalRequests.class));
		approvalRequest.setId(id);
		getReadWriteDao().insert(approvalRequest);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalRequests.class, null);
	}

	@Override
	public void updateApprovalRequest(ApprovalRequest approvalRequest) {
		assert (approvalRequest.getId() != null);
		getReadWriteDao().update(approvalRequest);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalRequests.class, approvalRequest.getId());
	}

	@Override
	public ApprovalRequest findApprovalRequestById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalRequest.class);
	}
	
	@Override
	public List<ApprovalRequest> listApprovalRequest() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.orderBy(Tables.EH_APPROVAL_REQUESTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalRequest.class));
	}
	
	@Override
	public List<ApprovalRequest> listApprovalRequestByCondition(ApprovalRequestCondition condition) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(condition.getNamespaceId()))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(condition.getOwnerType()))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(condition.getOwnerId()));
		
		if (condition.getApprovalType() != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(condition.getApprovalType()));
		}
		
		if (condition.getCategoryId() != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CATEGORY_ID.eq(condition.getCategoryId()));
		}
		
		if (condition.getCreatorUid() != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.eq(condition.getCreatorUid()));
		}
		
		if (condition.getPageAnchor() != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.ID.lt(condition.getPageAnchor()));
		}
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_REQUESTS.ID.desc())
									.limit(condition.getPageSize()+1)
									.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalRequest.class));
		}
		
		return new ArrayList<ApprovalRequest>();
	}

	@Override
	public List<ApprovalRequest> listApprovalRequestForWeb(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Long categoryId, Long fromDate, Long endDate, Byte queryType,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<User> userList, Long pageAnchor, int pageSize) {
		return null;
	}

	private EhApprovalRequestsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalRequestsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalRequestsDao getDao(DSLContext context) {
		return new EhApprovalRequestsDao(context.configuration());
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
