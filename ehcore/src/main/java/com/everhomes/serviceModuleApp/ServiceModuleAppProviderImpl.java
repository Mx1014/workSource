// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.rest.servicemoduleapp.OrganizationAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhOrganizationAppsRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

	@Override
	public List<ServiceModuleApp> listServiceModuleAppsByAppTypeAndKeyword(Long versionId, Byte appType, String keyword) {

		SelectQuery<EhServiceModuleAppsRecord> query = getReadOnlyContext().selectQuery(Tables.EH_SERVICE_MODULE_APPS);
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		if(appType != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(appType));
		}
		if(keyword != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.NAME.like("%" + keyword + "%"));
		}
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc());
		List<ServiceModuleApp> apps = query.fetch().map(r -> ConvertHelper.convert(r, ServiceModuleApp.class));
		return apps;
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleAppsByOrganizationId(Long versionId, Byte appType, String keyword, Long organizationId, Byte installFlag, Byte needSystemAppFlag, Long pageAnchor, int pageSize) {

		SelectQuery query = getReadOnlyContext().select(Tables.EH_SERVICE_MODULE_APPS.fields()).from(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));

		//不查系统应用
		if(needSystemAppFlag == null || needSystemAppFlag.byteValue() == 0){
			Condition systemAppCondition = Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.isNull().or(Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.eq((byte)0));
			query.addConditions(systemAppCondition);
		}


		if(appType != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(appType));
		}
		if(keyword != null){
			query.addJoin(Tables.EH_SERVICE_MODULE_APP_PROFILE, JoinType.LEFT_OUTER_JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APP_PROFILE.ORIGIN_ID));
			Condition condition = Tables.EH_SERVICE_MODULE_APPS.NAME.like("%" + keyword + "%");
			condition = condition.or(Tables.EH_SERVICE_MODULE_APP_PROFILE.APP_NO.like("%" + keyword + "%"));
			query.addConditions(condition);
		}
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()));

		if(installFlag != null && installFlag.byteValue() == 1){
			//已安装
			query.addJoin(Tables.EH_ORGANIZATION_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID));
			query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(organizationId));
			query.addConditions(Tables.EH_ORGANIZATION_APPS.STATUS.ne(OrganizationAppStatus.DELETE.getCode()));
		}else if(installFlag != null && installFlag.byteValue() == 0){
			//未安装
			SelectQuery<EhOrganizationAppsRecord> notExistQuery = getReadOnlyContext().selectQuery(Tables.EH_ORGANIZATION_APPS);
			notExistQuery.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(organizationId));
			notExistQuery.addConditions(Tables.EH_ORGANIZATION_APPS.STATUS.ne(OrganizationAppStatus.DELETE.getCode()));
			notExistQuery.addConditions(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID));
			query.addConditions(DSL.notExists(notExistQuery));
		}

		query.addOrderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc());

		if(pageAnchor != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.ID.gt(pageAnchor));
		}

		query.addLimit(pageSize);

		List<ServiceModuleApp> apps = query.fetch().map(r -> RecordHelper.convert(r, ServiceModuleApp.class));
		return apps;
	}


	@Override
	public List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String keywords, List<Long> developerIds, Byte appType, Byte mobileFlag, Byte pcFlag, Byte independentConfigFlag, Byte supportThirdFlag) {
		Condition cond = Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
		if(null != versionId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		if(null != moduleId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
		if(null != keywords){
			Condition keywordcond = Tables.EH_SERVICE_MODULE_APPS.NAME.like("%"+ keywords + "%").or(Tables.EH_SERVICE_MODULE_APP_PROFILE.APP_NO.like("%"+ keywords + "%"));
			cond = cond.and(keywordcond);
		}
		if(null != developerIds)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APP_PROFILE.DEVELOP_ID.in(developerIds));
		if(null != appType)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(appType));
		if(null != mobileFlag)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APP_PROFILE.MOBILE_FLAG.eq(mobileFlag));
		if(null != pcFlag)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APP_PROFILE.PC_FLAG.eq(pcFlag));
		if(null != independentConfigFlag)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APP_PROFILE.INDEPENDENT_CONFIG_FLAG.eq(independentConfigFlag));
		if(null != supportThirdFlag)
			cond = cond.and(Tables.EH_SERVICE_MODULE_APP_PROFILE.SUPPORT_THIRD_FLAG.eq(supportThirdFlag));

		return getReadOnlyContext().select(
				Tables.EH_SERVICE_MODULE_APPS.ID,
				Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID,
				Tables.EH_SERVICE_MODULE_APPS.VERSION_ID,
				Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID,
				Tables.EH_SERVICE_MODULE_APPS.NAME,
				Tables.EH_SERVICE_MODULE_APPS.MODULE_ID,
				Tables.EH_SERVICE_MODULE_APPS.INSTANCE_CONFIG,
				Tables.EH_SERVICE_MODULE_APPS.STATUS,
				Tables.EH_SERVICE_MODULE_APPS.ACTION_TYPE,
				Tables.EH_SERVICE_MODULE_APPS.CREATE_TIME,
				Tables.EH_SERVICE_MODULE_APPS.UPDATE_TIME,
				Tables.EH_SERVICE_MODULE_APPS.OPERATOR_UID,
				Tables.EH_SERVICE_MODULE_APPS.CREATOR_UID,
				Tables.EH_SERVICE_MODULE_APPS.MODULE_CONTROL_TYPE,
				Tables.EH_SERVICE_MODULE_APPS.CUSTOM_TAG,
				Tables.EH_SERVICE_MODULE_APPS.CUSTOM_PATH,
				Tables.EH_SERVICE_MODULE_APPS.APP_TYPE,
				Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG,
				Tables.EH_SERVICE_MODULE_APPS.DEFAULT_APP_FLAG,
				Tables.EH_SERVICE_MODULE_APPS.ACCESS_CONTROL_TYPE,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.ID.as("profileId"),
				Tables.EH_SERVICE_MODULE_APP_PROFILE.APP_NO,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.DISPLAY_VERSION,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.DESCRIPTION,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.MOBILE_FLAG,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.MOBILE_URIS,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.PC_FLAG,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.PC_URIS,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.APP_ENTRY_INFOS,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.INDEPENDENT_CONFIG_FLAG,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.DEPENDENT_APP_IDS,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.SUPPORT_THIRD_FLAG,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.ICON_URI,
				Tables.EH_SERVICE_MODULE_APP_PROFILE.DEVELOP_ID
		).from(Tables.EH_SERVICE_MODULE_APPS).leftOuterJoin(Tables.EH_SERVICE_MODULE_APP_PROFILE).on(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APP_PROFILE.ORIGIN_ID))
				.where(cond)
				.and(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_SERVICE_MODULE_APPS.ID.asc())
				.fetch().map(r -> 
				RecordHelper.convert(r, ServiceModuleApp.class)
				);
	}


	@Override
	public List<ServiceModuleApp> listManageServiceModuleApps(Integer namespaceId, Long versionId, Long orgId, Byte locationType, Byte appType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Field<?> fieldArr[] = Tables.EH_SERVICE_MODULE_APPS.fields();
		List<Field<?>> fields = new ArrayList<Field<?>>();
		fields.addAll(Arrays.asList(fieldArr));
		fields.add(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_NAME);
		fields.add(Tables.EH_SERVICE_MODULE_ENTRIES.ID);
		SelectQuery<Record> query = context.select(fields).from(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(appType));

		//入口类型
		query.addJoin(Tables.EH_SERVICE_MODULE_ENTRIES, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID));
		query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.LOCATION_TYPE.eq(locationType));
		query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.SCENE_TYPE.eq(ServiceModuleSceneType.MANAGEMENT.getCode()));

		//安装信息
//		query.addJoin(Tables.EH_ORGANIZATION_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID));
//		query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(orgId));

		query.addJoin(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID));
		query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(orgId));

		//同一个公司可能有多个园区给他授权同给一个应用的权限，可能会产生多个授权记录
		query.addGroupBy(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID);

		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.DEFAULT_ORDER.asc());

		List<ServiceModuleApp> apps = query.fetch().map((r) ->{
		    ServiceModuleApp ap = RecordHelper.convert(r, ServiceModuleApp.class);
		    try {

				String name = r.getValue(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_NAME);
				if(!StringUtils.isEmpty(name)) {
					ap.setName(name);
				}
				ap.setEntryId(r.getValue(Tables.EH_SERVICE_MODULE_ENTRIES.ID));
          } catch (IllegalArgumentException ex) {
                //Ignore this exception
            }

		    return ap;
		});
		return apps;
	}

	@Override
	public List<ServiceModuleApp> listInstallServiceModuleApps(Integer namespaceId, Long versionId, Long orgId, Byte locationType, Byte appType, Byte sceneType, Byte organizationAppStatus, Long appCategoryId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Field<?> fieldArr[] = Tables.EH_SERVICE_MODULE_APPS.fields();
		List<Field<?>> fields = new ArrayList<Field<?>>();
		fields.addAll(Arrays.asList(fieldArr));

		if(locationType != null || sceneType != null){
		  fields.add(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_NAME);
		  fields.add(Tables.EH_SERVICE_MODULE_ENTRIES.ID);
		}

		SelectQuery<Record> query = context.select(fields).from(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId));

		if(appType != null){
			query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(appType));
		}



		//入口类型
		if(locationType != null || sceneType != null || appCategoryId != null){
			query.addJoin(Tables.EH_SERVICE_MODULE_ENTRIES, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.MODULE_ID.eq(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID));
			query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.DEFAULT_ORDER.asc());
			if(locationType != null){
				query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.LOCATION_TYPE.eq(locationType));
			}

			if(sceneType != null){
				query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.SCENE_TYPE.eq(sceneType));
			}

			if(appCategoryId != null){
				query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.APP_CATEGORY_ID.eq(appCategoryId));
			}
		}


		//安装信息
		query.addJoin(Tables.EH_ORGANIZATION_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID));
		query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(orgId));
		if(organizationAppStatus != null){
			query.addConditions(Tables.EH_ORGANIZATION_APPS.STATUS.eq(organizationAppStatus));
		}

		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()));



		//query.addGroupBy(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID);

		List<ServiceModuleApp> apps = query.fetch().map((r) -> {
			ServiceModuleApp ap = RecordHelper.convert(r, ServiceModuleApp.class);

			try {
				String name = r.getValue(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_NAME);
				if(locationType != null || sceneType != null && !StringUtils.isEmpty(name)) {
					ap.setName(name);
				}

				ap.setEntryId(r.getValue(Tables.EH_SERVICE_MODULE_ENTRIES.ID));

			} catch (IllegalArgumentException ex) {
				//Ignore this exception
			}

		 return ap;
		});
		return apps;
	}

	@Override
	public List<ServiceModuleApp> listSystemApps(Long versionId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.select(Tables.EH_SERVICE_MODULE_APPS.fields()).from(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.eq((byte)1));
		List<ServiceModuleApp> apps = query.fetch().map(r -> RecordHelper.convert(r, ServiceModuleApp.class));
		return apps;
	}

	@Override
	public List<ServiceModuleApp> listDefaultApps(Long versionId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.select(Tables.EH_SERVICE_MODULE_APPS.fields()).from(Tables.EH_SERVICE_MODULE_APPS).getQuery();
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(versionId));
		query.addConditions(Tables.EH_SERVICE_MODULE_APPS.DEFAULT_APP_FLAG.eq((byte)1));
		List<ServiceModuleApp> apps = query.fetch().map(r -> RecordHelper.convert(r, ServiceModuleApp.class));
		return apps;
	}


}
