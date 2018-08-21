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
import com.everhomes.server.schema.tables.daos.EhPunchDayLogFilesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogFiles;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PunchDayLogFileProviderImpl implements PunchDayLogFileProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchDayLogFile(PunchDayLogFile punchDayLogFile) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchDayLogFiles.class));
		punchDayLogFile.setId(id);
		punchDayLogFile.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchDayLogFile.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(punchDayLogFile);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchDayLogFiles.class, null);
	}

	@Override
	public void updatePunchDayLogFile(PunchDayLogFile punchDayLogFile) {
		assert (punchDayLogFile.getId() != null);
		getReadWriteDao().update(punchDayLogFile);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchDayLogFiles.class, punchDayLogFile.getId());
	}

	@Override
	public PunchDayLogFile findPunchDayLogFileById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchDayLogFile.class);
	}
	
	@Override
	public List<PunchDayLogFile> listPunchDayLogFile() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_DAY_LOG_FILES)
				.orderBy(Tables.EH_PUNCH_DAY_LOG_FILES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchDayLogFile.class));
	}
	
	private EhPunchDayLogFilesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchDayLogFilesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchDayLogFilesDao getDao(DSLContext context) {
		return new EhPunchDayLogFilesDao(context.configuration());
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
