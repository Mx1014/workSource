// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressQueryHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressQueryHistories;
import com.everhomes.util.ConvertHelper;

@Component
public class ExpressQueryHistoryProviderImpl implements ExpressQueryHistoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressQueryHistory(ExpressQueryHistory expressQueryHistory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressQueryHistories.class));
		expressQueryHistory.setId(id);
		getReadWriteDao().insert(expressQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressQueryHistories.class, null);
	}

	@Override
	public void updateExpressQueryHistory(ExpressQueryHistory expressQueryHistory) {
		assert (expressQueryHistory.getId() != null);
		getReadWriteDao().update(expressQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressQueryHistories.class, expressQueryHistory.getId());
	}

	@Override
	public ExpressQueryHistory findExpressQueryHistoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressQueryHistory.class);
	}
	
	@Override
	public List<ExpressQueryHistory> listExpressQueryHistory() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_QUERY_HISTORIES)
				.orderBy(Tables.EH_EXPRESS_QUERY_HISTORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressQueryHistory.class));
	}
	
	private EhExpressQueryHistoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressQueryHistoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressQueryHistoriesDao getDao(DSLContext context) {
		return new EhExpressQueryHistoriesDao(context.configuration());
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
