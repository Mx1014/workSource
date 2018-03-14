// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.salary.SalaryEntityType;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSalaryEmployeePeriodValsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeePeriodVals;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEmployeePeriodValProviderImpl implements SalaryEmployeePeriodValProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEmployeePeriodVal(SalaryEmployeePeriodVal salaryEmployeePeriodVal) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEmployeePeriodVals.class));
		salaryEmployeePeriodVal.setId(id);
		salaryEmployeePeriodVal.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEmployeePeriodVal.setCreatorUid(UserContext.currentUserId());
//		salaryEmployeePeriodVal.setUpdateTime(salaryEmployeePeriodVal.getCreateTime());
//		salaryEmployeePeriodVal.setOperatorUid(salaryEmployeePeriodVal.getCreatorUid());
		getReadWriteDao().insert(salaryEmployeePeriodVal);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEmployeePeriodVals.class, null);
	}

	@Override
	public void updateSalaryEmployeePeriodVal(SalaryEmployeePeriodVal salaryEmployeePeriodVal) {
		assert (salaryEmployeePeriodVal.getId() != null);
//		salaryEmployeePeriodVal.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryEmployeePeriodVal.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEmployeePeriodVal);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEmployeePeriodVals.class, salaryEmployeePeriodVal.getId());
	}

	@Override
	public SalaryEmployeePeriodVal findSalaryEmployeePeriodValById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEmployeePeriodVal.class);
	}
	
	@Override
	public List<SalaryEmployeePeriodVal> listSalaryEmployeePeriodVal() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
				.orderBy(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeePeriodVal.class));
	}

	@Override
	public void deleteEmployeePeriodVals(Long ownerId, String month) {

		getReadWriteContext().delete(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_PERIOD.eq(month))
				.execute();
	}

	@Override
	public SalaryEmployeePeriodVal findSalaryEmployeePeriodValByGroupEntityIdByDetailId(Long groupEntityId, Long detailId, String month) {
		Record r = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.GROUP_ENTITY_ID.eq(groupEntityId))
				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.USER_DETAIL_ID.eq(detailId))
				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_PERIOD.eq(month)).fetchAny();
		if(null == r)
			return null;
		return ConvertHelper.convert(r, SalaryEmployeePeriodVal.class);
	}

	@Override
	public List<SalaryGroupEntity> listOpenSalaryGroupEntityByOrgId(Long ownerId, String month) {
		List<SalaryGroupEntity> results = null;
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_PERIOD.eq(month)).fetch();
		if (null == records) {
			return null;
		}
		records.stream().map(r -> {
			SalaryGroupEntity entity = ConvertHelper.convert(r, SalaryGroupEntity.class);
			entity.setId((Long) r.getValue("group_entity_id"));
			results.add(entity);
			return entity;
		});

		return results;
	}

	private EhSalaryEmployeePeriodValsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEmployeePeriodValsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEmployeePeriodValsDao getDao(DSLContext context) {
		return new EhSalaryEmployeePeriodValsDao(context.configuration());
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
//
//	@Override
//	public Integer countNullSalaryEmployeePeriodsByPeriodAndEntity(String ownerType, Long ownerId,
//																   String period, Long entityIdShifa) {
//		SelectConditionStep<Record1<Integer>> step = getReadOnlyContext().selectCount().from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS).join(Tables.EH_SALARY_EMPLOYEES)
//				.on(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.eq(Tables.EH_SALARY_EMPLOYEES.ID))
//				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.OWNER_TYPE.eq(ownerType))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.OWNER_ID.eq(ownerId))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.ORIGIN_ENTITY_ID.eq(entityIdShifa))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_VALUE.isNull())
//				.and(Tables.EH_SALARY_EMPLOYEES.SALARY_PERIOD.eq(period));
//		return	step.fetchOne().value1();
////				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeePeriodVal.class));
//	}
//
//	@Override
//	public List<SalaryEmployeePeriodVal> listSalaryEmployeePeriodVals(Long salaryEmployeeId) {
//		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
//				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.eq(salaryEmployeeId))
//				.orderBy(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.ID.asc())
//				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeePeriodVal.class));
//	}
//
//	@Override
//	public void updateSalaryEmployeePeriodVal(Long salaryEmployeeId, Long groupEntryId,
//			String salaryValue) {
//		getReadWriteContext().update(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS).set(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_VALUE,salaryValue)
//		.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.eq(salaryEmployeeId))
//		.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.GROUP_ENTITY_ID.eq(groupEntryId)).execute();
//
//	}
//
//	@Override
//	public void deletePeriodVals(Long employeeId) {
//		getReadWriteContext().delete(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
//				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.eq(employeeId)).execute();
//	}
//
//	@Override
//	public void createSalaryEmployeePeriodVals(List<SalaryEmployeePeriodVal> salaryEmployeePeriodVals) {
//		for (SalaryEmployeePeriodVal r : salaryEmployeePeriodVals) {
//			createSalaryEmployeePeriodVal(r);
//		}
//	}
//
//	@Override
//	public int countEntityIsNull(List<Long> salaryEmployeeIds, Long entityIdShifa) {
//		SelectConditionStep<Record1<Integer>> step = getReadOnlyContext().selectCount().
//				from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS)
//				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.in(salaryEmployeeIds))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.ORIGIN_ENTITY_ID.eq(entityIdShifa))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_VALUE.isNull());
//		return	step.fetchOne().value1();
//	}
//
//	@Override
//	public int countNumberEntityIsNull(List<Long> salaryEmployeeIds) {
//		SelectConditionStep<Record1<Integer>> step = getReadOnlyContext().selectCount().
//				from(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS).join(Tables.EH_SALARY_DEFAULT_ENTITIES)
//				.on(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.ORIGIN_ENTITY_ID.eq(Tables.EH_SALARY_DEFAULT_ENTITIES.ID))
//				.where(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_EMPLOYEE_ID.in(salaryEmployeeIds))
//				.and(Tables.EH_SALARY_DEFAULT_ENTITIES.TYPE.eq(SalaryEntityType.NUMBER.getCode()))
//				.and(Tables.EH_SALARY_EMPLOYEE_PERIOD_VALS.SALARY_VALUE.isNull());
//		return	step.fetchOne().value1();
//	}
}
