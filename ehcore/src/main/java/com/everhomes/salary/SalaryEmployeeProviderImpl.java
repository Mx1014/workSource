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
import com.everhomes.server.schema.tables.daos.EhSalaryEmployeesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryEmployees;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEmployeeProviderImpl implements SalaryEmployeeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEmployee(SalaryEmployee salaryEmployee) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEmployees.class));
		salaryEmployee.setId(id);
		salaryEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEmployee.setCreatorUid(UserContext.current().getUser().getId());
//		salaryEmployee.setUpdateTime(salaryEmployee.getCreateTime());
//		salaryEmployee.setOperatorUid(salaryEmployee.getCreatorUid());
		getReadWriteDao().insert(salaryEmployee);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEmployees.class, null);
	}

	@Override
	public void updateSalaryEmployee(SalaryEmployee salaryEmployee) {
		assert (salaryEmployee.getId() != null);
//		salaryEmployee.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryEmployee.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEmployee);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEmployees.class, salaryEmployee.getId());
	}

	@Override
	public SalaryEmployee findSalaryEmployeeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEmployee.class);
	}
	
	@Override
	public List<SalaryEmployee> listSalaryEmployee() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
	}
	
	private EhSalaryEmployeesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEmployeesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEmployeesDao getDao(DSLContext context) {
		return new EhSalaryEmployeesDao(context.configuration());
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
	public List<SalaryEmployee> listSalaryEmployeeByPeriodGroupId(Long salaryPeriodGroupId) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryPeriodGroupId))
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
	}

	@Override
	public int countUnCheckEmployee(Long salaryPeriodGroupId) { 
		return getReadOnlyContext().selectCount().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryPeriodGroupId)).execute();
	}

	@Override
	public List<SalaryEmployee> listSalaryEmployees(List<Long> userIds, List<String> periods) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.USER_ID.in(userIds))
				.and(Tables.EH_SALARY_EMPLOYEES.SALARY_PERIOD.in(periods))
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
	}

	@Override
	public void updateSalaryEmployeeCheckFlag(List<Long> salaryEmployeeIds, Byte checkFlag) {
		getReadWriteContext().update(Tables.EH_SALARY_EMPLOYEES).set(Tables.EH_SALARY_EMPLOYEES.STATUS, checkFlag)
				.where(Tables.EH_SALARY_EMPLOYEES.ID.in(salaryEmployeeIds)).execute();
	}
}
