// @formatter:off
package com.everhomes.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.ServiceModuleAppProfileProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configurations.ConfigurationsService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.launchpad.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.menu.WebMenuService;
import com.everhomes.module.*;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.acl.ListServiceModuleEntriesCommand;
import com.everhomes.rest.acl.ListServiceModuleEntriesResponse;
import com.everhomes.rest.acl.ServiceModuleEntryDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.*;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.configurations.admin.ConfigurationsCreateAdminCommand;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpadbase.IndexType;
import com.everhomes.rest.launchpadbase.LayoutType;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.CardExtension;
import com.everhomes.rest.launchpadbase.indexconfigjson.Application;
import com.everhomes.rest.launchpadbase.indexconfigjson.Container;
import com.everhomes.rest.launchpadbase.indexconfigjson.TopBarStyle;
import com.everhomes.rest.module.AccessControlType;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.TerminalType;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.portal.LaunchPadLayoutJson;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.widget.*;
import com.everhomes.rest.widget.NewsInstanceConfig;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.sequence.SequenceService;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPortalItemCategories;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppEntryProfile;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.google.gson.reflect.TypeToken;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PortalServiceImpl implements PortalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalServiceImpl.class);


	@Autowired
	private PortalLayoutProvider portalLayoutProvider;

	@Autowired
	private PortalItemGroupProvider portalItemGroupProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private PortalItemProvider portalItemProvider;

	@Autowired
	private PortalContentScopeProvider portalContentScopeProvider;

	@Autowired
	private PortalLayoutTemplateProvider portalLayoutTemplateProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private PortalItemCategoryProvider portalItemCategoryProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private PortalNavigationBarProvider portalNavigationBarProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private LaunchPadProvider launchPadProvider;

	@Autowired
	private PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

	@Autowired
	private PortalPublishLogProvider portalPublishLogProvider;

	@Autowired
	private CommunitySearcher communitySearcher;

	@Autowired
	private OrganizationSearcher organizationSearcher;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private NamespacesService namespacesService;

	@Autowired
	private PortalVersionProvider portalVersionProvider;

	@Autowired
	private PortalVersionUserProvider portalVersionUserProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	private WebMenuService webMenuService;

	@Autowired
	private ServiceModuleAppService serviceModuleAppService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private LaunchPadIndexProvider launchPadIndexProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private LocaleStringService localeStringService;


	@Autowired
	private RouterInfoService routerService;

	@Autowired
	private LaunchPadService launchPadService;

	@Autowired
    private ServiceModuleEntryProvider serviceModuleEntryProvider;
	@Autowired
    private ServiceModuleAppProfileProvider serviceModuleAppProfileProvider;

	@Autowired
    private ConfigurationsService configurationsService;
	@Override
	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd) {

		if(cmd.getVersionId() == null){
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(cmd.getNamespaceId());
			if(releaseVersion != null){
				cmd.setVersionId(releaseVersion.getId());
			}
		}

		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(cmd.getNamespaceId(), cmd.getVersionId(), cmd.getModuleId(), cmd.getKeywords(), cmd.getDeveloperIds(), cmd.getAppType(), cmd.getMobileFlag(), cmd.getPcFlag(), cmd.getIndependentConfigFlag(), cmd.getSupportThirdFlag());


		List<ServiceModuleAppDTO> dtos = new ArrayList<>();

		for (ServiceModuleApp app: moduleApps){

			//过滤掉系统应用
			if (cmd.getExcludeSystemAppFlag() != null && cmd.getExcludeSystemAppFlag().byteValue() == 1 && app.getSystemAppFlag() != null && app.getSystemAppFlag().byteValue() == 1){
				continue;
			}
			ServiceModuleAppDTO dto =  processServiceModuleAppDTO(app);
			dtos.add(dto);
		}

		ListServiceModuleAppsResponse response = new ListServiceModuleAppsResponse();
		response.setServiceModuleApps(dtos);
		return response;
	}

	@Override
	public PortalVersion findReleaseVersion(Integer namespaceId) {

		return portalVersionProvider.findReleaseVersion(namespaceId);

	}


	@Override
	public ListServiceModuleAppsResponse listServiceModuleAppsWithConditon(ListServiceModuleAppsCommand cmd) {
		//由于园区后台的关系，使用reflectionServiceModuleApps代替serviceModuleApps
//		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getActionType(), cmd.getCustomTag(), cmd.getCustomPath());
//		if(moduleApps != null && moduleApps.size() > 0){
//			List dtos = Collections.singletonList( processServiceModuleAppDTO(moduleApps.get(0)));
//			return new ListServiceModuleAppsResponse(dtos);
//		}
//		return null;
		List<ServiceModuleApp> moduleApps = serviceModuleAppService.listReleaseServiceModuleApp(cmd.getNamespaceId(), cmd.getModuleId(), null, cmd.getCustomTag(), null);
//		List<ServiceModuleAppDTO> moduleApps = serviceModuleProvider.listReflectionServiceModuleApp(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getActionType(), cmd.getCustomTag(), cmd.getCustomPath(), null);
//		LOGGER.debug("list apps size:" + moduleApps.size());
		if(moduleApps != null && moduleApps.size() > 0){
			List dtos = Collections.singletonList(ConvertHelper.convert(moduleApps.get(0), ServiceModuleAppDTO.class));
			return new ListServiceModuleAppsResponse(dtos);
		}
		return null;
	}


	@Override
	public ServiceModuleAppDTO createServiceModuleApp(CreateServiceModuleAppCommand cmd) {
		ServiceModule serviceModule = checkServiceModule(cmd.getModuleId());
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ServiceModuleApp moduleApp = ConvertHelper.convert(cmd, ServiceModuleApp.class);
		if(StringUtils.isEmpty(cmd.getInstanceConfig()) && null != serviceModule){
			moduleApp.setInstanceConfig(serviceModule.getInstanceConfig());
		}
		moduleApp.setNamespaceId(namespaceId);
		moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
		moduleApp.setCreatorUid(UserContext.current().getUser().getId());
		moduleApp.setOperatorUid(moduleApp.getCreatorUid());
		moduleApp.setActionType(serviceModule.getActionType());
		moduleApp.setModuleControlType(serviceModule.getModuleControlType());
		moduleApp.setAccessControlType(serviceModule.getAccessControlType());
		moduleApp.setEnableEnterprisePayFlag(serviceModule.getEnableEnterprisePayFlag());

		//todo
		moduleApp.setCustomTag(cmd.getCustomTag());
		moduleApp.setCustomPath(cmd.getCustomPath());

		moduleApp.setAppType(serviceModule.getAppType());

		serviceModuleAppProvider.createServiceModuleApp(moduleApp);
		return processServiceModuleAppDTO(moduleApp);
	}

	@Override
	public List<ServiceModuleAppDTO> batchCreateServiceModuleApp(BatchCreateServiceModuleAppCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if(null == cmd.getModuleApps() || cmd.getModuleApps().size() == 0){
			LOGGER.error("params moduleApps is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"params moduleApps is null.");
		}

		List<ServiceModuleApp> serviceModuleApps = new ArrayList<>();

		for (CreateServiceModuleApp createModuleApp: cmd.getModuleApps()) {
			ServiceModuleApp moduleApp = ConvertHelper.convert(createModuleApp, ServiceModuleApp.class);
			ServiceModule serviceModule = checkServiceModule(createModuleApp.getModuleId());
			moduleApp.setInstanceConfig(serviceModule.getInstanceConfig());
			moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
			moduleApp.setCreatorUid(UserContext.current().getUser().getId());
			moduleApp.setOperatorUid(moduleApp.getCreatorUid());
			moduleApp.setActionType(serviceModule.getActionType());
			moduleApp.setNamespaceId(namespaceId);
			moduleApp.setVersionId(cmd.getVersionId());
			//todo
			moduleApp.setCustomTag(createModuleApp.getCustomTag());
			moduleApp.setCustomPath(createModuleApp.getCustomPath());
			moduleApp.setModuleControlType(serviceModule.getModuleControlType());

			moduleApp.setAppType(serviceModule.getAppType());

			moduleApp.setAccessControlType(serviceModule.getAccessControlType());

			moduleApp.setEnableEnterprisePayFlag(serviceModule.getEnableEnterprisePayFlag());

			serviceModuleApps.add(moduleApp);
		}
		serviceModuleAppProvider.createServiceModuleApps(serviceModuleApps);
		return serviceModuleApps.stream().map(r ->{
			return processServiceModuleAppDTO(r);
		}).collect(Collectors.toList());
	}

	@Override
	public ServiceModuleAppDTO updateServiceModuleApp(UpdateServiceModuleAppCommand cmd) {
		ServiceModuleApp moduleApp = checkServiceModuleApp(cmd.getId());
		moduleApp.setOperatorUid(UserContext.current().getUser().getId());
		moduleApp.setName(cmd.getName());
		if(null != cmd.getModuleId()){
			moduleApp.setModuleId(cmd.getModuleId());
			ServiceModule serviceModule = checkServiceModule(cmd.getModuleId());
			if(StringUtils.isEmpty(cmd.getInstanceConfig()) && null != serviceModule){
				cmd.setInstanceConfig(serviceModule.getInstanceConfig());
			}
		}

		//todo
		if(!StringUtils.isEmpty(cmd.getCustomPath())){
			moduleApp.setCustomPath(cmd.getCustomPath());
		}
		if(!StringUtils.isEmpty(cmd.getCustomTag())){
			moduleApp.setCustomTag(cmd.getCustomTag());
		}

		if(!StringUtils.isEmpty(cmd.getAccessControlType())){
			moduleApp.setAccessControlType(cmd.getAccessControlType());
		}

		if(TrueOrFalseFlag.fromCode(cmd.getEnableEnterprisePayFlag()) != null){
			moduleApp.setEnableEnterprisePayFlag(cmd.getEnableEnterprisePayFlag());
		}

		moduleApp.setInstanceConfig(cmd.getInstanceConfig());
		serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
        updateServiceModuleAppEntryProfile(cmd,moduleApp);
		return processServiceModuleAppDTO(moduleApp);
	}

	//更新应用自定义配置应用入口信息
	private void updateServiceModuleAppEntryProfile(UpdateServiceModuleAppCommand cmd, ServiceModuleApp moduleApp) {
        if (TrueOrFalseFlag.FALSE.getCode().equals(cmd.getAppEntrySettingFlag())) {
            List<ServiceModuleAppEntryProfile> appEntryProfiles = this.serviceModuleAppProvider.listServiceModuleAppEntryProfile(moduleApp.getOriginId(),
                    null,null,null);
            if(!CollectionUtils.isEmpty(appEntryProfiles)) {
                for (ServiceModuleAppEntryProfile serviceModuleAppEntryProfile : appEntryProfiles) {
                    serviceModuleAppEntryProfile.setAppEntrySetting(cmd.getAppEntrySettingFlag());
                    this.serviceModuleAppProvider.updateServiceModuleAppEntryProfile(serviceModuleAppEntryProfile);
                }
            }
        }else if (!CollectionUtils.isEmpty(cmd.getAppEntryDtos())) {
            for (AppEntryDTO appEntryDTO : cmd.getAppEntryDtos()) {
                List<ServiceModuleAppEntryProfile> appEntryProfiles = this.serviceModuleAppProvider.listServiceModuleAppEntryProfile(moduleApp.getOriginId(),
                        appEntryDTO.getEntryId(),null,null);
                if(!CollectionUtils.isEmpty(appEntryProfiles)) {
                    ServiceModuleAppEntryProfile serviceModuleAppEntryProfile = appEntryProfiles.get(0);
                    serviceModuleAppEntryProfile.setAppEntrySetting(cmd.getAppEntrySettingFlag());
                    serviceModuleAppEntryProfile.setEntryName(appEntryDTO.getEntryName());
                    serviceModuleAppEntryProfile.setEntryUri(appEntryDTO.getIconUri());
                    this.serviceModuleAppProvider.updateServiceModuleAppEntryProfile(serviceModuleAppEntryProfile);
                }else {
                    ServiceModuleAppEntryProfile serviceModuleAppEntryProfile = new ServiceModuleAppEntryProfile();
                    serviceModuleAppEntryProfile.setOriginId(moduleApp.getOriginId());
                    serviceModuleAppEntryProfile.setEntryId(appEntryDTO.getEntryId());
                    serviceModuleAppEntryProfile.setAppEntrySetting(cmd.getAppEntrySettingFlag());
                    serviceModuleAppEntryProfile.setEntryName(appEntryDTO.getEntryName());
                    serviceModuleAppEntryProfile.setEntryUri(appEntryDTO.getIconUri());
                    this.serviceModuleAppProvider.createServiceModuleAppEntryProfile(serviceModuleAppEntryProfile);
                }
            }
        }
    }


	@Override
	public void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd) {
		ServiceModuleApp moduleApp = checkServiceModuleApp(cmd.getId());
		moduleApp.setOperatorUid(UserContext.current().getUser().getId());
		moduleApp.setStatus(ServiceModuleAppStatus.INACTIVE.getCode());
		serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
	}

	private ServiceModuleApp checkServiceModuleApp(Long id){
		ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(id);
		if(null == serviceModuleApp){
			LOGGER.error("Unable to find the serviceModuleApp.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the serviceModuleApp.");
		}
		return serviceModuleApp;
	}

	private ServiceModule checkServiceModule(Long id){
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(id);
		if(null == serviceModule){
			LOGGER.error("Unable to find the serviceModule.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the serviceModule.id={}",id);
		}
		return serviceModule;
	}

	@Override
	public List<PortalLayoutTemplateDTO> listPortalLayoutTemplates() {
		List<PortalLayoutTemplate> portalLayoutTemplates = portalLayoutTemplateProvider.listPortalLayoutTemplate();
		return portalLayoutTemplates.stream().map(r ->{
			return ConvertHelper.convert(r, PortalLayoutTemplateDTO.class);
		}).collect(Collectors.toList());
	}

	@Override
	public ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalLayout> portalLayouts = portalLayoutProvider.listPortalLayout(namespaceId, null, cmd.getVersionId());
		return new ListPortalLayoutsResponse(portalLayouts.stream().map(r ->{
			return processPortalLayoutDTO(r);
		}).collect(Collectors.toList()));
	}

	@Override
	public ServiceModuleAppDTO processServiceModuleAppDTO(ServiceModuleApp moduleApp){
		if(moduleApp == null){
			return null;
		}
		ServiceModuleAppDTO dto = ConvertHelper.convert(moduleApp, ServiceModuleAppDTO.class);

		ServiceModule serviceModule  = null;
		if(null != moduleApp.getModuleId() && moduleApp.getModuleId() != 0){
			serviceModule = checkServiceModule(moduleApp.getModuleId());
			dto.setModuleName(serviceModule.getName());

			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
			if(null != handler){
				HandlerProcessInstanceConfigCommand cmd = new HandlerProcessInstanceConfigCommand();
				cmd.setAppId(moduleApp.getId());
				cmd.setAppOriginId(moduleApp.getOriginId());
				dto.setInstanceConfig(handler.processInstanceConfig(moduleApp.getNamespaceId(),dto.getInstanceConfig(), cmd));
			}
		}

		if(moduleApp.getIconUri() != null){
			String url = contentServerService.parserUri(moduleApp.getIconUri(), ServiceModuleApp.class.getSimpleName(), moduleApp.getId());
			dto.setIconUrl(url);
		}else if(serviceModule != null && serviceModule.getIconUri() != null){
			//使用模块默认图标
			String url = contentServerService.parserUri(serviceModule.getIconUri(), ServiceModule.class.getSimpleName(), serviceModule.getId());
			dto.setIconUrl(url);
		}

		if(moduleApp.getPcUris() != null){
			List<String> pcUris = GsonUtil.fromJson(moduleApp.getPcUris(), new TypeToken<List<String>>(){}.getType());
			//List<String> pcUris = (ArrayList<String>)StringHelper.fromJsonString(moduleApp.getPcUris(), List.class);
			List<String> pcUrls = contentServerService.parserUri(pcUris, ServiceModuleApp.class.getSimpleName(), moduleApp.getId());
			dto.setPcUris(pcUris);
			dto.setPcUrls(pcUrls);
		}

		if(moduleApp.getMobileUris() != null){
			List<String> mobileUris = GsonUtil.fromJson(moduleApp.getMobileUris(), new TypeToken<List<String>>(){}.getType());
			//List<String> mobileUris = (ArrayList<String>)StringHelper.fromJsonString(moduleApp.getMobileUris(), List.class);
			List<String> mobileUrls = contentServerService.parserUri(mobileUris, ServiceModuleApp.class.getSimpleName(), moduleApp.getId());
			dto.setMobileUris(mobileUris);
			dto.setMobileUrls(mobileUrls);
		}

//		if(moduleApp.getDependentAppIds() != null){
//			List<Long> dependentAppIds = GsonUtil.fromJson(moduleApp.getDependentAppIds(), new TypeToken<List<Long>>(){}.getType());
//			List<String> dependentAppNames = new ArrayList<>();
//			if(dependentAppIds != null && dependentAppIds.size() > 0){
//				for (Long id: dependentAppIds){
//					ServiceModuleApp moduleAppByOriginId = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(id);
//					if(moduleAppByOriginId != null){
//						dependentAppNames.add(moduleAppByOriginId.getName());
//					}
//				}
//			}
//			dto.setDependentAppNames(dependentAppNames);
//			dto.setDependentAppIds(dependentAppIds);
//		}

		ListServiceModuleEntriesCommand cmd = new ListServiceModuleEntriesCommand();
		cmd.setModuleId(moduleApp.getModuleId());
		ListServiceModuleEntriesResponse response = serviceModuleService.listServiceModuleEntries(cmd);
		sortModuleEntryDto(response.getDtos());
		dto.setServiceModuleEntryDtos(response.getDtos());


		if(moduleApp.getAppEntryInfos() != null){
			//List<AppEntryInfoDTO> entryInfos = (ArrayList<AppEntryInfoDTO>)StringHelper.fromJsonString(moduleApp.getConfigAppIds(), List.class);
			List<AppEntryInfoDTO> entryInfos = GsonUtil.fromJson(moduleApp.getAppEntryInfos(), new TypeToken<List<AppEntryInfoDTO>>(){}.getType());
			dto.setAppEntryInfos(entryInfos);
		}
		List<ServiceModuleAppEntryProfile> appEntryProfiles = this.serviceModuleAppProvider.listServiceModuleAppEntryProfile(moduleApp.getOriginId(),
				null,null,null);
		List<AppEntryDTO> appEntryDTOS = new ArrayList<>();
		if (!CollectionUtils.isEmpty(appEntryProfiles)) {
		    dto.setAppEntrySettingFlag(appEntryProfiles.get(0).getAppEntrySetting());
			for (ServiceModuleAppEntryProfile serviceModuleAppEntryProfile : appEntryProfiles) {
                AppEntryDTO appEntryDTO = ConvertHelper.convert(serviceModuleAppEntryProfile, AppEntryDTO.class);
                appEntryDTO.setIconUri(serviceModuleAppEntryProfile.getEntryUri());
                String url = contentServerService.parserUri(appEntryDTO.getIconUri(), ServiceModuleAppEntryProfile.class.getSimpleName(), serviceModuleAppEntryProfile.getId());
                appEntryDTO.setIconUrl(url);
                ServiceModuleEntry entry = this.serviceModuleEntryProvider.findById(serviceModuleAppEntryProfile.getEntryId());
                if (entry != null) {
                    appEntryDTO.setTerminalType(entry.getTerminalType());
                    appEntryDTO.setSceneType(entry.getSceneType());
                    appEntryDTO.setLocationType(entry.getLocationType());
                }
                appEntryDTOS.add(appEntryDTO);
            }
		}else {
			dto.setAppEntrySettingFlag(TrueOrFalseFlag.FALSE.getCode());
		}
		sortAppEntryDto(appEntryDTOS);
		dto.setServiceModuleSelfEntryDtos(appEntryDTOS);
		return dto;
	}

	private void sortModuleEntryDto(List<ServiceModuleEntryDTO> list) {
	    Collections.sort(list, new Comparator<ServiceModuleEntryDTO>() {
            @Override
            public int compare(ServiceModuleEntryDTO o1, ServiceModuleEntryDTO o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                StringBuilder o1StringBuilder = new StringBuilder();
                StringBuilder o2StringBuilder = new StringBuilder();
                o1StringBuilder.append(o1.getTerminalType()==null?0:o1.getTerminalType()).append(o1.getSceneType()==null?0:100-o1.getSceneType());
                o2StringBuilder.append(o2.getTerminalType()==null?0:o2.getTerminalType()).append(o2.getSceneType()==null?0:100-o2.getSceneType());
                return o2StringBuilder.toString().compareTo(o1StringBuilder.toString());
//                if (o1 == null || o2 == null) {
//                    return -1;
//                }
//                if (o1.getTerminalType() == null || o2.getTerminalType() == null) {
//                    return -1;
//                }
//                if (!o1.getTerminalType().equals(o2.getTerminalType())) {
//                    return o2.getTerminalType().compareTo(o1.getTerminalType());
//                }
//                if (o1.getSceneType() == null || o2.getSceneType() == null) {
//                    return -1;
//                }
//                if (!o1.getSceneType().equals(o2.getSceneType())) {
//                    return o1.getSceneType().compareTo(o2.getSceneType());
//                }
//                return 0;
            }
        });
    }

    private void sortAppEntryDto(List<AppEntryDTO> list) {
        Collections.sort(list, new Comparator<AppEntryDTO>() {
            @Override
            public int compare(AppEntryDTO o1, AppEntryDTO o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                StringBuilder o1StringBuilder = new StringBuilder();
                StringBuilder o2StringBuilder = new StringBuilder();
                o1StringBuilder.append(o1.getTerminalType()==null?0:o1.getTerminalType()).append(o1.getSceneType()==null?0:100-o1.getSceneType());
                o2StringBuilder.append(o2.getTerminalType()==null?0:o2.getTerminalType()).append(o2.getSceneType()==null?0:100-o2.getSceneType());
//                if (o1.getTerminalType() == null || o2.getTerminalType() == null) {
//                    return -1;
//                }
//                if (!o1.getTerminalType().equals(o2.getTerminalType())) {
//                    return o2.getTerminalType().compareTo(o1.getTerminalType());
//                }
//                if (o1.getSceneType() == null || o2.getSceneType() == null) {
//                    return -1;
//                }
//                if (!o1.getSceneType().equals(o2.getSceneType())) {
//                    return o1.getSceneType().compareTo(o2.getSceneType());
//                }
                return o2StringBuilder.toString().compareTo(o1StringBuilder.toString());
            }
        });
    }

	private PortalLayoutDTO processPortalLayoutDTO(PortalLayout portalLayout){
		PortalLayoutDTO dto = ConvertHelper.convert(portalLayout, PortalLayoutDTO.class);
		return dto;
	}

	@Override
	public PortalLayoutDTO createPortalLayout(CreatePortalLayoutCommand cmd) {

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(cmd.getVersionId());

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalLayout portalLayout = ConvertHelper.convert(cmd, PortalLayout.class);
		portalLayout.setCreatorUid(user.getId());
		portalLayout.setOperatorUid(user.getId());
		portalLayout.setNamespaceId(namespaceId);
		portalLayout.setStatus(PortalLayoutStatus.ACTIVE.getCode());

		//防止老页面页面缓存，没有type
		if(PortalLayoutType.fromCode(cmd.getType()) == null){
			cmd.setType(PortalLayoutType.SECONDSERVICEMARKETLAYOUT.getCode());
		}

		this.dbProvider.execute((status) -> {
			if(null != cmd.getType()){
				PortalLayoutTemplate template = portalLayoutTemplateProvider.findPortalLayoutTemplateByType(cmd.getType());
				if(template == null){
					LOGGER.error("PortalLayoutTemplate not found type = {}", cmd.getType());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"PortalLayoutTemplate not found type = + " + cmd.getType());
				}

				//主页签标志，“首页”类型模板默认开启。
				portalLayout.setType(template.getType());
				portalLayout.setIndexFlag(cmd.getIndexFlag());
				if(PortalLayoutType.fromCode(template.getType()) == PortalLayoutType.SERVICEMARKETLAYOUT){
					portalLayout.setIndexFlag(TrueOrFalseFlag.TRUE.getCode());
				}

				if(TrueOrFalseFlag.fromCode(portalLayout.getIndexFlag()) == TrueOrFalseFlag.TRUE){
					checkIndexPortalLayout(portalLayout.getVersionId(), portalLayout.getType());
				}

				if(null != template && !StringUtils.isEmpty(template.getTemplateJson())){
					List<PortalItemGroup> groups = new ArrayList<>();
					PortalLayoutJson layoutJson = (PortalLayoutJson)StringHelper.fromJsonString(template.getTemplateJson(), PortalLayoutJson.class);
					portalLayout.setName(layoutJson.getLayoutName());
					portalLayout.setLocation(layoutJson.getLocation());
					portalLayoutProvider.createPortalLayout(portalLayout);
					if(null != layoutJson.getGroups()){
						for (PortalItemGroupJson jsonObj: layoutJson.getGroups()) {
							PortalItemGroup portalItemGroup = ConvertHelper.convert(jsonObj, PortalItemGroup.class);
							String instanceConfig = StringHelper.toJsonString(jsonObj.getInstanceConfig());
							if(!StringUtils.isEmpty(instanceConfig) && !"null".equals(instanceConfig)){
								portalItemGroup.setInstanceConfig(instanceConfig);
							}
							portalItemGroup.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
							portalItemGroup.setLayoutId(portalLayout.getId());
							portalItemGroup.setCreatorUid(user.getId());
							portalItemGroup.setOperatorUid(user.getId());
							portalItemGroup.setNamespaceId(namespaceId);
							groups.add(portalItemGroup);
						}
						portalItemGroupProvider.createPortalItemGroups(groups);
					}

				}
			}else{
				portalLayoutProvider.createPortalLayout(portalLayout);
			}

			return null;
		});
		return processPortalLayoutDTO(portalLayout);
	}

	@Override
	public PortalLayoutDTO updatePortalLayout(UpdatePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalLayout.getVersionId());

		if(TrueOrFalseFlag.fromCode(portalLayout.getIndexFlag()) != TrueOrFalseFlag.TRUE && TrueOrFalseFlag.fromCode(cmd.getIndexFlag()) == TrueOrFalseFlag.TRUE){
			checkIndexPortalLayout(portalLayout.getVersionId(), portalLayout.getType());
		}

		portalLayout.setLabel(cmd.getLabel());
		portalLayout.setDescription(cmd.getDescription());
		portalLayout.setOperatorUid(user.getId());
		portalLayout.setIndexFlag(cmd.getIndexFlag());

		dbProvider.execute((status) -> {
			//主页签标志发生变化，需要对layout和item的location刷新
			//if(TrueOrFalseFlag.fromCode(cmd.getIndexFlag()) != null && TrueOrFalseFlag.fromCode(portalLayout.getIndexFlag()) != TrueOrFalseFlag.fromCode(cmd.getIndexFlag())){

			if(TrueOrFalseFlag.fromCode(portalLayout.getIndexFlag()) == TrueOrFalseFlag.TRUE){
				PortalLayoutType portalLayoutType = PortalLayoutType.fromCode(portalLayout.getType());
				if(portalLayoutType != null){
					portalLayout.setName(portalLayoutType.getName());
					portalLayout.setLocation(portalLayoutType.getLocation());
				}
			}else {
				portalLayout.setName(EhPortalLayouts.class.getSimpleName() + portalLayout.getId());
				portalLayout.setLocation("/" + portalLayout.getName());
			}
			refleshItemLocation(portalLayout.getId(), portalLayout.getLocation());
			//}


			portalLayoutProvider.updatePortalLayout(portalLayout);
			return null;
		});

		return processPortalLayoutDTO(portalLayout);
	}

	private void checkIndexPortalLayout(Long versionId, Byte type){

		PortalLayout indexPortalLayout = portalLayoutProvider.findIndexPortalLayout(versionId, type);

		if(indexPortalLayout != null){
			LOGGER.error("index layout already exists version = {}, type = {}", versionId, type);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"index layout already exists version = {}, type = {}", versionId, type);
		}
	}

	private void refleshItemLocation(Long portalLayoutId, String location){

		List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroup(portalLayoutId);
		if(portalItemGroups == null){
			return;
		}

		for(PortalItemGroup portalItemGroup: portalItemGroups){
			List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(portalItemGroup.getId());

			if(portalItems == null){
				continue;
			}

			for(PortalItem portalItem: portalItems){

				if(!location.equals(portalItem.getItemLocation())){
					portalItem.setItemLocation(location);
					portalItemProvider.updatePortalItem(portalItem);
				}

			}

		}

	}


	@Override
	public PortalLayoutDTO findIndexPortalLayout(FindIndexPortalLayoutCommand cmd) {

		if(cmd.getVersionId() == null || PortalLayoutType.fromCode(cmd.getType()) == null){
			LOGGER.error("error invalid parameter, cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error invalid parameter, cmd = {}", cmd);
		}

		PortalLayout portalLayout = portalLayoutProvider.findIndexPortalLayout(cmd.getVersionId(), cmd.getType());

		return ConvertHelper.convert(portalLayout, PortalLayoutDTO.class);
	}

    @Override
	public void deletePortalLayout(DeletePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalLayout.getVersionId());

		portalLayout.setStatus(PortalLayoutStatus.INACTIVE.getCode());
		portalLayout.setOperatorUid(user.getId());
		portalLayoutProvider.updatePortalLayout(portalLayout);
	}

	private PortalLayout checkPortalLayout(Long id){
		PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(id);
		if(null == portalLayout){
			LOGGER.error("Unable to find the portalLayout.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalLayout.");
		}

		return portalLayout;
	}

	@Override
	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd) {
		List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroup(cmd.getLayoutId());

		List<PortalItemGroupDTO> dtos = portalItemGroups.stream().map(r ->{
			return processPortalItemGroupDTO(r);
		}).collect(Collectors.toList());

		Collections.sort(dtos, new Comparator<PortalItemGroupDTO>() {
			@Override
			public int compare(PortalItemGroupDTO o1, PortalItemGroupDTO o2) {
				Integer order1 = 0;
				if(null != o1.getDefaultOrder()){
					order1 = o1.getDefaultOrder();
				}

				Integer order2 = 0;
				if(null != o2.getDefaultOrder()){
					order2 = o2.getDefaultOrder();
				}
				return order1 - order2;
			}
		});
		return new ListPortalItemGroupsResponse(dtos);
	}

	@Override
	public PortalItemGroupDTO createPortalItemGroup(CreatePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getLayoutId());


		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalLayout.getVersionId());


		PortalItemGroup portalItemGroup = ConvertHelper.convert(cmd, PortalItemGroup.class);
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalLayout.getNamespaceId());
		portalItemGroup.setNamespaceId(namespaceId);
		portalItemGroup.setLayoutId(portalLayout.getId());
		portalItemGroup.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
		portalItemGroup.setCreatorUid(user.getId());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroup.setVersionId(portalLayout.getVersionId());

		portalItemGroup.setTitle(cmd.getTitle());
		portalItemGroup.setTitleFlag(cmd.getTitleFlag());
		portalItemGroup.setTitleUri(cmd.getTitleUri());
		portalItemGroup.setTitleStyle(cmd.getTitleStyle());
		portalItemGroup.setTitleMoreFlag(cmd.getTitleMoreFlag());
		portalItemGroup.setTitleSize(cmd.getTitleSize());
		portalItemGroup.setSubTitle(cmd.getSubTitle());

		Integer maxDefaultOrder = portalItemGroupProvider.findMaxDefaultOrder(portalLayout.getId());
		if(maxDefaultOrder == null){
			maxDefaultOrder = 0;
		}
		portalItemGroup.setDefaultOrder(maxDefaultOrder + 1);

		portalItemGroupProvider.createPortalItemGroup(portalItemGroup);
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public PortalItemGroupDTO updatePortalItemGroup(UpdatePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());


		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalItemGroup.getVersionId());

		portalItemGroup.setLabel(cmd.getLabel());
		portalItemGroup.setSeparatorFlag(cmd.getSeparatorFlag());
		portalItemGroup.setSeparatorHeight(cmd.getSeparatorHeight());
		portalItemGroup.setWidget(cmd.getWidget());
		portalItemGroup.setStyle(cmd.getStyle());
		portalItemGroup.setInstanceConfig(cmd.getInstanceConfig());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroup.setDescription(cmd.getDescription());
		portalItemGroup.setContentType(cmd.getContentType());
		portalItemGroup.setTitle(cmd.getTitle());
		portalItemGroup.setTitleFlag(cmd.getTitleFlag());
		portalItemGroup.setTitleUri(cmd.getTitleUri());
		portalItemGroup.setTitleStyle(cmd.getTitleStyle());
		portalItemGroup.setTitleMoreFlag(cmd.getTitleMoreFlag());
		portalItemGroup.setTitleSize(cmd.getTitleSize());
		portalItemGroup.setSubTitle(cmd.getSubTitle());
		portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public void deletePortalItemGroup(DeletePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalItemGroup.getVersionId());

		portalItemGroup.setStatus(PortalItemGroupStatus.INACTIVE.getCode());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
	}

	private PortalItemGroup checkPortalItemGroup(Long id){
		PortalItemGroup portalItemGroup = portalItemGroupProvider.findPortalItemGroupById(id);
		if(null == portalItemGroup){
			LOGGER.error("Unable to find the portalItemGroup.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItemGroup.");
		}

		return portalItemGroup;
	}

	private PortalItemGroupDTO processPortalItemGroupDTO(PortalItemGroup portalItemGroup){
		PortalItemGroupDTO dto = ConvertHelper.convert(portalItemGroup, PortalItemGroupDTO.class);
		dto.setCreateTime(portalItemGroup.getCreateTime().getTime());
		dto.setUpdateTime(portalItemGroup.getUpdateTime().getTime());
		ItemGroupInstanceConfig config = (ItemGroupInstanceConfig)StringHelper.fromJsonString(portalItemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);

		if(!StringUtils.isEmpty(portalItemGroup.getTitleUri())){
			String url = contentServerService.parserUri(portalItemGroup.getTitleUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setTitleUrl(url);
		}

		if(null != config){
			if(!StringUtils.isEmpty(config.getIconUri())){
				String url = contentServerService.parserUri(config.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				config.setIconUrl(url);
			}

			if(!StringUtils.isEmpty(config.getAllOrMoreIconUri())){
				String url = contentServerService.parserUri(config.getAllOrMoreIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				config.setAllOrMoreIconUrl(url);
			}


			//历史遗留问题，titleFlag和title放在了config中，但它们是组件公有属性，应该放在PortalItemGroup。改正之后，下面代码是兼容数据用的。
			if(StringUtils.isEmpty(dto.getTitleFlag()) && StringUtils.isEmpty(dto.getTitle())){
				dto.setTitleFlag(config.getTitleFlag());
				dto.setTitle(config.getTitle());
			}

			dto.setInstanceConfig(StringHelper.toJsonString(config));
		}
		return dto;
	}

	@Override
	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<PortalItem> portalItems = portalItemProvider.listPortalItems(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				Condition cond = Tables.EH_PORTAL_ITEMS.ITEM_GROUP_ID.eq(cmd.getItemGroupId());
				if(null != PortalScopeType.fromCode(cmd.getScopeType())){
					Condition cond1 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_TYPE.eq(cmd.getScopeType());
					Condition cond2 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(cmd.getScopeId());

					//如果是选择具体场景的实体 则要合上具体场景的 全部条件
					if(0 != cmd.getScopeId()){
						cond2 = cond2.or(Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(0L));
					}
					cond1 = cond1.and(cond2);

					//如果选择的场景是管理公司或者普通公司，则需要合上园区场景条件
					if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM || PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
						Condition cond3 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_TYPE.eq(PortalScopeType.COMMERCIAL.getCode());

						//合上园区场景的 全部条件
						Condition cond4 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(0L);

						//合上公司所入住的园区场景具体实体条件
						if(0 != cmd.getScopeId()){
							OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getScopeId());
							if(null != request){
								cond4 = cond4.or(Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(request.getCommunityId()));
							}
						}
						cond3 = cond3.and(cond4);
						cond1 = cond1.or(cond3);
					}
					cond = cond.and(cond1);
				}
				query.addConditions(cond);
				return query;
			}
		});
		ListPortalItemsResponse response = new ListPortalItemsResponse();
		response.setNextPageAnchor(locator.getAnchor());
		List<PortalItemDTO> dtos = portalItems.stream().map(r ->{
			return processPortalItemDTO(r);
		}).collect(Collectors.toList());
		response.setPortalItems(dtos);

		return response;
	}


	@Override
	public List<PortalItemDTO> listPortalItemsByItemGroupId(ListPortalItemsCommand cmd) {
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(cmd.getItemGroupId());
		return portalItems.stream().map(r ->{
			return processPortalItemDTO(r);
		}).collect(Collectors.toList());
	}


	@Override
	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getItemGroupId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalItemGroup.getVersionId());

		PortalLayout portalLayout = checkPortalLayout(portalItemGroup.getLayoutId());
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalItemGroup.getNamespaceId());
		PortalItem portalItem = ConvertHelper.convert(cmd, PortalItem.class);
		portalItem.setNamespaceId(namespaceId);
		if(null == PortalItemStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemStatus.ACTIVE.getCode());
		}
		if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
			portalItem.setDefaultOrder(10000);
		}else {
			Integer maxDefaultOrder = portalItemProvider.findMaxDefaultOrder(portalItemGroup.getId());
			if(maxDefaultOrder == null){
				maxDefaultOrder = 0;
			}

			if(maxDefaultOrder >= 10000){
				maxDefaultOrder = 9998;
			}
			portalItem.setDefaultOrder(maxDefaultOrder + 1);


		}
		portalItem.setCreatorUid(user.getId());
		portalItem.setOperatorUid(user.getId());
		portalItem.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());

		portalItem.setGroupName(portalItemGroup.getName());
		portalItem.setItemLocation(portalLayout.getLocation());
		portalItem.setVersionId(portalItemGroup.getVersionId());
		this.dbProvider.execute((status) -> {
			portalItemProvider.createPortalItem(portalItem);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				createContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), portalItem.getVersionId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemDTO(portalItem);
	}

	@Override
	public PortalItemDTO updatePortalItem(UpdatePortalItemCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItem portalItem = checkPortalItem(cmd.getId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalItem.getVersionId());

		if(null == PortalItemStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemStatus.ACTIVE.getCode());
		}else{
			portalItem.setStatus(cmd.getStatus());
		}
		portalItem.setOperatorUid(user.getId());
		portalItem.setLabel(cmd.getLabel());
		portalItem.setStatus(cmd.getStatus());
		portalItem.setActionType(cmd.getActionType());
		portalItem.setActionData(cmd.getActionData());
		portalItem.setDescription(cmd.getDescription());
		portalItem.setIconUri(cmd.getIconUri());
		portalItem.setBgcolor(cmd.getBgcolor());
		portalItem.setSelectedIconUri(cmd.getSelectedIconUri());
		portalItem.setItemWidth(cmd.getItemWidth());
		this.dbProvider.execute((status) -> {
			portalItemProvider.updatePortalItem(portalItem);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
				createContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), portalItem.getVersionId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemDTO(portalItem);
	}

	@Override
	public void deletePortalItem(DeletePortalItemCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());

		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(portalItem.getVersionId());

		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setStatus(PortalItemGroupStatus.INACTIVE.getCode());
		this.dbProvider.execute((status) -> {
			portalItemProvider.updatePortalItem(portalItem);
			portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
			return null;
		});
	}

	@Override
	public void setPortalItemStatus(SetPortalItemStatusCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setStatus(cmd.getStatus());
		portalItemProvider.updatePortalItem(portalItem);
	}

	private PortalItemDTO processPortalItemDTO(PortalItem portalItem){
		PortalItemDTO dto = ConvertHelper.convert(portalItem, PortalItemDTO.class);
		dto.setCreateTime(portalItem.getCreateTime().getTime());
		dto.setUpdateTime(portalItem.getUpdateTime().getTime());

		if(!StringUtils.isEmpty(portalItem.getIconUri())){
			String url = contentServerService.parserUri(portalItem.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}

		if(!StringUtils.isEmpty(portalItem.getSelectedIconUri())){
			String url = contentServerService.parserUri(portalItem.getSelectedIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setSelectedIconUrl(url);
		}

		if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(AllOrMoreType.ALL == AllOrMoreType.fromCode(actionData.getType())){
				dto.setContentName(configurationProvider.getValue(ConfigConstants.PORTAL_ITEM_ALL_TITLE, "全部"));
				if(!StringUtils.isEmpty(actionData.getDefUri())){
					String url = contentServerService.parserUri(actionData.getDefUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
					actionData.setDefUrl(url);
				}
				dto.setActionData(StringHelper.toJsonString(actionData));
			}else if(AllOrMoreType.MORE == AllOrMoreType.fromCode(actionData.getType())){
				dto.setContentName(configurationProvider.getValue(ConfigConstants.PORTAL_ITEM_MORE_TITLE, "更多"));
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
			LayoutActionData actionData = (LayoutActionData)StringHelper.fromJsonString(portalItem.getActionData(), LayoutActionData.class);
			PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(actionData.getLayoutId());
			if(null != portalLayout){
				dto.setContentName(portalLayout.getLabel());
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
			ModuleAppActionData actionData = (ModuleAppActionData)StringHelper.fromJsonString(portalItem.getActionData(), ModuleAppActionData.class);
			ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(actionData.getModuleAppId());
			if(null != serviceModuleApp){
//				String moduleAppTitle = configurationProvider.getValue(ConfigConstants.PORTAL_MODULE_APP_TITLE, "应用");
				dto.setContentName(serviceModuleApp.getName());
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.THIRDURL){
			UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(portalItem.getActionData(), UrlActionData.class);
			if(null != actionData)
				dto.setContentName(actionData.getUrl());
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ZUOLINURL){
			UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(portalItem.getActionData(), UrlActionData.class);
			if(null != actionData)
				dto.setContentName(actionData.getUrl());
		}

		List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
		dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));

		User user = userProvider.findUserById(portalItem.getOperatorUid());
		if(null != user){
			dto.setOperatorUName(user.getNickName());
		}

		return dto;
	}

	private PortalItem checkPortalItem(Long id){
		PortalItem portalItem = portalItemProvider.findPortalItemById(id);
		if(null == portalItem){
			LOGGER.error("Unable to find the portalItem.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItem.");
		}

		return portalItem;
	}

	private void createContentScopes(String contentType, Long contentId, Long versionId, List<PortalScope> portalScopes){
		User user = UserContext.current().getUser();
		List<PortalContentScope> scopes = new ArrayList<>();
		for (PortalScope scope: portalScopes) {
			if(null != scope.getScopeIds()){
				for (Long scopeId: scope.getScopeIds()) {
					PortalContentScope contentScope = new PortalContentScope();
					contentScope.setContentType(contentType);
					contentScope.setContentId(contentId);
					contentScope.setOperatorUid(user.getId());
					contentScope.setCreatorUid(user.getId());
					contentScope.setScopeType(scope.getScopeType());
					contentScope.setScopeId(scopeId);
					contentScope.setVersionId(versionId);
					scopes.add(contentScope);
				}
			}
		}
		portalContentScopeProvider.createPortalContentScopes(scopes);
	}

	private List<PortalContentScopeDTO> processListPortalContentScopeDTO(List<PortalContentScope> portalContentScopes){
		Map<String, List<PortalScopeDTO>> portalScopeMap = new HashMap<>();
		for (PortalContentScope portalContentScope: portalContentScopes) {
			if(null == portalScopeMap.get(portalContentScope.getScopeType())){
				List<PortalScopeDTO> scopes = new ArrayList<>();
				scopes.add(processPortalScopeDTO(portalContentScope));
				portalScopeMap.put(portalContentScope.getScopeType(), scopes);
			}else{
				portalScopeMap.get(portalContentScope.getScopeType()).add(processPortalScopeDTO(portalContentScope));
			}
		}

		List<PortalContentScopeDTO> dtos = new ArrayList<>();
		for (Map.Entry<String, List<PortalScopeDTO>> entry : portalScopeMap.entrySet()) {
			PortalContentScopeDTO dto = new PortalContentScopeDTO();
			dto.setScopeType(entry.getKey());
			dto.setScopes(entry.getValue());
			dtos.add(dto);
		}
		return dtos;
	}

	private PortalScopeDTO processPortalScopeDTO(PortalContentScope portalContentScope){
		PortalScopeDTO dto = ConvertHelper.convert(portalContentScope, PortalScopeDTO.class);
		if(PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.PM || PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.ORGANIZATION){
			Organization organization = organizationProvider.findOrganizationById(portalContentScope.getScopeId());
			if(null != organization){
				dto.setScopeName(organization.getName());
			}
		}else if(PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.COMMERCIAL || PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.RESIDENTIAL){
			Community community = communityProvider.findCommunityById(portalContentScope.getScopeId());
			if(null != community){
				dto.setScopeName(community.getName());
			}
		}

		if(portalContentScope.getScopeId() == 0L){
			dto.setScopeName("全部");
		}
		return dto;
	}

	@Override
	public void reorderPortalItem(ReorderPortalItemCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemReorder portalItemReorder : cmd.getReorders()) {
				PortalItem portalItem = checkPortalItem(portalItemReorder.getItemId());
				portalItem.setOperatorUid(user.getId());
				portalItem.setDefaultOrder(portalItemReorder.getDefaultOrder());
				if(null != ItemDisplayFlag.fromCode(portalItemReorder.getDisplayFlag()))
					portalItem.setDisplayFlag(portalItemReorder.getDisplayFlag());
				portalItemProvider.updatePortalItem(portalItem);
			}
			return null;
		});
	}

	@Override
	public void reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemGroupReorder portalItemGroupReorder : cmd.getReorders()) {
				PortalItemGroup portalItemGroup = checkPortalItemGroup(portalItemGroupReorder.getItemGroupId());
				portalItemGroup.setOperatorUid(user.getId());
				portalItemGroup.setDefaultOrder(portalItemGroupReorder.getDefaultOrder());
				portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
			}
			return null;
		});
	}

	@Override
	public PortalItemDTO getPortalItemById(GetPortalItemByIdCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		return processPortalItemDTO(portalItem);
	}

	@Override
	public PortalItemCategoryDTO getPortalItemCategoryById(GetPortalItemCategoryCommand cmd) {
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		PortalItemCategoryDTO dto = processPortalItemCategoryDTO(portalItemCategory);
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByCategoryId(portalItemCategory.getId());

		List<PortalItemDTO> items = portalItems.stream().map(i ->{
			return processPortalItemDTO(i);
		}).collect(Collectors.toList());

		dto.setItems(items);
		return dto;
	}


	@Override
	public ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd) {
		Integer namespaceId= UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		checkPortalItemGroup(cmd.getItemGroupId());
		List<PortalItemCategory> portalItemCategories = portalItemCategoryProvider.listPortalItemCategory(namespaceId, cmd.getItemGroupId());
		PortalItemCategory category = new PortalItemCategory();
		category.setId(0L);
		category.setName("未分组");
		portalItemCategories.add(category);
		List<PortalItemCategoryDTO> dtos = portalItemCategories.stream().map(r ->{
			PortalItemCategoryDTO dto = processPortalItemCategoryDTO(r);
			List<PortalItem> portalItems = portalItemProvider.listPortalItems(r.getId(), cmd.getItemGroupId());

			List<PortalItemDTO> items = portalItems.stream().map(i ->{
				return processPortalItemDTO(i);
			}).collect(Collectors.toList());

			Collections.sort(items, new Comparator<PortalItemDTO>() {
				@Override
				public int compare(PortalItemDTO o1, PortalItemDTO o2) {
					Integer order1 = 0;
					if(null != o1.getMoreOrder()) order1 = o1.getMoreOrder();

					Integer order2 = 0;
					if(null != o2.getMoreOrder()) order2 = o2.getMoreOrder();
					return order1 - order2;
				}
			});
			dto.setItems(items);
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(dtos, new Comparator<PortalItemCategoryDTO>() {
			@Override
			public int compare(PortalItemCategoryDTO o1, PortalItemCategoryDTO o2) {
				Integer order1 = 0;
				if(null != o1.getDefaultOrder()) order1 = o1.getDefaultOrder();

				Integer order2 = 0;
				if(null != o2.getDefaultOrder()) order2 = o2.getDefaultOrder();
				return order1 - order2;
			}
		});
		return new ListPortalItemCategoriesResponse(dtos);
	}

	@Override
	public PortalItemCategoryDTO createPortalItemCategory(CreatePortalItemCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalItemGroup itemGroup = checkPortalItemGroup(cmd.getItemGroupId());
		PortalItemCategory portalItemCategory = ConvertHelper.convert(cmd, PortalItemCategory.class);
		portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());
		portalItemCategory.setLabel(cmd.getName());
		portalItemCategory.setName(null);
		portalItemCategory.setOperatorUid(user.getId());
		portalItemCategory.setCreatorUid(user.getId());
		portalItemCategory.setNamespaceId(namespaceId);
		portalItemCategory.setItemGroupId(cmd.getItemGroupId());
		portalItemCategory.setVersionId(itemGroup.getVersionId());

		Integer defaultOrder = portalItemCategoryProvider.findDefaultOrder(cmd.getItemGroupId());
		if(defaultOrder == null){
			defaultOrder = 1;
		}
		portalItemCategory.setDefaultOrder(defaultOrder + 1);
		this.dbProvider.execute((status) -> {
			portalItemCategoryProvider.createPortalItemCategory(portalItemCategory);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				createContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), portalItemCategory.getVersionId(), cmd.getScopes());
			}
			return null;
		});
		return processPortalItemCategoryDTO(portalItemCategory);
	}

	@Override
	public PortalItemCategoryDTO updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd) {
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		portalItemCategory.setLabel(cmd.getName());
		portalItemCategory.setOperatorUid(UserContext.current().getUser().getId());
		portalItemCategory.setIconUri(cmd.getIconUri());
		this.dbProvider.execute((status) -> {
			portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
				createContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), portalItemCategory.getVersionId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemCategoryDTO(portalItemCategory);
	}

	@Override
	public void deletePortalItemCategory(DeletePortalItemCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		checkPortalItemCategory(cmd.getMoveItemCategoryId());
		portalItemCategory.setOperatorUid(user.getId());
		portalItemCategory.setStatus(PortalItemCategoryStatus.INACTIVE.getCode());

		this.dbProvider.execute((status) -> {
			List<PortalItem> portalItems = portalItemProvider.listPortalItemByCategoryId(portalItemCategory.getId());
			List<PortalItemReorder> itemReorders = new ArrayList<>();
			for (PortalItem portalItem: portalItems) {
				PortalItemReorder reorder = new PortalItemReorder();
				reorder.setItemId(portalItem.getId());
				itemReorders.add(reorder);
			}
			PortalItemCategoryRank rank = new PortalItemCategoryRank();
			rank.setItemCategoryId(cmd.getMoveItemCategoryId());
			rank.setItems(itemReorders);
			//把要删除的分类移动到对应的分类
			setPortalItemCategory(rank, user);
			portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
			return null;
		});
	}

	private PortalItemCategoryDTO processPortalItemCategoryDTO(PortalItemCategory portalItemCategory){
		PortalItemCategoryDTO dto = ConvertHelper.convert(portalItemCategory, PortalItemCategoryDTO.class);
		dto.setName(portalItemCategory.getLabel());
		List<PortalItem> portalItems = getItemAllOrMore(portalItemCategory.getNamespaceId(),portalItemCategory.getItemGroupId(), AllOrMoreType.ALL, null);
		if(portalItems.size() > 0){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItems.get(0).getActionData(), AllOrMoreActionData.class);
			if(null == AlignType.fromCode(portalItemCategory.getAlign())){
				dto.setAlign(actionData.getAlign());
			}

			if(StringUtils.isEmpty(portalItemCategory.getIconUri())){
				portalItemCategory.setIconUri(actionData.getDefUri());
			}
		}

		if(!StringUtils.isEmpty(portalItemCategory.getIconUri())){
			String url = contentServerService.parserUri(portalItemCategory.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}

		List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
		dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));

		return dto;
	}

	@Override
	public PortalItemDTO getAllOrMoreItem(GetItemAllOrMoreCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		checkPortalItemGroup(cmd.getItemGroupId());
		List<PortalItem> items = getItemAllOrMore(namespaceId, cmd.getItemGroupId(), AllOrMoreType.fromCode(cmd.getMoreOrAllType()), cmd.getVersionId());
		if(items.size() > 0){
			return processPortalItemDTO(items.get(0));
		}
		return null;
	}

	private List<PortalItem> getItemAllOrMore(Integer namespaceId,Long itemGroupId, AllOrMoreType type, Long versionId){
		List<PortalItem> items = new ArrayList<>();
		List<PortalItem> portalItems = portalItemProvider.listPortalItems(null, namespaceId, PortalItemActionType.ALLORMORE.getCode(), itemGroupId, PortalItemStatus.INACTIVE.getCode(), versionId);
		for (PortalItem portalItem: portalItems) {
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(null != actionData && AllOrMoreType.fromCode(actionData.getType()) == type){
				items.add(portalItem);
			}
		}
		return items;
	}

	private PortalItemCategory checkPortalItemCategory(Long id){
		PortalItemCategory portalItemCategory = portalItemCategoryProvider.findPortalItemCategoryById(id);
		if(null == portalItemCategory){
			LOGGER.error("Unable to find the portalItemCategory.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItemCategory.");
		}

		return portalItemCategory;
	}

	@Override
	public void reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemCategoryReorder portalItemGroupReorder : cmd.getReorders()) {
				PortalItemCategory portalItemCategory = portalItemCategoryProvider.findPortalItemCategoryById(portalItemGroupReorder.getItemCategoryId());
				portalItemCategory.setOperatorUid(user.getId());
				portalItemCategory.setDefaultOrder(portalItemGroupReorder.getDefaultOrder());
				portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			}
			return null;
		});

	}

	@Override
	public void setPortalItemActionData(SetPortalItemActionDataCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setActionData(cmd.getActionData());
		portalItemProvider.updatePortalItem(portalItem);
	}

	@Override
	public void setItemCategoryDefStyle(SetItemCategoryDefStyleCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		if(!StringUtils.isEmpty(portalItem.getActionData())){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			actionData.setAlign(cmd.getAlign());
			actionData.setDefUri(cmd.getDefUri());
			portalItem.setActionData(StringHelper.toJsonString(actionData));
			portalItemProvider.updatePortalItem(portalItem);
		}
	}

	@Override
	public void rankPortalItemCategory(RankPortalItemCategoryCommand cmd) {
		if(null == cmd.getRanks() && cmd.getRanks().size() == 0){
			LOGGER.error("Params ranks is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params ranks is null.");
		}
		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemCategoryRank portalItemCategoryRank : cmd.getRanks()) {
				setPortalItemCategory(portalItemCategoryRank, user);
			}
			return null;
		});

	}

	private void setPortalItemCategory(PortalItemCategoryRank rank, User user){
		PortalItemCategory portalItemCategory = checkPortalItemCategory(rank.getItemCategoryId());
		if(null != rank.getItems() && rank.getItems().size() > 0){
			for (PortalItemReorder item: rank.getItems()) {
				PortalItem portalItem = checkPortalItem(item.getItemId());
				//更多全部不进行分类
				if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
					continue;
				}
				portalItem.setOperatorUid(user.getId());
				portalItem.setItemCategoryId(portalItemCategory.getId());
				if(null != ItemDisplayFlag.fromCode(item.getDisplayFlag()))
					portalItem.setDisplayFlag(item.getDisplayFlag());
				if(null != item.getMoreOrder())
					portalItem.setMoreOrder(item.getMoreOrder());
				if(null != portalItem.getDefaultOrder())
					portalItem.setDefaultOrder(item.getDefaultOrder());
				portalItemProvider.updatePortalItem(portalItem);
			}
		}
	}


	@Override
	public PortalItemGroupDTO getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public ListPortalNavigationBarsResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd) {
		List<PortalNavigationBar> portalNavigationBars = portalNavigationBarProvider.listPortalNavigationBar(cmd.getVersionId());
		return new ListPortalNavigationBarsResponse(portalNavigationBars.stream().map(r ->{
			return processPortalNavigationBarDTO(r);
		}).collect(Collectors.toList()));
	}

    @Override
    public ListLaunchPadIndexResponse listLaunchPadIndexs(ListLaunchPadIndexCommand cmd) {
	    ListLaunchPadIndexResponse response = new ListLaunchPadIndexResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<LaunchPadIndexDTO> list = new ArrayList<>();
        String indexFlag = configurationProvider.getValue(cmd.getNamespaceId(),ConfigConstants.INDEX_FLAG, "");
        if (!StringUtils.isEmpty(indexFlag) && TrueOrFalseFlag.TRUE.getCode().equals(Byte.valueOf(indexFlag))) {
            response.setIndexFlag(TrueOrFalseFlag.TRUE.getCode());
        }else {
            response.setIndexFlag(TrueOrFalseFlag.FALSE.getCode());
        }
        List<LaunchPadIndex> launchPadIndices = launchPadIndexProvider.queryLaunchPadIndexs(locator, 100, (locator1, query) -> {
            query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
            query.addOrderBy(Tables.EH_LAUNCH_PAD_INDEXS.DEFAULT_ORDER.asc());
            return query;
        });
        launchPadIndices.stream().forEach(r-> {
            LaunchPadIndexDTO dto = ConvertHelper.convert(r, LaunchPadIndexDTO.class);
            if(!StringUtils.isEmpty(r.getIconUri())){
                String url = contentServerService.parserUri(r.getIconUri());
                dto.setIconUrl(url);
            }
            if(!StringUtils.isEmpty(r.getSelectedIconUri())){
                String url = contentServerService.parserUri(r.getSelectedIconUri());
                dto.setSelectedIconUrl(url);
            }
            list.add(dto);
        });
        response.setLaunchPadIndexs(list);
        return response;
    }

    @Override
    public void updateIndexFlag(UpdateIndexFlagCommand cmd) {
        ConfigurationsCreateAdminCommand configurationsCreateAdminCommand = new ConfigurationsCreateAdminCommand();
        configurationsCreateAdminCommand.setName(ConfigConstants.INDEX_FLAG);
        configurationsCreateAdminCommand.setValue(cmd.getIndexFlag().toString());
        configurationsService.crteateConfiguration(configurationsCreateAdminCommand);
    }

	@Override
	public GetIndexFlagResponse getIndexFlag(GetIndexFlagCommand cmd) {
		GetIndexFlagResponse response = new GetIndexFlagResponse();
		String indexFlag = configurationProvider.getValue(cmd.getNamespaceId(),ConfigConstants.INDEX_FLAG, "");
		if (!StringUtils.isEmpty(indexFlag) && TrueOrFalseFlag.TRUE.getCode().equals(Byte.valueOf(indexFlag))) {
			response.setIndexFlag(TrueOrFalseFlag.TRUE.getCode());
		}else {
			response.setIndexFlag(TrueOrFalseFlag.FALSE.getCode());
		}
		return response;
	}

	@Override
	public PortalNavigationBarDTO createPortalNavigationBar(CreatePortalNavigationBarCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalNavigationBar portalNavigationBar = ConvertHelper.convert(cmd, PortalNavigationBar.class);
		portalNavigationBar.setOperatorUid(user.getId());
		portalNavigationBar.setCreatorUid(user.getId());
		portalNavigationBar.setNamespaceId(namespaceId);
		portalNavigationBar.setStatus(PortalNavigationBarStatus.ACTIVE.getCode());
		Integer maxOrder = this.portalNavigationBarProvider.maxOrder(cmd.getNamespaceId(), cmd.getVersionId());
		if (maxOrder != null) {
		    portalNavigationBar.setDefaultOrder(maxOrder + 1);
        }else {
		    portalNavigationBar.setDefaultOrder(1);
        }
		portalNavigationBarProvider.createPortalNavigationBar(portalNavigationBar);
		return processPortalNavigationBarDTO(portalNavigationBar);
	}

	@Override
	public PortalNavigationBarDTO updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd) {
		PortalNavigationBar portalNavigationBar = checkPortalNavigationBar(cmd.getId());
		portalNavigationBar.setLabel(cmd.getLabel());
		portalNavigationBar.setDescription(cmd.getDescription());
		portalNavigationBar.setType(cmd.getType());
		portalNavigationBar.setConfigJson(cmd.getConfigJson());
		portalNavigationBar.setIconUri(cmd.getIconUri());
		portalNavigationBar.setSelectedIconUri(cmd.getSelectedIconUri());
		portalNavigationBar.setOperatorUid(UserContext.current().getUser().getId());
		portalNavigationBar.setTopBarStyle(cmd.getTopBarStyle());
		portalNavigationBarProvider.updatePortalNavigationBar(portalNavigationBar);
		return processPortalNavigationBarDTO(portalNavigationBar);
	}

    @Override
    public void updatePortalNavigationBarOrder(List<Long> ids) {
        for (int i = 0; i< ids.size(); i++) {
            PortalNavigationBar portalNavigationBar = this.portalNavigationBarProvider.findPortalNavigationBarById(ids.get(i));
            if (portalNavigationBar != null) {
                portalNavigationBar.setDefaultOrder(i + 1);
                this.portalNavigationBarProvider.updatePortalNavigationBar(portalNavigationBar);
            }
        }
    }

    @Override
	public void deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd) {

		portalNavigationBarProvider.deletePortalNavigationBar(cmd.getId());
	}

	private PortalNavigationBar checkPortalNavigationBar(Long id){
		PortalNavigationBar portalNavigationBar = portalNavigationBarProvider.findPortalNavigationBarById(id);
		if(null == portalNavigationBar){
			LOGGER.error("Unable to find the portalNavigationBar.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalNavigationBar.");
		}

		return portalNavigationBar;
	}

	private PortalNavigationBarDTO processPortalNavigationBarDTO(PortalNavigationBar portalNavigationBar){
		PortalNavigationBarDTO dto = ConvertHelper.convert(portalNavigationBar, PortalNavigationBarDTO.class);
		dto.setCreateTime(portalNavigationBar.getCreateTime().getTime());
		dto.setUpdateTime(portalNavigationBar.getUpdateTime().getTime());
		User operator = userProvider.findUserById(portalNavigationBar.getOperatorUid());
		if(null != operator) dto.setOperatorUName(operator.getNickName());

		//TODO
//
//		if(EntityType.fromCode(portalNavigationBar.getTargetType()) == EntityType.PORTAL_LAYOUT){
//			PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(portalNavigationBar.getTargetId());
//			if(null != portalLayout){
//				String layoutTitle = configurationProvider.getValue(ConfigConstants.PORTAL_LAYOUT_TITLE, "门户");
//				dto.setContentName(layoutTitle + "-" + portalLayout.getLabel());
//			}
//		}else if(EntityType.fromCode(portalNavigationBar.getTargetType()) == EntityType.SERVICE_MODULE_APP){
//			ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(portalNavigationBar.getTargetId());
//			if(null != serviceModuleApp){
//				String moduleAppTitle = configurationProvider.getValue(ConfigConstants.PORTAL_MODULE_APP_TITLE, "应用");
//				dto.setContentName(moduleAppTitle + "-" + serviceModuleApp.getName());
//			}
//		}
		if(!StringUtils.isEmpty(portalNavigationBar.getIconUri())){
			String url = contentServerService.parserUri(portalNavigationBar.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}
		if(!StringUtils.isEmpty(portalNavigationBar.getSelectedIconUri())){
			String url = contentServerService.parserUri(portalNavigationBar.getSelectedIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setSelectedIconUrl(url);
		}
		return dto;
	}

	@Override
	public ListScopeResponse listScopes(ListScopeCommand cmd){
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ListScopeResponse response = new ListScopeResponse();
		List<ScopeDTO> dtos = new ArrayList<>();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());
		OrganizationType oType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM){
			oType = OrganizationType.PM;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
			oType = OrganizationType.ENTERPRISE;
		}
		if(null != oType){
			List<String> groupTypes = new ArrayList<>();
			groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
			//将管理公司和普通公司合并 addby yanlong.liang 20181213
			List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, cmd.getKeywords(),"", locator, pageSize);
			for (Organization organization: organizations) {
				dtos.add(ConvertHelper.convert(organization, ScopeDTO.class));
			}
			response.setDtos(dtos);
			response.setNextPageAnchor(locator.getAnchor());
			return response;
		}

		CommunityType cType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.COMMERCIAL){
			cType = CommunityType.COMMERCIAL;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.RESIDENTIAL){
			cType = CommunityType.RESIDENTIAL;
		}
		CommunityType communityType = cType;
		if(null != communityType){
			List<Community> communities = communityProvider.listCommunities(namespaceId, locator, pageSize, new ListingQueryBuilderCallback() {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				    //将园区和小区合并 addby yanlong.liang 20181213
//					query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType.getCode()));
					if(!StringUtils.isEmpty(cmd.getKeywords())){
						query.addConditions(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeywords() + "%").or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeywords() + "%")));

					}
					return query;
				}
			});
			for (Community community: communities) {
				dtos.add(ConvertHelper.convert(community, ScopeDTO.class));
			}
		}
		response.setDtos(dtos);
		response.setNextPageAnchor(locator.getAnchor());
		return response;
	}


	@Override
	public ListScopeResponse searchScopes(ListScopeCommand cmd){

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		if(null == cmd.getAnchor()){
			cmd.setAnchor(0L);
		}
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ListScopeResponse response = new ListScopeResponse();
		List<ScopeDTO> dtos = new ArrayList<>();
		OrganizationType oType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM){
			oType = OrganizationType.PM;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
			oType = OrganizationType.ENTERPRISE;
		}
		if(null != oType){
			SearchOrganizationCommand command = new SearchOrganizationCommand();
			command.setNamespaceId(namespaceId);
			//不需要区分管理公司和普通公司  add by yanlong.liang 20181213
//			command.setOrganizationType(oType.getCode());
			command.setPageAnchor(cmd.getAnchor());
			command.setPageSize(pageSize);
			command.setKeyword(cmd.getKeywords());
			OrganizationQueryResult result = organizationSearcher.queryOrganization(command);
			response.setDtos(result.getDtos().stream().map(r ->{
				return ConvertHelper.convert(r, ScopeDTO.class);
			}).collect(Collectors.toList()));
			response.setNextPageAnchor(result.getPageAnchor());
			return response;
		}

		CommunityType cType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.COMMERCIAL){
			cType = CommunityType.COMMERCIAL;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.RESIDENTIAL){
			cType = CommunityType.RESIDENTIAL;
		}
		CommunityType communityType = cType;
		if(null != communityType){
		    //不需要区分园区和小区 add by yanlong.liang 20181213
			List<CommunityDoc> communities = communitySearcher.searchDocs(cmd.getKeywords(), null, null, null, cmd.getAnchor().intValue(), cmd.getPageSize());
			for (CommunityDoc communityDoc: communities) {
				dtos.add(ConvertHelper.convert(communityDoc, ScopeDTO.class));
			}
		}
		response.setDtos(dtos);
		response.setNextPageAnchor(cmd.getAnchor() + 1);
		return response;
	}

	@Override
	public PortalPublishLogDTO publish(PublishCommand cmd) {


		//正式版本保护机制，不能对正式版本做编辑和发布。
		protectReleaseVersion(cmd.getVersionId());

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalLayout> layouts = portalLayoutProvider.listPortalLayout(cmd.getNamespaceId(), null, cmd.getVersionId());

		checkPublishParam(cmd);


		//生成版本发布log
		PortalPublishLog portalPublishLog = createNewPortalPublishLog(cmd.getNamespaceId(), user, cmd.getVersionId());

		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				try{

					//保证同一个域空间只有一个人在发布，两个人同时发布的时候会阻塞，阻塞结束之后protectReleaseVersion会报错。
					coordinationProvider.getNamedLock(CoordinationLocks.PORTAL_PUBLISH.getCode() + namespaceId).enter(() -> {

						try {
							//正式版本保护机制，不能对正式版本做编辑和发布。
							protectReleaseVersion(cmd.getVersionId());
						}catch (Exception ex){
							portalPublishLog.setStatus(PortalPublishLogStatus.FAILING.getCode());
							String localizedString = localeStringService.getLocalizedString(PortalErrorCode.SCOPE, String.valueOf(PortalErrorCode.ERROR_VERSION_CONFLICT), UserContext.current().getUser().getLocale(), null);
							portalPublishLog.setContentData(localizedString);
							portalPublishLogProvider.updatePortalPublishLog(portalPublishLog);
						}

						UserContext.setCurrentUser(user);
						//login为null时，生成url会缺少token
						UserContext.current().setLogin(new UserLogin());
						//同步和发布的时候不用预览账号
						UserContext.current().setPreviewPortalVersionId(null);
						dbProvider.execute((status) -> {

							//清理预览版本的用户信息
							portalVersionUserProvider.deletePortalVersionUsers(namespaceId);

							//清理预览版本服务广场数据
							removePreviewVersion(namespaceId);

							//将当前版本改成release版本
							//服务广场layout的版本号用release版本的日期和大版本号组合，例如2018013001
							//从而服务广场layout的preview和release版本的版本号一致
							if(PortalPublishType.fromCode(cmd.getPublishType()) == PortalPublishType.RELEASE) {

								//正式发布时清理预览版本的状态
								removePreviewVersionStatus(namespaceId);

								//更新版本号为新的版本
								updateVersionToNewVersion(cmd.getVersionId());

								//更新正式版本标志
								updateReleaseVersion(namespaceId, cmd.getVersionId());
							}else {

								//增加预览版本次数，用于生成版本本号
								PortalVersion releaseVersion = findReleaseVersion(namespaceId);
								releaseVersion.setPreviewCount(releaseVersion.getPreviewCount() + 1);
								portalVersionProvider.updatePortalVersion(releaseVersion);
							}

							//发布应用
							publishServiceModuleApp(namespaceId, cmd.getVersionId());

							//发布item分类
							publishItemCategory(namespaceId, cmd.getVersionId(), cmd.getPublishType());

							//删除已有的layout
							deleteLayoutBeforePublish(namespaceId, cmd.getPublishType());


							for (PortalLayout layout: layouts) {

								//标准版不能删除
								if(namespacesService.isStdNamespace(namespaceId)){
									continue;
								}
								//发布layout
								publishLayout(layout, cmd.getVersionId(), cmd.getPublishType());
							}
                            //发布主页签 add by yanlong.liang 20181217
                            publishNavigationBar(cmd.getVersionId(), cmd.getPublishType());


						//正式发布之后，在此基础上生成小本版
						if(PortalPublishType.fromCode(cmd.getPublishType()) == PortalPublishType.RELEASE){

								//新版本复制一个小版本，比如发布3.1版本变成了5.0版本，那5.0要复制一个5.1，3.0也要复制一个新的3.1
								copyPortalToNewMinorVersion(namespaceId, cmd.getVersionId());

								PortalVersion publishVersion = portalVersionProvider.findPortalVersionById(cmd.getVersionId());
								//给发布的版本的父辈复制一个小版本，比如发布3.1版本变成了5.0版本，那5.0要复制一个5.1，3.0也要复制一个新的3.1
								copyPortalToNewMinorVersion(namespaceId, publishVersion.getParentId());

								//清理很的老版本
								cleanOldVersion(namespaceId);

							}else {
								//更新预览版本标志
								updatePreviewVersion(namespaceId, cmd.getVersionId());

								//更新预览用户
								updatePortalVersionUsers(namespaceId, cmd.getVersionId(), cmd.getVersionUserIds());
							}

							portalPublishLog.setProcess(100);
							portalPublishLog.setStatus(PortalPublishLogStatus.SUCCESS.getCode());
							portalPublishLogProvider.updatePortalPublishLog(portalPublishLog);

							return null;
						});

						return null;
					});


				}catch (Exception e){
					portalPublishLog.setStatus(PortalPublishLogStatus.FAILING.getCode());
					portalPublishLogProvider.updatePortalPublishLog(portalPublishLog);
					LOGGER.error("publish error:", e);
				}finally {
					LOGGER.debug("publish end...");
				}

			}
		});
		return ConvertHelper.convert(portalPublishLog, PortalPublishLogDTO.class);
	}


    @Override
    public void initAppEntryData() {
        List<NamespaceInfoDTO> namespaceInfoDTOS = this.namespacesService.listNamespace();
        List<ServiceModuleAppEntry> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(namespaceInfoDTOS)) {
            for (NamespaceInfoDTO namespaceInfoDTO : namespaceInfoDTOS) {
                PortalVersion version = findReleaseVersion(namespaceInfoDTO.getId());
                if (version != null) {
                    List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleApp(namespaceInfoDTO.getId(), version.getId(), null);
                    if (!CollectionUtils.isEmpty(apps)) {
                        for (ServiceModuleApp app : apps) {
                            List<ServiceModuleEntry> moduleEntries = this.serviceModuleEntryProvider.listServiceModuleEntries(app.getModuleId(),null,
                                    TerminalType.MOBILE.getCode(),null,null);
                            if (!CollectionUtils.isEmpty(moduleEntries)) {
                                for (ServiceModuleEntry moduleEntry : moduleEntries) {
                                    ServiceModuleAppEntry appEntry = ConvertHelper.convert(moduleEntry, ServiceModuleAppEntry.class);
                                    appEntry.setAppId(app.getOriginId());
                                    appEntry.setAppName(app.getName());
                                    appEntry.setId(null);
                                    list.add(appEntry);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.serviceModuleEntryProvider.batchCreateAppEntry(list);
    }

	/**
	 * 正式版本保护机制，不能对正式版本做编辑和发布。
	 * @param versionId
	 */
	private void protectReleaseVersion(Long versionId){

		if(versionId == null){
			return;
		}

		PortalVersion portalVersion = portalVersionProvider.findPortalVersionById(versionId);
		if(portalVersion != null && PortalVersionStatus.fromCode(portalVersion.getStatus()) == PortalVersionStatus.RELEASE){
			LOGGER.error("Page has expired, please refresh page, and edit again.");
			throw RuntimeErrorException.errorWith(PortalErrorCode.SCOPE, PortalErrorCode.ERROR_VERSION_CONFLICT,
					"Page has expired, please refresh page, and edit again.");
		}

	}

	private void deleteLayoutBeforePublish(Integer namespaceId, Byte publishType){

		assert namespaceId != null;

		//标准版不能删除
		if(namespacesService.isStdNamespace(namespaceId)){
			return;
		}

		launchPadProvider.deleteLaunchPadLayout(namespaceId, "ServiceMarketLayout", publishType);
		launchPadProvider.deleteLaunchPadLayout(namespaceId, "SecondServiceMarketLayout", publishType);
		launchPadProvider.deleteLaunchPadLayout(namespaceId, "AssociationLayout", publishType);
	}


	private void checkPublishParam(PublishCommand cmd){
		if(cmd.getVersionId() == null){
			LOGGER.error("invalid parameter, please refresh page and try again. cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameter, please refresh page and try again.");
		}

		PortalVersion portalVersion = portalVersionProvider.findPortalVersionById(cmd.getVersionId());

		if(portalVersion == null || !portalVersion.getNamespaceId().equals(cmd.getNamespaceId())){
			LOGGER.error("invalid parameter, please refresh page and try again. cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameter, please refresh page and try again.");
		}


	}

	private PortalPublishLog createNewPortalPublishLog(Integer namespaceId, User user, Long versionId){
		PortalPublishLog portalPublishLog = new PortalPublishLog();

		portalPublishLog.setNamespaceId(namespaceId);
		portalPublishLog.setStatus(PortalPublishLogStatus.PUBLISHING.getCode());
		portalPublishLog.setCreatorUid(user.getId());
		portalPublishLog.setOperatorUid(user.getId());
		portalPublishLog.setVersionId(versionId);
		portalPublishLog.setProcess(0);
		portalPublishLogProvider.createPortalPublishLog(portalPublishLog);
		return portalPublishLog;
	}

	//发布应用
	private void publishServiceModuleApp(Integer namespaceId, Long versionId){
		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, versionId, null);
		if(apps == null || apps.size() == 0){
			return;
		}

		for(ServiceModuleApp app: apps){
			PortalPublishHandler handler = getPortalPublishHandler(app.getModuleId());
			if(null != handler){

				HandlerPublishCommand cmd = new HandlerPublishCommand();
				cmd.setAppId(app.getId());
				cmd.setAppOriginId(app.getOriginId());

				String instanceConfig = handler.publish(app.getNamespaceId(), app.getInstanceConfig(), app.getName(), cmd);
				app.setInstanceConfig(instanceConfig);

				HandlerGetCustomTagCommand gtCustomTagCommand = new HandlerGetCustomTagCommand();
				gtCustomTagCommand.setAppId(app.getId());
				gtCustomTagCommand.setAppOriginId(app.getOriginId());
				String customTag = handler.getCustomTag(app.getNamespaceId(), app.getModuleId(), app.getInstanceConfig(), gtCustomTagCommand);
				app.setCustomTag(customTag);
				serviceModuleAppProvider.updateServiceModuleApp(app);
			}
			//发布应用时，同时同步应用入口信息
            publishAppEntries(app);
		}

		/**
	     * 所有应用发布完成之后，再给各个应用发送一个通知
	     */
		for(ServiceModuleApp app: apps){
			PortalPublishHandler handler = getPortalPublishHandler(app.getModuleId());
			if(null != handler){
				HandlerAfterAllAppPulishCommand cmd = new HandlerAfterAllAppPulishCommand();
				cmd.setAppId(app.getId());
				cmd.setAppOriginId(app.getOriginId());
				handler.afterAllAppPulish(app, cmd);
			}
		}

	}

	private void publishAppEntries(ServiceModuleApp app) {
        List<ServiceModuleAppEntry> appEntries = this.serviceModuleEntryProvider.listServiceModuleAppEntries(app.getOriginId(), null,
                TerminalType.MOBILE.getCode(), null, null);

        List<ServiceModuleEntry> moduleEntries = this.serviceModuleEntryProvider.listServiceModuleEntries(app.getModuleId(), null,
                TerminalType.MOBILE.getCode(), null, null);
        if (appEntries.size() > moduleEntries.size()) {
            if (CollectionUtils.isEmpty(moduleEntries)) {
                for (ServiceModuleAppEntry appEntry : appEntries) {
                    this.serviceModuleEntryProvider.deleteAppEntry(appEntry.getId());
                }
            } else {
                ServiceModuleEntry moduleEntry = moduleEntries.get(0);
                for (ServiceModuleAppEntry appEntry : appEntries) {
                    if (appEntry.getLocationType().equals(moduleEntry.getLocationType()) && appEntry.getSceneType().equals(moduleEntry.getSceneType())) {
                        appEntry.setAppCategoryId(moduleEntry.getAppCategoryId());
                        appEntry.setDefaultOrder(moduleEntry.getDefaultOrder());
                        appEntry.setEntryName(moduleEntry.getEntryName());
                        appEntry.setIconUri(moduleEntry.getIconUri());
                        this.serviceModuleEntryProvider.udpateAppEntry(appEntry);
                    } else {
                        this.serviceModuleEntryProvider.deleteAppEntry(appEntry.getId());
                    }
                }

            }

        } else if (appEntries.size() < moduleEntries.size()) {
            if (CollectionUtils.isEmpty(appEntries)) {
                for (ServiceModuleEntry moduleEntry : moduleEntries) {
                    ServiceModuleAppEntry appEntry = ConvertHelper.convert(moduleEntry, ServiceModuleAppEntry.class);
                    appEntry.setAppId(app.getOriginId());
                    appEntry.setAppName(app.getName());
                    appEntry.setId(null);
                    this.serviceModuleEntryProvider.createAppEntry(appEntry);
                }
            } else {
                ServiceModuleAppEntry appEntry = appEntries.get(0);
                for (ServiceModuleEntry moduleEntry : moduleEntries) {
                    if (appEntry.getLocationType().equals(moduleEntry.getLocationType()) && appEntry.getSceneType().equals(moduleEntry.getSceneType())) {
                        appEntry.setAppCategoryId(moduleEntry.getAppCategoryId());
                        appEntry.setDefaultOrder(moduleEntry.getDefaultOrder());
                        appEntry.setEntryName(moduleEntry.getEntryName());
                        appEntry.setIconUri(moduleEntry.getIconUri());
                        this.serviceModuleEntryProvider.udpateAppEntry(appEntry);
                    } else {
                        ServiceModuleAppEntry newAppEntry = ConvertHelper.convert(moduleEntry, ServiceModuleAppEntry.class);
                        newAppEntry.setAppId(app.getOriginId());
                        newAppEntry.setAppName(app.getName());
                        newAppEntry.setId(null);
                        this.serviceModuleEntryProvider.createAppEntry(newAppEntry);
                    }
                }
            }
        } else {
            if (!CollectionUtils.isEmpty(appEntries) && !CollectionUtils.isEmpty(moduleEntries)) {
                for (ServiceModuleAppEntry appEntry : appEntries) {
                    for (ServiceModuleEntry moduleEntry : moduleEntries) {
                        if (appEntry.getLocationType().equals(moduleEntry.getLocationType()) && appEntry.getSceneType().equals(moduleEntry.getSceneType())) {
                            appEntry.setAppCategoryId(moduleEntry.getAppCategoryId());
                            appEntry.setDefaultOrder(moduleEntry.getDefaultOrder());
                            appEntry.setEntryName(moduleEntry.getEntryName());
                            appEntry.setIconUri(moduleEntry.getIconUri());
                            this.serviceModuleEntryProvider.udpateAppEntry(appEntry);
                        }
                    }
                }
            }
        }
    }

	private void cleanOldVersion(Integer namespaceId){
		List<PortalVersion> list = portalVersionProvider.listPortalVersion(namespaceId, null);

		if(list == null || list.size() <= 20){
			return;
		}

		//查出20以后的第一个小版本
		int index = 20;
		for( ; index<list.size(); index++){
			if(list.get(index).getMinorVersion() == 1){
				break;
			}
		}

		//删除之后的所有版本
		for(; index<list.size(); index++){
			deleteVersion(list.get(index).getId());
		}
	}


	public PortalPublishLogDTO getPortalPublishLog(GetPortalPublishLogCommand cmd){
		return ConvertHelper.convert(portalPublishLogProvider.findPortalPublishLogById(cmd.getId()), PortalPublishLogDTO.class);
	}

	private void removePreviewVersion(Integer namespaceId){
		launchPadProvider.deletePreviewVersionItems(namespaceId);
		launchPadProvider.deletePreviewVersionCategories(namespaceId);
		launchPadProvider.deletePreviewVersionLayouts(namespaceId);
	}

	/**
	 * 发布成功后将该版本号改成当前日期的大版本
	 * @param versionId
	 */
	private void updateVersionToNewVersion(Long versionId){
		PortalVersion publishVersion = portalVersionProvider.findPortalVersionById(versionId);
		PortalVersion maxBigVersion = portalVersionProvider.findMaxBigVersion(publishVersion.getNamespaceId());

		Calendar calendar = Calendar.getInstance();
		Integer nowDateVersion = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DATE);


		publishVersion.setDateVersion(nowDateVersion);
		if(maxBigVersion.getDateVersion().intValue() == nowDateVersion.intValue()){
			publishVersion.setBigVersion(maxBigVersion.getBigVersion() + 1);
		}else {
			publishVersion.setBigVersion(1);
		}
		publishVersion.setMinorVersion(0);

		//新版本preview count 为0
		publishVersion.setPreviewCount(0);

		portalVersionProvider.updatePortalVersion(publishVersion);
	}


	/**
	 * 正式发布时清理预览版本的状态
	 * @param namespaceId
	 */
	private void removePreviewVersionStatus(Integer namespaceId){


		PortalVersion previewVersion = portalVersionProvider.findPreviewVersion(namespaceId);

		if(previewVersion != null){
			previewVersion.setStatus(null);
		}

		portalVersionProvider.updatePortalVersion(previewVersion);


		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		if(releaseVersion != null){
			releaseVersion.setPreviewCount(0);
		}

		portalVersionProvider.updatePortalVersion(releaseVersion);

	}

	private void publishLayout(PortalLayout layout, Long versionId, Byte publishType){

		User user = UserContext.current().getUser();

		List<PortalLaunchPadMapping> portalLaunchPadMappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_LAYOUT.getCode(), layout.getId(), null);

		//String now = DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH);
		//Long versionCode = Long.valueOf(now + "01");
		LaunchPadLayoutJson layoutJson = new LaunchPadLayoutJson();
		layoutJson.setDisplayName(layout.getLabel());
		layoutJson.setLayoutName(layout.getName());
		List<LaunchPadLayoutGroup> groups = new ArrayList<>();
		List<PortalItemGroup> itemGroups = portalItemGroupProvider.listPortalItemGroup(layout.getId());
		for (PortalItemGroup itemGroup: itemGroups) {
			LaunchPadLayoutGroup group = ConvertHelper.convert(itemGroup, LaunchPadLayoutGroup.class);
			group.setGroupName(itemGroup.getLabel());
			group.setGroupId(itemGroup.getId());
			if(null != itemGroup.getSeparatorFlag()){
				group.setSeparatorFlag(itemGroup.getSeparatorFlag().intValue());
			}

			if(null != itemGroup.getSeparatorHeight()){
				group.setSeparatorHeight(itemGroup.getSeparatorHeight().doubleValue());
			}

			ItemGroupInstanceConfig instanceConfig = new ItemGroupInstanceConfig();
			if(!StringUtils.isEmpty(itemGroup.getInstanceConfig())){
				instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
				group.setColumnCount(instanceConfig.getColumnCount());
			}


//			//设置标题信息
//			setTitleConfig(group, instanceConfig, user);
			if(!StringUtils.isEmpty(itemGroup.getTitleUri())){
				String url = contentServerService.parserUri(itemGroup.getTitleUri(), EntityType.USER.getCode(), user.getId());
				group.setTitleUrl(url);
			}

			//历史遗留问题，titleFlag和title放在了config中，但它们是组件公有属性，应该放在PortalItemGroup。改正之后，下面代码是兼容数据用的。
			if(StringUtils.isEmpty(itemGroup.getTitleFlag()) && StringUtils.isEmpty(itemGroup.getTitle())){
				group.setTitleFlag(instanceConfig.getTitleFlag());
				group.setTitle(instanceConfig.getTitle());
			}


			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				NavigatorInstanceConfig config = new NavigatorInstanceConfig();
				config.setBackgroundColor(instanceConfig.getBackgroundColor());
				if(Style.fromCode(group.getStyle()) == Style.GALLERY){
					config.setCssStyleFlag(CssStyleFlagType.YES.getCode());
				}
				if(null == instanceConfig.getPadding()){
					instanceConfig.setPadding(1);
				}

				if(null == instanceConfig.getMargin()){
					instanceConfig.setMargin(1);
				}

				if(null == instanceConfig.getBackgroundColor()){
					instanceConfig.setBackgroundColor("#FFFFFF");
				}
				config.setPaddingBottom(instanceConfig.getPadding());
				config.setPaddingLeft(instanceConfig.getPadding());
				config.setPaddingRight(instanceConfig.getPadding());
				config.setPaddingTop(instanceConfig.getPadding());
				config.setColumnSpacing(instanceConfig.getMargin());
				config.setLineSpacing(instanceConfig.getMargin());
				config.setItemGroup(itemGroup.getName());
				config.setAllOrMoreFlag(instanceConfig.getAllOrMoreFlag());
				config.setAllOrMoreIconUri(instanceConfig.getAllOrMoreIconUri());
				config.setAllOrMoreLabel(instanceConfig.getAllOrMoreLabel());
				config.setAllOrMoreType(instanceConfig.getAllOrMoreType());

				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.BANNERS){
				BannersInstanceConfig config = new BannersInstanceConfig();
				if (!StringUtils.isEmpty(itemGroup.getInstanceConfig())) {
				    config = (BannersInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), BannersInstanceConfig.class);
                }
				if(StringUtils.isEmpty(group.getStyle())){
					group.setStyle(BannerStyle.DEFAULT.getCode());
				}
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS){
				NewsInstanceConfig config  =  new NewsInstanceConfig();
				ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
				if(moduleApp != null && moduleApp.getInstanceConfig() != null){
					config = (NewsInstanceConfig)StringHelper.fromJsonString(moduleApp.getInstanceConfig(), NewsInstanceConfig.class);
				}else if(itemGroup.getInstanceConfig() != null){
					config = (NewsInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), NewsInstanceConfig.class);
				}
				config.setItemGroup(itemGroup.getName());
				config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());

				ServiceModuleApp serviceModuleApp = getServiceModuleApp(itemGroup);
				if(serviceModuleApp != null){
					config.setModuleId(serviceModuleApp.getModuleId());
					config.setAppId(serviceModuleApp.getOriginId());
				}

				group.setInstanceConfig(config);

			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS_FLASH){

				NewsFlashInstanceConfig config = new NewsFlashInstanceConfig();
				ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
				if(moduleApp != null && moduleApp.getInstanceConfig() != null){
					config = (NewsFlashInstanceConfig)StringHelper.fromJsonString(moduleApp.getInstanceConfig(), NewsFlashInstanceConfig.class);
				}else  if(itemGroup.getInstanceConfig() != null){
					config = (NewsFlashInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), NewsFlashInstanceConfig.class);
				}
				config.setItemGroup(itemGroup.getName());
				config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());
				config.setNewsSize(instanceConfig.getNewsSize());

				ServiceModuleApp serviceModuleApp = getServiceModuleApp(itemGroup);
				if(serviceModuleApp != null){
					config.setModuleId(serviceModuleApp.getModuleId());
					config.setAppId(serviceModuleApp.getOriginId());
				}

				group.setInstanceConfig(config);

			}else if(Widget.fromCode(group.getWidget()) == Widget.BULLETINS){
				BulletinsInstanceConfig config = (BulletinsInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), BulletinsInstanceConfig.class);
				config.setItemGroup(itemGroup.getName());
				if(!StringUtils.isEmpty(config.getIconUri())){
					String url = contentServerService.parseSharedUri(config.getIconUri());
					config.setIconUrl(url);
				}

				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.OPPUSH){
				OPPushInstanceConfig config = new OPPushInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				config.setNewsSize(instanceConfig.getNewsSize());
				config.setDescriptionHeight(0);
				config.setSubjectHeight(0);
				config.setEntityCount(0);
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.ACTIVITY
						|| OPPushWidgetStyle.LIST_VIEW.equals(OPPushWidgetStyle.fromCode(itemGroup.getStyle()))
						|| OPPushWidgetStyle.fromCode(itemGroup.getStyle()) == null){
					//客户端居然是依赖名字判断的 * 1
					itemGroup.setName("OPPushActivity");
				}
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.SERVICE_ALLIANCE
						|| OPPushWidgetStyle.LARGE_IMAGE_LIST_VIEW.equals(OPPushWidgetStyle.fromCode(itemGroup.getStyle()))){
					//客户端居然是依赖名字判断的 * 2
					itemGroup.setName("Gallery");
				}
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.BIZ
						|| OPPushWidgetStyle.HORIZONTAL_SCROLL_VIEW.equals(OPPushWidgetStyle.fromCode(itemGroup.getStyle()))){
					//客户端居然是依赖名字判断的 * 3
					itemGroup.setName("OPPushBiz");
				}
				publishOPPushItem(itemGroup, versionId, layout.getLocation(), publishType);
				config.setItemGroup(itemGroup.getName());
//				group.setInstanceConfig(StringHelper.toJsonString(config));

				ServiceModuleApp serviceModuleApp = getServiceModuleApp(itemGroup);
				if(serviceModuleApp != null){
					config.setModuleId(serviceModuleApp.getModuleId());
					config.setAppId(serviceModuleApp.getOriginId());
				}

				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.TAB){
				TabInstanceConfig config = new TabInstanceConfig();
				publishTabItem(itemGroup, versionId, layout.getLocation(), publishType);
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.CARDEXTENSION){
				CardExtension config = new CardExtension();

				if(instanceConfig.getAppOriginId() != null){

					List<Long> appOriginIds = new ArrayList<>();
					appOriginIds.add(instanceConfig.getAppOriginId());
					List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsByOriginIds(versionId, appOriginIds);

					if(apps != null && apps.size() > 0){
						ServiceModuleApp app = apps.get(0);
						config.setModuleId(app.getModuleId());
						config.setAppId(app.getOriginId());
						config.setItemGroup(itemGroup.getName());

						ServiceModule module = serviceModuleProvider.findServiceModuleById(app.getModuleId());

						Byte clientHandlerType = 0;
						String host = "";
						if(module != null){
							config.setClientHandlerType(module.getClientHandlerType());
							clientHandlerType = module.getClientHandlerType();
							host = module.getHost();
						}

						String appConfig = launchPadService.refreshActionData(app.getInstanceConfig());
						//填充路由信息
						RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(app.getModuleId(), app.getOriginId(), app.getName(), appConfig, null, null, null, clientHandlerType);
						config.setRouterPath(routerInfo.getPath());
						config.setRouterQuery(routerInfo.getQuery());

						if(StringUtils.isEmpty(host)){
							host  = "default";
						}

						String router = "zl://" + host + config.getRouterPath() + "?" + config.getRouterQuery();
						config.setRouter(router);

					}
				}

				group.setInstanceConfig(config);

			}else if (Widget.fromCode(group.getWidget()) == Widget.VANKESMARTCARD) {
                VanKeSmartCardConfig config = (VanKeSmartCardConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), VanKeSmartCardConfig.class);
                group.setInstanceConfig(config);
			}
			groups.add(group);

			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				// 发布item
				publishItem(itemGroup, versionId, publishType);
			}
		}
		layoutJson.setGroups(groups);
		LaunchPadLayout launchPadLayout = new LaunchPadLayout();


		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(layout.getNamespaceId());
		Long versionCode =  releaseVersion.getDateVersion().longValue() * 10000 + releaseVersion.getBigVersion() * 100 + releaseVersion.getPreviewCount();


//		//正式发布才能更改layout，预览的都用新增的，正式发布时会清除掉当前域空间所有的预览版本数据。
//		if(portalLaunchPadMappings.size() > 0  && PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
//
//			for (PortalLaunchPadMapping mapping: portalLaunchPadMappings) {
//				launchPadLayout = launchPadProvider.findLaunchPadLayoutById(mapping.getLaunchPadContentId());
//				if(null != launchPadLayout){
////					if(launchPadLayout.getVersionCode().toString().indexOf(now) != -1){
////						versionCode = launchPadLayout.getVersionCode() + 1;
////					}
//
//					launchPadLayout.setVersionCode(versionCode.longValue());
//					layoutJson.setVersionCode(versionCode.toString());
//					String json = StringHelper.toJsonString(layoutJson);
//					launchPadLayout.setLayoutJson(json);
//
//					launchPadProvider.updateLaunchPadLayout(launchPadLayout);
//
//				}
//			}
//		}else{


		//上面mapping不靠谱，此处删除所有的同名字的layout  edit by yanjun 201805161519
		if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
			List<LaunchPadLayout> launchPadLayouts = launchPadProvider.getLaunchPadLayouts(layout.getName(), layout.getNamespaceId());
			if(launchPadLayouts != null){
				for (LaunchPadLayout deleteLayout: launchPadLayouts){
					launchPadProvider.deleteLaunchPadLayout(deleteLayout.getId());
				}
			}
		}

		layoutJson.setVersionCode(versionCode.toString());
		String json = StringHelper.toJsonString(layoutJson);
		launchPadLayout.setNamespaceId(layout.getNamespaceId());
		launchPadLayout.setName(layout.getName());
		launchPadLayout.setVersionCode(versionCode.longValue());
		launchPadLayout.setMinVersionCode(0L);
		launchPadLayout.setStatus(LaunchPadLayoutStatus.ACTIVE.getCode());
		launchPadLayout.setScopeCode((byte)0);
		launchPadLayout.setApplyPolicy((byte)0);
		launchPadLayout.setScopeId(0L);
		launchPadLayout.setLayoutJson(json);

		if(PortalPublishType.fromCode(publishType) == PortalPublishType.PREVIEW){
			launchPadLayout.setPreviewPortalVersionId(versionId);
		}

		for (SceneType sceneType: SceneType.values()) {
			if(sceneType == SceneType.DEFAULT ||
					sceneType == SceneType.PARK_TOURIST ||
					sceneType == SceneType.PM_ADMIN){
				launchPadLayout.setSceneType(sceneType.getCode());
				launchPadProvider.createLaunchPadLayout(launchPadLayout);
				PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
				mapping.setContentType(EntityType.PORTAL_LAYOUT.getCode());
				mapping.setPortalContentId(layout.getId());
				mapping.setCreatorUid(user.getId());
				mapping.setLaunchPadContentId(launchPadLayout.getId());
				mapping.setVersionId(layout.getVersionId());
				portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
			}
		}
//		}
	}


//	private void setTitleConfig(LaunchPadLayoutGroup group, ItemGroupInstanceConfig instanceConfig, User user){
//
//		if(group == null || instanceConfig == null || user == null){
//			return;
//		}
//
//		group.setTitleFlag(instanceConfig.getTitleFlag());
//		group.setTitleStyle(instanceConfig.getTitleStyle());
//		group.setSubTitle(instanceConfig.getSubTitle());
//		group.setTitleSize(instanceConfig.getTitleSize());
//		group.setTitleMoreFlag(instanceConfig.getTitleMoreFlag());
//
//		group.setTitle(instanceConfig.getTitle());
//		if(!StringUtils.isEmpty(instanceConfig.getTitleUri())){
//			String url = contentServerService.parserUri(instanceConfig.getTitleUri(), EntityType.USER.getCode(), user.getId());
//			group.setIconUrl(url);
//		}
//	}


	private ServiceModuleApp getServiceModuleApp(PortalItemGroup itemGroup){

		if(itemGroup == null){
			return null;
		}

		ItemGroupInstanceConfig instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);

		if(instanceConfig != null && instanceConfig.getModuleAppId() != null){
			ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());

			if(serviceModuleApp != null){
				return serviceModuleApp;
			}
		}

		if(instanceConfig != null && instanceConfig.getAppOriginId() != null){
			ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(instanceConfig.getAppOriginId());
			if(serviceModuleApp != null){
				return serviceModuleApp;
			}
		}
		return null;
	}


	//暂时没有用到
	private void publishNavigationBar(Long versionId, Byte publishType){

		PortalVersionDTO portalVersion = findPortalVersionById(versionId);


		//正式发布删除旧的index
		if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
			CrossShardListingLocator locator = new CrossShardListingLocator();
			List<LaunchPadIndex> launchPadIndices = launchPadIndexProvider.queryLaunchPadIndexs(locator, 100, (locator1, query) -> {
				query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.NAMESPACE_ID.eq(portalVersion.getNamespaceId()));
				return query;
			});

			if(launchPadIndices != null){
				for (LaunchPadIndex index: launchPadIndices){
					launchPadIndexProvider.deleteLaunchPadIndex(index);
				}
			}
		}


		List<PortalNavigationBar> portalNavigationBars = portalNavigationBarProvider.listPortalNavigationBar(versionId);

		if(portalNavigationBars != null){
			for (PortalNavigationBar na: portalNavigationBars){
				LaunchPadIndex index = ConvertHelper.convert(na, LaunchPadIndex.class);

				if(IndexType.fromCode(na.getType()) == IndexType.CONTAINER){
					Container container = (Container) StringHelper.fromJsonString(index.getConfigJson(), Container.class);
                    if (TopBarStyle.LUCENCY_SHADE.getCode().equals(na.getTopBarStyle())) {
                        container.setLayoutType(LayoutType.NAV_LUCENCY.getCode());
                    }else if (TopBarStyle.OPAQUE_DEFORMATION.getCode().equals(na.getTopBarStyle())) {
                        container.setLayoutType(LayoutType.NAV_OPAQUE.getCode());
                    }else if (TopBarStyle.OPAQUE_STATIC.getCode().equals(na.getTopBarStyle())) {
                        container.setLayoutType(LayoutType.NAV_STATIC.getCode());
                    }else {
                        if (container.getLayoutId() != null) {
                            PortalLayout portalLayout = this.portalLayoutProvider.findPortalLayoutById(container.getLayoutId());
                            if (portalLayout != null) {
                                if (PortalLayoutType.ASSOCIATIONLAYOUT.getCode().equals(portalLayout.getType())) {
                                    container.setLayoutType(LayoutType.TAB.getCode());
                                }
                            }
                        }
                    }
//					List<PortalLaunchPadMapping> portalLaunchPadMappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_LAYOUT.name(), container.getLayoutId(), null);
//
//					if(portalLaunchPadMappings != null && portalLaunchPadMappings.size() > 0){
//						container.setLayoutId(portalLaunchPadMappings.get(0).getLaunchPadContentId());
//					}

					index.setConfigJson(container.toString());

				}else if(IndexType.fromCode(na.getType()) == IndexType.APPLICATION){
					//TODO  转换成router
					Application application = new Application();
                    List<Long> appOriginIds = new ArrayList<>();
                    JSONObject jsonObject = JSON.parseObject(index.getConfigJson());
                    if (jsonObject.get("appOriginId") != null) {
                        appOriginIds.add(Long.valueOf(jsonObject.get("appOriginId").toString()));
                        List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsByOriginIds(versionId, appOriginIds);

                        if(apps != null && apps.size() > 0) {
                            ServiceModuleApp app = apps.get(0);

                            ServiceModule module = serviceModuleProvider.findServiceModuleById(app.getModuleId());

                            Byte clientHandlerType = 0;
                            String host = "";
                            if (module != null) {
                                clientHandlerType = module.getClientHandlerType();
                                host = module.getHost();
                            }

                            String appConfig = launchPadService.refreshActionData(app.getInstanceConfig());
                            //填充路由信息
                            RouterInfo routerInfo = serviceModuleAppService.convertRouterInfo(app.getModuleId(), app.getOriginId(), app.getName(), appConfig, null, null, null, clientHandlerType);

                            if (StringUtils.isEmpty(host)) {
                                host = "default";
                            }

                            String router = "zl://" + host + routerInfo.getPath() + "?" + routerInfo.getQuery();
                            application.setRouter(router);
                            index.setConfigJson(application.toString());
                        }
                    }

				}
                index.setName(na.getLabel());
				index.setCreateTime(new Timestamp(System.currentTimeMillis()));
				index.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				index.setCreatorUid(UserContext.currentUserId());

				launchPadIndexProvider.createLaunchPadIndex(index);
			}

		}




	}


	private void publishTabItem(PortalItemGroup itemGroup, Long versionId, String location, Byte publishType){

		List<LaunchPadItem> items = launchPadProvider.findLaunchPadItem(itemGroup.getNamespaceId(), itemGroup.getName(), location);
		for (LaunchPadItem item: items) {

			//正式发布才能删除items，正式发布时会清除掉当前域空间所有的预览版本数据。
			if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
				launchPadProvider.deleteLaunchPadItem(item.getId());
			}
		}

		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(itemGroup.getId());
		for (PortalItem portalItem: portalItems) {
			LaunchPadItem item = ConvertHelper.convert(portalItem, LaunchPadItem.class);
			item.setItemLabel(portalItem.getLabel());
			item.setItemName(portalItem.getName());
			item.setAppId(AppConstants.APPID_DEFAULT);
			if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
				setItemLayoutActionData(item, portalItem.getActionData());
				item.setAccessControlType(AccessControlType.ALL.getCode());
			}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
				setItemModuleAppActionData(item, portalItem.getActionData());
			}

			item.setGroupId(itemGroup.getId());
			item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
			item.setMinVersion(1L);
			item.setItemGroup(portalItem.getGroupName());
			item.setItemLabel(portalItem.getLabel());
			item.setItemName(portalItem.getName());
			item.setDeleteFlag(DeleteFlagType.YES.getCode());
			item.setScaleType(ScaleType.TAILOR.getCode());
			item.setScopeCode(ScopeType.ALL.getCode());
			item.setScopeId(0L);

			if(PortalPublishType.fromCode(publishType) == PortalPublishType.PREVIEW){
				item.setPreviewPortalVersionId(versionId);
			}

			for (SceneType sceneType: SceneType.values()) {
				if(sceneType == SceneType.DEFAULT ||
						sceneType == SceneType.PARK_TOURIST ||
						sceneType == SceneType.PM_ADMIN){
					item.setSceneType(sceneType.getCode());
					launchPadProvider.createLaunchPadItem(item);
				}
			}
		}
	}

	private void publishOPPushItem(PortalItemGroup itemGroup, Long versionId, String location, Byte publishType){
		List<LaunchPadItem> items = launchPadProvider.findLaunchPadItem(itemGroup.getNamespaceId(), itemGroup.getName(), location);
		for (LaunchPadItem item: items) {

			//正式发布才能删除items，正式发布时会清除掉当前域空间所有的预览版本数据。
			if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
				launchPadProvider.deleteLaunchPadItem(item.getId());
			}
		}
		PortalLayout layout = portalLayoutProvider.findPortalLayoutById(itemGroup.getLayoutId());
		LaunchPadItem item = new LaunchPadItem();
		ItemGroupInstanceConfig instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
		item.setNamespaceId(itemGroup.getNamespaceId());
		item.setAppId(AppConstants.APPID_DEFAULT);
		item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
		item.setMinVersion(1L);
		item.setItemGroup(itemGroup.getName());
		item.setItemLocation(location);
		if(StringUtils.isEmpty(itemGroup.getTitle())){
			//查询的时候itemName为空会报错
			item.setItemLabel("default");
			item.setItemName("default");
		}else {
			item.setItemLabel(itemGroup.getTitle());
			item.setItemName(itemGroup.getTitle());
		}

		item.setDeleteFlag(DeleteFlagType.YES.getCode());
		item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
		item.setScaleType(ScaleType.TAILOR.getCode());
		item.setScopeCode(ScopeType.ALL.getCode());
		item.setScopeId(0L);

		if(PortalPublishType.fromCode(publishType) == PortalPublishType.PREVIEW){
			item.setPreviewPortalVersionId(versionId);
		}

		if(null != layout){
			item.setItemLocation(layout.getLocation());
		}
		if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.BIZ){
			item.setActionType(ActionType.OFFICIAL_URL.getCode());
			UrlActionData data = new UrlActionData();
			data.setUrl(instanceConfig.getBizUrl());
			item.setActionData(StringHelper.toJsonString(data));
			item.setAccessControlType(AccessControlType.ALL.getCode());
		}else if(instanceConfig.getModuleAppId() == null){
			return;
		}else{
			ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
			if(null != moduleApp){
				item.setActionType(moduleApp.getActionType());
				item.setActionData(moduleApp.getInstanceConfig());
			}
			setItemModuleAppActionData(item, instanceConfig.getModuleAppId(), null);
		}
		for (SceneType sceneType: SceneType.values()) {
			if(sceneType == SceneType.DEFAULT ||
					sceneType == SceneType.PARK_TOURIST ||
					sceneType == SceneType.PM_ADMIN){
				item.setSceneType(sceneType.getCode());
				launchPadProvider.createLaunchPadItem(item);
			}
		}
	}

	private void publishItem(PortalItemGroup itemGroup, Long versionId, Byte publishType){
		User user = UserContext.current().getUser();
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(itemGroup.getId(), null);
		Map<Long, String> categoryIdMap = getItemCategoryMap(itemGroup.getNamespaceId(), itemGroup.getId());

		//下面通过mapping的方式不靠谱，导致了很多的没有被删除，然后重复了。直接全干了吧。
		// 例如：在3版本添加了itemA并发布，然后回到2版本再发布时，因为2版本mapping中没有itemA信息，2版本发布时并不会删除广场上的itemA。
		if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){

			if(portalItems != null && portalItems.size() > 0){
				List<LaunchPadItem> oldpadItems = launchPadProvider.findLaunchPadItem(itemGroup.getNamespaceId(), itemGroup.getName(), portalItems.get(0).getItemLocation(), null, null, null);
				if(null != oldpadItems && oldpadItems.size() > 0){
					for (LaunchPadItem item: oldpadItems) {
						launchPadProvider.deleteLaunchPadItem(item.getId());
					}
				}
			}

		}

		//增加“全部更多”item
		addAllAndMoreItem(itemGroup.getId(), portalItems);

		for (PortalItem portalItem: portalItems) {


			List<PortalLaunchPadMapping> mappings = new ArrayList<>();
			if(portalItem.getId() != null){
				mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), null);
			}

			if(null != mappings && mappings.size() > 0){
				for (PortalLaunchPadMapping mapping: mappings) {

					//正式发布才能删除items，预览的都用新增的，正式发布时会清除掉当前域空间所有的预览版本数据。
					if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
						launchPadProvider.deleteLaunchPadItem(mapping.getLaunchPadContentId());
					}

					portalLaunchPadMappingProvider.deletePortalLaunchPadMapping(mapping.getId());
				}
			}

			if(PortalItemStatus.ACTIVE == PortalItemStatus.fromCode(portalItem.getStatus())){
				List<PortalContentScope> contentScopes = new ArrayList<>();

				//“全部更多”类型的item发布范围是所有的范围
				if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
					contentScopes = getAllScope();
				}else {
					contentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
				}

				for (PortalContentScope scope: contentScopes) {
					LaunchPadItem item = ConvertHelper.convert(portalItem, LaunchPadItem.class);
					item.setAppId(AppConstants.APPID_DEFAULT);
					item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
					item.setMinVersion(1L);
					item.setItemGroup(portalItem.getGroupName());
					item.setItemLabel(portalItem.getLabel());
					item.setItemName(portalItem.getName());
					item.setDeleteFlag(DeleteFlagType.YES.getCode());
					item.setScaleType(ScaleType.TAILOR.getCode());

					item.setGroupId(itemGroup.getId());

					//更多全部不进行分类
					if(PortalItemActionType.fromCode(portalItem.getActionType()) != PortalItemActionType.ALLORMORE){
						item.setCategryName(categoryIdMap.get(portalItem.getItemCategoryId()));
					}

					if(PortalScopeType.RESIDENTIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.RESIDENTIAL.getCode());
						item.setSceneType(SceneType.DEFAULT.getCode());
					}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.COMMUNITY.getCode());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.PM.getCode());
						item.setSceneType(SceneType.PM_ADMIN.getCode());
					}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.ORGANIZATION.getCode());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}
					item.setScopeId(scope.getScopeId());
					if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
						setItemLayoutActionData(item, portalItem.getActionData());
						item.setAccessControlType(AccessControlType.ALL.getCode());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
						setItemModuleAppActionData(item, portalItem.getActionData());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ZUOLINURL){
						item.setActionType(ActionType.OFFICIAL_URL.getCode());
						item.setActionData(portalItem.getActionData());
						item.setAccessControlType(AccessControlType.ALL.getCode());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.THIRDURL){
						item.setActionType(ActionType.THIRDPART_URL.getCode());
						item.setActionData(portalItem.getActionData());
						item.setAccessControlType(AccessControlType.ALL.getCode());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){

						AllOrMoreActionData data = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
						if(AllOrMoreType.fromCode(data.getType()) == AllOrMoreType.ALL){
							item.setActionType(ActionType.ALL_BUTTON.getCode());
						}else{
							item.setActionType(ActionType.MORE_BUTTON.getCode());
						}
						MoreActionData actionData = new MoreActionData();
						actionData.setItemLocation(portalItem.getItemLocation());
						actionData.setItemGroup(portalItem.getGroupName());
						item.setActionData(StringHelper.toJsonString(actionData));
						item.setDeleteFlag(DeleteFlagType.NO.getCode());
						item.setAccessControlType(AccessControlType.ALL.getCode());
					}

					//全部更多在后面
					if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
						item.setDefaultOrder(10000);
					}else if(item.getDefaultOrder() != null && item.getDefaultOrder().intValue() >= 10000){
						item.setDefaultOrder(9999);
					}

					if(PortalPublishType.fromCode(publishType) == PortalPublishType.PREVIEW){
						item.setPreviewPortalVersionId(versionId);
					}

					launchPadProvider.createLaunchPadItem(item);

					if(portalItem.getId() != null){
						PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
						mapping.setContentType(EntityType.PORTAL_ITEM.getCode());
						mapping.setPortalContentId(portalItem.getId());
						mapping.setLaunchPadContentId(item.getId());
						mapping.setCreatorUid(user.getId());
						mapping.setVersionId(portalItem.getVersionId());
						portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
					}

				}
			}

		}
	}

	private void addAllAndMoreItem(Long itemGroupId, List<PortalItem> portalItems){


		//兼容老数据，记录原来是否已经存在“全部更多”
		for (PortalItem portalItem: portalItems){
			if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE && PortalItemStatus.ACTIVE == PortalItemStatus.fromCode(portalItem.getStatus())){
				return;
			}
		}


		//新的配置中设置了开启“全部更多”
		PortalItemGroup temp = portalItemGroupProvider.findPortalItemGroupById(itemGroupId);
		NavigatorInstanceConfig config = (NavigatorInstanceConfig)StringHelper.fromJsonString(temp.getInstanceConfig(), NavigatorInstanceConfig.class);
		if(config != null && TrueOrFalseFlag.fromCode(config.getAllOrMoreFlag()) == TrueOrFalseFlag.TRUE && portalItems.size() > 0){

			PortalItem item = ConvertHelper.convert(portalItems.get(0), PortalItem.class);
			item.setId(null);
			item.setLabel(config.getAllOrMoreLabel());
			item.setName(config.getAllOrMoreLabel());
			item.setIconUri(config.getAllOrMoreIconUri());
			item.setActionType(PortalItemActionType.ALLORMORE.getCode());
			item.setActionData("{\"type\":\"" + config.getAllOrMoreType() + "\"}");
			item.setDisplayFlag(TrueOrFalseFlag.TRUE.getCode());
			item.setItemCategoryId(null);
			item.setDefaultOrder(10000);
			item.setStatus(PortalItemStatus.ACTIVE.getCode());

			portalItems.add(item);
		}else {
			//全部更多是关闭的，就将所有的Item设置成可见的
			for(PortalItem portalItem: portalItems){
				portalItem.setDisplayFlag(TrueOrFalseFlag.TRUE.getCode());
			}
		}


	}


	private List<PortalContentScope> getAllScope(){

		List<PortalContentScope> contentScopes = new ArrayList<>();
		PortalContentScope scope1 = new PortalContentScope();
		scope1.setScopeId(0L);
		scope1.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
		contentScopes.add(scope1);

		PortalContentScope scope2 = ConvertHelper.convert(scope1, PortalContentScope.class);
		scope2.setScopeType(PortalScopeType.COMMERCIAL.getCode());
		contentScopes.add(scope2);

		PortalContentScope scope3 = ConvertHelper.convert(scope1, PortalContentScope.class);
		scope3.setScopeType(PortalScopeType.PM.getCode());
		contentScopes.add(scope3);

		PortalContentScope scope4 = ConvertHelper.convert(scope1, PortalContentScope.class);
		scope4.setScopeType(PortalScopeType.ORGANIZATION.getCode());
		contentScopes.add(scope4);

		return contentScopes;

	}

	private void setItemLayoutActionData(LaunchPadItem item, String actionData){
		LayoutActionData data = (LayoutActionData)StringHelper.fromJsonString(actionData, LayoutActionData.class);
		PortalLayout layout = portalLayoutProvider.findPortalLayoutById(data.getLayoutId());
		if(null != layout){

			//tab widget have to use router way
			boolean routerFlag = false;
			List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroup(layout.getId());
			if(portalItemGroups !=null) {
				for (PortalItemGroup itemGroup: portalItemGroups){
					if(Widget.fromCode(itemGroup.getWidget()) == Widget.TAB){
						routerFlag = true;
						break;
					}
				}
			}

			if(routerFlag){
				PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(layout.getNamespaceId());
				Long versionCode =  releaseVersion.getDateVersion().longValue() * 10000 + releaseVersion.getBigVersion() * 100 + releaseVersion.getPreviewCount();
				item.setActionType(ActionType.ROUTER.getCode());
				AssociationActionData associationActionData = new AssociationActionData();
				String url = "zl://association/main?layoutName=" + layout.getName() +
						"&itemLocation=" + layout.getLocation()+
						"&versionCode=" + versionCode.toString()+
						"&displayName=" + layout.getLabel();
				associationActionData.setUrl(url);
				associationActionData.setLayoutName(layout.getName());
				associationActionData.setItemLocation(layout.getLocation());
				associationActionData.setContainerType(ContainerType.TAB.getCode());
				item.setActionData(associationActionData.toString());
			}else {
				item.setActionType(ActionType.NAVIGATION.getCode());
				NavigationActionData navigationActionData = new NavigationActionData();
				navigationActionData.setItemLocation(layout.getLocation());
				navigationActionData.setLayoutName(layout.getName());
				navigationActionData.setTitle(layout.getLabel());
				navigationActionData.setContainerType(ContainerType.NAVIGATOR.getCode());
				item.setActionData(StringHelper.toJsonString(navigationActionData));
			}

		}else{
			LOGGER.error("Unable to find the portal layout.id = {}, actionData = {}", data.getLayoutId(), actionData);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portal layout.");
		}
	}

	private void setItemModuleAppActionData(LaunchPadItem item, String actionData){
		ModuleAppActionData data = (ModuleAppActionData)StringHelper.fromJsonString(actionData, ModuleAppActionData.class);
		setItemModuleAppActionData(item, data.getModuleAppId(), data.getModuleEntryId());
	}

	private void setItemModuleAppActionData(LaunchPadItem item, Long moduleAppId, Long moduleEntryId){
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(moduleAppId);
		if(null != moduleApp){
			item.setAppId(moduleApp.getOriginId());

			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
			item.setActionType(moduleApp.getActionType());
			item.setAccessControlType(moduleApp.getAccessControlType());
			if(null != handler){
				//一开始发布的时候就已经发布过应用了。
//				String instanceConfig = handler.publish(moduleApp.getNamespaceId(), moduleApp.getInstanceConfig(), item.getItemLabel());
//				moduleApp.setInstanceConfig(instanceConfig);
//				serviceModuleAppProvider.updateServiceModuleApp(moduleApp);

				HandlerGetItemActionDataCommand handlerCmd = new HandlerGetItemActionDataCommand();
				handlerCmd.setAppOriginId(moduleApp.getOriginId());
				handlerCmd.setAppId(moduleApp.getId());
				handlerCmd.setModuleEntryId(moduleEntryId);
				item.setActionData(handler.getItemActionData(moduleApp.getNamespaceId(), moduleApp.getInstanceConfig(), handlerCmd));
			}else{
				item.setActionData(moduleApp.getInstanceConfig());
			}
		}
	}

	private String setItemModuleAppActionData(String name, Long moduleAppId){
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(moduleAppId);
		if(null != moduleApp){
			return moduleApp.getInstanceConfig();

			//一开始发布的时候就已经发布过应用了。
//			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
//			if(null != handler){
//				String instanceConfig = handler.publish(moduleApp.getNamespaceId(), moduleApp.getInstanceConfig(), name);
//				moduleApp.setInstanceConfig(instanceConfig);
//				serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
//				return instanceConfig;
//			}
		}
		return null;
	}

	public void publishItemCategory(Integer namespaceId, Long versionId, Byte publishType){
		User user = UserContext.current().getUser();
		List<PortalItem> allItems = getItemAllOrMore(namespaceId, null, AllOrMoreType.ALL, versionId);

		List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroupByWidgetAndStyle(namespaceId, versionId, Widget.NAVIGATOR.getCode(), Style.TAB.getCode());


		//Tab类型没有全部更多，此处生成一个
		for (PortalItemGroup portalItemGroup: portalItemGroups){
			PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(portalItemGroup.getLayoutId());
			if(portalLayout == null){
				continue;
			}

			PortalItem portalItem = new  PortalItem();
			portalItem.setItemLocation(portalLayout.getLocation());
			portalItem.setGroupName(portalItemGroup.getName());
			portalItem.setItemGroupId(portalItemGroup.getId());
			allItems.add(portalItem);
		}


		//查找开启“全部”的itemGroup
		List<PortalItemGroup> allPortalItemGroups = portalItemGroupProvider.listPortalItemGroupByVersion(namespaceId, versionId);
		if(allPortalItemGroups != null){
			for(PortalItemGroup group: allPortalItemGroups){

				ItemGroupInstanceConfig instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(group.getInstanceConfig(), ItemGroupInstanceConfig.class);
				if(instanceConfig != null && TrueOrFalseFlag.fromCode(instanceConfig.getAllOrMoreFlag()) == TrueOrFalseFlag.TRUE && AllOrMoreType.fromCode(instanceConfig.getAllOrMoreType()) == AllOrMoreType.ALL){

					//配置了"全部"，但是原来没有“全部”类型item的ItemGroup需要加上
					boolean exitFlag = false;
					for(PortalItem item: allItems){
						if(group.getId().equals(item.getItemGroupId())){
							exitFlag = true;
							break;
						}
					}

					if(!exitFlag){

						PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(group.getLayoutId());
						PortalItem portalItem = new  PortalItem();
						portalItem.setItemLocation(portalLayout.getLocation());
						portalItem.setGroupName(group.getName());
						portalItem.setItemGroupId(group.getId());
						allItems.add(portalItem);
					}
				}

			}
		}



		for (PortalItem item: allItems) {

			//下面通过mapping的方式不靠谱，导致了很多的没有被删除，然后重复了。直接这个组的全干掉。
			if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
				List<ItemServiceCategry> oldCategorys = launchPadProvider.listItemServiceCategries(namespaceId, item.getItemLocation(), item.getGroupName());
				if(null != oldCategorys && oldCategorys.size() > 0){
					for (ItemServiceCategry oldCategry: oldCategorys) {
						launchPadProvider.deleteItemServiceCategryById(oldCategry.getId());
					}
				}
			}

			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(item.getActionData(), AllOrMoreActionData.class);
			List<PortalItemCategory> categorys = portalItemCategoryProvider.listPortalItemCategory(namespaceId, item.getItemGroupId());
			for (PortalItemCategory category: categorys) {
				List<PortalLaunchPadMapping> mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM_CATEGORY.getCode(), category.getId(), null);
				if(null != mappings && mappings.size() > 0){
					for (PortalLaunchPadMapping mapping: mappings) {

						//正式发布才能删除Categry，预览的都用新增的，正式发布时会清除掉当前域空间所有的预览版本数据。
						if(PortalPublishType.fromCode(publishType) == PortalPublishType.RELEASE){
							launchPadProvider.deleteItemServiceCategryById(mapping.getLaunchPadContentId());
						}

						portalLaunchPadMappingProvider.deletePortalLaunchPadMapping(mapping.getId());
					}
				}

				if(PortalItemCategoryStatus.fromCode(category.getStatus()) == PortalItemCategoryStatus.ACTIVE){
					List<PortalContentScope> contentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), category.getId());
					for (PortalContentScope scope: contentScopes) {
						ItemServiceCategry itemCategory = ConvertHelper.convert(category, ItemServiceCategry.class);
						if(PortalScopeType.RESIDENTIAL == PortalScopeType.fromCode(scope.getScopeType())){
							itemCategory.setSceneType(SceneType.DEFAULT.getCode());
							itemCategory.setScopeCode(ScopeType.RESIDENTIAL.getCode());
						}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
							itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
							itemCategory.setScopeCode(ScopeType.COMMUNITY.getCode());
						}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
							itemCategory.setSceneType(SceneType.PM_ADMIN.getCode());
							itemCategory.setScopeCode(ScopeType.PM.getCode());
						}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
							itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
							itemCategory.setScopeCode(ScopeType.ORGANIZATION.getCode());
						}
						itemCategory.setScopeId(scope.getScopeId());
						itemCategory.setStatus(ItemServiceCategryStatus.ACTIVE.getCode());
						itemCategory.setCreatorUid(user.getId());

						if(StringUtils.isEmpty(category.getIconUri()) && null != actionData){
							itemCategory.setIconUri(actionData.getDefUri());
						}

						if(AlignType.CENTER == AlignType.fromCode(category.getAlign()))
							itemCategory.setAlign(ItemServiceCategryAlign.CENTER.getCode());
						else if(AlignType.LEFT == AlignType.fromCode(category.getAlign()))
							itemCategory.setAlign(ItemServiceCategryAlign.LEFT.getCode());

						if(null == ItemServiceCategryAlign.fromCode(itemCategory.getAlign())  && null != actionData){
							if(AlignType.CENTER == AlignType.fromCode(actionData.getAlign()))
								itemCategory.setAlign(ItemServiceCategryAlign.CENTER.getCode());
							else if(AlignType.LEFT == AlignType.fromCode(actionData.getAlign()))
								itemCategory.setAlign(ItemServiceCategryAlign.LEFT.getCode());
						}
						itemCategory.setOrder(category.getDefaultOrder());
						itemCategory.setItemLocation(item.getItemLocation());
						itemCategory.setItemGroup(item.getGroupName());

						if(PortalPublishType.fromCode(publishType) == PortalPublishType.PREVIEW){
							itemCategory.setPreviewPortalVersionId(versionId);
						}

						itemCategory.setGroupId(category.getItemGroupId());

						launchPadProvider.createItemServiceCategry(itemCategory);

						PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
						mapping.setContentType(EntityType.PORTAL_ITEM_CATEGORY.getCode());
						mapping.setPortalContentId(category.getId());
						mapping.setLaunchPadContentId(itemCategory.getId());
						mapping.setCreatorUid(user.getId());
						mapping.setVersionId(category.getVersionId());
						portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
					}
				}
			}
		}
	}

	private Map<Long, String> getItemCategoryMap(Integer namespaceId, Long itemGroupId){
		Map<Long, String> categoryMap = new HashMap<>();
		List<PortalItemCategory> categories = portalItemCategoryProvider.listPortalItemCategory(namespaceId, itemGroupId);
		for (PortalItemCategory  category: categories) {
			categoryMap.put(category.getId(), category.getName());
		}
		return categoryMap;
	}


	@Override
	public void syncLaunchPadData(SyncLaunchPadDataCommand cmd){

		ValidatorUtil.validate(cmd);

		// 涉及的表比较多，经常会出现id冲突，sb事务又经常是有问题无法回滚。无奈之举，在此同步一次Sequence。
		// 大师改好事务之后，遇到有缘人再来此删掉下面这行代码

		// 大师好样的，事务好了。
		//sequenceService.syncSequence();

		//同步和发布的时候不用预览账号
		UserContext.current().setPreviewPortalVersionId(null);

		List<Tuple<String, String>> list = new ArrayList<>();

		if(StringUtils.isEmpty(cmd.getLocation()) || StringUtils.isEmpty(cmd.getName())){
			list.add(new Tuple<>("/home", "ServiceMarketLayout"));
			list.add(new Tuple<>("/secondhome", "SecondServiceMarketLayout"));
			list.add(new Tuple<>("/association", "AssociationLayout"));
		}else {
			list.add(new Tuple<>(cmd.getLocation(), cmd.getName()));
		}

		List<NamespaceInfoDTO> namespaceInfoDTOS = new ArrayList<>();
		if(cmd.getNamespaceId() != null){
			NamespaceInfoDTO newdto = new NamespaceInfoDTO();
			newdto.setId(cmd.getNamespaceId());
			namespaceInfoDTOS.add(newdto);
		}else {
			namespaceInfoDTOS = namespacesService.listNamespace();
		}

		for(NamespaceInfoDTO dto: namespaceInfoDTOS){
			//一个个域空间同步
			try {

				LOGGER.info("syncLaunchPadData namespaceId={}  start", dto.getId());
				dbProvider.execute((status -> {

					//清理预览版本的用户信息
					portalVersionUserProvider.deletePortalVersionUsers(dto.getId());

					//获取一个版本号
					PortalVersion portalVersion = createBigPortalVersion(dto.getId());

					//同步是基于上个版本的，先copy一份moduleApp，同步item的时候增量方式
					if(portalVersion.getParentId() != null){
						copyServiceModuleAppToNewVersion(dto.getId(), portalVersion.getParentId(), portalVersion.getId());
					}

					for (Tuple<String, String> t: list) {
						syncLayout(dto.getId(), t.first(), t.second(), portalVersion);
					}


					//同步完之后fork一个小版本，用于编辑
					copyPortalToNewMinorVersion(dto.getId(), portalVersion.getId());

					//更新当前版本为正式版本
					updateReleaseVersion(dto.getId(), portalVersion.getId());

					//清理很的老版本
					cleanOldVersion(dto.getId());

					return null;
				}));
				LOGGER.info("syncLaunchPadData namespaceId={}  end", dto.getId());

			} catch (Exception e) {
				LOGGER.error("syncLaunchPadData namespaceId=" + dto.getId() + " exception", e);
			}
		}

	}


	public void updateReleaseVersion(Integer namespaceId, Long newReleaseVersionId){
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		if(releaseVersion != null){
			releaseVersion.setStatus(null);
			portalVersionProvider.updatePortalVersion(releaseVersion);
		}
		PortalVersion newReleaseVersion = portalVersionProvider.findPortalVersionById(newReleaseVersionId);
		newReleaseVersion.setStatus(PortalVersionStatus.RELEASE.getCode());
		portalVersionProvider.updatePortalVersion(newReleaseVersion);
	}


	public void updatePreviewVersion(Integer namespaceId, Long newPreviewVersionId){
		PortalVersion previewVersion = portalVersionProvider.findPreviewVersion(namespaceId);
		if(previewVersion != null){
			previewVersion.setStatus(null);
			portalVersionProvider.updatePortalVersion(previewVersion);
		}
		PortalVersion newPreviewVersion = portalVersionProvider.findPortalVersionById(newPreviewVersionId);
		newPreviewVersion.setStatus(PortalVersionStatus.PREVIEW.getCode());
		portalVersionProvider.updatePortalVersion(newPreviewVersion);
	}

	/**
	 * 查询版本信息
	 * @param versionId
	 * @return
	 */

	@Override
	public PortalVersionDTO findPortalVersionById(Long versionId){

		PortalVersion version = portalVersionProvider.findPortalVersionById(versionId);
		return ConvertHelper.convert(version, PortalVersionDTO.class);
	}

	/**
	 * 基于已发布的最新的版本产生一个新大的版本号，一般用于同步和发布
	 * @param namespaceId
	 * @return
	 */
	private PortalVersion createBigPortalVersion(Integer namespaceId){

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		PortalVersion newVersion = new PortalVersion();
		newVersion.setNamespaceId(namespaceId);

		newVersion.setMinorVersion(0);
		newVersion.setCreateTime(new Timestamp(System.currentTimeMillis()));
		newVersion.setSyncTime(new Timestamp(System.currentTimeMillis()));


		Calendar calendar = Calendar.getInstance();
		Integer nowDateVersion = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DATE);
		newVersion.setDateVersion(nowDateVersion);

		//如果不存在旧版本则设置大版本为1，旧版本存在并且日期和当期前日期不一致则大版本往上加，否则是新日期设置大版本为1
		if (releaseVersion == null){
			newVersion.setBigVersion(1);
		} else if(releaseVersion.getDateVersion().intValue() == nowDateVersion.intValue()){
			newVersion.setBigVersion(releaseVersion.getBigVersion() + 1);
			newVersion.setParentId(releaseVersion.getId());
		}else {
			newVersion.setBigVersion(1);
			newVersion.setParentId(releaseVersion.getId());
		}

		portalVersionProvider.createPortalVersion(newVersion);
		return newVersion;
	}

	/**
	 * 基于大版本产生一个小版本
	 * @param versionId
	 * @return
	 */
	private PortalVersion createMinorPortalVersion(Long versionId){

		PortalVersion oldVersion = portalVersionProvider.findPortalVersionById(versionId);
		PortalVersion newVersion = new PortalVersion();
		newVersion.setNamespaceId(oldVersion.getNamespaceId());
		newVersion.setDateVersion(oldVersion.getDateVersion());
		newVersion.setParentId(versionId);
		newVersion.setBigVersion(oldVersion.getBigVersion());
		newVersion.setMinorVersion(1);
		newVersion.setPreviewCount(0);
		newVersion.setCreateTime(new Timestamp(System.currentTimeMillis()));
		newVersion.setSyncTime(new Timestamp(System.currentTimeMillis()));

		portalVersionProvider.createPortalVersion(newVersion);
		return newVersion;
	}


	private Long copyPortalToNewMinorVersion(Integer namespaceId, Long oldVersionId){

		//1.获取一个新版本
		PortalVersion newPortalVersion = createMinorPortalVersion(oldVersionId);

		//2.复制一份app
		copyServiceModuleAppToNewVersion(namespaceId, oldVersionId, newPortalVersion.getId());

		//3.复制PortalLayout、PortalItemGroup、PortalItemCategory、PortalItem
		copyPortalLayoutToNewVersion(namespaceId, oldVersionId, newPortalVersion.getId());


		return newPortalVersion.getId();
	}

	private void copyServiceModuleAppToNewVersion(Integer namespaceId, Long oldVersionId, Long newVersionId){
		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, oldVersionId, null);

		if(serviceModuleApps == null || serviceModuleApps.size() == 0){
			return;
		}

		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());

		for (ServiceModuleApp app: serviceModuleApps){
			app.setId(null);
			app.setVersionId(newVersionId);
			app.setCreateTime(createTimestamp);
			app.setUpdateTime(createTimestamp);
		}
		serviceModuleAppProvider.createServiceModuleApps(serviceModuleApps);

	}

	private void copyPortalLayoutToNewVersion(Integer namespaceId, Long oldVersionId, Long newVersionId){


		List<PortalLayout> portalLayouts = portalLayoutProvider.listPortalLayoutByVersion(namespaceId, oldVersionId);
		if(portalLayouts == null || portalLayouts.size() == 0){
			return;
		}

		List<PortalContentScope> portalContentScopes = new ArrayList<>();
		List<PortalLaunchPadMapping> portalLaunchPadMappings = new ArrayList<>();
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());

		Map<Long, Long> layoutIdMap = new HashMap<>();
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalLayouts.class), (long)portalLayouts.size() + 1);
		for(PortalLayout layout: portalLayouts){
			id++;
			List<PortalContentScope> newPortalContentScope = getNewPortalContentScope(EntityType.PORTAL_LAYOUT.getCode(), layout.getId(), id);
			portalContentScopes.addAll(newPortalContentScope);
			List<PortalLaunchPadMapping> newPortalLaunchPadMapping = getNewPortalLaunchPadMapping(EntityType.PORTAL_LAYOUT.getCode(), layout.getId(), id);
			portalLaunchPadMappings.addAll(newPortalLaunchPadMapping);
            //发布后，主页签保存的LayoutId就消失了，现在用map来保存，然后在更新主页签的layouytId
            // add by yanlong.liang 20181219
			layoutIdMap.put(layout.getId(), id);


			copyPortalItemGroupToNewVersion(namespaceId, layout.getId(), id, newVersionId);

			layout.setId(id);
			layout.setVersionId(newVersionId);
			layout.setCreateTime(createTimestamp);
			layout.setUpdateTime(createTimestamp);
		}

		portalLayoutProvider.createPortalLayouts(portalLayouts);

		//9、生成ContentScopes
		for (PortalContentScope scope: portalContentScopes){
			scope.setVersionId(newVersionId);
		}
		portalContentScopeProvider.createPortalContentScopes(portalContentScopes);

		//10、生成LaunchPadMappings
		for (PortalLaunchPadMapping mapping: portalLaunchPadMappings){
			mapping.setVersionId(newVersionId);
		}
		portalLaunchPadMappingProvider.createPortalLaunchPadMappings(portalLaunchPadMappings);

		//此时发生了一个很伤心的事情，指向门户的item的actionData的内容中指定的layoutId还是旧的，指向应用的也是旧的。
		updateItemActionData(namespaceId, newVersionId);

		// 此时又发生了一个很伤心的事情，例如运营板块也包含了一些应用id，也是旧的
		updateItemGroupConfig(namespaceId, newVersionId);

		//生成新的主页签 add by yanlong.liang 20181219
        List<PortalNavigationBar> portalNavigationBars = portalNavigationBarProvider.listPortalNavigationBar(oldVersionId);
        if (!CollectionUtils.isEmpty(portalNavigationBars)) {
            for (PortalNavigationBar portalNavigationBar : portalNavigationBars) {

                if(IndexType.fromCode(portalNavigationBar.getType()) == IndexType.CONTAINER) {
                    Container container = (Container) StringHelper.fromJsonString(portalNavigationBar.getConfigJson(), Container.class);
                    if (container != null && container.getLayoutId() != null) {
                        Long newLayoutId = layoutIdMap.get(container.getLayoutId());
                        container.setLayoutId(newLayoutId);
                        portalNavigationBar.setConfigJson(container.toString());
                        this.portalNavigationBarProvider.updatePortalNavigationBar(portalNavigationBar);
                    }

                }
                PortalNavigationBar newPortalNavigationBar = ConvertHelper.convert(portalNavigationBar, PortalNavigationBar.class);
                newPortalNavigationBar.setVersionId(newVersionId);
                newPortalNavigationBar.setId(null);
                this.portalNavigationBarProvider.createPortalNavigationBar(newPortalNavigationBar);
            }
        }

	}

	private void updateItemActionData(Integer namespaceId, Long newVersionId){

		List<PortalLayout> newPortalLayouts = portalLayoutProvider.listPortalLayoutByVersion(namespaceId, newVersionId);
		List<ServiceModuleApp> newServiceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, newVersionId, null);
		List<PortalItem> portalItems = portalItemProvider.listPortalItemsByVersionId(newVersionId);
		if (portalItems != null || portalItems.size() > 0){
			for (PortalItem item: portalItems){

				ItemActionData actionData = (ItemActionData) StringHelper.fromJsonString(item.getActionData(), ItemActionData.class);
				if(actionData == null){
					continue;
				}

				//如果是指向layout，则更新layout的Id
				if(PortalItemActionType.fromCode(item.getActionType()) == PortalItemActionType.LAYOUT && actionData.getLayoutId() != null){

					PortalLayout oldPortalLayout = portalLayoutProvider.findPortalLayoutById(actionData.getLayoutId());
					if(oldPortalLayout == null){
						continue;
					}

					for(PortalLayout portalLayout: newPortalLayouts){
						if(portalLayout.getLocation().equals(oldPortalLayout.getLocation()) && portalLayout.getName().equals(oldPortalLayout.getName())){
							actionData.setLayoutId(portalLayout.getId());
							item.setActionData(actionData.toString());
							portalItemProvider.updatePortalItem(item);
							break;
						}
					}
				}else if(PortalItemActionType.fromCode(item.getActionType()) == PortalItemActionType.MODULEAPP && actionData.getModuleAppId() != null){
					//如果是指向serviceModuleApp，则更新serviceModuleApp的Id
					ServiceModuleApp oldServiceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(actionData.getModuleAppId());
					if(oldServiceModuleApp == null){
						continue;
					}

					for(ServiceModuleApp serviceModuleApp: newServiceModuleApps){
						if(serviceModuleApp.getOriginId().longValue() == oldServiceModuleApp.getOriginId().longValue()){
							actionData.setModuleAppId(serviceModuleApp.getId());
							item.setActionData(actionData.toString());
							portalItemProvider.updatePortalItem(item);
							break;
						}
					}
				}

			}
		}

	}

	private void updateItemGroupConfig(Integer namespaceId, Long newVersionId){

		List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroupByVersion(namespaceId, newVersionId);
		List<ServiceModuleApp> newServiceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, newVersionId, null);
		if (portalItemGroups != null || portalItemGroups.size() > 0){
			for (PortalItemGroup itemGroup: portalItemGroups){

				ItemGroupInstanceConfig config = (ItemGroupInstanceConfig) StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
				if(config == null || config.getModuleAppId() == null){
					continue;
				}

				ServiceModuleApp oldServiceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(config.getModuleAppId());
				if(oldServiceModuleApp == null){
					continue;
				}

				for(ServiceModuleApp serviceModuleApp: newServiceModuleApps){
					if(serviceModuleApp.getOriginId().longValue() == oldServiceModuleApp.getOriginId().longValue()){
						config.setModuleAppId(serviceModuleApp.getId());
						itemGroup.setInstanceConfig(config.toString());
						portalItemGroupProvider.updatePortalItemGroup(itemGroup);
						break;
					}
				}

			}
		}

	}


	private void copyPortalItemGroupToNewVersion(Integer namespaceId, Long oldLayoutId, Long newLayoutId, Long newVersionId) {

		List<PortalItemGroup> portalitemGroups = portalItemGroupProvider.listPortalItemGroup(oldLayoutId);
		if(portalitemGroups == null || portalitemGroups.size() == 0 ){
			return;
		}

		List<PortalContentScope> portalContentScopes = new ArrayList<>();
		List<PortalLaunchPadMapping> portalLaunchPadMappings = new ArrayList<>();
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());

		if (portalitemGroups != null && portalitemGroups.size() > 0) {
			Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemGroups.class), (long) portalitemGroups.size() + 1);
			for (PortalItemGroup itemGroup : portalitemGroups) {
				id++;
				List<PortalContentScope> newPortalContentScope = getNewPortalContentScope(EntityType.PORTAL_ITEM_GROUP.getCode(), itemGroup.getId(), id);
				portalContentScopes.addAll(newPortalContentScope);
				List<PortalLaunchPadMapping> newPortalLaunchPadMapping = getNewPortalLaunchPadMapping(EntityType.PORTAL_ITEM_GROUP.getCode(), itemGroup.getId(), id);
				portalLaunchPadMappings.addAll(newPortalLaunchPadMapping);

				//复制category
				boolean categoryFlag = copyPortalItemCategoryToNewVersion(namespaceId, itemGroup.getId(), id, newVersionId);
				if(!categoryFlag){
					//复制item
					copyPortalItemToNewVersion(namespaceId, itemGroup.getId(), id, null, null, newVersionId);
				}

				itemGroup.setId(id);
				itemGroup.setLayoutId(newLayoutId);
				itemGroup.setVersionId(newVersionId);
				itemGroup.setCreateTime(createTimestamp);
				itemGroup.setUpdateTime(createTimestamp);

			}
			portalItemGroupProvider.createPortalItemGroups(portalitemGroups);
		}

		for (PortalContentScope scope: portalContentScopes){
			scope.setVersionId(newVersionId);
		}
		portalContentScopeProvider.createPortalContentScopes(portalContentScopes);

		for (PortalLaunchPadMapping mapping: portalLaunchPadMappings){
			mapping.setVersionId(newVersionId);
		}
		portalLaunchPadMappingProvider.createPortalLaunchPadMappings(portalLaunchPadMappings);
	}


	private boolean copyPortalItemCategoryToNewVersion(Integer namespaceId, Long oldItemGroupId, Long newItemGroupId, Long newVersionId) {

		List<PortalItemCategory> portalItemCategories = portalItemCategoryProvider.listPortalItemCategory(namespaceId, oldItemGroupId);
		if(portalItemCategories ==  null || portalItemCategories.size() == 0 ){
			return false;
		}


		List<PortalContentScope> portalContentScopes = new ArrayList<>();
		List<PortalLaunchPadMapping> portalLaunchPadMappings = new ArrayList<>();
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
		//7、复制一份item_categories
		if(portalItemCategories != null && portalItemCategories.size()> 0){
			Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemCategories.class), (long)portalItemCategories.size() + 1);
			for(PortalItemCategory itemCategory: portalItemCategories){
				id++;
				List<PortalContentScope> newPortalContentScope = getNewPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), itemCategory.getId(), id);
				portalContentScopes.addAll(newPortalContentScope);
				List<PortalLaunchPadMapping> newPortalLaunchPadMapping = getNewPortalLaunchPadMapping(EntityType.PORTAL_ITEM_CATEGORY.getCode(), itemCategory.getId(), id);
				portalLaunchPadMappings.addAll(newPortalLaunchPadMapping);

				//复制item
				copyPortalItemToNewVersion(namespaceId, oldItemGroupId, newItemGroupId, itemCategory.getId(), id, newVersionId);

				itemCategory.setId(id);
				itemCategory.setItemGroupId(newItemGroupId);
				itemCategory.setVersionId(newVersionId);
				itemCategory.setCreateTime(createTimestamp);
				itemCategory.setUpdateTime(createTimestamp);
			}
			//复制item，未分组
			copyPortalItemToNewVersion(namespaceId, oldItemGroupId, newItemGroupId, 0L, null, newVersionId);

			portalItemCategoryProvider.createPortalItemCategories(portalItemCategories);
		}

		for (PortalContentScope scope: portalContentScopes){
			scope.setVersionId(newVersionId);
		}
		portalContentScopeProvider.createPortalContentScopes(portalContentScopes);

		for (PortalLaunchPadMapping mapping: portalLaunchPadMappings){
			mapping.setVersionId(newVersionId);
		}
		portalLaunchPadMappingProvider.createPortalLaunchPadMappings(portalLaunchPadMappings);
		return true;
	}


	private void copyPortalItemToNewVersion(Integer namespaceId, Long oldItemGroupId, Long newItemGroupId, Long oldItemCategoryId, Long newItemCategoryId, Long newVersionId) {

		List<PortalItem> portalItems = portalItemProvider.listPortalItems(oldItemCategoryId, oldItemGroupId);
		if(portalItems == null || portalItems.size() == 0 ){
			return;
		}

		List<PortalContentScope> portalContentScopes = new ArrayList<>();
		List<PortalLaunchPadMapping> portalLaunchPadMappings = new ArrayList<>();
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());

		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalItems.class), (long)portalItems.size() + 1);
		for(PortalItem item: portalItems){
			id++;
			List<PortalContentScope> newPortalContentScope = getNewPortalContentScope(EntityType.PORTAL_ITEM.getCode(), item.getId(), id);
			portalContentScopes.addAll(newPortalContentScope);
			List<PortalLaunchPadMapping> newPortalLaunchPadMapping = getNewPortalLaunchPadMapping(EntityType.PORTAL_ITEM.getCode(), item.getId(), id);
			portalLaunchPadMappings.addAll(newPortalLaunchPadMapping);

			item.setId(id);
			item.setItemGroupId(newItemGroupId);
			item.setItemCategoryId(newItemCategoryId);
			item.setVersionId(newVersionId);
			item.setCreateTime(createTimestamp);
			item.setUpdateTime(createTimestamp);
		}
		portalItemProvider.createPortalItems(portalItems);

		for (PortalContentScope scope: portalContentScopes){
			scope.setVersionId(newVersionId);
		}
		portalContentScopeProvider.createPortalContentScopes(portalContentScopes);

		for (PortalLaunchPadMapping mapping: portalLaunchPadMappings){
			mapping.setVersionId(newVersionId);
		}
		portalLaunchPadMappingProvider.createPortalLaunchPadMappings(portalLaunchPadMappings);
	}


	private List<PortalContentScope> getNewPortalContentScope(String contentType, Long contentId, Long newContentId){
		List<PortalContentScope> tempScope = portalContentScopeProvider.listPortalContentScope(contentType, contentId);

		if(tempScope == null){
			return null;
		}

		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
		for (PortalContentScope scope: tempScope){
			scope.setId(null);
			scope.setContentId(newContentId);
			scope.setCreateTime(createTimestamp);
			scope.setUpdateTime(createTimestamp);
		}

		return tempScope;
	}

	private List<PortalLaunchPadMapping> getNewPortalLaunchPadMapping(String contentType, Long contentId, Long newContentId){
		List<PortalLaunchPadMapping> tempMapping = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(contentType, contentId, null);

		if(tempMapping == null){
			return null;
		}

		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
		for (PortalLaunchPadMapping mapping: tempMapping){
			mapping.setId(null);
			mapping.setPortalContentId(newContentId);
			mapping.setCreateTime(createTimestamp);
		}
		return tempMapping;
	}

	private PortalLayout syncLayout(Integer namespaceId, String location, String name, PortalVersion newVersion){
		User user = UserContext.current().getUser();
		List<LaunchPadLayout> padLayouts = launchPadProvider.getLaunchPadLayouts(name, namespaceId);
		// 每次同步开始的时候，先把reflectionServiceModuleApp中对应的数据状态置为无效
		this.serviceModuleProvider.lapseReflectionServiceModuleAppByNamespaceId(namespaceId);

		PortalLayout layout = null;
		if(padLayouts.size() == 0){
			LOGGER.error("Unable to find the lunch pad layout. namespaceId = {}, location = {}, layoutName = {}", namespaceId, location, name);
			layout = new PortalLayout();
			layout.setId(0L);
		}

		for (LaunchPadLayout padLayout: padLayouts) {
			layout = portalLayoutProvider.getPortalLayout(padLayout.getNamespaceId(), name, newVersion.getId());
			if(null == layout){
				layout = ConvertHelper.convert(padLayout, PortalLayout.class);
				LaunchPadLayoutJson layoutJson = (LaunchPadLayoutJson)StringHelper.fromJsonString(padLayout.getLayoutJson(), LaunchPadLayoutJson.class);
				layout.setLabel(layoutJson.getDisplayName());
				layout.setLocation(location);
				layout.setOperatorUid(user.getId());
				layout.setCreatorUid(user.getId());
				layout.setVersionId(newVersion.getId());
				portalLayoutProvider.createPortalLayout(layout);
				List<LaunchPadLayoutGroup> padLayoutGroups = layoutJson.getGroups();
				for (LaunchPadLayoutGroup padLayoutGroup: padLayoutGroups) {
					PortalItemGroup itemGroup = ConvertHelper.convert(padLayoutGroup, PortalItemGroup.class);
					itemGroup.setNamespaceId(layout.getNamespaceId());
					itemGroup.setLayoutId(layout.getId());
					itemGroup.setLabel(padLayoutGroup.getGroupName());
					itemGroup.setStatus(layout.getStatus());
					itemGroup.setCreatorUid(user.getId());
					itemGroup.setOperatorUid(user.getId());
					itemGroup.setVersionId(newVersion.getId());
					if(null != padLayoutGroup.getSeparatorFlag()){
						itemGroup.setSeparatorFlag(padLayoutGroup.getSeparatorFlag().byteValue());
					}

					if(null != padLayoutGroup.getSeparatorHeight()){
						itemGroup.setSeparatorHeight(new BigDecimal(padLayoutGroup.getSeparatorHeight()));
					}
					if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NAVIGATOR){
						NavigatorInstanceConfig instanceConfig = (NavigatorInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NavigatorInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						if(Style.fromCode(padLayoutGroup.getStyle()) == Style.GALLERY){
							config.setPadding(instanceConfig.getPaddingTop());
							config.setMargin(instanceConfig.getLineSpacing());
						}
						if(!StringUtils.isEmpty(padLayoutGroup.getTitle()) || !StringUtils.isEmpty(padLayoutGroup.getIconUrl())){
							config.setTitleFlag(TitleFlag.LEFT.getCode());
							config.setTitle(padLayoutGroup.getTitle());
							//config.setTitleUri(padLayoutGroup.getIconUrl());
						}
						config.setColumnCount(padLayoutGroup.getColumnCount());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
						if(name.equals("ServiceMarketLayout"))
							syncItemCategory(itemGroup.getNamespaceId(),itemGroup.getId(), itemGroup.getName(), newVersion.getId());
						syncItem(itemGroup.getNamespaceId(), location, itemGroup.getName(), itemGroup.getId(), newVersion);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.BANNERS){
						BannersInstanceConfig instanceConfig = (BannersInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), BannersInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.BULLETINS){
						BulletinsInstanceConfig instanceConfig = (BulletinsInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), BulletinsInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.OPPUSH){
						OPPushInstanceConfig instanceConfig = (OPPushInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), OPPushInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						List<LaunchPadItem> padItems = launchPadProvider.listLaunchPadItemsByItemGroup(padLayout.getNamespaceId(), location, instanceConfig.getItemGroup());
						if(padItems.size() > 0){
							config.setTitleFlag(TitleFlag.LEFT.getCode());
							config.setTitle(padItems.get(0).getItemLabel());

							Long moduleId = null;
							if(OPPushWidgetStyle.LIST_VIEW == OPPushWidgetStyle.fromCode(padLayoutGroup.getStyle())){
								moduleId = 10600L;
								itemGroup.setContentType(EntityType.ACTIVITY.getCode());

							}else if(OPPushWidgetStyle.LARGE_IMAGE_LIST_VIEW == OPPushWidgetStyle.fromCode(padLayoutGroup.getStyle())){
								moduleId = 40500L;
								itemGroup.setContentType(EntityType.SERVICE_ALLIANCE.getCode());
							}else{
								UrlActionData urlData = (UrlActionData)StringHelper.fromJsonString(padItems.get(0).getActionData(), UrlActionData.class);
								config.setBizUrl(urlData.getUrl());
								itemGroup.setContentType(EntityType.BIZ.getCode());
							}

							if(null != moduleId){
								LaunchPadItem padItem = padItems.get(0);
								ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), padItem.getActionData(), padItem.getActionType(), padItem.getItemLabel(), newVersion);
								config.setModuleAppId(moduleApp.getId());
							}
						}

						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));

						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.TAB){
						TabInstanceConfig instanceConfig = (TabInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), TabInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						portalItemGroupProvider.createPortalItemGroup(itemGroup);

						syncItem(itemGroup.getNamespaceId(), location, itemGroup.getName(), itemGroup.getId(), newVersion);

					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NEWS){
						Long moduleId = 10800L;
						ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
						NewsInstanceConfig instanceConfig = (NewsInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NewsInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), serviceModule.getActionType(), itemGroup.getLabel(), newVersion);
						config.setModuleAppId(moduleApp.getId());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NEWS_FLASH){
						NewsFlashInstanceConfig instanceConfig = (NewsFlashInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NewsFlashInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						Long moduleId = 10800L;
						ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
						ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), serviceModule.getActionType(), itemGroup.getLabel(), newVersion);
						config.setModuleAppId(moduleApp.getId());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}
				}
			}

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(padLayout.getId());
			mapping.setPortalContentId(layout.getId());
			mapping.setContentType(EntityType.PORTAL_LAYOUT.getCode());
			mapping.setCreatorUid(user.getId());
			mapping.setVersionId(layout.getVersionId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
		}
		return layout;
	}

	private void syncItemCategory(Integer namespaceId, Long itemGroupId, String itemName, Long versionId){
		User user = UserContext.current().getUser();
		List<ItemServiceCategry> categories = launchPadProvider.listItemServiceCategries(namespaceId, null, itemName);
		for (ItemServiceCategry category: categories) {
			PortalItemCategory portalItemCategory = portalItemCategoryProvider.getPortalItemCategoryByName(namespaceId, itemGroupId, category.getName());
			if(null == portalItemCategory){
				portalItemCategory = ConvertHelper.convert(category, PortalItemCategory.class);
				portalItemCategory.setItemGroupId(itemGroupId);
				portalItemCategory.setDefaultOrder(category.getOrder());
				portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());
				portalItemCategory.setVersionId(versionId);

				// 添加item 分类
				portalItemCategoryProvider.createPortalItemCategory(portalItemCategory);
			}
			syncContentScope(user, namespaceId, EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), ScopeType.ALL.getCode(), 0L, category.getSceneType());

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(category.getId());
			mapping.setPortalContentId(portalItemCategory.getId());
			mapping.setContentType(EntityType.PORTAL_ITEM_CATEGORY.getCode());
			mapping.setCreatorUid(user.getId());
			mapping.setVersionId(portalItemCategory.getVersionId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
		}
	}


	private void syncItem(Integer namespaceId, String location, String itemGroupName, Long itemGroupId, PortalVersion newVersion){
		User user = UserContext.current().getUser();
		List<LaunchPadItem> padItems = launchPadProvider.listLaunchPadItemsByItemGroup(namespaceId, location, itemGroupName);
		for (LaunchPadItem padItem: padItems) {
			if(ScopeType.USER == ScopeType.fromCode(padItem.getScopeCode())){
				continue;
			}
			PortalItem item = portalItemProvider.getPortalItemByGroupNameAndName(namespaceId, location, itemGroupName, padItem.getItemName(), itemGroupId);
			if(null == item){
				item = ConvertHelper.convert(padItem, PortalItem.class);
				item.setItemGroupId(itemGroupId);
				item.setLabel(padItem.getItemLabel());
				item.setName(padItem.getItemName());
				item.setGroupName(padItem.getItemGroup());
				item.setStatus(PortalItemStatus.ACTIVE.getCode());
				item.setCreatorUid(user.getId());
				item.setOperatorUid(user.getId());
				item.setVersionId(newVersion.getId());
				PortalItemCategory category = portalItemCategoryProvider.getPortalItemCategoryByName(namespaceId, itemGroupId, padItem.getCategryName());
				if(null != category){
					item.setItemCategoryId(category.getId());
				}
				if(ActionType.NONE == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.NONE.getCode());
				}else if(ActionType.MORE_BUTTON == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.ALLORMORE.getCode());
					AllOrMoreActionData actionData = new AllOrMoreActionData();
					actionData.setType(AllOrMoreType.MORE.getCode());
					item.setActionData(StringHelper.toJsonString(actionData));
				}else if(ActionType.ALL_BUTTON == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.ALLORMORE.getCode());
					AllOrMoreActionData actionData = new AllOrMoreActionData();
					actionData.setType(AllOrMoreType.ALL.getCode());
					item.setActionData(StringHelper.toJsonString(actionData));
//				}else if(ActionType.OFFICIAL_URL == ActionType.fromCode(padItem.getActionType())){
//					item.setActionType(PortalItemActionType.ZUOLINURL.getCode());
//					if(!StringUtils.isEmpty(padItem.getActionData())){
//						UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(padItem.getActionData(), UrlActionData.class);
//						item.setActionData(StringHelper.toJsonString(actionData));
//					}
				}else if(ActionType.THIRDPART_URL == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.THIRDURL.getCode());
					if(!StringUtils.isEmpty(padItem.getActionData())){
						UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(padItem.getActionData(), UrlActionData.class);
						item.setActionData(StringHelper.toJsonString(actionData));
					}
				}else if(ActionType.NAVIGATION  == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.LAYOUT.getCode());
					if(!StringUtils.isEmpty(padItem.getActionData())){
						NavigationActionData data = (NavigationActionData)StringHelper.fromJsonString(padItem.getActionData(), NavigationActionData.class);
						PortalLayout layout = syncLayout(item.getNamespaceId(), data.getItemLocation(), data.getLayoutName(), newVersion);
						LayoutActionData actionData = new LayoutActionData();
						actionData.setLayoutId(layout.getId());
						item.setActionData(StringHelper.toJsonString(actionData));
					}
				}else{
					item.setActionType(PortalItemActionType.MODULEAPP.getCode());
					ModuleAppActionData actionData = new ModuleAppActionData();
					ServiceModuleApp moduleApp= syncServiceModuleApp(padItem.getNamespaceId(), padItem.getActionData(), padItem.getActionType(), padItem.getItemLabel(), newVersion);
					actionData.setModuleAppId(moduleApp.getId());
					item.setActionData(StringHelper.toJsonString(actionData));
				}

				//添加item到数据库
				portalItemProvider.createPortalItem(item);
			}

			syncContentScope(user, namespaceId, EntityType.PORTAL_ITEM.getCode(), item.getId(), padItem.getScopeCode(), padItem.getScopeId(), padItem.getSceneType());

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(padItem.getId());
			mapping.setPortalContentId(item.getId());
			mapping.setContentType(EntityType.PORTAL_ITEM.getCode());
			mapping.setCreatorUid(user.getId());
			mapping.setVersionId(item.getVersionId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);

		}
	}

	private ServiceModuleApp syncServiceModuleApp(Integer namespaceId, String actionData, Byte actionType, String itemLabel, PortalVersion newVersion){
		User user = UserContext.current().getUser();
		ServiceModuleApp moduleApp = new ServiceModuleApp();
		moduleApp.setInstanceConfig(actionData);
		moduleApp.setActionType(actionType);
		moduleApp.setName(itemLabel);
		moduleApp.setNamespaceId(namespaceId);
		moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
		moduleApp.setCreatorUid(user.getId());
		moduleApp.setOperatorUid(user.getId());
		moduleApp.setVersionId(newVersion.getId());

		ServiceModule serviceModule = null;

		//臆测lei.lv是想获取配置成13、44、60的item的模块
		if(ActionType.fromCode(actionType) == ActionType.OFFICIAL_URL || ActionType.ROUTER == ActionType.fromCode(actionType) || ActionType.OFFLINE_WEBAPP  == ActionType.fromCode(actionType)){
			Set<String> beans = PortalUrlParserBeanUtil.getkeys();
			Long moduleId = 0L;
			for (String bean : beans) {
				PortalUrlParser parser = PlatformContext.getComponent(bean);
				if(parser != null){
					try{
						moduleId = parser.getModuleId(namespaceId, actionData, actionType, itemLabel);
						if(moduleId != null && moduleId != 0L){
							serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
							if(serviceModule != null){
								break;
							}
						}
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}else if(ActionType.THIRDPART_URL  == ActionType.fromCode(actionType) ){
			//第三方链接没有模块
			return moduleApp;
		}else{
			List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModule(actionType);
			if(serviceModules.size() != 0){
				serviceModule = serviceModules.get(0);
			}
		}

		ServiceModuleApp existServiceModuleApp = null;

		//模块则根据模块id等配置查找已存在的应用，没有则根据actiontype和actionData查询
		if(serviceModule != null){
			moduleApp.setModuleId(serviceModule.getId());
			moduleApp.setModuleControlType(serviceModule.getModuleControlType());
			if(StringUtils.isEmpty(itemLabel)){
				moduleApp.setName(serviceModule.getName());
			}
			if(MultipleFlag.fromCode(serviceModule.getMultipleFlag()) == MultipleFlag.YES){
				PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
				if(null != handler){
					HandlerGetAppInstanceConfigCommand cmd = new HandlerGetAppInstanceConfigCommand();
					String instanceConfig = handler.getAppInstanceConfig(namespaceId, actionData, cmd);
					moduleApp.setInstanceConfig(instanceConfig);
				}
			}

			// 同步reflectionServiceModule表
			//this.serviceModuleService.getOrCreateReflectionServiceModuleApp(namespaceId, actionData, moduleApp.getInstanceConfig(), itemLabel, serviceModule);

			//查找设置多入口入口标识
			String customTag = null;
			String handlerPrefix = PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX;
			PortalPublishHandler handler = PlatformContext.getComponent(handlerPrefix + serviceModule.getId());
			if(null != handler){

				HandlerGetCustomTagCommand gtCustomTagCommand = new HandlerGetCustomTagCommand();
				gtCustomTagCommand.setAppId(moduleApp.getId());
				gtCustomTagCommand.setAppOriginId(moduleApp.getOriginId());

				customTag = handler.getCustomTag(namespaceId, serviceModule.getId(), moduleApp.getInstanceConfig(), gtCustomTagCommand);
				LOGGER.debug("get customTag from handler = {}, customTag = {}",handler,customTag);
			}
			moduleApp.setCustomTag(customTag);

			//查找已存在的模块应用
			existServiceModuleApp = serviceModuleAppProvider.findServiceModuleApp(namespaceId, newVersion.getId(), serviceModule.getId(), customTag);

		}else {
			//查找已存在的模块应用
			existServiceModuleApp = serviceModuleAppProvider.findServiceModuleApp(namespaceId, newVersion.getId(), moduleApp.getActionType(), moduleApp.getInstanceConfig());
		}

		//如果没有则创建，有则返回已存在的
		if(existServiceModuleApp == null){
			serviceModuleAppProvider.createServiceModuleApp(moduleApp);
		}else {
			moduleApp = existServiceModuleApp;
		}

		return moduleApp;
	}

//	private ServiceModuleApp getServiceModuleApp(Integer namespaceId, PortalVersion version, String actionData, String instanceConfig, ServiceModule serviceModule){
//
//
//	}
//


	private PortalContentScope syncContentScope(User user, Integer namespaceId, String contentType, Long contentId, Byte scopeType, Long scopeId, String sceneType){
		PortalContentScope scope = new PortalContentScope();
		scope.setContentType(contentType);
		scope.setContentId(contentId);
		scope.setNamespaceId(namespaceId);
		scope.setCreatorUid(user.getId());
		scope.setOperatorUid(user.getId());
		if(ScopeType.ALL == ScopeType.fromCode(scopeType)){
			if(SceneType.PM_ADMIN == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.PM.getCode());
				scope.setScopeId(0L);
			}else if(SceneType.PARK_TOURIST == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.COMMERCIAL.getCode());
				scope.setScopeId(0L);
			}else if(SceneType.DEFAULT == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
				scope.setScopeId(0L);
			}else{
				LOGGER.debug("data error. sceneType = " + sceneType);
				return null;
			}

		}else if(ScopeType.COMMUNITY == ScopeType.fromCode(scopeType)){
			Community community = communityProvider.findCommunityById(scopeId);
			scope.setScopeType(PortalScopeType.COMMERCIAL.getCode());
			if(null != community){
				if(CommunityType.RESIDENTIAL == CommunityType.fromCode(community.getCommunityType())){
					scope.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
				}
			}
			scope.setScopeId(scopeId);
		}else if(ScopeType.ORGANIZATION == ScopeType.fromCode(scopeType)){
			Organization organization = organizationProvider.findOrganizationById(scopeId);
			scope.setScopeType(PortalScopeType.ORGANIZATION.getCode());
			if(null != organization){
				if(OrganizationType.PM == OrganizationType.fromCode(organization.getOrganizationType())){
					scope.setScopeType(PortalScopeType.PM.getCode());
				}
			}
			scope.setScopeId(scopeId);
		}else if(ScopeType.RESIDENTIAL == ScopeType.fromCode(scopeType)){
			scope.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
			scope.setScopeId(scopeId);

		}else if(ScopeType.PM == ScopeType.fromCode(scopeType)){
			scope.setScopeType(PortalScopeType.PM.getCode());
			scope.setScopeId(scopeId);

		}else{
			LOGGER.debug("data error. scopeType = " + scopeType);
			return null;
		}

		//添加item的scope到数据库
		portalContentScopeProvider.createPortalContentScope(scope);
		return scope;
	}

	@Override
	public PortalPublishHandler getPortalPublishHandler(Long moduleId) {
		PortalPublishHandler handler = null;

		if(moduleId != null && moduleId.longValue() > 0) {
			String handlerPrefix = PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX;
			try {
				handler = PlatformContextNoWarnning.getComponent(handlerPrefix + moduleId);
			}catch (Exception ex){
				//LOGGER.info("PortalPublishHandler not exist moduleId = {}", moduleId);
			}

		}

		return handler;
	}

	@Override
	public ListPortalVersionResponse listPortalVersions(ListPortalVersionCommand cmd){
		List<PortalVersion> list = portalVersionProvider.listPortalVersion(cmd.getNamespaceId(), cmd.getStatus());

		List<PortalVersionDTO> dtos = list.stream().map(r -> ConvertHelper.convert(r, PortalVersionDTO.class)).collect(Collectors.toList());

		ListPortalVersionResponse response = new ListPortalVersionResponse();
		response.setDtos(dtos);
		return response;
	}

	@Override
	public void updatePortalVersionUsers(UpdatePortalVersionUsersCommand cmd) {
		updatePortalVersionUsers(cmd.getNamespaceId(), cmd.getVersionId(), cmd.getUserIds());
	}


	private void updatePortalVersionUsers(Integer namespaceId, Long versionId, List<Long> userIds) {
		portalVersionUserProvider.deletePortalVersionUsers(namespaceId);
		if(userIds != null){
			for (Long userId: userIds){
				PortalVersionUser portalVersionUser  = new PortalVersionUser();
				portalVersionUser.setNamespaceId(namespaceId);
				portalVersionUser.setUserId(userId);
				portalVersionUser.setVersionId(versionId);
				portalVersionUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
				portalVersionUserProvider.createPortalVersionUser(portalVersionUser);
			}
		}
	}


	@Override
	public ListPortalVersionUsersResponse listPortalVersionUsers(ListPortalVersionUsersCommand cmd) {
		List<PortalVersionUser> portalVersionUsers = portalVersionUserProvider.listPortalVersionUsers(cmd.getNamespaceId(), cmd.getVersionId());
		ListPortalVersionUsersResponse response = new ListPortalVersionUsersResponse();
		List<PortalVersionUserDTO> dtos = new ArrayList<>();
		if(portalVersionUsers != null){
			for (PortalVersionUser versionUser: portalVersionUsers){
				PortalVersionUserDTO dto = ConvertHelper.convert(versionUser, PortalVersionUserDTO.class);
				User user = userProvider.findUserById(dto.getUserId());
				if(user != null){
					dto.setNickName(user.getNickName());
				}

				UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(dto.getUserId(), IdentifierType.MOBILE.getCode());
				if(identifier != null){
					dto.setPhone(identifier.getIdentifierToken());
				}
				dtos.add(dto);
			}
			response.setDtos(dtos);
		}

		return response;
	}

	@Override
	public void revertVersion(RevertVersionCommand cmd){

		PortalVersion portalversin = portalVersionProvider.findPortalVersionById(cmd.getId());

		//大本版不能回滚
		if(portalversin.getMinorVersion().intValue() != 0){
			deleteVersion(cmd.getId());
			copyPortalToNewMinorVersion(portalversin.getNamespaceId(), portalversin.getParentId());
		}

	}
//
//
	public void deleteVersion(Long versionId){
//		delete from `eh_portal_content_scopes`;
//		delete from `eh_portal_item_categories`;
//		delete from `eh_portal_item_groups`;
//		delete from `eh_portal_items`;
//		delete from `eh_portal_launch_pad_mappings`;
//		delete from `eh_portal_layouts`;
//		delete from `eh_service_module_apps`;
//		delete from `eh_portal_versions`;
//		delete from `eh_portal_version_users`;

		serviceModuleAppProvider.deleteByVersionId(versionId);
		portalLayoutProvider.deleteByVersionId(versionId);
		portalItemGroupProvider.deleteByVersionId(versionId);
		portalItemCategoryProvider.deleteByVersionId(versionId);
		portalItemProvider.deleteByVersionId(versionId);
		portalContentScopeProvider.deleteByVersionId(versionId);
		portalLaunchPadMappingProvider.deleteByVersionId(versionId);
		portalVersionProvider.deleteById(versionId);
		portalVersionUserProvider.deleteByVersionId(versionId);

	}

	@Override
	public void initAppEntryProfileData() {
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                List<NamespaceInfoDTO> namespaceInfoDTOS = namespacesService.listNamespace();
                for (NamespaceInfoDTO namespaceInfoDTO : namespaceInfoDTOS) {
                    PortalVersion portalVersion = portalVersionProvider.findReleaseVersion(namespaceInfoDTO.getId());
                    List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceInfoDTO.getId(),portalVersion.getId(),
                            null,null,null, ServiceModuleAppType.COMMUNITY.getCode(),null,null,null,null);
                    if (!CollectionUtils.isEmpty(serviceModuleApps)) {
                        for (ServiceModuleApp serviceModuleApp : serviceModuleApps) {
                            List<ServiceModuleEntry> serviceModuleEntries = serviceModuleEntryProvider.listServiceModuleEntries(serviceModuleApp.getModuleId(),
                                    null,null,null,null);
                            if (!CollectionUtils.isEmpty(serviceModuleEntries)) {
                                for (ServiceModuleEntry serviceModuleEntry : serviceModuleEntries) {
                                    ServiceModuleAppEntryProfile serviceModuleAppEntryProfile = new ServiceModuleAppEntryProfile();
                                    serviceModuleAppEntryProfile.setOriginId(serviceModuleApp.getOriginId());
                                    serviceModuleAppEntryProfile.setEntryId(serviceModuleEntry.getId());
                                    serviceModuleAppEntryProfile.setAppEntrySetting(TrueOrFalseFlag.TRUE.getCode());
                                    serviceModuleAppEntryProfile.setEntryName(serviceModuleApp.getName());
                                    serviceModuleAppEntryProfile.setEntryUri(serviceModuleApp.getIconUri());
                                    serviceModuleAppProvider.createServiceModuleAppEntryProfile(serviceModuleAppEntryProfile);
                                }
                            }
                        }
                    }
                    LOGGER.info("NamespaceId = {}",namespaceInfoDTO.getId());
                }
            LOGGER.info("initAppEntryProfileData success!");
            }
        });

	}
}