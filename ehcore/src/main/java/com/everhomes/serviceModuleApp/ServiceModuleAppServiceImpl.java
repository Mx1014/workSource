// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.acl.ServiceModuleAppProfile;
import com.everhomes.acl.ServiceModuleAppProfileProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.acl.ServiceModuleEntryConstans;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.launchpadbase.ListLaunchPadAppsCommand;
import com.everhomes.rest.launchpadbase.ListLaunchPadAppsResponse;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Card;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleLocationType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.rest.servicemoduleapp.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import com.google.gson.reflect.TypeToken;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ServiceModuleAppServiceImpl implements ServiceModuleAppService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleAppServiceImpl.class);

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private PortalVersionProvider portalVersionProvider;
	@Autowired
	private PortalService portalService;

	@Autowired
	private OrganizationAppProvider organizationAppProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private ServiceModuleEntryProvider serviceModuleEntryProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private AppCommunityConfigProvider appCommunityConfigProvider;

	@Autowired
	private ServiceModuleAppProfileProvider serviceModuleAppProfileProvider;

	@Autowired
	private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;

	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleApps(Integer namespaceId) {

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, releaseVersion.getId(), null);

		return serviceModuleApps;
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleApps(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String customPath) {

		if(versionId == null){
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
			if(releaseVersion != null){
				versionId = releaseVersion.getId();
			}
		}

		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, versionId, moduleId);

		return moduleApps;
	}

	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleAppsByOriginIds(Integer namespaceId, List<Long> originIds) {

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleAppsByOriginIds(releaseVersion.getId(), originIds);

		return moduleApps;
	}

	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleAppByModuleIds(Integer namespaceId, List<Long> moduleIds){
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> apps = new ArrayList<>();
		if(releaseVersion != null){
			apps = serviceModuleAppProvider.listServiceModuleAppByModuleIds(namespaceId, releaseVersion.getId(), moduleIds);
		}

		return apps;
	}


	@Override
	public List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String controlType){
		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, versionId, moduleId, actionType, customTag, controlType);
		return apps;
	}

	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String controlType){
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		List<ServiceModuleApp> apps = new ArrayList<>();
		if(releaseVersion != null){
			apps = listServiceModuleApp(namespaceId, releaseVersion.getId(), moduleId, actionType, customTag , controlType);
		}

		return apps;
	}

    @Override
    public List<Long> listReleaseServiceModuleIdsByNamespace(Integer namespaceId) {
		List<Long> moduleIds = new ArrayList<>();
		List<ServiceModuleApp> apps = listReleaseServiceModuleApps(namespaceId);

		if(apps != null && apps.size() > 0){
			Set<Long> set = apps.stream().map(r -> r.getModuleId()).collect(Collectors.toSet());
			moduleIds = new ArrayList<>(set);
		}
		return moduleIds;
    }

	@Override
	public List<Long> listReleaseServiceModuleIdsWithParentByNamespace(Integer namespaceId) {
		List<Long> moduleIds = listReleaseServiceModuleIdsByNamespace(namespaceId);
		List<ServiceModule> modules = this.serviceModuleProvider.listServiceModule(moduleIds);
		Set<Long> process_moduleIds = new HashSet<>();
		modules.stream().map(r -> {
			String[] ids = r.getPath().split("/");
			for (String id : ids) {
				if(!id.equals(""))
					process_moduleIds.add(Long.valueOf(id));
			}
			return null;
		}).collect(Collectors.toList());

		return new ArrayList<>(process_moduleIds);
	}

	@Override
	public ServiceModuleApp findReleaseServiceModuleAppByOriginId(Long originId) {

		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleAppByOriginId(originId);
		if(serviceModuleApps != null && serviceModuleApps.size()> 0){

			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(serviceModuleApps.get(0).getNamespaceId());

			for (ServiceModuleApp app: serviceModuleApps){
				if(releaseVersion.getId().longValue() == app.getVersionId().longValue()){
					return app;
				}
			}

		}

		return null;
	}


	@Override
	public ListServiceModuleAppsForBannerResponse listServiceModuleAppsForBanner(ListServiceModuleAppsForBannerCommand cmd) {

		List<ServiceModuleApp> apps = listReleaseServiceModuleApps(cmd.getNamespaceId());

		if(apps == null){
			return null;
		}

		List<ServiceModuleAppDTO> dtos = new ArrayList<>();
		for (ServiceModuleApp app: apps){
			if(app.getActionType() == null){
				continue;
			}
			ServiceModuleAppDTO dto = ConvertHelper.convert(app, ServiceModuleAppDTO.class);
			PortalPublishHandler handler = portalService.getPortalPublishHandler(app.getModuleId());

			if(null != handler){
				dto.setInstanceConfig(handler.getItemActionData(app.getNamespaceId(), app.getInstanceConfig()));
			}
			dtos.add(dto);
		}

		ListServiceModuleAppsForBannerResponse  response = new ListServiceModuleAppsForBannerResponse();
		response.setApps(dtos);
		return response;
	}

	@Override
	public ServiceModuleAppDTO installApp(InstallAppCommand cmd) {

		ServiceModuleApp serviceModuleApp = findReleaseServiceModuleAppByOriginId(cmd.getOriginId());

		if(serviceModuleApp == null){
			LOGGER.error("app not found, originId = {}", cmd.getOriginId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_SERVICEMODULEAPP_NOT_FOUND, "app not found, originId = " + cmd.getOriginId());
		}


		OrganizationApp orgapps = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(cmd.getOriginId(), cmd.getOrganizationId());

		if(orgapps != null){
			LOGGER.error("already install this app, originId = {}", cmd.getOriginId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_ALREADY_INSTALL_THIS_APP, "already install this app, originId = " + cmd.getOriginId());
		}

		orgapps = new OrganizationApp();
		orgapps.setAppOriginId(cmd.getOriginId());
		orgapps.setOrgId(cmd.getOrganizationId());
		orgapps.setStatus(OrganizationAppStatus.ENABLE.getCode());
		orgapps.setCreatorUid(UserContext.currentUserId());
		orgapps.setCreateTime(new Timestamp(System.currentTimeMillis()));

		Long id = organizationAppProvider.createOrganizationApp(orgapps);

		//给自己添加自己园区的所有授权
		serviceModuleAppAuthorizationService.addAllCommunityAppAuthorizations(serviceModuleApp.getNamespaceId(), cmd.getOrganizationId(), cmd.getOriginId());

		ServiceModuleAppDTO dto = ConvertHelper.convert(serviceModuleApp, ServiceModuleAppDTO.class);
		dto.setStatus(orgapps.getStatus());
		dto.setOrgAppId(id);

		return dto;
	}

	@Override
	public void uninstallApp(UninstallAppCommand cmd) {

		OrganizationApp orgapp = organizationAppProvider.getOrganizationAppById(cmd.getOrgAppId());

		if(orgapp == null || OrganizationAppStatus.DELETE == OrganizationAppStatus.fromCode(orgapp.getStatus())){
			LOGGER.error("org app not found, orgAppId = {}", cmd.getOrgAppId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_ORG_APP_NOT_FOUND, "org app not found, orgAppId = " + cmd.getOrgAppId());
		}

		orgapp.setStatus(OrganizationAppStatus.DELETE.getCode());
		orgapp.setOperatorUid(UserContext.currentUserId());
		orgapp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
		organizationAppProvider.updateOrganizationApp(orgapp);

		//删除该公司的所有授权
		serviceModuleAppAuthorizationService.removeAllCommunityAppAuthorizations(UserContext.getCurrentNamespaceId(), orgapp.getOrgId(), orgapp.getAppOriginId());

	}

	@Override
	public void uninstallAppByOrganizationId(Long organizationId) {
		//TODO
	}


	@Override
	public ListServiceModuleAppsByOrganizationIdResponse listServiceModuleAppsByOrganizationId(ListServiceModuleAppsByOrganizationIdCommand cmd) {

		ListServiceModuleAppsByOrganizationIdResponse response = new  ListServiceModuleAppsByOrganizationIdResponse();

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(cmd.getNamespaceId());

		int pageSize = 20;
		if(cmd.getPageSize() != null){
			pageSize = cmd.getPageSize();
		}

		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsByOrganizationId(releaseVersion.getId(), cmd.getAppType(), cmd.getKeyword(), cmd.getOrganizationId(), cmd.getInstallFlag(), null, cmd.getPageAnchor(), pageSize + 1);
		if(apps == null){
			return null;
		}

		if(apps.size() > pageSize){
			apps.remove(pageSize);
			response.setNextPageAnchor(apps.get(pageSize - 1).getId());
		}

		List<ServiceModuleAppDTO> dtos = new ArrayList<>();
		for (ServiceModuleApp app: apps){
			ServiceModuleAppDTO dto = ConvertHelper.convert(app, ServiceModuleAppDTO.class);
			OrganizationApp orgapp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(app.getOriginId(), cmd.getOrganizationId());
			if (orgapp != null){
				dto.setOrgAppId(orgapp.getId());
				dto.setStatus(orgapp.getStatus());
			}

			ServiceModuleAppProfile profile = serviceModuleAppProfileProvider.findServiceModuleAppProfileByOriginId(app.getOriginId());
			if(profile != null){
				dto.setAppNo(profile.getAppNo());
				dto.setDisplayVersion(profile.getDisplayVersion());
				String url = contentServerService.parserUri(profile.getIconUri(), dto.getClass().getSimpleName(), dto.getId());
				dto.setIconUrl(url);
				dto.setDescription(profile.getDescription());

				if(profile.getPcUris() != null){
					List<String> pcUris = GsonUtil.fromJson(profile.getPcUris(), new TypeToken<List<String>>(){}.getType());
					List<String> pcUrls = contentServerService.parserUri(pcUris, profile.getClass().getSimpleName(), profile.getId());
					dto.setPcUris(pcUris);
					dto.setPcUrls(pcUrls);
				}

				if(profile.getMobileUris() != null){
					List<String> mobileUris = GsonUtil.fromJson(profile.getMobileUris(), new TypeToken<List<String>>(){}.getType());
					List<String> mobileUrls = contentServerService.parserUri(mobileUris, profile.getClass().getSimpleName(), profile.getId());
					dto.setMobileUris(mobileUris);
					dto.setMobileUrls(mobileUrls);
				}

				if(profile.getDependentAppIds() != null){
					List<Long> dependentAppIds = GsonUtil.fromJson(profile.getDependentAppIds(), new TypeToken<List<Long>>(){}.getType());
					List<String> dependentAppNames = new ArrayList<>();
					if(dependentAppIds != null && dependentAppIds.size() > 0){
						for (Long id: dependentAppIds){
							ServiceModuleApp moduleAppByOriginId = findReleaseServiceModuleAppByOriginId(id);
							if(moduleAppByOriginId != null){
								dependentAppNames.add(moduleAppByOriginId.getName());
							}
						}
					}
					dto.setDependentAppIds(dependentAppIds);
					dto.setDependentAppNames(dependentAppNames);
				}

				if(profile.getAppEntryInfos() != null){
					List<AppEntryInfoDTO> entryInfos = GsonUtil.fromJson(profile.getAppEntryInfos(), new TypeToken<List<AppEntryInfoDTO>>(){}.getType());
					dto.setAppEntryInfos(entryInfos);
				}
			}

			dtos.add(dto);
		}


		response.setServiceModuleApps(dtos);
		return response;
	}


	@Override
	public ListLaunchPadAppsResponse listLaunchPadApps(ListLaunchPadAppsCommand cmd) {

		List<AppDTO> appDtos = new ArrayList<>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		Long orgId = null;
		Byte appType = null;
		Byte locationType = null;
		Byte sceneType = null;

		if(Widget.fromCode(cmd.getWidget()) == Widget.CARD){
			locationType = ServiceModuleLocationType.MOBILE_WORKPLATFORM.getCode();
			Card cardConfig = (Card)StringHelper.fromJsonString(cmd.getInstanceConfig(), Card.class);
			if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.OA){
				orgId = cmd.getContext().getOrganizationId();
				appType = ServiceModuleAppType.OA.getCode();
				sceneType = ServiceModuleSceneType.CLIENT.getCode();
			}else if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.COMMUNITY){
				OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getContext().getCommunityId());
				orgId = organizationProperty.getOrganizationId();
				appType = ServiceModuleAppType.COMMUNITY.getCode();
				sceneType = ServiceModuleSceneType.MANAGEMENT.getCode();
			}
		}else if (Widget.fromCode(cmd.getWidget()) == Widget.NAVIGATOR){
			locationType = ServiceModuleLocationType.MOBILE_COMMUNITY.getCode();
			OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getContext().getCommunityId());
			orgId = organizationProperty.getOrganizationId();
			appType = ServiceModuleAppType.COMMUNITY.getCode();
			sceneType = ServiceModuleSceneType.CLIENT.getCode();
		}

		List<ServiceModuleApp> apps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, locationType, appType, sceneType);

		if(apps != null && apps.size() > 0){
			for (ServiceModuleApp app: apps){

				AppDTO appDTO = ConvertHelper.convert(app, AppDTO.class);
				appDTO.setName(app.getName());
				if(app.getIconUri() != null){
					String url = contentServerService.parserUri(app.getIconUri(), ServiceModuleAppDTO.class.getSimpleName(), app.getId());
					appDTO.setIconUrl(url);
				}

				ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(app.getModuleId());
				appDTO.setClientHandlerType(serviceModule.getClientHandlerType());

				appDtos.add(appDTO);
			}
		}

		ListLaunchPadAppsResponse response = new ListLaunchPadAppsResponse();
		response.setApps(appDtos);
		return response;
	}

	@Override
	public void updateStatus(UpdateStatusCommand cmd) {
		OrganizationApp orgapp = organizationAppProvider.getOrganizationAppById(cmd.getOrgAppId());
		if(orgapp == null){
			LOGGER.error("OrganizationApp not found, orgappid={}", cmd.getOrgAppId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_ORG_APP_NOT_FOUND, "org app not found, orgAppId = " + cmd.getOrgAppId());
		}

		orgapp.setStatus(cmd.getStatus());

		organizationAppProvider.updateOrganizationApp(orgapp);
	}

	@Override
	public void changeCommunityConfigFlag(ChangeCommunityConfigFlagCommand cmd) {
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		if(TrueOrFalseFlag.fromCode(cmd.getAppSelfConfigFlag()) == TrueOrFalseFlag.fromCode(community.getAppSelfConfigFlag())){
			LOGGER.error("Status exception，from status={}, to status={}", community.getAppSelfConfigFlag(), cmd.getAppSelfConfigFlag());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_APP_COMMUNITY_STATUS_EXCEPTION, "Status exception，from status=" + community.getAppSelfConfigFlag() + "to status=" + cmd.getAppSelfConfigFlag());

		}

		community.setAppSelfConfigFlag(cmd.getAppSelfConfigFlag());
		communityProvider.updateCommunity(community);

		//清空原有自定义配置
		appCommunityConfigProvider.deleteAppCommunityConfigByCommunityId(cmd.getCommunityId());

		//查询管理公司
		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getCommunityId());

		//自己配置则复制一份
		if(cmd.getAppSelfConfigFlag().byteValue() == 1){
			//管理公司安装的app

			Organization organization = organizationProvider.findOrganizationById(organizationProperty.getOrganizationId());
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(organization.getNamespaceId());

			List<OrganizationApp> organizationApps = organizationAppProvider.queryOrganizationApps(new ListingLocator(), 100000, new ListingQueryBuilderCallback() {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
					query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(organizationProperty.getOrganizationId()));
					query.addJoin(Tables.EH_SERVICE_MODULE_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID));
					query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(ServiceModuleAppType.COMMUNITY.getCode()));
					query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(releaseVersion.getId()));
					return query;
				}
			});

			//园区自定义的app配置
			for (OrganizationApp organizationApp: organizationApps){
				AppCommunityConfig config = new AppCommunityConfig();
				config.setAppOriginId(organizationApp.getAppOriginId());
				config.setCommunityId(cmd.getCommunityId());
				config.setDisplayName(organizationApp.getDisplayName());
				config.setVisibilityFlag(organizationApp.getVisibilityFlag() == null ? TrueOrFalseFlag.TRUE.getCode(): organizationApp.getVisibilityFlag());
				config.setCreatorUid(UserContext.currentUserId());
				config.setCreateTime(new Timestamp(System.currentTimeMillis()));
				appCommunityConfigProvider.createAppCommunityConfig(config);
			}
		}
	}

	@Override
	public ListAppCommunityConfigsResponse listAppCommunityConfigs(ListAppCommunityConfigsCommand cmd) {
		Community community = null;
		if(cmd.getCommunityId() != null){
			community = communityProvider.findCommunityById(cmd.getCommunityId());
		}

		List<AppCommunityConfigDTO>  dtos = new ArrayList<>();
		if(community != null && community.getAppSelfConfigFlag() != null && community.getAppSelfConfigFlag().byteValue() == 1){

			//自定义配置的
			List<AppCommunityConfig> appCommunityConfigs = appCommunityConfigProvider.queryAppCommunityConfigs(new ListingLocator(), 100000, new ListingQueryBuilderCallback() {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
					query.addConditions(Tables.EH_APP_COMMUNITY_CONFIGS.COMMUNITY_ID.eq(cmd.getCommunityId()));
					return query;
				}
			});

			if(appCommunityConfigs != null){
				for (AppCommunityConfig config: appCommunityConfigs){
					AppCommunityConfigDTO dto = getAppCommunityConfigDTO(config);
					if(dto != null){
						dtos.add(dto);
					}

				}
			}

		}else {

			//跟随默认方案的
			//管理公司安装的app

			if(cmd.getOrganizationId() == null){
				List<OrganizationCommunityDTO> orgcoms = organizationProvider.findOrganizationCommunityByCommunityId(cmd.getCommunityId());
				if(orgcoms != null && orgcoms.size() > 0){
					cmd.setOrganizationId(orgcoms.get(0).getOrganizationId());
				}
			}

			Organization organization = organizationProvider.findOrganizationById(cmd.getOrganizationId());
			if(organization == null){
				return null;
			}
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(organization.getNamespaceId());

			List<OrganizationApp> organizationApps = organizationAppProvider.queryOrganizationApps(new ListingLocator(), 100000, new ListingQueryBuilderCallback() {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
					query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(cmd.getOrganizationId()));
					query.addJoin(Tables.EH_SERVICE_MODULE_APPS, JoinType.JOIN, Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID));
					query.addConditions(Tables.EH_SERVICE_MODULE_APPS.APP_TYPE.eq(ServiceModuleAppType.COMMUNITY.getCode()));
					query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(releaseVersion.getId()));
					return query;
				}
			});

			if(organizationApps != null){
				for (OrganizationApp app: organizationApps){
					AppCommunityConfig config = new AppCommunityConfig();
					config.setAppOriginId(app.getAppOriginId());
					config.setDisplayName(app.getDisplayName());
					config.setVisibilityFlag(app.getVisibilityFlag() == null ? TrueOrFalseFlag.TRUE.getCode() : app.getVisibilityFlag());
					AppCommunityConfigDTO dto = getAppCommunityConfigDTO(config);

					if(dto != null){
						dtos.add(dto);
					}

				}

			}

		}

		ListAppCommunityConfigsResponse response = new ListAppCommunityConfigsResponse();
		response.setDtos(dtos);
		return response;
	}

	private AppCommunityConfigDTO getAppCommunityConfigDTO(AppCommunityConfig config){
		AppCommunityConfigDTO dto = ConvertHelper.convert(config, AppCommunityConfigDTO.class);
		ServiceModuleApp serviceModuleApp = this.findReleaseServiceModuleAppByOriginId(dto.getAppOriginId());

		if(serviceModuleApp == null || TrueOrFalseFlag.fromCode(serviceModuleApp.getSystemAppFlag()) == TrueOrFalseFlag.TRUE){
			return null;
		}

		dto.setServiceModuleAppName(serviceModuleApp.getName());

		ServiceModuleAppProfile profile = serviceModuleAppProfileProvider.findServiceModuleAppProfileByOriginId(dto.getAppOriginId());

		if(profile != null){
			dto.setAppNo(profile.getAppNo());
			String url = contentServerService.parserUri(profile.getIconUri(), profile.getClass().getSimpleName(), profile.getOriginId());
			dto.setIconUrl(url);
		}

		return dto;
	}

	@Override
	public void updateAppCommunityConfig(UpdateAppCommunityConfigCommand cmd) {

		if(cmd.getCommunityId() != null){
			AppCommunityConfig config = appCommunityConfigProvider.findAppCommunityConfigByCommunityIdAndAppOriginId(cmd.getCommunityId(), cmd.getAppOriginId());
			if(config == null){
				config = new AppCommunityConfig();
				config.setAppOriginId(cmd.getAppOriginId());
				config.setCreateTime(new Timestamp(System.currentTimeMillis()));
				config.setCreatorUid(UserContext.currentUserId());
				config.setDisplayName(cmd.getDisplayName());
				config.setVisibilityFlag(cmd.getVisibilityFlag());
				appCommunityConfigProvider.createAppCommunityConfig(config);
			}else {
				config.setDisplayName(cmd.getDisplayName());
				config.setVisibilityFlag(cmd.getVisibilityFlag());
				config.setOperatorUid(UserContext.currentUserId());
				config.setOperatorTime(new Timestamp(System.currentTimeMillis()));
				appCommunityConfigProvider.updateAppCommunityConfig(config);
			}

		}else {
			OrganizationApp orgapp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(cmd.getAppOriginId(), cmd.getOrganizationId());
			orgapp.setDisplayName(cmd.getDisplayName());
			orgapp.setVisibilityFlag(cmd.getVisibilityFlag());
			organizationAppProvider.updateOrganizationApp(orgapp);
		}

	}

	/**
	 *
	 * @param organizationId
	 */
	@Override
	public void installDefaultAppByOrganizationId(Long organizationId){
		Organization organization = organizationProvider.findOrganizationById(organizationId);
		if(organization == null){
			return;
		}
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(organization.getNamespaceId());
		List<ServiceModuleApp> defaultApps = serviceModuleAppProvider.listDefaultApps(releaseVersion.getId());
		List<ServiceModuleApp> systemApps = serviceModuleAppProvider.listSystemApps(releaseVersion.getId());

		List<ServiceModuleApp> apps = new ArrayList<>();
		apps.addAll(defaultApps);
		apps.addAll(systemApps);

		if(apps != null){
			InstallAppCommand cmd = new InstallAppCommand();
			cmd.setOrganizationId(organizationId);
			for (ServiceModuleApp app: apps){
				//管理公司安装全部、其他公司安装oa应用
				if(TrueOrFalseFlag.fromCode(organization.getPmFlag()) == TrueOrFalseFlag.TRUE
						|| ServiceModuleAppType.fromCode(app.getAppType()) == ServiceModuleAppType.OA) {
					cmd.setOriginId(app.getOriginId());
					installApp(cmd);
				}
			}
		}
	}

	/**
	 * 给所有公司安装一个应用
	 * @param cmd
	 */
	@Override
	public void installAppForAllOrganizations(InstallAppForAllOrganizationsCommand cmd) {

		ServiceModuleApp serviceModuleApp = findReleaseServiceModuleAppByOriginId(cmd.getOriginId());

		if(serviceModuleApp == null){
			LOGGER.error("app not found, originId = {}", cmd.getOriginId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_SERVICEMODULEAPP_NOT_FOUND, "app not found, originId = " + cmd.getOriginId());
		}

		//找出该域空间所有的公司
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Organization> organizations = this.organizationProvider.listEnterpriseByNamespaceIds(serviceModuleApp.getNamespaceId(), null,
				null, null, locator, 1000000);


		//给每个公司安装应用
		for (Organization org: organizations){

			//管理公司安装全部、其他公司安装oa应用
			if(TrueOrFalseFlag.fromCode(org.getPmFlag()) == TrueOrFalseFlag.TRUE
					|| ServiceModuleAppType.fromCode(serviceModuleApp.getAppType()) == ServiceModuleAppType.OA) {

				//已经安装的跳过
				OrganizationApp orgapps = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(cmd.getOriginId(), org.getId());
				if(orgapps != null){
					LOGGER.info("already install this app, orgid = {}, originId = {}", org.getId(), cmd.getOriginId());
					continue;
				}

				orgapps = new OrganizationApp();
				orgapps.setAppOriginId(cmd.getOriginId());
				orgapps.setOrgId(org.getId());
				orgapps.setStatus(OrganizationAppStatus.ENABLE.getCode());
				orgapps.setCreatorUid(UserContext.currentUserId());
				orgapps.setCreateTime(new Timestamp(System.currentTimeMillis()));

				//安装
				organizationAppProvider.createOrganizationApp(orgapps);

				//给自己添加自己园区的所有授权
				serviceModuleAppAuthorizationService.addAllCommunityAppAuthorizations(serviceModuleApp.getNamespaceId(), org.getId(), cmd.getOriginId());
			}

		}

	}
}
