// @formatter:off
package com.everhomes.approval;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Field;
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
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalDayActualTimeDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalDayActualTime;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RecordHelper;

@Component
public class ApprovalDayActualTimeProviderImpl implements ApprovalDayActualTimeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalDayActualTime(ApprovalDayActualTime approvalDayActualTime) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalDayActualTime.class));
		approvalDayActualTime.setId(id);
		getReadWriteDao().insert(approvalDayActualTime);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalDayActualTime.class, null);
	}
	
	@Override
	public void createApprovalDayActualTimes(List<ApprovalDayActualTime> approvalDayActualTimeList) {
		List<EhApprovalDayActualTime> list = approvalDayActualTimeList.stream().map(a->{
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalDayActualTime.class));
			a.setId(id);
			return ConvertHelper.convert(a, EhApprovalDayActualTime.class);
		}).collect(Collectors.toList());
		getReadWriteDao().insert(list);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalDayActualTime.class, null);
	}

	@Override
	public void updateApprovalDayActualTime(ApprovalDayActualTime approvalDayActualTime) {
		assert (approvalDayActualTime.getId() != null);
		getReadWriteDao().update(approvalDayActualTime);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalDayActualTime.class, approvalDayActualTime.getId());
	}

	@Override
	public ApprovalDayActualTime findApprovalDayActualTimeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalDayActualTime.class);
	}
	
	@Override
	public List<ApprovalDayActualTime> listApprovalDayActualTime() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_DAY_ACTUAL_TIME)
				.orderBy(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalDayActualTime.class));
	}
	
	@Override
	public List<ApprovalDayActualTime> listApprovalDayActualTimeByUserIds(Date fromDate, Date toDate, String ownerType,
			Long ownerId, Byte approvalType, List<Long> userIdList) {
		Field<?>[] fields = Arrays.copyOf(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.fields(), Tables.EH_APPROVAL_DAY_ACTUAL_TIME.fields().length + 1);
		fields[fields.length-1] = Tables.EH_APPROVAL_REQUESTS.CATEGORY_ID;
		Result<Record> result = getReadOnlyContext().select(fields).from(Tables.EH_APPROVAL_DAY_ACTUAL_TIME)
				.join(Tables.EH_APPROVAL_REQUESTS)
				.on(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(approvalType))
				.and(Tables.EH_APPROVAL_REQUESTS.ID.eq(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.OWNER_ID))
				.where(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.USER_ID.in(userIdList))
				.and(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.TIME_DATE.ge(fromDate))
				.and(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.TIME_DATE.lt(toDate))
				.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, ApprovalDayActualTime.class));
		}
		
		return null;
	}

	private EhApprovalDayActualTimeDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalDayActualTimeDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalDayActualTimeDao getDao(DSLContext context) {
		return new EhApprovalDayActualTimeDao(context.configuration());
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
