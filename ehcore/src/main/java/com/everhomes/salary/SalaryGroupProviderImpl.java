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
import com.everhomes.server.schema.tables.daos.EhSalaryGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryGroups;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryGroupProviderImpl implements SalaryGroupProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryGroup(SalaryGroup salaryGroup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroups.class));
		salaryGroup.setId(id);
		salaryGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryGroup.setCreatorUid(UserContext.current().getUser().getId());
//		salaryGroup.setUpdateTime(salaryGroup.getCreateTime());
//		salaryGroup.setOperatorUid(salaryGroup.getCreatorUid());
		getReadWriteDao().insert(salaryGroup);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroups.class, null);
	}

	@Override
	public void updateSalaryGroup(SalaryGroup salaryGroup) {
		assert (salaryGroup.getId() != null);
//		salaryGroup.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroup.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryGroup);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroups.class, salaryGroup.getId());
	}

	@Override
	public SalaryGroup findSalaryGroupById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroup.class);
	}
	
	@Override
	public List<SalaryGroup> listSalaryGroup() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS)
				.orderBy(Tables.EH_SALARY_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroup.class));
	}
	
	private EhSalaryGroupsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryGroupsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryGroupsDao getDao(DSLContext context) {
		return new EhSalaryGroupsDao(context.configuration());
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
	public List<SalaryGroup> listSalaryGroup(String ownerType, Long ownerId, String period,
			List<Byte> status) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS)
				.where(Tables.EH_SALARY_GROUPS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SALARY_GROUPS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_GROUPS.SALARY_PERIOD.eq(period))
				.and(Tables.EH_SALARY_GROUPS.STATUS.in(status))
				.orderBy(Tables.EH_SALARY_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroup.class));
	}

	@Override
	public List<SalaryGroup> listSalaryGroup(Byte status, Timestamp date) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS)
				.where(Tables.EH_SALARY_GROUPS.STATUS.eq(status))
				.and(Tables.EH_SALARY_GROUPS.SEND_TIME.lessOrEqual(date))
				.orderBy(Tables.EH_SALARY_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroup.class));
	}

	@Override
	public void updateSalaryGroupEmailContent(String ownerType, Long ownerId, String emailContent) {
		getReadWriteContext().update(Tables.EH_SALARY_GROUPS).set(Tables.EH_SALARY_GROUPS.EMAIL_CONTENT, emailContent)
				.where(Tables.EH_SALARY_GROUPS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SALARY_GROUPS.OWNER_TYPE.eq(ownerType)).execute();
	}

	@Override
	public void deleteSalaryGroup(Long organizationGroupId, String salaryPeriod) {
		getReadWriteContext().delete(Tables.EH_SALARY_GROUPS)
				.where(Tables.EH_SALARY_GROUPS.ORGANIZATION_GROUP_ID.eq(organizationGroupId))
				.and(Tables.EH_SALARY_GROUPS.SALARY_PERIOD.eq(salaryPeriod)).execute();
	}

	@Override
	public SalaryGroup findSalaryGroupByOrgId(Long id, String lastPeriod) {
		Record result = getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS)
				.where(Tables.EH_SALARY_GROUPS.ORGANIZATION_GROUP_ID.eq(id))
				.and(Tables.EH_SALARY_GROUPS.SALARY_PERIOD.eq(lastPeriod))
				.fetchOne();
		if(null == result)
			return null;
		return ConvertHelper.convert(result, SalaryGroup.class);
	}

}
