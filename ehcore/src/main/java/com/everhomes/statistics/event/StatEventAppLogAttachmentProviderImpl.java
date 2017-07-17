// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhStatEventAppLogAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventAppLogAttachments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StatEventAppLogAttachmentProviderImpl implements StatEventAppLogAttachmentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventAppLogAttachment(StatEventAppLogAttachment statEventAppLogAttachment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventAppLogAttachments.class));
		statEventAppLogAttachment.setId(id);
		statEventAppLogAttachment.setCreateTime(DateUtils.currentTimestamp());
		statEventAppLogAttachment.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventAppLogAttachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventAppLogAttachments.class, id);
	}

	@Override
	public void updateStatEventAppLogAttachment(StatEventAppLogAttachment statEventAppLogAttachment) {
        rwDao().update(statEventAppLogAttachment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventAppLogAttachments.class, statEventAppLogAttachment.getId());
	}

	@Override
	public StatEventAppLogAttachment findStatEventAppLogAttachmentById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventAppLogAttachment.class);
	}
	
	// @Override
	// public List<StatEventAppLogAttachment> listStatEventAppLogAttachment() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_APP_LOG_ATTACHMENTS)
	//			.orderBy(Tables.EH_STAT_EVENT_APP_LOG_ATTACHMENTS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventAppLogAttachment.class));
	// }
	
	private EhStatEventAppLogAttachmentsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventAppLogAttachmentsDao(context.configuration());
	}

	private EhStatEventAppLogAttachmentsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventAppLogAttachmentsDao(context.configuration());
	}
}
