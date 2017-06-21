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
import com.everhomes.server.schema.tables.EhSalaryEntityCategories;
import com.everhomes.server.schema.tables.daos.EhSalaryEntityCategoriesDao;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEntityCategoryProviderImpl implements SalaryEntityCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEntityCategory(SalaryEntityCategory salaryEntityCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEntityCategories.class));
		salaryEntityCategory.setId(id);
		salaryEntityCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEntityCategory.setCreatorUid(UserContext.current().getUser().getId());
		salaryEntityCategory.setUpdateTime(salaryEntityCategory.getCreateTime());
		salaryEntityCategory.setOperatorUid(salaryEntityCategory.getCreatorUid());
		getReadWriteDao().insert(salaryEntityCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEntityCategories.class, null);
	}

	@Override
	public void updateSalaryEntityCategory(SalaryEntityCategory salaryEntityCategory) {
		assert (salaryEntityCategory.getId() != null);
		salaryEntityCategory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEntityCategory.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEntityCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEntityCategories.class, salaryEntityCategory.getId());
	}

	@Override
	public SalaryEntityCategory findSalaryEntityCategoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEntityCategory.class);
	}
	
	@Override
	public List<SalaryEntityCategory> listSalaryEntityCategory() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_ENTITY_CATEGORIES)
				.orderBy(Tables.EH_SALARY_ENTITY_CATEGORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEntityCategory.class));
	}
	
	private EhSalaryEntityCategoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEntityCategoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEntityCategoriesDao getDao(DSLContext context) {
		return new EhSalaryEntityCategoriesDao(context.configuration());
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
