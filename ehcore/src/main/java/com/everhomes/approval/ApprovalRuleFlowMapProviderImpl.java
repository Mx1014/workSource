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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalRuleFlowMapDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalRuleFlowMap;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalRuleFlowMapProviderImpl implements ApprovalRuleFlowMapProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalRuleFlowMap.class));
		approvalRuleFlowMap.setId(id);
		getReadWriteDao().insert(approvalRuleFlowMap);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalRuleFlowMap.class, null);
	}

	@Override
	public void updateApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap) {
		assert (approvalRuleFlowMap.getId() != null);
		getReadWriteDao().update(approvalRuleFlowMap);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalRuleFlowMap.class, approvalRuleFlowMap.getId());
	}

	@Override
	public ApprovalRuleFlowMap findApprovalRuleFlowMapById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalRuleFlowMap.class);
	}
	
	@Override
	public List<ApprovalRuleFlowMap> listApprovalRuleFlowMap() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP)
				.orderBy(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalRuleFlowMap.class));
	}
	
	@Override
	public ApprovalRuleFlowMap findOneApprovalRuleFlowMapByFlowId(Long flowId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP)
				.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.FLOW_ID.eq(flowId))
				.and(Tables.EH_APPROVAL_RULE_FLOW_MAP.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.limit(1)
				.fetchOne();
		
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalRuleFlowMap.class);
		}
		
		return null;
	}

	@Override
	public void deleteRuleFlowMapByRuleId(Long ruleId) {
		getReadWriteContext().delete(Tables.EH_APPROVAL_RULE_FLOW_MAP)
			.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.eq(ruleId))
			.execute();
	}

	@Override
	public void updateApprovalRuleFlowMapByRuleId(Long ruleId) {
		getReadWriteContext().update(Tables.EH_APPROVAL_RULE_FLOW_MAP)
			.set(Tables.EH_APPROVAL_RULE_FLOW_MAP.STATUS, CommonStatus.INACTIVE.getCode())
			.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.eq(ruleId))
			.execute();
	}

	@Override
	public List<ApprovalRuleFlowMap> listApprovalRuleFlowMapByRuleIds(List<Long> ruleIdList) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP)
				.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.in(ruleIdList))
				.and(Tables.EH_APPROVAL_RULE_FLOW_MAP.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.asc())
				.fetch();

		if (result != null && result.isNotEmpty()) {
		return result.map(r->ConvertHelper.convert(r, ApprovalRuleFlowMap.class));
		}
		
		return new ArrayList<ApprovalRuleFlowMap>();
	}

	@Override
	public ApprovalRuleFlowMap findConcreteApprovalRuleFlowMap(Long ruleId, Byte approvalType) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP)
				.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.eq(ruleId))
				.and(Tables.EH_APPROVAL_RULE_FLOW_MAP.APPROVAL_TYPE.eq(approvalType))
				.and(Tables.EH_APPROVAL_RULE_FLOW_MAP.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.limit(1)
				.fetchOne();
		
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalRuleFlowMap.class);
		}
		
		return null;
	}

	private EhApprovalRuleFlowMapDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalRuleFlowMapDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalRuleFlowMapDao getDao(DSLContext context) {
		return new EhApprovalRuleFlowMapDao(context.configuration());
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
	public List<ApprovalRuleFlowMap> listRuleFlowMapsByRuleId(Long ruleId) {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULE_FLOW_MAP)
				.where(Tables.EH_APPROVAL_RULE_FLOW_MAP.RULE_ID.eq(ruleId))
				.orderBy(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalRuleFlowMap.class));
	}
}
