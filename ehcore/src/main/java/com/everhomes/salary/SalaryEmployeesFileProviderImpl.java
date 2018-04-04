// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSalaryEmployeesFilesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeesFiles;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEmployeesFileProviderImpl implements SalaryEmployeesFileProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEmployeesFiles.class));
		salaryEmployeesFile.setId(id);
		salaryEmployeesFile.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEmployeesFile.setCreatorUid(UserContext.currentUserId());
//		salaryEmployeesFile.setUpdateTime(salaryEmployeesFile.getCreateTime());
//		salaryEmployeesFile.setOperatorUid(salaryEmployeesFile.getCreatorUid());
		getReadWriteDao().insert(salaryEmployeesFile);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEmployeesFiles.class, null);
	}

	@Override
	public void updateSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile) {
		assert (salaryEmployeesFile.getId() != null);
//		salaryEmployeesFile.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryEmployeesFile.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEmployeesFile);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEmployeesFiles.class, salaryEmployeesFile.getId());
	}

	@Override
	public SalaryEmployeesFile findSalaryEmployeesFileById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEmployeesFile.class);
	}
	
	@Override
	public List<SalaryEmployeesFile> listSalaryEmployeesFile() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES_FILES)
				.orderBy(Tables.EH_SALARY_EMPLOYEES_FILES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeesFile.class));
	}

	@Override
	public void deleteEmployeesFile(Long ownerId, String month) {
		getReadWriteContext().delete(Tables.EH_SALARY_EMPLOYEES_FILES)
				.where(Tables.EH_SALARY_EMPLOYEES_FILES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_EMPLOYEES_FILES.SALARY_PERIOD.eq(month))
				.execute();
	}

	@Override
	public SalaryEmployeesFile findSalaryEmployeesFileByDetailIDAndMonth(Long ownerId, Long detailId, String month) {
		Record r = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES_FILES)
				.where(Tables.EH_SALARY_EMPLOYEES_FILES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_EMPLOYEES_FILES.USER_DETAIL_ID.eq(detailId))
				.and(Tables.EH_SALARY_EMPLOYEES_FILES.SALARY_PERIOD.eq(month)).fetchAny();
		if(null == r)
			return null;
		return ConvertHelper.convert(r, SalaryEmployeesFile.class);
	}

	private EhSalaryEmployeesFilesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEmployeesFilesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEmployeesFilesDao getDao(DSLContext context) {
		return new EhSalaryEmployeesFilesDao(context.configuration());
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
