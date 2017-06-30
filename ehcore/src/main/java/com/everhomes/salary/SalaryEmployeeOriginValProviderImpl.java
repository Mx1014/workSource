// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.user.User;
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
import com.everhomes.server.schema.tables.daos.EhSalaryEmployeeOriginValsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeeOriginVals;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryEmployeeOriginValProviderImpl implements SalaryEmployeeOriginValProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal) {
		User user = UserContext.current().getUser();
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryEmployeeOriginVals.class));
		salaryEmployeeOriginVal.setId(id);
		salaryEmployeeOriginVal.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryEmployeeOriginVal.setCreatorUid(user.getId());
		salaryEmployeeOriginVal.setNamespaceId(user.getNamespaceId());
		getReadWriteDao().insert(salaryEmployeeOriginVal);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryEmployeeOriginVals.class, null);
	}

	@Override
	public void updateSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal) {
		assert (salaryEmployeeOriginVal.getId() != null);
//		salaryEmployeeOriginVal.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryEmployeeOriginVal.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryEmployeeOriginVal);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryEmployeeOriginVals.class, salaryEmployeeOriginVal.getId());
	}

	@Override
	public SalaryEmployeeOriginVal findSalaryEmployeeOriginValById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryEmployeeOriginVal.class);
	}
	
	@Override
	public List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginVal() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS)
				.orderBy(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeeOriginVal.class));
	}

	@Override
	public List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByUserId(Long userId, String ownerType, Long ownerId){
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS)
				.where(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.USER_ID.eq(userId))
                .and(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.OWNER_ID.eq(ownerId))
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeeOriginVal.class));
	}

	@Override
	public void deleteSalaryEmployeeOriginValByGroupId(Long groupId){
		DSLContext context = this.getContext(AccessSpec.readWrite());
		context.delete(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS)
                .where(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.GROUP_ID.eq(groupId))
                .execute();
	}

	@Override
    public void deleteSalaryEmployeeOriginValByGroupIdUserId(Long groupId, Long userId){
        DSLContext context = this.getContext(AccessSpec.readWrite());
        context.delete(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS)
                .where(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.USER_ID.eq(userId))
                .and(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.GROUP_ID.eq(groupId))
                .execute();
    }
/*	@Override
	public List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByUserId(Long userId){
		return getReadOnlyContext().select().from(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS)
				.where(Tables.EH_SALARY_EMPLOYEE_ORIGIN_VALS.USER_ID.eq(userId))
				.fetch().map(r -> ConvertHelper.convert(r, SalaryEmployeeOriginVal.class));
	}*/

	private EhSalaryEmployeeOriginValsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryEmployeeOriginValsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryEmployeeOriginValsDao getDao(DSLContext context) {
		return new EhSalaryEmployeeOriginValsDao(context.configuration());
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
