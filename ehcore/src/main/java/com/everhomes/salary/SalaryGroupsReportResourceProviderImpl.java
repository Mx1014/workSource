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
import com.everhomes.server.schema.tables.daos.EhSalaryGroupsReportResourcesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryGroupsReportResources;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryGroupsReportResourceProviderImpl implements SalaryGroupsReportResourceProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroupsReportResources.class));
		salaryGroupsReportResource.setId(id);
//		salaryGroupsReportResource.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroupsReportResource.setCreatorUid(UserContext.current().getUser().getId());
//		salaryGroupsReportResource.setUpdateTime(salaryGroupsReportResource.getCreateTime());
//		salaryGroupsReportResource.setOperatorUid(salaryGroupsReportResource.getCreatorUid());
		getReadWriteDao().insert(salaryGroupsReportResource);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroupsReportResources.class, null);
	}

	@Override
	public void updateSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource) {
		assert (salaryGroupsReportResource.getId() != null);
//		salaryGroupsReportResource.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroupsReportResource.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryGroupsReportResource);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupsReportResources.class, salaryGroupsReportResource.getId());
	}

	@Override
	public SalaryGroupsReportResource findSalaryGroupsReportResourceById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroupsReportResource.class);
	}
	
	@Override
	public List<SalaryGroupsReportResource> listSalaryGroupsReportResource() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES)
				.orderBy(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupsReportResource.class));
	}

	@Override
	public void deleteSalaryGroupsReportResourceByPeriodAndType(Long ownerId, String salaryPeriod, Byte reportType) {
		getReadWriteContext().delete(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES)
				.where(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.REPORT_TYPE.eq(reportType))
				.and(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.SALARY_PERIOD.eq(salaryPeriod))
				.execute();
	}

	@Override
	public SalaryGroupsReportResource findSalaryGroupsReportResourceByPeriodAndType(Long ownerId, String month, Byte code) {
		Record record = getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES)
				.where(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.SALARY_PERIOD.eq(month))
				.and(Tables.EH_SALARY_GROUPS_REPORT_RESOURCES.REPORT_TYPE.eq(code)).fetchAny();
		if (null == record) {
			return null;
		}
		return ConvertHelper.convert(record, SalaryGroupsReportResource.class);
	}

	private EhSalaryGroupsReportResourcesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryGroupsReportResourcesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryGroupsReportResourcesDao getDao(DSLContext context) {
		return new EhSalaryGroupsReportResourcesDao(context.configuration());
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
