// @formatter:off
package com.everhomes.approval;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
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
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalCategory.class);
	}
	
	@Override
	public List<ApprovalCategory> listApprovalCategory() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_CATEGORIES)
				.orderBy(Tables.EH_APPROVAL_CATEGORIES.ID.asc())
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
}
