// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.salary.SalaryGroupStatus;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
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
	public List<SalaryEmployee> listSalaryEmployees(List<Long> detailIds, List<String> periods) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.USER_DETAIL_ID.in(detailIds))
				.and(Tables.EH_SALARY_EMPLOYEES.SALARY_PERIOD.in(periods))
				.and(Tables.EH_SALARY_EMPLOYEES.STATUS.eq(SalaryGroupStatus.SENDED.getCode()))
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
	}

	@Override
	public void updateSalaryEmployeeCheckFlag(List<Long> salaryEmployeeIds, Byte checkFlag) {
		getReadWriteContext().update(Tables.EH_SALARY_EMPLOYEES).set(Tables.EH_SALARY_EMPLOYEES.STATUS, checkFlag)
				.where(Tables.EH_SALARY_EMPLOYEES.ID.in(salaryEmployeeIds)).execute();
	}

	@Override
	public List<SalaryEmployee> listSalaryEmployees(Long salaryPeriodGroupId, List<Long> detailIds, Byte checkFlag, CrossShardListingLocator locator, int pageSize) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.USER_DETAIL_ID.in(detailIds));
		step.and(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryPeriodGroupId));
		if(null != checkFlag)
			step.and(Tables.EH_SALARY_EMPLOYEES.STATUS.eq(checkFlag));
		if (null != locator && locator.getAnchor() != null) {
			step.and(Tables.EH_SALARY_EMPLOYEES.ID.gt(locator.getAnchor()));
		}
		step.limit(pageSize);
		return step.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
	}

	@Override
	public void deleteSalaryEmployee(Long ownerId, Long detail_id, Long salaryGroupId) {
		getReadWriteContext().delete(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.USER_DETAIL_ID.eq(detail_id))
				.and(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryGroupId))
				.and(Tables.EH_SALARY_EMPLOYEES.OWNER_ID.eq(ownerId)).execute();

	}

	@Override
	public void deleteSalaryEmployee(SalaryEmployee employee) {
		getReadWriteDao().delete(employee);
	}

	@Override
	public Integer countSalaryEmployeesByStatus(Long salaryPeriodGroupId, Byte checkFlag) {

		SelectConditionStep<Record1<Integer>> step = getReadOnlyContext().selectCount().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryPeriodGroupId));
		if(null != checkFlag)
			step.and(Tables.EH_SALARY_EMPLOYEES.STATUS.eq(checkFlag));
		return step.fetchOne().value1();
	}

	@Override
	public SalaryEmployee findSalaryEmployeeBySalaryGroupIdAndDetailId(Long salaryGroupId, Long detailId) {
		List<SalaryEmployee> result = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryGroupId))
				.and(Tables.EH_SALARY_EMPLOYEES.USER_DETAIL_ID.eq(detailId))
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
		if (null == result || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public SalaryEmployee findSalaryEmployee(Long ownerId, Long detailId, Long salaryGroupId) {
		List<SalaryEmployee> result = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEES)
				.where(Tables.EH_SALARY_EMPLOYEES.USER_DETAIL_ID.eq(detailId))
				.and(Tables.EH_SALARY_EMPLOYEES.SALARY_GROUP_ID.eq(salaryGroupId))
				.and(Tables.EH_SALARY_EMPLOYEES.OWNER_ID.eq(ownerId))
				.orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployee.class));
		if (null == result || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}
}
