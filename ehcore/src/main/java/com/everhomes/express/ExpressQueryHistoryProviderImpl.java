// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressQueryHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressQueryHistories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
		expressQueryHistory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressQueryHistory.setCreatorUid(UserContext.current().getUser().getId());
		expressQueryHistory.setUpdateTime(expressQueryHistory.getCreateTime());
		expressQueryHistory.setOperatorUid(expressQueryHistory.getCreatorUid());
		getReadWriteDao().insert(expressQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressQueryHistories.class, null);
	}

	@Override
	public void updateExpressQueryHistory(ExpressQueryHistory expressQueryHistory) {
		assert (expressQueryHistory.getId() != null);
		expressQueryHistory.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressQueryHistory.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressQueryHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressQueryHistories.class, expressQueryHistory.getId());
	}

	@Override
	public void clearExpressQueryHistory(Integer namespaceId, Long userId) {
		getReadWriteContext().update(Tables.EH_EXPRESS_QUERY_HISTORIES)
			.set(Tables.EH_EXPRESS_QUERY_HISTORIES.STATUS, CommonStatus.INACTIVE.getCode())
			.set(Tables.EH_EXPRESS_QUERY_HISTORIES.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
			.set(Tables.EH_EXPRESS_QUERY_HISTORIES.OPERATOR_UID, userId)
			.where(Tables.EH_EXPRESS_QUERY_HISTORIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_EXPRESS_QUERY_HISTORIES.CREATOR_UID.eq(userId))
			.and(Tables.EH_EXPRESS_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.execute();
	}

	@Override
	public ExpressQueryHistory findExpressQueryHistoryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressQueryHistory.class);
	}
	
	@Override
	public ExpressQueryHistory findExpressQueryHistory(Integer namespaceId, Long userId, Long expressCompanyId, String billNo) {
		Record record = getReadOnlyContext().select().from(Tables.EH_EXPRESS_QUERY_HISTORIES)
				.where(Tables.EH_EXPRESS_QUERY_HISTORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.EXPRESS_COMPANY_ID.eq(expressCompanyId))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.BILL_NO.eq(billNo))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.CREATOR_UID.eq(userId))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.fetchOne();
		return record == null ? null : ConvertHelper.convert(record, ExpressQueryHistory.class);
	}

	@Override
	public List<ExpressQueryHistory> listExpressQueryHistory() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_QUERY_HISTORIES)
				.orderBy(Tables.EH_EXPRESS_QUERY_HISTORIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressQueryHistory.class));
	}
	
	@Override
	public List<ExpressQueryHistory> listExpressQueryHistoryByUser(Integer namespaecId, Long userId, Integer pageSize) {
		if (pageSize == null) {
			pageSize = 4;
		}
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_QUERY_HISTORIES)
				.where(Tables.EH_EXPRESS_QUERY_HISTORIES.NAMESPACE_ID.eq(namespaecId))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.CREATOR_UID.eq(userId))
				.and(Tables.EH_EXPRESS_QUERY_HISTORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_EXPRESS_QUERY_HISTORIES.UPDATE_TIME.desc())
				.limit(pageSize)
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
