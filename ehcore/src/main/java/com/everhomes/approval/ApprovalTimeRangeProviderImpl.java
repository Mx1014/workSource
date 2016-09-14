// @formatter:off
package com.everhomes.approval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TimeRange;
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
	public void createApprovalTimeRanges(List<ApprovalTimeRange> approvalTimeRanges){
		List<EhApprovalTimeRanges> list = approvalTimeRanges.stream().map(a->{
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalTimeRanges.class));
			a.setId(id);
			return ConvertHelper.convert(a, EhApprovalTimeRanges.class);
		}).collect(Collectors.toList());
		getReadWriteDao().insert(list);
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
	
	@Override
	public List<ApprovalTimeRange> listApprovalTimeRangeByOwnerId(Long ownerId) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_TIME_RANGES)
														.where(Tables.EH_APPROVAL_TIME_RANGES.OWNER_ID.eq(ownerId))
														.orderBy(Tables.EH_APPROVAL_TIME_RANGES.ID.asc())
														.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalTimeRange.class));
		}
		
		return new ArrayList<ApprovalTimeRange>();
	}

	@Override
	public List<ApprovalTimeRange> listApprovalTimeRangeByUserId(Long userId, Integer namespaceId, String ownerType,
			Long ownerId) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_TIME_RANGES)
				.join(Tables.EH_APPROVAL_REQUESTS)
				.on(Tables.EH_APPROVAL_TIME_RANGES.OWNER_ID.eq(Tables.EH_APPROVAL_REQUESTS.ID))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.eq(userId))
				.and(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
				.fetch();
				
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalTimeRange.class));
		}
				
		return new ArrayList<ApprovalTimeRange>();
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
