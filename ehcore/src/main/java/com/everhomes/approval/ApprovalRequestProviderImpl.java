// @formatter:off
package com.everhomes.approval;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.Row2;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.ApprovalQueryType;
import com.everhomes.rest.approval.ApprovalRequestCondition;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalRequests;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ListUtils;
import com.hp.hpl.sparta.xpath.Step;

@Component
public class ApprovalRequestProviderImpl implements ApprovalRequestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalRequestProviderImpl.class);
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
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(condition.getOwnerId()))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		
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
	public List<ApprovalRequest> listApprovalRequestWaitingForApproving(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Long categoryId, Long fromDate, Long endDate,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<Long> userIds, Long pageAnchor, int pageSize) {
		
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
				
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		if(null!= approvalType){
			step = step.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(approvalType));
			if(approvalType.equals(ApprovalType.EXCEPTION.getCode())){
				if (fromDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.LONG_TAG1.greaterOrEqual(fromDate));
				}
				
				if (endDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.LONG_TAG1.lessOrEqual(endDate));
				}
			}
			else if(approvalType.equals(ApprovalType.OVERTIME.getCode())){
				if (fromDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.greaterOrEqual(new Date(fromDate)));
				}
				
				if (endDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.lessOrEqual(new Date(endDate)));
				}
			}
		}
		if (categoryId != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CATEGORY_ID.eq(categoryId));
		} 
		
		step = step.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_STATUS.eq(ApprovalStatus.WAITING_FOR_APPROVING.getCode()));
		
		if (ListUtils.isNotEmpty(userIds)) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.in(userIds));
		}
		
		List<Row2<Long, Byte>> flowLevelList = approvalFlowLevelList.stream().map(a->DSL.row(a.getFlowId(), a.getLevel())).collect(Collectors.toList());
		step = step.and(DSL.row(Tables.EH_APPROVAL_REQUESTS.FLOW_ID, Tables.EH_APPROVAL_REQUESTS.NEXT_LEVEL).in(flowLevelList));
		
		if (pageAnchor != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.ID.lt(pageAnchor));
		}
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_REQUESTS.ID.desc()).limit(pageSize).fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalRequest.class));
		}
		
		return new ArrayList<ApprovalRequest>();
	}

	@Override
	public List<ApprovalRequest> listApprovalRequestByEffectiveDateAndCreateUid(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Date effectiveDate,Long createUid,List<Byte> approvalStatus) {
		
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(approvalType))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
				
		if (effectiveDate != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.eq(effectiveDate));
		}
		
		if (createUid != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.eq(createUid));
		}
		if(null == approvalStatus){
			approvalStatus= new ArrayList<Byte>();
			approvalStatus.add(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
			approvalStatus.add(ApprovalStatus.AGREEMENT.getCode());
		}
		step = step.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_STATUS.in(approvalStatus));
		  
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_REQUESTS.ID.desc()).fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalRequest.class));
		}
		
		return new ArrayList<ApprovalRequest>();
	}

	@Override
	public List<ApprovalRequest> listApprovalRequestApproved(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Long categoryId, Long fromDate, Long endDate,
			List<ApprovalFlowLevel> approvalFlowLevelList, List<Long> userIds, Long pageAnchor, int pageSize) {
		/**
		 * 
			select *
			from eh_approval_requests t1
			where t1.namespace_id = 1000000
			and t1.owner_type = 'organization'
			and t1.owner_id = 1000001
			and t1.approval_type = 2
			and t1.status = 2
			and t1.category_id = 1
			and t1.long_tag1 >= 10000
			and t1.long_tag1 < 20000
			and t1.creator_uid in (1,2,3)
			and exists (
				select 1
				from eh_approval_op_requests t2
				where t1.id = t2.request_id
				and (t2.flow_id, t2.level) in ((3,1), (3,2))
				and t2.approval_status in (1,2)	
			)
			order by t1.update_time desc
			limit 0, 20
		 */
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		
		if(null!= approvalType){
			step = step.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(approvalType));
			if(approvalType.equals(ApprovalType.EXCEPTION.getCode())){
				if (fromDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.LONG_TAG1.greaterOrEqual(fromDate));
				}
				
				if (endDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.LONG_TAG1.lessOrEqual(endDate));
				}
			}
			else if(approvalType.equals(ApprovalType.OVERTIME.getCode())){
				if (fromDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.greaterOrEqual(new Date(fromDate)));
				}
				
				if (endDate != null) {
					step = step.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.lessOrEqual(new Date(endDate)));
				}
			}
		}
		
				
		if (categoryId != null) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CATEGORY_ID.eq(categoryId));
		}
		 
		
		if (ListUtils.isNotEmpty(userIds)) {
			step = step.and(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.in(userIds));
		}
		List<Row2<Long, Byte>> flowLevelList = approvalFlowLevelList.stream().map(a->DSL.row(a.getFlowId(), a.getLevel())).collect(Collectors.toList());
		step = step.andExists(
				getReadOnlyContext().selectOne().from(Tables.EH_APPROVAL_OP_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.ID.eq(Tables.EH_APPROVAL_OP_REQUESTS.REQUEST_ID))
				.and(DSL.row(Tables.EH_APPROVAL_OP_REQUESTS.FLOW_ID, Tables.EH_APPROVAL_OP_REQUESTS.LEVEL).in(flowLevelList))
				.and(Tables.EH_APPROVAL_OP_REQUESTS.APPROVAL_STATUS.in(ApprovalStatus.AGREEMENT.getCode(), ApprovalStatus.REJECTION.getCode()))
				);
		
		if (pageAnchor == null) {
			pageAnchor = 0L;
		}
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_REQUESTS.UPDATE_TIME.desc()).limit(Long.valueOf(pageAnchor*pageSize).intValue(), pageSize).fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalRequest.class));
		}
		
		return new ArrayList<ApprovalRequest>();
	}

	@Override
	public boolean checkExcludeAbsenceRequest(Long userId, Long requestId, Date date) {
		ApprovalRequest approvalRequest = findApprovalRequestById(requestId);
		//针对同一天既有请假申请，又有忘打卡申请，已最后提交的申请为依据
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_REQUESTS)
				.where(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.eq(userId))
				.and(Tables.EH_APPROVAL_REQUESTS.NAMESPACE_ID.eq(approvalRequest.getNamespaceId()))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(approvalRequest.getOwnerType()))
				.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(approvalRequest.getOwnerId()))
				.and(Tables.EH_APPROVAL_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_TYPE.eq(ApprovalType.EXCEPTION.getCode()))
				.and(Tables.EH_APPROVAL_REQUESTS.CREATE_TIME.gt(approvalRequest.getCreateTime()))
				.and(Tables.EH_APPROVAL_REQUESTS.LONG_TAG1.eq(date.getTime()))
				.limit(1)
				.fetchOne();
		
		if (record != null) {
			return true;
		}
		
		return false;
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

	@Override
	public void deleteApprovalRequest(ApprovalRequest approvalRequest) {
		getReadWriteDao().delete(approvalRequest);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalRequests.class, null);
	}

	@Override
	public Double countHourLengthByUserAndMonth(Long userId, String ownerType, Long ownerId,
			String punchMonth) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date beginDate;
		try {
			beginDate = new Date(dateFormat.parse(punchMonth+"01").getTime());
		
			Date endDate = new Date(dateFormat.parse((Integer.valueOf(punchMonth)+1)+"01").getTime());
			SelectConditionStep<Record1<BigDecimal>> step = getReadOnlyContext().select(Tables.EH_APPROVAL_REQUESTS.HOUR_LENGTH.sum()).from(Tables.EH_APPROVAL_REQUESTS)
					.where(Tables.EH_APPROVAL_REQUESTS.CREATOR_UID.eq(userId))
					.and(Tables.EH_APPROVAL_REQUESTS.OWNER_TYPE.eq(ownerType))
					.and(Tables.EH_APPROVAL_REQUESTS.OWNER_ID.eq(ownerId))
					.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.greaterOrEqual(beginDate))
					.and(Tables.EH_APPROVAL_REQUESTS.EFFECTIVE_DATE.lt(endDate))
					.and(Tables.EH_APPROVAL_REQUESTS.APPROVAL_STATUS.eq(ApprovalStatus.AGREEMENT.getCode()));
//			LOGGER.debug(step.toString());
			return step.fetchOneInto(Double.class);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
		  
	}
}
