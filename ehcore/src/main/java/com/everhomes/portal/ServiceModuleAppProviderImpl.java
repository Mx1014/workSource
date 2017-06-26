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
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ServiceModuleAppProviderImpl implements ServiceModuleAppProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceModuleApp(ServiceModuleApp serviceModuleApp) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleApps.class));
		serviceModuleApp.setId(id);
		serviceModuleApp.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleApp.setCreatorUid(UserContext.current().getUser().getId());
		serviceModuleApp.setUpdateTime(serviceModuleApp.getCreateTime());
		serviceModuleApp.setOperatorUid(serviceModuleApp.getCreatorUid());
		getReadWriteDao().insert(serviceModuleApp);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleApps.class, null);
	}

	@Override
	public void updateServiceModuleApp(ServiceModuleApp serviceModuleApp) {
		assert (serviceModuleApp.getId() != null);
		serviceModuleApp.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleApp.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(serviceModuleApp);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleApps.class, serviceModuleApp.getId());
	}

	@Override
	public ServiceModuleApp findServiceModuleAppById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceModuleApp.class);
	}
	
	@Override
	public List<ServiceModuleApp> listServiceModuleApp() {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_MODULE_APPS)
				.orderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));
	}
	
	private EhServiceModuleAppsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhServiceModuleAppsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhServiceModuleAppsDao getDao(DSLContext context) {
		return new EhServiceModuleAppsDao(context.configuration());
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
