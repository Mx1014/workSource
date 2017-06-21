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
import com.everhomes.server.schema.tables.daos.EhSalaryDefaultEntriesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryDefaultEntries;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryDefaultEntryProviderImpl implements SalaryDefaultEntryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryDefaultEntry(SalaryDefaultEntry salaryDefaultEntry) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryDefaultEntries.class));
		salaryDefaultEntry.setId(id);
		salaryDefaultEntry.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryDefaultEntry.setCreatorUid(UserContext.current().getUser().getId());
//		salaryDefaultEntry.setUpdateTime(salaryDefaultEntry.getCreateTime());
//		salaryDefaultEntry.setOperatorUid(salaryDefaultEntry.getCreatorUid());
		getReadWriteDao().insert(salaryDefaultEntry);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryDefaultEntries.class, null);
	}

	@Override
	public void updateSalaryDefaultEntry(SalaryDefaultEntry salaryDefaultEntry) {
		assert (salaryDefaultEntry.getId() != null);
//		salaryDefaultEntry.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryDefaultEntry.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryDefaultEntry);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryDefaultEntries.class, salaryDefaultEntry.getId());
	}

	@Override
	public SalaryDefaultEntry findSalaryDefaultEntryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryDefaultEntry.class);
	}
	
	@Override
	public List<SalaryDefaultEntry> listSalaryDefaultEntry() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_DEFAULT_ENTRIES)
				.orderBy(Tables.EH_SALARY_DEFAULT_ENTRIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryDefaultEntry.class));
	}
	
	private EhSalaryDefaultEntriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryDefaultEntriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryDefaultEntriesDao getDao(DSLContext context) {
		return new EhSalaryDefaultEntriesDao(context.configuration());
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
