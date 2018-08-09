// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserAppsDao;
import com.everhomes.server.schema.tables.pojos.EhUserApps;
import com.everhomes.server.schema.tables.records.EhUserAppsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserAppProviderImpl implements UserAppProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createUserApp(UserApp userApp) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserApps.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		userApp.setId(id);

		EhUserAppsDao dao = new EhUserAppsDao(context.configuration());
		dao.insert(userApp);
	}

	@Override
	public void updateUserApp(UserApp userApp) {
		assert userApp.getId() != null;

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		EhUserAppsDao dao = new EhUserAppsDao(context.configuration());
		dao.update(userApp);
	}

	@Override
	public UserApp findUserAppById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		EhUserAppsDao dao = new EhUserAppsDao(context.configuration());
		EhUserApps userApp = dao.findById(id);

		return ConvertHelper.convert(userApp, UserApp.class);
	}

	@Override
	public List<UserApp> listUserApps(Long userId, Byte locationType, Long locationTargetId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		SelectQuery<EhUserAppsRecord> query = context.selectQuery(Tables.EH_USER_APPS);
		query.addConditions(Tables.EH_USER_APPS.USER_ID.eq(userId));
		query.addConditions(Tables.EH_USER_APPS.LOCATION_TYPE.eq(locationType));
		query.addConditions(Tables.EH_USER_APPS.LOCATION_TARGET_ID.eq(locationTargetId));

		query.addOrderBy(Tables.EH_USER_APPS.ORDER.asc());

		List<UserApp> userApps = query.fetchInto(UserApp.class);

		return userApps;
	}

	@Override
	public void delete(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		EhUserAppsDao dao = new EhUserAppsDao(context.configuration());
		dao.deleteById(id);
	}


	@Override
	public void deleteByUserId(Long userId, Byte locationType, Long locationTargetId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		DeleteQuery<EhUserAppsRecord> query = context.deleteQuery(Tables.EH_USER_APPS);
		query.addConditions(Tables.EH_USER_APPS.USER_ID.eq(userId));
		query.addConditions(Tables.EH_USER_APPS.LOCATION_TYPE.eq(locationType));
		query.addConditions(Tables.EH_USER_APPS.LOCATION_TARGET_ID.eq(locationTargetId));
		query.execute();
	}


	@Override
	public void delete(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		EhUserAppsDao dao = new EhUserAppsDao(context.configuration());
		dao.deleteById(ids);
	}
}
