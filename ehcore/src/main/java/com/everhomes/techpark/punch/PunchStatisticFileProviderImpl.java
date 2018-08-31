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
import com.everhomes.server.schema.tables.daos.EhPunchStatisticFilesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchStatisticFiles;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PunchStatisticFileProviderImpl implements PunchStatisticFileProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchStatisticFile(PunchStatisticFile punchStatisticFile) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchStatisticFiles.class));
		punchStatisticFile.setId(id);
		punchStatisticFile.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchStatisticFile.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(punchStatisticFile);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchStatisticFiles.class, null);
	}

	@Override
	public void updatePunchStatisticFile(PunchStatisticFile punchStatisticFile) {
		assert (punchStatisticFile.getId() != null);
		getReadWriteDao().update(punchStatisticFile);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchStatisticFiles.class, punchStatisticFile.getId());
	}

	@Override
	public PunchStatisticFile findPunchStatisticFileById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchStatisticFile.class);
	}
	
	@Override
	public List<PunchStatisticFile> listPunchStatisticFile() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_STATISTIC_FILES)
				.orderBy(Tables.EH_PUNCH_STATISTIC_FILES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchStatisticFile.class));
	}
	
	private EhPunchStatisticFilesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchStatisticFilesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchStatisticFilesDao getDao(DSLContext context) {
		return new EhPunchStatisticFilesDao(context.configuration());
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
