package com.everhomes.journal;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.journal.JournalStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhJournalConfigsDao;
import com.everhomes.server.schema.tables.daos.EhJournalsDao;
import com.everhomes.server.schema.tables.pojos.EhJournalConfigs;
import com.everhomes.server.schema.tables.pojos.EhJournals;
import com.everhomes.server.schema.tables.records.EhJournalConfigsRecord;
import com.everhomes.server.schema.tables.records.EhJournalsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class JournalProviderImpl implements JournalProvider {
	
	@Autowired
    private DbProvider dbProvider;
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	@Override
	public Journal findJournal(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhJournals.class));
		EhJournalsDao dao = new EhJournalsDao(context.configuration());
		
		return ConvertHelper.convert(dao.findById(id), Journal.class);
	}

	@Override
	public void createJournal(Journal journal) {
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhJournals.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhJournalsDao dao = new EhJournalsDao(context.configuration());
		journal.setId(id);
		dao.insert(journal);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhJournals.class, null);
	}
	
	@Override
	public void updateJournal(Journal journal) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhJournalsDao dao = new EhJournalsDao(context.configuration());
		dao.update(journal);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhJournals.class, null);
	}

	@Override
	public void updateJournalConfig(JournalConfig journalConfig) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhJournalConfigsDao dao = new EhJournalConfigsDao(context.configuration());
		dao.update(journalConfig);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhJournalConfigs.class, null);
		
	}

	@Override
	public List<Journal> listJournals(Integer namespaceId, String keyword,
			Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhJournals.class));
		SelectQuery<EhJournalsRecord> query = context.selectQuery(Tables.EH_JOURNALS);
		if(namespaceId != null)
			query.addConditions(Tables.EH_JOURNALS.NAMESPACE_ID.eq(namespaceId));
		if(StringUtils.isNotBlank(keyword))
			query.addConditions(Tables.EH_JOURNALS.TITLE.eq(keyword));
		if(pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_JOURNALS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		query.addConditions(Tables.EH_JOURNALS.STATUS.eq(JournalStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_JOURNALS.CREATE_TIME.desc());
		
		if(pageSize != null)
			query.addLimit(pageSize);
		List<Journal> ret = query.fetch().stream().map( r -> ConvertHelper.convert(r, Journal.class))
				.collect(Collectors.toList());
		return ret;
	}

	@Override
	public JournalConfig findJournalConfig(Integer namespaceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhJournalConfigs.class));
		SelectQuery<EhJournalConfigsRecord> query = context.selectQuery(Tables.EH_JOURNAL_CONFIGS);
		query.addConditions(Tables.EH_JOURNAL_CONFIGS.NAMESPACE_ID.eq(namespaceId));
		return ConvertHelper.convert(query.fetchOne(), JournalConfig.class);
	}

	@Override
	public void createJournalConfig(JournalConfig journalConfig) {
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhJournalConfigs.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhJournalConfigsDao dao = new EhJournalConfigsDao(context.configuration());
		journalConfig.setId(id);
		dao.insert(journalConfig);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhJournalConfigs.class, null);
		
	}

	
}
