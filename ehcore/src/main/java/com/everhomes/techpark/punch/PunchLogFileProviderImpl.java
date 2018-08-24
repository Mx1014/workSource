// @formatter:off
package com.everhomes.techpark.punch;

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
import com.everhomes.server.schema.tables.daos.EhPunchLogFilesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchLogFiles;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PunchLogFileProviderImpl implements PunchLogFileProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchLogFile(PunchLogFile punchLogFile) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchLogFiles.class));
		punchLogFile.setId(id);
		getReadWriteDao().insert(punchLogFile);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchLogFiles.class, null);
	}

	@Override
	public void updatePunchLogFile(PunchLogFile punchLogFile) {
		assert (punchLogFile.getId() != null);
		getReadWriteDao().update(punchLogFile);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchLogFiles.class, punchLogFile.getId());
	}

	@Override
	public PunchLogFile findPunchLogFileById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchLogFile.class);
	}
	
	@Override
	public List<PunchLogFile> listPunchLogFile() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_LOG_FILES)
				.orderBy(Tables.EH_PUNCH_LOG_FILES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchLogFile.class));
	}
	
	private EhPunchLogFilesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchLogFilesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchLogFilesDao getDao(DSLContext context) {
		return new EhPunchLogFilesDao(context.configuration());
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
