package com.everhomes.xfyun;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleLocationType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@Component
public class XunfeiYunServiceImpl implements XunfeiYunService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XunfeiYunServiceImpl.class);
	
	@Autowired
	ServiceModuleAppProvider serviceModuleAppProvider;
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
	OrganizationProvider organizationProvider;
	@Autowired
	PortalVersionProvider portalVersionProvider;
	@Override
	public QueryRoutersResponse queryRouters(QueryRoutersCommand cmd) {

		// 业务跳转
		Long moduleId = getHandlerPrefixByQuery(cmd);
		if (null != moduleId) {
			QueryRoutersResponse resp = new QueryRoutersResponse();
			List<AppDTO> appDtos = getTargetApps(UserContext.getCurrentNamespaceId(), cmd.getContext().getCommunityId(),
					moduleId);
			resp.setRouterDtos(appDtos);
			return resp;
		}

		return queryCommonRouters(cmd);
	}
	
	private QueryRoutersResponse queryCommonRouters(QueryRoutersCommand cmd) {

		List<AppDTO> routerDtos = new ArrayList<>();
		AppDTO appDto = new AppDTO();
		if ("我的工单".equals(cmd.getRouteTextInfo())) {
			appDto.setRouter("zl://workflow/tasks");
			routerDtos.add(appDto);
		} else if ("悦邻优选".equals(cmd.getRouteTextInfo())) {
			appDto.setRouter("");
			routerDtos.add(appDto);
		}

		QueryRoutersResponse resp = new QueryRoutersResponse();
		resp.setRouterDtos(routerDtos);
		return resp;
	}

	private Long getHandlerPrefixByQuery(QueryRoutersCommand cmd) {

		if ("物业报修".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.PM_TASK_MODULE;
		}

		if ("服务热线".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.HOTLINE_MODULE;
		}

		if ("访客预约".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.ENTERPRISE_VISITOR_MODULE;
		}

		if ("停车缴费".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.PARKING_MODULE;
		}

		return null;
	}
	
	private List<AppDTO> getTargetApps(Integer namespaceId, Long communityId, Long moduleId) {
		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
		if (null == organizationProperty) {
			return new ArrayList<>(1);
		}

		Long orgId = organizationProperty.getOrganizationId();
		Byte sceneType = ServiceModuleSceneType.CLIENT.getCode();
		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleApp(namespaceId, moduleId, null,
				null, null);
		return serviceModuleAppService.toAppDtos(communityId, orgId, sceneType, apps);
	}
	
}
