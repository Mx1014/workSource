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
import com.everhomes.server.schema.tables.daos.EhSalaryPayslipDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryPayslipDetails;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryPayslipDetailProviderImpl implements SalaryPayslipDetailProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryPayslipDetails.class));
		salaryPayslipDetail.setId(id);
		salaryPayslipDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryPayslipDetail.setCreatorUid(UserContext.current().getUser().getId());
//		salaryPayslipDetail.setUpdateTime(salaryPayslipDetail.getCreateTime());
		salaryPayslipDetail.setOperatorUid(salaryPayslipDetail.getCreatorUid());
		getReadWriteDao().insert(salaryPayslipDetail);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryPayslipDetails.class, null);
	}

	@Override
	public void updateSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail) {
		assert (salaryPayslipDetail.getId() != null);
		salaryPayslipDetail.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryPayslipDetail.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryPayslipDetail);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslipDetails.class, salaryPayslipDetail.getId());
	}

	@Override
	public SalaryPayslipDetail findSalaryPayslipDetailById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryPayslipDetail.class);
	}
	
	@Override
	public List<SalaryPayslipDetail> listSalaryPayslipDetail() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIP_DETAILS)
				.orderBy(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryPayslipDetail.class));
	}
	
	private EhSalaryPayslipDetailsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryPayslipDetailsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryPayslipDetailsDao getDao(DSLContext context) {
		return new EhSalaryPayslipDetailsDao(context.configuration());
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
	public Integer countSend(Long payslipId) {
		// TODO Auto-generated method stub
		return null;
	}
}
