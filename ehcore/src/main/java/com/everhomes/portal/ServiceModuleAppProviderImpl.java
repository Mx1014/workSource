// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.portal.ServiceModuleAppStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleAppProviderImpl.class);


	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceModuleApp(ServiceModuleApp serviceModuleApp) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleApps.class));
		serviceModuleApp.setId(id);
		serviceModuleApp.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleApp.setUpdateTime(serviceModuleApp.getCreateTime());
		getReadWriteDao().insert(serviceModuleApp);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleApps.class, null);
	}

	@Override
	public void createServiceModuleApps(List<ServiceModuleApp> serviceModuleApps) {
		LOGGER.debug("create service module app size = {}", serviceModuleApps.size());
		if(serviceModuleApps.size() == 0){
			return;
		}
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleApps.class), (long)serviceModuleApps.size());
		List<EhServiceModuleApps> moduleApps = new ArrayList<>();
		for (ServiceModuleApp moduleApp: serviceModuleApps) {
			id ++;
			moduleApp.setId(id);
			moduleApp.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			moduleApp.setUpdateTime(moduleApp.getCreateTime());
			moduleApps.add(ConvertHelper.convert(moduleApp, EhServiceModuleApps.class));
		}
		getReadWriteDao().insert(moduleApps);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleApps.class, null);
	}


	@Override
	public void updateServiceModuleApp(ServiceModuleApp serviceModuleApp) {
		assert (serviceModuleApp.getId() != null);
		serviceModuleApp.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(serviceModuleApp);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleApps.class, serviceModuleApp.getId());
	}

	@Override
	public ServiceModuleApp findServiceModuleAppById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceModuleApp.class);
	}
	
	@Override
	public List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long moduleId) {
		Condition cond = Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
		if(null != moduleId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_MODULE_APPS)
				.where(cond)
				.and(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
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
