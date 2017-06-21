// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
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
import com.everhomes.server.schema.tables.daos.EhSalaryEntryCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryEntryCategories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEntryCategoryProviderImpl implements SalaryEntityCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEntityCategory(SalaryEntityCategory salaryEntryCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEntryCategories.class));
		salaryEntryCategory.setId(id);
		salaryEntryCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEntryCategory.setCreatorUid(UserContext.current().getUser().getId());
		salaryEntryCategory.setUpdateTime(salaryEntryCategory.getCreateTime());
		salaryEntryCategory.setOperatorUid(salaryEntryCategory.getCreatorUid());
		getReadWriteDao().insert(salaryEntryCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEntryCategories.class, null);
	}

	@Override
	public void updateSalaryEntityCategory(SalaryEntityCategory salaryEntryCategory) {
		assert (salaryEntryCategory.getId() != null);
		salaryEntryCategory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEntryCategory.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEntryCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEntryCategories.class, salaryEntryCategory.getId());
	}

	@Override
	public SalaryEntityCategory findSalaryEntityCategoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEntityCategory.class);
	}
	
	@Override
	public List<SalaryEntityCategory> listSalaryEntityCategory() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_ENTRY_CATEGORIES)
				.orderBy(Tables.EH_SALARY_ENTRY_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEntityCategory.class));
	}
	
	private EhSalaryEntryCategoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEntryCategoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEntryCategoriesDao getDao(DSLContext context) {
		return new EhSalaryEntryCategoriesDao(context.configuration());
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
