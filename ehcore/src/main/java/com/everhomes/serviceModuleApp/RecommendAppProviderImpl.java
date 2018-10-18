// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRecommendAppsDao;
import com.everhomes.server.schema.tables.daos.EhUserAppsDao;
import com.everhomes.server.schema.tables.pojos.EhRecommendApps;
import com.everhomes.server.schema.tables.pojos.EhUserApps;
import com.everhomes.server.schema.tables.records.EhRecommendAppsRecord;
import com.everhomes.server.schema.tables.records.EhUserAppsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendAppProviderImpl implements RecommendAppProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;


	@Override
	public void createRecommendApp(RecommendApp recommendApp) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRecommendApps.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		recommendApp.setId(id);

		EhRecommendAppsDao dao = new EhRecommendAppsDao(context.configuration());
		dao.insert(recommendApp);
	}

	@Override
	public void updateRecommendApp(RecommendApp recommendApp) {
		assert recommendApp.getId() != null;

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		EhRecommendAppsDao dao = new EhRecommendAppsDao(context.configuration());
		dao.update(recommendApp);
	}

	@Override
	public RecommendApp findRecommendAppById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		EhRecommendAppsDao dao = new EhRecommendAppsDao(context.configuration());
		EhRecommendApps recommendApp = dao.findById(id);

		return ConvertHelper.convert(recommendApp, RecommendApp.class);
	}

	@Override
	public List<RecommendApp> listRecommendApps(Byte scopeType, Long scopeId, Long appId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		SelectQuery<EhRecommendAppsRecord> query = context.selectQuery(Tables.EH_RECOMMEND_APPS);
		query.addConditions(Tables.EH_RECOMMEND_APPS.SCOPE_TYPE.eq(scopeType));
		query.addConditions(Tables.EH_RECOMMEND_APPS.SCOPE_ID.eq(scopeId));
		if(appId != null){
			query.addConditions(Tables.EH_RECOMMEND_APPS.APP_ID.eq(appId));
		}

		query.addOrderBy(Tables.EH_RECOMMEND_APPS.ORDER.asc());

		List<RecommendApp> recommendApps = query.fetchInto(RecommendApp.class);

		return recommendApps;
	}

	@Override
	public void delete(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		EhRecommendAppsDao dao = new EhRecommendAppsDao(context.configuration());
		dao.deleteById(id);
	}

	@Override
	public void delete(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		EhRecommendAppsDao dao = new EhRecommendAppsDao(context.configuration());
		dao.deleteById(ids);
	}

	@Override
	public void deleteByScope(Byte scopeType, Long scopeId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		DeleteQuery<EhRecommendAppsRecord> query = context.deleteQuery(Tables.EH_RECOMMEND_APPS);
		query.addConditions(Tables.EH_RECOMMEND_APPS.SCOPE_TYPE.eq(scopeType));
		query.addConditions(Tables.EH_RECOMMEND_APPS.SCOPE_ID.eq(scopeId));
		query.execute();
	}

	@Override
	public Integer findMaxOrder(Byte scopeType, Long scopeId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRecommendApps.class));
		Integer maxId = context.select(Tables.EH_RECOMMEND_APPS.ORDER.max())
				.from(Tables.EH_RECOMMEND_APPS)
				.where(Tables.EH_RECOMMEND_APPS.SCOPE_TYPE.eq(scopeType)
						.and(Tables.EH_RECOMMEND_APPS.SCOPE_ID.eq(scopeId)))
				.fetchOne().value1();

		return maxId;
	}
}
