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
import com.everhomes.server.schema.tables.daos.EhApprovalRulesDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalRules;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalRuleProviderImpl implements ApprovalRuleProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalRule(ApprovalRule approvalRule) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalRules.class));
		approvalRule.setId(id);
		getReadWriteDao().insert(approvalRule);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalRules.class, null);
	}

	@Override
	public void updateApprovalRule(ApprovalRule approvalRule) {
		assert (approvalRule.getId() != null);
		getReadWriteDao().update(approvalRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalRules.class, approvalRule.getId());
	}

	@Override
	public ApprovalRule findApprovalRuleById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalRule.class);
	}
	
	@Override
	public List<ApprovalRule> listApprovalRule() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULES)
				.orderBy(Tables.EH_APPROVAL_RULES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalRule.class));
	}
	
	
	@Override
	public List<ApprovalRule> listApprovalRule(Integer namespaceId, String ownerType, Long ownerId, Long pageAnchor,
			int pageSize) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULES)
				.where(Tables.EH_APPROVAL_RULES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_RULES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_APPROVAL_RULES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

		if (pageAnchor != null) {
		step = step.and(Tables.EH_APPROVAL_RULES.ID.gt(pageAnchor));
		}
		
		Result<Record> result = step.orderBy(Tables.EH_APPROVAL_RULES.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
		return result.map(r->ConvertHelper.convert(r, ApprovalRule.class));
		}
		
		return new ArrayList<ApprovalRule>();
	}

	@Override
	public ApprovalRule findApprovalRuleByName(Integer namespaceId, String ownerType, Long ownerId, String ruleName) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULES)
							.where(Tables.EH_APPROVAL_RULES.NAMESPACE_ID.eq(namespaceId))
							.and(Tables.EH_APPROVAL_RULES.OWNER_TYPE.eq(ownerType))
							.and(Tables.EH_APPROVAL_RULES.OWNER_ID.eq(ownerId))
							.and(Tables.EH_APPROVAL_RULES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
							.and(Tables.EH_APPROVAL_RULES.NAME.eq(ruleName))
							.limit(1)
							.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalRule.class);
		}
		return null;
	}

	@Override
	public List<ApprovalRule> listApprovalRule(Integer namespaceId, String ownerType, Long ownerId) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_RULES)
									.where(Tables.EH_APPROVAL_RULES.NAMESPACE_ID.eq(namespaceId))
									.and(Tables.EH_APPROVAL_RULES.OWNER_TYPE.eq(ownerType))
									.and(Tables.EH_APPROVAL_RULES.OWNER_ID.eq(ownerId))
									.and(Tables.EH_APPROVAL_RULES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
									.orderBy(Tables.EH_APPROVAL_RULES.ID.asc())
									.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalRule.class));
		}
		
		return new ArrayList<ApprovalRule>();
	}

	private EhApprovalRulesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalRulesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalRulesDao getDao(DSLContext context) {
		return new EhApprovalRulesDao(context.configuration());
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
