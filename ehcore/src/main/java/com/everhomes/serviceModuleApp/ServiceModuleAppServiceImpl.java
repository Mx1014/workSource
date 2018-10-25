// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.acl.ServiceModuleAppProfile;
import com.everhomes.acl.ServiceModuleAppProfileProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.launchpad.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.*;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.common.OwnerType;
import com.everhomes.rest.launchpad.LaunchPadCategoryDTO;
import com.everhomes.rest.launchpad.ListAllAppsResponse;
import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Card;
import com.everhomes.rest.module.*;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.portal.AllOrMoreType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import com.google.gson.reflect.TypeToken;
import org.jooq.*;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	private SequenceProvider sequenceProvider;

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

	@Autowired
	private RouterInfoService routerService;

	@Autowired
	private LaunchPadService launchPadService;

	@Autowired
	private CommunityBizProvider communityBizProvider;

	@Autowired
	private AppCategoryProvider appCategoryProvider;


	@Autowired
	private UserAppProvider userAppProvider;

	@Autowired
	private UserAppFlagProvider userAppFlagProvider;


	@Autowired
	private RecommendAppProvider recommendAppProvider;


    @Autowired
    private DbProvider dbProvider;

    @Autowired
	private LaunchPadConfigProvider launchPadConfigProvider;

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


		//增加应用广场配置
		List<OrganizationCommunity> orgcommunities = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId());
		if(orgcommunities != null){
			for (OrganizationCommunity organizationCommunity: orgcommunities) {
				UpdateAppCommunityConfigCommand appCommunityConfigCommand = new UpdateAppCommunityConfigCommand();
				appCommunityConfigCommand.setAppOriginId(cmd.getOriginId());
				appCommunityConfigCommand.setCommunityId(organizationCommunity.getCommunityId());
				appCommunityConfigCommand.setDisplayName(serviceModuleApp.getName());
				appCommunityConfigCommand.setVisibilityFlag(TrueOrFalseFlag.TRUE.getCode());
				updateAppCommunityConfig(appCommunityConfigCommand);
			}
		}

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
		serviceModuleAppAuthorizationService.removeAllCommunityAppAuthorizations(orgapp.getOrgId(), orgapp.getAppOriginId());

		//删除应用广场配置
		List<OrganizationCommunity> orgcommunities = organizationProvider.listOrganizationCommunities(orgapp.getOrgId());
		if(orgcommunities != null){
			for (OrganizationCommunity organizationCommunity: orgcommunities){
				appCommunityConfigProvider.deleteAppCommunityConfigByCommunityIdAndAppOriginId(organizationCommunity.getCommunityId(), orgapp.getAppOriginId());
			}

		}

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
		Long communityId = null;
		Byte appType = null;
		Byte locationType = null;
		Byte sceneType = null;


		//广场和工作台OA是查询安装的应用
		boolean installFlag = false;

		//工作台园区运营是查询授权管理的应用
		boolean manageFlag = false;

		//广场查询用户自定义的应用
        boolean userAppFlag = false;

		if(Widget.fromCode(cmd.getWidget()) == Widget.CARD){
			locationType = ServiceModuleLocationType.MOBILE_WORKPLATFORM.getCode();
			orgId = cmd.getContext().getOrganizationId();
			Card cardConfig = (Card)StringHelper.fromJsonString(cmd.getInstanceConfig(), Card.class);
			if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.OA){
				appType = ServiceModuleAppType.OA.getCode();
				//sceneType = ServiceModuleSceneType.CLIENT.getCode();

				installFlag = true;
			} else if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.COMMUNITY){
				//OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getContext().getCommunityId());
				//orgId = organizationProperty.getOrganizationId();
				appType = ServiceModuleAppType.COMMUNITY.getCode();
				sceneType = ServiceModuleSceneType.MANAGEMENT.getCode();

				manageFlag = true;
			}

		}else if (Widget.fromCode(cmd.getWidget()) == Widget.NAVIGATOR){
			locationType = ServiceModuleLocationType.MOBILE_COMMUNITY.getCode();
			communityId = cmd.getContext().getCommunityId();
			OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
			orgId = organizationProperty.getOrganizationId();
			appType = ServiceModuleAppType.COMMUNITY.getCode();
			sceneType = ServiceModuleSceneType.CLIENT.getCode();

            userAppFlag = true;
		}

		List<ServiceModuleApp> apps = new ArrayList<>();
		if(installFlag) {
			apps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, locationType, appType, sceneType, OrganizationAppStatus.ENABLE.getCode(), null);
		}else if(manageFlag){
			apps = serviceModuleAppProvider.listManageServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, locationType, appType);
		}else if(userAppFlag){
			apps = findUserCommunityApps(communityId);
		}

		if(apps != null && apps.size() > 0){
			for (ServiceModuleApp app: apps){

				AppDTO appDTO = toAppDto(app, sceneType, communityId, orgId);
				if(appDTO == null){
					continue;
				}

				appDtos.add(appDTO);
			}

			//广场加上全部
			if(userAppFlag){
				AppDTO allIcon = getAllIcon(communityId, orgId, AllOrMoreType.ALL.getCode());
				appDtos.add(allIcon);
			}

		}

		ListLaunchPadAppsResponse response = new ListLaunchPadAppsResponse();
		response.setApps(appDtos);
		return response;
	}

	private List<ServiceModuleApp> findUserCommunityApps(Long communityId){

		List<ServiceModuleApp> apps = new ArrayList<>();

		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
		Long orgId = organizationProperty.getOrganizationId();

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> tempApps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), ServiceModuleAppType.COMMUNITY.getCode(), ServiceModuleSceneType.CLIENT.getCode(), OrganizationAppStatus.ENABLE.getCode(), null);
		if(tempApps != null && tempApps.size() > 0) {

			//用户是否启用自定义配置
			UserAppFlag userAppFlag = userAppFlagProvider.findUserAppFlag(UserContext.currentUserId(), ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), communityId);

			//有用户自定义应用
			if(userAppFlag != null){
				List<UserApp> userApps = userAppProvider.listUserApps(UserContext.currentUserId(), ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), communityId);
				if (userApps != null && userApps.size() != 0) {
					for (UserApp userApp : userApps) {
						for (ServiceModuleApp app : tempApps) {
							if (userApp.getAppId().equals(app.getOriginId())) {
								apps.add(app);
							}
						}
					}
				}
			} else {
				//List<UserApp> userApps = userAppProvider.listUserApps(UserContext.currentUserId(), ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), communityId);

				Byte scopeType = ScopeType.ORGANIZATION.getCode();
				Long scopeId = orgId;

				Community community = communityProvider.findCommunityById(communityId);

				//园区自定义配置的
				if (community != null && community.getAppSelfConfigFlag() != null && community.getAppSelfConfigFlag().byteValue() == 1) {
					scopeType = ScopeType.COMMUNITY.getCode();
					scopeId = communityId;
				}

				List<RecommendApp> recommendApps = recommendAppProvider.listRecommendApps(scopeType, scopeId, null);
				//有推荐的应用
				if (recommendApps != null && recommendApps.size() != 0) {
					for (RecommendApp recommendApp : recommendApps) {
						for (ServiceModuleApp app : tempApps) {
							if (recommendApp.getAppId().equals(app.getOriginId())) {
								apps.add(app);
							}
						}
					}


				}
			}
		}

		return apps;
	}


	private AppDTO getAllIcon(Long communityId, Long orgId, String allOrMoreType){

		AppDTO allDto = new AppDTO();
		allDto.setAppId(-1L);
		if(AllOrMoreType.fromCode(allOrMoreType) == AllOrMoreType.MORE){
			allDto.setName("更多");
		}else {
			allDto.setName("全部");
		}

		allDto.setModuleId(-10000L);
		allDto.setClientHandlerType((byte)0);
		//填充路由信息
		RouterInfo routerInfo = convertRouterInfo(allDto.getModuleId(), allDto.getAppId(), allDto.getName(), null, "/" + AllOrMoreType.fromCode(allOrMoreType).getCode(), null, null);
		allDto.setRouterPath(routerInfo.getPath());
		allDto.setRouterQuery(routerInfo.getQuery());

		Community community = communityProvider.findCommunityById(communityId);

		Byte ownerType = OwnerType.ORGANIZATION.getCode();
		Long ownerId = orgId;
		//园区自定义配置的
		if(community != null && community.getAppSelfConfigFlag() != null && community.getAppSelfConfigFlag().byteValue() == 1){
			ownerType = OwnerType.COMMUNITY.getCode();
			ownerId = communityId;
		}

		LaunchPadConfig launchPadConfig = launchPadConfigProvider.findLaunchPadConfig(ownerType, ownerId);
		if(launchPadConfig != null){
			String url = contentServerService.parserUri(launchPadConfig.getNavigatorAllIconUri(), LaunchPadConfig.class.getSimpleName(), launchPadConfig.getId());
			allDto.setIconUrl(url);
		}


		return allDto;

	}

	// 移动端广场需要编辑名称，所以需要 communityId, orgId
	private AppDTO toAppDto(ServiceModuleApp app, Byte sceneType, Long communityId, Long orgId){

		AppDTO appDTO = ConvertHelper.convert(app, AppDTO.class);

		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(app.getModuleId());


		//广场的应用设置可见性，并编辑的名称。
		if(ServiceModuleAppType.fromCode(serviceModule.getAppType()) == ServiceModuleAppType.COMMUNITY && ServiceModuleSceneType.fromCode(sceneType) == ServiceModuleSceneType.CLIENT){

			Community community = communityProvider.findCommunityById(communityId);

			//园区自定义配置的
			if(community != null && community.getAppSelfConfigFlag() != null && community.getAppSelfConfigFlag().byteValue() == 1){
				AppCommunityConfig appCommunity = appCommunityConfigProvider.findAppCommunityConfigByCommunityIdAndAppOriginId(communityId, app.getOriginId());

				if(appCommunity != null){
					if(appCommunity.getVisibilityFlag() != null && appCommunity.getVisibilityFlag().byteValue() == 0){
						//隐藏的应用要去掉
						return null;
					}
					if(appCommunity.getDisplayName() != null){
						appDTO.setName(appCommunity.getDisplayName());
					}
				}

			}else {
				//跟随公司的
				OrganizationApp orgapp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(app.getOriginId(), orgId);

				if(orgapp != null){
					if(orgapp.getVisibilityFlag() != null && orgapp.getVisibilityFlag().byteValue() == 0){
						//隐藏的应用要去掉
						return null;
					}

					if(orgapp.getDisplayName() != null){
						appDTO.setName(orgapp.getDisplayName());
					}
				}
			}
		}

		appDTO.setAppId(app.getOriginId());


		appDTO.setClientHandlerType(serviceModule.getClientHandlerType());

		PortalPublishHandler handler = portalService.getPortalPublishHandler(app.getModuleId());
		if(handler != null){
			String itemActionData = handler.getItemActionData(app.getNamespaceId(), app.getInstanceConfig());
			if(itemActionData != null){
				app.setInstanceConfig(itemActionData);
			}
		}

		app.setInstanceConfig(launchPadService.refreshActionData(app.getInstanceConfig()));


		Byte routerLocationType = null;
		Byte routerSceneType = null;
		if(app.getEntryId() != null){
			ServiceModuleEntry entry = serviceModuleEntryProvider.findById(app.getEntryId());
			if(entry != null){
				routerLocationType = entry.getLocationType();
				routerSceneType = entry.getSceneType();
			}

			//优先使用entryIcon
			if(!StringUtils.isEmpty(entry.getIconUri())){
				String url = contentServerService.parserUri(entry.getIconUri(), entry.getClass().getName(), entry.getId());
				appDTO.setIconUrl(url);
			}else {
				ServiceModuleAppProfile profile = serviceModuleAppProfileProvider.findServiceModuleAppProfileByOriginId(app.getOriginId());
				if(profile != null && profile.getIconUri() != null){
					String url = contentServerService.parserUri(profile.getIconUri(), ServiceModuleAppDTO.class.getSimpleName(), app.getId());
					appDTO.setIconUrl(url);
				}
			}

		}

		//填充路由信息
		RouterInfo routerInfo = convertRouterInfo(appDTO.getModuleId(), app.getOriginId(), appDTO.getName(), app.getInstanceConfig(), null, routerLocationType, routerSceneType);
		appDTO.setRouterPath(routerInfo.getPath());
		appDTO.setRouterQuery(routerInfo.getQuery());

		return appDTO;
	}

	@Override
	public RouterInfo convertRouterInfo(Long moduleId, Long appId, String title, String actionData, String path, Byte locationType, Byte sceneType){

		if(StringUtils.isEmpty(path)){
			path = "/index";
		}

		String query = "appId=" + appId;

        if(locationType != null){
            query = query + "&locationType=" + locationType;
        }

        if(sceneType != null){
            query = query + "&sceneType=" + sceneType;
        }

        try {
			// 加上默认的参数appId和displayName
			query = query + "&displayName=" + URLEncoder.encode(title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("query encode, query=", query);
		}

		RouterInfo routerInfo = routerService.getRouterInfo(moduleId, path, actionData);
		if(routerInfo != null){
			if(routerInfo.getQuery() != null){
				query = query  + "&" + routerInfo.getQuery();
			}
			//routerInfo.setQuery(query);

		}else {
			routerInfo = new RouterInfo();

			//没有实现接口的模块默认的返回
			routerInfo.setPath(path);
			String queryInDefaultWay = routerService.getQueryInDefaultWay(actionData);
			if(queryInDefaultWay != null){
				query = query  + "&" + queryInDefaultWay;
			}
		}
		routerInfo.setQuery(query);
		return routerInfo;
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

		//清空原有首页推荐
		recommendAppProvider.deleteByScope(ScopeType.COMMUNITY.getCode(), cmd.getCommunityId());

		//查询管理公司
		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getCommunityId());

		// 删除自定义电商配置
		CommunityBiz communityBiz = communityBizProvider.findCommunityBiz(null, community.getId(), null);
		if(communityBiz != null){
			communityBizProvider.deleteCommunityBiz(communityBiz.getId());
		}

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

			//园区自定义电商配置
			CommunityBiz organizationCommunityBiz = communityBizProvider.findCommunityBiz(organization.getId(), null, null);
			if(organizationCommunityBiz != null){
				organizationCommunityBiz.setId(null);
				organizationCommunityBiz.setOrganizationId(null);
				organizationCommunityBiz.setCommunityId(community.getId());
				communityBizProvider.createCommunityBiz(organizationCommunityBiz);
			}
		}
	}

	@Override
	public ListAppCommunityConfigsResponse listAppCommunityConfigs(ListAppCommunityConfigsCommand cmd) {

		ListAppCommunityConfigsResponse response = new ListAppCommunityConfigsResponse();

		Community community = null;
		if(cmd.getCommunityId() != null){
			community = communityProvider.findCommunityById(cmd.getCommunityId());
		}

		List<RecommendApp>  recommendApps = new ArrayList<>();

		List<AppCommunityConfigDTO>  dtos = new ArrayList<>();

		//1、查询配置
		//自定义的园区查询园区自定义配置表EH_APP_COMMUNITY_CONFIGS，跟随默认的园区查询安装表。


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

			//首页推荐的应用
			recommendApps = recommendAppProvider.listRecommendApps(ScopeType.COMMUNITY.getCode(), cmd.getCommunityId(), null);

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
					query.addConditions(Tables.EH_ORGANIZATION_APPS.STATUS.eq(OrganizationAppStatus.ENABLE.getCode()));
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

			//首页推荐的应用
			recommendApps = recommendAppProvider.listRecommendApps(ScopeType.ORGANIZATION.getCode(), cmd.getOrganizationId(), null);

		}

		if(dtos == null){
			return response;
		}

		//2、将应用分类

		List<AppCategory> appCategories = appCategoryProvider.listAppCategories(ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), 0L);

		if(appCategories == null){
			return response;
		}

		List<AppCategoryDTO> appCategoryDtos = new ArrayList<>();

		for (AppCategory appCategory: appCategories) {

			AppCategoryDTO dto = ConvertHelper.convert(appCategory, AppCategoryDTO.class);

			List<ServiceModuleEntry> serviceModuleEntries = serviceModuleEntryProvider.listServiceModuleEntries(null, appCategory.getId(), TerminalType.MOBILE.getCode(), ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), ServiceModuleSceneType.CLIENT.getCode());

			List<AppCommunityConfigDTO> tempConfigDtos = new ArrayList<>();
			if(serviceModuleEntries != null && serviceModuleEntries.size() > 0){
				for (AppCommunityConfigDTO appCommunityConfigDto: dtos){
					for (ServiceModuleEntry entry: serviceModuleEntries){
						if(entry.getModuleId().equals(appCommunityConfigDto.getModuleId())){

							if(StringUtils.isEmpty(appCommunityConfigDto.getDisplayName())){
								appCommunityConfigDto.setDisplayName(entry.getEntryName());
							}

							if(!StringUtils.isEmpty(entry.getIconUri())){
								String url = contentServerService.parserUri(entry.getIconUri(), entry.getClass().getName(), entry.getId());
								appCommunityConfigDto.setIconUrl(url);
							}


							tempConfigDtos.add(appCommunityConfigDto);
						}
					}
				}
				dto.setAppCommunityConfigDtos(tempConfigDtos);
			}

			appCategoryDtos.add(dto);
		}


		List<AppCommunityConfigDTO>  recommendDtos = new ArrayList<>();
		//首页推荐的应用
		if(recommendApps != null){
			for (RecommendApp recommendApp: recommendApps){
				for (AppCommunityConfigDTO appCommunityConfigDto: dtos){
					if(recommendApp.getAppId().equals(appCommunityConfigDto.getAppOriginId())){
						recommendDtos.add(appCommunityConfigDto);
						appCommunityConfigDto.setRecommendFlag(TrueOrFalseFlag.TRUE.getCode());
					}
				}
			}
		}


		response.setRecommendAppsDtos(recommendDtos);

		response.setAppCategoryDtos(appCategoryDtos);
		return response;
	}

	private AppCommunityConfigDTO getAppCommunityConfigDTO(AppCommunityConfig config){
		AppCommunityConfigDTO dto = ConvertHelper.convert(config, AppCommunityConfigDTO.class);
		ServiceModuleApp serviceModuleApp = this.findReleaseServiceModuleAppByOriginId(dto.getAppOriginId());

		if(serviceModuleApp == null || TrueOrFalseFlag.fromCode(serviceModuleApp.getSystemAppFlag()) == TrueOrFalseFlag.TRUE){
			return null;
		}

		dto.setServiceModuleAppName(serviceModuleApp.getName());
		dto.setModuleId(serviceModuleApp.getModuleId());

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
				config.setCommunityId(cmd.getCommunityId());
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

			//推荐设置
			changeRecommendApp(ScopeType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getAppOriginId(), cmd.getRecommendFlag());

		}else {
			OrganizationApp orgapp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(cmd.getAppOriginId(), cmd.getOrganizationId());
			orgapp.setDisplayName(cmd.getDisplayName());
			orgapp.setVisibilityFlag(cmd.getVisibilityFlag());
			organizationAppProvider.updateOrganizationApp(orgapp);

			//推荐设置
			changeRecommendApp(ScopeType.ORGANIZATION.getCode(), cmd.getOrganizationId(), cmd.getAppOriginId(), cmd.getRecommendFlag());
		}

	}


	private void changeRecommendApp(Byte scopeType, Long scopeId, Long appId, Byte recommendFlag){

		List<RecommendApp> recommendApps = recommendAppProvider.listRecommendApps(scopeType, scopeId, appId);

		if(TrueOrFalseFlag.fromCode(recommendFlag) == TrueOrFalseFlag.TRUE && (recommendApps == null || recommendApps.size() == 0)){
			RecommendApp recommendApp = new RecommendApp();
			recommendApp.setScopeType(scopeType);
			recommendApp.setScopeId(scopeId);
			recommendApp.setAppId(appId);
			Integer maxOrder = recommendAppProvider.findMaxOrder(scopeType, scopeId);
			if(maxOrder == null){
				recommendApp.setOrder(1);
			}else {
				recommendApp.setOrder(maxOrder + 1);
			}
			recommendAppProvider.createRecommendApp(recommendApp);
		}else if(TrueOrFalseFlag.fromCode(recommendFlag) != TrueOrFalseFlag.TRUE && recommendApps != null && recommendApps.size() > 0){
			recommendAppProvider.delete(recommendApps.get(0).getId());
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

	@Override
	public ListAllLaunchPadAppsResponse listAllLaunchPadApps(ListAllLaunchPadAppsCommand cmd) {

//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
//
//		Byte locationType = ServiceModuleLocationType.MOBILE_COMMUNITY.getCode();
//		Long communityId = cmd.getContext().getCommunityId();
//		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
//		Long orgId = organizationProperty.getOrganizationId();
//		Byte appType = ServiceModuleAppType.COMMUNITY.getCode();
//		Byte sceneType = ServiceModuleSceneType.CLIENT.getCode();
//
//		List<AppCategory> appCategories = appCategoryProvider.listAppCategories(ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), 0L);
//
//		if(appCategories == null){
//			return null;
//		}
//
//		List<AppCategoryDTO> appCategoryDtos = new ArrayList<>();
//
//		for (AppCategory appCategory: appCategories) {
//
//			AppCategoryDTO dto = ConvertHelper.convert(appCategory, AppCategoryDTO.class);
//
//			List<ServiceModuleApp> apps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, locationType, appType, sceneType, null, appCategory.getId());
//
//			List<AppDTO> appDtos = new ArrayList<>();
//			if (apps != null && apps.size() > 0) {
//				for (ServiceModuleApp app : apps) {
//					AppDTO appDTO = toAppDto(app, sceneType, communityId, orgId);
//					if (appDTO == null) {
//						continue;
//					}
//					appDtos.add(appDTO);
//				}
//			}
//			dto.setAppDtos(appDtos);
//
//			appCategoryDtos.add(dto);
//		}
//
//		ListAllLaunchPadAppsResponse response = new ListAllLaunchPadAppsResponse();
//		response.setAppCategoryDtos(appCategoryDtos);
//
//		return response;

		//该接口已废弃，使用listAllApps
		ListAllLaunchPadAppsResponse response = new ListAllLaunchPadAppsResponse();
		List<AppCategoryDTO> appCategoryDtos = new ArrayList<>();
		response.setAppCategoryDtos(appCategoryDtos);
		return response;
	}


	@Override
	public ListAllAppsResponse listAllApps(ListAllLaunchPadAppsCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		Byte locationType = ServiceModuleLocationType.MOBILE_COMMUNITY.getCode();
		Long communityId = cmd.getContext().getCommunityId();
		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
		Long orgId = organizationProperty.getOrganizationId();
		Byte appType = ServiceModuleAppType.COMMUNITY.getCode();
		Byte sceneType = ServiceModuleSceneType.CLIENT.getCode();

		List<AppCategory> appCategories = appCategoryProvider.listAppCategories(ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), 0L);

		if(appCategories == null){
			return null;
		}

		List<LaunchPadCategoryDTO> categoryDtos = new ArrayList<>();

		for (AppCategory appCategory: appCategories) {

			LaunchPadCategoryDTO dto = ConvertHelper.convert(appCategory, LaunchPadCategoryDTO.class);

			List<ServiceModuleApp> apps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, locationType, appType, sceneType, OrganizationAppStatus.ENABLE.getCode(), appCategory.getId());

			List<AppDTO> appDtos = toAppDtos(communityId, orgId, sceneType, apps);

			dto.setAppDtos(appDtos);

			categoryDtos.add(dto);
		}


		List<ServiceModuleApp> userCommunityApps = findUserCommunityApps(communityId);

		List<AppDTO> appDtos = toAppDtos(communityId, orgId, sceneType, userCommunityApps);


		//加上"全部"Icon
		AppDTO allIcon = getAllIcon(communityId, orgId, AllOrMoreType.ALL.getCode());
		appDtos.add(allIcon);


		ListAllAppsResponse response = new ListAllAppsResponse();

		response.setCategoryDtos(categoryDtos);

		response.setDefaultDtos(appDtos);

		return response;
	}

	private List<AppDTO> toAppDtos(Long communityId, Long orgId, Byte sceneType, List<ServiceModuleApp> userCommunityApps) {
		List<AppDTO> appDtos = new ArrayList<>() ;
		if(userCommunityApps != null && userCommunityApps.size() > 0){
			for (ServiceModuleApp app: userCommunityApps){
				AppDTO appDTO = toAppDto(app, sceneType, communityId, orgId);
				if(appDTO == null){
					continue;
				}
				appDtos.add(appDTO);
			}
		}

		return appDtos;
	}

	@Override
	public void updateBaseUserApps(UpdateUserAppsCommand cmd) {

		Long userId = UserContext.currentUserId();

		if(userId == null || userId == 0){
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_UNAUTHENTITICATION, "Authentication is required");
		}

		if(cmd.getCommunityId() == null){
            LOGGER.error("communityId is null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "communityId is null");

        }

        dbProvider.execute(status -> {

        	//保存自定义标志
			saveUserAppFlag(userId, ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), cmd.getCommunityId());

            userAppProvider.deleteByUserId(userId, ServiceModuleLocationType.MOBILE_COMMUNITY.getCode(), cmd.getCommunityId());


            //没有自己的就返回吧
            if(cmd.getAppIds() == null){
            	return null;
			}

            Integer order = 1;

            for (Long appId: cmd.getAppIds()){

                UserApp userApp = new UserApp();
                userApp.setAppId(appId);
                userApp.setUserId(userId);
                userApp.setLocationType(ServiceModuleLocationType.MOBILE_COMMUNITY.getCode());
                userApp.setLocationTargetId(cmd.getCommunityId());
                userApp.setOrder(order);
                order = order + 1;
                userAppProvider.createUserApp(userApp);
            }

            return null;
        });

	}


	private UserAppFlag saveUserAppFlag(Long userId, Byte locationType, Long locationTargetId){
		UserAppFlag userAppFlag = userAppFlagProvider.findUserAppFlag(userId, locationType, locationTargetId);
		if(userAppFlag == null){
			userAppFlag = new UserAppFlag();
			userAppFlag.setUserId(userId);
			userAppFlag.setLocationType(locationType);
			userAppFlag.setLocationTargetId(locationTargetId);
			userAppFlagProvider.createUserAppFlag(userAppFlag);
		}

		return userAppFlag;
	}

	@Override
	public void updateRecommendApps(UpdateRecommendAppsCommand cmd) {

		if(ScopeType.fromCode(cmd.getScopeType()) == null || cmd.getScopeId() == null || cmd.getAppIds() == null || cmd.getAppIds().size() == 0){
			LOGGER.error("invalid parameter, cmd = {}.", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter, cmd = " + cmd.toString());

		}

		dbProvider.execute(status -> {

			recommendAppProvider.deleteByScope(cmd.getScopeType(), cmd.getScopeId());

			Integer order = 1;

			for (Long appId: cmd.getAppIds()){

				RecommendApp recommendApp = new RecommendApp();
				recommendApp.setAppId(appId);
				recommendApp.setScopeType(cmd.getScopeType());
				recommendApp.setScopeId(cmd.getScopeId());
				recommendApp.setOrder(order);
				order = order + 1;
				recommendAppProvider.createRecommendApp(recommendApp);
			}

			return null;
		});
	}
}
