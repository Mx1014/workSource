// @formatter:off
package com.everhomes.approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.ApprovalCategoryStatus;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhApprovalCategoryInitLogsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalCategories;
import com.everhomes.server.schema.tables.pojos.EhApprovalCategoryInitLogs;
import com.everhomes.server.schema.tables.records.EhApprovalCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhApprovalCategoryInitLogsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApprovalCategoryProviderImpl implements ApprovalCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalCategory(ApprovalCategory approvalCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalCategories.class));
		approvalCategory.setId(id);
		approvalCategory.setCreatorUid(UserContext.currentUserId() == null ? 0L : UserContext.currentUserId());
		approvalCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalCategory.setUpdateTime(approvalCategory.getCreateTime());
		approvalCategory.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().insert(approvalCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalCategories.class, null);
	}

	@Override
	public void updateApprovalCategory(ApprovalCategory approvalCategory) {
		assert (approvalCategory.getId() != null);
		approvalCategory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalCategory.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().update(approvalCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalCategories.class, approvalCategory.getId());
	}

	@Override
	public Integer getNextApprovalCategoryDefaultOrder(Integer namespaceId, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		Condition condition = Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
		condition = condition.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId));

		SelectConditionStep<Record1<Integer>> query = context.select(Tables.EH_APPROVAL_CATEGORIES.DEFAULT_ORDER.max().add(1)).from(Tables.EH_APPROVAL_CATEGORIES).where(condition);

		Record1<Integer> result = query.fetchOne();

		if (result != null) {
			return result.value1();
		}
		return null;
	}

	@Override
	public ApprovalCategory findApprovalCategoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalCategory.class);
	}

	@Override
	public ApprovalCategory findApprovalCategoryByOriginId(Long originId, Long ownerId) {
		assert (originId != null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhApprovalCategoriesRecord> query = context.selectQuery(Tables.EH_APPROVAL_CATEGORIES);
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.ORIGIN_ID.eq(originId));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.STATUS.ne(ApprovalCategoryStatus.DELETED.getCode()));

		EhApprovalCategoriesRecord record = query.fetchOne();

		return ConvertHelper.convert(record, ApprovalCategory.class);
	}

	@Override
	public ApprovalCategory findApprovalCategoryById(Integer namespaceId, String ownerType, Long ownerId, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhApprovalCategoriesRecord> query = context.selectQuery(Tables.EH_APPROVAL_CATEGORIES);
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.ID.eq(id));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.STATUS.ne(ApprovalCategoryStatus.DELETED.getCode()));

		EhApprovalCategoriesRecord record = query.fetchOne();

		return ConvertHelper.convert(record, ApprovalCategory.class);
	}

	@Override
	public List<ApprovalCategory> listBaseApprovalCategory() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
				.where(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(0))
				.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ApprovalOwnerType.ORGANIZATION.getCode()))
				.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(0L))
				.and(Tables.EH_APPROVAL_CATEGORIES.STATUS.ne(ApprovalCategoryStatus.DELETED.getCode()))
				.orderBy(Tables.EH_APPROVAL_CATEGORIES.DEFAULT_ORDER.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalCategory.class));
	}
	
	
	
	private EhApprovalCategoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalCategoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalCategoriesDao getDao(DSLContext context) {
		return new EhApprovalCategoriesDao(context.configuration());
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
	public List<ApprovalCategory> listApprovalCategory(QueryApprovalCategoryCondition condition) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhApprovalCategoriesRecord> query = context.selectQuery(Tables.EH_APPROVAL_CATEGORIES);
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(condition.getNamespaceId()));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(condition.getOwnerType()));
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(condition.getOwnerId()));
		if (condition.getApprovalType() != null) {
			query.addConditions(Tables.EH_APPROVAL_CATEGORIES.APPROVAL_TYPE.eq(condition.getApprovalType()));
		}
		if (!CollectionUtils.isEmpty(condition.getStatusList())) {
			query.addConditions(Tables.EH_APPROVAL_CATEGORIES.STATUS.in(condition.getStatusList()));
		}
		query.addConditions(Tables.EH_APPROVAL_CATEGORIES.STATUS.ne(ApprovalCategoryStatus.DELETED.getCode()));
		query.addOrderBy(Tables.EH_APPROVAL_CATEGORIES.DEFAULT_ORDER.asc());

		Result<EhApprovalCategoriesRecord> result = query.fetch();
		if (result != null && result.size() > 0) {
			return result.map(r -> ConvertHelper.convert(r, ApprovalCategory.class));
		}

		return new ArrayList<ApprovalCategory>();
	}

	@Override
	public void createApprovalCategoryInitLog(ApprovalCategoryInitLog approvalCategoryInitLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalCategoryInitLogs.class));
		approvalCategoryInitLog.setId(id);
		approvalCategoryInitLog.setCreatorUid(UserContext.currentUserId() == null ? 0L : UserContext.currentUserId());
		approvalCategoryInitLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhApprovalCategoryInitLogs.class));
		EhApprovalCategoryInitLogsDao dao = new EhApprovalCategoryInitLogsDao(context.configuration());
		dao.insert(approvalCategoryInitLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalCategoryInitLogs.class, null);
	}

	@Override
	public int countApprovalCategoryInitLogByOwnerId(Integer namespaceId, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhApprovalCategoryInitLogsRecord> query = context.selectQuery(Tables.EH_APPROVAL_CATEGORY_INIT_LOGS);
		query.addConditions(Tables.EH_APPROVAL_CATEGORY_INIT_LOGS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_APPROVAL_CATEGORY_INIT_LOGS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_APPROVAL_CATEGORY_INIT_LOGS.OWNER_ID.eq(ownerId));

		return query.fetchCount();
	}

	@Override
	public ApprovalCategory findApprovalCategoryByName(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, String categoryName) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
							.where(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
							.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ownerType))
							.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId))
							.and(Tables.EH_APPROVAL_CATEGORIES.APPROVAL_TYPE.eq(approvalType))
							.and(Tables.EH_APPROVAL_CATEGORIES.CATEGORY_NAME.eq(categoryName))
							.and(Tables.EH_APPROVAL_CATEGORIES.STATUS.ne(ApprovalCategoryStatus.DELETED.getCode()))
							.limit(1)
							.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalCategory.class);
		}
		return null;
	}

}
