// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.tables.daos.EhUserAppsDao;
import com.everhomes.server.schema.tables.pojos.EhUserApps;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserAppProviderImpl implements UserAppProvider {

	@Autowired
	private DbProvider dbProvider;

	@Override
	public void createUserApp(UserApp userApp) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));

		EhUserAppsDao dao = new EhUserAppsDao(context);
		context
	}

	@Override
	public void updateUserApp(UserApp userApp) {

	}

	@Override
	public ServiceModuleApp findUserAppById(Long id) {
		return null;
	}

	@Override
	public List<ServiceModuleApp> listUserApps(Integer namespaceId, Long versionId, Long moduleId) {
		return null;
	}

	@Override
	public void deleteUserApp(Long id) {

	}
}
