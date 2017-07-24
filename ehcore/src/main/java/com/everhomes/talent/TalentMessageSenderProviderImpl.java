// @formatter:off
package com.everhomes.talent;

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
import com.everhomes.server.schema.tables.daos.EhTalentMessageSendersDao;
import com.everhomes.server.schema.tables.pojos.EhTalentMessageSenders;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentMessageSenderProviderImpl implements TalentMessageSenderProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalentMessageSender(TalentMessageSender talentMessageSender) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalentMessageSenders.class));
		talentMessageSender.setId(id);
		talentMessageSender.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentMessageSender.setCreatorUid(UserContext.current().getUser().getId());
		talentMessageSender.setUpdateTime(talentMessageSender.getCreateTime());
		talentMessageSender.setOperatorUid(talentMessageSender.getCreatorUid());
		getReadWriteDao().insert(talentMessageSender);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalentMessageSenders.class, null);
	}

	@Override
	public void updateTalentMessageSender(TalentMessageSender talentMessageSender) {
		assert (talentMessageSender.getId() != null);
		talentMessageSender.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentMessageSender.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talentMessageSender);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalentMessageSenders.class, talentMessageSender.getId());
	}

	@Override
	public TalentMessageSender findTalentMessageSenderById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TalentMessageSender.class);
	}
	
	@Override
	public TalentMessageSender findTalentMessageSender(Integer namespaceId, String ownerType, Long ownerId,
			Long organizationMemberId, Long userId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_TALENT_MESSAGE_SENDERS)
				.where(Tables.EH_TALENT_MESSAGE_SENDERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_TALENT_MESSAGE_SENDERS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_TALENT_MESSAGE_SENDERS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_TALENT_MESSAGE_SENDERS.ORGANIZATION_MEMBER_ID.eq(organizationMemberId))
				.and(Tables.EH_TALENT_MESSAGE_SENDERS.USER_ID.eq(userId))
				.fetchOne();
		return record == null ? null : ConvertHelper.convert(record, TalentMessageSender.class);
	}

	@Override
	public List<TalentMessageSender> listTalentMessageSender() {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_MESSAGE_SENDERS)
				.orderBy(Tables.EH_TALENT_MESSAGE_SENDERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentMessageSender.class));
	}
	
	@Override
	public List<TalentMessageSender> listTalentMessageSenderByOwner(String ownerType, Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_MESSAGE_SENDERS)
				.where(Tables.EH_TALENT_MESSAGE_SENDERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_TALENT_MESSAGE_SENDERS.OWNER_ID.eq(ownerId))
				.orderBy(Tables.EH_TALENT_MESSAGE_SENDERS.ID.desc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentMessageSender.class));
	}

	private EhTalentMessageSendersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentMessageSendersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentMessageSendersDao getDao(DSLContext context) {
		return new EhTalentMessageSendersDao(context.configuration());
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
