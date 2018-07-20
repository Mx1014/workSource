// @formatter:off
package com.everhomes.visitorsys;

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
import com.everhomes.server.schema.tables.daos.EhVisitorSysMessageReceiversDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysMessageReceivers;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysMessageReceiverProviderImpl implements VisitorSysMessageReceiverProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysMessageReceivers.class));
		visitorSysMessageReceiver.setId(id);
		visitorSysMessageReceiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysMessageReceiver.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysMessageReceiver.setOperateTime(visitorSysMessageReceiver.getCreateTime());
		visitorSysMessageReceiver.setOperatorUid(visitorSysMessageReceiver.getCreatorUid());
		getReadWriteDao().insert(visitorSysMessageReceiver);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysMessageReceivers.class, null);
	}

	@Override
	public void updateVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver) {
		assert (visitorSysMessageReceiver.getId() != null);
		visitorSysMessageReceiver.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysMessageReceiver.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysMessageReceiver);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysMessageReceivers.class, visitorSysMessageReceiver.getId());
	}

	@Override
	public VisitorSysMessageReceiver findVisitorSysMessageReceiverById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysMessageReceiver.class);
	}
	
	@Override
	public List<VisitorSysMessageReceiver> listVisitorSysMessageReceiver() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS)
				.orderBy(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysMessageReceiver.class));
	}

	@Override
	public VisitorSysMessageReceiver findMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId, Long createorId) {
		List<VisitorSysMessageReceiver> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS)
				.where(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.CREATOR_UID.eq(createorId))
				.orderBy(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysMessageReceiver.class));
		return list==null || list.size()==0?null:list.get(0);
	}

	@Override
	public void deleteMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId, Long createorId) {
		getReadWriteContext().delete(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS)
				.where(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.CREATOR_UID.eq(createorId))
				.execute();
	}

	@Override
	public List<VisitorSysMessageReceiver> listVisitorSysMessageReceiverByOwner(Integer namespaceId, String ownerType, Long ownerId) {
		List<VisitorSysMessageReceiver> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS)
				.where(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.OWNER_ID.eq(ownerId))
				.orderBy(Tables.EH_VISITOR_SYS_MESSAGE_RECEIVERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysMessageReceiver.class));
		return list;
	}

	private EhVisitorSysMessageReceiversDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysMessageReceiversDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysMessageReceiversDao getDao(DSLContext context) {
		return new EhVisitorSysMessageReceiversDao(context.configuration());
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
