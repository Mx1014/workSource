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
import com.everhomes.server.schema.tables.daos.EhSalaryDepartStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryDepartStatistics;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryDepartStatisticProviderImpl implements SalaryDepartStatisticProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryDepartStatistics.class));
		salaryDepartStatistic.setId(id);
		salaryDepartStatistic.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryDepartStatistic.setCreatorUid(UserContext.current().getUser().getId());
		salaryDepartStatistic.setUpdateTime(salaryDepartStatistic.getCreateTime());
		salaryDepartStatistic.setOperatorUid(salaryDepartStatistic.getCreatorUid());
		getReadWriteDao().insert(salaryDepartStatistic);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryDepartStatistics.class, null);
	}

	@Override
	public void updateSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic) {
		assert (salaryDepartStatistic.getId() != null);
		salaryDepartStatistic.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryDepartStatistic.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryDepartStatistic);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryDepartStatistics.class, salaryDepartStatistic.getId());
	}

	@Override
	public SalaryDepartStatistic findSalaryDepartStatisticById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryDepartStatistic.class);
	}
	
	@Override
	public List<SalaryDepartStatistic> listSalaryDepartStatistic() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_DEPART_STATISTICS)
				.orderBy(Tables.EH_SALARY_DEPART_STATISTICS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryDepartStatistic.class));
	}
	
	private EhSalaryDepartStatisticsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryDepartStatisticsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryDepartStatisticsDao getDao(DSLContext context) {
		return new EhSalaryDepartStatisticsDao(context.configuration());
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
