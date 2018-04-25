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
import com.everhomes.server.schema.tables.daos.EhSalaryPayslipsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryPayslips;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryPayslipProviderImpl implements SalaryPayslipProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryPayslip(SalaryPayslip salaryPayslip) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryPayslips.class));
		salaryPayslip.setId(id);
		salaryPayslip.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryPayslip.setCreatorUid(UserContext.current().getUser().getId());
//		salaryPayslip.setUpdateTime(salaryPayslip.getCreateTime());
		salaryPayslip.setOperatorUid(salaryPayslip.getCreatorUid());
		getReadWriteDao().insert(salaryPayslip);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryPayslips.class, null);
	}

	@Override
	public void updateSalaryPayslip(SalaryPayslip salaryPayslip) {
		assert (salaryPayslip.getId() != null);
//		salaryPayslip.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryPayslip.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryPayslip);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslips.class, salaryPayslip.getId());
	}

	@Override
	public SalaryPayslip findSalaryPayslipById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryPayslip.class);
	}
	
	@Override
	public List<SalaryPayslip> listSalaryPayslip() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIPS)
				.orderBy(Tables.EH_SALARY_PAYSLIPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryPayslip.class));
	}
	
	private EhSalaryPayslipsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryPayslipsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryPayslipsDao getDao(DSLContext context) {
		return new EhSalaryPayslipsDao(context.configuration());
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
