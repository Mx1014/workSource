// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.module.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.acl.ServiceModuleEntryConstans;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.launchpadbase.ListLaunchPadAppsCommand;
import com.everhomes.rest.launchpadbase.ListLaunchPadAppsResponse;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Card;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.rest.servicemoduleapp.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

		ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(cmd.getAppId());
		if(serviceModuleApp == null){
			LOGGER.error("app not found, appId = {}", cmd.getAppId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_SERVICEMODULEAPP_NOT_FOUND, "app not found, appId = " + cmd.getAppId());
		}


		OrganizationApp orgapps = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(serviceModuleApp.getOriginId(), cmd.getOrgId());

		if(orgapps != null){
			LOGGER.error("already install this app, appId = {}", cmd.getAppId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_ALREADY_INSTALL_THIS_APP, "already install this app, appId = " + cmd.getAppId());
		}

		orgapps = new OrganizationApp();
		orgapps.setAppOriginId(serviceModuleApp.getOriginId());
		orgapps.setOrgId(cmd.getOrgId());
		//TODO
		//orgapps.setAppType();
		orgapps.setStatus(OrganizationAppStatus.VALID.getCode());
		orgapps.setCreatorUid(UserContext.currentUserId());
		orgapps.setCreateTime(new Timestamp(System.currentTimeMillis()));

		Long id = organizationAppProvider.createOrganizationApp(orgapps);


		ServiceModuleAppDTO dto = ConvertHelper.convert(serviceModuleApp, ServiceModuleAppDTO.class);
		dto.setOrgAppId(id);

		return dto;
	}

	@Override
	public void uninstallApp(UninstallAppCommand cmd) {

		OrganizationApp orgapp = organizationAppProvider.getOrganizationAppById(cmd.getOrgAppId());

		if(orgapp == null || OrganizationAppStatus.INVALID == OrganizationAppStatus.fromCode(orgapp.getStatus())){
			LOGGER.error("org app not found, orgAppId = {}", cmd.getOrgAppId());
			throw RuntimeErrorException.errorWith(ServiceModuleAppServiceErrorCode.SCOPE,
					ServiceModuleAppServiceErrorCode.ERROR_ORG_APP_NOT_FOUND, "org app not found, orgAppId = " + cmd.getOrgAppId());
		}

		orgapp.setStatus(OrganizationAppStatus.INVALID.getCode());
		orgapp.setOperatorUid(UserContext.currentUserId());
		orgapp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
		organizationAppProvider.updateOrganizationApp(orgapp);

	}

	@Override
	public ListServiceModuleAppsByOrgIdResponse listServiceModuleAppsByOrgId(ListServiceModuleAppsByOrgIdCommand cmd) {

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(cmd.getNamespaceId());

		//获取所有应用，在内存中处理。
		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsByAppTypeAndKeyword(releaseVersion.getId(), cmd.getAppType(), cmd.getKeyword());
		if(apps == null){
			return null;
		}

		List<ServiceModuleAppDTO> dtos = new ArrayList<>();
		for (ServiceModuleApp app: apps){
			OrganizationApp orgapp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(app.getOriginId(), cmd.getOrgId());

			ServiceModuleAppDTO dto = null;
			//已安装的、未安装的、全部
			if(TrueOrFalseFlag.fromCode(cmd.getInstallFlag()) == TrueOrFalseFlag.TRUE && orgapp != null){
				dto = ConvertHelper.convert(app, ServiceModuleAppDTO.class);
				dto.setOrgAppId(orgapp.getId());
				dtos.add(dto);
			}else if(TrueOrFalseFlag.fromCode(cmd.getInstallFlag()) == TrueOrFalseFlag.FALSE && orgapp == null) {
				dto = ConvertHelper.convert(app, ServiceModuleAppDTO.class);
				dtos.add(dto);
			}else if(cmd.getInstallFlag() == null){
				dto = ConvertHelper.convert(app, ServiceModuleAppDTO.class);
				if (orgapp != null){
					dto.setOrgAppId(orgapp.getId());
				}
				dtos.add(dto);
			}
		}

		ListServiceModuleAppsByOrgIdResponse response = new  ListServiceModuleAppsByOrgIdResponse();
		response.setServiceModuleApps(dtos);
		return response;
	}


	@Override
	public ListLaunchPadAppsResponse listLaunchPadApps(ListLaunchPadAppsCommand cmd) {

		List<AppDTO> appDtos = new ArrayList<>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		Long orgId = null;
		Byte entryType = null;
		Byte appType = null;

		if(Widget.fromCode(cmd.getWidget()) == Widget.CARD){
			Card cardConfig = ConvertHelper.convert(cmd.getInstanceConfig(), Card.class);
			if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.OA){
				orgId = cmd.getContext().getOrgId();
				entryType = ServiceModuleEntryConstans.app_oa_client;
				appType = ServiceModuleAppType.OA.getCode();
			}else if(ServiceModuleAppType.fromCode(cardConfig.getAppType()) == ServiceModuleAppType.COMMUNITY){
				OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getContext().getCommunityId());
				orgId = organizationProperty.getOrganizationId();
				entryType = ServiceModuleEntryConstans.app_community_management;
				appType = ServiceModuleAppType.COMMUNITY.getCode();
			}
		}else if (Widget.fromCode(cmd.getWidget()) == Widget.NAVIGATOR){
			OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(cmd.getContext().getCommunityId());
			orgId = organizationProperty.getOrganizationId();
			entryType = ServiceModuleEntryConstans.app_community_client;
			appType = ServiceModuleAppType.COMMUNITY.getCode();
		}

		List<ServiceModuleApp> apps = serviceModuleAppProvider.listInstallServiceModuleApps(namespaceId, releaseVersion.getId(), orgId, appType, entryType);

		if(apps != null && apps.size() > 0){
			for (ServiceModuleApp app: apps){

				AppDTO appDTO = new AppDTO();
				appDTO.setName(app.getName());
				String url = null;
				if(app.getMobileUri() != null){
					url = contentServerService.parserUri(app.getMobileUri(), ServiceModuleAppDTO.class.getSimpleName(), app.getId());
				}
				appDTO.setIconUrl(url);

				ServiceModuleRouterHandler handler = serviceModuleService.getServiceModuleRouterHandler(app.getModuleId());
				if(handler != null){
					String router = handler.getRouter(app);
					appDTO.setRouter(router);
				}
				appDtos.add(appDTO);
			}
		}

		ListLaunchPadAppsResponse response = new ListLaunchPadAppsResponse();
		response.setApps(appDtos);
		return response;
	}
}
