// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
