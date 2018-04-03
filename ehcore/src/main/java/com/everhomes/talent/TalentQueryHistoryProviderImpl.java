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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTalentQueryHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhTalentQueryHistories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentQueryHistoryProviderImpl implements TalentQueryHistoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalentQueryHistory(TalentQueryHistory talentQueryHistory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalentQueryHistories.class));
		talentQueryHistory.setId(id);
		talentQueryHistory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentQueryHistory.setCreatorUid(UserContext.current().getUser().getId());
		talentQueryHistory.setUpdateTime(talentQueryHistory.getCreateTime());
		talentQueryHistory.setOperatorUid(talentQueryHistory.getCreatorUid());
		getReadWriteDao().insert(talentQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalentQueryHistories.class, null);
	}

	@Override
	public void updateTalentQueryHistory(TalentQueryHistory talentQueryHistory) {
		assert (talentQueryHistory.getId() != null);
		talentQueryHistory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talentQueryHistory.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talentQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalentQueryHistories.class, talentQueryHistory.getId());
	}

	@Override
	public void deleteTalentQueryHistory(TalentQueryHistory talentQueryHistory) {
		getReadWriteDao().delete(talentQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalentQueryHistories.class, talentQueryHistory.getId());
	}
	
	@Override
	public TalentQueryHistory findTalentQueryHistoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TalentQueryHistory.class);
	}
	
	@Override
	public TalentQueryHistory findTalentQueryHistoryByKeyword(Long userId, String keyword) {
		Record record = getReadOnlyContext().select().from(Tables.EH_TALENT_QUERY_HISTORIES)
			.where(Tables.EH_TALENT_QUERY_HISTORIES.CREATOR_UID.eq(userId))
			.and(Tables.EH_TALENT_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.and(Tables.EH_TALENT_QUERY_HISTORIES.KEYWORD.eq(keyword))
			.fetchOne();
		
		return record == null ? null : ConvertHelper.convert(record, TalentQueryHistory.class);
	}

	@Override
	public List<TalentQueryHistory> listTalentQueryHistory() {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_QUERY_HISTORIES)
				.orderBy(Tables.EH_TALENT_QUERY_HISTORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TalentQueryHistory.class));
	}
	
	@Override
	public List<TalentQueryHistory> listTalentQueryHistoryByUser(Long userId) {
		return getReadOnlyContext().select().from(Tables.EH_TALENT_QUERY_HISTORIES)
				.where(Tables.EH_TALENT_QUERY_HISTORIES.CREATOR_UID.eq(userId))
				.and(Tables.EH_TALENT_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_TALENT_QUERY_HISTORIES.ID.desc())
				.limit(6)
				.fetch().map(r -> ConvertHelper.convert(r, TalentQueryHistory.class));
	}

	private EhTalentQueryHistoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentQueryHistoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentQueryHistoriesDao getDao(DSLContext context) {
		return new EhTalentQueryHistoriesDao(context.configuration());
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
