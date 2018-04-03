// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhStatEventAppAttachmentLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventAppAttachmentLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StatEventAppAttachmentLogProviderImpl implements StatEventAppAttachmentLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventAppAttachmentLog(StatEventAppAttachmentLog statEventAppAttachmentLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventAppAttachmentLogs.class));
		statEventAppAttachmentLog.setId(id);
		statEventAppAttachmentLog.setCreateTime(DateUtils.currentTimestamp());
		statEventAppAttachmentLog.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventAppAttachmentLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventAppAttachmentLogs.class, id);
	}

	@Override
	public void updateStatEventAppAttachmentLog(StatEventAppAttachmentLog statEventAppAttachmentLog) {
		// statEventAppAttachmentLog.setUpdateTime(DateUtils.currentTimestamp());
		// statEventAppAttachmentLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventAppAttachmentLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventAppAttachmentLogs.class, statEventAppAttachmentLog.getId());
	}

	@Override
	public StatEventAppAttachmentLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventAppAttachmentLog.class);
	}

	private EhStatEventAppAttachmentLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventAppAttachmentLogsDao(context.configuration());
	}

	private EhStatEventAppAttachmentLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventAppAttachmentLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
