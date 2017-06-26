// @formatter:off
package com.everhomes.portal;

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
import com.everhomes.server.schema.tables.daos.EhPortalPublishLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalPublishLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalPublishLogProviderImpl implements PortalPublishLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalPublishLog(PortalPublishLog portalPublishLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalPublishLogs.class));
		portalPublishLog.setId(id);
		portalPublishLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalPublishLog.setCreatorUid(UserContext.current().getUser().getId());
		portalPublishLog.setUpdateTime(portalPublishLog.getCreateTime());
		portalPublishLog.setOperatorUid(portalPublishLog.getCreatorUid());
		getReadWriteDao().insert(portalPublishLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalPublishLogs.class, null);
	}

	@Override
	public void updatePortalPublishLog(PortalPublishLog portalPublishLog) {
		assert (portalPublishLog.getId() != null);
		portalPublishLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalPublishLog.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(portalPublishLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalPublishLogs.class, portalPublishLog.getId());
	}

	@Override
	public PortalPublishLog findPortalPublishLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalPublishLog.class);
	}
	
	@Override
	public List<PortalPublishLog> listPortalPublishLog() {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_PUBLISH_LOGS)
				.orderBy(Tables.EH_PORTAL_PUBLISH_LOGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalPublishLog.class));
	}
	
	private EhPortalPublishLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalPublishLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalPublishLogsDao getDao(DSLContext context) {
		return new EhPortalPublishLogsDao(context.configuration());
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
