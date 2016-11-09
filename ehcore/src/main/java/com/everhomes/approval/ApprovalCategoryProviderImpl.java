// @formatter:off
package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
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
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalCategories;
import com.everhomes.util.ConvertHelper;

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
		getReadWriteDao().insert(approvalCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalCategories.class, null);
	}

	@Override
	public void updateApprovalCategory(ApprovalCategory approvalCategory) {
		assert (approvalCategory.getId() != null);
		getReadWriteDao().update(approvalCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalCategories.class, approvalCategory.getId());
	}

	@Override
	public ApprovalCategory findApprovalCategoryById(Long id) {
		assert (id != null);
		if(id.equals( ApprovalServiceImpl.defaultCategory.getId()))
			return ConvertHelper.convert( ApprovalServiceImpl.defaultCategory, ApprovalCategory.class);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalCategory.class);
	}
	
	@Override
	public List<ApprovalCategory> listApprovalCategory() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
				.orderBy(Tables.EH_APPROVAL_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalCategory.class));
	}
	
	@Override
	public List<ApprovalCategory> listApprovalCategoryForStatistics(Integer namespaceId, String ownerType, Long ownerId,
			Byte approvalType, Date fromDate) {
		//请假类型被删除，下个月的统计不出现该类型的统计项，本月及以前仍然有该类型的统计项
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
				.where(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_APPROVAL_CATEGORIES.APPROVAL_TYPE.eq(approvalType))
				.and(Tables.EH_APPROVAL_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode())
						.or(Tables.EH_APPROVAL_CATEGORIES.STATUS.eq(CommonStatus.INACTIVE.getCode())
								.and(Tables.EH_APPROVAL_CATEGORIES.UPDATE_TIME.gt(new Timestamp(fromDate.getTime())))))
				.orderBy(Tables.EH_APPROVAL_CATEGORIES.ID.asc())
				.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalCategory.class));
		}
		
		return new ArrayList<ApprovalCategory>();
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
	public List<ApprovalCategory> listApprovalCategory(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
									.where(Tables.EH_APPROVAL_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
									.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_TYPE.eq(ownerType))
									.and(Tables.EH_APPROVAL_CATEGORIES.OWNER_ID.eq(ownerId))
									.and(Tables.EH_APPROVAL_CATEGORIES.APPROVAL_TYPE.eq(approvalType))
									.and(Tables.EH_APPROVAL_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
									.orderBy(Tables.EH_APPROVAL_CATEGORIES.ID.asc())
									.fetch();
		if (result != null && result.size() > 0) {
			return result.map(r->ConvertHelper.convert(r, ApprovalCategory.class));
		}
		
		return new ArrayList<ApprovalCategory>();
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
							.and(Tables.EH_APPROVAL_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
							.limit(1)
							.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, ApprovalCategory.class);
		}
		return null;
	}

}
