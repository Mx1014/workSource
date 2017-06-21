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
import com.everhomes.server.schema.tables.daos.EhSalaryGroupEntriesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryGroupEntries;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryGroupEntryProviderImpl implements SalaryGroupEntityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryGroupEntity(SalaryGroupEntity salaryGroupEntry) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroupEntries.class));
		salaryGroupEntry.setId(id);
		salaryGroupEntry.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryGroupEntry.setCreatorUid(UserContext.current().getUser().getId());
//		salaryGroupEntry.setUpdateTime(salaryGroupEntry.getCreateTime());
//		salaryGroupEntry.setOperatorUid(salaryGroupEntry.getCreatorUid());
		getReadWriteDao().insert(salaryGroupEntry);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroupEntries.class, null);
	}

	@Override
	public void updateSalaryGroupEntity(SalaryGroupEntity salaryGroupEntry) {
		assert (salaryGroupEntry.getId() != null);
//		salaryGroupEntry.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroupEntry.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryGroupEntry);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupEntries.class, salaryGroupEntry.getId());
	}

	@Override
	public SalaryGroupEntity findSalaryGroupEntityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroupEntity.class);
	}
	
	@Override
	public List<SalaryGroupEntity> listSalaryGroupEntity() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTRIES)
				.orderBy(Tables.EH_SALARY_GROUP_ENTRIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
	}
	
	private EhSalaryGroupEntriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryGroupEntriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryGroupEntriesDao getDao(DSLContext context) {
		return new EhSalaryGroupEntriesDao(context.configuration());
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
