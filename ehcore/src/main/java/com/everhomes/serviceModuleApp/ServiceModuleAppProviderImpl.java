// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

		if(serviceModuleApp.getOriginId() == null){
			serviceModuleApp.setOriginId(id);
		}

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
		/**
		 * 有id使用原来的id，没有则生成新的
		 */
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleApps.class), (long)serviceModuleApps.size() + 1);
		List<EhServiceModuleApps> moduleApps = new ArrayList<>();
		for (ServiceModuleApp moduleApp: serviceModuleApps) {
			if(moduleApp.getId() == null){
				id ++;
				moduleApp.setId(id);
			}

			if(moduleApp.getOriginId() == null){
				moduleApp.setOriginId(id);
			}

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
	public List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId){
		return listServiceModuleApp(namespaceId, versionId, moduleId, null, null, null);
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppByActionType(Integer namespaceId, Long versionId, Byte actionType){
		return listServiceModuleApp(namespaceId, versionId, null, actionType, null, null);
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppsByModuleIds(Integer namespaceId, Long versionId, List<Long> moduleIds) {
		Condition cond = Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
		if (null != moduleIds && moduleIds.size() > 0)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.in(moduleIds));

		if(versionId != null){
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		}
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_MODULE_APPS)
				.where(cond)
				.and(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppsByOriginIds(Long versionId, List<Long> originIds) {

		SelectQuery<EhServiceModuleAppsRecord> query = getReadOnlyContext().selectQuery(Tables.EH_SERVICE_MODULE_APPS);
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.in(originIds));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc());
		List<ServiceModuleApp> apps = query.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));
		return apps;
	}


	@Override
	public void deleteByVersionId(Long versionId){
		DeleteQuery query = getReadWriteContext().deleteQuery(Tables.EH_SERVICE_MODULE_APPS);
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleApps.class, null);
	}


	@Override
	public List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String controlType) {
		Condition cond = Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
		if(null != versionId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		if(null != moduleId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
		if(null != actionType)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.ACTION_TYPE.eq(actionType));
		if(null != customTag)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.CUSTOM_TAG.eq(customTag));
		if(null != controlType)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_CONTROL_TYPE.eq(controlType));

		return getReadOnlyContext().select().from(Tables.EH_SERVICE_MODULE_APPS)
				.where(cond)
				.and(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppByModuleIds(Integer namespaceId, Long versionId, List<Long> moduleIds) {
		Condition cond = Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
		if(null != versionId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		if(null != moduleIds)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.in(moduleIds));
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

	@Override
	public ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String customTag) {

		SelectQuery<EhServiceModuleAppsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId));
		if(versionId != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		}

		if(moduleId != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
		}

		if(customTag != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.CUSTOM_TAG.eq(customTag));
		}
		ServiceModuleApp app = query.fetchAnyInto(ServiceModuleApp.class);

		return  app;
	}

	@Override
	public ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Byte actionType, String instanceConfig) {

		SelectQuery<EhServiceModuleAppsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId));
		if(versionId != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		}

		if(instanceConfig != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.INSTANCE_CONFIG.eq(instanceConfig));
		}

		if(actionType!= null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.ACTION_TYPE.eq(actionType));
		}
		ServiceModuleApp app = query.fetchAnyInto(ServiceModuleApp.class);

		return  app;
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppByOriginId(Long originId) {

		SelectQuery<EhServiceModuleAppsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(originId));

		List<ServiceModuleApp> apps = query.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));

		return  apps;
	}
}
